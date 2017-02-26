package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.content.misc2.MysteryBox;

/**
 * @author Arsen Maxyutov.
 */
public class VoteShop extends PointsShop {

	public VoteShop(int id, String name, Container container) {
		super(id, name, container);
	}

	@Override
	public int getPrice(int itemId) {
		switch(itemId) {
			case MysteryBox.ID:
			case 10025:
				return 20;
			case 17237:
			case 16755:
			case 18747:
			case 16865:
				return 20;
			case 16931:
            case 15020:
            case 15019:
            case 15018:
				return 20;
            case 17017:
                return 50;
			case 15220: // Imbued rings

			case 19747:
			case 13101:
				return 30;
			case 15600:
			case 15606:
            case 17171:
            case 15612:
			case 15618:
			case 15602:
			case 15608:
			case 15614:
			case 15620:
			case 15604:
			case 15610:
			case 15616:
			case 15622:
			case 15021:
			case 15022:
			case 15023:
			case 15024:
			case 15025:
			case 15026:
			case 15027:
			case 15028:
			case 15029:
			case 15030:
			case 15031:
			case 15032:
			case 15033:
			case 15034:
			case 15035:
			case 15036:
			case 15037:
			case 15038:
			case 15039:
			case 15040:
			case 15041:
			case 15042:
			case 15043:

			case 15044:
				return 10;
			case 16153:
			case 16154:
			case 16155:
			case 16156:
				return 25;
			case 10858:
				return 60;
			case 14876:
                return 3;
			case 2890:
				return 8;
			case 19780:
			case 18808:
				return 70;
		}
		return 5000;
	}

	@Override
	public void buyFromShop(Player player, Item item) {
		super.buyFromShop(player, item);
	}

	@Override
	public String getPointsName() {
		return "Vote Points";
	}

	@Override
	protected int getPointsAmount(Player player) {
		return player.getPoints().getVotingPoints();
	}

	@Override
	protected void setPointsAmount(Player player, int value) {
		player.getPoints().setVotingPoints(value);
	}

}
