package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.EResult;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientSetIgnoreFriendResponse implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientSetIgnoreFriendResponse;
	}

	// Static size: 8
	public long unknown = 0;
	// Static size: 4
	public EResult result = EResult.Invalid;

	public MsgClientSetIgnoreFriendResponse() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(unknown);
		stream.write(result.v());
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		unknown = stream.readLong();
		result = EResult.f(stream.readInt());
	}
}
