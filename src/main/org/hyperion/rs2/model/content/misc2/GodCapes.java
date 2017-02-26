package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;

public class GodCapes implements ContentTemplate {

	private static int[][] IDs = {
			//ObjectId,ItemId
			{2873, 2412}, //Saradomin
			{2874, 2414}, //Zamorak
			{2875, 2413}, //Guthix
	};

	private int getItem(int objectid) {
		for(int i = 0; i < IDs.length; i++) {
			if(objectid == IDs[i][0])
				return IDs[i][1];
		}
		return - 1;
	}

	public boolean hasCapes(Player player) {
		for(Item item : player.getInventory().toArray()) {
			if(item == null)
				continue;
			int id = item.getId();
			int capeid = - 1;
			Item cape = player.getEquipment().get(Equipment.SLOT_CAPE);
			if(cape != null)
				capeid = cape.getId();
			for(int i = 0; i < IDs.length; i++) {
				if(IDs[i][1] == id || IDs[i][1] == capeid)
					return true;
			}

		}
		return false;
	}

	private void getCape(Player player, int objectid) {
		if(hasCapes(player)) {
			player.getActionSender().sendMessage("You already have a god cape.");
			return;
		}
		player.getActionSender().sendMessage("The gods reward you with a god cape.");
		ContentEntity.addItem(player, getItem(objectid));
		player.playAnimation(Animation.create(645, 0));
	}

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c, int d) {
		if(type == 6) {
			getCape(player, a);
			player.face(Position.create(b, c, player.getPosition().getZ()));
		}
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {

	}

	@Override
	public int[] getValues(int type) {
		if(type == 6)
			return Misc.getColumn(IDs, 0);
		return null;
	}

}
