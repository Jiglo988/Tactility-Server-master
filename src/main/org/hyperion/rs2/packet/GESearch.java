package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.NameUtils;

public class GESearch implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		Long message = packet.getLong();
		String itemName = "";
		try {
			itemName = NameUtils.longToName(message);
		} catch(Exception e) {
			return;
		}
		if(itemName.equals(player.lastSearch))
			return;
		player.lastSearch = itemName;
	}

}
