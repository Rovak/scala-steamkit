package rovak.steamkit.steam.language.internal.msg

import rovak.steamkit.steam.language.internal.SteamSerializableMessage
import rovak.steamkit.util.stream.BinaryReader
import rovak.steamkit.util.stream.BinaryWriter
import rovak.steamkit.steam.language.EMsg

class MsgChannelEncryptResponse extends SteamSerializableMessage {

  var msg = EMsg.ChannelEncryptResponse

  var protocolVersion: Int = MsgChannelEncryptRequest.PROTOCOL_VERSION
  var keySize: Int = 128

  def serialize(stream: BinaryWriter) {
    stream.write(protocolVersion)
    stream.write(keySize)
  }

  def deserialize(stream: BinaryReader) {
    protocolVersion = stream.readInt()
    keySize = stream.readInt()
  }

}


