package org.hyperion.sql.impl.log;

import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.impl.log.type.IPLog;
import org.hyperion.sql.impl.log.type.TaskLog;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gilles on 29/02/2016.
 */
public final class LogManager {

    public final static int BUFFER_SIZE = 10000;
    private static int flushMask = 0;
    private static boolean tasksEnabled = false;

    private static final ConcurrentMap<Log.LogType, Set<Log>> MAIN = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Log.LogType, Set<Log>> WAITING = new ConcurrentHashMap<>();

    static {
        for (final Log.LogType type : Log.LogType.values()) {
            MAIN.put(type, new LinkedHashSet<>());
            WAITING.put(type, new LinkedHashSet<>());
        }
    }

    private LogManager() {
    }

    public static boolean insertLog(final Log log) {
        if (!DbHub.initialized() || !DbHub.getPlayerDb().isInitialized())
            return false;
        switch (log.getType()) {
            case IP:
                DbHub.getPlayerDb().getLogs().insertIpLog((IPLog) log);
                return true;
            case TASK:
                if (!tasksEnabled)
                    return false;
            default:
                if (flushing(log.getType())) {
                    WAITING.get(log.getType()).add(log);
                    return true;
                }
                if (!WAITING.get(log.getType()).isEmpty()) {
                    MAIN.get(log.getType()).addAll(WAITING.get(log.getType()));
                    WAITING.get(log.getType()).clear();
                }
                MAIN.get(log.getType()).add(log);
                if (MAIN.get(log.getType()).size() >= BUFFER_SIZE)
                    flushInTask(log.getType());
                return false;
        }
    }

    public static void flush(final Log.LogType type) {
        if (!DbHub.initialized() || !DbHub.getPlayerDb().isInitialized())
            return;
        flushMask |= type.getFlag();
        boolean success = false;
        switch (type) {
            case TASK:
                success = DbHub.getPlayerDb().getLogs().insertTaskLogs(logs(TaskLog.class, type));
                break;
        }
        if (success)
            MAIN.get(type).clear();
        flushMask &= ~type.getFlag();
    }

    public static void flushInTask(final Log.LogType type) {
        GameEngine.submitSql(new EngineTask<Boolean>("Flush logs", 5, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                flush(type);
                return true;
            }
        });
    }

    public static void flushAll() {
        MAIN.keySet().forEach(LogManager::flush);
    }

    private static <T extends Log> Iterator<T> logs(final Class<T> clazz, final Log.LogType type) {
        return MAIN.get(type).stream().map(clazz::cast).iterator();
    }

    private static boolean flushing(final Log.LogType type) {
        return (flushMask & type.getFlag()) != 0;
    }

    static {
        NewCommandHandler.submit(
                new NewCommand("toggletasklog", Rank.DEVELOPER) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        tasksEnabled = !tasksEnabled;
                        player.sendMessage("Task are now " + (tasksEnabled ? "logged" : "no longer logged") + ".");
                        return true;
                    }
                }
        );
    }
}
