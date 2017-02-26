package org.hyperion.sql.impl.log.type;

import org.hyperion.sql.impl.log.Log;

/**
 * Created by Gilles on 2/03/2016.
 */
public class TaskLog extends Log {

    private final String taskName;
    private final long executeTime;
    private final String className;

    public TaskLog(String taskName, long executeTime, String className) {
        super(now(), LogType.TASK);
        this.taskName = taskName;
        this.executeTime = executeTime;
        this.className = className;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public String getClassName() {
        return className;
    }
}
