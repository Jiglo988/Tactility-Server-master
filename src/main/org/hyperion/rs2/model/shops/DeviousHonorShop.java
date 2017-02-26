package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.EquipmentReq;

public class DeviousHonorShop extends PointsShop {

	public DeviousHonorShop(int id, String name, Container container) {
		super(id, name, container);
	}

	@Override
	public String getPointsName() {
		return "Honor Points";
	}

	@Override
	public int getPrice(int itemId) {
		switch(itemId) {
			case 16887:
			case 16337:
			case 19605: //ags
				return 2500;

			case 17193:
			case 17339:
			case 17061: //sag coif
				return 1999;

			case 17215: //sag vamb
			case 17317: //sag boots
				return 1499;

			case 19817:
			case 19816:
			case 19815:
				return 750;

			case 18349:
			case 18351:
			case 18353:
				return 499;

			case 19713:
			case 19716:
			case 19719:
				return 999;

			case 15332:
                return 1;
			case 2430:
				return 2;
		}
		return 5000;
	}

	@Override
	protected int getPointsAmount(Player player) {
		return player.getPoints().getHonorPoints();
	}

	@Override
	protected void setPointsAmount(Player player, int value) {
		player.getPoints().setHonorPoints(value);
	}

	@Override
	public void buyFromShop(Player player, Item item) {
		if(player.needsNameChange() || player.doubleChar()) {
			return;
		}
		int requiredElo = EquipmentReq.requiredEloRating(item.getId());
		if(player.getPoints().getEloPeak() < requiredElo) {
			player.getActionSender().sendMessage("You need a PvP rating of at least " + requiredElo + " to buy this item.");
		} else {
			super.buyFromShop(player, item);
		}
	}

}
