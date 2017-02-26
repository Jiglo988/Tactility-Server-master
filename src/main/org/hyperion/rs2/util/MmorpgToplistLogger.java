package org.hyperion.rs2.util;

import org.hyperion.util.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MmorpgToplistLogger {

	/**
	 * The URL to update the votecount from.
	 */
	public static final String URL_STRING = "http://www.mmorpgtoplist.com/runescape";

	/**
	 * The server title to search for.
	 */
	public static final String SEARCH_TITLE = "DeviousPK || Nr 1 SPAWN";

	/**
	 * The connection timeout for the connection to
	 * <code>URL_STRING</code>.
	 */
	public static final int CONNECTION_TIMEOUT = 3000;

	/**
	 * The votecount.
	 */
	private int votecount = 0;

	/**
	 * The singleton.
	 */
	private static MmorpgToplistLogger singleton = new MmorpgToplistLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(MmorpgToplistLogger.getLogger().constructQuery());

	}

	/**
	 * Constructs the SQL query to be called with an SQL connection.
	 *
	 * @returns the sql query to be inserted in a database used for
	 * votecount logging.
	 */
	public String constructQuery() {
		try {
			refresh();
		} catch(Exception e) {
			e.printStackTrace();
		}
		String base = "INSERT INTO `mmorpgtoplist`(`votecount`, `minutestime`) VALUES (";
		String params = votecount + "," + Time.currentTimeMinutes() + ")";
		return base + params;
	}

	/**
	 * Gets the Singleton
	 *
	 * @return
	 */
	public static MmorpgToplistLogger getLogger() {
		return singleton;
	}

	/**
	 * Constructor which determines votecount upon calling.
	 */
	private MmorpgToplistLogger() {
		try {
			refresh();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the votes count.
	 *
	 * @throws IOException
	 */
	public void refresh() throws IOException {
		URL url = new URL(URL_STRING);
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while((line = in.readLine()) != null) {
			//System.out.println(line);
			if(line.contains(SEARCH_TITLE)) {
				while(! line.contains("<div class=\"middlebdcb\">")) {
					line = in.readLine();
				}
				int endtagIndex = line.indexOf(">");
				line = line.substring(endtagIndex + 1);
				int begintagIndex = line.indexOf("<");
				line = line.substring(0, begintagIndex);
				votecount = Integer.parseInt(line);
				//System.out.println(line);
				break;
			}
		}
		in.close();
	}

	/**
	 * Returns the last recorded votecount.
	 *
	 * @return
	 */
	public int getVoteCount() {
		return votecount;
	}

}
