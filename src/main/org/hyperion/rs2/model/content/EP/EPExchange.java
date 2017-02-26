package org.hyperion.rs2.model.content.EP;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ContentEntity;

public class EPExchange {

	public static boolean exchangeDrops(Player p) {
		boolean exchanged = false;
		for(Item i : p.getInventory().toArray()) {
			if(replaceItem(p, i))
				exchanged = true;
		}
		return exchanged;
	}

	public static boolean replaceItem(Player p, Item item) {
		if(item == null)
			return false;
		int count = item.getCount();
		int id = item.getId();
		int points = 0;
		switch(id) {
			case 14889:
				points = 5;
				break;
			case 14888:
				points = 10;
				break;
			case 14887:
				points = 15;
				break;
			case 14885:
				points = 20;
				break;
			case 14881:
				points = 25;
				break;
			case 14880:
				points = 30;
				break;
			case 14879:
				points = 35;
				break;
			case 14878:
				points = 40;
				break;
			case 14876:
				points = 50;
				break;
			default:
				return false;
		}
		points *= count;
		ContentEntity.deleteItemA(p, id, count);
		p.getPoints().increasePkPoints(points * 4);
		return true;
	}

}
