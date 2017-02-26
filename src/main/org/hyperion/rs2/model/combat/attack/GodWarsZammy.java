package org.hyperion.rs2.model.combat.attack;

import org.hyperion.engine.task.Task;
import org.hyperion.map.WorldMap;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

public class GodWarsZammy implements Attack {

	public String getName() {
		return "GodWarsZammy";
	}

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}


			if(n.getDefinition().getId() == 6206) {
				//range
				if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
					return 0;
				n.cE.doAnim(n.getDefinition().getAtkEmote(0));
				n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
				Combat.npcAttack(n, attack, Combat.random(20), 1000, 1);
				Combat.npcRangeAttack(n, attack, 1209, 40, false);
			} else if(n.getDefinition().getId() == 6208) {
				int attackId = Combat.random(9);
				if(attackId != 0 && distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
					//melee
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					Combat.npcAttack(n, attack, Combat.random(18), 500, 0);
				} else {
					if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
						return 0;
					//magic
					n.cE.doAnim(n.getDefinition().getAtkEmote(1));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					//n.cE.doGfx(1212,0);
					Combat.npcAttack(n, attack, Combat.random(12), 1000, 2);
					Combat.npcRangeAttack(n, attack, 1213, 57, false);
				}
			} else if(n.getDefinition().getId() == 6204) {
				if(distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
					//melee
					n.cE.doAnim(n.getDefinition().getAtkEmote(0));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					Combat.npcAttack(n, attack, Combat.random(16), 500, 0);
					//Combat.npcRangeAttack(n,attack,1190,57);
				} else
					return 0;

			} else if(n.getDefinition().getId() == 6203) {
				int attackId = Combat.random(7);
				//melee
				if(attackId >= 1) {
					if(distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
						n.cE.doAnim(n.getDefinition().getAtkEmote(0));
						n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
						boolean prayerSmash = false;
						if(Combat.random(9) == 0)
							prayerSmash = true;
						if(! prayerSmash)
							Combat.npcAttack(n, attack, Combat.random(76), 500, 0, prayerSmash);
						else {
							Combat.npcAttack(n, attack, Combat.random(46), 500, 0, prayerSmash);
						}
					} else
						return 0;
				} else {
					if(! WorldMap.projectileClear(n.getPosition().getZ(), n.getDefinition().sizeX() + n.getPosition().getX(), n.getDefinition().sizeY() + n.getPosition().getY(), attack.getAbsX(), attack.getAbsY()))
						return 0;
					//mage
					n.cE.doAnim(n.getDefinition().getAtkEmote(1));
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					for(Player p : RegionManager.getLocalPlayers(n)) {
						int distance2 = p.getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
						if(distance2 <= 10) {
							Combat.npcAttack(n, p.cE, Combat.random(12), 1000, 2);
							//n.cE.doGfx(1212,0);
							Combat.npcRangeAttack(n, p.cE, 1213, 65, true);
						}
					}
					World.submit(new Task(1000) {
						@Override
						public void execute() {
							for(Player p : RegionManager.getLocalPlayers(n)) {
								int distance2 = p.getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
								if(distance2 <= 10) {
									p.cE.doGfx(1211, 0);
								}
							}
							this.stop();
						}
					});
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
		int[] j = {6208, 6204, 6206, 6203,};
		return j;
	}

}
