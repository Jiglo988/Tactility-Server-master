package org.hyperion.rs2.util;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.World;

public enum MassEvent {
	INSTANCE;

	public static MassEvent getSingleton() {
		return INSTANCE;
	}

	public final void executeEvent(final EventBuilder e) {
		World.submit(new Task(e.getDelay()) {
			public void execute() {
				if(e.checkStop())
					this.stop();
				World.getPlayers().forEach(e::execute);
				if(e.getDelay() == 0)
					EventBuilder.stopEvent(e);
			}
		});
	}

	MassEvent() {
	}
}


