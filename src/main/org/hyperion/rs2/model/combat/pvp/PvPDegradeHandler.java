package org.hyperion.rs2.model.combat.pvp;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

public final class PvPDegradeHandler {
	
	public static void checkDegrade(final Player player) {
		for(Item item : player.getEquipment().toArray()) {
			if(item == null) continue;
			final int id = item.getId();
			final int degrade = player.getPvPStorage().degrade(id);
			if(degrade == 0)
				player.getEquipment().remove(item);
			if(degrade < 600)
				sendWarningMessages(player, degrade, id);
		}
	}
	
	private static void sendWarningMessages(final Player player, final int degrade, final int id) {
		final String item = ItemDefinition.forId(id).getName();
		if(degrade%100 == 0) {
			player.sendf("@dre@Your %s is at %d%%.", item, degrade/10);
		}
	}
	
}
