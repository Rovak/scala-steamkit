package rovak.steamkit.steam.language;

public enum EWorkshopEnumerationType {
	RankedByVote(0),
	Recent(1),
	Trending(2),
	FavoriteOfFriends(3),
	VotedByFriends(4),
	ContentByFriends(5),
	RecentFromFollowedUsers(6), ;

	private int code;

	private EWorkshopEnumerationType(int code) {
		this.code = code;
	}

	public int v() {
		return code;
	}
}
