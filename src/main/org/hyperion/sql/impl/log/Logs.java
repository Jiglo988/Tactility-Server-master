package org.hyperion.sql.impl.log;

import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.dao.SqlDaoManager;
import org.hyperion.sql.db.Db;
import org.hyperion.sql.impl.log.type.IPLog;
import org.hyperion.sql.impl.log.type.TaskLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gilles on 29/02/2016.
 */
public class Logs extends SqlDaoManager<LogDao> {
    public Logs(Db db) {
        super(db, LogDao.class);
    }

    private boolean valid(final int[] result) {
        return result.length > 0 && result[0] > 0;
    }

    public void createTables() {
        GameEngine.submitSql(new EngineTask<Boolean>("Create tables", 5, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                try(final LogDao dao = open()) {
                    dao.initTasks();
                } catch(Exception ex) {
                    if (DbHub.isConsoleDebug())
                        ex.printStackTrace();
                }
                return true;
            }
        });
    }

    public void insertIpLog(IPLog ipLog) {
        GameEngine.submitSql(new EngineTask<Boolean>("Insert IP", 5, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                try(final LogDao dao = open()) {
                    dao.addIp(ipLog.getTimestamp().toString(), ipLog.getPlayerName(), ipLog.getIp());
                } catch(Exception ex) {
                    if (DbHub.isConsoleDebug())
                        ex.printStackTrace();
                }
                return true;
            }
        });
    }

    public boolean insertTaskLogs(Iterator<TaskLog> taskLogs) {
        try(final LogDao dao = open()) {
            return valid(dao.insertValues(taskLogs));
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
        }
        return true;
    }

    public List<IPLog> getAltsByIp(String ip) {
        try(final LogDao dao = open()) {
            return dao.getAltsForIp(ip);
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<IPLog> getIpForPlayer(String playerName) {
        try(final LogDao dao = open()) {
            return dao.getIpForPlayer(playerName);
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}
