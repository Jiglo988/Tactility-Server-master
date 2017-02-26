package org.hyperion.rs2.model.content.skill.dungoneering;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.achievements.AchievementHandler;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.model.itf.impl.DungoneeringParty;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.DungeoneeringFloorsTask;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 2/20/15
 * Time: 8:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class Dungeon {
    public static final List<Dungeon> activeDungeons = new CopyOnWriteArrayList<>();
    public final int heightLevel;
    public final DungeonDifficulty difficulty;
    public final int teamSize;
    private final Map<Player, Integer> deaths = new HashMap<>();
    private final List<Player> players;
    private final long start_time;
    private final DungeonDifficulty.DungeonSize size;
    private List<Room> rooms = new CopyOnWriteArrayList<>();

    public Dungeon(final List<Player> players, final DungeonDifficulty difficulty, final DungeonDifficulty.DungeonSize size) {

        this.players = players;
        this.heightLevel = players.get(0).getIndex();
        this.difficulty = difficulty;
        this.start_time = System.currentTimeMillis();
        this.size = size;
        this.teamSize = players.size();
        activeDungeons.add(this);
    }

    private static final String toPercent(final double d) {
        return String.format("%.0f%%", d * 100D);
    }

    public void start() {
        addRooms();
        assignChildren();
        final Room start = rooms.get(0);
        for (final Player player : players) {
            player.setTeleportTarget(start.getSpawnLocation());
            player.getDungeoneering().setCurrentRoom(start);
            player.getInventory().add(Item.create(995, (int) (difficulty.coins * size.multiplier)));
            for (final Item bound : player.getDungeoneering().getBinds()) {
                if (bound == null || (!FightPits.scItems.contains(bound.getId()) && !ItemSpawning.canSpawn(bound.getId()))) {
                    continue;
                }
                player.getInventory().add(bound);
            }
        }
        start.initialized = true;
        final Point loc = start.definition.randomLoc();
        final NPC trader = NPCManager.addNPC(Position.create(loc.x, loc.y, start.heightLevel), DungeoneeringManager.TRADER_ID, -1);
        start.events.add(trader);

    }

    public final void remove(final Player player, boolean complete) {
        Trade.declineTrade(player);
        player.getDungeoneering().loadXP(player.getSkills(), false);
        players.remove(player);
        if (complete) {
            player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().add(player);
            long elapsed_time = System.currentTimeMillis() - start_time;
            long delta_time = (long) (difficulty.time * size.multi_time) - elapsed_time;
            long time = TimeUnit.MINUTES.convert(delta_time, TimeUnit.MILLISECONDS);
            double multiplier = (time / 10D) + 1.0;
            if (multiplier > 1.5)
                multiplier = 1.5;
            if (multiplier < 0.5) multiplier = 0.5;
            int death = deaths.getOrDefault(player, 0);
            double death_penalty = Math.pow(0.85, death);
            if (death_penalty < 0.4)
                death_penalty = 0.4;
            double team_penalty = Math.pow(1.04, teamSize - 1);
            final double size_multi = size.multiplier;
            final int xp = (int) ((difficulty.xp * multiplier) * death_penalty * size_multi * team_penalty);
            int tokens = xp / 30;
            player.getSkills().addExperience(Skills.DUNGEONEERING, xp);
            player.getDungeoneering().setTokens(player.getDungeoneering().getTokens() + tokens);

            player.getAchievementTracker().dungFloorCompleted(DungeoneeringFloorsTask.Difficulty.valueOf(difficulty.name()),
                    DungeoneeringFloorsTask.Size.valueOf(size.name()));
            final String s = String.format("Size Bonus: %s Team Bonus: %s Death Penalty: %s Time Multi: %s", toPercent(size_multi), toPercent(death_penalty), toPercent(team_penalty), toPercent(multiplier));
            player.sendf("Alert##Experience: %,d | %,d##%s##Time: %,d seconds", difficulty.xp, xp, s, TimeUnit.SECONDS.convert(elapsed_time, TimeUnit.MILLISECONDS));
            player.sendMessage("@red@----------------------DUNGEON COMPLETE----------------------", "@blu@BaseXP: @bla@" + difficulty.xp, s, "@blu@Final Exp: @bla@ " + xp, "@blu@Time: @bla@" + TimeUnit.SECONDS.convert(elapsed_time, TimeUnit.MILLISECONDS) + " seconds");
        } else {
            if (!player.getDungeoneeringLeader().equals(player)) {
                player.getDungeoneeringLeader().write(DungoneeringParty.getInterface().createDataBuilder().put((byte) 1).putRS2String(player.getName()).toPacket());
                players.stream().filter(target -> target != null).forEach(target -> target.sendf("Player @red@%s@bla@ has left the party.", player.getName()));
            } else {
                if (players.size() >= 1) {
                    final Player leader = players.get(0);
                    players.stream().filter(target -> target != null).forEach(target -> {
                        target.setDungeoneeringLeader(leader);
                        target.sendf("Dungeon leader @red@%s@bla@ has left. @red@%s the new leader.", player.getName(), leader != target ? String.format("%s @bla@is", leader.getName()) : "You @bla@are");
                    });
                }
            }
        }
        final List<Item> inventory = Arrays.asList(player.getInventory().toArray());
        if (!inventory.isEmpty()) {
            inventory.stream().filter(value -> value != null && value.getId() != 15707).forEach(player.getInventory()::remove);
        }
        final List<Item> equipment = Arrays.asList(player.getEquipment().toArray());
        if (!equipment.isEmpty()) {
            equipment.stream().filter(value -> value != null && value.getId() != 15707).forEach(player.getEquipment()::remove);
        }
        player.getDungeoneering().setCurrentRoom(null);
        player.getDungeoneering().setCurrentDungeon(null);
        player.getExtraData().put("DungeonInvitation", null);
        ClanManager.leaveChat(player, true, false);
        player.setTeleportTarget(Position.create(2987, 9637, 0));
        if (players.size() == 0)
            destroy();

    }

    public void addRooms() {
        int loopAround = 1;
        int size = this.size.size;
        rooms.add(RoomDefinition.getStartRoom().getRoom(this, loopAround));
        size--;
        while (size > 0) {
            final List<RoomDefinition> list = new ArrayList<>();
            list.addAll(RoomDefinition.ROOM_DEFINITIONS_LIST);
            Collections.shuffle(list);
            for (final RoomDefinition def : list) {
                rooms.add(def.getRoom(this, loopAround));
                if (--size == 0)
                    break;
            }
            loopAround++;
        }
    }

    public void complete() {
        synchronized (this) {
            players.forEach(player -> {
                AchievementHandler.progressAchievement(player, "Dungeon");
                remove(player, true);
            });
        }
    }

    public void assignChildren() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            rooms.get(i).setChild(rooms.get(i + 1));
        }
        final Room boss = rooms.get(rooms.size() - 1);
        boss.boss = true;
        boss.heightLevel = (int) (heightLevel * Math.pow(4, 3));

        for (int i = 1; i < rooms.size(); i++) {
            rooms.get(i).setParent(rooms.get(i - 1));
        }

    }

    public void destroy() {
        synchronized (this) {
            for (final Room room : rooms)
                room.destroy();
            rooms.clear();
            players.clear();
            activeDungeons.remove(this);
        }

    }

    public Room getStartRoom() {
        return rooms.get(0);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void kill(final Player player) {
        int old = deaths.getOrDefault(player, 0);
        deaths.put(player, old + 1);
        player.getDungeoneering().setCurrentRoom(getStartRoom());
    }

}
