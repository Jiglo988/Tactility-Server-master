package org.hyperion.rs2.model.content.misc;

public class PickpocketNpc {
	private int npcId, level, experience, damage;
	private int[] items;
	private int[] amounts;

	public int getNpcId() {
		return npcId;
	}

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return experience;
	}

	public int getDamage() {
		return damage;
	}

	public int[] getItems() {
		return items;
	}

	public int[] getAmounts() {
		return amounts;
	}
}