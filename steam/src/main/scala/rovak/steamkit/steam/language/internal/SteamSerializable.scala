package rovak.steamkit.steam.language.internal

import rovak.steamkit.util.stream.{BinaryReader, BinaryWriter}

trait SteamSerializable {
  def serialize(stream: BinaryWriter): Unit
  def deserialize(stream: BinaryReader): Unit
}
