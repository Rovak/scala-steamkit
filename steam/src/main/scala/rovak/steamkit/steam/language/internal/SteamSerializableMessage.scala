package rovak.steamkit.steam.language.internal

import rovak.steamkit.steam.language.EMsg

trait SteamSerializableMessage extends SteamSerializable {
  var msg: EMsg
}
