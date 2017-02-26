package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.NameUtils;

public class ReportAbuse implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		long l = packet.getLong();
		boolean mute = packet.get() == 1 ? true : false;
		try {
			String name = NameUtils.longToName(l);
			int rule = packet.get();
			//World.getAbuseHandler().reportAbuse(player,name,rule);
		} catch(ArrayIndexOutOfBoundsException e2) {
			//errors todo with input will be handled here
		} catch(Exception e) {
			e.printStackTrace();//shouldnt need to be called
		}
	}
}
