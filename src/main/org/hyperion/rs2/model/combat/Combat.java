package org.hyperion.rs2.model.combat;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.engine.task.impl.WildernessBossTask;
import org.hyperion.map.WorldMap;
import org.hyperion.map.pathfinding.Path;
import org.hyperion.map.pathfinding.PathTest;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.pvp.PvPDegradeHandler;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.duel.DuelRule.DuelRules;
import org.hyperion.rs2.model.content.ClickId;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.minigame.*;
import org.hyperion.rs2.model.content.misc.ItemDegrading;
import org.hyperion.rs2.model.content.skill.Prayer;
import org.hyperion.rs2.model.content.skill.slayer.SlayerTask;
import org.hyperion.rs2.model.content.specialareas.SpecialArea;
import org.hyperion.rs2.model.content.specialareas.SpecialAreaHolder;
import org.hyperion.rs2.model.shops.SlayerShop;
import org.hyperion.util.Misc;

/**
 * @authors Martin and Arsen
 */

public class Combat {


    public static boolean processCombat(final CombatEntity combatEntity) {
        try {
            /**
             * Logical check if combatEntity isn't null, isn't dead, etc..
             */
            if (!CombatAssistant.isValid(combatEntity))
                return false;
            /**
             * Facing
             */
            combatEntity.face(combatEntity.getOpponent().getAbsX() + combatEntity.getOpponent().getOffsetX(), combatEntity.getOpponent().getAbsY() + combatEntity.getOpponent().getOffsetY(), true);

            if (combatEntity.predictedAtk > System.currentTimeMillis()) {
                return true;
            }

            String message = canAtk(combatEntity, combatEntity.getOpponent());
            if (message.length() > 1) {
                if (combatEntity.getEntity() instanceof Player)
                    combatEntity.getPlayer().getActionSender().sendMessage(message);
                return false;
            }
            /**
             * Add opponent to attackers list
             */
            if (!combatEntity.getOpponent().getAttackers().contains(combatEntity)) {
                combatEntity.getOpponent().getAttackers().add(combatEntity);
            }

            combatEntity._getPlayer().ifPresent(p -> p.getExtraData().put("combatimmunity", System.currentTimeMillis()));

            /**
             * Distance and freezetimer check.
             */
            int distance = combatEntity.getEntity().getPosition().distance((combatEntity.getOpponent().getEntity().getPosition()));
            /*Checks if standing on eachother*/
            if (distance == 0) {
				/*If standing on eachother and frozen*/
                if (combatEntity.isFrozen())
                    return false;
                if (!combatEntity.getOpponent().vacating) {
                    combatEntity.vacating = true;
                    combatEntity.getEntity().vacateSquare();
                }
                return true;
            }
            combatEntity.vacating = false;
            /**
             * Run seperate code depending on whether the combatEntity is an NPC or a Player.
             */
            if (combatEntity.getEntity() instanceof Player) {
                final Player player = combatEntity.getPlayer();
                if (player.getNpcState()) {
                    player.setPNpc(-1);
                }
                if (combatEntity.getOpponent()._getPlayer().isPresent()) {
                    final Player opp = combatEntity.getOpponent().getPlayer();
                    if (opp.getNpcState()) {
                        opp.setPNpc(-1);
                    }
                    if (!player.getSession().isConnected() && !opp.getSession().isConnected()) {
                        return false;
                    }
                    if (player.getExtraData().getLong("stuntimez") > System.currentTimeMillis()) {
                        player.sendMessage("You are too dazed to fight");
                        return false;
                    }
                }
                return processPlayerCombat(combatEntity, distance);
            } else {
                if (combatEntity.getOpponent()._getPlayer().isPresent() && !combatEntity.getOpponent().getPlayer().getSession().isConnected())
                    return false;
                combatEntity.getOpponent().lastHit = System.currentTimeMillis();
                return processNpcCombat(combatEntity, distance);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean processPlayerCombat(final CombatEntity combatEntity, int distance) throws Exception {
        /**
         * Initializing variables.
         */
        boolean hit = false;
        boolean special = false;
        boolean finishOff = true;
        boolean doubleHit = false;
        int arrowId = CombatAssistant.getArrowsId(combatEntity.getPlayer().getEquipment());
        int weaponId = CombatAssistant.getWeaponId(combatEntity.getPlayer().getEquipment());
        int damgDouble = 0;
        int damg = 0;
        Entity attacker = combatEntity.getEntity();
        final Entity opponent = combatEntity.getOpponent().getEntity();

        /**
         * Skull Adding
         */
        CombatAssistant.checkSkull(combatEntity);


        int magicAtk = combatEntity.getNextMagicAtk();
        if (combatEntity.getNextMagicAtk() > 0) {
            if (distance > 11) {
                if (opponent instanceof Player)
                    combatEntity.getPlayer().getActionSender().follow(opponent.getIndex(), 1);
                return true;
            } else if (!WorldMap.projectileClear(attacker.getPosition(), opponent.getPosition())) {
                if (opponent instanceof Player)
                    combatEntity.getPlayer().getActionSender().follow(opponent.getIndex(), 1);
                else
                    follow(combatEntity, combatEntity.getOpponent());
                return true;
            } else {
                if (distance > 8) {
                    if (opponent instanceof Player) {
                        combatEntity.getPlayer().getActionSender().follow(opponent.getIndex(), 1);
                    } else
                        follow(combatEntity, combatEntity.getOpponent());
                } else combatEntity.getPlayer().getWalkingQueue().reset();
            }
            // cast the actual spell using magic code :), result was if
            // its succesfuly or not (i.e no runes)
            int result = Magic.castSpell(combatEntity, combatEntity.getOpponent(), magicAtk);
            if (result == Magic.SPELL_SUCCESFUL) {
                // if it worked remove the spell :)
                combatEntity.deleteSpellAttack();
                combatEntity.predictedAtk = System.currentTimeMillis() + 2500;

                // combatEntity.predictedAtk =
                // System.currentTimeMillis()+2000;
                // spell hit etc
                hit = true;
                finishOff = false;
                /**
                 * Degrading
                 */
                PvPDegradeHandler.checkDegrade(combatEntity.getPlayer());
                if (!(combatEntity.getAutoCastId() > 0)) {
                    combatEntity.setOpponent(null);
                    return false;
                }
                return true;
            } else if (result == 0) {
                // no runes so reset
                return false;
            }
        }
        /**
         * Max Hit and Combat Style Determination.
         */
        int bowType = CombatAssistant.getCombatStyle(combatEntity);
        // Check Arrows/Bow
        if (bowType <= Constants.NOAMMO) {
            combatEntity.getPlayer().getWalkingQueue().reset();
            switch (bowType) {
                case Constants.RANGEDNOARROWS:
                    combatEntity.getPlayer().getActionSender().sendMessage("You have no arrows left in your quiver.");
                    //System.out.println("No arrows!");
                    break;
                case Constants.RANGEDNOBOLTS:
                    combatEntity.getPlayer().getActionSender().sendMessage("You have no bolts left.");
                    break;
                case Constants.UNIQUENOAMMO:
                    combatEntity.getPlayer().getActionSender().sendMessage("You have no ammo left.");
                    break;
                case Constants.UNIQUEWRONG:
                    combatEntity.getPlayer().getActionSender().sendMessage("You cannot use this type of ammo with this weapon.");
                    break;
            }
            //System.out.println("Returning false");
            return false;
        }
        int maxHit = 0;
        final int combatStyle;
        if (bowType == Constants.MELEETYPE) {
            maxHit = CombatAssistant.calculateMaxHit(combatEntity.getPlayer());
            combatStyle = Constants.MELEE;
            if (combatEntity.getAutoCastId() <= 0) {
                if (opponent instanceof Player)
                    combatEntity.getPlayer().getActionSender().follow(opponent.getIndex(), 1);
            }
        } else {
            maxHit = CombatAssistant.calculateRangeMaxHit(combatEntity.getPlayer());
            combatStyle = Constants.RANGE;
        }
        final int possibleMaxHit = maxHit;
        /**
         * Special Activating
         */
        if (!hit && combatEntity.getNextMagicAtk() <= 0) {
            if (combatEntity.getPlayer().specOn) {
                if (combatEntity.predictedAtk > System.currentTimeMillis() + 600) {
                    return true;
                }
                combatEntity.getPlayer().specOn = false;
                if (weaponId == -1) {

                } else if (SpecialAttacks.special(combatEntity.getPlayer(), maxHit, weaponId, distance, combatStyle)) {
                    hit = true;
                    finishOff = false;
                    special = true;
                    if (weaponId != 15241) {
                        combatEntity.predictedAtk = (System.currentTimeMillis() + combatEntity.getAtkSpeed());
                    } else {
                        if (Misc.random(150) == 0) { // 1/101 chance of exploding when specing
                            combatEntity.getPlayer().getEquipment().set(Equipment.SLOT_WEAPON, null);
                            combatEntity.getPlayer().getActionSender().sendMessage("@red@Your handcannon exploded!");
                        }
                    }
                } else {
                    combatEntity.getPlayer().getSpecBar().sendSpecAmount();
                    return true;
                }
                combatEntity.getPlayer().getSpecBar().sendSpecAmount();
            }
        }
        /**
         * Autocasting
         */
        if (!hit) {
            if (combatEntity.getAutoCastId() > 0) {
                if (combatEntity.getPlayer().duelRule[DuelRules.MAGE.ordinal()]
                        && combatEntity.getPlayer().duelAttackable > 0) {
                    combatEntity.getPlayer().getActionSender().sendMessage("You cannot use magic in this duel.");
                    combatEntity.setAutoCastId(0);
                    return false;
                }
                if (distance > 11 || !WorldMap.projectileClear(attacker.getPosition(), opponent.getPosition())) {
                    if (opponent instanceof Player)
                        combatEntity.getPlayer().getActionSender().follow(combatEntity.getOpponent().getEntity().getIndex(), 1);
                    return true;// too far away
                } else {
                    if (distance > 8) {
                        if (opponent instanceof Player) {
                            combatEntity.getPlayer().getActionSender().follow(opponent.getIndex(), 1);
                        } else
                            follow(combatEntity, combatEntity.getOpponent());
                    }
                    combatEntity.getPlayer().getWalkingQueue().reset();
                }
                // timer
                if (combatEntity.predictedAtk > System.currentTimeMillis()) {
                    return true;
                }
                combatEntity.addSpellAttack(combatEntity.getAutoCastId());
                hit = true;
                finishOff = false;
            }
        }
        /**
         * Ranging
         */
        if (!hit) {
            // If in Duel , Return
            if (bowType != 8 && combatEntity.getPlayer().duelRule[DuelRules.RANGE.ordinal()]
                    && combatEntity.getPlayer().duelAttackable > 0) {
                combatEntity.getPlayer().getActionSender().sendMessage("You cannot use range in this duel.");
                return false;
            }
            if (bowType != 8 && combatEntity.getPlayer().getPosition().disabledRange()) {
                combatEntity.getPlayer().getActionSender().sendMessage("You cannot use ranged at ::13s, sorry! For tribridding or pure bridding go to ::mb");
                return false;
            }
            //can't range ppl from afar that are in non range zone
            if (bowType != 8 && combatEntity.getOpponent() != null && combatEntity.getOpponent().getEntity() instanceof Player && combatEntity.getOpponent().getPlayer().getPosition().disabledRange()) {
                combatEntity.getPlayer().getActionSender().sendMessage("That person is in a no range zone!");
                return false;
            }
            if (bowType != Constants.MELEETYPE) {
                if (distance > 8) {
                    if (opponent instanceof Player)
                        combatEntity.getPlayer().getActionSender().follow(combatEntity.getOpponent().getEntity().getIndex(), 1);
                    return true;// too far away
                } else if (!WorldMap.projectileClear(combatEntity.getEntity().getPosition(), combatEntity.getOpponent().getEntity().getPosition())) {
                    if (combatEntity.getOpponent().getEntity() instanceof Player)
                        combatEntity.getPlayer().getActionSender().follow(combatEntity.getOpponent().getEntity().getIndex(), 1);
                    else
                        follow(combatEntity, combatEntity.getOpponent());
                    return true;
                } else {
                    combatEntity.getPlayer().getActionSender().resetFollow();
                    combatEntity.getPlayer().getWalkingQueue().reset();
                }
                int arrowType = CombatAssistant.getArrowType(arrowId);
                maxHit = CombatAssistant.calculateRangeMaxHit(combatEntity.getPlayer());


                // System.out.println("range max: "+maxHit);

                /**
                 * Sending Projectile part.
                 */
                CombatAssistant.fireProjectile(combatEntity, bowType, arrowType);
                /**
                 * Set Attack Speed
                 */
                combatEntity.predictedAtk = (System.currentTimeMillis() + combatEntity.getAtkSpeed());
                // drawback gfx
                CombatAssistant.drawBackGfx(combatEntity, weaponId, arrowId, bowType);
                /**
                 * Get random Damage Hit
                 */
                damg = random(maxHit);

                /**
                 * Checks Range bonus etc
                 */
                damg = CombatCalculation.getCalculatedDamage(combatEntity.getPlayer(), combatEntity.getOpponent().getEntity(), damg, Constants.RANGE, maxHit);

                if (CombatAssistant.darkBow(weaponId) || weaponId == 16337 || weaponId == 16887) {
                    damgDouble = random(maxHit);
                    doubleHit = true;
                    damgDouble = CombatCalculation.getCalculatedDamage(combatEntity.getPlayer(), combatEntity.getOpponent().getEntity(), damgDouble, Constants.RANGE, maxHit);
                }

                /**
                 * Enchanted Bolts Effects
                 */
                double boltBonus = 1;
                if (Misc.random(4) == 0 && damg > 0.3 * maxHit && bowType == Constants.RANGEDBOLTS) {
                    switch (arrowId) {
                        case 9242:
                            if (combatEntity.getOpponent().getEntity() instanceof Player)
                                damg = combatEntity.getOpponent().getPlayer().getSkills().getLevelForExp(Skills.HITPOINTS) / 5;
                            else
                                damg = combatEntity.getOpponent().getNPC().health / 5;
                            int newHp = (int) (combatEntity.getPlayer().getSkills().getLevel(Skills.HITPOINTS) * 0.9);
                            combatEntity.getPlayer().getSkills().setLevel(Skills.HITPOINTS, newHp);
                            combatEntity.getOpponent().doGfx(754);
                            break;
                        case 9243:
                            boltBonus = 1.1;
                            combatEntity.getOpponent().doGfx(758);
                            break;
                        case 9244:
                            if (combatEntity.getOpponent().getEntity() instanceof Player && combatEntity.getOpponent().getPlayer() != null) {
                                Item shield = combatEntity.getOpponent().getPlayer().getEquipment().get(Equipment.SLOT_SHIELD);
                                if (combatEntity.getOpponent().getPlayer().superAntiFire) {
                                    boltBonus = .7;
                                    combatEntity.getOpponent().getPlayer().getActionSender().sendMessage("Your super antifire soaks the dragon bolt's effect!");
                                } else if (System.currentTimeMillis() - combatEntity.getOpponent().getPlayer().antiFireTimer < 360000) {
                                    boltBonus = 1;
                                    combatEntity.getOpponent().getPlayer().getActionSender().sendMessage("Your antifire blocks the nasty heat from the bolts!");
                                } else if (shield != null && (shield.getId() == 11283 || shield.getId() == 11284)) {
                                    boltBonus = 1.15;
                                    combatEntity.getOpponent().getPlayer().getActionSender().sendMessage("Your shield blocks most of the heat from the bolts!");
                                } else {
                                    boltBonus = 1.5;
                                    combatEntity.getOpponent().getPlayer().getActionSender().sendMessage("@dbl@You are horribly burt by the dragonfire!");
                                }
                            } else {
                                boltBonus = 1.35;
                            }
                            combatEntity.getOpponent().doGfx(756);
                            break;
                        case 9245:
                            boltBonus = 1.3;
                            combatEntity.getOpponent().getEntity().playGraphics(Graphic.create(753));
                            break;
                    }
                    damg *= boltBonus;
                }

                if (weaponId == 16887 && Misc.random(10) == 1) {
                    combatEntity.doGfx(855);
                    if (combatEntity.getEntity() instanceof Player) {
                        ContentEntity.heal(combatEntity.getPlayer(), (damg + damgDouble) / 3);
                        combatEntity.getPlayer().sendMessage("You restore some hitpoints.");
                    }
                }

                if (weaponId == 16337 && Misc.random(8) == 1) {
                    combatEntity.getOpponent().doGfx(469);
                    damgDouble *= 1.35;
                    damg *= 1.35;
                    combatEntity.getPlayer().sendMessage("Your arrows slice through the armour.");

                }
                if (combatEntity.getOpponent().getEntity() instanceof NPC && combatEntity.getPlayer().getSlayer().isTask(combatEntity.getOpponent().getNPC().getDefinition().getId())) {
                    if (SlayerShop.hasFocus(combatEntity.getPlayer()))
                        damg *= 1.15;
                }

                if (combatEntity.getOpponent().getEntity() instanceof Player) {
                    //divine spirit shield


                    /**
                     * Prayer Checking
                     */

                } else {
                    if (SlayerTask.getLevelById(combatEntity.getOpponent().getNPC().getDefinition().getId()) > combatEntity.getPlayer().getSkills().getLevel(Skills.SLAYER))
                        damg = 0;
                }

                // delay = 1500;
                hit = true;
                combatEntity.getPlayer().getWalkingQueue().reset();// dont
                // move!
            }
        }
        /**
         * Melee
         */
        if (!hit) {
            if (combatEntity.getPlayer().duelRule[DuelRules.MELEE.ordinal()]
                    && combatEntity.getPlayer().duelAttackable > 0) {
                combatEntity.getPlayer().getActionSender().sendMessage("You cannot use melee in this duel.");
                return false;
            }
            if (!combatEntity.getEntity().getPosition().isWithinDistance(combatEntity.getOpponent().getEntity().getPosition(), (1 + (combatEntity.canMove() ? 1 : 0)))) {

                if (opponent instanceof Player)
                    combatEntity.getPlayer().getActionSender().follow(combatEntity.getOpponent().getEntity().getIndex(), 1);
                return true;// too far away
            } else {
                if (!combatEntity.canMove() && combatEntity.getEntity().getPosition().distance(combatEntity.getOpponent().getEntity().getPosition()) == 2)
                    return true;
                //combatEntity.getPlayer().getWalkingQueue().reset();
            }

			/*
			 * if(!WorldMap.projectileClear(combatEntity.getEntity().
			 * getLocation().getZ(),
			 * combatEntity.getEntity().getPosition().getX(),
			 * combatEntity.getEntity().getPosition().getY(),
			 * combatEntity
			 * .getOpponent().getEntity().getPosition().getX() +
			 * combatEntity
			 * .getOpponent().getOffsetX(),combatEntity.getOpponent
			 * ().getEntity().getPosition().getY() +
			 * combatEntity.getOpponent().getOffsetY())) return true;
			 */

            if (!WorldMap.projectileClear(combatEntity.getEntity().getPosition(), combatEntity.getOpponent().getEntity().getPosition()))
                return true;
            if (combatEntity.predictedAtk > System.currentTimeMillis()) {
                return true;// we dont want to reset attack but just
                // wait another 500ms or so...
            }
            int addspeed = combatEntity.getAtkSpeed();
            if (addspeed != 0)
                combatEntity.predictedAtk = (System.currentTimeMillis() + combatEntity.getAtkSpeed());
            else
                combatEntity.predictedAtk = System.currentTimeMillis() + 2400;

            			/*
			 * else
			 * combatEntity.getPlayer().getActionSender().resetFollow();
			 */// this isnt too nessary in melee, only magic and range
			/*if(bowType != Constants.RANGEDNOARROWS)
				combatEntity.doAtkEmote();
			else
				combatEntity.doAnim(422);*/// you dont try shoot arrows
            // wen u have no arrows
            /**
             * Get random Damage Hit.
             */
            damg = random(maxHit);
            boolean verac = false;
            if (CombatAssistant.isVeracEquiped(combatEntity.getPlayer())
                    && random(6) == 1)
                verac = true;
            if (combatEntity.getOpponent().getEntity() instanceof Player) {
                if (!verac) {
                    /**
                     * Here is the Hit determine stuff, Includes Overhead Prayers.
                     */
                    int MeleeAtk = CombatAssistant.calculateMeleeAttack(combatEntity.getPlayer());
                    int MeleeDef = CombatAssistant.calculateMeleeDefence(combatEntity.getOpponent().getPlayer());
					/*if(combatEntity.getPlayer().getName().toLowerCase().equals("dr house")){
						combatEntity.getPlayer().getActionSender().sendMessage("Atk : " + MeleeAtk + " Def : " + MeleeDef);
					}*/
                    int deltaBonus = MeleeAtk - MeleeDef;
                    int toAdd = Misc.random(deltaBonus / 3);
                    damg += toAdd;
                    combatEntity.getPlayer().debugMessage("Toadd: " + toAdd);
                    if (damg < 0)
                        damg = 0;
                    if (damg > maxHit)
                        damg = maxHit;

					/*if(combatEntity.getPlayer().getName().toLowerCase().equals("dr house")){
						combatEntity.getPlayer().getActionSender().sendMessage("Damg : " + damg);
					}*/
                }
            } else {
                if (verac) {
                } else
                    damg = CombatCalculation.getCalculatedDamage(combatEntity.getEntity(), combatEntity.getOpponent().getEntity(), damg, combatStyle, maxHit);
                if (SlayerTask.getLevelById(combatEntity.getOpponent().getNPC().getDefinition().getId()) > combatEntity.getPlayer().getSkills().getLevel(Skills.SLAYER))
                    damg = 0;
            }
            hit = true;
        }
        /**
         * Spirit shield effects.
         */
        if (combatEntity.getPlayer() != null && Rank.hasAbility(combatEntity.getPlayer(), Rank.ADMINISTRATOR)) {
            //combatEntity.getPlayer().getActionSender().sendMessage("Damg without divine would be: " + damg);
            damg = SpiritShields.applyEffects(opponent.cE, damg);
            //combatEntity.getPlayer().getActionSender().sendMessage("Damg with divine is: " + damg);
        } else {
            damg = SpiritShields.applyEffects(opponent.cE, damg);
        }
        if (finishOff && hit) {
            int wepId = 0;
            if (combatEntity.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON) != null)
                wepId = combatEntity.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON).getId();
            if (combatEntity.getPlayer().getNpcState()) {
                combatEntity.doAnim(NPCDefinition.forId(combatEntity.getPlayer().getNpcId()).getAtkEmote(0));
            } else if (wepId == 4212 || FightPits.isBow(wepId) || wepId == 14121) {
                combatEntity.doAnim(426);
                combatEntity.isDoingAtk = true;
            } else if (wepId == 0) {
                combatEntity.doAnim(422);
            } else
                combatEntity.doAtkEmote();
        }


        if (combatEntity.getOpponent().getEntity() instanceof NPC && combatEntity.getPlayer().getSlayer().isTask(combatEntity.getOpponent().getNPC().getDefinition().getId())) {
            if (SlayerShop.hasHelm(combatEntity.getPlayer()))
                damg *= 1.15;
        }
        if (hit) {

            /**
             * Degrading
             */
            PvPDegradeHandler.checkDegrade(combatEntity.getPlayer());
            ItemDegrading.check(combatEntity.getPlayer());
        }
        if (finishOff) {

            finishOff(combatEntity, damg, hit, bowType, damgDouble, doubleHit, distance, possibleMaxHit, combatStyle);
        }

        Curses.applyLeeches(combatEntity.getPlayer());
        return true;
    }

    public static boolean finishOff(final CombatEntity combatEntity, int damg, final boolean hit, final int bowType, int damgDoubl, final boolean doubleHit, final int distance, final int maxHit, final int combatStyle) {
        if (!hit)
            return true;
        final CombatEntity opponent = combatEntity.getOpponent();
        opponent.lastHit = System.currentTimeMillis();

        int delay = 300 + distance * 200;
        //Auto kill ring :p
        if (combatEntity.getEntity() instanceof Player) {
            if (opponent.getNPC() != null)
                opponent.getNPC().lastAttacker = combatEntity.getPlayer().getName();
            Player player = ((Player) combatEntity.getEntity());
            if (player.getEquipment().contains(4657)) {
                if (Rank.hasAbility(player, Rank.OWNER)) {
                    if (opponent.getEntity() instanceof Player) {
                        Player p = ((Player) opponent.getEntity());
                        opponent.getDamageDealt().clear();
                        damg = p.getSkills().getLevel(Skills.HITPOINTS) * 2;
                    } else {
                        damg = ((NPC) opponent.getEntity()).health;
                    }
                }
            }
        }

        /**
         * Zanik's crossbow prayer take-off
         */


        if (opponent.getEntity() instanceof Player) {
            damg = opponent.getPlayer().getInflictDamage(damg, combatEntity.getEntity(), false, combatStyle);
            if (doubleHit)
                damgDoubl = opponent.getPlayer().getInflictDamage(damgDoubl, combatEntity.getEntity(), false, combatStyle);
            if (combatEntity.getEntity() instanceof Player)
                opponent.getPlayer().getLastAttack().updateLastAttacker(combatEntity.getPlayer().getName());


        }
        final int damgDouble = damgDoubl;
        final int damage = damg;
        if (doubleHit)
            CombatAssistant.addExperience(combatEntity, bowType, damgDouble);
        CombatAssistant.addExperience(combatEntity, bowType, damage);

        TaskManager.submit(new Task(delay, "combat") {
            public void execute() {
                if (combatEntity == null || opponent == null) {
                    this.stop();
                    return;
                }
                /**
                 * Another verification check incase of glitchers.
                 */
                String message = canAtk(combatEntity, opponent);
                if (message.length() > 1) {
                    combatEntity.getPlayer().getActionSender().sendMessage(message);
                    this.stop();
                    return;
                }
                /**
                 * Poisoning of enemy.
                 */
                if (!opponent.isPoisoned()) {
                    if (damage > 0 && combatEntity.getWeaponPoison() > 0) {
                        if (random(10) <= combatEntity.getWeaponPoison()) {
                            poisonEntity(combatEntity.getOpponent());
                        }
                    }
                }
                /**
                 * Smiting.
                 */
                if (opponent.getEntity() instanceof Player) {
                    if (opponent.getPlayer().getPrayers().isEnabled(23)) {
                        Prayer.smite(combatEntity.getPlayer(), damage);
                    }
                }


                /**
                 * Applies Damage.
                 */
                int critical = damage >= 0.90 * maxHit ? 5 : 0; // Later substract 5
                int actualDamage = opponent.hit(damage, combatEntity.getEntity(), false, combatStyle + critical);

                /**
                 * Recoil and vengeance.
                 */
                if (opponent.getEntity() instanceof Player) {
                    Magic.vengeance(opponent.getPlayer(), combatEntity, actualDamage);
                    Magic.recoil(opponent.getPlayer(), combatEntity, actualDamage);

                    if (doubleHit) {
                        int actualDoubleHit = opponent.hit(damgDouble, combatEntity.getEntity(), false, 1);
                        Magic.vengeance(opponent.getPlayer(), combatEntity, actualDoubleHit);
                        Magic.recoil(opponent.getPlayer(), combatEntity, actualDoubleHit);
                    }
                }

                /**
                 * Soulsplit.
                 */
                if (combatEntity.getPlayer().getPrayers().isEnabled(48)) {
                    Prayer.soulSplit(combatEntity.getPlayer(), opponent, actualDamage);
                    if (doubleHit) {
                        Prayer.soulSplit(combatEntity.getPlayer(), opponent, damgDouble);
                    }
                }
                if (isGuthanEquiped(combatEntity.getPlayer()))
                    combatEntity.getPlayer().heal((int) (actualDamage * 0.5));
                if (opponent.getCurrentAtker() == null
                        || opponent.getCurrentAtker() == combatEntity) {
					/*
					 * opponentHit.face(combatEntity.getAbsX(
					 * ), combatEntity.getAbsY());
					 */
                    if (opponent.getEntity() instanceof Player
                            || opponent.getNPC().getDefinition().doesDefEmote())
                        opponent.doDefEmote();
                    if (opponent.getEntity() instanceof NPC
                            || opponent.getPlayer().autoRetailate) {
                        opponent.setOpponent(combatEntity);
                    }
                    if (opponent.summonedNpc != null) {
                        opponent.summonedNpc.cE.setOpponent(combatEntity);
                        opponent.summonedNpc.cE.face(combatEntity.getAbsX(), combatEntity.getAbsY());
                        opponent.summonedNpc.setInteractingEntity(combatEntity.getEntity());
                    }
                }

                this.stop();
            }
        });
        return true;

    }

    /**
     * Processes the combat for an NPC combatEntity.
     *
     * @param combatEntity
     * @param distance
     * @return
     */
    private static boolean processNpcCombat(final CombatEntity combatEntity, int distance) {
        if (combatEntity.attack == null)
            combatEntity.attack = NPCManager.getAttack(combatEntity.getNPC());
        // combatEntity.doAtkEmote();

        if (combatEntity.attack != null) {
            // timer
			/*
			 * if(combatEntity.predictedAtk >
			 * System.currentTimeMillis()){
			 * follow(combatEntity,combatEntity.getOpponent()); return
			 * true;//we dont want to reset attack but just wait another
			 * 500ms or so... }
			 */
            if (combatEntity.getOpponent().getEntity() instanceof Player) {
                if (!combatEntity.getOpponent().getPlayer().isActive()
                        || combatEntity.getOpponent().getPlayer().isHidden()) {
                    resetAttack(combatEntity);
                    return false;
                }
            }
            if (combatEntity.getNPC().ownerId >= 1 && combatEntity.getNPC().summoned) {
                if (combatEntity.getOpponent() != null && combatEntity.getOpponent().getEntity() instanceof Player) {
                    if (!Position.inAttackableArea(combatEntity.getOpponent().getPlayer()) || !isInMulti(combatEntity.getOpponent())) {
                        return false;
                    }
                }
            }
            if (combatEntity.getNPC().ownerId >= 1 && !combatEntity.getNPC().summoned) {
                if (combatEntity.getOpponent() != null && combatEntity.getOpponent().getEntity() instanceof Player) {
                    if (combatEntity.getNPC().ownerId != combatEntity.getOpponent().getPlayer().getIndex()) {
                        return false;
                    }
                }
            }
            combatEntity.getNPC().face(combatEntity.getOpponent().getEntity().getPosition());
            int type = combatEntity.attack.handleAttack(combatEntity.getNPC(), combatEntity.getOpponent());
            if (type == 1 && combatEntity.getOpponent() != null
                    && combatEntity.getNPC().agressiveDis > 0
                    && combatEntity.getEntity().getPosition().distance(combatEntity.getOpponent().getEntity().getPosition()) <= combatEntity.getNPC().agressiveDis) {
                type = 0;
            }
            if (type == 5) {
				/*
				 * if(combatEntity.getOpponent().getOpponent() == null
				 * || combatEntity.getOpponent().getOpponent() ==
				 * combatEntity){
				 * //combatEntity.getOpponent().face(combatEntity
				 * .getAbsX(),combatEntity.getAbsY());
				 * combatEntity.getOpponent
				 * ().face(combatEntity.getAbsX()
				 * +combatEntity.getOffsetX
				 * (),combatEntity.getAbsY()+combatEntity.getOffsetY());
				 *
				 * if(combatEntity.getOpponent().getEntity() instanceof
				 * Player ||
				 * combatEntity.getOpponent().getNPC().getDefinition
				 * ().doesDefEmote())
				 * combatEntity.getOpponent().doDefEmote();
				 * if(combatEntity.getOpponent().getEntity() instanceof
				 * NPC ||
				 * combatEntity.getOpponent().getPlayer().autoRetailate
				 * ){
				 * combatEntity.getOpponent().setOpponent(combatEntity);
				 * } }
				 */
                if (combatEntity.getOpponent().getEntity() instanceof Player)
                    combatEntity.getOpponent().getPlayer().getLastAttack().updateLastAttacker(combatEntity.getNPC().getIndex());
                combatEntity.getOpponent().lastHit = System.currentTimeMillis();
                // successful
            } else if (type == 1) {
                // cancel
                return false;
            } else if (type == 0) {

                follow(combatEntity, combatEntity.getOpponent());
            }
            //System.out.println("Npc attack type: " + type);
        }
        return true;
        // combatEntity.getOpponent().hit(1,combatEntity.getOpponent().getEntity(),false);
        // npc combat, not as complicated as player combat
    }

    public static boolean npcAttack(final NPC npc, final CombatEntity combatEntity, final int damg, final int delay, int type) {
        if (type >= 3)
            type = Constants.MAGE;
        return npcAttack(npc, combatEntity, damg, delay, type, false);
    }

    public static boolean npcAttack(final NPC npc, final CombatEntity combatEntity, final int damg, final int delay, final int type, final boolean prayerBlock) {

        TaskManager.submit(new Task(delay, "npcattack") {
            @Override
            public void execute() {
                if ((combatEntity == null ||
                        combatEntity.getEntity() == null ||
                        npc == null) || (combatEntity._getPlayer().isPresent() && !combatEntity.getPlayer().getSession().isConnected())) {
                    this.stop();
                    return;
                }
                int newDamg = SpiritShields.applyEffects(combatEntity, damg);

                if (combatEntity.getEntity() instanceof Player) {
                    //divine spirit shield
                    //prayers and curses
                    if (!prayerBlock) {
                        newDamg = combatEntity.getPlayer().getInflictDamage(newDamg, npc, false, type);
                    }

                    /*if(type == 1
                            && Combat.random(npc.getDefinition().getBonus()[3]) < Combat.random(CombatAssistant.calculateRangeDefence(combatEntity.getPlayer()))) {
                        newDamg = 0;
                    }
                    if(type == 2
                            && Combat.random(npcc.getDefinition().getBonus()[4]) < Combat.random(CombatAssistant.calculateMageDef(combatEntity.getPlayer()))) {
                        newDamg = 0;
                    }*/
                    //defence
                    if (npc.getDefinition().getId() == 9463) {
                        if (Misc.random(12) == 0) {
                            combatEntity.setFreezeTimer(2000);
                            newDamg += Misc.random(2);
                            combatEntity.getPlayer().getActionSender().sendMessage("The ice strykewyrm used his ice bite!");
                        }
                    }
                }
                if (combatEntity.getOpponent() == null
                        || combatEntity.getOpponent() == npc.cE) {

                    combatEntity.face(combatEntity.getAbsX(), combatEntity.getAbsY());
                    if (combatEntity.getEntity() instanceof Player
                            || combatEntity.getNPC().getDefinition().doesDefEmote())
                        combatEntity.doDefEmote();
                    if (combatEntity.getEntity() instanceof NPC
                            || combatEntity.getPlayer().autoRetailate) {
                        //System.out.println("SETING OPP LOOL3");
                        combatEntity.setOpponent(npc.cE);
                        if (combatEntity.summonedNpc != null) {
                            combatEntity.summonedNpc.cE.setOpponent(npc.cE);
                            combatEntity.summonedNpc.cE.face(npc.cE.getAbsX(), npc.cE.getAbsY());
                            combatEntity.summonedNpc.setInteractingEntity(npc);
                        }
                    }

                }

                // combatEntity.doDefEmote();
                combatEntity.hit(newDamg, npc.cE.getEntity(), false, type >= 3 ? Constants.MAGE
                        : type);
                this.stop();
            }
        });
        return false;
    }

    public static void npcRangeAttack(final NPC n, final CombatEntity attack, int gfx, int height, boolean slowdown) {

        // offset values for the projectile
        int offsetY = ((n.cE.getAbsX() + n.cE.getOffsetX()) - attack.getAbsX())
                * -1;
        int offsetX = ((n.cE.getAbsY() + n.cE.getOffsetY()) - attack.getAbsY())
                * -1;
        // find our lockon target
        int hitId = attack.getSlotId(n);
        // extra variables - not for release
        int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX()
                + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY()
                + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
        int timer = 1;
        int min = 16;
        if (distance > 8) {
            timer += 2;
        } else if (distance >= 4) {
            timer++;
        }
        min -= (distance - 1) * 2;
        int speed = 75 - min;
        int slope = 7 + distance;
        if (slowdown)
            speed = speed * 2;
        // create the projectile
        // System.out.println("hitId: "+hitId);
        if (attack.getPlayer() != null)
            attack.getPlayer().getActionSender().createGlobalProjectile(n.cE.getAbsY()
                    + n.cE.getOffsetY(), n.cE.getAbsX() + n.cE.getOffsetX(), offsetY, offsetX, 50, speed, gfx, height, 35, hitId, slope);
    }

    public static boolean canAttack() {
        /**
         * Attacking is permitted
         *
         * Not enough wilderness Level
         *
         * Already in Combat
         *
         * being attacked & cannot attack another entity
         */
        return true;
    }

    // 1 - attack is ok
    // 0 - wild level not enough
    // 2 - aready in combat them
    // 3 - your being attacked

    public static String canAtk(CombatEntity combatEntity, CombatEntity opponent) {
        if (combatEntity.getEntity() instanceof Player && opponent.getEntity() instanceof Player) {
            Player p = combatEntity.getPlayer();
            Player opp = opponent.getPlayer();

            if (!Position.inAttackableArea(opp))
                return "This player is not in an attackable area";
            if (!Position.inAttackableArea(p))
                return "You are not in an attackable area";
            if (FightPits.isSameTeam(p, opp))
                return "Friend, not food";
        }
        if (combatEntity.getAbsZ() != opponent.getAbsZ())
            return "This player is too far away to attack!";

        if (!isInMulti(combatEntity) || !isInMulti(opponent)) {
			/* Summon Npcs */
            if (combatEntity.getEntity() instanceof NPC) {
                if (combatEntity.getNPC().getDefinition().getId() == 21 || combatEntity.getNPC().getDefinition().getId() == 2256)
                    return "blablabla";
                if (combatEntity.getNPC().summoned) {
                    if (opponent.getEntity() instanceof NPC)// summon attacking
                        // another npc
                        // in a singles
                        // area = OK
                        return "1";
                    else
                        // otherwise there attacking a player in singles
                        return "blablabla";
                    //not summoned npc
                } else {
                    if (opponent.getEntity() instanceof Player && ((System.currentTimeMillis() - opponent.lastHit < 5000 && opponent.getPlayer().getLastAttack().getLastNpcAttack() != combatEntity.getNPC().getIndex()) || opponent.getPlayer().getLastAttack().timeSinceLastAttack() < 5000))
                        return "blablabla";
                }
            }
            if ((combatEntity.getEntity() instanceof Player)
                    && (opponent.getEntity() instanceof Player)
                    && ContentManager.handlePacket(6, (Player) combatEntity.getEntity(), 30000, -1, -1, -1)
                    && ContentManager.handlePacket(6, (Player) opponent.getEntity(), 30000, -1, -1, -1))
                return "1";
            String type = "NPC";
            if (opponent.getEntity() instanceof Player)
                type = "player";
            if (type.equals("player") && combatEntity.getEntity() instanceof Player) {
                /**
                 * If opponent hasent been in combat for a while, u can attack him
                 * If he hasent, you look if his last attacker = you
                 */
                if (opponent.getPlayer().getLastAttack().timeSinceLastAttack() < 5000) {
                    if (!opponent.getPlayer().getLastAttack().getName().equalsIgnoreCase(combatEntity.getPlayer().getName()))
                        return "This player is already in combat.";
                }
                /**
                 * If you are in combat, is the person who recently attacked you = person who u wanna atk?
                 */
                if (System.currentTimeMillis() - combatEntity.lastHit < 5000) {
                    if (!combatEntity.getPlayer().getLastAttack().getName().equals(opponent.getPlayer().getName()))
                        return "I am already in combat";
                }
            } else {
                if (type.equalsIgnoreCase("NPC")) {
                    NPC npc = opponent.getNPC();
                    if (combatEntity.getPlayer() != null) {
                        /*if (npc.getDefinition().getId() == 5666 &&
                                (System.currentTimeMillis() - NpcDeathTask.borkKillers.getOrDefault(combatEntity.getPlayer().getName(), 0L) < (Time.ONE_MINUTE * 3)))
                            return "Let someone else try killing barrelchest!";*/
                        if (System.currentTimeMillis() - npc.getCombat().lastHit < 9000 && !npc.lastAttacker.equalsIgnoreCase(combatEntity.getPlayer().getName()))
                            return "This monster is already in combat";
                    } else if (opponent.getOpponent() != combatEntity)
                        return "blablabla";
                    return "1";
                } else if (opponent.getOpponent() != null && opponent.getOpponent() != combatEntity) {
                    return "This " + type + " is already in combat...";
                }
            }
        }
        if (combatEntity.getEntity() instanceof Player
                && opponent.getEntity() instanceof Player) {
            if (combatEntity.getPlayer().duelAttackable > 0) {
                if (opponent.getEntity().getIndex() == combatEntity.getPlayer().duelAttackable) {
                    return "1";
                } else {
                    return "This is not your opponent!";
                }
            }
            if ((combatEntity.getAbsX() >= 2460
                    && combatEntity.getAbsX() <= 2557
                    && combatEntity.getAbsY() >= 3264 && combatEntity.getAbsY() <= 3335)
                    || /* fun pk */
                    combatEntity.getEntity().getPosition().inFunPk())// fun
                // pk
                // singles
                return "1";
            if (CastleWars.getCastleWars().canAttack(combatEntity.getPlayer(), opponent))
                return "1";
            int cb1 = combatEntity.getCombat();
            int cb2 = opponent.getCombat();
            if (combatEntity.getEntity() instanceof Player &&
                    ContentManager.handlePacket(
                            6, combatEntity.getPlayer(), ClickId.ATTACKABLE))
                return "1";
            if (LastManStanding.inLMSArea(combatEntity.getAbsX(), combatEntity.getAbsY())) {
                Participant player = LastManStanding.getLastManStanding().participants.get(combatEntity.getPlayer().getName());
                Participant opp = LastManStanding.getLastManStanding().participants.get(opponent.getPlayer().getName());
                if (player == null || opp == null) {
                    return "Something went wrong!";
                }
                return "1";
            }
            //ardy pvp code below.
			/*if(combatEntity.getEntity().getPosition().inArdyPvPArea() && opponent.getEntity().getPosition().inArdyPvPArea()) {
				if(Math.abs(cb1 - cb2) <= 6) {
					return "";
				} else {
					return "You can not attack this opponent";
				}
			}*/
            // wilderness level is too great
            String differenceOk = "You need to move deeper into the wilderness to attack this player.";

            int difference = getRealLevel(combatEntity, opponent);
            if (cb1 - cb2 <= difference && cb1 - cb2 >= 0 && difference > 0)
                differenceOk = "";
            else if (cb2 - cb1 <= difference && cb2 - cb1 >= 0 && difference > 0)
                differenceOk = "";
            return differenceOk;
        }
        // this will be returned for summons in a multi area
        return "1";
    }

    public static void resetAttack(CombatEntity combatEntity) {
        if (combatEntity == null)
            return;
        if (combatEntity.getOpponent() != null) {
            if (combatEntity.getOpponent().getAttackers().contains(combatEntity)) {
                combatEntity.getOpponent().getAttackers().remove(combatEntity);
            }
            combatEntity.setOpponent(null);
        }
    }

    public static void logoutReset(CombatEntity combatEntity) {
        if (combatEntity == null)
            return;
        if (combatEntity.getAttackers().size() > 0) {
            CombatEntity c3[] = new CombatEntity[combatEntity.getAttackers().size()];
            int i = 0;
            for (CombatEntity c4 : combatEntity.getAttackers()) {
                c3[i] = c4;
                i++;
            }
            for (CombatEntity c2 : c3) {
                resetAttack(c2);
            }
            c3 = null;
        }
        combatEntity.getAttackers().clear();
        resetAttack(combatEntity);
    }

    public static int getRealLevel(CombatEntity combatEntity, CombatEntity b) {
        int a = getWildLevel(combatEntity.getAbsX(), combatEntity.getAbsY(), combatEntity.getAbsZ());
        int d = getWildLevel(b.getAbsX(), b.getAbsY(), b.getAbsZ());
        return Math.min(a, d);
    }

    public static int getWildLevel(int absX, int absY) {
        return getWildLevel(absX, absY, 0);
    }

    public static int getWildLevel(int absX, int absY, int absZ) {
        for (SpecialArea area : SpecialAreaHolder.getAreas())
            if (area.isPkArea() && area.inArea(absX, absY, absZ))
                return area.getPkLevel();
        if ((absY >= 10340 && absY <= 10364 && absX <= 3008 && absX >= 2992))
            return (((absY - 10340) / 8) + 3);
        else if ((absY >= 3520 && absY <= 3967 && absX <= 3392 && absX >= 2942))
            return (((absY - 3520) / 8) + 3);
        else if (absY <= 10349 && absX >= 3010 && absX <= 3058 && absY >= 10306) //stair case nigga shit
            return 57;
        else if (absX >= 3064 && absX <= 3070 && absY >= 10252 && absY <= 10260)
            return 53;
        else if (DangerousPK.inDangerousPK(absX, absY))
            return 12;
        else
            return -1;
    }

    public static boolean isInMulti(CombatEntity combatEntity) {
        if (combatEntity == null || combatEntity.getEntity() == null)
            return false;
        if (WildernessBossTask.currentBoss != null)
            if (Misc.isInCircle(WildernessBossTask.currentBoss.getPosition().getX(), WildernessBossTask.currentBoss.getPosition().getY(), combatEntity.getEntity().getPosition().getX(), combatEntity.getEntity().getPosition().getY(), 10))
                return true;
        if ((combatEntity.getAbsX() >= 3136 && combatEntity.getAbsX() <= 3327
                && combatEntity.getAbsY() >= 3520 && combatEntity.getAbsY() <= 3607)
                || (combatEntity.getAbsX() >= 3190
                && combatEntity.getAbsX() <= 3327
                && combatEntity.getAbsY() >= 3648 && combatEntity.getAbsY() <= 3839)
                || (combatEntity.getAbsX() >= 3200
                && combatEntity.getAbsX() <= 3390
                && combatEntity.getAbsY() >= 3840 && combatEntity.getAbsY() <= 3967)
                || (combatEntity.getAbsX() >= 2992
                && combatEntity.getAbsX() <= 3007
                && combatEntity.getAbsY() >= 3912 && combatEntity.getAbsY() <= 3967)
                || (combatEntity.getAbsX() >= 2946
                && combatEntity.getAbsX() <= 2959
                && combatEntity.getAbsY() >= 3816 && combatEntity.getAbsY() <= 3831)
                || (combatEntity.getAbsX() >= 3008
                && combatEntity.getAbsX() <= 3199
                && combatEntity.getAbsY() >= 3856 && combatEntity.getAbsY() <= 3903)
                || (combatEntity.getAbsX() >= 3008
                && combatEntity.getAbsX() <= 3071
                && combatEntity.getAbsY() >= 3600 && combatEntity.getAbsY() <= 3711)
                || (combatEntity.getAbsX() >= 2889
                && combatEntity.getAbsX() <= 2941
                && combatEntity.getAbsY() >= 4426 && combatEntity.getAbsY() <= 4465)
                || // dag kings
                (combatEntity.getAbsX() >= 2460
                        && combatEntity.getAbsX() <= 2557
                        && combatEntity.getAbsY() >= 3264 && combatEntity.getAbsY() <= 3335)
                || // Wilderness agility downstairs
                (combatEntity.getAbsX() >= 2992
                        && combatEntity.getAbsX() <= 3008
                        && combatEntity.getAbsY() >= 10340 && combatEntity.getAbsY() <= 10364)
                || // fun pk multi
                (combatEntity.getAbsX() >= 3071
                        && combatEntity.getAbsX() <= 3146
                        && combatEntity.getAbsY() >= 3394 && combatEntity.getAbsY() <= 3451)
                || // barb
                (combatEntity.getAbsX() >= 2814
                        && combatEntity.getAbsX() <= 2942
                        && combatEntity.getAbsY() >= 5250 && combatEntity.getAbsY() <= 5373)
                || // godwars
                (combatEntity.getAbsX() >= 3072
                        && combatEntity.getAbsX() <= 3327
                        && combatEntity.getAbsY() >= 3608 && combatEntity.getAbsY() <= 3647)
                || //corp beast
                (combatEntity.getAbsX() >= 2500 && combatEntity.getAbsY() >= 4630 &&
                        combatEntity.getAbsX() <= 2539 && combatEntity.getAbsY() <= 4660)
                ||
                (combatEntity.getAbsX() >= 2343 && combatEntity.getAbsY() >= 9823 &&
                        combatEntity.getAbsX() <= 2354 && combatEntity.getAbsY() <= 9834)
                ||
                (combatEntity.getAbsX() >= 2256 && combatEntity.getAbsY() >= 4680 &&
                        combatEntity.getAbsX() <= 2287 && combatEntity.getAbsY() <= 4711 && combatEntity.getAbsZ() == 0)
                || inNonSpawnMulti(combatEntity.getAbsX(), combatEntity.getAbsY()) || Position.create(combatEntity.getAbsX(), combatEntity.getAbsY(), 0).inFunPk()
                || (LastManStanding.inLMSArea(combatEntity.getAbsX(), combatEntity.getAbsY()))
                || (combatEntity.getAbsZ() > 0 && combatEntity.getAbsX() > 3540 && combatEntity.getAbsX() < 3585 && combatEntity.getAbsY() > 9935 && combatEntity.getAbsY() < 9975))
            return true;
        if (combatEntity.getEntity() instanceof Player) {
            if (ContentManager.handlePacket(ClickType.OBJECT_CLICK1, combatEntity.getPlayer(), ClickId.ATTACKABLE))
                return true;
        }
        return false;
    }

    public static boolean inNonSpawnMulti(final int x, final int y) {
        return (x > 2640 && y > 9615 && x < 2673 && y < 9669) || (x > 3028 && y > 3827 && x < 3112 && y < 3854) ||
                (x < 3332 && y < 5566 && x > 3133 && y > 5433);
    }

    public static int random(int range) {
        return (int) (java.lang.Math.random() * (range + 1));
    }

    public static void follow(final CombatEntity combatEntity, final CombatEntity opponent) {
        if (combatEntity.isFrozen())
            return;
        if (combatEntity._getPlayer().isPresent()) {
            follow3(combatEntity, opponent);
        } else {
            follow3(combatEntity, opponent);
        }

    }

    public static void follow3(final CombatEntity combatEntity, final CombatEntity opponent) {
        try {
            if (combatEntity.getPlayer() != null && combatEntity.getPlayer().isDead()) {
                return;
            }
            int dis = combatEntity.getEntity().getPosition().distance(opponent.getEntity().getPosition());
            if (dis > 20 || dis < 1)
                return;

            combatEntity.face(opponent.getAbsX(), opponent.getAbsY());

            combatEntity.getEntity().setInteractingEntity(opponent.getEntity());


            int toX = opponent.getAbsX();
            int toY = opponent.getAbsY();
            //System.out.println("X : " + startx + " Y : " + starty);
            if (opponent.getEntity().getWalkingQueue().getPublicPoint() != null) {
                toX = opponent.getEntity().getWalkingQueue().getPublicPoint().getX();
                toY = opponent.getEntity().getWalkingQueue().getPublicPoint().getY();
            }
            int baseX = combatEntity.getAbsX() - 25;
            int baseY = combatEntity.getAbsY() - 25;
            combatEntity.getEntity().getWalkingQueue().reset();
            Path p = PathTest.getPath(combatEntity.getAbsX(), combatEntity.getAbsY(), toX, toY);
            if (p != null) {
                for (int i = 1; i < p.getLength(); i++) {
                    //player.getActionSender().sendMessage((baseX+p.getX(i))+"	"+(baseY+p.getY(i)));
                    if (!WorldMap.checkPos(combatEntity.getAbsZ(), combatEntity.getAbsX(), combatEntity.getAbsY(), baseX + p.getX(i), baseY + p.getY(i), 0))
                        break;
                    if ((baseX + p.getX(i)) != toX || (baseY + p.getY(i)) != toY)
                        combatEntity.getEntity().getWalkingQueue().addStep((baseX + p.getX(i)), (baseY + p.getY(i)));
                }
                combatEntity.getEntity().getWalkingQueue().finish();
            } else {
                //System.out.println("Derp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		/*combatEntity.getEntity().getWalkingQueue().reset();
		if(combatEntity.getEntity() instanceof Player) {
			follow2(combatEntity, combatEntity.getAbsX(), combatEntity.getAbsY(), opponent.getAbsX(), opponent.getAbsY(), opponent.getAbsZ());
			// follow2(combatEntity,combatEntity.getEntity().getWalkingQueue().getPublicPoint().getX(),combatEntity.getEntity().getWalkingQueue().getPublicPoint().getY(),opponent.getAbsX(),opponent.getAbsY(),opponent.getAbsZ());
		} else
			follow2(combatEntity, combatEntity.getAbsX(), combatEntity.getAbsY(), opponent.getAbsX(), opponent.getAbsY(), opponent.getAbsZ());
		combatEntity.getEntity().getWalkingQueue().finish();   */
    }

    public static void follow2(final CombatEntity combatEntity, int x, int y, int toX, int toY, int height) {

      /*  try {
            long time = System.currentTimeMillis();
		    int path[][] = PathfinderV2.findRoute(x, y, toX, toY, height);
            if(path == null) return;
            combatEntity.getEntity().getWalkingQueue().reset();
            for(int[] p : path) {
                if(p[0] != toX || p[1] != toY)
                    combatEntity.getEntity().getWalkingQueue().addStep(p[0], p[1]);
                System.out.printf("%d , %d\n", p[0], p[1]);
            }
            combatEntity.getEntity().getWalkingQueue().finish();
            System.out.println("Took: "+(System.currentTimeMillis() - time));
        }catch(Exception e) {

        } */

        int moveX = 0;
        int moveY = 0;

        if (x > toX)
            moveX = -1;
        else if (x < toX)
            moveX = 1;
        if (y > toY)
            moveY = -1;
        else if (y < toY)
            moveY = 1;
        if (moveX != 0 && moveY != 0) {
            if (!WorldMap.checkPos(height, x, y, (x + moveX), (y + moveY), 0)) {
                if (WorldMap.checkPos(height, x, y, (x + moveX), y, 0)) {
                    moveY = 0;
                } else if (WorldMap.checkPos(height, x, y, x, (y + moveY), 0)) {
                    moveX = 0;
                } else {
                    return;
                }
            }
        } else if (!WorldMap.checkPos(height, x, y, x + moveX, y
                + moveY, 0)) {
            if (moveX != 0) {
                if (!WorldMap.checkPos(height, x, y, x + moveX, y
                        + 1, 0)) {
                    moveY = 1;
                } else if (!WorldMap.checkPos(height, x, y, x + moveX, y
                        - 1, 0)) {
                    moveY = -1;
                }
            } else if (moveY != 0) {
                if (!WorldMap.checkPos(height, x, y, x + 1, y
                        + moveY, 0)) {
                    moveX = 1;
                } else if (!WorldMap.checkPos(height, x, y, x - 1, y
                        + moveY, 0)) {
                    moveX = -1;
                }
            }
        }
        combatEntity.getEntity().getWalkingQueue().addStep(x + moveX, y + moveY);
    }

    /**
     * This method is used to poison a CombatEntility.
     *
     * @param combatEntity
     */
    public static void poisonEntity(final CombatEntity combatEntity) {
        if (combatEntity == null)
            return;
        if (combatEntity.isPoisoned())
            return;
        if (combatEntity.getPlayer() != null)
            combatEntity.getPlayer().getActionSender().sendMessage("You have been poisoned.");
        combatEntity.setPoisoned(true);
        TaskManager.submit(new Task(16000,"poisoned") {
            private int lastDamg = -1;
            private int ticks = 4;

            @Override
            public void execute() {
                if (!combatEntity.isPoisoned()) {
                    this.stop();
                    return;
                }
                if (combatEntity.getEntity() instanceof Player) {
                    if (!combatEntity.getPlayer().isActive()) {
                        this.stop();
                        return;
                    }
                }
                if (lastDamg == -1)
                    lastDamg = random(7);
                if (ticks == 0) {
                    lastDamg--;
                    ticks = 4;
                }
                ticks--;
                if (lastDamg == 0) {
                    if (combatEntity.getPlayer() != null)
                        combatEntity.getPlayer().getActionSender().sendMessage("Your poison clears up.");
                    combatEntity.setPoisoned(false);
                    this.stop();
                } else {
                    combatEntity.hit(lastDamg, null, true, 0);
                }
            }
        });
    }

    public void ateFood(final CombatEntity combatEntity) {
        if (combatEntity.predictedAtk > System.currentTimeMillis() + 1000)// this
            // should
            // make
            // sure,
            // you
            // dont
            // eat
            // and
            // hit
            // at
            // the
            // same
            // time.
            return;
        combatEntity.predictedAtk = Math.max(System.currentTimeMillis() + 1000, combatEntity.predictedAtk);
        // combatEntity.predictedAtk2 = System.currentTimeMillis()+1000;
        // combatEntity.predictedAtk3 = System.currentTimeMillis()+1000;
    }

    public static boolean canAtkDis(final CombatEntity combatEntity, final CombatEntity attack) {
        int distance = combatEntity.getEntity().getPosition().distance(attack.getEntity().getPosition());
        if (distance > 1) {
            //System.out.println("Distance check can atk " + distance);
            return WorldMap.projectileClear(combatEntity.getEntity().getPosition(), combatEntity.getOpponent().getEntity().getPosition());
        } else {
            //System.out.println("Pos check can atk");
            return WorldMap.checkPos(attack.getEntity().getPosition().getZ(), combatEntity.getEntity().getPosition().getX(), combatEntity.getEntity().getPosition().getY(), attack.getEntity().getPosition().getX(), attack.getEntity().getPosition().getY(), 1);
        }
    }

    public static void removeArrow(Player player, int bowType, Position loc) {
        if (player.getEquipment().get(Equipment.SLOT_WEAPON) == null)
            return;
        int slot = Equipment.SLOT_ARROWS;
        if (bowType == Constants.RANGEDWEPSTYPE) {
            slot = Equipment.SLOT_WEAPON;
        }
        if (player.getEquipment().get(slot) != null) {
            Item item = new Item(player.getEquipment().get(slot).getId(), 1);
            if (item.getId() != 4740 && item.getId() != 15243) {
                if (random(3) != 1) {
                    if (player.getEquipment().get(Equipment.SLOT_CAPE) != null
                            && player.getEquipment().get(Equipment.SLOT_CAPE).getId() == 10499) {
                        // player.getInventory().add(item);
                        return;
                    } else {
                        GlobalItemManager.newDropItem(player, new GlobalItem(player, loc, item));
                    }
                }
            }
        }
        if (player.getEquipment().get(slot).getCount() <= 1)
            player.getEquipment().set(slot, null);
        else
            player.getEquipment().set(slot, new Item(player.getEquipment().get(slot).getId(), (player.getEquipment().get(slot).getCount() - 1)));

    }

    public static void addXP(Player player, int damg, boolean bow) {
        if (player == null)
            return;
        if (damg > 0) {
            int exp = damg * 4;
            if (!player.getPosition().inPvPArea())
                exp = damg * 300;
            if (player.getSkills().getLevelForExp(player.cE.getAtkType()) >= 90 && !player.getPosition().inPvPArea())
                exp = damg * 800;
            if (player.cE.getAtkType() == 3) {
                player.cE.setAtkType(2);
            }
            if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
                if ((player.cE.getAtkType() == 5 || player.cE.getAtkType() == 2)
                        && CombatAssistant.isControlled(player.getEquipment().get(Equipment.SLOT_WEAPON).getId())) {
                    player.cE.setAtkType(3);
                }
            }
            if (player.cE.getAtkType() == 6 && bow) {
                player.getSkills().addExperience(4, 0.66 * exp);
                player.getSkills().addExperience(3, 0.33 * exp);
                player.getSkills().addExperience(1, 0.33 * exp);
                return;
            }
            if (player.cE.getAtkType() == 6 && !bow) {
                player.cE.setAtkType(1);
            }
            if (player.cE.getAtkType() == 5 && bow) {
                player.getSkills().addExperience(4, exp);
                player.getSkills().addExperience(3, 0.33 * exp);
                return;
            }
            if (player.cE.getAtkType() == 5 && !bow) {
                player.cE.setAtkType(2);
            }
            if (player.cE.getAtkType() == 4 && bow) {
                player.getSkills().addExperience(4, exp);
                player.getSkills().addExperience(3, 0.33 * exp);
                return;
            }
            if (player.cE.getAtkType() == 4 && !bow) {
                player.cE.setAtkType(0);
            }
            if (player.cE.getAtkType() == 1 && bow) {
                player.cE.setAtkType(6);
            } else if (bow) {
                player.getSkills().addExperience(4, exp);
                player.getSkills().addExperience(3, 0.33 * exp);
            } else if (player.cE.getAtkType() != 3) {
                player.getSkills().addExperience(player.cE.getAtkType(), exp);
                player.getSkills().addExperience(3, 0.33 * exp);
            } else {
                player.getSkills().addExperience(0, 0.33 * exp);
                player.getSkills().addExperience(1, 0.33 * exp);
                player.getSkills().addExperience(2, 0.33 * exp);
                player.getSkills().addExperience(3, 0.33 * exp);
            }
        }
    }

    public static boolean usingPhoenixNecklace(Player player) {
        if (player.getEquipment().get(Equipment.SLOT_AMULET) == null)
            return false;
        return player.getEquipment().get(Equipment.SLOT_AMULET).getId() == 11090;
    }

    public static boolean ringOfLifeEqupped(Player player) {
        if (player.getEquipment().get(Equipment.SLOT_RING) == null)
            return false;
        return player.getEquipment().get(Equipment.SLOT_RING).getId() == 2570;
    }

    public static boolean isGuthanEquiped(Player player) {
        if (player.getEquipment().get(Equipment.SLOT_HELM) == null
                || player.getEquipment().get(Equipment.SLOT_WEAPON) == null
                || player.getEquipment().get(Equipment.SLOT_CHEST) == null
                || player.getEquipment().get(Equipment.SLOT_BOTTOMS) == null)
            return false;
        return player.getEquipment().get(Equipment.SLOT_HELM).getId() == 4724
                && player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 4726
                && player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 4728
                && player.getEquipment().get(Equipment.SLOT_BOTTOMS).getId() == 4730;
    }

    public static boolean isToragEquiped(Player player) {
        if (player.getEquipment().get(Equipment.SLOT_HELM) == null
                || player.getEquipment().get(Equipment.SLOT_WEAPON) == null
                || player.getEquipment().get(Equipment.SLOT_CHEST) == null
                || player.getEquipment().get(Equipment.SLOT_BOTTOMS) == null)
            return false;
        return player.getEquipment().get(Equipment.SLOT_HELM).getId() == 4745
                && player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 4747
                && player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 4749
                && player.getEquipment().get(Equipment.SLOT_BOTTOMS).getId() == 4751;
    }

    public static void createGlobalProjectile(Entity e, int casterY, int casterX, int offsetY, int offsetX, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int slope) {
        if (e == null)
            return;
        for (Player p : e.getLocalPlayers()) {
            p.getActionSender().createProjectile(casterY, casterX, offsetY, offsetX, angle, speed, gfxMoving, startHeight, endHeight, lockon, slope);
        }
    }

}