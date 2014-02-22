package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.language.EMsg
import _root_.rovak.steamkit.steam.language.internal.MsgHdrProtoBuf
import _root_.rovak.steamkit.types.JobID
import _root_.rovak.steamkit.types.steamid.SteamID
import _root_.rovak.steamkit.util.stream.{BinaryReader, BinaryWriter}
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.Arrays
import com.google.protobuf.AbstractMessage
import com.google.protobuf.GeneratedMessage

object ClientMsgProtobuf {
  def create[U <: GeneratedMessage.Builder[U]](builder: U) = {
    //new ClientMsgProtobuf[builder.]()
  }
}

class ClientMsgProtobuf[U <: GeneratedMessage.Builder[U]](private var clazz: Class[_ <: AbstractMessage], eMsg: EMsg, payloadReserve: Int)
  extends MsgBase[MsgHdrProtoBuf](classOf[MsgHdrProtoBuf], payloadReserve) {

  def isProto = true

  var msgType = header.msg

  var sessionId = 0
  var steamId: SteamID = null
  var targetJobId: JobID = null
  var sourceJobId: JobID = null
  def protoHeader = header.proto

  val m = clazz.getMethod("newBuilder")
  var body = m.invoke(null).asInstanceOf[U]
  header.msg = eMsg

  def this(clazz: Class[_ <: AbstractMessage], eMsg: EMsg) {
    this(clazz, eMsg, 64)
  }

  def this(clazz: Class[_ <: AbstractMessage],
           eMsg: EMsg,
           msg: MsgBase[MsgHdrProtoBuf],
           payloadReserve: Int) {
    this(clazz, eMsg, payloadReserve)
    header.proto.setJobidTarget(msg.header.proto.getJobidSource)
  }

  def this(clazz: Class[_ <: AbstractMessage], msg: IPacketMsg) {
    this(clazz, msg.msgType)
    deserialize(msg.data)
  }

  def this(clazz: Class[_ <: AbstractMessage], eMsg: EMsg, msg: MsgBase[MsgHdrProtoBuf]) {
    this(clazz, eMsg, msg, 64)
  }

  override def serialize(): Array[Byte] = {
    val ms = new BinaryWriter()
    header.serialize(ms)
    ms.write(body.build().toByteArray)
    ms.write(outputStream.toByteArray)
    ms.toByteArray
  }

  def deserialize(data: Array[Byte]) {
    val is = new BinaryReader(data)
    header.deserialize(is)
    val m = clazz.getMethod("newBuilder")
    body = m.invoke(null).asInstanceOf[U]
    body.mergeFrom(is.stream)
    val payloadOffset = is.getPosition
    val payloadLen = is.getRemaining
    reader = new BinaryReader(new ByteArrayInputStream(Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen)))
  }
}
