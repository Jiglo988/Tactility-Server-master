package org.hyperion.rs2.util;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class PlayercountLogger {

	/**
	 * Holds the first time in ms that playercount logging has started.
	 */
	private static final long BEGIN_TIME = 1321649384735L; // 19 November

	private static PlayercountLogger singleton = new PlayercountLogger();

	/**
	 * Returns time in minutes from <code>BEGIN_TIME</code> up to now.
	 *
	 * @return
	 */
	private int getTime() {
		long mstime = System.currentTimeMillis() - BEGIN_TIME;
		long stime = mstime / 1000;
		long minutetime = stime / 60;
		return (int) minutetime;
	}


	public static PlayercountLogger getLogger() {
		return singleton;
	}

	private PlayercountLogger() {

	}

	/**
	 * Writes the playercounter log.
	 *
	 * @param players
	 */
	public void log(int players) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("./data/playercountlog.txt", true));
			bw.write(players + "," + getTime());
			bw.newLine();
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
