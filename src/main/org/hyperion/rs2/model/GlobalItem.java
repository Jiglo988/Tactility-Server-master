package org.hyperion.rs2.model;

public class GlobalItem {
	private Position position;

	private Item item;

	public GlobalItem(Player player, int x, int y, int z, Item item) {
		position = Position.create(x, y, z);
		this.item = item;
		this.owner = player;
	}

	public void destroy() {
		owner = null;
		item = null;
		position = null;
	}

	public GlobalItem(Player player, Position loc, Item item) {
		position = loc;
		this.item = item;
		this.owner = player;
	}

	public Player owner;

	public Position getPosition() {
		return position;
	}

	public Item getItem() {
		return item;
	}

	public void setNewItem(Item item) {
		this.item = item;
	}

	public long createdTime = System.currentTimeMillis();

	public boolean itemHidden = true;
}
