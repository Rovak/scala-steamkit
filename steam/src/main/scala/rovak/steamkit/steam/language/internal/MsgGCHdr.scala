package rovak.steamkit.steam.language.internal

import rovak.steamkit.util.stream.{BinaryWriter, BinaryReader}

class MsgGCHdr extends GCSerializableHeader {

  var msg = 0
  var headerVersion: Short = 1
  var targetJobID = BinaryReader.LongMaxValue
  var sourceJobID = BinaryReader.LongMaxValue

  def serialize(stream: BinaryWriter) {
    stream.write(headerVersion)
    stream.write(targetJobID)
    stream.write(sourceJobID)
  }

  def deserialize(stream: BinaryReader) {
    headerVersion = stream.readShort()
    targetJobID = stream.readLong()
    sourceJobID = stream.readLong()
  }

}

