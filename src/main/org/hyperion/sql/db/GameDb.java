package org.hyperion.sql.db;

import org.hyperion.Configuration;
import org.hyperion.sql.impl.punishments.Punishments;

import static org.hyperion.Configuration.ConfigurationObject.*;

/**
 * Created by Gilles on 3/02/2016.
 */
public class GameDb extends Db {

    private Punishments punishment;

    public Punishments getPunishment() {
        return punishment;
    }

    @Override
    protected boolean isEnabled() {
        return Configuration.getBoolean(GAME_DB_ENABLED);
    }

    @Override
    protected String getUrl() {
        return Configuration.getString(GAME_DB_URL);
    }

    @Override
    protected String getUsername() {
        return Configuration.getString(GAME_DB_USER);
    }

    @Override
    protected String getPassword() {
        return Configuration.getString(GAME_DB_PASSWORD);
    }

    @Override
    protected void postInit() {
        punishment = new Punishments(this);
    }
}
