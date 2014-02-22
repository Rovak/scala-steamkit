package rovak.steamkit.steam.language.internal.msg

import rovak.steamkit.steam.language.internal.SteamSerializableMessage
import rovak.steamkit.steam.language.{EUniverse, EMsg}
import rovak.steamkit.util.stream.{BinaryReader, BinaryWriter}

object MsgChannelEncryptRequest {
  val PROTOCOL_VERSION = 1
}

class MsgChannelEncryptRequest extends SteamSerializableMessage {

  var msg = EMsg.ChannelEncryptRequest

  val PROTOCOL_VERSION: Int = 1
  var protocolVersion: Int = MsgChannelEncryptRequest.PROTOCOL_VERSION
  var universe: EUniverse = EUniverse.Invalid

  def serialize(stream: BinaryWriter) {
    stream.write(protocolVersion)
    stream.write(universe.v)
  }

  def deserialize(stream: BinaryReader) {
    protocolVersion = stream.readInt()
    universe = EUniverse.f(stream.readInt())
  }

}
