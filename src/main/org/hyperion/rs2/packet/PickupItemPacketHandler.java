package org.hyperion.rs2.packet;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.net.Packet;

public class PickupItemPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
			case 236:
	        /*
			 * Option 1.
			 */
				option1(player, packet);
				break;
		}
	}

	/**
	 * Handles the first option on a player option menu.
	 *
	 * @param player
	 * @param packet
	 */
	private void option1(final Player player, Packet packet) {
		final int itemY = packet.getLEShort();
		final int itemID = packet.getShort();
		final int itemX = packet.getLEShort();
		final Position loc = Position.create(itemX, itemY, 0);
		World.submit(new Task(600,"pickupitemhandler") {
			int timeout = 0;

			@Override
			public void execute() {
                if(loc.distance(player.getPosition()) == 1 && timeout > 0) {

                    GlobalItemManager.pickupItem(player, itemID, itemX, itemY);
                    player.playAnimation(Animation.create(7270));
                    player.getWalkingQueue().finish();
                    player.getWalkingQueue().reset();
                    this.stop();
                } else if(loc.distance(player.getPosition()) == 0) {
					//player.getLogging().log("Picked up item : " + itemID);
					GlobalItemManager.pickupItem(player, itemID, itemX, itemY);
                    this.stop();
                } else if(++ timeout >= 10) {
					this.stop();
				}
			}

		});
	}


}

