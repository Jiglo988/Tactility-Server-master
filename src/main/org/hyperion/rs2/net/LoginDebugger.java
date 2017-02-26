package org.hyperion.rs2.net;

import debug.Debugger;
import org.hyperion.util.Time;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class LoginDebugger extends Debugger {

	public static final File LOG_FILE = new File("./logs/logindump.log");

	public static final int MAX_LOGS_SIZE = 2000;

	public static final boolean DEFAULT_ENABLED = Boolean.FALSE;

	private List<String> logs = new LinkedList<String>();

	private static Debugger singleton = new LoginDebugger(LOG_FILE);

	public LoginDebugger(File logFile) {
		super(logFile);
		setEnabled(DEFAULT_ENABLED);
	}

	public static Debugger getDebugger() {
		return singleton;
	}

	public void log(String message) {
		//System.out.println(message);
		if(! isEnabled())
			return;
		if(message == null) {
			System.out.println("Null being added into log: " + message);
			return;
		}
		if(message.toLowerCase().contains("graham"))
			System.out.println(message);
		String line = Time.getGMTDate() + "\t" + message;
		synchronized(logs) {
			if(logs.size() >= MAX_LOGS_SIZE) {
				logs.remove(0);
			}
			logs.add(line);
		}
	}

	public List<String> getLogs() {
		System.out.println("Getting logs..");
		int counter = 0;
		for(String log : logs) {
			if(log != null)
				counter++;
		}
		System.out.println(counter + " logs collected!");
		return logs;
	}


	static {
	}

}
