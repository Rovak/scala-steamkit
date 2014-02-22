package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.language.EMsg
import _root_.rovak.steamkit.steam.language.internal.MsgHdr
import _root_.rovak.steamkit.util.stream.BinaryReader

/**
 * Represents a packet message with basic header information.
 */
class PacketMsg(msg: EMsg, val data: Array[Byte]) extends IPacketMsg {
  
  private lazy val msgHdr = {
    val hdr = new MsgHdr()
    hdr.deserialize(new BinaryReader(data))
    hdr
  }

  /**
   * Gets the source job id for this packet message.
   * @return The source job id.
   */
  def sourceJobId = msgHdr.sourceJobID
  
  /**
   * Gets the target job id for this packet message.
   * @return The target job id.
   */
  def targetJobId = msgHdr.targetJobID

  /**
   * Gets the network message type of this packet message.
   * @return The message type.
   */
  def msgType = msg

  /**
   * Gets a value indicating whether this packet message is protobuf backed.
   * This type of message is never protobuf backed.
   */
  def isProto = false
}
