package org.hyperion.engine.task;

import org.hyperion.rs2.model.World;
import org.hyperion.rs2.saving.PlayerSaving;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class TaskManager {

	private final static Queue<Task> pendingTasks = new LinkedList<>();

	private final static List<Task> activeTasks = new LinkedList<>();

	public static void sequence() {
		try {
			Task t;
			while ((t = pendingTasks.poll()) != null) {
				if (t.isRunning()) {
					activeTasks.add(t);
				}
			}

			Iterator<Task> it = activeTasks.iterator();

			while (it.hasNext()) {
				t = it.next();
				if (!t.tick())
					it.remove();
			}
		} catch(Throwable e) {
			e.printStackTrace();
			World.getPlayers().stream().filter(player -> player != null).forEach(PlayerSaving::save);
		}
	}

	public static void submit(Task... tasks) {
		for(Task task : tasks) {
			if (!task.isRunning())
				return;
			if (task.isImmediate()) {
				task.execute();
			}
			pendingTasks.add(task);
		}
	}

	public static void cancelTasks(Object key) {
		try {
			pendingTasks.stream().filter(t -> t.getKey().equals(key)).forEach(Task::stop);
			activeTasks.stream().filter(t -> t.getKey().equals(key)).forEach(Task::stop);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static int getTaskAmount() {
		return (pendingTasks.size() + activeTasks.size());
	}
}
