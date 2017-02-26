package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Player;

public abstract class EventBuilder {
	private boolean executing;
	private int milliseconds;

	public EventBuilder(int ms) {
		this.milliseconds = ms;
	}

	public EventBuilder() {
		this(0);
	}

	public int getDelay() {
		return milliseconds;
	}

	public static final void stopEvent(EventBuilder e) {
		e.executing = false;
	}

	public boolean checkStop() {
		return ! executing;
	}

	public abstract void execute(Player p);
}
