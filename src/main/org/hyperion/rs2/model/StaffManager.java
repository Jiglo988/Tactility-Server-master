package org.hyperion.rs2.model;

import java.util.LinkedList;
import java.util.List;

public class StaffManager {

	public static List<Player> getOnlineStaff() {
		List<Player> onlineStaff = new LinkedList<>();
		for(Player player : World.getPlayers()) {
			if(player == null || player.isHidden())
				continue;
            if(Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
                if(Rank.getPrimaryRank(player).ordinal() < Rank.WIKI_EDITOR.ordinal())
                    continue;
            }
			if(Rank.isStaffMember(player) && Rank.getPrimaryRank(player) != Rank.OWNER && !player.getName().equalsIgnoreCase("nab")) {
				onlineStaff.add(player);
			}
		}
		return onlineStaff;
	}

	static {
	}
}