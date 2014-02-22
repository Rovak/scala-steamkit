package rovak.steamkit.steam.language.internal.msg

import rovak.steamkit.steam.language.{EResult, EMsg}
import rovak.steamkit.util.stream.{BinaryReader, BinaryWriter}
import rovak.steamkit.steam.language.internal.SteamSerializableMessage

class MsgChannelEncryptResult extends SteamSerializableMessage {
  var msg = EMsg.ChannelEncryptResult

  var result = EResult.Invalid


  def serialize(stream: BinaryWriter) {
    stream.write(result.v)
  }

  def deserialize(stream: BinaryReader) {
    result = EResult.f(stream.readInt())
  }
}

