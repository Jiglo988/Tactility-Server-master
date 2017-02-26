package org.hyperion.rs2.model.content.bounty;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.bounty.BountyPerks.Perk;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 9/26/14
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
public final class BountyPerkHandler {
	
    public static void handleSpecialPerk(final Player player) {
        final int level = player.getBHPerks().hasPerk(Perk.SPEC_RESTORE);
        if(level < 0)
            return;
        player.getSpecBar().increment((int)(10 * Math.pow(2, level)));
		player.getSpecBar().sendSpecAmount();
		player.getSpecBar().sendSpecBar();

    }

    public static long getVengTimer(final Player player) {
        final int level = player.getBHPerks().hasPerk(Perk.VENG_REDUCTION);
        long nextVeng = 30000;
        if(level < 0)
            return nextVeng;
        nextVeng -= (1000 * Math.pow(1.9, level + 1.2));//4000, 8000, 16000 ms reductions
        return nextVeng;
    }

    public static void appendPrayerLeechPerk(final Player attacker, final Player opponent, final int hit) {
        final int level = attacker.getBHPerks().hasPerk(Perk.PRAY_LEECH);
        if(level < 0)
            return;
        if(opponent != null)  {
            int toDrain = (int)(hit * .1);
            toDrain *= Math.pow(1.6, level);
            if(!opponent.equals(attacker.getBountyHunter().getTarget()))
            	toDrain = toDrain/3;
            opponent.getSkills().detractLevel(5, toDrain);
        }

    }
    
    public static void upgrade(final Player player, final Perk perk) {
    	if(player.getBHPerks().hasPerk(perk) >= perk.maxLevel) {
    		player.sendMessage("You already have the maximum level for this perk!");
    		player.getActionSender().removeChatboxInterface();
    		return;
    	}
		if(player.getBountyHunter().getKills() >= player.getBHPerks().calcNextPerkCost()) {
			player.getBountyHunter().setKills(player.getBountyHunter().getKills() - player.getBHPerks().calcNextPerkCost());
			player.getBHPerks().upgradePerk(perk);
		} else {
			player.sendMessage("You don't have enough BH points for this!");
		}
		player.getActionSender().removeChatboxInterface();
    }
}
