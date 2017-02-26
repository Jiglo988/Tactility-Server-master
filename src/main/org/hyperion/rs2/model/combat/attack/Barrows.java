package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.content.skill.Prayer;

public class Barrows implements Attack {

	public static final int
			AHRIM = 2025, DHAROK = 2026, GUTHAN = 2027, KARIL = 2028, TORAG = 2029, VERAC = 2030;


	public String getName() {
		return "Barrows";
	}

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				if(n.getDefinition().getId() != 2025 && n.getDefinition().getId() != 2028) {
					if(distance > (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
						return 0;
					}
				}
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
			if(n.getDefinition().getId() == AHRIM) {
				//ahrim
				if(distance < (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					int hit = Combat.random(19);
					Prayer.smite(attack.getPlayer(), (hit / 2));
					Combat.npcAttack(n, attack, hit, 700, 2);
					Combat.npcRangeAttack(n, attack, 139, 43, false);
				}
			}
			if(n.getDefinition().getId() == DHAROK) {
				//dharok
				if(distance <= (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					int maxHit = 16 + ((n.maxHealth - n.health) / 5);
					Prayer.smite(attack.getPlayer(), (maxHit / 2));
					Combat.npcAttack(n, attack, maxHit, 500, 0);
				} else
					return 0;
			}

			if(n.getDefinition().getId() == GUTHAN) {
				//guthan
				if(distance <= (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					int hit = Combat.random(25);
					boolean block = Combat.npcAttack(n, attack, hit, 500, 0);
					if(! block) {
						Prayer.smite(attack.getPlayer(), (hit / 2));
						if(Combat.random(2) == 0) {
							n.health += hit;
							n.cE.doGfx(398);
						}
					}
				} else
					return 0;
			}
			if(n.getDefinition().getId() == KARIL) {
				//karil
				if(distance < (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					int hit = Combat.random(19);
					Prayer.smite(attack.getPlayer(), (hit / 2));
					Combat.npcAttack(n, attack, hit, 700, 1);
					Combat.npcRangeAttack(n, attack, 28, 80, false);
				}
			}
			if(n.getDefinition().getId() == TORAG) {
				//torag
				if(distance < (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					int hit = Combat.random(25);
					Prayer.smite(attack.getPlayer(), (hit / 2));
					Combat.npcAttack(n, attack, hit, 500, 0);
				} else
					return 0;
			}
			if(n.getDefinition().getId() == VERAC) {
				//verac
				if(distance <= (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					int hit = Combat.random(25);
					Prayer.smite(attack.getPlayer(), (hit / 2));
					if(Combat.random(2) == 0)
						Combat.npcAttack(n, attack, hit, 500, 0, true);
					else
						Combat.npcAttack(n, attack, hit, 500, 0, false);
				} else
					return 0;
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
		int[] j = {2025, 2026, 2027, 2028, 2030,};
		return j;
	}
}
