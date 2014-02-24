package org.rovak.steamclient.steam3

import steam.SteamId
import rovak.steamkit.steam.language.EPersonaState

object Commands {

  /**
   * Sends a chat message
   *
   * @param steamId SteamId to send to
   * @param message Message to send
   */
  case class SendChatMessage(steamId: SteamId, message: String)

  /**
   * Changes the status of the bot
   *
   * @param name Name
   * @param status Status
   */
  case class Status(name: String, status: EPersonaState)
}
