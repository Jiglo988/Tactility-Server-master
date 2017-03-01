package org.hyperion.rs2.model.content.minigame.lastmanstanding;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.content.misc2.Jail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.hyperion.rs2.model.content.minigame.lastmanstanding.Constants.*;

public class LastManStanding {

    enum GameState {
        IDLE, STARTED;
    }

    private GameState gameState;
    private List<Player> playersInLobby;
    private List<Player> playersInGame;
    private Set<ChestObject> chests;

    public static final Set<ChestObject> OBJECTS = new HashSet<>();

    public static final List<Item> ITEMS = new ArrayList<>();

    private boolean gassed;
    private int gassTimer;
    private int lobbyElapsed;

    public LastManStanding() {
        gameState = GameState.IDLE;
        playersInLobby = new ArrayList<>();
        playersInGame = new ArrayList<>();
        chests = new HashSet<>();

        gassed = false;
    }

    public void addToLobby(Player player) {
        if (player.duelAttackable > 0) {
            player.getActionSender().sendMessage(
                    "You cannot do that in the duel arena.");
            return;
        }
        if(Jail.inJail(player)) {
            player.sendMessage("You cannot teleport out of jail.");
            return;
        }
        if (player.getCombat().getPlayer().getLastAttack().timeSinceLastAttack() < 9000) {
            player.sendMessage("You cannot do that whilst in combat");
            return;
        }
        if (player.getCombat().getFamiliar() != null) {
            player.sendMessage("You cannot have a familiar whilst in this minigame.");
            return;
        }
        if (player.getInventory().contains() || player.getEquipment().contains()) {
            player.sendMessage("You cannot bring any items.");
        }

        if (!player.getPosition().inPvPArea()) {

            if (!playersInLobby.contains(player)) {
                player.getInterfaceState().resetInterfaces();
                for(int i = 0; i <= 6; i++) {
                    player.getSkills().setLevel(i, 99);
                    player.getSkills().setExperience(i, Math.max(13100000, player.getSkills().getExperience(i)));
                }
                int inventorySlot = 0;
                for(Item item : player.getInventory().toArray()) {
                    if(item != null) {
                        Bank.deposit(player, inventorySlot, item.getId(), item.getCount(), true);
                    }
                    inventorySlot++;
                }
                int equipmentSlot = 0;
                for(Item item : player.getEquipment().toArray()) {
                    if(item != null) {
                        Bank.deposit(player, equipmentSlot, item.getId(), item.getCount(), true);
                    }
                    equipmentSlot++;
                }
                player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                player.setTeleportTarget(Constants.LOBBY);
                player.sendMessage("@whi@You have entered the lobby.");
                playersInLobby.add(player);
                refreshLobby();

                switch (gameState) {
                    case IDLE:
                        if (playersInLobby.size() >= ENOUGH_PLAYERS && gameState != GameState.STARTED) {
                            attemptStart();
                        }
                        break;
                    case STARTED:
                        player.sendMessage("@red@Game has already started, you'll be added the next round.");
                        break;
                    default:
                        break;
                }
            }
        } else {
            player.sendMessage("@red@You cannot join Last Man Standing Minigame right now.");
        }
    }

    public void removePlayerFromLobby(Player player) {
        if (playersInLobby.contains(player)) {
            playersInLobby.remove(player);

            player.getEquipment().clear();
            player.getInventory().clear();
            player.getActionSender().showInterfaceWalkable(-1);
            player.setTeleportTarget(FINISHED_LOCATION);

            refreshLobby();
        }
    }

    public void refreshLobby() {
        lobbyElapsed = 0;
        for (Player p : playersInLobby) {
            if (p != null) {
                if (!p.isActive()) {
                    exit(p);
                } else {
                    int requirements = ENOUGH_PLAYERS - (playersInLobby.size());
                    if (requirements <= 0) {
                        return;
                    }
                    String message = requirements == 1 ? "1 player is needed to start the game." : requirements + " players are needed to start the game.";
                    p.sendMessage(message);
                }
            } else {
                exit(p);
            }
        }
    }

    public void refresh(Player player) {
        if (gameState == GameState.STARTED) {
            player.getActionSender().sendClientConfig(560, 1);
            player.getActionSender().sendString(2805, "There are " + playersInGame.size() + " players remaining.");
            player.getActionSender().sendString(2806, gassed ? "Gassed!" : "");
            player.getActionSender().showInterfaceWalkable(2804);
            if (playersInGame.size() <= 1) {
                Player winner = (Player) playersInGame.get(0);
                winner.sendMessage("@gre@Congratulations, you have won the game!");
                World.submit(new Task(200) {

                    @Override
                    public void execute() {
                        end();
                        winner.getInventory().add(new Item(13663, 1));
                        World.getPlayers().forEach(p -> {
                            p.sendMessage("@whi@" + winner.getName() + " has won the last Man Standing Minigame!");
                        });
                        this.stop();
                    }

                });
            }
        }
    }

    public void attemptStart() {
        if (lobbyElapsed != 0) {
            return;
        }
        World.submit(new Task(1000) {
            @Override
            public void execute() {
                if (playersInLobby.size() < ENOUGH_PLAYERS) {
                    playersInLobby.forEach(p -> {
                        p.sendMessage("@red@The lobby timer has been cancelled.");
                    });
                    refreshLobby();
                    stop();
                    return;
                }

                if (lobbyElapsed >= TIME_TO_START) {
                    playersInLobby.forEach(p -> playersInGame.add(p));
                    playersInLobby.clear();
                    start();
                    lobbyElapsed = 0;
                    stop();
                    return;
                } else if (lobbyElapsed % 5 == 0) { //every 5 seconds
                    playersInLobby.forEach(p -> p.sendMessage(Constants.TIME_TO_START - lobbyElapsed + " @whi@seconds till the game starts."));
                }
                lobbyElapsed++;
            }
        });
    }

    public void start() {
        if (gameState != GameState.STARTED) {

            gameState = GameState.STARTED;


            chests.clear();
            World.submit(new Task(1000) {
                @Override
                public void execute() {
                    if (playersInGame.size() <= 0) {
                        stop();
                        return;
                    }

                    gassTimer++;

                    if (gassed) {
                        playersInGame.forEach(p -> {
                            p.inflictDamage(1, null, true, 1);

                            if (p.getSkills().getLevel(3) <= 0) {
                                exit(p);
                            }

                        });
                    }

                    if (gassTimer > ELAPSED_TO_GAS) {
                        if (!gassed) {
                            setGassed(true);
                        }
                    }

                    playersInGame.forEach(player -> {
                        if(player.getCombat().getFamiliar() != null)
                            player.SummoningCounter = 0;
                    });
                }
            });

            playersInGame.forEach(p -> {
                p.setTeleportTarget(Constants.LOCATION);
                p.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                refresh(p);
                p.sendMessage("The game has started!");
            });
        }
    }

    public void exit(Player player) {
        if (playersInLobby.contains(player)) {

            playersInLobby.remove(player);
            player.setTeleportTarget(FINISHED_LOCATION);
            refreshLobby();
        }

        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
            playersInGame.forEach(this::refresh);
            player.getEquipment().clear();
            player.getInventory().clear();
            player.getActionSender().showInterfaceWalkable(-1);
            player.setTeleportTarget(FINISHED_LOCATION);
        }
    }

    public boolean kill(Player killer, Player target) {
        if (!withinGame(killer)) {
            return false;
        }
        DeathDrops.dropsAtDeath(killer, target);
        exit(target);
        if (playersInGame.size() <= 1) {
            Player winner = (Player) playersInGame.get(0);
            winner.sendMessage("@gre@Congratulations, you have won the game!");
            World.submit(new Task(200) {

                @Override
                public void execute() {
                    end();
                    winner.getInventory().add(new Item(13663, 1));
                    this.stop();
                }

            });
            return true;
        }
        playersInGame.forEach(this::refresh);

        killer.getInventory().add(new Item(385, 2));
        killer.sendMessage("@whi@You have killed " + target.getName());
        return true;
    }

    public void lootChest(Player player, ChestObject obj) {
        if (!playersInGame.contains(player)) {
            return;
        }

        player.sendMessage("Remaining: " + obj.getRemaining());
        if (obj.decrement()) {
            List<Item> items = ITEMS;
            Item random = items.get(ThreadLocalRandom.current().nextInt(items.size()));
            player.getInventory().add(random);
            boolean plural = random.getCount() > 1;
            String amountType = plural ? "some" : "a";
            String name = plural ? random.getDefinition().getName() + "s" : random.getDefinition().getName();
            player.sendMessage("@blu@You find " + amountType + " " + name + ".");
        } else {
            player.sendMessage("@red@There is no longer any stuff in this chest.");
        }
    }

    public void end() {
        for (int i = 0; i < playersInGame.size(); i++ ) {
            Player p = playersInGame.get(i);
            exit(p);
        }
        chests.clear();
        gameState = GameState.IDLE;
        gassed = false;
        gassTimer = 0;
        lobbyElapsed = 0;
    }

    public static LastManStanding lastManStanding = null;

    public static LastManStanding getLastManStanding() {
        if (lastManStanding == null)
            lastManStanding = new LastManStanding();
        return lastManStanding;
    }

    public void handleObject(Player player, int id, int x, int y, int z) {
        Position loc = Position.create(x, y, z);
        chests.stream().filter(o -> o.getDefinition().getId() == id && o.equals(loc)).findAny().ifPresent(obj -> lootChest(player, obj));
    }

    public boolean withinGame(String name) {
        return playersInGame.stream().filter(player -> player.getName().equalsIgnoreCase(name)).findAny().isPresent();
    }

    public boolean withinGame(Player player) {
        return playersInGame.contains(player);
    }

    public boolean withinLobby(Player player) {
        return playersInLobby.contains(player);
    }

    public boolean isStarted() {
        return gameState == GameState.STARTED;
    }

    public void setGassed(boolean gassed) {
        this.gassed = gassed;
        playersInGame.forEach(p -> {
            refresh(p);
            p.sendMessage("@red@The gas has triggered!");
        });
    }

    public boolean isGassed() {
        return gassed;
    }

    public List<Player> getLobby() {
        return playersInLobby;
    }

    @Override
    public String toString() {
        return "[started=" + gameState + ", gassed=" + gassed + ", lobby=" + playersInLobby.size() + ", players=" + playersInGame.size() + ", elapsed=" + gassTimer + ", lobbyElapsed=" + lobbyElapsed + "]";
    }
}