package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * npc options
 *
 * @author Martin
 */
public class NPCOptionPacketHandler implements PacketHandler {

	/**
	 * Option 1 opcode.
	 */
	private static final int OPTION_1 = 155, OPTION_2 = 17, OPTION_3 = 21;

	@Override
	public void handle(Player player, Packet packet) {
		if(player.getRandomEvent().isDoingRandom()) {
			player.getRandomEvent().display();
			return;
		}
		switch(packet.getOpcode()) {
			case OPTION_1:
				handleOption1(player, packet);
				break;
			case OPTION_2:
				handleOption2(player, packet);
				break;
			case OPTION_3:
				handleOption3(player, packet);
				break;
		}
	}

	private void handleOption3(Player player, Packet packet) {
		int slot = packet.getShort();
		player.delayObjectClick[0] = slot;
		player.delayObjectClick[3] = 7;
	}

	/**
	 * Handles the option 1 packet.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleOption1(Player player, Packet packet) {
		int slot = packet.getLEShort() & 0xFFFF;
		player.delayObjectClick[0] = slot;
		player.delayObjectClick[3] = 5;
	}

	/**
	 * Handles the option 2 packet.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleOption2(Player player, Packet packet) {
		int slot = packet.getLEShortA() & 0xFFFF;
		player.delayObjectClick[0] = slot;
		player.delayObjectClick[3] = 6;
	}


}
