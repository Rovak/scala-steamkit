package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EChatAction;
import rovak.steamkit.steam.language.EChatActionResult;
import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.types.steamid.SteamID;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientChatActionResult implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatActionResult;
	}

	// Static size: 8
	private long steamIdChat = 0;

	public SteamID getSteamIdChat() {
		return new SteamID(steamIdChat);
	}

	public void setSteamIdChat(SteamID steamId) {
		steamIdChat = steamId.convertToLong();
	}

	// Static size: 8
	private long steamIdUserActedOn = 0;

	public SteamID getSteamIdUserActedOn() {
		return new SteamID(steamIdUserActedOn);
	}

	public void setSteamIdUserActedOn(SteamID steamId) {
		steamIdUserActedOn = steamId.convertToLong();
	}

	// Static size: 4
	public EChatAction chatAction = null;
	// Static size: 4
	public EChatActionResult actionResult = null;

	public MsgClientChatActionResult() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChat);
		stream.write(steamIdUserActedOn);
		stream.write(chatAction.v());
		stream.write(actionResult.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChat = stream.readLong();
		steamIdUserActedOn = stream.readLong();
		chatAction = EChatAction.f(stream.readInt());
		actionResult = EChatActionResult.f(stream.readInt());
	}
}
