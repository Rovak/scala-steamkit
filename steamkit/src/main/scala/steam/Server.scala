package steam

import java.net.InetSocketAddress

case class Server(ip: String, port: Int) {
  def ToInetAdresss = new InetSocketAddress(ip, port)
}
