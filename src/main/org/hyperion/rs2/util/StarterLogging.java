package org.hyperion.rs2.util;

import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.util.Time;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StarterLogging {

	/**
	 * The delay after which a player can receive a starter again.
	 */
	public static final long STARTER_MAX_DELAY = Time.ONE_DAY;

	public static final long STARTER_MIN_DELAY = Time.ONE_HOUR * 5;

	public static final String SAVE_FILE = "starters.log";

	private final Map<String, Long> starters = new HashMap<String, Long>();

	private static StarterLogging singleton = new StarterLogging();

	public static StarterLogging getLogging() {
		return singleton;
	}

	public void save(String ip, long time) {
		starters.put(ip, time);
		FileLogging.saveGameLog(SAVE_FILE, ip + ":" + time);
	}

	public long lastStarterReceived(String ip) {
		Long time = starters.get(ip);
		if(time == null)
			return 0;
		return time;
	}

	public static void loadData() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(SAVE_FILE));
			String line;
			final long currentTime = System.currentTimeMillis();
			while((line = in.readLine()) != null) {
				String[] parts = line.split(":");
				String ip = parts[0];
				long time = Long.parseLong(parts[1]);
				if(currentTime - time < STARTER_MAX_DELAY)
					getLogging().starters.put(ip, time);
			}
			in.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(SAVE_FILE));
			for(Map.Entry<String, Long> entry : getLogging().starters
					.entrySet()) {
				out.write(entry.getKey() + ":" + entry.getValue());
				out.newLine();
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	static {
		if(new File(SAVE_FILE).exists())
			loadData();
	}

}
