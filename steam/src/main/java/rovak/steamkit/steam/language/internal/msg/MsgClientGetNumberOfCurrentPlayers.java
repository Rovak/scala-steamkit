package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.types.gameid.GameID;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientGetNumberOfCurrentPlayers implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientGetNumberOfCurrentPlayers;
	}

	// Static size: 8
	private long gameID = 0;

	public GameID getGameId() {
		return new GameID(gameID);
	}

	public void setGameId(GameID GameID) {
		gameID = GameID.toLong();
	}

	public MsgClientGetNumberOfCurrentPlayers() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(gameID);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		gameID = stream.readLong();
	}
}
