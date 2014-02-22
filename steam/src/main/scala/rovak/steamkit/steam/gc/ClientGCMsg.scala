package rovak.steamkit.steam.gc

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Arrays
import rovak.steamkit.steam.language.internal.IGCSerializableMessage
import rovak.steamkit.steam.language.internal.MsgGCHdr
import rovak.steamkit.util.stream.BinaryReader
import rovak.steamkit.util.stream.BinaryWriter
//remove if not needed

class ClientGCMsg[T <: IGCSerializableMessage](clazz: Class[T], payloadReserve: Int) extends GCMsgBase[MsgGCHdr](classOf[MsgGCHdr], payloadReserve) {

  var body = clazz.newInstance()

  def this(clazz: Class[T]) {
    this(clazz, 64)
  }

  def this(clazz: Class[T], msg: GCMsgBase[MsgGCHdr], payloadReserve: Int) {
    this(clazz, payloadReserve)
    header.targetJobID = msg.header.sourceJobID
  }

  def this(clazz: Class[T], msg: GCMsgBase[MsgGCHdr]) {
    this(clazz, msg, 64)
  }

  def this(clazz: Class[T], msg: IPacketGCMsg) {
    this(clazz)
    try {
      deserialize(msg.data)
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  override def serialize(): Array[Byte] = {
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
    reader = new BinaryReader(new ByteArrayInputStream(Arrays.copyOfRange(data, payloadOffset, payloadOffset + payloadLen)))
  }
}
