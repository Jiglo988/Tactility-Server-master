package org.hyperion.sql.dao;

import org.hyperion.sql.DbHub;
import org.hyperion.sql.db.Db;

public class SqlDaoManager<T extends SqlDao> {

    protected final Db db;
    protected final Class<T> clazz;

    protected T dao;

    protected SqlDaoManager(final Db db, final Class<T> clazz) {
        this.db = db;
        this.clazz = clazz;

        dao = db.dbi.onDemand(clazz);
    }

    public T dao() {
        return dao;
    }

    public T open() {
        try{
            return db.dbi.open(clazz);
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return null;
        }
    }

}
