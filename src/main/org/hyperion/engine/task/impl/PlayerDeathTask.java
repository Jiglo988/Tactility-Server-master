package org.hyperion.engine.task.impl;

import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.achievements.AchievementHandler;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.EloRating;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.bounty.BountyPerkHandler;
import org.hyperion.rs2.model.content.bounty.place.BountyHandler;
import org.hyperion.rs2.model.content.misc2.Jail;
import org.hyperion.rs2.model.content.pvptasks.TaskHandler;
import org.hyperion.rs2.model.content.specialareas.impl.PurePk;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Gilles
 */
public class PlayerDeathTask extends Task {

    public static final Map<String, Queue<String>> kills = new HashMap<>();
    private static final Position DEATH_POSITION = Position.create(3096, 3471, 0);

    private final Player player;

    private int timer = 0;

    public PlayerDeathTask(Player player) {
        super(600, player);
        this.player = player;
        player.setDead(true);
    }

    @Override
    public void execute() {
        try {
            if (!player.isActive() || player.isHidden() || !player.isDead()) {
                this.stop();
                return;
            }
            if (!World.getPlayers().contains(player))
                player.destroy();
            if (Jail.inJail(player)) {
                player.getSkills().setLevel(Skills.HITPOINTS, player.getSkills().getLevelForExp(Skills.HITPOINTS));
                player.setDead(false);
                this.stop();
                return;
            }
            switch (timer) {
                case 2:
                    startDeath();
                    break;
                case 9:
                    resetPlayer();
                    GameEngine.submitIO(new EngineTask<Boolean>("saving player", false) {
                        @Override
                        public Boolean call() throws Exception {
                            PlayerSaving.setSaving(player);
                            PlayerSaving.save(player);
                            return true;
                        }
                    });
                    break;
                case 11:
                    player.playAnimation(Animation.create(-1, 0));
                    player.setDead(false);
                    this.stop();
                    break;
            }
            timer++;
        } catch (Exception e) {
            e.printStackTrace();
            this.stop();
        }
    }

    private void startDeath() {
        player.playAnimation(Animation.create(0x900, 0));
        Combat.logoutReset(player.cE);
        player.cE.setPoisoned(false);
        player.getWalkingQueue().reset();
        player.getActionSender().resetFollow();
        player.cE.morrigansLeft = 0;
    }

    private void resetPlayer() {
        player.playAnimation(Animation.create(-1, 0));
        player.getCombat().setOpponent(null);
        TaskManager.submit(new Task(200, "reset skills after death") {
            @Override
            protected void execute() {
                stop();
                for (int i = 0; i < Skills.SKILL_COUNT - 3; i++) {
                    player.getSkills().setLevel(i, player.getSkills().getLevelForExp(i));
                }
            }
        });
        if (System.currentTimeMillis() - player.getExtraData().getLong("lastdeath") > 120000) {
            player.getSpecBar().setAmount(SpecialBar.FULL);
            player.getExtraData().put("lastdeath", System.currentTimeMillis());
        } else {
            player.sendMessage("You don't restore special energy as you have died too recently.");
        }
        player.specOn = false;
        player.teleBlockTimer = System.currentTimeMillis();
        player.getActionSender().resetFollow();
        if (player.getRunePouch().size() > 0)
            player.getRunePouch().clear();
        player.getSpecBar().sendSpecAmount();
        player.getSpecBar().sendSpecBar();
        player.cE.setPoisoned(false);
        player.cE.setFreezeTimer(0);
        Player killer = player.cE.getKiller();
        if (!player.getLocation().onDeath(player)) {
            if (killer != null) {
                //blood lust system
                ContentManager.handlePacket(6, player, 38000, killer.getClientIndex(), -1, -1);
                BountyHandler.handle(killer, player.getName());
                /**
                 * Increasing stupid points and stuff.
                 */
                killer.sendMessage(sendKillMessage(player.getSafeDisplayName()));
                BountyPerkHandler.handleSpecialPerk(killer);
                boolean isDev = false;
                if (Rank.getPrimaryRank(killer).ordinal() >= Rank.DEVELOPER.ordinal()
                        || Rank.getPrimaryRank(player).ordinal() >= Rank.DEVELOPER.ordinal())
                    isDev = true;
                if (!isDev) {
                    killer.increaseKillCount();
                    int oldKillerRating = killer.getPoints().getEloRating();
                    if (killer.getPosition().getZ() != PurePk.HEIGHT) {
                        killer.getPoints().updateEloRating(player.getPoints().getEloRating(), EloRating.WIN);
                        player.getPoints().updateEloRating(oldKillerRating, EloRating.LOSE);
                    }
                }
                try {
                    if (killer.getPvPTask() != null)
                        TaskHandler.checkTask(killer, player);
                } catch (Exception e) {
                    System.err.println("PvP tasks error!");
                    e.printStackTrace();
                }
                killer.getBountyHunter().handleBHKill(player);
                //killer.getAchievementTracker().playerKill();
                if (isRecentKill(killer, player)) {
                    killer.sendPkMessage("You have recently killed this player and do not receive PK points.");
                    if (killer.getGameMode() <= player.getGameMode())
                        handlePkpTransfer(killer, player, 0);
                } else {
                    if (player.getKillCount() >= 10) {
                        killer.increaseKillStreak();
                    }
                    killer.getAchievementTracker().playerKill();
                    killer.addLastKill(player.getName());
                    int pkpIncrease = (int) Math.pow(player.getKillCount(), .8);
                    if (pkpIncrease > 400)
                        pkpIncrease = 400;

                    int pointsToAdd = ((player.wildernessLevel / 2 + player.getBounty()) + pkpIncrease);

                    if (player.getKillStreak() >= 6) {
                        AchievementHandler.progressAchievement(player, "Killstreak");
                        World.getPlayers().stream().filter(p -> p != null).forEach(p -> p.sendPkMessage(killer.getSafeDisplayName() + " has just ended " + player.getSafeDisplayName() + "'s rampage of " + player.getKillStreak() + " kills."));
                    }
                    handlePkpTransfer(killer, player, pointsToAdd > 0 ? pointsToAdd : 5);
                    if (Rank.hasAbility(killer, Rank.SUPER_DONATOR))
                        killer.getSpecBar().increment(SpecialBar.FULL / 5);
                    if (Rank.hasAbility(killer, Rank.DONATOR))
                        killer.getSpecBar().increment(SpecialBar.CYCLE_INCREMENT);
                    killer.getSpecBar().sendSpecBar();
                    killer.getSpecBar().sendSpecAmount();

                }
                if (!isDev) {
                    player.increaseDeathCount();
                    player.resetKillStreak();
                    player.resetBounty();
                }
                //DeathDrops.dropAllItems(player, killer);
                DeathDrops.dropsAtDeath(player, killer);
            } else {
                DeathDrops.dropsAtDeath(player, player);
            }
            player.setTeleportTarget(DEATH_POSITION, false);
            player.getActionSender().sendMessage(getDeathMessage());
        } else {
            player.setDead(false);
        }
        player.setSkulled(false);
        player.resetPrayers();
    }

    public static boolean isRecentKill(final Player killer, final Player player) {
        if (killer.equals(player))
            return true;
        if (kills.containsKey(killer.getShortIP())) {
            final Queue<String> recent = kills.get(killer.getShortIP());
            if (recent.size() == 3)
                recent.poll();
            if (recent.contains(player.getShortIP()))
                return true;
            recent.offer(player.getShortIP());
        } else {
            final Queue<String> queue = new ArrayBlockingQueue<>(3);
            queue.add(player.getShortIP());
            kills.put(killer.getShortIP(), queue);
        }
        return false;
    }

    public static int pkpToTransfer(final Player victim) {
        int base = 8;
        base += (int) (victim.wildernessLevel / 1.5D);

        base += (int) (Math.pow(victim.getKillCount(), 0.765) - Math.pow(victim.getDeathCount(), 0.67));

        if (base > 150)
            base = 150;
        if (base < 8)
            base = 8;
        return base;
    }

    private static void handlePkpTransfer(final Player killer, final Player player, int original) {
        int toTransfer = pkpToTransfer(player);
        final PlayerPoints kP = player.getPoints();
        if (kP.getPkPoints() < toTransfer) {
            int deltaRemove = (toTransfer - kP.getPkPoints() + 10) / 9;
            int remove = player.getBank().remove(Item.create(5020, deltaRemove));
            kP.increasePkPoints((remove * 10), false);
        }

        if (kP.getPkPoints() < toTransfer)
            toTransfer = kP.getPkPoints();
        if (!player.isNewlyCreated()) {
            kP.setPkPoints(kP.getPkPoints() - toTransfer);
            player.sendPkMessage("You lose " + toTransfer + " Pk points for this death");
        }
        toTransfer *= 0.9D;
        toTransfer = killer.getPoints().pkpBonus(toTransfer);
        killer.getPoints().increasePkPoints(toTransfer + original, false);

        killer.sendPkMessage("You have received " + (toTransfer + original) + " PK points for this kill");


    }

    private static final String[] KILLER_MESSAGES = new String[]{
            "You have wiped the floor with %s.",
            "%s regrets the day that he met you.",
            "You rock, clearly %s does not.",
            "You have sent %s to his grave.",
            "All the kings horses and men could never put %s back together again...",
            "%s falls before your might.",
            "With a crushing blow %s's life is met with an untimely end.",
            "You have ended %s's life abruptly.",
            "The mysteries of life can no longer be discovered by %s.",
            "The sword is obviously mightier than %s.",
            "The death of %s is a burden you must bear.",
            "%s must of dissapointed the gods...",
            "I think %s said square root, not square up - either way, he died."
    };

    private static final String[] DEATH_MESSAGES = new String[]{
            "Oh dear! You are dead",
            "Death is a harsh mistress",
            "Life is fragile, you had to learn it the hard way",
            "Life is part of a cycle, the cycle just ended.",
            "The darkness of the afterlife awaits you...",
            "You're stupid... and dead"
    };

    private static String sendKillMessage(String name) {
        name = TextUtils.titleCase(name);
        return getMiscMessage(KILLER_MESSAGES, name);
    }

    private static String getDeathMessage() {
        return getMiscMessage(DEATH_MESSAGES, null);
    }

    private static String getMiscMessage(final String[] array, final String name) {
        final int rand = Misc.random(array.length - 1);
        return array[rand].replaceAll("%s", name);
    }
}