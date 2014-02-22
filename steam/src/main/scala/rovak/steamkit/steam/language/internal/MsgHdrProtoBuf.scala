package rovak.steamkit.steam.language.internal

import steam.generated.SteammessagesBase.CMsgProtoBufHeader
import rovak.steamkit.steam.language.EMsg
import rovak.steamkit.util.stream.BinaryReader
import rovak.steamkit.util.stream.BinaryWriter
import rovak.steamkit.util.MsgUtil

class MsgHdrProtoBuf extends SteamSerializableHeader {

  var msg = EMsg.Invalid
  var headerLength = 0
  var proto = CMsgProtoBufHeader.newBuilder()

  def serialize(stream: BinaryWriter) {
    val pArr = proto.build().toByteArray
    headerLength = pArr.length
    stream.write(MsgUtil.makeMsg(msg.v(), protobuf = true))
    stream.write(headerLength)
    stream.write(pArr)
  }

  def deserialize(stream: BinaryReader) {
    msg = MsgUtil.getMsg(stream.readInt())
    headerLength = stream.readInt()
    proto = CMsgProtoBufHeader.newBuilder()
    val header = stream.readBytes(headerLength)
    proto.mergeFrom(header)
  }
}
