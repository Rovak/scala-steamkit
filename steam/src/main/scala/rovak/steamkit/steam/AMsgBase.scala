package rovak.steamkit.steam

import _root_.rovak.steamkit.util.stream.{BinaryReader, BinaryWriter}
import java.nio.charset.Charset

/**
 * This class provides a payload backing to client messages.
 */
abstract class AMsgBase(payloadReserve: Int = 0) {

  val outputStream = new BinaryWriter(payloadReserve)

  protected var reader: BinaryReader = null

  /**
   * Writes a single 32bit integer to the message payload.
   * @param data	The integer.
   */
  def write(data: Int) = outputStream.write(data)

  /**
   * Writes a single 64bit long to the message payload.
   * @param data	The long.
   */
  def write(data: Long) = outputStream.write(data)

  /**
   * Writes the specified byte array to the message payload.
   * @param data	The byte array.
   */
  def write(data: Array[Byte]) = outputStream.write(data)

  /**
   * Writes the specified string to the message payload using the specified encoding.
   * This function does not write a terminating null character.
   *
   * @param data		The string to write.
   * @param encoding	The encoding to use
   */
  def write(data: String, encoding: Charset) = {
    if (data != null) outputStream.write(data.getBytes(encoding))
  }

  /**
   * Writes the specified string and a null terminator to the message payload using the specified encoding.
   *
   * @param data		The string to write.
   * @param encoding	The encoding to use.
   */
  def writeNullTermString(data: String, encoding: Charset) {
    write(data, encoding)
    write("\0", encoding)
  }
}

