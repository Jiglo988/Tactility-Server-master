package org.hyperion.rs2.model.content.bounty.rewards;

public final class BHDrop {
	//The id of the item
	private final int id;
	//Chance of receiving the item
	private final int chance;
	//If its in the rare drop table or not
	private final boolean rare;
	
	private BHDrop(final int id, final int chance, final boolean rare) {
		this.id = id;
		this.chance = chance;
		this.rare = rare;
	}
	
	public static BHDrop create(final int id, final int chance, final boolean rare) {
		return new BHDrop(id, chance, rare);
	}
	
	public int getId() {
		return id;
	}
	
	public int getChance() {
		return chance;
	}
	
	public boolean isRare() {
		return rare;
	}

}
