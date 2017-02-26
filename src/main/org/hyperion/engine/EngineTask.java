package org.hyperion.engine;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * The IO task
 *
 * Created by Gilles on 23/02/2016.
 */
public abstract class EngineTask<T> implements Callable<T> {

    private final String taskName;
    private final long timeout;
    private final TimeUnit timeUnit;
    private final boolean cancellable;

    public EngineTask(String taskName, long timeout, TimeUnit timeUnit, boolean cancellable) {
        this.taskName = taskName;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.cancellable = cancellable;
    }

    public EngineTask(String taskName, boolean cancellable) {
        this(taskName, 5, TimeUnit.SECONDS, cancellable);
        if(cancellable)
            throw new IllegalStateException("This constructor can only be called if the task is not cancellable.");
    }

    public EngineTask(String taskName, long timeout, TimeUnit timeUnit) {
        this(taskName, timeout, timeUnit, true);
    }

    public final String getTaskName() {
        return taskName;
    }

    public final long getTimeout() {
        return timeout;
    }

    public final TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public final boolean isCancellable() {
        return cancellable;
    }

    public void stopTask() {}
}
