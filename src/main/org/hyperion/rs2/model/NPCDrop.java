package org.hyperion.rs2.model;

public class NPCDrop {
	private int itemId; //item id
	private int minAmount, maxAmount;
	private int chance; //chance out of 1000, i.e. chance = 100 = 1/10 chance of the item
	
	private NPCDrop(int itemId, int minAmount, int maxAmount, int chance) {
		this.itemId = itemId;
		this.maxAmount = maxAmount;
		this.minAmount = minAmount;
		this.chance = chance;
	}
	
	public static NPCDrop create(int itemId, int minAmount, int maxAmount, int chance) {
		return new NPCDrop(itemId, minAmount, maxAmount, chance);
	}

	public int getChance() {
		return chance;
	}
	
	public int getId() {
		return itemId;
	}
	
	public int getMin() {
		return minAmount;
	}
	
	public int getMax() {
		return maxAmount;
	}
}
