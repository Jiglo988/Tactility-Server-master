package org.hyperion.rs2.model.combat.attack;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.*;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.region.RegionManager;
import org.hyperion.util.Misc;


public class CorporealBeast implements Attack {

	@Override
	public String getName() {
		return "Corporeal Beast";
	}

	@Override
	public int[] npcIds() {
		return new int[]{8133};
	}
	/**
	 * 
	 * @param atk - damager
	 * @param damage - original damage
	 * @param style - style type, differentiate damage
	 * @return modified damage
	 */
	public static int reduceDamage(Player atk, int damage, int style) {
		Item weapon = atk.getEquipment().get(Equipment.SLOT_WEAPON);
		if(style == Constants.MELEE) {
            if(!CombatAssistant.isStab(weapon)) {
			    damage =  (int)(damage * (0.6 + (Math.random() * .4)));
			    if(Combat.random(10) == 1)
				    atk.getActionSender().sendMessage("Your weapon doesn't rip through the beast's tough skin!");
			    if(Rank.hasAbility(atk, Rank.ADMINISTRATOR)) {
				    atk.getActionSender().sendMessage("You loose damage because your weapon isn't a stab weapon!");
			    }
            } else {
                damage *= 1.2;
            }
		}
		else if(style == Constants.MAGE)
			damage = (int)(damage * Math.random());
		else if(style == Constants.RANGE) {
			if(damage > 50)
				damage = 50;
            if(Misc.random(3) == 0)
                atk.getActionSender().sendMessage("Your bolts bounce off the beast's tough skin!");

        }
		/**
		 * I want to make it so crush/slash aren't as effective as stab, but I can't find where it handles that
		 */
		if(atk.getEquipment().get(Equipment.SLOT_WEAPON) != null && !CombatAssistant.is2H(weapon.getDefinition().getId()) && style == Constants.MELEE) {
			int oldDamage = damage;
			damage = damage/2;
			if(Rank.hasAbility(atk, Rank.ADMINISTRATOR)) {
				atk.getActionSender().sendMessage("Your damage is halved "+oldDamage+" to "+damage);
			}
		}
		atk.increaseCorpDamage(damage);
		return damage;
	}
	
	private static final int MELEE_EMOTE = 10057, STOMP_EMOTE = 10496, STOMP_GFX = 1834;
	
	private static final int MELEE_EMOTE2 = 10058;
	
	private static final int RANGE_EMOTE = 10053;
	
	private static final int MAGE_EMOTE = 10410;
	
	private static final int maxMelee = 42, maxRange = 38, maxMage = 45;
	
	/**
	 * 
	 * @param n - corp
	 * @param attack - damager
	 * @param sendHome - true : out of bounds stomp home; false: in bounds under him stomp damage
	 */
	public static void stomp(NPC n, CombatEntity attack, boolean sendHome) {
		if(n == null)
			return;
		n.cE.doAnim(STOMP_EMOTE);
		n.cE.doGfx(STOMP_GFX);
		if(!sendHome) {
			attack.getPlayer().getActionSender().sendMessage("The corporeal beast stomps on you!");
			attack.hit(Combat.random(20)+40, n, false, Constants.MELEE);
		} else {
			attack.getPlayer().getActionSender().showInterface(12414);
			attack.getPlayer().setTeleportTarget(Position.create(3087, 3493, 0));
			attack.getPlayer().getActionSender().sendMessage("The corporeal beast stomps on you all the way back home...");
		}
		
	}
	@Override
	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance(n.getPosition());
		if(distance < (3 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
			if(n.cE.predictedAtk > System.currentTimeMillis()) {
				return 6;
			}
			int attackId = Combat.random(2);
			if(attackId == 0 && distance - ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2) > 2)
				attackId = 1 + Combat.random(1);
			if(attackId == 0) {
				//melee attack
				n.cE.doAnim(Combat.random(1) == 1 ? MELEE_EMOTE : MELEE_EMOTE2);
				//n.cE.doGfx(1886);
				n.cE.predictedAtk = (System.currentTimeMillis() + 2200);
				Combat.npcAttack(n, attack, CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(maxMelee),1 , maxMelee), 1200, 0);
			} else if(attackId == 1) {
				n.cE.doAnim(RANGE_EMOTE);
				n.cE.predictedAtk = (System.currentTimeMillis() + 2200);
				World.submit(new Task(1000) {
					@Override
					public void execute() {
						//range attack
						Combat.npcAttack(n, attack, CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(maxRange),1 , maxRange), 1100, 1);
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
						int min = 28;
						if(distance > 8) {
							timer += 2;
						} else if(distance >= 4) {
							timer++;
						}
						min -= (distance - 1) * 2;
						int speed = 40 - min;
						int slope = 3 + distance;
						//create the projectile
						//attack.getPlayerByName().getActionSender().createGlobalProjectile(n.cE.getAbsY() + n.cE.getOffsetY(), n.cE.getAbsX() + n.cE.getOffsetX(), offsetY, offsetX, 50, speed, 1824, 99, 35, hitId, slope);
						//attack.getPlayerByName().getActionSender().createGlobalProjectile(n.cE.getAbsY() + n.cE.getOffsetY(), n.cE.getAbsX() + n.cE.getOffsetX(), offsetY, offsetX, 50, speed + 10, 1824, 99, 35, hitId, slope);
						for(Player players : RegionManager.getLocalPlayers(n)) {
							//if(players.getLocation().distance(n.getLocation()) < 8) {
								distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
								speed = 75 - (distance - 1) * 2;
								slope = 3 + distance;
								CombatEntity atk = players.getCombat();
								atk.getPlayer().getActionSender().createGlobalProjectile(n.cE.getAbsY() + 3, n.cE.getAbsX() + 3, offsetY, offsetX, 35, speed - 15, 1824, 99, 35, hitId, slope);
								//atk.getPlayerByName().getActionSender().createGlobalProjectile(n.cE.getAbsY() + 3, n.cE.getAbsX() + 3, offsetY, offsetX, 35, speed, 1824, 99, 35, hitId, slope);
								Combat.npcAttack(n, atk, CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(maxMage), 2, maxMage), 1800, 2);
							//}
						}
						//attack.getPlayerByName().getActionSender().createGlobalProjectile(n.cE.getAbsY() + 3, n.cE.getAbsX() + 3, offsetY, offsetX, 35, speed, 1824, 99, 35, hitId, slope);
						//attack.getPlayerByName().getActionSender().createGlobalProjectile(casterY, casterX, offsetY, offsetX, angle, speed, gfxMoving, startHeight, endHeight, lockon, slope)
						//Combat.npcAttack(n, attack, Combat.random(maxMage), 1200, 2);
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

}
