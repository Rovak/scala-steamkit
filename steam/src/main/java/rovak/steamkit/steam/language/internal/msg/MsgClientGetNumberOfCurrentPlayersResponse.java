package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.EResult;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientGetNumberOfCurrentPlayersResponse implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientGetNumberOfCurrentPlayersResponse;
	}

	// Static size: 4
	public EResult result = EResult.Invalid;
	// Static size: 4
	public int numPlayers = 0;

	public MsgClientGetNumberOfCurrentPlayersResponse() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(result.v());
		stream.write(numPlayers);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		result = EResult.f(stream.readInt());
		numPlayers = stream.readInt();
	}
}
