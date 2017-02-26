package org.hyperion.rs2.model.content.skill;

//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import org.hyperion.data.PersistenceManager;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.achievements.AchievementHandler;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.minigame.RecipeForDisaster;
import org.hyperion.rs2.model.content.misc.PrayerIcon;
import org.hyperion.util.Misc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

/**
 * Prayer skill handler
 *
 * @author Graham
 */
public class Prayer implements ContentTemplate {

    public static final int EXP_MULTIPLIER = 4 * Constants.XPRATE;

    public final static int[][] BONESEXP = {{526, 5}, // NPC BONES
            {528, 5}, // BURNT BONES
            {530, 5}, // BAT BONES
            {2859, 5}, // WOLF BONES
            {3179, 5}, // MONKEY BONES
            {3180, 5}, // MONKEY BONES
            {3181, 5}, // MONKEY BONES
            {3182, 5}, // MONKEY BONES
            {3183, 5}, // MONKEY BONES
            {3185, 5}, // MONKEY BONES
            {3186, 5}, // MONKEY BONES
            {3187, 5}, // MONKEY BONES
            {532, 15}, // BIG BONES
            {534, 30}, // BABY DRAGON BONES
            {536, 72}, // DRAGON BONES
            {2530, 5}, // PLAYER BONES
            {3123, 25}, // SHAIKAHAN BONES
            {3125, 23}, // JOGRE BONES
            {3127, 25}, // BURNT JOGRE BONES
            {4812, 82}, // ZOGRE BONES
            {4830, 84}, // FAYGR BONES
            {4832, 96}, // RAURG BONES
            {4834, 140}, // OURG BONES
            {6729, 125}, // DAGANNOTH BONES
            {6812, 50}, // WYVERN BONES
            {10976, 1000}, // LONG BONE
            {10977, 1250}, // CURVED BONE
            {11337, 625}, // MANGLED BONE
            {11338, 2500}, // CHEWED BONE
    };

    public boolean buryBones(final Player player, final int fromSlot, int item, final boolean altar, int multiplier) {
        final int buryItem = item;
        int buryXP = getBonesExp(buryItem);
        buryXP *= multiplier;
        if (player.getEquipment().get(Equipment.SLOT_AMULET) != null
                && player.getEquipment().get(Equipment.SLOT_AMULET).getId() == 1718) {
            buryXP *= 2;
        }
        if (buryXP == -1) {
            return false;
        }
        if (player.isBusy()) {
            return true;
        }
        if (player.getExtraData().get("prayerburytimer") != null) {
            return true;
        }
        player.getExtraData().put("prayerburytimer", 1);
        player.getWalkingQueue().reset();
        player.setBusy(true);
        player.setCanWalk(false);
        if (altar) {
            buryXP *= 2;
            player.getActionSender().sendMessage("The gods are pleased with your offerings.");
            player.playAnimation(Animation.create(896, 0));
        } else {
            player.playAnimation(Animation.create(827, 0));
        }
        ContentEntity.deleteItem(player, buryItem);
        final int fBuryXP = buryXP;
        World.submit(new Task(2000L, "prayer") {
            @Override
            public void execute() {
                if (!player.isBusy()) {
                    stop2();
                    return;
                }
                if (player.getRandomEvent().skillAction(4)) {
                    this.stop();
                    return;
                }
                ContentEntity.addSkillXP(player, fBuryXP * EXP_MULTIPLIER, 5);
                if (!altar) {
                    ContentEntity.sendMessage(player, "You bury the bones.");
                }
                stop2();
            }

            public void stop2() {
                player.getExtraData().remove("prayerburytimer");
                player.setBusy(false);
                player.setCanWalk(true);
                this.stop();
            }
        });
        return true;
    }

    public static int getBonesExp(int id) {
        for (int i = 0; i < BONESEXP.length; i++) {
            if (id == BONESEXP[i][0]) {
                return BONESEXP[i][1];
            }
        }
        return -1;
    }

    public static void retribution(final Player player) {
        player.playGraphics(Graphic.create(437));
        if (player.getCombat().getOpponent() != null) {
            player.getCombat().getOpponent()
                    ._getPlayer()
                    .filter(target -> target != null && !target.isDead())
                    .ifPresent(target -> player.inflictDamage(new Damage.Hit(Misc.random(22), Damage.HitType.NORMAL_DAMAGE, Constants.EMPTY)));
        }
    }

    public static void wrath(final Player player) {
        player.playGraphics(Graphic.create(2259));
        if (player.getCombat().getOpponent() != null) {
            player.getCombat().getOpponent()._getPlayer().filter(target -> target != null && !target.isDead()).ifPresent(target -> target.inflictDamage(new Damage.Hit(Misc.random(28), Damage.HitType.NORMAL_DAMAGE, Constants.EMPTY)));
        }
        if (player.getLocation().isMulti()) {
            player.getLocalPlayers()
                    .stream()
                    .filter(target -> target.cE != null && !target.isDead() && Combat.canAtk(player.cE, target.cE).length() <= 2 && !player.cE.equals(target.getCombat().getOpponent()) && target.getLocation().isMulti())
                    .forEach(target -> {
                target.playGraphics(Graphic.create(2260));
                target.inflictDamage(new Damage.Hit(Misc.random(25), Damage.HitType.NORMAL_DAMAGE, Constants.EMPTY));
            });
        }
    }

    public static void redemption(Player p) {
        if (p.getPrayers().isEnabled(22)) {
            if (p.getSkills().getLevel(3) < (p.getSkills().getLevelForExp(3) / 10)
                    && p.getSkills().getLevel(3) > 0) {
                p.cE.doGfx(436, 0);
                p.getSkills().detractLevel(5, p.getSkills().getLevel(5));
                p.heal(p.getSkills().getLevelForExp(3) / 5);
            }
        }
    }

    public static void smite(Player p, double hit) {
        hit = (hit / 4);
        p.getSkills().detractLevel(5, (int) hit);
    }

    public static void soulSplit(final Player player, final CombatEntity victim, int damg) {
        if (!(System.currentTimeMillis() - player.lastTimeSoulSplit > 1800))
            return;
        if (victim == null || victim.getEntity() == null)
            return;
        player.lastTimeSoulSplit = System.currentTimeMillis();
        if (victim.getEntity() instanceof Player) {
            int amount = (int) (damg * 0.2);// 40%
            victim.getPlayer().getSkills().detractLevel(5, amount);
            if (!player.isDead())
                player.heal(amount);
        } else {
            int amount = (int) (damg * 0.2);// 20%
            if (!player.isDead())
                player.heal(amount);
        }
        int offsetY = (player.cE.getAbsX() - victim.getAbsX()) * -1;
        int offsetX = (player.cE.getAbsY() - victim.getAbsY()) * -1;
        // find our lockon target
        final int hitId = player.cE.getSlotId(player.cE.getEntity());
        final int speed = 70;
        final int slope = 0;
        final int time = 24;
        final int distance = player.getPosition().distance(victim.getEntity().getPosition());
        if (distance < 20)
            player.getActionSender().createGlobalProjectile(player.cE.getAbsY(), player.cE.getAbsX(), offsetY, offsetX, 50, speed, 2263, 20, 9, hitId, time, slope);
        /* UNTIL THIS */
        World.submit(new Task(1200, "prayer2") {
            public void execute() {
                try {
                    if (victim != null)
                        victim.doGfx(2264, 0);
                    if (victim.getEntity() instanceof Player && distance < 20) {
                        int offsetY = (victim.getAbsX() - player.cE.getAbsX())
                                * -1;
                        int offsetX = (victim.getAbsY() - player.cE.getAbsY())
                                * -1;
                        victim.getPlayer().getActionSender().createGlobalProjectile(victim.getAbsY(), victim.getAbsX(), offsetY, offsetX, 50, speed, 2263, 20, 9, hitId, time, slope);
                    }
                    this.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                    this.stop();
                }
            }
        });

    }

    private static List<PrayerIcon> prayers;


    public static void zaniksEffect(final Player player, int damage) {
        if (player == null || player.getCombat().getOpponent() == null)
            return;
        if (player.getEquipment().getItemId(Equipment.SLOT_WEAPON) != 14684)
            return;
        final int time = damage * 150 + 2000;
        player.getCombat().getOpponent()._getPlayer().ifPresent(opp -> {


            for (int i : Prayers.OVERHEADS) {


                if (opp.getPrayers().isEnabled(i)) {
                    final int idx = Prayer.getIndex(i);
                    if (i == -1)
                        continue;
                    PrayerIcon p2 = prayers.get(idx);
                    opp.getActionSender().sendClientConfig(p2.getFrame(), 0);
                    opp.getPrayers().setEnabled(p2.getId(), false);
                    opp.setDrainRate(getPryDrain(opp));
                    setHeadIcon(opp, p2);
                    player.forceMessage("The gods cannot save you now!");
                }
            }

            opp.getPrayers().disableFor(time, Prayers.OVERHEADS);


        });
    }

    public static void dragonScimitar(Player player) {

        int time = 6000;
        if (player.cE.getOpponent() != null && player.cE.getOpponent().getEntity() instanceof Player) {
            Player opponent = player.cE.getOpponent().getPlayer();
            if (opponent != null) {
                if (Rank.isAbilityToggled(opponent, Rank.DONATOR))
                    time += 2000;
                if (Rank.isAbilityToggled(opponent, Rank.SUPER_DONATOR))
                    time += 2000;
            }
        }

        for (int i : Prayers.OVERHEADS) {

            if (player.getPrayers().isEnabled(i)) {
                final int idx = getIndex(i);
                if (i == -1)
                    continue;
                PrayerIcon p2 = prayers.get(idx);
                player.getActionSender().sendClientConfig(p2.getFrame(), 0);
                player.getPrayers().setEnabled(p2.getId(), false);
                player.setDrainRate(getPryDrain(player));
                setHeadIcon(player, p2);
            }
        }
        player.getPrayers().disableFor(time, Prayers.OVERHEADS);

    }

    public static void resetInterface(Player player) {
        if (prayers == null)
            return;
        for (PrayerIcon prayer : prayers) {
            player.getActionSender().sendClientConfig(prayer.getFrame(), 0);
        }
    }

    public void rechargePrayer(Player player) {
        player.playAnimation(Animation.create(645, 0));
        player.getSkills().setLevel(5, player.getSkills().getLevelForExp(5));
        player.getSummBar().increment(100);
        player.getActionSender().sendString(38760, player.getSummBar().getAmount() + "");
        player.getActionSender().sendSkill(24);
        player.getActionSender().sendMessage("You recharge your prayer and summoning points.");
        /*
         * if(c.playerLevel[5] >=
		 * c.getActionAssistant().getLevelForXP(c,c.playerXP[5])) {
		 * c.getActionAssistant
		 * ().sendMessage(c,"You have already full Prayer points."); } else {
		 * c.setPrayerLevel
		 * (c.getActionAssistant().getLevelForXP(c,c.playerXP[5]));
		 * c.getActionAssistant().startAnimation(c,250);
		 * c.getActionAssistant().sendMessage
		 * (c,"You recharge your Prayer points."); c.playerLevel[5] =
		 * c.getActionAssistant().getLevelForXP(c,c.playerXP[5]);
		 * c.getActionAssistant().setSkillLevel(c,5, c.playerLevel[5],
		 * c.playerXP[5]); }
		 */
    }

    /**
     * Gets the index of the item
     *
     * @param item The item of which index has to be returned.
     */

    public static int getIndex(int item) {
        for (PrayerIcon p : prayers) {
            if (p.getId() == item)
                return prayers.indexOf(p);
        }
        return -1;
    }

    public static double getPryDrain(Player c) {
        double start = 0.0;// so if multiable prayers are on, it drains faster
        for (int i = 0; i < Prayers.SIZE; i++) {
            for (PrayerIcon p : prayers) {
                if (p.getId() == i && c.getPrayers().isEnabled(i))
                    start += p.getDrain();
            }
        }
        return start;
    }

    /**
     * Activates a prayer.
     *
     * @param c  The client.
     * @param id The id of the prayer.
     */
    @Override
    public boolean clickObject(Player p, int type, int id, int itemSlot, int c, int d) {
        // System.out.println(" Type : " + type + " Id : " + id + " ItemSlot " +
        //itemSlot + "  C  " + c + "  D  " + d);
        if (type == 1)
            buryBones(p, itemSlot, id, false, 1);
        if (type == 14) {
            if (c == 409)
                buryBones(p, itemSlot, id, true, 2);
            else if (c == 13192)
                buryBones(p, itemSlot, id, true, 4);
            else {
                p.getActionSender().sendMessage("This altar doesn't seem to be setup for bone offering.");
                return false;
            }
        }
        if (type == 6)
            switch (id) {
                case 61:
                    changeCurses(p);
                    break;
                case 410:
                    changeLunars(p);
                    break;
                case 6552:
                    changeAncients(p);
                    break;
                default:
                    rechargePrayer(p);
            }

        /*
         * prayer -= 25000; prayer = (int) prayer / 2;
		 */
        // p.getActionSender().sendMessage("Inside the clickObject method");
        // p.increaseSkullTimer();
        if (RecipeForDisaster.inRFD(p)) {
            p.getActionSender().sendMessage("You cannot use prayer in this minigame.");
            p.resetPrayers();
            return false;
        }

        id = getPrayerForActionButton(id);
        if (getIndex(id) == -1)
            return false;
        PrayerIcon p2 = prayers.get(getIndex(id));


        for (Iterator<Prayers.DisabledPrayer> it = p.getPrayers().getDisabled().iterator(); it.hasNext(); ) {
            Prayers.DisabledPrayer disabledPrayer = it.next();
            long disabledFor = System.currentTimeMillis() - disabledPrayer.getPrayersDisabledAt();
            if (disabledFor <= disabledPrayer.getPrayersDisabledFor()) {
                long timeRemaining = (disabledPrayer.getPrayersDisabledFor() - disabledFor) / 1000;
                for (int prayer : disabledPrayer.getPrayersDisabled()) {
                    if (prayer == p2.getId()) {
                        p.getActionSender().sendMessage("This prayer is disabled for another: " + timeRemaining);
                        p.getActionSender().sendClientConfig(p2.getFrame(), 0);
                        return true;
                    }
                }
            } else
                it.remove();
        }

        if (p2.isCurse()
                && p.getSkills().getLevelForExp(Skills.DEFENCE) < 30) {
            p.getActionSender().sendMessage("You need a Defense level of 30 to use this prayer.");
            p.resetPrayers();
            return true;
        }

        // Initialize the prayerLevel (double).
        // c.setPrayerLevel(c.playerLevel[5]);

        // Check if the player has prayer left.
        if (p.getSkills().getLevel(Skills.PRAYER) <= 0) {
            p.getActionSender().sendClientConfig(p2.getFrame(), 0);
            p.getActionSender().sendMessage("You need to recharge your Prayer at a altar.");
            return true;
        }

        if (p.duelRule[7] && p.duelAttackable > 0) {
            p.getActionSender().sendMessage("You cannot use prayer in this duel.");
            return true;
        }

        // Check if the player has a high enough level.
        if (p.getSkills().getLevelForExp(5) < p2.getLevel()) {
            p.getActionSender().sendClientConfig(p2.getFrame(), 0);
            p.getActionSender().sendMessage("You need a Prayer level of "
                    + p2.getLevel() + " to use " + p2.getName() + ".");
            p.getActionSender().sendString(357, "You need a @dbl@Prayer level of "
                    + p2.getLevel() + " to use " + p2.getName() + ".");
            p.getActionSender().sendPacket164(356);
            return true;
        }

        if (p.getSkills().getLevelForExp(1) < 70
                && p2.getName().equals("Piety")) {
            p.getActionSender().sendClientConfig(p2.getFrame(), 0);
            p.getActionSender().sendMessage("You need a Defence level of 70 to use "
                    + p2.getName() + ".");
            p.getActionSender().sendString(357, "You need a @dbl@Defence level of 70 to use "
                    + p2.getName() + ".");
            p.getActionSender().sendPacket164(356);
            return true;
        }
        if (p.getSkills().getLevelForExp(1) < 60
                && p2.getName().equals("Chivalry")) {
            p.getActionSender().sendClientConfig(p2.getFrame(), 0);
            p.getActionSender().sendMessage("You need a Defence level of 60 to use "
                    + p2.getName() + ".");
            p.getActionSender().sendString(357, "You need a @dbl@Defence level of 60 to use "
                    + p2.getName() + ".");
            p.getActionSender().sendPacket164(356);
            return true;
        }

        if (!hasUnlocked(p, p2.getId())) {
            p.getActionSender().sendClientConfig(p2.getFrame(), 0);
            p.getActionSender().sendMessage("You haven't unlocked "
                    + p2.getName() + ".");
            p.getActionSender().sendString(357, "You haven't unlocked "
                    + p2.getName() + ".");
            p.getActionSender().sendPacket164(356);
            return true;
        }

        // Check if the prayer is already activated.
        if (p.getPrayers().isEnabled(p2.getId())) {
            p.getActionSender().sendClientConfig(p2.getFrame(), 0);
            p.getPrayers().setEnabled(p2.getId(), false);
            // p.setDrainRate(-p.getDrain());
            p.setDrainRate(getPryDrain(p));
            setHeadIcon(p, p2);
        } else {
            if (!p.getPrayers().isDefaultPrayerbook() && p2.getId() < 30) {
                p.resetPrayers();
                return false;
            } else if (p.getPrayers().isDefaultPrayerbook() && p2.getId() >= 30) {
                p.resetPrayers();
                return false;
            }
            if (p2.getId() == 30) {
                //12567 ,2213
                if (!p.getPosition().inPvPArea())
                    ContentEntity.startAnimation(p, 12567);
                ContentEntity.playerGfx(p, 2213, 0);
            } else if (p2.getId() == 35) {
                ContentEntity.playerGfx(p, 2266, 0);
                if (!p.getPosition().inPvPArea())
                    ContentEntity.startAnimation(p, 12589);
            } else if (p2.getId() == 49) {
                ContentEntity.playerGfx(p, 2226, 0);
                if (!p.getPosition().inPvPArea())
                    ContentEntity.startAnimation(p, 12565);
                //IMPORTANT LINE OF CODE! p.canLeech.clear();
            }
            /*
             * System.out.println("0; "+p.getPrayers()[0]);
			 * System.out.println("1; "+p.getPrayers()[1]);
			 * System.out.println("0 bol; "+p.getPrayers().isEnabled(p.getPrayers()[0]]);
			 * System.out.println("1 bol; "+p.getPrayers().isEnabled(p.getPrayers()[1]]);
			 */
            for (int j = 0; j < p2.getPrayers().length; j++) {
                if ((p2.getPrayers()[j] != -1)
                        && (p.getPrayers().isEnabled(p2.getPrayers()[j]))) {
                    PrayerIcon p1 = prayers.get(getIndex(p2.getPrayers()[j]));
                    // Set the config to 0.
                    p.getActionSender().sendClientConfig(p1.getFrame(), 0);
                    // Delete the drain of them.
                    /*
                     * p.setDrainRate(-p1.getDrain());
					 * p.setDrainRate(-p2.getDrain());
					 */
                    // Set the prayers to false.
                    p.getPrayers().setEnabled(p1.getId(), false);
                    // Reset headicon.
                    // TODO
                    p.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                }
            }

            // Set the prayer to true (activating)
            p.getPrayers().setEnabled(p2.getId(), true);
            // Light up the prayer.
            p.getActionSender().sendClientConfig(p2.getFrame(), 1);
            // Add the drain.
            p.setDrainRate(getPryDrain(p));
            // c.setDrainRate(p.getDrain());
            // Set the headicon.
            setHeadIcon(p, p2);
        }
        // updatePrayerIdClicked(p);
        return true;
    }

    void changeAncients(Player player) {
        player.cE.setAutoCastId(-1);
        player.playAnimation(Animation.create(645));
        player.playGraphics(Graphic.create(436));
        if (player.getSpellBook().isAncient()) {
            player.getSpellBook().changeSpellBook(SpellBook.REGULAR_SPELLBOOK);
            player.getActionSender().sendSidebarInterface(6, 1151);
        } else {
            player.getSpellBook().changeSpellBook(SpellBook.ANCIENT_SPELLBOOK);
            player.getActionSender().sendSidebarInterface(6, 12855);
        }
        player.getActionSender().sendMessage("You have the feeling that your spellbook has just been changed..");
    }

    public static boolean hasUnlocked(final Player player, final int prayer) {
        switch (prayer) {
            case Prayers.PRAYER_RIGOUR:
                return player.getPermExtraData().getBoolean("rigour");
            case Prayers.PRAYER_AUGURY:
                return player.getPermExtraData().getBoolean("augury");
            case Prayers.CURSE_WRATH:
            case Prayers.PRAYER_RETRIBUTION:
                return player.getPermExtraData().getBoolean("wrath");
        }
        return true;
    }

    public void changeLunars(Player player) {
        player.cE.setAutoCastId(-1);
        player.playAnimation(Animation.create(645));
        if (!player.getSpellBook().isLunars()) {
            player.getSpellBook().changeSpellBook(SpellBook.LUNAR_SPELLBOOK);
            player.getActionSender().sendSidebarInterface(6, 29999);
        } else {
            player.getSpellBook().changeSpellBook(SpellBook.REGULAR_SPELLBOOK);
            player.getActionSender().sendSidebarInterface(6, 1151);
        }
        player.getActionSender().sendMessage("You have the feeling that your spellbook has just been changed..");
    }

    public static void changeCurses(Player player) {
        player.playAnimation(Animation.create(645, 0));
        if (!player.getDungeoneering().inDungeon())
            player.getSkills().setLevel(5, player.getSkills().getLevelForExp(5));
        player.getActionSender().sendSkill(5);
        player.getActionSender().sendMessage("You succesfully change your Prayer book.");
        AchievementHandler.progressAchievement(player, "New Prayers");
        boolean current_book = player.getPrayers().isDefaultPrayerbook();
        player.getPrayers().setPrayerbook(!current_book);
        player.resetPrayers();
        if (!player.getPrayers().isDefaultPrayerbook()) {
            player.getActionSender().sendSidebarInterface(5, 22500);
        } else {
            player.getActionSender().sendSidebarInterface(5, 5608);
        }
    }

    public static void setHeadIcon(Player c, PrayerIcon icon) {
        c.headIconId = getIconId(c, icon);
        c.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    public static void setHeadIcon(Player p) {
        setHeadIcon(p, null);
    }

    public static int getIconId(Player p, PrayerIcon icon) {
        // curses
        if (p.getPrayers().isEnabled(48))
            return 17;// soulsplit
        if (p.getPrayers().isEnabled(47))
            return 16;// wrath
        if (p.getPrayers().isEnabled(36) && p.getPrayers().isEnabled(37))
            return 15;// combo
        if (p.getPrayers().isEnabled(36) && p.getPrayers().isEnabled(38))
            return 14;// combo
        if (p.getPrayers().isEnabled(36) && p.getPrayers().isEnabled(39))
            return 13;// combo
        if (p.getPrayers().isEnabled(36))
            return 12;
        if (p.getPrayers().isEnabled(38))
            return 11;
        if (p.getPrayers().isEnabled(37))
            return 10;
        if (p.getPrayers().isEnabled(39))
            return 9;
        if (p.getPrayers().isEnabled(16))// magic
            return 2;
        if (p.getPrayers().isEnabled(17))
            return 1;
        if (p.getPrayers().isEnabled(18))
            return 0;
        if (p.getPrayers().isEnabled(21))
            return 3;
        if (p.getPrayers().isEnabled(22))
            return 5;
        if (p.getPrayers().isEnabled(23))
            return 4;
        return -1;
    }

    /**
     * 0: Thick skin - 1 prayer point every 12 seconds - 0.0416 1: Burst of
     * strength - 1 prayer point every 12 seconds - 0.0416 2: Clarity of Thought
     * - 1 prayer point every 12 seconds - 0.0416 3: Rock Skin - 1 prayer point
     * every 6 seconds - 0.0833 4: Superhuman Strength - 1 prayer point every 6
     * seconds - 0.0833 5: Improved Reflexes - 1 prayer point every 6 seconds -
     * 0.0833 6: Rapid Restore - 1 point every 34 seconds - 0.0147 7: Rapid Heal
     * - 1 point every 18 seconds - 0.0277 8: Protect Item - 1 point every 18
     * seconds - 0.0277 9: Steel Skin - 1 prayer point every 3 seconds - 0.1666
     * 10: Ultimate Strength - 1 prayer point every 3 seconds - 0.1666 11:
     * Incredible Reflexes - 1 prayer point every 3 seconds - 0.1666 12: Protect
     * from Magic - 1 point every 3 seconds - 0.1666 13: Protect from Ranged - 1
     * point every 3 seconds - 0.1666 14: Protect from Melee - 1 point every 3
     * seconds - 0.1666 15: Retribution - 1 point every 14 seconds - 0.0357 16:
     * Redemption - 1 point every 7 seconds - 0.0174 17: Smite - 1 point every 2
     * seconds - 0.2500
     */

    // protect 50% in pvp 100% in pvnpc
    // smite 1/4 damage of other prayer
    // retri hit 25% of the dead player's prayer level
    // redemp: lower then 10% of hp , will heal a number of hitpoints equal to
    // 25% the player's Prayer level
    // rapid restore: 2x restore skill
    // rapid heal: 2x faster hp heal

    /**
     * Loads the XML file of prayer.
     *
     * @throws FileNotFoundException
     */

    @SuppressWarnings("unchecked")
    @Override
    public void init() throws FileNotFoundException {
        prayers = (List<PrayerIcon>) PersistenceManager.load(new FileInputStream("./data/prayer.xml"));
    }

    @Override
    public int[] getValues(int type) {
        if (type == 1) {
            int[] j = new int[BONESEXP.length];
            for (int i = 0; i < BONESEXP.length; i++) {
                j[i] = BONESEXP[i][0];
            }
            return j;
        }
        if (type == 6) {
            int[] j = {61, 409, 410, 411, 412, 2478, 2479, 2480, 2481, 2482, 2483, 2484,
                    2485, 2486, 2487, 2488, 2489, 2490, 2640, 4008, 6552,};
            return j;
        }
        if (type == 14) {
            int[] j = new int[BONESEXP.length];
            for (int i = 0; i < BONESEXP.length; i++) {
                j[i] = BONESEXP[i][0];
            }
            return j;
        }
        if (type != 0)
            return null;

		/*
		 * int ai[] = { 25000, 25002, 25004, 25006, 25008, 25010, 25012, 25014,
		 * 25016, 25018, 25020, 25022, 25024, 25026, 25028, 25030, 25032, 25034,
		 * 25036, 25038, 25040, 25042, 25044, 25046, 25048, 25050 };
		 */
        int ai[] = {5609, 5610, 5611, 18000, 18002, 5612, 5613, 5614, 5615, 5616, 5617,
                18004, 18006, 5618, 5619, 5620, 5621, 5622, 5623, 18008, 18010, 683, 684,
                685, 18012, 18014, 18016, 18018,
                /* curses */22503, 22505, 22507, 22509, 22511, 22513, 22515, 22517,
                22519, 22521, 22523, 22525, 22527, 22529, 22531, 22533, 22535, 22537,
                22539, 22541,};
        return ai;
    }

    private static int usingOnAltar(int objectid) {
        switch (objectid) {
            case 409:
                return 2;
            case 13192:
                return 4;
        }
        return 0;
    }

	/*
	 * drain on your own levels for attacking due to curses
	 */
	/*
	 * public static int calculateDrainPercent(Player player,int prayerId,int
	 * skill){ int maxPercent = 20; int startsAt = 10; if(prayerId == 49){
	 * if(skill == 0 || skill == 1){ startsAt = 15; maxPercent = 30; } else
	 * if(skill == 2){ startsAt = 23; maxPercent = 33; } }else if(prayerId > 40)
	 * maxPercent = 25; startsAt += (player.skillCursesNegative[skill] / 6);
	 * if(startsAt > maxPercent) startsAt = maxPercent; return startsAt; }
	 */

	/*
	 * public static int getGainForSkill(Player player,int skill){ int total =
	 * 0; for(int i = 30; i < 49; i++) if(player.getPrayers().isEnabled(i] &&
	 * isPrayerForSkillIdOnGain(player,skill)) total +=
	 * calculateGainPercent(player,i,skill); return total; }
	 */

	/*
	 * public static int getDrainForSkill(Player player,int skill){//need to
	 * check does other player have prayer on int total = 0; for(int i = 30; i <
	 * 49; i++) if(isPrayerForSkillIdOnDrain(player,skill)) total +=
	 * calculateDrainPercent(player,i,skill); return total; }
	 */


    public int getPrayerForActionButton(int id) {
        int[] j = {5609, 5610, 5611, 18000, 18002, 5612, 5613, 5614, 5615, 5616, 5617,
                18004, 18006, 5618, 5619, 5620, 5621, 5622, 5623, 18008, 18010, 683, 684,
                685, 18012, 18014, 18016, 18018};
        /*
		 * int j[] = { 25000, 25002, 25004, 25006, 25008, 25010, 25012, 25014,
		 * 25016, 25018, 25020, 25022, 25024, 25026, 25028, 25030, 25032, 25034,
		 * 25036, 25038, 25040, 25042, 25044, 25046, 25048, 25050 };
		 */
        if (id >= 22503) {
            int index = 30;// amount of old prayers first curse will have index
            // 20
            index += ((id - 22503) / 2);
            return index;
        }
        for (int i = 0; i < j.length; i++) {
            if (j[i] == id)
                return i;
        }
        return -1;
    }

	/*
	 * public void updatePrayerIdClicked(Player player){ for(int i = 0; i < 8;
	 * i++){ if(isPrayerForSkillIdOnGain(player,i)){ //player.lastCurseCast[i] =
	 * System.currentTimeMillis(); player.skillCursesPositive[i] = 6; return; }
	 * } }
	 */

	/*
	 * public static boolean isPrayerForSkillIdOnGain(Player player,int index){
	 * if(index == 0 && (player.getPrayers().isEnabled(40] || player.getPrayers().isEnabled(49]))//att
	 * return true; if(index == 1 && (player.getPrayers().isEnabled(43] ||
	 * player.getPrayers().isEnabled(49]))//def return true; if(index == 2 &&
	 * (player.getPrayers().isEnabled(44] || player.getPrayers().isEnabled(49]))//str return true;
	 * if(index == 3 && player.getPrayers().isEnabled(41])//range return true; if(index ==
	 * 4 && player.getPrayers().isEnabled(42])//magic return true; if(index == 5 &&
	 * player.getPrayers().isEnabled(45])//energy return true; if(index == 6 &&
	 * player.getPrayers().isEnabled(46])//special return true; return false; }
	 */

    public static boolean isPrayerForSkillIdOnDrain(Player player, int index) {
        if (index == 0
                && (player.getPrayers().isEnabled(31) || player.getPrayers().isEnabled(40) || player.getPrayers().isEnabled(49)))// att
            return true;
        if (index == 1
                && (player.getPrayers().isEnabled(31) || player.getPrayers().isEnabled(43) || player.getPrayers().isEnabled(49)))// def
            return true;
        if (index == 2
                && (player.getPrayers().isEnabled(31) || player.getPrayers().isEnabled(44) || player.getPrayers().isEnabled(49)))// str
            return true;
        if (index == 3 && (player.getPrayers().isEnabled(32) || player.getPrayers().isEnabled(41)))// range
            return true;
        if (index == 4 && (player.getPrayers().isEnabled(33) || player.getPrayers().isEnabled(42)))// magic
            return true;
        if (index == 5 && (player.getPrayers().isEnabled(34) || player.getPrayers().isEnabled(45)))// energy
            return true;
        return index == 6 && (player.getPrayers().isEnabled(34) || player.getPrayers().isEnabled(46));
    }

	/*
	 * public static void updateCurses(Player player) { for(int i = 0; i < 8;
	 * i++){ //if(System.currentTimeMillis()-player.lastCurseCast[i] < 20000){
	 * if(isPrayerForSkillIdOnGain(player,i)){ player.skillCursesPositive[i]++;
	 * if(player.skillCursesPositive[i] % 6 == 0){
	 * //if(System.currentTimeMillis() - player.cE.lastHit < 10000){//only in
	 * combat if(player.cE.getOpponent() != null &&
	 * player.cE.getOpponent().getEntity() instanceof Player){
	 * ContentEntity.startAnimation(player, 12575); //int gfx =
	 * getGfxIdForPrayer(i); //System.out.println("gfx: "+gfx+" i: "+i);
	 * //ContentEntity.playerGfx(player.cE.getOpponent().getPlayerByName(), gfx, 0);
	 * break; } int gfxProjectile = getGfxIdForPrayerProjectile(i);
	 * ContentEntity.playerGfx(player, gfxProjectile); //} } } // } } for(int i
	 * = 0; i < 8; i++){ //if(System.currentTimeMillis()-player.lastCurseCast[i]
	 * < 20000){ for(CombatEntility ce : player.cE.getAttackers()){
	 * if(ce.getEntity() instanceof Player){ Player p = ce.getPlayerByName();
	 * if(isPrayerForSkillIdOnDrain(p,i)){ player.skillCursesNegative[i]++;
	 * //player.lastCurseCast[i] = System.currentTimeMillis(); } } //} } } }
	 */

    // player.sendMessage("Your "+drainNames[i]+" has been leeched by "+player2.name())

	/*
	 * 2226
	 */

}
