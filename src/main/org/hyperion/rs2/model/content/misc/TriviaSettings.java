package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;

public class TriviaSettings {

	public static final boolean DEFAULT_ENABLED = false;

	private static final int TIMER = 3000;

	private long lastTimeAnswered;

	public void updateTimer() {
		lastTimeAnswered = System.currentTimeMillis();
	}

	public TriviaSettings(long lastTimeAnswered) {
		this.lastTimeAnswered = lastTimeAnswered;
	}

	public boolean canAnswer() {
		return System.currentTimeMillis() - lastTimeAnswered > TIMER;
	}

	private void resetTimer() {
		lastTimeAnswered = 0;
	}

	public static void resetAllTimers() {
		for(Player p : World.getPlayers()) {
			if(p != null) {
                p.getTrivia().resetTimer();
            }
		}
	}
}
