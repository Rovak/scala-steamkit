package rovak.steamkit.steam.language.internal.msg;

import rovak.steamkit.steam.language.EMsg;
import rovak.steamkit.steam.language.internal.ISteamSerializableMessage;
import rovak.steamkit.util.stream.BinaryReader;
import rovak.steamkit.util.stream.BinaryWriter;

import java.io.IOException;

public class MsgClientVACBanStatus implements ISteamSerializableMessage {

	@Override
	public EMsg getEMsg() {
		return EMsg.ClientVACBanStatus;
	}

	// Static size: 4
	public int numBans = 0;

	public MsgClientVACBanStatus() {

	}

	@Override
	public void serialize(BinaryWriter stream) throws IOException {
		stream.write(numBans);
	}

	@Override
	public void deSerialize(BinaryReader stream) throws IOException {
		numBans = stream.readInt();
	}
}
