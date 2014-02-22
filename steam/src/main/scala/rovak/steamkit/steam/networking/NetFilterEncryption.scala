package rovak.steamkit.steam.networking

import rovak.steamkit.util.CryptoHelper

class NetFilterEncryption(key: Array[Byte]) {

  def processIncoming(data: Array[Byte]) = CryptoHelper.SymmetricDecrypt(data, key)

  def processOutgoing(data: Array[Byte]) = CryptoHelper.SymmetricEncrypt(data, key)

}
