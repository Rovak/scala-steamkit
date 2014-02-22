package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EChatInfoType;
import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.types.steamid.SteamID;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientChatMemberInfo implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatMemberInfo;
	}

	// Static size: 8
	private long steamIdChat = 0;

	public SteamID getSteamIdChat() {
		return new SteamID(steamIdChat);
	}

	public void setSteamIdChat(SteamID steamId) {
		steamIdChat = steamId.convertToLong();
	}

	// Static size: 4
	public EChatInfoType type = null;

	public MsgClientChatMemberInfo() {
	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChat);
		stream.write(type.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChat = stream.readLong();
		type = EChatInfoType.f(stream.readInt());
	}
}
