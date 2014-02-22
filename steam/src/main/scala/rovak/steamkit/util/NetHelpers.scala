package rovak.steamkit.util

import java.net.{UnknownHostException, InetAddress}
import java.nio.ByteBuffer

object NetHelpers {

  def getIPAddress(ipAddr: Long): InetAddress = {
    val buff = ByteBuffer.allocate(4)
    buff.putInt(ipAddr.toInt)
    try {
      return InetAddress.getByAddress(buff.array())
    } catch {
      case e: UnknownHostException => e.printStackTrace()
    }
    null
  }

  def getIPAddress(ipAddr: InetAddress): Long = {
    val buff = ByteBuffer.wrap(ipAddr.getAddress)
    buff.getInt & 0xFFFFFFFFL
  }
}
