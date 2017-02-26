package org.hyperion.rs2.model.content.jge.event;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.content.jge.JGrandExchange;
import org.hyperion.util.Time;

public class PulseGrandExchangeTask extends Task {

    public PulseGrandExchangeTask(){
        super(15 * Time.ONE_MINUTE);
    }

    public void execute() {
        JGrandExchange.getInstance().stream().forEach(JGrandExchange.getInstance()::submit);
    }
}
