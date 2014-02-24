package org.rovak.steamclient.steam3

import steam.SteamId

object Commands {

  case class SendChatMessage(steamId: SteamId, message: String)

}
