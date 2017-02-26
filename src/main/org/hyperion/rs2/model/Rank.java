package org.hyperion.rs2.model;

import org.hyperion.rs2.util.TextUtils;

/**
 * Created by DeviousPK on 10/05/14.
 */
public enum Rank {

	PLAYER("@bla@"), // 0
	HERO("@bla@"), // 1
	LEGEND("@bla@"), // 2
	VETERAN("@ffb226@"), // 3
	DONATOR("@ba0000@"), // 4
	SUPER_DONATOR("@03ce10@"), // 5
	WIKI_EDITOR("@bla@"), // 6
	EVENT_MANAGER("@bla@"), // 7
	HELPER("@e519c0@"), // 8
	FORUM_MODERATOR("@blu@"), // 9
	MODERATOR("@blu@"), // 10
	GLOBAL_MODERATOR("@blu@"), // 11
	COMMUNITY_MANAGER("@cya@"), // 12
	HEAD_MODERATOR("@00cccc@"), // 13
	ADMINISTRATOR("@dbl@"), // 14
	DEVELOPER("@6F0095@"), // 15
	OWNER("@FFFFFF@"); // 16

	private final long bitMask;
	private final String yellColor;

	Rank(String yellColor) {
		this.bitMask = 1L << ordinal();
		this.yellColor = yellColor;
	}

	public long getBitMask() {
		return bitMask;
	}

	public String getYellColor() {
		return yellColor;
	}

	@Override
	public String toString() {
		String name = super.toString();
		name = name.replace("_", " ");
		name = TextUtils.titleCase(name, true);
		name = name.replaceAll("Super", "S.").replaceAll("Head", "H.").replaceAll("Forum", "F.").replaceAll("Community", "Comm.");
		return name;
	}


	/* Static methods */

	public static int shift = 0;

	static {
		shift = Rank.values().length + 8;
	}

	public static Rank forIndex(final long index) {
		if(index >= values().length)
			return PLAYER;
		return values()[((int) index)];
	}

	
	public static long setPrimaryRank(Player player, Rank rank) {
		return setPrimaryRank(player.getPlayerRank(), rank);
	}

	public static long setPrimaryRank(long r, Rank rank) {
		return setPrimaryRank(r, rank, false);
	}

	public static long setPrimaryRank(Player player, Rank rank, boolean removeCurrentRankAbility) {
		return setPrimaryRank(player.getPlayerRank(), rank, removeCurrentRankAbility);
	}

	public static long setPrimaryRank(long r, Rank rank, boolean removeCurrentRankAbility) {
		if(removeCurrentRankAbility)
			r = Rank.removeAbility(r, Rank.forIndex((r >> shift)));
		r = r & ~((r >> shift) << shift);
		r = r | (rank.ordinal() << shift);
		return Rank.addAbility(r, rank);
	}

	public static Rank getPrimaryRank(Player player) {
		return getPrimaryRank(player.getPlayerRank());
	}

	public static Rank getPrimaryRank(long r) {
		return Rank.forIndex(getPrimaryRankIndex(r));
	}

	public static long getPrimaryRankIndex(Player player) {
		return player.getPlayerRank() >> shift;
	}

	public static long getPrimaryRankIndex(long r) {
		return r >> shift;
	}

	public static long addAbility(Player player, Rank rank) {
		return addAbility(player.getPlayerRank(), rank);
	}

	public static long addAbility(long r, Rank rank) {
		if(isAbilityToggled(r, rank))
			return r;
		return r | rank.getBitMask();
	}

	public static long removeAbility(Player player, Rank rank) {
		return removeAbility(player.getPlayerRank(), rank);
	}

	public static long removeAbility(long r, Rank rank) {
		if(rank == Rank.PLAYER)
			return r;
		if(getPrimaryRank(r) == rank)
			r = setPrimaryRank(r, Rank.PLAYER);
		return r & ~rank.getBitMask();
	}

	public static boolean hasAbility(Player player, Rank... ranks) {
		return player != null && hasAbility(player.getPlayerRank(), ranks);
	}

	public static boolean hasAbility(long r, Rank... ranks) {
		if(isAbilityToggled(r, Rank.OWNER))
			return true;
		for(Rank rank : ranks) {
			if(rank.ordinal() < Rank.ADMINISTRATOR.ordinal()) {
				if(hasAbility(r, ADMINISTRATOR))
					return true;
			}
			if(rank.ordinal() >= Rank.HELPER.ordinal()) {
				for(int i = Rank.OWNER.ordinal(); i >= rank.ordinal(); i--) {
					if(isAbilityToggled(r, Rank.forIndex(i)))
						return true;
				}
			}
			if(rank == Rank.DONATOR) {
				if(isAbilityToggled(r, Rank.SUPER_DONATOR))
					return true;
			}
			if(rank == Rank.HERO) {
				if(isAbilityToggled(r, Rank.LEGEND))
					return true;
			}
			if(isAbilityToggled(r, rank))
				return true;
		}
		return false;
	}

	public static boolean isAbilityToggled(Player player, Rank rank) {
		return (player.getPlayerRank() & rank.getBitMask()) == rank.getBitMask();
	}

	public static boolean isAbilityToggled(long r, Rank rank) {
		return (r & rank.getBitMask()) == rank.getBitMask();
	}

	public static boolean isStaffMember(Player player) {
		return isStaffMember(player.getPlayerRank());
	}

	public static boolean isStaffMember(long r) {
		for(Rank rank : Rank.values()) {
			if(rank.ordinal() >= Rank.HELPER.ordinal()) {
				if(hasAbility(r, rank))
					return true;
			}
		}
		return false;
	}
}


