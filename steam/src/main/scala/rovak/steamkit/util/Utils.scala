package rovak.steamkit.util

import rovak.steamkit.steam.language.EOSType
import java.net.{SocketException, NetworkInterface}

object Utils {

  def getOSType = System.getProperty("os.name") match {
    case "Windows 7" => EOSType.Win7
    case "Windows 2003" => EOSType.Win2003
    case "Windows XP" => EOSType.WinXP
    case "Windows 2000" => EOSType.Win200
    case "Windows NT" => EOSType.WinNT
    case "Windows ME" => EOSType.WinME
    case "Windows 98" => EOSType.Win98
    case "Windows 95" => EOSType.Win95
    case os if os.startsWith("Win") => EOSType.WinUnknown
    case os if os.startsWith("Mac") => EOSType.MacOSUnknown
    case os if os.indexOf("nix") >= 0 => EOSType.LinuxUnknown
    case _ => EOSType.Unknown
  }

  def generateMachineID(): Array[Byte] = {
    try {
      val interfaces = NetworkInterface.getNetworkInterfaces
      do {
        val n = interfaces.nextElement()
        if (n.getHardwareAddress != null && n.getHardwareAddress.length > 0) {
          return CryptoHelper.SHAHash(n.getHardwareAddress)

        }
      } while (NetworkInterface.getNetworkInterfaces.hasMoreElements)
    } catch {
      case e: SocketException => e.printStackTrace()
    }
    null
  }
}
