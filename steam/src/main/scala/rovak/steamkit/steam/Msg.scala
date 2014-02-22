package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.language.internal.{SteamSerializableMessage, MsgHdr, ISteamSerializableMessage}
import _root_.rovak.steamkit.types.JobID
import _root_.rovak.steamkit.types.steamid.SteamID
import _root_.rovak.steamkit.util.stream.{BinaryReader, BinaryWriter}
import java.io.ByteArrayInputStream
import java.util.Arrays

class Msg[T <: SteamSerializableMessage](clazz: Class[T], payloadReserve: Int) extends MsgBase[MsgHdr](classOf[MsgHdr], payloadReserve) {

  def isProto = false
  var msgType = header.msg
  var sessionId = 0
  var steamId = new SteamID()
  var targetJobId = new JobID()
  var sourceJobId = new JobID()

  var body = clazz.newInstance()
  header.msg = body.msg

  def this(clazz: Class[T]) {
    this(clazz, 0)
  }

  def this(msg: MsgBase[MsgHdr], clazz: Class[T], payloadReserve: Int) {
    this(clazz, payloadReserve)
    header.targetJobID = msg.header.sourceJobID
  }

  def this(msg: IPacketMsg, clazz: Class[T]) {
    this(clazz)
    deserialize(msg.data)
  }

  def serialize(): Array[Byte] = {
    val ms = new BinaryWriter()
    header.serialize(ms)
    body.serialize(ms)
    ms.write(outputStream.toByteArray)
    ms.toByteArray
  }

  def deserialize(data: Array[Byte]): Unit =  {
    val ms = new BinaryReader(data)
    header.deserialize(ms)
    body.deserialize(ms)
    val payloadOffset = ms.getPosition
    val payloadLen = ms.getRemaining
    reader = new BinaryReader(new ByteArrayInputStream(Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen)))
  }
}
