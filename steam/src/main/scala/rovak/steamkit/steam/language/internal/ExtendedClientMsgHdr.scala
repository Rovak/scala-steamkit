package rovak.steamkit.steam.language.internal

import rovak.steamkit.steam.language.EMsg
import rovak.steamkit.util.stream.BinaryReader
import rovak.steamkit.util.stream.BinaryWriter

class ExtendedClientMsgHdr extends SteamSerializableHeader {

  var msg: EMsg = EMsg.Invalid
  var headerSize: Byte = 36
  var headerVersion: Short = 2
  var targetJobID: Long = BinaryReader.LongMaxValue
  var sourceJobID: Long = BinaryReader.LongMaxValue
  var headerCanary: Byte = 239.toByte
  var steamID = 0L
  var sessionID = 0

  def serialize(stream: BinaryWriter) = {
    stream.write(msg.v())
    stream.write(headerSize)
    stream.write(headerVersion)
    stream.write(targetJobID)
    stream.write(sourceJobID)
    stream.write(headerCanary)
    stream.write(steamID)
    stream.write(sessionID)
  }

  def deserialize(stream: BinaryReader): Unit = {
    msg = EMsg.f(stream.readInt())
    headerSize = stream.readByte()
    headerVersion = stream.readShort()
    targetJobID = stream.readLong()
    sourceJobID = stream.readLong()
    headerCanary = stream.readByte()
    steamID = stream.readLong()
    sessionID = stream.readInt()
  }
}
