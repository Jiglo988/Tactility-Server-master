package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.GlobalItemManager;
import org.hyperion.rs2.model.ObjectManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.DoorManager;
import org.hyperion.rs2.model.content.skill.Farming;
import org.hyperion.rs2.net.Packet;

// Referenced classes of package org.hyperion.rs2.packet:
//            PacketHandler

public class ReloadRegion implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		DoorManager.refresh(player);
		GlobalItemManager.displayItems(player);
		Farming.farming.refreshFarmObjects(player);
		ObjectManager.load(player);
		player.getWalkingQueue().reset();
	}
}
