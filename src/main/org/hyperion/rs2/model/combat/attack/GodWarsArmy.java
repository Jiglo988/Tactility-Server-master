package org.hyperion.rs2.model.combat.attack;

import org.hyperion.map.WorldMap;
import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

public class GodWarsArmy implements Attack {

	public String getName() {
		return "GodWarsArmy";
	}

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
			if(n.getDefinition().getId() == 6225) {
				if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
					return 0;
				//range
				n.cE.doAnim(n.getDefinition().getAtkEmote(0));
				n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
				Combat.npcAttack(n, attack, Combat.random(25), 1000, 1);
				Combat.npcRangeAttack(n, attack, 1190, 57, false);
			} else if(n.getDefinition().getId() == 6223) {
				if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
					return 0;
				//mage
				n.cE.doAnim(n.getDefinition().getAtkEmote(0));
				n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
				Combat.npcAttack(n, attack, Combat.random(25), 1000, 2);
				Combat.npcRangeAttack(n, attack, 1190, 57, false);
			} else if(n.getDefinition().getId() == 6227) {
				if(distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
					//melee
					n.cE.doAnim(n.getDefinition().getAtkEmote(1));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					Combat.npcAttack(n, attack, Combat.random(18), 500, 0);
					//Combat.npcRangeAttack(n,attack,1190,57);
				} else
					return 0;

			} else if(n.getDefinition().getId() == 6222) {
				int attackId = Combat.random(4);
				//melee
				if(attackId == 1 && distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					Combat.npcAttack(n, attack, Combat.random(45), 500, 0);
				} else if(attackId == 0 && distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
					if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
						return 0;
					//mage
					n.cE.doAnim(n.getDefinition().getAtkEmote(1));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					for(Player p : RegionManager.getLocalPlayers(n)) {
						int distance2 = p.getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
						if(distance2 <= 10) {
							Combat.npcRangeAttack(n, p.cE, 1198, 68, true);
							Combat.npcAttack(n, p.cE, Combat.random(27), 1000, 2);
						}
					}
				} else {
					if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
						return 0;
					//range
					n.cE.doAnim(n.getDefinition().getAtkEmote(1));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					for(Player p : RegionManager.getLocalPlayers(n)) {
						int distance2 = p.getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
						if(distance2 <= 10) {
							Combat.npcRangeAttack(n, p.cE, 1197, 68, true);
							Combat.npcAttack(n, p.cE, Combat.random(69), 1000, 1);
						}
					}
				}
			}
			return 5;
		} else if(n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 15)) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int[] npcIds() {
		int[] j = {6223, 6225, 6227, 6222,};
		return j;
	}
}
