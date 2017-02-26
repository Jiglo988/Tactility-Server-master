package org.hyperion;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.QuestTab;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Time;

public class Uptime {

	public static final long SERVER_STARTUP = System.currentTimeMillis();

	public long millisUptime() {
		return System.currentTimeMillis() - SERVER_STARTUP;
	}

	public int minutesUptime() {
		long ms = System.currentTimeMillis() - SERVER_STARTUP;
		long seconds = ms / 1000;
		return (int) (seconds / 60);
	}

	/**
	 * @return The Uptime as a String.
	 */
	public String toString() {
		int minutes = minutesUptime();
		int hours = minutes / 60;
		if(hours <= 0) {
			return (minutes + " Mins");
		}
		minutes = minutes % 60;
		return (hours + " Hours, " + minutes + " Mins");
	}

	static {
		TaskManager.submit(new Task(Time.ONE_MINUTE, "Uptime") {
			@Override
			protected void execute() {
				World.getPlayers().forEach(player -> player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.UPTIME));
				System.out.println("Uptime: " + Server.getUptime().toString() + " - Players online: " + World.getPlayers().size() + " - Staff online: " + World.getPlayers().stream().filter(p -> p != null && Rank.isStaffMember(p)).count());
			}
		});
	}
}
