package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.combat.Constants;
import org.hyperion.util.Misc;

public class DragonfireShield {
	public static void handleSpecial(Player player, CombatEntity opp) {
		if(player != null && opp != null) {
            String canAtk = Combat.canAtk(player.cE, opp);
            if(canAtk.length() > 1) {
                player.getActionSender().sendMessage(canAtk);
                return;
            }
            if (player.canDFS() && Rank.hasAbility(player, Rank.DONATOR)) {
                try {
                	int damage = Misc.random(20) + 5;
                    opp.hit(damage, player, false, Constants.MAGE);
                	if(Misc.random(3) == 1) {
                		player.getCombat().hit(damage/2, player, false, org.hyperion.rs2.Constants.DEFLECT);
                		player.getActionSender().sendMessage("@dbl@The heat of the shield burns you!");
                	}
                    player.playAnimation(Animation.create(6696));
                    opp.getEntity().playGraphics(Graphic.create(1167));
                    player.playGraphics(Graphic.create(1165));
                    player.resetDFS();
                } catch (NullPointerException e) {
                    System.out.println("Dragonfire shield corruption!");
                }
            } else if(!Rank.hasAbility(player, Rank.DONATOR)){
                player.getActionSender().sendMessage("This feature is exclusive to donators, sorry!");
            } else {
                player.getActionSender().sendMessage("Your shield is recharging...");
            }
        }
	}
}
