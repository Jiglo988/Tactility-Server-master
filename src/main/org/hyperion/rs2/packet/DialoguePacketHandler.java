package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Player;


public class DialoguePacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, org.hyperion.rs2.net.Packet packet) {
		// TODO Auto-generated method stub
		//player.getLogging().log("Opened dialogue");
		DialogueManager.openDialogue(player, player.getInterfaceState().getNextDialogueId(0));
	}

}
