package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.net.Packet;

import java.util.logging.Logger;

/**
 * Reports information about unhandled packets.
 *
 * @author Graham Edgecombe
 */
public class DefaultPacketHandler implements PacketHandler {

	/**
	 * The logger instance.
	 */
	private static final Logger logger = Logger.getLogger(DefaultPacketHandler.class.getName());

	@Override
	public void handle(Player player, Packet packet) {
		if(Rank.hasAbility(player, Rank.ADMINISTRATOR))
			player.getActionSender().sendMessage("Packet : [opcode=" + packet.getOpcode() + " length=" + packet.getLength() + " payload=" + packet.getPayload() + "]");
		//logger.info("Packet : [opcode=" + packet.getOpcode() + " length=" + packet.getLength() + " payload=" + packet.getPayload() + "]");
	}

}
