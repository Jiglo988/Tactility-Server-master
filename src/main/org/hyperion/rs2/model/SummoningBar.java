package org.hyperion.rs2.model;

public class SummoningBar {
	public static final int DELAY = 1500;
	
	private long lastSpec;
	
	public void setLast(long l) {
		lastSpec = l;
	}
	
	public long getLast() {
		return lastSpec;
	}
	
	private int amount;
	
	private Player player;
	public SummoningBar(Player p) {
		this.player = p;
	}
	
	public void increment(int increase) {
		amount += increase;
		if(amount > 100)
			amount = 100;
	}
	
	public void decrement(int decrease) {
		amount -= decrease;
		if(amount < 0)
			amount = 0;
	}
	
	public void cycle() {
		amount += 10;
		if(amount > 100)
			amount = 100;
	}
	
	public int getAmount() {
		return amount;
	}
	
}
