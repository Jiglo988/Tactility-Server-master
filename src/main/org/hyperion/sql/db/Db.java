package org.hyperion.sql.db;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.hyperion.Server;
import org.hyperion.sql.DbHub;
import org.skife.jdbi.v2.DBI;

import java.util.logging.Level;

public abstract class Db {

    public DBI dbi;
    private boolean initialized = false;

    protected abstract boolean isEnabled();
    protected abstract String getUrl();
    protected abstract String getUsername();
    protected abstract String getPassword();
    protected abstract void postInit();

    public void init() {
        if(!isEnabled()){
            if(DbHub.isConsoleDebug())
                Server.getLogger().log(Level.INFO, "Db is not enabled - Not initializing: " + getClass().getSimpleName());
            return;
        }
        final MysqlConnectionPoolDataSource pool = new MysqlConnectionPoolDataSource();
        pool.setUrl(getUrl());
        pool.setUser(getUsername());
        pool.setPassword(getPassword());
        dbi = new DBI(pool);

        postInit();
        initialized = true;

        if(DbHub.isConsoleDebug())
            Server.getLogger().log(Level.INFO, "Successfully connected to " + getClass().getSimpleName() + ".");
    }

    public boolean isInitialized() {
        return initialized;
    }
}
