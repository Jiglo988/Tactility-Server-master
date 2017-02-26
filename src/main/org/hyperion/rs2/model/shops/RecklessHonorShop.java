package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.EquipmentReq;

public class RecklessHonorShop extends PointsShop {

	public RecklessHonorShop(int id, String name, Container container) {
		super(id, name, container);
	}

	@Override
	public String getPointsName() {
		return "DeviousPK Points";
	}

	@Override
	public int getPrice(int itemId) {
		switch(itemId) {
			case 19817:
			case 19816:
			case 19815:
				return 5000;
		}
		return 5000;
	}

	@Override
	protected int getPointsAmount(Player player) {
		return player.getPoints().getPkPoints();
	}

	@Override
	protected void setPointsAmount(Player player, int value) {
		player.getPoints().setPkPoints(value);
	}

	@Override
	public void buyFromShop(Player player, Item item) {
		int requiredHonors = EquipmentReq.requiredHonorPoints(item.getId());
		if(player.getPoints().getHonorPoints() < requiredHonors) {
			player.getActionSender().sendMessage("You need at least " + requiredHonors + " honor points to buy this item.");
		} else {
			super.buyFromShop(player, item);
			player.getActionSender().sendString(3901, String.format("Honor points: @gre@%,d", player.getPoints().getHonorPoints()));
		}
	}
}
