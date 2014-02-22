package rovak.steamkit.steam.language.internal

import rovak.steamkit.steam.language.EMsg
import rovak.steamkit.util.stream.{BinaryWriter, BinaryReader}

class MsgHdr extends SteamSerializableHeader {
  var msg = EMsg.Invalid
  var targetJobID = BinaryReader.LongMaxValue
  var sourceJobID = BinaryReader.LongMaxValue


  def serialize(stream: BinaryWriter) {
    stream.write(msg.v)
    stream.write(targetJobID)
    stream.write(sourceJobID)
  }

  def deserialize(stream: BinaryReader) {
    msg = EMsg.f(stream.readInt())
    targetJobID = stream.readLong()
    sourceJobID = stream.readLong()
  }
}
