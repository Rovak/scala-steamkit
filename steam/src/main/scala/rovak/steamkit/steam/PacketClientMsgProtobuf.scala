package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.language.EMsg
import _root_.rovak.steamkit.steam.language.internal.ExtendedClientMsgHdr
import _root_.rovak.steamkit.util.stream.BinaryReader

class PacketClientMsgProtobuf(msg: EMsg, val data: Array[Byte]) extends IPacketMsg {

  def isProto = true
  def msgType = msg

  private val extendedHdr = {
    val is = new BinaryReader(data)
    val msg = new ExtendedClientMsgHdr()
    msg.deserialize(is)
    msg
  }

  def targetJobId = extendedHdr.targetJobID
  def sourceJobId = extendedHdr.sourceJobID

}
