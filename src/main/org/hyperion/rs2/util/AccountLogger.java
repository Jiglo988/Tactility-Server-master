package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AccountLogger {

	public static final String DUPERS_FILE = "./dupers.txt";

	public static final int MAX_SAVED_STATUSES = 40;

	private Player player;

	private LinkedList<Status> lastStatuses;

	private boolean watched = false;

	public AccountLogger(Player player) {
		this.player = player;
		lastStatuses = new LinkedList<Status>();
	}


	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	/**
	 * Logs the given line which should represent an action.
	 * This only happens after the account value of an account has been modified.
	 *
	 * @param line
	 */
	public void log(String line) {
		log(line, false);
	}

	public void log(String line, boolean forced) {
		try {
			int value = player.getAccountValue().getTotalValue();
			Status new_status = new Status(value, line);
			if(lastStatuses.size() == 0) {
				lastStatuses.add(new_status);
				return;
			}
			Status last_status = lastStatuses.getLast();
			lastStatuses.add(new_status);
			if(lastStatuses.size() > MAX_SAVED_STATUSES)
				lastStatuses.poll();
			int change = new_status.value - last_status.value;
			if(forced || watched) {
				write(new_status, change);
			} else if(change > 10000) {
				for(Status status : lastStatuses) {
					write(status, status.value);
				}
				lastStatuses.clear();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void write(final Status status, int change) {
		//SQLite.getDatabase().submitQuery(query);
	}

	/**
	 * Checks if a player is considered as suspicious and should be logged.
	 *
	 * @param player
	 * @return
	 */
	public static boolean isSuspicious(Player player) {
		return (dupers.containsKey(player.getName().toLowerCase()));
	}

	private static Map<String, Object> dupers = new HashMap<String, Object>();

	public static Map<String, Object> getDupers() {
		return dupers;
	}

	static {
		try {
			BufferedReader in = new BufferedReader(new FileReader(DUPERS_FILE));
			String line;
			while((line = in.readLine()) != null) {
				dupers.put(line.toLowerCase(), new Object());
			}
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static class Status {

		protected String line;

		protected int value;

		protected Date date;


		public Status(int value, String line) {
			this.value = value;
			this.line = line;
			date = new Date();
		}

		@Override
		public boolean equals(Object o) {
			if(o instanceof Status) {
				Status other = (Status) o;
				return line.equals(other.line) && value == other.value;
			}
			return false;
		}

		@Override
		public String toString() {
			return date.toString() + " : " + value + " : " + line;
		}
	}
}
