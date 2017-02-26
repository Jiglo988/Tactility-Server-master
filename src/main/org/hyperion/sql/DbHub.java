package org.hyperion.sql;

import org.hyperion.Configuration;
import org.hyperion.sql.db.DonationsDb;
import org.hyperion.sql.db.GameDb;
import org.hyperion.sql.db.PlayerDb;

public class DbHub {

    private final static boolean CONSOLE_DEBUG = Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG);
    private final static boolean PLAYER_DEBUG = false;

    private static DonationsDb donationsDb;
    private static PlayerDb playerDb;
    private static GameDb gameDb;

    private static boolean initialized;

    public static boolean isConsoleDebug() {
        return CONSOLE_DEBUG;
    }

    public static boolean isPlayerDebug() {
        return PLAYER_DEBUG;
    }

    public static DonationsDb getDonationsDb() {
        return donationsDb;
    }

    public static PlayerDb getPlayerDb() {
        return playerDb;
    }

    public static GameDb getGameDb() {
        return gameDb;
    }

    public static void init() {
        donationsDb = new DonationsDb();
        donationsDb.init();

        playerDb = new PlayerDb();
        playerDb.init();

        gameDb = new GameDb();
        gameDb.init();
        initialized = true;
    }

    public static boolean initialized() {
        return initialized;
    }
}
