package rovak.steamkit.steam.language.internal;

import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public interface ISteamSerializable {
	void serialize(BinaryWriter stream) throws IOException;

	void deSerialize(BinaryReader stream) throws IOException;
}
