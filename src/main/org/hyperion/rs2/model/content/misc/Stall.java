package org.hyperion.rs2.model.content.misc;

public class Stall {
	private int stallId, level, experience;
	private double respawnTime;
	private int[] items;
	private int[] amounts;

	public int getStallId() {
		return stallId;
	}

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return experience;
	}

	public double getRespawnTime() {
		return respawnTime;
	}

	public int[] getItems() {
		return items;
	}

	public int[] getAmounts() {
		return amounts;
	}
}