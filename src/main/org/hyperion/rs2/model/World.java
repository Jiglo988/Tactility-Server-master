package org.hyperion.rs2.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.mina.core.session.IoSession;
import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.engine.task.impl.*;
import org.hyperion.map.BlockPoint;
import org.hyperion.map.DirectionCollection;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.WorldLoader;
import org.hyperion.rs2.model.content.bounty.BountyHunterLogout;
import org.hyperion.rs2.model.content.bounty.BountyHunterTask;
import org.hyperion.rs2.model.content.jge.event.PulseGrandExchangeTask;
import org.hyperion.rs2.model.content.publicevent.ServerEventTask;
import org.hyperion.rs2.model.punishment.event.PunishmentExpirationTask;
import org.hyperion.rs2.net.PacketManager;
import org.hyperion.rs2.packet.PacketHandler;
import org.hyperion.rs2.util.ConfigurationParser;
import org.hyperion.rs2.util.EntityList;

import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.logging.Level;

/**
 * Holds data global to the game world.
 *
 * @author Gilles
 */
public final class World {

    /**
     * TEMP
     */
    private final static Set<String> unlockedPlayers = new HashSet<>();

    private final static Set<String> unlockedRichPlayers = new HashSet<>();

    public static int worldmapobjects = 10331; // 10331, 5116
    @SuppressWarnings("unchecked")
    public static Map<BlockPoint, DirectionCollection>[] World_Objects = new Hashtable[worldmapobjects];

    static {
        for (int i = 0; i < worldmapobjects; i++) {
            World_Objects[i] = null;
            World_Objects[i] = new Hashtable<>();
        }
    }

    public static void submit(Task task) {
        TaskManager.submit(task);
    }

    /**
     * END OF THE TEMP LEFTOVER CODE
     */

    /** Private constructor to prevent instancing **/
    private World() {}

    /** The highest playercount that happened while the server was online in this session **/
    private static int maxPlayerCount = 0;

    /** The current WorldLoader that is being used to load the Players **/
    private static WorldLoader loader;

    /** Used to block the game thread until updating has completed. */
    private static Phaser synchronizer = new Phaser(1);

    /** A thread pool that will update players in parallel. */
    private static ExecutorService updateExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

    /** The queue of {@link Player}s waiting to be logged in. **/
    private final static Queue<Player> logins = new ConcurrentLinkedQueue<>();

    /**The queue of {@link Player}s waiting to be logged out. **/
    private final static Queue<Player> logouts = new ConcurrentLinkedQueue<>();

    /** All of the registered players. */
    private final static EntityList<Player> players = new EntityList<>(Constants.MAX_PLAYERS);

    /** All of the registered NPCs. */
    public final static EntityList<NPC> npcs = new EntityList<>(Constants.MAX_NPCS);

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static Queue<Player> getLoginQueue() {
        return logins;
    }

    public static Queue<Player> getLogoutQueue() {
        return logouts;
    }

    public static EntityList<Player> getPlayers() {
        return players;
    }

    public static EntityList<NPC> getNpcs() {
        return npcs;
    }

    public static void register(Entity entity) {
        EntityHandler.register(entity);
    }

    public static void unregister(Entity entity) {
        EntityHandler.deregister(entity);
    }

    public static WorldLoader getLoader() {
        return loader;
    }

    public static int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public static void sequence() {
        // Handle queued logins.
        for (int amount = 0; amount < 10; amount++) {
            Player player = logins.poll();
            if (player == null)
                break;
            register(player);
        }

        // Handle queued logouts.
        int amount = 0;
        Iterator<Player> $it = logouts.iterator();
        while ($it.hasNext()) {
            Player player = $it.next();
            if (player == null || amount >= 10)
                break;
            if (EntityHandler.deregister(player)) {
                $it.remove();
            }
            amount++;
        }

        NpcCombatTask.aggressiveNPCS();

        UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
        UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();
        // Then we execute pre-updating code.
        players.stream().filter(player -> player != null).forEach(playerUpdate::executePreUpdate);
        npcs.stream().filter(npc -> npc != null).forEach(npcUpdate::executePreUpdate);
        // Then we execute parallelized updating code.
        synchronizer.bulkRegister(players.size());
        players.stream().filter(player -> player != null).forEach(playerUpdate::executeUpdate);
        synchronizer.arriveAndAwaitAdvance();
        // Then we execute post-updating code.
        players.stream().filter(player -> player != null).forEach(playerUpdate::executePostUpdate);
        npcs.stream().filter(npc -> npc != null).forEach(npcUpdate::executePostUpdate);
    }

    public static void loadConfiguration() {
        try(FileInputStream fis = new FileInputStream("data/configuration.cfg")) {
            ConfigurationParser p = new ConfigurationParser(fis);
            Map<String, String> mappings = p.getMappings();

            if (mappings.containsKey("worldLoader")) {
                String worldLoaderClass = mappings.get("worldLoader");
                Class<?> loader = Class.forName(worldLoaderClass);
                World.loader = (WorldLoader) loader.newInstance();
            } else {
                loader = new GenericWorldLoader();
            }
            Map<String, Map<String, String>> complexMappings = p.getComplexMappings();
			/*
			 * Packets configuration.
			 */
            if (complexMappings.containsKey("packetHandlers")) {
                for (Map.Entry<String, String> handler : complexMappings.get("packetHandlers").entrySet()) {
                    int id = Integer.parseInt(handler.getKey());
                    Class<?> handlerClass = Class.forName(handler.getValue());
                    PacketManager.getPacketManager().bind(id, (PacketHandler)handlerClass.newInstance());
                    Server.getLogger().fine("Bound " + handler.getValue() + " to opcode: " + id);
                }
            }
        } catch(Exception e) {
            Server.getLogger().log(Level.SEVERE, "Something went wrong while loading the World configuration file.");
        }
    }

    public static void sendGlobalMessage(String message) {
        getPlayers().forEach(player -> player.sendServerMessage(message));
    }

    public static void updatePlayersOnline() {
        if(players.size() > maxPlayerCount)
            maxPlayerCount = players.size();
        World.getPlayers().forEach(player -> player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.PLAYERS_ONLINE));
    }

    public static void updateStaffOnline() {
        World.getPlayers().forEach(player -> player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.STAFF_ONLINE));
    }

    public static void registerGlobalEvents() {
        submit(GetPassTask.getTask());
        submit(new PlayerTask36Seconds());
        submit(new PlayerTask1Second());
        submit(new EarnPotentialTask());
        submit(new PromoteTask());
        submit(new ServerEventTask());
        submit(new ServerMessageTask());
        submit(new BountyHunterTask());
        submit(new BountyHunterLogout());
        submit(new PunishmentExpirationTask());
        submit(new WildernessBossTask(true));
        submit(new PulseGrandExchangeTask());
}

    //TODO MOVE THOSE TO THE CORRECT PLACE
    public static void resetPlayersNpcs(Player player) {
        getNpcs().stream().filter(npc -> npc != null && npc.ownerId == player.getIndex() && player.cE.summonedNpc != npc).forEach(npc -> {
            npc.serverKilled = true;
            if (!npc.isDead()) {
                World.unregister(npc);
            }
            npc.setDead(true);
            npc.health = 0;
        });
    }

    public static void resetSummoningNpcs(Player player) {
        NPC npc = player.cE.summonedNpc;
        if (npc == null)
            return;
        npc.serverKilled = true;
        if (!npc.isDead()) {
            submit(new NpcDeathTask(npc));
        }
        npc.setDead(true);
        npc.health = 0;
        player.SummoningCounter = 0;
        player.getActionSender().sendCombatLevel();
        player.cE.summonedNpc = null;
    }

    /**
     * Checks if a player with the specified name is online.
     *
     * @param name
     * @return the player with the specified username, not case sensitive
     */
    public static Player getPlayerByName(String name) {
            Optional<Player> op = players.search(p -> p != null && p.getName().toLowerCase().equals(name.toLowerCase().replace("_", " ").toLowerCase()));
            return op.isPresent() ? op.get() : null;
    }

    public static boolean playerIsOnline(final Object value) {
        return players.search(player -> player != null && player.getName().toLowerCase().equals(String.valueOf(value).replace("_", " ").toLowerCase())).isPresent();
    }

    /**
     * Attempts to gracefully close a session
     *
     * @param session The session that is about to be closed
     */
    public static boolean gracefullyExitSession(IoSession session) {
        if (session.containsAttribute("player")) {
            try {
                Player p = (Player) session.getAttribute("player");
                if (p != null) {
                    unregister(p);
                    return true;
                }
            } catch (ClassCastException e) {
                System.err.println("Session attribute \"player\" was not a player");
            }
        }
        return false;
    }

    /**
     * Unregisters a player, and saves their game.
     *
     * @param player The player to unregister.
     */
    public static void unregister(final Player player) {
        getLogoutQueue().add(player);
    }
}