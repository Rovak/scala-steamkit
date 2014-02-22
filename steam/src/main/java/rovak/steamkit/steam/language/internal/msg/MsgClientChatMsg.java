package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EChatEntryType;
import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.types.steamid.SteamID;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientChatMsg implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatMsg;
	}

	// Static size: 8
	private long steamIdChatter = 0;

	public SteamID getSteamIdChatter() {
		return new SteamID(steamIdChatter);
	}

	public void setSteamIdChatter(SteamID steamId) {
		steamIdChatter = steamId.convertToLong();
	}

	// Static size: 8
	private long steamIdChatRoom = 0;

	public SteamID getSteamIdChatRoom() {
		return new SteamID(steamIdChatRoom);
	}

	public void setSteamIdChatRoom(SteamID steamId) {
		steamIdChatRoom = steamId.convertToLong();
	}

	// Static size: 4
	public EChatEntryType chatMsgType = null;

	public MsgClientChatMsg() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChatter);
		stream.write(steamIdChatRoom);
		stream.write(chatMsgType.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChatter = stream.readLong();
		steamIdChatRoom = stream.readLong();
		chatMsgType = EChatEntryType.f(stream.readInt());
	}
}
