package rovak.steamkit.steam.gc

import rovak.steamkit.types.JobID

trait IClientGCMsg {

  /**
   * True if this instance is protobuf backed; otherwise, false.
   */
  def isProto: Boolean

  /**
   * The message type.
   */
  var msgType: Int = 0

  /**
   * The target job id.
   */
  var targetJobId = JobID.invalid
  var sourceJobId = JobID.invalid

  /**
   * serializes this client message instance to a byte array.
   * @return Data representing a client message.
   */
  def serialize: Array[Byte]

  /**
   * Initializes this client message by deserializing the specified data.
   * @param data The data representing a client message.
   */
  def deserialize(data: Array[Byte])

}
