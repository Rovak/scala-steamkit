package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EChatRoomEnterResponse;
import rovak.steamkit.steam.language.EChatRoomType;
import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.types.steamid.SteamID;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientChatEnter implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientChatEnter;
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
	private long steamIdFriend = 0;

	public SteamID getSteamIdFriend() {
		return new SteamID(steamIdFriend);
	}

	public void setSteamIdFriend(SteamID steamId) {
		steamIdFriend = steamId.convertToLong();
	}

	// Static size: 4
	public EChatRoomType chatRoomType = null;
	// Static size: 8
	private long steamIdOwner = 0;

	public SteamID getSteamIdOwner() {
		return new SteamID(steamIdOwner);
	}

	public void setSteamIdOwner(SteamID steamId) {
		steamIdOwner = steamId.convertToLong();
	}

	// Static size: 8
	private long steamIdClan = 0;

	public SteamID getSteamIdClan() {
		return new SteamID(steamIdClan);
	}

	public void setSteamIdClan(SteamID steamId) {
		steamIdClan = steamId.convertToLong();
	}

	// Static size: 1
	public byte chatFlags = 0;
	// Static size: 4
	public EChatRoomEnterResponse enterResponse = null;

	public MsgClientChatEnter() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(steamIdChat);
		stream.write(steamIdFriend);
		stream.write(chatRoomType.v());
		stream.write(steamIdOwner);
		stream.write(steamIdClan);
		stream.write(chatFlags);
		stream.write(enterResponse.v());

	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		steamIdChat = stream.readLong();
		steamIdFriend = stream.readLong();
		chatRoomType = EChatRoomType.f(stream.readInt());
		steamIdOwner = stream.readLong();
		steamIdClan = stream.readLong();
		chatFlags = stream.readByte();
		enterResponse = EChatRoomEnterResponse.f(stream.readInt());
	}
}
