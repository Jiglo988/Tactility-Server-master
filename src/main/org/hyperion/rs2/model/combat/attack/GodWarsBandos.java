package org.hyperion.rs2.model.combat.attack;

import org.hyperion.map.WorldMap;
import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

public class GodWarsBandos implements Attack {

	public static final int BANDOS_BOSS = 6260;

	public String getName() {
		return "GodWarsBandos";
	}

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				if(n.getDefinition().getId() == 6261) {
					if(distance > (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
						return 0;
					}
				}
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
			if(n.getDefinition().getId() == 6265) {
				if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
					return 0;
				//range
				n.cE.doAnim(n.getDefinition().getAtkEmote(0));
				n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
                final int damage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(19), 1, 19);
				Combat.npcAttack(n, attack, damage, 700, 1);
				Combat.npcRangeAttack(n, attack, 1206, 43, false);

			} else if(n.getDefinition().getId() == 6263) {
				if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
					return 0;
				//mage
				n.cE.doAnim(n.getDefinition().getAtkEmote(0));
				n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
                final int damage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(17), 2, 17);
                Combat.npcAttack(n, attack, damage, 700, 2);
				Combat.npcRangeAttack(n, attack, 1203, 43, true);
			} else if(n.getDefinition().getId() == 6261) {

				//melee
				if(distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
                    final int damage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(25), 0, 25);
                    Combat.npcAttack(n, attack, damage, 500, 0);
				} else
					return 0;
			} else if(n.getDefinition().getId() == BANDOS_BOSS) {
				int attackId = Combat.random(4);
				//melee
				if(attackId >= 2) {
					if(distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
						n.cE.doAnim(n.getDefinition().getAtkEmote(0));
						n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
                        final int damage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(55), 0, 55);
                        Combat.npcAttack(n, attack, damage, 500, 0);
					} else
						return 0;
				} else {
					if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
						return 0;
					//range
					n.cE.doAnim(n.getDefinition().getAtkEmote(1));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					for(Player p : RegionManager.getLocalPlayers(n)) {
						int distance2 = p.getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
						if(distance2 <= 10 && WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), p.cE.getAbsX(), p.cE.getAbsY())) {
							p.cE.doGfx(1177, 0);
                            final int damage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(37), 1, 37);
                            Combat.npcAttack(n, p.cE, damage, 500, 1);
						}
					}
				}
				if(distance > (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))
					return 0;
			}

			return 5;
		} else if(n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 10)) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int[] npcIds() {
		int[] j = {6265, 6263, 6261, 6260,};
		return j;
	}

}
