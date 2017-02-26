package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.NameUtils;

public class ClanChatPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		long name = packet.getLong();
		if (player.getInterfaceState().receiveStringListener(NameUtils.longToName(name))) {
			return;
		}
		ClanManager.joinClanChat(player, name);
	}
}
