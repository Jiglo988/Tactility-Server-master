package org.hyperion.rs2.model.content.bounty.place;

public class Bounty {
	private final long declared;
	private final String playerName;
	private final String fromPlayer;
	private final int bounty;
	
	public static Bounty create(final String playerName, final String fromPlayer, final int bounty) {
		return new Bounty(playerName, fromPlayer, bounty);
	}
	
	public Bounty(final String playerName, final String fromPlayer, final int bounty) {
		this.declared = System.currentTimeMillis();
		this.fromPlayer = fromPlayer;
		this.playerName = playerName;
		this.bounty = bounty;
	}
	
	public long getDeclared() {
		return declared;
	}

	public String getName() {
		return playerName;
	}

	public String getBy() {
		return fromPlayer;
	}

	public int getBounty() {
		return bounty;
	}
}
