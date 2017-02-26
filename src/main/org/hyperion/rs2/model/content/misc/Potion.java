package org.hyperion.rs2.model.content.misc;

public class Potion {
	private int potionId;
	private int[] secondItem, finishedPotion, level, experience;

	public int getPotionId() {
		return potionId;
	}

	public int[] getSecondItems() {
		return secondItem;
	}

	public int[] getPotionLevel() {
		return level;
	}

	public int[] getPotionExp() {
		return experience;
	}

	public int[] getFinishedPotion() {
		return finishedPotion;
	}

}