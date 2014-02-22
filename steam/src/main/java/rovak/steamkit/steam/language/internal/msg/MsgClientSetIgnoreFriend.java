package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.types.steamid.SteamID;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientSetIgnoreFriend implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientSetIgnoreFriend;
	}

	// Static size: 8
	private long mySteamId = 0;

	public SteamID getMySteamId() {
		return new SteamID(mySteamId);
	}

	public void setMySteamId(SteamID steamId) {
		mySteamId = steamId.convertToLong();
	}

	// Static size: 8
	private long steamIdFriend = 0;

	public SteamID getSteamIdFriend() {
		return new SteamID(steamIdFriend);
	}

	public void setSteamIdFriend(SteamID steamId) {
		steamIdFriend = steamId.convertToLong();
	}

	// Static size: 1
	public byte ignore = 0;

	public MsgClientSetIgnoreFriend() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(mySteamId);
		stream.write(steamIdFriend);
		stream.write(ignore);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		mySteamId = stream.readLong();
		steamIdFriend = stream.readLong();
		ignore = stream.readByte();
	}
}
