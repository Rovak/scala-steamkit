package rovak.steamkit.steam.language.internal

import rovak.steamkit.steam.language.EMsg

trait SteamSerializableHeader extends SteamSerializable {
  var msg: EMsg
}
