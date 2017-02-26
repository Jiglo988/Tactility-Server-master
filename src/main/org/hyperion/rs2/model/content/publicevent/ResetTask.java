package org.hyperion.rs2.model.content.publicevent;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.content.Events;
import org.hyperion.util.Time;

/**
 * Created by Gilles on 15/02/2016.
 */
public final class ResetTask extends Task {

    public ResetTask() {
        super(Time.THIRTY_MINUTES);
    }

    @Override
    public void execute() {
        Events.resetEvent();
        this.stop();
    }
}
