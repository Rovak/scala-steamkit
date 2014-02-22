package rovak.steamkit.steam

import _root_.rovak.steamkit.steam.language.EMsg
import _root_.rovak.steamkit.types.JobID
import _root_.rovak.steamkit.types.steamid.SteamID

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
