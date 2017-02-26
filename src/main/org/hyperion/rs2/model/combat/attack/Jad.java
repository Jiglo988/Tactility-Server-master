package org.hyperion.rs2.model.combat.attack;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;

public class Jad implements Attack {

	public String getName() {
		return "Jad";
	}

	public static final int DEATH_EMOTE = 9279;
	public static final int MAGE_EMOTE = 9300;
	public static final int RANGE_EMOTE = 9276;
	public static final int MELEE_EMOTE = 9277;

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (15 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				return 6;//we dont want to reset attack but just wait another 500ms or so...
			}
			int attackId = Combat.random(2);
			if(attackId == 0 && distance - ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2) > 1)
				attackId = 1 + Combat.random(1);
			//0 -is melee anim
			//1 is range anim
			//2 is mage anim

			/*
			 * REAL JAD ATTACKS

				441 = Jad mage attack(projectile First)
				442 = Jad mage attack(projectile Middle)
				453 = Jad mage attack(projectile Last)
				454 = jad rocks falling range attack
			*/
			if(attackId == 0) {
				//melee attack
				n.cE.doAnim(MELEE_EMOTE);
				n.cE.predictedAtk = (System.currentTimeMillis() + 2200);
				Combat.npcAttack(n, attack, Combat.random(96), 500, 0);
				attack.doGfx(451, 0);
			} else if(attackId == 1) {
				n.cE.doAnim(RANGE_EMOTE);
				n.cE.predictedAtk = (System.currentTimeMillis() + 2400);
				World.submit(new Task(1000) {
					@Override
					public void execute() {
						//range attack
						attack.doGfx(451, 0);
						Combat.npcAttack(n, attack, Combat.random(96), 1200, 1);
						this.stop();
					}
				});
			} else if(attackId == 2) {
				//mage attack
				n.cE.doAnim(MAGE_EMOTE);
				n.cE.predictedAtk = (System.currentTimeMillis() + 3300);
				World.submit(new Task(1500) {
					@Override
					public void execute() {
						//offset values for the projectile
						int offsetY = ((n.cE.getAbsX() + n.cE.getOffsetX()) - attack.getAbsX()) * - 1;
						int offsetX = ((n.cE.getAbsY() + n.cE.getOffsetY()) - attack.getAbsY()) * - 1;
						//find our lockon target
						int hitId = attack.getSlotId(n);
						//extra variables - not for release
						int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
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
						attack.getPlayer().getActionSender().createGlobalProjectile(n.cE.getAbsY() + n.cE.getOffsetY(), n.cE.getAbsX() + n.cE.getOffsetX(), offsetY, offsetX, 50, speed, 448, 99, 35, hitId, slope);
						attack.getPlayer().getActionSender().createGlobalProjectile(n.cE.getAbsY() + n.cE.getOffsetY(), n.cE.getAbsX() + n.cE.getOffsetX(), offsetY, offsetX, 50, speed + 10, 449, 99, 35, hitId, slope);
						attack.getPlayer().getActionSender().createGlobalProjectile(n.cE.getAbsY() + n.cE.getOffsetY(), n.cE.getAbsX() + n.cE.getOffsetX(), offsetY, offsetX, 50, speed + 20, 450, 99, 35, hitId, slope);
						Combat.npcAttack(n, attack, Combat.random(96), 800, 2);
						this.stop();
					}
				});
			}
			return 5;
		} else if(n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 100)) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int[] npcIds() {
		int[] j = {2745,};
		return j;
	}
}
