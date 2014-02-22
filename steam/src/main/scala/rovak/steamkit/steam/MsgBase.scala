package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.language.internal.SteamSerializableHeader

abstract class MsgBase[T <: SteamSerializableHeader](cls: Class[T], payloadReserve: Int) extends AMsgBase(payloadReserve) with IClientMsg {

  var header = cls.newInstance()

}

