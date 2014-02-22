package steam

import rovak.steamkit.types.steamid.SteamID
import rovak.steamkit.steam.language.{EAccountType, EUniverse}

object SteamId {
  implicit def Long2SteamId(id: Long) = SteamId(id)
  implicit def SteamId2Long(steamId: SteamId) = steamId.id
  implicit def SteamId2SteamId(steamId: SteamID) = SteamId(steamId.getAccountID)
  implicit def SteamId2SteamId(steamId: SteamId) = new SteamID(steamId.id)
}

case class SteamId(
  id: Long = 0,
  accountInstance: Long = 0,
  universe: EUniverse = EUniverse.Invalid,
  accountType: EAccountType = EAccountType.AnonUser)