package org.hyperion.rs2.model.content.bounty.place;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.util.PushMessage;
import org.hyperion.rs2.util.TextUtils;

import java.util.HashMap;
import java.util.Map;

public final class BountyHandler {
	private final static Map<String, Bounty> bounties = new HashMap<>();
	
	public static boolean add(final String playerName, final String fromPlayer, int bounty) {
		bounty = (int)(bounty * .85);
		if(bounty < 400)
		    return false;
		Bounty old = bounties.get(playerName);
		Player player = World.getPlayerByName(fromPlayer);
		if(old != null && old.getBounty() > bounty)
			return false;
		else if(old != null) {
			bounties.remove(playerName);
		}
		PushMessage.pushGlobalMessage(String.format("[@or2@Bounty@bla@] %s has just placed a bounty of %d on %s's head!", TextUtils.ucFirst(fromPlayer), bounty, TextUtils.ucFirst(playerName)));
		player.getAchievementTracker().bountyPlaced(bounty);
		bounties.put(playerName, Bounty.create(playerName, fromPlayer, bounty));
        return true;
	}
	
	public static void handle(final Player killer, String key) {
		final Bounty bounty = bounties.get(key);
		if(bounty != null) {
            int pkpToGain = bounty.getBounty();
            if(ipCheck(killer, key))
                return;
			bounties.remove(key);
			PushMessage.pushGlobalMessage(String.format("[@or2@Bounty@bla@]: %s has just defeated %s for a %d Pk points bounty!", killer.getName(),key, pkpToGain));
			killer.getPoints().increasePkPoints(pkpToGain);
			killer.getAchievementTracker().bountyKill(pkpToGain);
		}
	}

    private static boolean ipCheck(final Player killer, final String key) {
        Player keyPlayer = World.getPlayerByName(key);
		return keyPlayer != null && keyPlayer.getShortIP().equalsIgnoreCase(killer.getShortIP());
	}
	
	public static void listBounties(Player player) {
		bounties.values().forEach(bounty -> player.sendf("From: @or2@%s@bla@ For: @red@%s@bla@ Amount: @blu@%d@bla@.", bounty.getBy(), bounty.getName(), bounty.getBounty()));
	}
}
