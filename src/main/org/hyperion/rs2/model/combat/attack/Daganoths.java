package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;

public class Daganoths implements Attack {

	public String getName() {
		return "Daganoths";
	}

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (15 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
			if(n.getDefinition().getId() == 2882) {
				//prime
				if(distance <= (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					int hit = Combat.random(61);
					Combat.npcAttack(n, attack, hit, 700, 2);
					Combat.npcRangeAttack(n, attack, 476, 43, false);
				} else
					return 0;
			} else if(n.getDefinition().getId() == 2881) {
				//supreme
				if(distance <= (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					int hit = Combat.random(30);
					Combat.npcAttack(n, attack, hit, 700, 1);
					Combat.npcRangeAttack(n, attack, 475, 43, false);
				} else
					return 0;
			} else if(n.getDefinition().getId() == 2881) {
				//spinyop
				if(distance <= (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					int hit = Combat.random(30);
					Combat.npcAttack(n, attack, hit, 700, 1);
					Combat.npcRangeAttack(n, attack, 475, 43, false);
				} else
					return 6;
			} else if(n.getDefinition().getId() == 2883) {
				//rex
				if(distance <= (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					int hit = Combat.random(28);
					Combat.npcAttack(n, attack, hit, 500, 0);
					n.cE.doAtkEmote();
				} else
					return 0;
			}
			return 5;
		} else if(n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 100)) {
			if(n.getDefinition().getId() == 2892)
				return 6;
			return 0;
		} else {
			return 0;
		}
	}

	@Override
	public int[] npcIds() {
		int[] j = {2881, 2882, 2883, 2892,};
		return j;
	}
}
