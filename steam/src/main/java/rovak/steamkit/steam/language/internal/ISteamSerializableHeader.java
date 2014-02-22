package rovak.steamkit.steam.language.internal;

import rovak.steamkit.steam.language.EMsg;

public interface ISteamSerializableHeader extends ISteamSerializable {
	void setMsg(EMsg msg);
}
