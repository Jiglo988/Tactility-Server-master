package org.hyperion.rs2.model.content.misc;

public class ScoreboardPlayer {

	private String name;

	private int bounty;

	public ScoreboardPlayer(String name, int bounty) {
		this.name = name;
		this.bounty = bounty;
	}

	public int getBounty() {
		return bounty;
	}

	public String getName() {
		return name;
	}

}
