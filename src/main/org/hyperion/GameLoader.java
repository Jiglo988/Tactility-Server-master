package org.hyperion;

/**
 * Created by Gilles on 11/02/2016.
 */

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.hyperion.engine.GameEngine;
import org.hyperion.map.WorldMap;
import org.hyperion.rs2.ConnectionHandler;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.ShopManager;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.DoorManager;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.jge.JGrandExchange;
import org.hyperion.rs2.model.content.misc.Lottery;
import org.hyperion.rs2.model.content.misc.TriviaBot;
import org.hyperion.rs2.model.content.skill.dungoneering.RoomDefinition;
import org.hyperion.rs2.model.joshyachievementsv2.Achievements;
import org.hyperion.rs2.model.possiblehacks.PossibleHacksHolder;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
import org.hyperion.sql.DbHub;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Gilles
 */
public final class GameLoader {

    private final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
    private final ScheduledExecutorService gameThread = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());
    private final IoAcceptor acceptor = new NioSocketAcceptor();
    private final GameEngine gameEngine = new GameEngine();
    private final int port;

    protected GameLoader(int port) {
        this.port = port;
    }

    public void init() {
        Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");
        executeServiceLoad();
        acceptor.setHandler(new ConnectionHandler());
        serviceLoader.shutdown();
    }

    public void finish() throws IOException, InterruptedException {
        if (!serviceLoader.awaitTermination(1, TimeUnit.MINUTES))
            throw new IllegalStateException("The background service load took too long!");
        acceptor.bind(new InetSocketAddress(port));
        gameThread.scheduleAtFixedRate(gameEngine, 0, Configuration.getInt(Configuration.ConfigurationObject.ENGINE_DELAY), TimeUnit.MILLISECONDS);
    }

    private void executeServiceLoad() {
        //TODO FIX THIS
        //serviceLoader.execute(CleanCharacterFiles::startup);
        serviceLoader.execute(NPCDefinition::init);
        serviceLoader.execute(PossibleHacksHolder::getInstance);
        serviceLoader.execute(RoomDefinition::load);
        serviceLoader.execute(ItemDefinition::init);
        serviceLoader.execute(ClanManager::load);
        serviceLoader.execute(ObjectManager::init);
        serviceLoader.execute(Lottery::init);
        serviceLoader.execute(WorldMap::loadWorldMap);
        serviceLoader.execute(DoorManager::init);
        serviceLoader.execute(NPCManager::init);
        serviceLoader.execute(ContentManager::init);
        serviceLoader.execute(Wilderness::init);
        serviceLoader.execute(GlobalItemManager::init);
        serviceLoader.execute(ShopManager::init);
        serviceLoader.execute(World::loadConfiguration);
        serviceLoader.execute(World::registerGlobalEvents);
        serviceLoader.execute(DbHub::init);
        serviceLoader.execute(PunishmentManager::init);
        serviceLoader.execute(JGrandExchange::init);
        serviceLoader.execute(TriviaBot::init);
        serviceLoader.execute(RestartTask::submitRestartTask);
        serviceLoader.execute(Achievements::load);
    }

    public GameEngine getEngine() {
        return gameEngine;
    }
}
