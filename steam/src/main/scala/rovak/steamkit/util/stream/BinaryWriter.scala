package rovak.steamkit.util.stream

import com.google.protobuf.CodedOutputStream
import java.io.{ByteArrayOutputStream, OutputStream}
import java.nio.ByteBuffer

object BinaryWriter {

  /**
   * Implicity convert to byte array
   *
   * @return
   */
  implicit def ToByteArray(bw: BinaryWriter) = bw.toByteArray
}

class BinaryWriter(os: OutputStream) {

  var writer = CodedOutputStream.newInstance(os)
  var stream: Option[ByteArrayOutputStream] = None

  def this(stream: ByteArrayOutputStream) = {
    this(stream.asInstanceOf[OutputStream])
    this.stream = Option(stream)
  }

  def this(size: Int) = this(new ByteArrayOutputStream(size))

  def this() = this(32)

  def write(data: Short) = {
    val buffer = ByteBuffer.allocate(2)
    buffer.putShort(data)
    writeR(buffer)
  }

  def writeR(buffer: ByteBuffer): Unit = {
    for (i <- buffer.capacity() - 1 to 0 by -1) {
      write(buffer.get(i))
    }
  }

  def write(data: Array[Byte]): Unit = {
    writer.writeRawBytes(data)
    writer.flush()
  }

  def write(data: Int): Unit = {
    val buffer = ByteBuffer.allocate(4)
    buffer.putInt(data)
    writeR(buffer)
  }

  def write(data: Long): Unit = {
    val buffer = ByteBuffer.allocate(8)
    buffer.putLong(data)
    writeR(buffer)
  }

  def write(data: Byte): Unit = {
    writer.writeRawByte(data)
    writer.flush()
  }

  /**
   * Flush stream
   */
  def flush() = os.flush()

  def toByteArray: Array[Byte] = stream.map(_.toByteArray).getOrElse(null)

}
