package rovak.steamkit.steam.language;

public enum EContentDownloadSourceType {
	Invalid(0),
	CS(1),
	CDN(2),
	LCS(3),
	Proxy(4), ;

	private int code;

	private EContentDownloadSourceType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
