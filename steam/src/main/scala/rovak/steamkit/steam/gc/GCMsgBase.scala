package rovak.steamkit.steam.gc

import java.io.IOException
import lombok.Getter
import rovak.steamkit.steam.AMsgBase
import rovak.steamkit.steam.language.internal.IGCSerializableHeader
import rovak.steamkit.types.JobID

/**
 * This is the abstract base class for all available game coordinator messages.
 * It's used to maintain packet payloads and provide a header for all gc messages.
 * @param <T>	The header type for this gc message.
 */
abstract class GCMsgBase[T](clazz: Class[T], payloadReserve: Int) extends AMsgBase(payloadReserve) with IClientGCMsg {

  var header = clazz.newInstance

  def isProto = false

  /**
   * serializes this client message instance to a byte array.
   * @return Data representing a client message.
   */
  def serialize(): Array[Byte]

  /**
   * Initializes this client message by deserializing the specified data.
   * @param data	The data representing a client message.
   */
  def deserialize(data: Array[Byte]): Unit
}

