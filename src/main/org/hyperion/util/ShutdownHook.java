package org.hyperion.util;

import org.hyperion.Server;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.skill.dungoneering.Dungeon;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.sql.impl.log.LogManager;

import java.util.logging.Logger;

/**
 * Created by Gilles on 11/02/2016.
 */
public class ShutdownHook extends Thread {

    /**
     * The ShutdownHook logger to print out information.
     */
    private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

    @Override
    public void run() {
        logger.info("The shutdown hook is processing all required actions...");
        Server.setUpdating(true);
        World.getPlayers().forEach(Trade::declineTrade);
        Dungeon.activeDungeons.forEach(Dungeon::complete);
        World.getPlayers().stream().filter(player -> player != null).forEach(PlayerSaving::save);
        ClanManager.save();
        LogManager.flushAll();
        Server.getLoader().getEngine().finish();
        logger.info("The shutdown hook actions have been completed, shutting the server down...");
    }
}
