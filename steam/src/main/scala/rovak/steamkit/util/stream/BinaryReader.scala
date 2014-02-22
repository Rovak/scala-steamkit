package rovak.steamkit.util.stream

import com.google.protobuf.{InvalidProtocolBufferException, CodedInputStream}
import java.io.InputStream
import java.nio.ByteBuffer

object BinaryReader {
  val LongMaxValue = 0xFFFFFFFFFFFFFFFFL
}

/**
 * Binary Reader
 *
 * @param reader inputstream
 * @param len length
 */
class BinaryReader(reader: CodedInputStream, len: Int = 0) {

  def this(stream: InputStream) = {
    this(CodedInputStream.newInstance(stream))
  }

  def this(data: Array[Byte]) = {
    this(CodedInputStream.newInstance(data), data.length)
    reader.pushLimit(data.length)
  }

  /**
   * @param size buffer size
   */
  def buffer(size: Int) = {
    val buffer = new Array[Byte](size)
    for (i <- 1 to size) buffer(size - i) = reader.readRawByte()
    ByteBuffer.wrap(buffer)
  }

  /**
   * @return Current position
   */
  def getPosition = reader.getTotalBytesRead

  /**
   * @return Remaining bytes
   */
  def getRemaining = len - getPosition

  def readLong() = buffer(8).getLong
  def readInt() = buffer(4).getInt
  def readShort() = buffer(2).getShort
  def readByte() = reader.readRawByte
  def readBytes(len: Int): Array[Byte] = reader.readRawBytes(len)
  def isAtEnd = reader.isAtEnd
  def readBytes(): Array[Byte] = readBytes(getRemaining)
  def readFloat() = buffer(4).getFloat
  def readString() = reader.readString
  def stream = reader

}