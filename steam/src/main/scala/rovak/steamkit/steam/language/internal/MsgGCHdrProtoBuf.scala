package rovak.steamkit.steam.language.internal

import steam.generated.SteammessagesBase
import rovak.steamkit.util.stream.{BinaryReader, BinaryWriter}
import rovak.steamkit.util.MsgUtil

class MsgGCHdrProtoBuf extends GCSerializableHeader {

  var msg = 0
  var headerLength: Int = 0
  var proto = SteammessagesBase.CMsgProtoBufHeader.newBuilder

  def serialize(stream: BinaryWriter) {
    val msProto: Array[Byte] = proto.build.toByteArray
    headerLength = msProto.length
    stream.write(MsgUtil.makeGCMsg(msg, protobuf = true))
    stream.write(headerLength)
    stream.write(msProto)
  }

  def deserialize(stream: BinaryReader) {
    msg = MsgUtil.getGCMsg(stream.readInt())
    headerLength = stream.readInt()
    proto.mergeFrom(stream.readBytes(headerLength))
  }
}

