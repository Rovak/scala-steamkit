package org.rovak.steamclient.steam3

import akka.actor._
import akka.io.{IO, Tcp}
import akka.util.{Timeout, ByteStringBuilder, ByteString}
import rovak.steamkit.util.stream.{BinaryWriter, BinaryReader}
import rovak.steamkit.steam.language._
import rovak.steamkit.steam.language.internal.msg._
import rovak.steamkit.util.crypto.{RSACrypto, CryptoHelper}
import java.io._
import akka.io.Tcp.Event
import rovak.steamkit.steam.networking.NetFilterEncryption
import rovak.steamkit.types.steamid.SteamID
import java.net.InetAddress
import org.apache.commons.io.IOUtils
import scala.concurrent.duration._
import com.google.protobuf.InvalidProtocolBufferException
import steam.{Server, SteamId}
import rovak.steamkit.util.{ZipUtil, KeyDictionary, Utils, NetHelpers}
import rovak.steamkit.steam._
import com.typesafe.config.ConfigFactory
import rovak.steamkit.steam.msg.LogOnDetails
import rovak.steamkit.steam.msg.OneTimePasswordDetails
import akka.actor.Terminated
import rovak.steamkit.steam.msg.MachineAuthDetails
import org.rovak.steamclient.steam3.handlers.{MessageHandlerStack, FriendHandler, BotHandler}

object TcpConn {
  val magic = 0x31305456
}

object SteamClient {
  // Configuration
  lazy val config = ConfigFactory.load.getConfig("steamclient")

  // Messages
  case object HeartbeatTick
  case object Ack extends Event

  case class ConnectToServer(server: Server)
  case class SteamResponse(data: Array[Byte]) {
    lazy val reader = new BinaryReader(data)
  }

  case class SteamLogin(username: String, password: String, authToken: String = "")

  case class SteamData(data: Array[Byte])
}

class Buffer(actor: ActorRef) {

  import SteamClient.SteamData

  private val buffer = new ByteStringBuilder()
  private var stored = 0
  private var waitingFor = -1
  def append(data: ByteString) = {

    buffer.append(data)
    stored += data.length

    println(data.length)

    if (waitingFor == -1) {
      val reader = new BinaryReader(data.toArray)
      waitingFor = reader.readInt()
    }

    if (stored >= waitingFor) {
      actor ! SteamData(buffer.result().toArray)
      buffer.clear()
      stored = 0
      waitingFor = -1
    }
  }
}

class SteamClient extends MessageHandlerStack
  with BotHandler
  with FriendHandler {

  import Tcp._
  import context.dispatcher
  import SteamClient._
  import steam.generated.SteammessagesClientserver._
  import steam.generated.SteammessagesBase._

  implicit val timeout = Timeout(10 seconds)

  lazy val sentryFile = new File(SteamClient.config.getString("sentryfilelocation"), "sentry.bin")
  lazy val machineId = com.google.protobuf.ByteString.copyFrom(Utils.generateMachineID())

  var netFilter: Option[NetFilterEncryption] = None
  var tempSessionKey: Array[Byte] = null
  var heartBeatFunc: Option[Cancellable] = None
  var connection: ActorRef = null
  var sessionId: Option[Int] = None
  var steamId: Option[SteamID] = None
  var buffer = new Buffer(self)
  var connectedUniverse: EUniverse = null
  var protoVersion: Int = 1

  /**
   * Convert response to a valid packet message
   * @return
   */
  def getPacketMsg(response: SteamResponse): IPacketMsg = {
    val rawEmsg = response.reader.readInt()
    val emsg = EMsg.f(MsgUtils.ToMsg(rawEmsg))

    emsg match {
      case EMsg.ChannelEncryptRequest | EMsg.ChannelEncryptResult =>
        new PacketMsg(emsg, response.data)
      case x if MsgUtils.IsProtoBuf(rawEmsg) =>
          new PacketClientMsgProtobuf(emsg, response.data)
      case x =>
          new PacketClientMsg(emsg, response.data)
    }
  }

  /**
   * Connect the bot to the given server
   */
  def connectToServer(server: Server) = {
    import context.system
    IO(Tcp) ! Connect(server.ToInetAdresss)
  }

  /**
   * Logoff
   */
  def handleLoggedOff(msg: IPacketMsg) = {
    self ! PoisonPill
  }

  /**
   * Handles the Authentication request of the current machine
   */
  def handleMachineAuthUpdate(packetMsg: IPacketMsg) = {
    val machineAuth = new ClientMsgProtobuf[CMsgClientUpdateMachineAuth.Builder](classOf[CMsgClientUpdateMachineAuth], packetMsg)
    val msg = machineAuth.body.build()
    val data = msg.getBytes.toByteArray
    val sentryHash = CryptoHelper.SHAHash(data)

    IOUtils.write(data, new FileOutputStream(sentryFile))

    // inform the steam servers that we're accepting this sentry file
    sendMachineAuthResponse(MachineAuthDetails(
      jobId = machineAuth.sourceJobId,
      filename = msg.getFilename,
      bytesWritten = msg.getCubtowrite,
      filesize = data.length,
      offset = msg.getOffset,
      result = EResult.OK,
      lastError = 0,
      sentryFileHash = sentryHash,
      oneTimePassword = OneTimePasswordDetails(
        passwordType = msg.getOtpType,
        id = msg.getOtpIdentifier,
        sharedSecret = msg.getOtpSharedsecret.toByteArray,
        timeDrift = msg.getOtpTimedrift)
    ))
  }

  def wrappedHandleMessage(message: IPacketMsg) = message.msgType match {
    case EMsg.ChannelEncryptRequest =>
      handleEncryptRequest(message)
    case EMsg.ChannelEncryptResult =>
      handleEncryptResult(message)
      // TODO Login here
    case EMsg.ClientLogOnResponse =>
      handleLogOnResponse(message)
    case EMsg.ClientUpdateMachineAuth =>
      handleMachineAuthUpdate(message)
    case EMsg.Multi =>
      handleMulti(message)
    case EMsg.ClientNewLoginKey =>
      handleLoginKey(message)
    case EMsg.ClientLoggedOff =>
      handleLoggedOff(message)
    case x => println("unmatched message handling", x, message, message.isProto)
  }


  /**
   * Sends a machine auth response.
   */
  def sendMachineAuthResponse(details: MachineAuthDetails) {
    val response = new ClientMsgProtobuf[CMsgClientUpdateMachineAuthResponse.Builder](classOf[CMsgClientUpdateMachineAuthResponse], EMsg.ClientUpdateMachineAuthResponse)

    // so we respond to the correct message
    response.protoHeader.setJobidTarget(details.jobId)

    response.body.setCubwrote(details.bytesWritten)
    response.body.setEresult(details.result.v())

    response.body.setFilename(details.filename)
    response.body.setFilesize(details.filesize)

    response.body.setGetlasterror(details.lastError)
    response.body.setOffset(details.offset)

    response.body.setShaFile(com.google.protobuf.ByteString.copyFrom(details.sentryFileHash))

    response.body.setOtpIdentifier(details.oneTimePassword.id)
    response.body.setOtpType(details.oneTimePassword.passwordType)
    response.body.setOtpValue(details.oneTimePassword.value)

    send(response)
  }

  /**
   * Handle the given login key and return as accepted
   */
  def handleLoginKey(packetMsg: IPacketMsg) = {
    val loginKey = new ClientMsgProtobuf[CMsgClientNewLoginKey.Builder](classOf[CMsgClientNewLoginKey], packetMsg)
    val resp = new ClientMsgProtobuf[CMsgClientNewLoginKeyAccepted.Builder](classOf[CMsgClientNewLoginKeyAccepted], EMsg.ClientNewLoginKeyAccepted)
    resp.body.setUniqueId(loginKey.body.getUniqueId)
    send(resp)
  }

  /**
   * Handled when the user properly logged in
   */
  def handleLogOnResponse(packetMsg: IPacketMsg) {
    if (!packetMsg.isProto) {
      throw new Exception("Invalid message!")
      return
    }

    val logonResp = new ClientMsgProtobuf[CMsgClientLogonResponse.Builder](classOf[CMsgClientLogonResponse], packetMsg)

    EResult.f(logonResp.body.getEresult) match {
      case EResult.OK =>

        sessionId = Option(logonResp.protoHeader.getClientSessionid)
        steamId = Option(new SteamID(logonResp.protoHeader.getSteamid))

        val hbDelay: Int = logonResp.body.getOutOfGameHeartbeatSeconds

        heartBeatFunc = Option(context.system.scheduler.schedule(hbDelay seconds,
          hbDelay seconds,
          self,
          HeartbeatTick))
      case EResult.AccountLogonDenied =>
        println("Login failed, need key")
      case _ =>
        println("unhandled logonresponse")

    }
  }

  /**
   * Logs the client into the Steam3 network.
   * The client should already have been connected at this point.
   *
   * @param details	The details to use for logging on.
   */
  def logOn(details: LogOnDetails) {
    if (details.username.isEmpty || details.password.isEmpty) {
      throw new Exception("Logon requires a username and password to be set in 'details'.")
    }

    val logon = new ClientMsgProtobuf[CMsgClientLogon.Builder](classOf[CMsgClientLogon], EMsg.ClientLogon)
    val steamId = new SteamID(0, details.accountInstance, connectedUniverse, EAccountType.Individual)
    val localIp = NetHelpers.getIPAddress(InetAddress.getByName(InetAddress.getLocalHost.getHostAddress)).asInstanceOf[Int]
    logon.protoHeader.setClientSessionid(0)
    logon.protoHeader.setSteamid(steamId.convertToLong)
    logon.body.setObfustucatedPrivateIp(localIp ^ MsgClientLogon.ObfuscationMask)
    logon.body.setAccountName(details.username)
    logon.body.setPassword(details.password)
    logon.body.setProtocolVersion(MsgClientLogon.CurrentProtocol)
    logon.body.setClientOsType(Utils.getOSType.v)
    logon.body.setClientLanguage("english")
    logon.body.setSteam2TicketRequest(details.requestSteam2Ticket)
    logon.body.setClientPackageVersion(1771)
    logon.body.setMachineId(machineId)
    if (!details.authCode.isEmpty) {
      logon.body.setAuthCode(details.authCode)
    }
    details.sentryFileHash.map { filehash =>
      logon.body.setShaSentryfile(com.google.protobuf.ByteString.copyFrom(filehash))
      logon.body.setEresultSentryfile(EResult.OK.v())
    } getOrElse {
      logon.body.clearShaSentryfile()
      logon.body.setEresultSentryfile(EResult.FileNotFound.v())
    }

    send(logon)
  }

  /**
   * Handle a packet which contains multiple sub packets
   */
  def handleMulti(packetMsg: IPacketMsg) {
    if (!packetMsg.isProto) {
      throw new Exception(s"HandleMulti got non-proto MsgMulti!! ${packetMsg.msgType.toString}")
      return
    }

    val msgMulti = new ClientMsgProtobuf[CMsgMulti.Builder](classOf[CMsgMulti], packetMsg)
    var payload = msgMulti.body.getMessageBody.toByteArray

    if (msgMulti.body.getSizeUnzipped > 0) {
      try {
        payload = ZipUtil.deCompress(payload)
        readSubMessage(new BinaryReader(payload))
      }
      catch {
        case ex: IOException => println("HandleMulti encountered an exception when decompressing.", ex.toString)
      }
    }
  }

  /**
   * Reads a submessage from the reader
   */
  def readSubMessage(reader: BinaryReader): Unit = {
    if (!reader.isAtEnd) {
      val subSize = reader.readInt()
      val subData = reader.readBytes(subSize)
      internalHandleMessage(getPacketMsg(SteamResponse(subData)))
      readSubMessage(reader)
    }
  }

  /**
   * Logs the client into the Steam3 network as an anonymous user.
   * The client should already have been connected at this point.
   * Results are returned in a LoggedOnCallback
   */
  def logOnAnonymous() = {
    println("Logging in!")
    val logon = new ClientMsgProtobuf[CMsgClientLogon.Builder](classOf[CMsgClientLogon], EMsg.ClientLogon)
    val auId = SteamId(universe = connectedUniverse)
    logon.protoHeader.setClientSessionid(0)
    logon.protoHeader.setSteamid(auId)
    logon.body.setProtocolVersion(protoVersion)
    logon.body.setClientOsType(Utils.getOSType.v)
    logon.body.setMachineId(machineId)
    send(logon)
  }

  /**
   * Sends the specified client message to the server.
   * This method automatically assigns the correct SessionID and SteamID of the message.
   * @param msg	The client message to send.
   */
  def send(msg: IClientMsg) = {

    sessionId.map(x => msg.sessionId = x)
    steamId.map(x => msg.steamId = x)

    try {

      println(s"Sending message, session: ${msg.sessionId}, steamId: ${msg.steamId}, id: ${msg.msgType}")

      var data = msg.serialize()

      data = netFilter.map(_.processOutgoing(data)).getOrElse(data)

      val builder = new ByteStringBuilder
      val writer = new BinaryWriter(builder.asOutputStream)
      writer.write(data.length)
      writer.write(TcpConn.magic)
      writer.write(data)
      writer.flush()

      connection ! Write(builder.result(), Ack)
    }
    catch {
      case e: Exception => println(s"Error while sending message: ${e.getMessage} => ${e.getStackTraceString}")
    }
  }


  def handleEncryptRequest(packetMsg: IPacketMsg) {

    val encRequest = new Msg[MsgChannelEncryptRequest](packetMsg, classOf[MsgChannelEncryptRequest])
    connectedUniverse = encRequest.body.universe
    protoVersion = encRequest.body.protocolVersion
    tempSessionKey = CryptoHelper.GenerateRandomBlock(32)

    val pubKey = KeyDictionary.getPublicKey(connectedUniverse)
    val rsa = new RSACrypto(pubKey)
    val cryptedSessKey = rsa.encrypt(tempSessionKey)
    val keyCrc = CryptoHelper.CRCHash(cryptedSessKey)

    val encResp = new Msg[MsgChannelEncryptResponse](classOf[MsgChannelEncryptResponse])
    encResp.write(cryptedSessKey)
    encResp.write(keyCrc)
    encResp.write(0)

    send(encResp)
  }

  /**
   * Handle the result of an encryption
   */
  def handleEncryptResult(packetMsg: IPacketMsg) = {

    val encResult = new Msg[MsgChannelEncryptResult](packetMsg, classOf[MsgChannelEncryptResult])

    encResult.body.result match {
      case EResult.OK =>
        println("changing netfilter!")
        netFilter = Option(new NetFilterEncryption(tempSessionKey))
      case _ =>
          println("unhandled encrypt result")
    }
  }

  def processReceivedData(reader: BinaryReader) = {
    try {
      val packetLength = reader.readInt()
      val packetMagic = reader.readInt()
      if (packetMagic != TcpConn.magic) {
        throw new Exception("Invalid magic!")
      }
      println("reading bytes")
      val rawResultData = reader.readBytes(packetLength)
      val resultData = netFilter.map(_.processIncoming(rawResultData)).getOrElse(rawResultData)

      println("getting packet msg")
      val packetMsg = getPacketMsg(SteamResponse(resultData))

      println("handling internal message")
      internalHandleMessage(packetMsg)
    }
    catch {
      case e: InvalidProtocolBufferException =>
        println("proto error!" + e.getMessage + " => " + e.getStackTraceString)
      case e: Exception =>
        println("Error: " + e.getMessage + " => " + e.getStackTraceString)
    }
  }

  /**
   * Cleanup
   */
  override def postStop() {
    heartBeatFunc.map { func =>
      if (!func.isCancelled) func.cancel()
    }
    connection ! Close
  }

  /**
   * Login with steam details
   */
  def login(login: SteamLogin) = {

    var hash = Array[Byte]()
    if (sentryFile.exists) {
      val source = scala.io.Source.fromFile(sentryFile.getAbsoluteFile)
      val byteArray = source.map(_.toByte).toArray
      source.close()
      hash = CryptoHelper.SHAHash(byteArray)
    }

    logOn(LogOnDetails(
      username = login.username,
      password = login.password,
      authCode = login.authToken,
      accountInstance = SteamID.DesktopInstance,
      sentryFileHash = Option(hash)
    ))
  }

  /**
   * Send heartbeat
   */
  def beat() = send(new ClientMsgProtobuf[CMsgClientHeartBeat.Builder](classOf[CMsgClientHeartBeat], EMsg.ClientHeartBeat))

  def wrappedReceive = {
    case ConnectToServer(server) =>
      connectToServer(server)
    case CommandFailed(_: Connect) ⇒
      context stop self
    case c @ Connected(remote, local) =>
      connection = sender
      connection ! Register(self)
      context watch connection
    case HeartbeatTick =>
      beat()
    case data: ByteString =>
      connection ! Write(data)
    case CommandFailed(w: Write) =>
      println("failed") // O/S buffer was full
    case Terminated(tcp) =>
      self ! PoisonPill
    case PeerClosed =>
      println("peer closed!")
      self ! PoisonPill
    case Received(data) =>
      buffer.append(data)
    case SteamData(data) =>
      processReceivedData(new BinaryReader(data))
    case _: ConnectionClosed =>
      context stop self
  }
}
