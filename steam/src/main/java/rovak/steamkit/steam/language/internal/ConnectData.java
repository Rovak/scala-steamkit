package rovak.steamkit.steam.language.internal;

import java.io.IOException;

import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

public class ConnectData implements ISteamSerializable {
	public static final int CHALLENGE_MASK = ChallengeData.CHALLENGE_MASK;
	// Static size: 4
	public int challengeValue = 0;

	public ConnectData() {
		challengeValue = 0;
	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(challengeValue);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		challengeValue = stream.readInt();
	}
}
