package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.gc.IPacketGCMsg
import _root_.rovak.steamkit.steam.language.EMsg
import _root_.rovak.steamkit.steam.language.internal.{ExtendedClientMsgHdr, MsgGCHdr}
import _root_.rovak.steamkit.types.JobID
import _root_.rovak.steamkit.util.stream.BinaryReader

class PacketClientGCMsg(msg: EMsg, val data: Array[Byte]) extends IPacketGCMsg {

  private val extendedHdr = {
    val is = new BinaryReader(data)
    val msg = new MsgGCHdr()
    msg.deserialize(is)
    msg
  }

  def isProto = false

  def msgType = msg

  /**
   * Gets the source job id for this packet message.
   * @return The source job id.
   */
  override def sourceJobId = extendedHdr.sourceJobID

  /**
   * Gets the target job id for this packet message.
   * @return The target job id.
   */
  override def targetJobId = extendedHdr.targetJobID
}
