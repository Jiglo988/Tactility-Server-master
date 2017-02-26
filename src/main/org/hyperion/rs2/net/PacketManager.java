package org.hyperion.rs2.net;

import org.apache.mina.core.session.IoSession;
import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.packet.DefaultPacketHandler;
import org.hyperion.rs2.packet.InterfacePacketHandler;
import org.hyperion.rs2.packet.PacketHandler;
import org.hyperion.rs2.packet.QuietPacketHandler;

import java.nio.BufferUnderflowException;
import java.util.logging.Logger;

/**
 * Managers <code>PacketHandler</code>s.
 *
 * @author Graham Edgecombe
 */
public class PacketManager {

	/**
	 * The logger class.
	 */
	private static final Logger logger = Logger.getLogger(PacketManager.class.getName());

	/**
	 * The instance.
	 */
	private static final PacketManager INSTANCE = new PacketManager();

	/**
	 * Gets the packet manager instance.
	 *
	 * @return The packet manager instance.
	 */
	public static PacketManager getPacketManager() {
		return INSTANCE;
	}

	/**
	 * The packet handler array.
	 */
	public PacketHandler[] packetHandlers = new PacketHandler[256];

	/**
	 * Creates the packet manager.
	 */
	public PacketManager() {
	    /*
		 * Set default handlers.
		 */
		final PacketHandler defaultHandler = new DefaultPacketHandler();
		for(int i = 0; i < packetHandlers.length; i++) {
			if(packetHandlers[i] == null) {
				packetHandlers[i] = defaultHandler;
			}
		}
	}

	/**
	 * Binds an opcode to a handler.
	 *
	 * @param id      The opcode.
	 * @param handler The handler.
	 */
	public void bind(int id, PacketHandler handler) {
		packetHandlers[id] = handler;
	}

	/**
	 * Handles a packet.
	 *
	 * @param session The session.
	 * @param packet  The packet.
	 */
	public void handle(IoSession session, Packet packet) {
		Player player = (Player) session.getAttribute("player");
        try {
			if(((packet.getOpcode() == InterfacePacketHandler.DATA_OPCODE) || (player.verificationCodeEntered || packet.getOpcode() == 4 || packet.getOpcode() == 103)) || packetHandlers[packet.getOpcode()] instanceof QuietPacketHandler) {
				packetHandlers[packet.getOpcode()].handle(player, packet);
			}
		} catch(BufferUnderflowException nio) {
			if(!World.gracefullyExitSession(session))
				session.close(false);
		} catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception with packet " + packet.getOpcode() + " caused by Player : " + player.getName());
			FileLogging.writeError("packet_errors.txt", ex);
			if(!World.gracefullyExitSession(session))
				session.close(false);
		} finally {
            player.getExtraData().put("packetCount", player.getExtraData().getInt("packetCount")-1);
            try {
                packet.getPayload().clear();
                packet.getPayload().free();
            } catch (Exception ignored){}
        }
	}


}
