package org.hyperion.rs2.model.combat.attack;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;

public class Ket implements Attack {

	public String getName() {
		return "Ket";
	}

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (15 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
			if(n.getDefinition().getId() == 2627) {
				//bats
				if(distance <= (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					final int hit = Combat.random(4);
					Combat.npcAttack(n, attack, hit, 500, 0);
					if(attack.getPlayer().getSkills().getLevel(5) > 0)
						attack.getPlayer().getSkills().detractLevel(5, (1 + hit));
					n.cE.doAnim(9232);
				} else
					return 0;
			} else if(n.getDefinition().getId() == 2630) {
				//level 45 blobs
				if(distance <= (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					Combat.npcAttack(n, attack, 8, 500, 0);
                    n.cE.doAnim(9233);
				} else
					return 0;
			} else if(n.getDefinition().getId() == 2738) {
				//level 22 blobs
				if(distance <= (1 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);
					Combat.npcAttack(n, attack, 4, 500, 0);
					n.cE.doAtkEmote();
				} else
					return 0;
			} else {
				//ket
				int attackId = Combat.random(1);
				if(attackId == 0 && distance <= (1 + (n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2)) {
					//melee
					n.cE.doAnim(9265);
					n.cE.predictedAtk = (System.currentTimeMillis() + 2000);
					Combat.npcAttack(n, attack, Combat.random(40), 500, 0);
				} else {
					//magic
					n.cE.doAnim(9266);
					n.cE.predictedAtk = (System.currentTimeMillis() + 3000);

					//offset values for the projectile
					int offsetY = ((n.cE.getAbsX() + n.cE.getOffsetX()) - attack.getAbsX()) * - 1;
					int offsetX = ((n.cE.getAbsY() + n.cE.getOffsetY()) - attack.getAbsY()) * - 1;
					//find our lockon target
					int hitId = attack.getSlotId(n);
					//extra variables - not for release
					int timer = 1;
					int min = 16;
					if(distance > 8) {
						timer += 2;
					} else if(distance >= 4) {
						timer++;
					}
					min -= (distance - 1) * 2;
					int speed = 75 - min;
					int slope = 7 + distance;
					//create the projectile
					attack.getPlayer().getActionSender().createGlobalProjectile(n.cE.getAbsY() + n.cE.getOffsetY(), n.cE.getAbsX() + n.cE.getOffsetX(), offsetY, offsetX, 50, speed, 445, 99, 35, hitId, slope);
					Combat.npcAttack(n, attack, Combat.random(23), 700, 2);
					World.submit(new Task(700) {
						@Override
						public void execute() {

							attack.doGfx(446);
							this.stop();
						}
					});
				}
			}
			return 5;
		} else if(n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 100)) {
			return 0;
		} else {
			return 0;
		}
	}

	@Override
	public int[] npcIds() {
		int[] j = {2743, 2744, 2627, 2630, 2738,};
		return j;
	}
}
