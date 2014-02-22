package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EChatAction;
import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.types.steamid.SteamID;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientChatAction implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatAction;
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
	private long steamIdUserToActOn = 0;

	public SteamID getSteamIdUserToActOn() {
		return new SteamID(steamIdUserToActOn);
	}

	public void setSteamIdUserToActOn(SteamID steamId) {
		steamIdUserToActOn = steamId.convertToLong();
	}

	// Static size: 4
	public EChatAction chatAction = null;

	public MsgClientChatAction() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChat);
		stream.write(steamIdUserToActOn);
		stream.write(chatAction.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChat = stream.readLong();
		steamIdUserToActOn = stream.readLong();
		chatAction = EChatAction.f(stream.readInt());
	}
}
