package org.hyperion.rs2.model.content.publicevent;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.Events;

public class EventCountdownTask extends Task {

    final Runnable run;
    final String command;
    final String name;
    final Position position;
    final String message;
    final boolean safe;

    private int counter = 120;
	
	public EventCountdownTask(ServerEventTask.CountDownEventBuilder builder) {
		super(1000);
        this.name = builder.name;
        this.command = builder.command;
        this.position = builder.position;
        this.run = builder.run;
        this.message = builder.message;
        this.safe = builder.safe;
	}
	public void execute() {
		if(counter == 120) {
			Events.fireNewEvent(name, safe, counter, position);
            World.getPlayers().stream().forEach(p -> p.sendServerMessage(name + " event is starting in 2 minutes!"));
		}
		if(--counter == 0) {
			run.run();
            World.submit(new ResetTask());
            this.stop();
		}
        if(counter%10 == 0) {
            for(NPC npc : World.getNpcs()) {
                if(npc != null)
                    npc.forceMessage(name + " event in " + counter + " seconds! Go to " + command + " for " + message + "!");

            }
        }


	}
}
