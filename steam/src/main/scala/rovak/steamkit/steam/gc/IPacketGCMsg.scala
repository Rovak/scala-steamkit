package rovak.steamkit.steam.gc

import rovak.steamkit.steam.language.internal.MsgHdr
import rovak.steamkit.util.stream.BinaryReader
import rovak.steamkit.steam.language.EMsg

trait IPacketGCMsg {

  /**
   * Gets a value indicating whether this packet message is protobuf backed.
   * @return true if this instance is protobuf backed; otherwise, false
   */
  def isProto: Boolean

  /**
   * Gets the network message type of this packet message.
   * @return The message type.
   */
  def msgType: EMsg

  /**
   * Gets the target job id for this packet message.
   * @return The target job id.
   */
  def targetJobId: Long

  /**
   * Gets the source job id for this packet message.
   * @return The source job id.
   */
  def sourceJobId: Long

  /**
   * Gets the underlying data that represents this client message.
   * @return The data.
   */
  val data: Array[Byte]
}
