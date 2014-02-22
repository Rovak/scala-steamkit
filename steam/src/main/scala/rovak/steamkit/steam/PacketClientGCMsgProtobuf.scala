package rovak.steamkit.steam

import rovak.steamkit.steam.language.EMsg
import rovak.steamkit.steam.language.internal.{MsgGCHdrProtoBuf, ExtendedClientMsgHdr}
import rovak.steamkit.util.stream.BinaryReader

class PacketClientGCMsgProtobuf(msg: EMsg, val data: Array[Byte]) extends IPacketMsg {

  def isProto = true
  def msgType = msg

  private val extendedHdr = {
    val is = new BinaryReader(data)
    val msg = new MsgGCHdrProtoBuf()
    msg.deserialize(is)
    msg
  }

  def targetJobId = extendedHdr.proto.getJobidTarget
  def sourceJobId = extendedHdr.proto.getJobidSource

}
