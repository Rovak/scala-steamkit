package rovak.steamkit.steam

import rovak.steamkit.steam.language.EMsg
import rovak.steamkit.types.JobID
import rovak.steamkit.types.steamid.SteamID

trait IClientMsg {

  def isProto: Boolean
  var msgType: EMsg
  var sessionId: Int
  var steamId: SteamID
  var targetJobId: JobID
  var sourceJobId: JobID


  def serialize(): Array[Byte]
  def deserialize(data: Array[Byte]): Unit
}
