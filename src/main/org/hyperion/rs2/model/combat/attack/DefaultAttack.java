package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;

public class DefaultAttack implements Attack {

	public String getName() {
		return "DefaultAttack";
	}

	public int handleAttack(NPC n, CombatEntity attack) {
		if(Position.create(n.getPosition().getX() + n.cE.getOffsetX(), n.getPosition().getY() + n.cE.getOffsetY(), n.getPosition().getZ()).isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				//System.out.println("Predicted attack waiting.");
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
            if(attack != null && attack.getEntity() instanceof NPC && attack.getNPC().ownerId > 0) {
                final Player player = (Player)World.getPlayers().get(attack.getNPC().ownerId);
                if(player != null)
                    attack = player.cE;
            }
			if(! Combat.canAtkDis(n.cE, attack)) {
				//System.out.println("Can atk waiting");
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
			n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
			int maxHit = 0;
			if(n.getDefinition().combat() > 200)
				maxHit = n.getDefinition().combat() / 9;
			else if(n.getDefinition().combat() > 175)
				maxHit = (int) (n.getDefinition().combat() / 8.5);
			else if(n.getDefinition().combat() > 150)
				maxHit = n.getDefinition().combat() / 8;
			else if(n.getDefinition().combat() > 120)
				maxHit = (int) (n.getDefinition().combat() / 7.5);
			else if(n.getDefinition().combat() > 100)
				maxHit = n.getDefinition().combat() / 7;
			else if(n.getDefinition().combat() > 80)
				maxHit = (int) (n.getDefinition().combat() / 8.3);
			else if(n.getDefinition().combat() > 60)
				maxHit = (int) (n.getDefinition().combat() / 8.7);
			else if(n.getDefinition().combat() > 40)
				maxHit = n.getDefinition().combat() / 8;
			else if(n.getDefinition().combat() > 20)
				maxHit = n.getDefinition().combat() / 6;
			else
				maxHit = n.getDefinition().combat() / 6;
			//attack.hit(Combat.random(maxHit),attack.getEntity(),false);
	        /*if(Combat.random(n.getDefinition().getBonus()[1]) < Combat.random(CombatAssistant.calculateMeleeDefence(attack.getPlayer(), 1))){
				maxHit = 0;
			}*/
            int tempDamage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(maxHit), Constants.MELEE, maxHit);
			Combat.npcAttack(n, attack, tempDamage, 500, Constants.MELEE);
			n.cE.doAtkEmote();
			return 5;
		} else if(n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 9)) {
			int distance = n.getPosition().distance(n.cE.getOpponent().getEntity().getPosition());
			//System.out.println("Distance between npcs is :" + distance);
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int[] npcIds() {
		int[] j = {- 1,};
		return j;
	}
}