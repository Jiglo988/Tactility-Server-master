package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.BoB;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.minigame.Bork;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc2.Food;
import org.hyperion.rs2.model.content.skill.Summoning;
import org.hyperion.rs2.model.content.skill.dungoneering.DungeoneeringManager;
import org.hyperion.rs2.model.shops.PvMStore;
import org.hyperion.util.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The death event handles player and npc deaths. Drops loot, does animation, teleportation, etc.
 *
 * @author Graham
 */

public class NpcDeathTask extends Task {

    public static Map<String, Long> borkKillers = new HashMap<>();


    public static int npcIdForDoubleDrops;


    public static final int CYCLES_AMOUNT = 9;

    private final NPC npc;

    /**
     * Creates te death event for the specified entity.
     */
    public NpcDeathTask(NPC npc) {
        super(600);
        this.npc = npc;
    }

    private int timer = CYCLES_AMOUNT;

    @Override
    public void execute() {
        try {
            if (!npc.isDead()) {
                this.stop();
                return;
            }
            executeNpcDeath();
        } catch (Exception e) {
            e.printStackTrace();
            this.stop();
        }
    }


    private void executeNpcDeath() {
        if (timer == 7) {
            npc.playAnimation(Animation.create(npc.getDefinition().deathEmote(), 0));
            if(npc.cE != null) {
                Combat.logoutReset(npc.cE);
                npc.cE.setPoisoned(false);
            }
            npc.getWalkingQueue().reset();
        } else if (timer == 0) {
            int tokens = 0;
            int x = npc.getPosition().getX(), y = npc.getPosition().getY(), z = npc.getPosition().getZ();
            final Map<Player, Double> killers = new HashMap<>();
            for (final Map.Entry<String, Integer> killer : npc.getCombat().getDamageDealt().entrySet()) {
                if (killer == null) continue;
                final Optional<NPCKillReward> reward = getReward(npc.getDefinition().getId());
                if (!reward.isPresent()) break;
                final Player player = World.getPlayerByName(killer.getKey().toLowerCase().trim());
                if (player == null) continue;
                double percent = killer.getValue() / ((double) npc.maxHealth);
                if (percent > 0.1 || (npcIdForDoubleDrops == npc.getDefinition().getId() && percent > 0.05)) {
                    killers.put(player, percent);
                    final int dp = (int) (reward.get().dp * percent);
                    final int pkp = (int) (reward.get().pkp * percent);
                    tokens = (int) (reward.get().tokens * percent);
                    player.getPoints().increasePkPoints(pkp);//1750 hp, 175pkp
                    player.getPoints().increaseDonatorPoints(dp, false);//12 donators pts to divvy up?
                    double increment = Rank.hasAbility(player, Rank.SUPER_DONATOR) ? 0.035 : 0.04;
                    for (double d = 0.03; d < percent; d += increment) {
                        if (unreacheablenpc(npc.getDefinition().getId())) {
                            x = player.getPosition().getX();
                            y = player.getPosition().getY();
                            z = player.getPosition().getZ();
                        }
                        GlobalItem globalItem5 = new GlobalItem(
                                player, x, y, z,
                                new Item(391, 1));
                        GlobalItemManager.newDropItem(player, globalItem5);
                    }

                }

            }

            Player killer = npc.cE.getKiller();
            if (killer != null) {
                if (!npc.serverKilled) {
                    ContentManager.handlePacket(16, killer, npc.getDefinition().getId(), npc.getPosition().getX(), npc.getPosition().getY(), npc.getIndex());
                    for (final Map.Entry<Player, Double> kill : killers.entrySet()) {
                        if (kill.getValue() > 0.20 && killer != kill.getKey())
                            ContentManager.handlePacket(16, kill.getKey(), npc.getDefinition().getId(), npc.getPosition().getX(), npc.getPosition().getY(), npc.getIndex());

                    }
                    if (Bork.handleBorkDeath(killer, npc))
                        return;
                }

            }
            npc.setTeleportTarget(npc.getSpawnPosition(), false);
            if (npc.npcDeathTimer != -1) {
                if (npc.getDefinition().getId() == 5666)
                    timer = 10 + 190;
                else timer = 10 + 60;
                // killer.debugMessage("Time to wait: " + timer);
                npc.isHidden(true);
            } else {
                npc.isHidden(true);
            }
            try {
                if (Summoning.isBoB(npc.getDefinition().getId()) &&
                        ((Player) World.getPlayers().get(npc.ownerId)).cE.summonedNpc == npc)
                    BoB.dropBoB(npc.getPosition(), (Player) World.getPlayers().get(npc.ownerId));
            } catch (Exception e) {
            }//it throws a aload of index out of bounds exceptions if a player logs out, handle it if u want i was just a lil lazy at the tiem i c, btw remem , getlevelforexp , if we do binary search with that , its gonna be boostspeed prohax!!!!!! :P guess what, Idk concept of binary search ive heard of it but never  looked at code or implementation ok ill explain in 30 secs
            if (killer != null) {
                Player player = killer;
                if (player.slayerTask == npc.getDefinition().getId()) {
                    player.getSkills().addExperience(Skills.SLAYER, npc.maxHealth * 125);
                    if (--player.slayerAm <= 0) {
                        player.slayerTask = 0;
                        player.getSkills().addExperience(Skills.SLAYER, 10 * npc.maxHealth * 25);
                        DialogueManager.openDialogue(player, 33);
                    }
                }
                //bones
                if (unreacheablenpc(npc.getDefinition().getId())) {
                    x = killer.getPosition().getX();
                    y = killer.getPosition().getY();
                    z = killer.getPosition().getZ();
                }
                if (npc.bones > 0) {
                    GlobalItem globalItem5 = new GlobalItem(
                            player, x, y, z,
                            new Item(npc.bones, 1));
                    GlobalItemManager.newDropItem(player, globalItem5);
                }
                if (!player.getDungeoneering().inDungeon()) {
                    //charms
                    if (npc.charm > 0) {
                        GlobalItem globalItem5 = new GlobalItem(
                                player, x, y, z,
                                new Item(npc.charm, 1));
                        if (player.getInventory().contains(16639))
                            ContentEntity.addItem(player, npc.charm, 1);
                        else
                            GlobalItemManager.newDropItem(player, globalItem5);
                    }
                    //talismines
                    int tali = NPCManager.getTalismine(npc.getDefinition());
                    if (tali > 0) {
                        GlobalItem globalItem5 = new GlobalItem(
                                player, x, y, z,
                                new Item(tali, 1)
                        );
                        GlobalItemManager.newDropItem(player, globalItem5);
                    }

                    if (tokens <= 0)
                        tokens = Misc.random(11) == 0 ? Misc.random(npc.getDefinition().combat() / 10 + 1) : 0;
                    if (tokens > 0 && npc.getDefinition().getId() != 5399) {

                        {
                            GlobalItem globalItem5 = new GlobalItem(
                                    player, x, y, z,
                                    new Item(PvMStore.TOKEN, tokens)
                            );
                            if (player.getInventory().freeSlots() < 1 || !player.getInventory().contains(16638))
                                GlobalItemManager.newDropItem(player, globalItem5);
                            else
                                player.getInventory().add(globalItem5.getItem());
                        }

                    }
                }
                final int kills = player.getNPCLogs().log(npc);
                if (npc.getDefinition().getId() == 5666)
                    borkKillers.put(player.getName(), System.currentTimeMillis());
                player.sendf("You now have @dre@%d@bla@ %s %s.", kills, npc.getDefinition().getName().toLowerCase().replace("_", " "), kills == 1 ? "kill" : "kills");
                player.getAchievementTracker().npcKill(npc.getDefinition().getId());

                if (kills % 1000 == 0) {
                    final Item add = Item.create(PvMStore.TOKEN, npc.getDefinition().combat());
                    player.sendf("For this milestone, you receive @dre@%d@bla@ PvM Tokens", add.getCount());
                    if (!player.getInventory().add(add))
                        player.getBank().add(add);
                }
                //normal drops
                if (!player.getDungeoneering().inDungeon()) {
                    final boolean isTask = player.getSlayer().isTask(npc.getDefinition().getId());
                    if (npc.getDefinition().getDrops() != null && npc.getDefinition().getDrops().size() >= 1) {
                        int chance = isTask ? 750 : 1000;
                        if (npc.getDefinition().getId() == 8349 && player.getPosition().inPvPArea())
                            chance = 750;
                        if (npcIdForDoubleDrops == npc.getDefinition().getId())
                            chance = 500;
                        chance *= 0.9;
                        for (NPCDrop drop : npc.getDefinition().getDrops()) {
                            if (drop == null) continue;
                            if (Combat.random(chance) <= drop.getChance()) {
                                int amt = drop.getMin() + Combat.random(drop.getMax() - drop.getMin());
                                if (amt < 0)
                                    amt = 1;
                                if (player.getInventory().contains(16639) && drop.getId() >= 12158 && drop.getId() <= 12163)
                                    ContentEntity.addItem(player, drop.getId() == 12162 ? 12163 : drop.getId(), amt);
                                else {
                                    GlobalItem globalItem = new GlobalItem(player, x, y, z,
                                            Item.create(drop.getId(), amt));
                                    if (drop.getChance() < 30) {
                                        for (Player p : player.getRegion().getPlayers())
                                            p.sendLootMessage("Loot", player.getSafeDisplayName() + " has just gotten " + (amt == 1 ? Misc.aOrAn(ItemDefinition.forId(drop.getId()).getName()) : amt) + " " + ItemDefinition.forId(drop.getId()).getName() + (amt > 1 ? "s" : "") + ".");
                                    }
                                    GlobalItemManager.newDropItem(player, globalItem);
                                }
                            }
                        }

                    }
/*
                    if(ClueScrollUtils.dropClueScroll(player, npc)) {
                        Item clueScroll = ClueScrollUtils.getScroll(npc);
                        GlobalItem globalItem = new GlobalItem(player, npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ(), clueScroll);
                        GlobalItemManager.newDropItem(player, globalItem);
                        for (Player p : player.getRegion().getPlayers())
                            p.sendLootMessage("Loot", player.getSafeDisplayName() + " has just gotten " + Misc.aOrAn(clueScroll.getDefinition().getName()) + " " + clueScroll.getDefinition().getName() + ".");
                    }
*/

                    if (isTask && Misc.random(1000) < 1) {
                        GlobalItem globalItem = new GlobalItem(player, npc.getPosition().getX(),
                                npc.getPosition().getY(), npc.getPosition().getZ(),
                                Item.create(18768, 1));
                        for (Player p : player.getRegion().getPlayers())
                            p.sendLootMessage("Loot", player.getSafeDisplayName() + " has just gotten a " + ItemDefinition.forId(18768).getName() + ".");

                        GlobalItemManager.newDropItem(player, globalItem);

                    }
                } else {
                    if (player.getDungeoneering().getRoom().boss) {
                        for (int i = 0; i < player.getDungeoneering().getCurrentDungeon().difficulty.ordinal() + 1; i++) {
                            final ItemDefinition def = ItemDefinition.forId(FightPits.scItems.get(Misc.random(FightPits.scItems.size() - 1)));
                            GlobalItem globalItem = new GlobalItem(player, npc.getPosition().getX(),
                                    npc.getPosition().getY(), npc.getPosition().getZ(),
                                    Item.create(def.getId(), def.isStackable() ? (1 + Misc.random(49)) : 1));
                            GlobalItemManager.newDropItem(player, globalItem);
                        }

                    } else {

                        for (int i = 0; i < (npc.getDefinition().combat() / 50 + 1); i++) {
                            final ItemDefinition def = ItemDefinition.forId(DungeoneeringManager.randomItem());
                            GlobalItem globalItem = new GlobalItem(player, npc.getPosition().getX(),
                                    npc.getPosition().getY(), npc.getPosition().getZ(),
                                    Item.create(def.getId(), def.isStackable() ? (1 + Misc.random(100)) : 1));
                            GlobalItemManager.newDropItem(player, globalItem);
                            globalItem.createdTime = System.currentTimeMillis() + 47000L;
                            globalItem = new GlobalItem(player, npc.getPosition().getX(),
                                    npc.getPosition().getY(), npc.getPosition().getZ(),
                                    Item.create(Food.randomFood(), 1));
                            globalItem.createdTime = System.currentTimeMillis() + 47000L;
                            GlobalItemManager.newDropItem(player, globalItem);
                        }
                        GlobalItem globalItem = new GlobalItem(player, npc.getPosition().getX(),
                                npc.getPosition().getY(), npc.getPosition().getZ(),
                                Item.create(995, npc.getDefinition().combat() * 15 + 1));
                        GlobalItemManager.newDropItem(player, globalItem);
                        globalItem.createdTime = System.currentTimeMillis() + 30000L;
                    }


                }
            }
        } else if (timer == -1) {
            if (WildernessBossTask.isWildernessBoss(npc.getDefinition().getId())) {
                World.getPlayers().forEach(p -> p.sendServerMessage(WildernessBossTask.currentBoss.getDefinition().getName() + " has been defeated!"));
                World.submit(new WildernessBossTask(false));
                WildernessBossTask.currentBoss = null;
            }
            World.unregister(npc);
            this.stop();
        } else if (timer == 10) {
            npc.isHidden(false);
            npc.playAnimation(Animation.create(-1, 0));
            npc.setDead(false);
            npc.cE.setFreezeTimer(0);
            npc.health = npc.maxHealth;
            this.stop();
        }
        timer--;
    }


    private static boolean unreacheablenpc(final int id) {
        return id == 8596;
    }

    public static Optional<NPCKillReward> getReward(final int id) {
        if (id == npcIdForDoubleDrops)
            return Optional.of(new NPCKillReward(150, 1000, 40));
        switch (id) {

            case 8133:
                return Optional.of(new NPCKillReward(10, 200, 10));
            case 8596:
                return Optional.of(new NPCKillReward(40, 300, 8));
            case 50:
                return Optional.of(new NPCKillReward(25, 200, 7));
        }
        if (WildernessBossTask.isWildernessBoss(id))
            return Optional.of(new NPCKillReward(200, 700, 30));
        return Optional.empty();
    }

    private static final class NPCKillReward {

        private final int dp;
        private final int pkp;
        private final int tokens;

        public NPCKillReward(final int dp, final int pkp, final int tokens) {
            this.dp = dp;
            this.pkp = pkp;
            this.tokens = tokens;
        }


    }


}
