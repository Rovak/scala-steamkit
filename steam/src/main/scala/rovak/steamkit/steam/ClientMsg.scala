package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.language.EMsg
import _root_.rovak.steamkit.steam.language.internal.{ExtendedClientMsgHdr, ISteamSerializableMessage}
import _root_.rovak.steamkit.types.JobID
import _root_.rovak.steamkit.types.steamid.SteamID
import _root_.rovak.steamkit.util.stream.{BinaryWriter, BinaryReader}
import scala.collection.JavaConversions._
import java.io.ByteArrayOutputStream
import java.util

class ClientMsg[T <: ISteamSerializableMessage](clazz: Class[T], payloadReserve: Int)
  extends MsgBase[ExtendedClientMsgHdr](classOf[ExtendedClientMsgHdr], payloadReserve) {

  def isProto = false

  var data = null

  var msgType = header.msg
  var sessionId = header.sessionID
  var steamId = new SteamID(header.steamID)
  var targetJobId = new JobID(header.targetJobID)
  var sourceJobId = new JobID(header.sourceJobID)

  var body: T = clazz.newInstance()
  header.msg = body.getEMsg

  def this(clazz: Class[T]) {
    this(clazz, 64)
  }

  def this(clazz: Class[T], msg: MsgBase[ExtendedClientMsgHdr], payloadReserve: Int) {
    this(clazz, payloadReserve)
    header.targetJobID = msg.header.sourceJobID
  }

  def this(msg: IPacketMsg, clazz: Class[T]) {
    this(clazz)
    deserialize(msg.data)
  }

  def this(clazz: Class[T], msg: MsgBase[ExtendedClientMsgHdr]) {
    this(clazz, msg, 64)
  }

  def serialize() = {
    val ms = new BinaryWriter(new ByteArrayOutputStream())
    header.serialize(ms)
    body.serialize(ms)
    ms.write(outputStream.toByteArray)
    ms.toByteArray
  }

  def deserialize(data: Array[Byte]): Unit = {
    val cs = new BinaryReader(data)
    header.deserialize(cs)
    body.deSerialize(cs)
    val payloadOffset = cs.getPosition
    val payloadLen = cs.getRemaining
    reader = new BinaryReader(util.Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen))
  }
}
