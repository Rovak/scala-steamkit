package rovak.steamkit.steam

import rovak.steamkit.steam.language.EMsg
import rovak.steamkit.steam.language.internal.ExtendedClientMsgHdr
import rovak.steamkit.util.stream.BinaryReader
//remove if not needed

class PacketClientMsg(val msgType: EMsg, val data: Array[Byte])
  extends IPacketMsg {

  def isProto = false

  private val extendedHdr = {
    val is = new BinaryReader(data)
    val msg = new ExtendedClientMsgHdr()
    msg.deserialize(is)
    msg
  }

  var targetJobId = extendedHdr.targetJobID
  var sourceJobId = extendedHdr.sourceJobID

}
