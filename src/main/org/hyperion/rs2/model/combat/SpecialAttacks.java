package org.hyperion.rs2.model.combat;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.weapons.SpecialWeapon;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.duel.DuelRule.DuelRules;
import org.hyperion.rs2.model.content.skill.Prayer;
import org.hyperion.rs2.model.shops.SlayerShop;
import org.hyperion.util.Misc;

import java.util.HashMap;
import java.util.Map;

public class SpecialAttacks {

	private static final Map<Integer, SpecialWeapon> specialWeapons = new HashMap<Integer, SpecialWeapon>();


	public static boolean clickedSpecialButton(Player player) {
		if(player.getEquipment().get(3) == null)
			return false;
		int weaponId = player.getEquipment().get(3).getId();
		switch(weaponId) {
			case 4153:
			case 17646:
				// maul
				if(player.cE.getOpponent() != null) {
					String message = Combat.canAtk(player.cE, player.cE.getOpponent());
					if(message.length() > 1) {
						return false;
					}
					player.specOn = false;
					// int maxHit = 0;
					int maxHit = CombatAssistant.calculateMaxHit(player);
					special(player, maxHit, weaponId, 0, Constants.MELEE);
				}
				break;
			case 1377:
				// dbaa TODO
				break;
		}
		return true;
	}

	public static int defence(final CombatEntity combatEntity, int maxDamg,
	                          int atkBonus) {
		if(combatEntity.getOpponent().getEntity() instanceof Player) {
			if(Combat.random(CombatAssistant.calculateMeleeAttack(combatEntity
					.getPlayer()) + atkBonus) < Combat.random(CombatAssistant
					.calculateMeleeDefence(combatEntity.getOpponent()
							.getPlayer())))
				return 0;
		} else {
			if(Combat.random(CombatAssistant.calculateMeleeAttack(combatEntity
					.getPlayer()) + atkBonus) < Combat
					.random(combatEntity.getOpponent().getNPC().getDefinition()
							.getBonus()[(combatEntity.getAtkType() + 2)]))
				return 0;
		}
		return maxDamg;
	}


	public static boolean special(final Player player,
	                              int maxDamg, final int weaponId, int currentdistance,
	                              int combatStyle) {
		CombatEntity combatEntity = player.getCombat();
		int distance = combatEntity.getEntity().getPosition().distance((combatEntity.getOpponent().getEntity().getPosition()));
		int playerGfx = - 1;
		int specialDis = - 1;
		int oppGfx = - 1;
		int specialDrain = - 1;
		double specialAccuracy = 1;
		boolean ranged = false;
		final int specialAnimation;
		int minimum = 5;

		switch(weaponId) {
			case 19780:
				specialAnimation = 4000;
				oppGfx = 1248;
				specialAccuracy = 1;
				specialDis = 1;
				specialDrain = 60;
				break;
			case 10858:
				specialAnimation = 4000;
				oppGfx = 383;
				specialAccuracy = 1;
				specialDis = 1;
				specialDrain = 50;
				break;
			case 13883:
				ranged = true;
				specialAnimation = 10504;
				playerGfx = 1836;
				specialAccuracy = 1.4;
				specialDrain = 50;
				specialDis = 6;
				break;
			case 13879:
				ranged = true;
				specialAnimation = 10501;
				playerGfx = 1838;
				specialAccuracy = 1.2;
				specialDrain = 50;
				specialDis = 6;
				break;
			case 14484:
				specialAnimation = 10961;
				specialDis = 1;
				specialDrain = 50;
				specialAccuracy = 1.6;
				break;
			case 5698:
				playerGfx = 252;
				specialAnimation = 1062;
				specialDis = 1;
				specialDrain = 25;
				specialAccuracy = 1.25;
				break;
			case 1215:
				playerGfx = 252;
				specialAnimation = 1062;
				specialDis = 1;
				specialDrain = 25;
				specialAccuracy = 1.25;
				break;
			case 1231:
				playerGfx = 252;
				specialAnimation = 1062;
				specialDis = 1;
				specialDrain = 25;
                specialAccuracy = 1.255;
                break;
			case 5680:
				playerGfx = 252;
				specialAnimation = 1062;
				specialDis = 1;
				specialDrain = 25;
                specialAccuracy = 1.25;
				break;
			case 859:
				playerGfx = -1;
				specialAnimation = 426;
				specialDis = 9;
				specialDrain = 55;
				minimum = 10;
				break;
			case 861:
				playerGfx = 256;
				specialAnimation = 426;
				specialDis = 9;
				specialDrain = 55;
				minimum = 10;
				break;
			case 3204:
				playerGfx = 282;
				specialAnimation = 440;
				specialDis = 2;
				specialDrain = 30;
				break;
			case 4587:
				playerGfx = 2117;
				specialAnimation = 12031;
				specialDis = 1;
				specialDrain = 55;
                specialAccuracy = 1.4;
				break;
			case 4151:
				oppGfx = 341;
				specialAnimation = 1658;
				specialDis = 1;
				specialDrain = 50;
				break;
            case 1434:
                playerGfx = 251;
                specialAnimation = 1060;
                specialDis = 1;
                specialDrain = 25;
                specialAccuracy = 1.45;
                break;
            case 11061:
                specialAnimation = 6147;
                specialDis = 1;
                specialDrain = 100;
                break;
            case 17640:
                specialAnimation = 6147;
                specialDis = 1;
                specialDrain = 75;
                specialAccuracy = 1.55;
                break;
            case 10887:
				specialAnimation = 5870;
				playerGfx = 1027;
				specialDis = 1;
				specialDrain = 50;
                specialAccuracy = 1.4;
				break;
			case 1249:
				playerGfx = 253;
				specialAnimation = 1667;
				specialDis = 1;
				specialDrain = 25;
				break;
			case 1263:
				playerGfx = 253;
				specialAnimation = 1667;
				specialDis = 1;
				specialDrain = 75;
				break;
			case 5716:
				playerGfx = 253;
				specialAnimation = 1667;
				specialDis = 1;
				specialDrain = 75;
				break;
			case 5730:
				playerGfx = 253;
				specialAnimation = 1667;
				specialDis = 1;
				specialDrain = 50;
				break;
			case 4153:  //gmauls
				playerGfx = 340;
				specialAnimation = 1667;
				specialDis = 1;
                if(combatEntity.getOpponent() != null)
                    specialAccuracy = Math.pow(CombatAssistant.calculateMeleeDefence(combatEntity.getOpponent().getEntity()), 0.135) - 1.1;
				specialDrain = 50;
                break;
            case 17646:
				playerGfx = 340;
                specialAnimation = 1667;
                specialDis = 1;
                if(combatEntity.getOpponent() != null)
                    specialAccuracy = Math.pow(CombatAssistant.calculateMeleeDefence(combatEntity.getOpponent().getEntity()), 0.09) - 1.0;
                specialDrain = 34;
                break;
			case 7158:
				playerGfx = 559;
				specialAnimation = 3157;
				specialDis = 1;
				specialDrain = 60;
				break;
			case 1305:
				playerGfx = 2117;
				specialAnimation = 12033;
				specialDis = 1;
				specialDrain = 25;
                specialAccuracy = 1.45;
				break;
            case 19605:
                playerGfx = 1222;
                specialAnimation = 7074;
                specialDis = 1;
                specialDrain = 50;
                specialAccuracy = 1.40;
                break;
			case 11694:
				playerGfx = 1222;
				specialAnimation = 7074;
				specialDis = 1;
				specialDrain = 50;
				specialAccuracy = 1.3;
				break;
			case 11698:
				playerGfx = 1220;
				specialAnimation = 7071;
				specialDis = 1;
				specialDrain = 50;
				break;
			case 11696:
				playerGfx = 1223;
				specialAnimation = 7073;
				specialDis = 1;
				specialDrain = 65;
				break;
			case 11700:
				playerGfx = 1221;
				specialAnimation = 7070;
				specialDis = 1;
				specialDrain = 75;
				break;
			case 11730:
				oppGfx = 1194;
				specialAnimation = 7072;
				specialDis = 1;
				specialDrain = 100;
				specialAccuracy = 1.1;
				break;
			case 13899:
				specialAnimation = 10502;
				specialDis = 1;
				specialDrain = 25;
                specialAccuracy = 1.22;
				break;
			case 13902:
				playerGfx = 1840;
				specialAnimation = 10505;
				specialDis = 1;
				specialDrain = 35;
				break;
			case 15241:
				playerGfx = 2141;
				specialAnimation = player.getCombat().getAtkEmote();
				specialDis = 9;
				specialDrain = 50;
				specialAccuracy = 1.15;
				player.getCombat().predictedAtk = System.currentTimeMillis() + 700;
				ranged = true;
				break;
            case 14684:
                playerGfx = 2010;
                specialAnimation = player.getCombat().getAtkEmote();
                specialDis = 9;
                specialDrain = 50;
                specialAccuracy = 1.25;
                maxDamg *= 1.25;
                ranged = true;
                break;
			case 11235:
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				int bowType = CombatAssistant.getCombatStyle(player.cE);
				if(bowType <= Constants.NOAMMO) {
					player.getActionSender()
							.sendMessage("You have no arrows left in your quiver.");
				}
				playerGfx = - 1;
				specialAnimation = 426;
				specialDis = 9;
				specialDrain = 60;
				minimum = 5;
				if(player.getEquipment()
						.get(Equipment.SLOT_ARROWS).getId() == 11212) {
					maxDamg += maxDamg * 0.05;// 50% for d arrows
					minimum += 3;
                    specialAccuracy = 1.2;
				}
				break;
			default:
				return false;
		}

		if(ranged && (player.getPosition().disabledRange() || player.duelRule[DuelRules.RANGE.ordinal()])) {
			player.getActionSender().sendMessage("You cannot used ranged weapons here!");
			return false;
		}
	    /*
		 * If opponent is too far, don't attack yet.
		 */
		if(! player.getPosition().isWithinDistance(player.cE.getOpponent().getEntity().getPosition(),
				(specialDis + (player.cE.canMove() ? 1 : 0)))) {
			player.specOn = true;
			if(player.cE.getOpponent().getEntity() instanceof Player)
				player.getActionSender().follow(player.cE.getOpponent().getEntity().getIndex(),
						1);
			return false;// too far away
		}
		/*
		 * Vigour happens before check
		 */
		if(player.getEquipment().get(Equipment.SLOT_RING) != null)
			if(player.getEquipment().get(Equipment.SLOT_RING).getId() == 19669)
				specialDrain = (int)(specialDrain - (specialDrain * .1));
        if(player.getEquipment().get(Equipment.SLOT_RING) != null)
            if(player.getEquipment().get(Equipment.SLOT_RING).getId() == 17660)
                specialDrain = (int)(specialDrain - (specialDrain * .17));
		/*
		 * If you don't have enough special energy, reset.
		 */
		if(player.getSpecBar().getAmount() < specialDrain) {
			player.getActionSender().sendMessage(
					"You dont have enough special energy to do this attack.");
			player.cE.setOpponent(null);
			return false;
		}
		/*
		 * Determine Hit Damage
		 */
		int atkBonus = 20;
		int tempDamage = minimum + Misc.random(maxDamg-minimum);
		if(tempDamage > maxDamg) {
			tempDamage = maxDamg;
		}
		int deltaBonus;
        if(player.cE.getOpponent().getEntity() instanceof Player) {
			if(! ranged)
				deltaBonus = (int)(CombatAssistant.calculateMeleeAttack(player) * specialAccuracy)
						- CombatAssistant.calculateMeleeDefence(player.cE
						.getOpponent().getPlayer());
			else
				deltaBonus = (int)(CombatAssistant.calculateRangeAttack(player) * specialAccuracy)
						- CombatAssistant.calculateRangeDefence(player.cE
						.getOpponent().getPlayer());

		} else {
            if(!ranged)
			    deltaBonus = (int)(CombatAssistant.calculateMeleeAttack(player) * specialAccuracy)
					- (int)((player.cE.getOpponent().getCombat()/1.6) + 64);
            else
                deltaBonus = (int)(CombatAssistant.calculateRangeAttack(player) * specialAccuracy)
                        - (int)(player.cE.getOpponent().getCombat()/1.5);
		}

        int randomIncrease = Misc.random(deltaBonus/3);

        // System.out.println("RandomIncrease " + randomIncrease +
		// " Deltabonus : " + deltaBonus);
		tempDamage += randomIncrease;
		if(tempDamage < 0)
			tempDamage = 0;
		else if(tempDamage > maxDamg)
			tempDamage = maxDamg;
        if(weaponId != 14684)
		    tempDamage = SpiritShields.applyEffects(player.cE.getOpponent(), tempDamage);


		/*
		 * Determine if damage is critical..
		 */

        Prayer.zaniksEffect(combatEntity.getPlayer(), tempDamage);

        final CombatEntity oldEntity = player.getCombat().getOpponent();
        if(oldEntity.getEntity() instanceof Player)
            tempDamage = oldEntity.getPlayer().getInflictDamage(tempDamage, player, false, combatStyle);
        else {
            if(SlayerShop.hasHelm(player) && !ranged && player.getSlayer().isTask(oldEntity.getNPC().getDefinition().getId())) {
                tempDamage *= 1.15;
            } else if (player.getSlayer().isTask(oldEntity.getNPC().getDefinition().getId()) && SlayerShop.hasFocus(player)) {
                tempDamage *= 1.15;
            }
        }
		/*
		 * For cool banners.
		 */
		if(player.getName().equalsIgnoreCase("graham")) {
			tempDamage *= 1.5;
			if(tempDamage > maxDamg)
				tempDamage = maxDamg;
		}
		final int hitDamage = tempDamage;
		int critical = hitDamage > maxDamg * 0.9 ? 5 : 0;
		/*
		 * hitDamage will be Applied on Opponent..
		 */
		player.cE.doAnim(specialAnimation);
		if(playerGfx > 0 && weaponId != 861 && weaponId != 15241) {
			player.cE.doGfx(playerGfx);
		} else if(weaponId == 15241) {
			player.playGraphics(Graphic.create(playerGfx));
		}
		if(oppGfx > 0)
			player.cE.getOpponent().doGfx(oppGfx);
		player.getSpecBar().decrease(specialDrain);
		player.getSpecBar().sendSpecBar();

		if(player.cE.getWeaponPoison() > 0
				&& Combat.random(10) <= player.cE.getWeaponPoison()
				&& ! player.cE.getOpponent().isPoisoned())
			Combat.poisonEntity(player.cE.getOpponent());

		if(player.cE.getOpponent().getCurrentAtker() == null
				|| player.cE.getOpponent().getCurrentAtker() == player.cE) {
			player.cE.getOpponent().face(player.cE.getAbsX(),
					player.cE.getAbsY());
			if(player.cE.getOpponent().getEntity() instanceof Player
					|| player.cE.getOpponent().getNPC().getDefinition()
					.doesDefEmote())
				player.cE.getOpponent().doDefEmote();
			if(player.cE.getOpponent().getEntity() instanceof NPC
					|| player.cE.getOpponent().getPlayer().autoRetailate) {
				player.cE.getOpponent().setOpponent(player.cE);
			}
		}

        player.getExtraData().put("lastspecialatk", System.currentTimeMillis());
		/**
		 * Apply damage
		 */

		ApplyHitBlock:{
            if(oldEntity.getEntity() instanceof Player)
                oldEntity.getPlayer().getLastAttack().updateLastAttacker(player.getName());
			switch(weaponId) {
			case 11235:
			case 15701:
			case 15702:
			case 15703:
			case 15704:
			case 19780:
			case 10858:
			case 5730:
				break ApplyHitBlock;
			}
			Combat.addXP(player, hitDamage, ranged);
			int delay = 300 + distance * 200;
			final int cbStyle = combatStyle;
			final int crit = critical;
            if(delayedWeapon(weaponId)) {
			World.submit(new org.hyperion.engine.task.Task(delay, "combat") {
				public void execute() {
					if(player.getPrayers().isEnabled(48))
						Prayer.soulSplit(player, oldEntity, hitDamage);
                    if(oldEntity != null)
					    oldEntity.hit(hitDamage, player, false, cbStyle + crit);
                    if(oldEntity.getEntity() instanceof Player) {
                        Magic.vengeance(oldEntity.getPlayer(),
                                player.cE, hitDamage);
                    }
					this.stop();
				}
			});
			} else {
				player.cE.getOpponent().hit(hitDamage, player,
						false, cbStyle + crit);
                if(oldEntity.getEntity() instanceof Player)
                    Magic.vengeance(oldEntity.getPlayer(),
                        player.cE, hitDamage);
			}

		}

		// this.stop();
		// }
		// });
		switch(weaponId) {
		case 5730:
			if(player.cE.getOpponent().getEntity() instanceof Player)  {
                final Player opp = player.cE.getOpponent().getPlayer();
				opp.vacateSquare();
                opp.playGraphics(Graphic.create(245, 6553600));
                opp.cE.setFreezeTimer(2000);
                opp.foodTimer = System.currentTimeMillis();
                opp.getExtraData().put("stuntimez", System.currentTimeMillis() + 2000L);
            }
		break;
			// dark bows
			case 13879:
                final int amount = player.getEquipment().getCount(13879);
                player.getEquipment().set(Equipment.SLOT_WEAPON, Item.create(player.getEquipment().getItemId(Equipment.SLOT_WEAPON), amount - 1));
				final CombatEntity opp = player.cE.getOpponent();
				if(opp == null)
					break;
				if(opp.morrigansLeft > 0) {
					opp.morrigansLeft += hitDamage / 5;
					break;
				}
				opp.morrigansLeft = hitDamage / 5;
				World.submit(new org.hyperion.engine.task.Task(1000,"special attacks") {
					public void execute() {
						opp.morrigansLeft--;
						if(opp.morrigansLeft <= 0) {
							this.stop();
						}
						if(!player.isDead())
						opp.hit(5, player, false, Constants.RANGE);
					}
				});
				break;

			case 11235:
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				if(player.getPosition().disabledRange())
					return false;
				// ContentEntity.startAnimation(combatEntity.getPlayerByName(), 426);
				int clientSpeed;
				int showDelay;
				int slope;
				if(currentdistance <= 1) {
					clientSpeed = 55;
				} else if(currentdistance <= 3) {
					clientSpeed = 55;
				} else if(currentdistance <= 8) {
					clientSpeed = 65;
				} else {
					clientSpeed = 75;
				}
				showDelay = 45;
				slope = 15;
				clientSpeed += 30;
				int deltaBonus2 = -5;
				int deltaBonus3 = -5;
				if(player.getCombat().getOpponent() != null && player.getCombat().getOpponent().getEntity() instanceof Player) {
					deltaBonus2 = CombatAssistant.calculateRangeAttack(player) -
						CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer());
					deltaBonus3 = CombatAssistant.calculateRangeAttack(player) -
							CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer());
					player.debugMessage("ur range atk is: "+CombatAssistant.calculateRangeAttack(player));
					player.debugMessage("opp range def is: "+CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer()));
				}

				int toAddFirst = Misc.random(deltaBonus2 / 3);
				int toAddSecond = Misc.random(deltaBonus3 / 3);
				int offsetY = ((player.cE.getAbsX()) - player.cE
						.getOpponent().getAbsX()) * - 1;
				int offsetX = ((player.cE.getAbsY()) - player.cE
						.getOpponent().getAbsY()) * - 1;
				int hitId = player.cE.getSlotId(player);
				// attacker.playProjectile(Projectile.create(attacker.getLocation(),
				// victim.getCentreLocation(),
				// attacker.getEquipment().get(Equipment.SLOT_ARROWS).getId() ==
				// 11212 ? 1099 : 1101, showDelay, 50, clientSpeed - 10, 41, 31,
				// victim.getProjectileLockonIndex(), 3, 36));
				// attacker.playProjectile(Projectile.create(attacker.getLocation(),
				// victim.getCentreLocation(),
				// attacker.getEquipment().get(Equipment.SLOT_ARROWS).getId() ==
				// 11212 ? 1099 : 1101, showDelay, 50, clientSpeed + 10, 46, 36,
				// victim.getProjectileLockonIndex(), slope + 6, 36));
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed - 10,
								player.getEquipment()
										.get(Equipment.SLOT_ARROWS).getId() == 11212 ? 1099
										: 1102, 46, 31, hitId, 3);
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed + 10,
								player.getEquipment()
										.get(Equipment.SLOT_ARROWS).getId() == 11212 ? 1099
										: 1102, 46, 36, hitId, slope + 6);
				int damg4 = Combat.random(maxDamg);
				int damg8 = Combat.random(maxDamg);
				damg4 = damg4 + toAddFirst;
				damg8 = damg8 + toAddSecond;
				if(damg4 <= minimum)
					damg4 = minimum;
				if(damg4 > maxDamg)
					damg4 = maxDamg;
				if(damg8 <= minimum)
					damg8 = minimum;
				if(damg8 > maxDamg)
					damg8 = maxDamg;
				damg4 = SpiritShields.applyEffects(player.cE.getOpponent(), damg4);
				damg8 = SpiritShields.applyEffects(player.cE.getOpponent(), damg8);
                if(oldEntity.getEntity() instanceof Player) {
                    damg4 = oldEntity.getPlayer().getInflictDamage(damg4, player, false, combatStyle);
                    damg8 = oldEntity.getPlayer().getInflictDamage(damg8, player, false, combatStyle);
                }
                Combat.addXP(player, damg4, true);
                Combat.addXP(player, damg8, true);
                final int hit1 = damg4;
                final int hit2 = damg8;
                final CombatEntity entity =player.cE.getOpponent();
                if(entity != null) {
                    World.submit(new org.hyperion.engine.task.Task(200 * distance + 300, "combat") {
                        public void execute() {
                            entity.hit(hit1, player, false, 1);
                            entity.hit(hit2, player, false, 1);
                            if(entity.getEntity() instanceof Player)
                                Magic.vengeance(oldEntity.getPlayer(),
                                    player.cE, hit1);
                            this.stop();
                        }
                    });

                    entity
                            .getEntity()
                            .playGraphics(
                                    Graphic.create(player
                                                    .getEquipment().get(Equipment.SLOT_ARROWS)
                                                    .getId() == 11212 ? 1100 : 1103,
                                            6553600 + clientSpeed));
                }
				break;

            case 11696:
            case 13902:

                if(player.getCombat().getOpponent() != null) {
                    player.getCombat().getOpponent()._getPlayer().ifPresent(p -> {
                        int lvl = p.getSkills().getLevel(1);
                        int remove = weaponId == 13902 ? hitDamage/2 : hitDamage;
                        int toRemove = lvl - remove < 1 ? lvl - 1 : remove;
                        p.getSkills().setLevel(1, lvl - toRemove);
                        p.getExtraData().put("ovlreset1", System.currentTimeMillis());
                        p.sendMessage("@red@Your defence has been lowered and divine effects disabled for the next 10 seconds!");
                    });
                    player.sendMessage("@red@Your opponent's overload and divine have been disabled for 15 seconds!");
                }

                break;

			case 10858:
				try {
					maxDamg = (int)(maxDamg * 1.5);
					int dmg = Misc.random(maxDamg);
					if(dmg < maxDamg/2)
						dmg = maxDamg/2;
					if(player.getPrayers().isEnabled(48))
						Prayer.soulSplit(player,
								player.cE.getOpponent(), dmg);
					if(player.cE.getOpponent().getEntity() instanceof Player) {
						Magic.vengeance(player.cE.getOpponent().getPlayer(),
								player.cE, dmg);
						dmg = player.cE.getOpponent().getPlayer().getInflictDamage(dmg, player, false, Constants.MAGE);
					}
					int crit1 = 0;
					if(dmg > 1.20 * maxDamg)
						crit1 = 5;
					dmg = SpiritShields.applyEffects(player.cE.getOpponent(), dmg);
					player.cE.getOpponent().hit(dmg, player, false, Constants.MAGE + crit1);
					if(player.cE.getOpponent().getEntity() instanceof Player) {
						Player opponent = player.cE.getOpponent().getPlayer();
						opponent.getSkills().detractLevel(Skills.DEFENCE, hitDamage);
					}
				} catch (Exception e){
					e.printStackTrace();
				}
				break;

			case 19780:
				maxDamg = (int)(maxDamg * 1.5);
				int dmg = Misc.random(maxDamg);
				if(dmg < maxDamg/2)
					dmg = maxDamg/2;
				if(player.getPrayers().isEnabled(48))
					Prayer.soulSplit(player,
							player.cE.getOpponent(), dmg);
				if(player.cE.getOpponent().getEntity() instanceof Player) {
					Magic.vengeance(player.cE.getOpponent().getPlayer(),
							player.cE, dmg);
                    dmg = player.cE.getOpponent().getPlayer().getInflictDamage(dmg, player, false, Constants.MAGE);
				}
                if(oldEntity.getEntity() instanceof Player) {
                }
				int crit1 = 0;
				if(dmg > 1.20 * maxDamg)
					crit1 = 5;
				dmg = SpiritShields.applyEffects(player.cE.getOpponent(), dmg);
				player.cE.getOpponent().hit(dmg, player, false, Constants.MAGE + crit1);
				break;
			case 5698:// dds
			case 1215:
			case 1231:
			case 5680:
                /**
                 * Determine Hit Damage
                 */

                tempDamage = Misc.random(maxDamg);
                deltaBonus = (int)(CombatAssistant.calculateMeleeAttack(player) * specialAccuracy)
                            - CombatAssistant.calculateMeleeDefence(oldEntity.getEntity());

                randomIncrease = Misc.random(deltaBonus / 3);
				/*
                if(Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
                    player.getActionSender().sendMessage("Delta bonus: " + deltaBonus);
                }
                */
                // System.out.println("RandomIncrease " + randomIncrease +
                // " Deltabonus : " + deltaBonus);
                tempDamage += randomIncrease;
                if(tempDamage < 0)
                    tempDamage = 0;
                else if(tempDamage > maxDamg)
                    tempDamage = maxDamg;
                tempDamage = SpiritShields.applyEffects(player.cE.getOpponent(), tempDamage);
                if(oldEntity.getEntity() instanceof Player) {
                    tempDamage = oldEntity.getPlayer().getInflictDamage(tempDamage, player, false, combatStyle);
                }
                /**
                 * Determine if damage is critical..
                 */

                final int damg5 = tempDamage;
                final int crit = damg5 > 0.9 * maxDamg ? 5 : 0;

                int delay = 300 + distance * 200;
                Combat.addXP(player, damg5, false);

                World.submit(new org.hyperion.engine.task.Task(delay, "combat") {
                    @Override
                    public void execute() {
                        oldEntity.hit(damg5, player,
                                false, crit);
                        oldEntity._getPlayer().ifPresent(p -> Magic.vengeance(p,player.cE, hitDamage));
                        this.stop();
                    }
                });

				break;
			case 11730:
				int damage = Misc.random(16);
				player.cE.getOpponent().hit(damage, player,
						false, Constants.MAGE);
				Combat.addXP(player, damage, false);
				break;
            case 11700:
                if(hitDamage > 0) {
                    player.cE.getOpponent().doGfx(369, 0);
                    if(player.getCombat().canBeFrozen())
                        player.cE.getOpponent().setFreezeTimer(20000);
                }
                break;
            case 11061:
            case 17640:
               // if(Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
                    //player.getActionSender().sendMessage("Damage: "+hitDamage);
                    try {
                        if(player.cE.getOpponent().getEntity() instanceof Player) {
                            Player opponent = player.cE.getOpponent().getPlayer();
                            opponent.getSkills().detractLevel(Skills.PRAYER, hitDamage);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                //} @ff66ff@
                break;
			case 14484:
				player.cE.doGfx(1950, 0);
				if(hitDamage > 0) {
                    player.cE.getOpponent().hit(hitDamage /2,
                            player, false, 0);
					World.submit(new org.hyperion.engine.task.Task(1000,"specialattacks3") {
						@Override
						public void execute() {
							String message = Combat.canAtk(player.cE, oldEntity);
							if(message.length() > 1) {
								this.stop();
								return;
							}

						    oldEntity.hit(hitDamage / 4,
									player, false, 0);
							oldEntity.hit(
									hitDamage / 4 + 1,
									player, false, 0);
							this.stop();
						}
					});
				} else {
					int notPrayDamg = SpiritShields.applyEffects(player.cE.getOpponent(), CombatCalculation.getCalculatedDamage(player, player.cE.getOpponent().getEntity(), Combat.random(maxDamg), 0, maxDamg));
                    if(oldEntity.getEntity() instanceof Player)
                        notPrayDamg = oldEntity.getPlayer().getInflictDamage(notPrayDamg, player, false, Constants.MELEE);
                    final int maxDamg3 = notPrayDamg;
					oldEntity.hit(maxDamg3,
							player, false, 0);
                    final int newMaxDamage = (int)(maxDamg * 1.5);
					World.submit(new org.hyperion.engine.task.Task(1000,"specialattacks2") {
						@Override
						public void execute() {
							String message = Combat.canAtk(player.cE, oldEntity);
							if(message.length() > 1) {
								this.stop();
								return;
							}
							if(maxDamg3 <= 0) {
								int maxDamg4 = CombatCalculation.getCalculatedDamage(player, oldEntity.getEntity(), Combat.random(newMaxDamage), 0,newMaxDamage);
								maxDamg4 = SpiritShields.applyEffects(oldEntity, maxDamg4);
                                if(oldEntity.getEntity() instanceof Player)
                                    maxDamg4 = oldEntity.getPlayer().getInflictDamage(maxDamg4, player, false, Constants.MELEE);
								oldEntity.hit(maxDamg4,
										player, false, 0);
								int maxDamg5 = SpiritShields.applyEffects(player.cE.getOpponent(), CombatCalculation.getCalculatedDamage(player, oldEntity.getEntity(), Combat.random((int) (newMaxDamage * 1.3)), 0, (int) (newMaxDamage * 1.3)));
								if (oldEntity.getEntity() instanceof Player)
									maxDamg5 = oldEntity.getPlayer().getInflictDamage(maxDamg5, player, false, Constants.MELEE);
								if(maxDamg4 == 0)
									oldEntity.hit(maxDamg5,
											player, false, 0);
								else
									oldEntity.hit(
											maxDamg4 / 2,
											player, false, 0);
							} else {
								oldEntity.hit(
										maxDamg3 / 2,
										player, false, 0);
								oldEntity.hit(
										maxDamg3 / 2,
										player, false, 0);
							}
							this.stop();
						}
					});
				}
				break;
			case 4587:
				if(hitDamage > 0 && player != null && player.cE != null &&  player.cE.getOpponent() != null) {
					if(player.cE.getOpponent().getEntity() instanceof Player) {
						Player prayerOff = player.cE.getOpponent().getPlayer();
						if(prayerOff != null) {
							Prayer.dragonScimitar(prayerOff);
						}
					}
				}
				break;
			/**case 15241: // HandCannon
				if(player.getLocation().disabledRange())
					return false;
				World.submit(new Event(1000) {
					@Override
					public void execute() {
						try {
							if(player.cE == null || player.cE.getOpponent() == null) {
								this.stop();
								return;
							}
							/**
							 * Another verification check incase of glitchers.
							 */
							/**String message = Combat.canAttack(player.cE, player.cE.getOpponent());
							if(message.length() > 1) {
								player.getActionSender().sendMessage(message);
								this.stop();
								return;
							}
							int arrowType = CombatAssistant.getArrowType(0);
							int maxHit = CombatAssistant.calculateRangeMaxHit(player);
							player.cE.doAtkEmote();

							// System.out.println("range max: "+maxHit);

							/**
							 * Sending Projectile part.
							 */
							//CombatAssistant.fireProjectile(player.cE, Constants.UNIQUEAMMO, arrowType);
							/**
							 * Set Attack Speed
							 */
							//player.cE.predictedAtk = (System.currentTimeMillis() + player.cE.getAtkSpeed());
							// drawback gfx
							//CombatAssistant.drawBackGfx(player.cE, weaponId, 15243, Constants.UNIQUEAMMO);
							/**
							 * Get random Damage Hit
							 */
							/*int damg = Misc.random(maxHit);
							/**
							 * Checks Range bonus etc
							 */
							/*int rangeAtk = CombatAssistant.calculateRangeAttack(player);
							int rangeDef;
							if(player.cE.getOpponent().getEntity() instanceof Player)
								rangeDef = CombatAssistant.calculateRangeDefence(player.cE.getOpponent().getPlayerByName());
							else
								rangeDef = player.cE.getOpponent().getCombat() / 2;
							int deltaRangeBonus = rangeAtk - rangeDef;
							int toadd = Misc.random(deltaRangeBonus / 20);
							//System.out.println("Toadd is " + toadd);
							damg += toadd;
							if(damg < 0)
								damg = 0;
							else if(damg > maxHit)
								damg = maxHit;

							/**
							 * Applies Damage.
							 */
							/*int critical = damg >= 0.90 * maxHit ? 5 : 0; // Later substract 5
							int actualDamage = player.cE.getOpponent().hit(damg, player, false, Constants.RANGE
									+ critical);

						} catch(Exception e) {
							this.stop();
							return;
						}
						this.stop();
					}
				});
				break;**/
			case 859:
				if(!Rank.hasAbility(player, Rank.DEVELOPER) || player.getPosition().disabledRange())
					return false;
				// ContentEntity.startAnimation(combatEntity.getPlayerByName(), 426);
				if(currentdistance <= 1) {
					clientSpeed = 55;
				} else if(currentdistance <= 3) {
					clientSpeed = 55;
				} else if(currentdistance <= 8) {
					clientSpeed = 65;
				} else {
					clientSpeed = 75;
				}
				showDelay = 45;
				slope = 15;
				clientSpeed += 30;
				deltaBonus2 = -5;
				deltaBonus3 = -5;
				if(player.getCombat().getOpponent() != null && player.getCombat().getOpponent().getEntity() instanceof Player) {
					deltaBonus2 = CombatAssistant.calculateRangeAttack(player) -
							CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer());
					deltaBonus3 = CombatAssistant.calculateRangeAttack(player) -
							CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer());
					player.debugMessage("ur range atk is: "+CombatAssistant.calculateRangeAttack(player));
					player.debugMessage("opp range def is: "+CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer()));
				}

				toAddFirst = Misc.random(deltaBonus2 / 3);
				toAddSecond = Misc.random(deltaBonus3 / 3);
				offsetY = ((player.cE.getAbsX()) - player.cE
						.getOpponent().getAbsX()) * - 1;
				offsetX = ((player.cE.getAbsY()) - player.cE
						.getOpponent().getAbsY()) * - 1;
				hitId = player.cE.getSlotId(player);
				// attacker.playProjectile(Projectile.create(attacker.getLocation(),
				// victim.getCentreLocation(),
				// attacker.getEquipment().get(Equipment.SLOT_ARROWS).getId() ==
				// 11212 ? 1099 : 1101, showDelay, 50, clientSpeed - 10, 41, 31,
				// victim.getProjectileLockonIndex(), 3, 36));
				// attacker.playProjectile(Projectile.create(attacker.getLocation(),
				// victim.getCentreLocation(),
				// attacker.getEquipment().get(Equipment.SLOT_ARROWS).getId() ==
				// 11212 ? 1099 : 1101, showDelay, 50, clientSpeed + 10, 46, 36,
				// victim.getProjectileLockonIndex(), slope + 6, 36));
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed - 10,
								1099, 46, 45, hitId, 3);
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed + 10,
								256, 46, 40, hitId, 6);
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed - 10,
								1099, 46, 35, hitId, 9);
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed + 10,
								256, 46, 30, hitId, 12);
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed - 10,
								1099, 46, 25, hitId, 15);
				damg4 = Combat.random(maxDamg);
				damg8 = Combat.random(maxDamg);
				damg4 = damg4 + toAddFirst;
				damg8 = damg8 + toAddSecond;
				if(damg4 <= minimum)
					damg4 = minimum;
				if(damg4 > maxDamg)
					damg4 = maxDamg;
				if(damg8 <= minimum)
					damg8 = minimum;
				if(damg8 > maxDamg)
					damg8 = maxDamg;
				damg4 = SpiritShields.applyEffects(player.cE.getOpponent(), damg4);
				damg8 = SpiritShields.applyEffects(player.cE.getOpponent(), damg8);
				if(oldEntity.getEntity() instanceof Player) {
					damg4 = oldEntity.getPlayer().getInflictDamage(damg4, player, false, combatStyle);
					damg8 = oldEntity.getPlayer().getInflictDamage(damg8, player, false, combatStyle);
				}
				final int hit1tt = damg4;
				final int hit2tt = damg8;
				final int hit3tt = Math.min(damg4, damg8);
				final int hit4tt = hit3tt/2;
				final int hit5tt = hit4tt/2;
				Combat.addXP(player, hit1tt, true);
				Combat.addXP(player, hit2tt, true);
				Combat.addXP(player, hit3tt, true);
				Combat.addXP(player, hit3tt, true);
				Combat.addXP(player, hit3tt, true);
				final CombatEntity entitytt =player.cE.getOpponent();
				if(entitytt != null) {
					World.submit(new org.hyperion.engine.task.Task(200 * distance + 300, "combat") {
						public void execute() {
							entitytt.hit(hit1tt, player, false, 1);
							entitytt.hit(hit2tt, player, false, 1);
							entitytt.hit(hit3tt, player, false, 1);
							entitytt.hit(hit4tt, player, false, 1);
							entitytt.hit(hit5tt, player, false, 1);
							if(entitytt.getEntity() instanceof Player)
								Magic.vengeance(oldEntity.getPlayer(),
										player.cE, hit1tt);
							this.stop();
						}
					});

					entitytt
							.getEntity()
							.playGraphics(
									Graphic.create(player
													.getEquipment().get(Equipment.SLOT_ARROWS)
													.getId() == 11212 ? 1100 : 1103,
											6553600 + clientSpeed));
				}
				break;
			case 861:
				// TODO
				if(!Rank.hasAbility(player, Rank.DEVELOPER) || player.getPosition().disabledRange())
					return false;
				// ContentEntity.startAnimation(combatEntity.getPlayerByName(), 426);
				if(currentdistance <= 1) {
					clientSpeed = 55;
				} else if(currentdistance <= 3) {
					clientSpeed = 55;
				} else if(currentdistance <= 8) {
					clientSpeed = 65;
				} else {
					clientSpeed = 75;
				}
				showDelay = 45;
				slope = 15;
				clientSpeed += 30;
				deltaBonus2 = -5;
				deltaBonus3 = -5;
				if(player.getCombat().getOpponent() != null && player.getCombat().getOpponent().getEntity() instanceof Player) {
					deltaBonus2 = CombatAssistant.calculateRangeAttack(player) -
							CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer());
					deltaBonus3 = CombatAssistant.calculateRangeAttack(player) -
							CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer());
					player.debugMessage("ur range atk is: "+CombatAssistant.calculateRangeAttack(player));
					player.debugMessage("opp range def is: "+CombatAssistant.calculateRangeDefence(player.getCombat().getOpponent().getPlayer()));
				}

				toAddFirst = Misc.random(deltaBonus2 / 3);
				toAddSecond = Misc.random(deltaBonus3 / 3);
				offsetY = ((player.cE.getAbsX()) - player.cE
						.getOpponent().getAbsX()) * - 1;
				offsetX = ((player.cE.getAbsY()) - player.cE
						.getOpponent().getAbsY()) * - 1;
				hitId = player.cE.getSlotId(player);
				// attacker.playProjectile(Projectile.create(attacker.getLocation(),
				// victim.getCentreLocation(),
				// attacker.getEquipment().get(Equipment.SLOT_ARROWS).getId() ==
				// 11212 ? 1099 : 1101, showDelay, 50, clientSpeed - 10, 41, 31,
				// victim.getProjectileLockonIndex(), 3, 36));
				// attacker.playProjectile(Projectile.create(attacker.getLocation(),
				// victim.getCentreLocation(),
				// attacker.getEquipment().get(Equipment.SLOT_ARROWS).getId() ==
				// 11212 ? 1099 : 1101, showDelay, 50, clientSpeed + 10, 46, 36,
				// victim.getProjectileLockonIndex(), slope + 6, 36));
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed - 10,
								256, 46, 31, hitId, 3);
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed + 10,
								256, 46, 36, hitId, slope + 6);
				player.getActionSender()
						.createGlobalProjectile(
								player.cE.getAbsY(),
								player.cE.getAbsX(),
								offsetY,
								offsetX,
								50,
								clientSpeed,
								256, 46, 41, hitId, slope + 9);
				damg4 = Combat.random(maxDamg);
				damg8 = Combat.random(maxDamg);
				damg4 = damg4 + toAddFirst;
				damg8 = damg8 + toAddSecond;
				if(damg4 <= minimum)
					damg4 = minimum;
				if(damg4 > maxDamg)
					damg4 = maxDamg;
				if(damg8 <= minimum)
					damg8 = minimum;
				if(damg8 > maxDamg)
					damg8 = maxDamg;
				damg4 = SpiritShields.applyEffects(player.cE.getOpponent(), damg4);
				damg8 = SpiritShields.applyEffects(player.cE.getOpponent(), damg8);
				if(oldEntity.getEntity() instanceof Player) {
					damg4 = oldEntity.getPlayer().getInflictDamage(damg4, player, false, combatStyle);
					damg8 = oldEntity.getPlayer().getInflictDamage(damg8, player, false, combatStyle);
				}
				Combat.addXP(player, damg4, true);
				Combat.addXP(player, damg8, true);
				Combat.addXP(player, Math.min(damg4, damg8), true);
				final int hit1t = damg4;
				final int hit2t = damg8;
				final CombatEntity entityt =player.cE.getOpponent();
				if(entityt != null) {
					World.submit(new org.hyperion.engine.task.Task(200 * distance + 300, "combat") {
						public void execute() {
							entityt.hit(hit1t, player, false, 1);
							entityt.hit(hit2t, player, false, 1);
							entityt.hit(Math.min(hit1t, hit2t), player, false, 1);
							if(entityt.getEntity() instanceof Player)
								Magic.vengeance(oldEntity.getPlayer(),
										player.cE, hit1t);
							this.stop();
						}
					});

					entityt
							.getEntity()
							.playGraphics(
									Graphic.create(player
													.getEquipment().get(Equipment.SLOT_ARROWS)
													.getId() == 11212 ? 1100 : 1103,
											6553600 + clientSpeed));
				}
				break;
		}

		return true;
	}

	private static boolean delayedWeapon(int weaponId) {
		switch(weaponId) {
        case 14484:
		case 4153:
		case 17646:
			return false;
		}
		return true;
	}

	/**
	 * Gets the weapon Damagebonus..
	 */
	public static double getSpecialBonus(Player p) {
		double specDamage = 1;
		Item weapon = p.getEquipment().get(Equipment.SLOT_WEAPON);
		if(weapon == null)
			return specDamage;
		switch(weapon.getId()) {
            case 17640:
                specDamage = 1.25;
                break;
			case 13883:
				specDamage = 1.15;
                break;
			case 11730:
			case 11696:
				specDamage = 1.13;
				break;
			case 10887:
			case 1215: // dragon daggers
			case 1231:
			case 5680:
			case 5698:
			case 15007:
				specDamage = 1.22;

				break;
			case 1305:
            case 13899:
            case 11694:
				specDamage = 1.33;
				break;
            case 19605:
                specDamage = 1.30;
                break;
			case 15020:
			case 1434:
				specDamage = 1.4;
				break;
			case 14484:
				specDamage = 1.2;
				break;
		}
		return specDamage;
	}
}
