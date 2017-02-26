package org.hyperion.rs2.util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Arsen Maxyutov
 */
public class LogsCleaner implements Runnable {

	public static final File DIR = new File("./logs/");

	public static final long INACTIVE_PERIOD = 1000L * 60 * 60 * 24 * 30;

	private int counter = 0;

	@Override
	public void run() {
		System.out.println("Started log files cleaner!");
		for(File file : DIR.listFiles()) {
			try {
				checkFile(file);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Logs deleted: " + counter);
	}

	private void checkFile(File file) throws Exception {
		if(file.isDirectory()) {
			for(File sub : file.listFiles()) {
				checkFile(sub);
			}
		} else {
			Log log = new Log(file);
			if(log.shouldDelete())
				file.delete();
			else if(log.size() > Log.MAX_LOG_SIZE)
				log.resizeFile();
		}
	}

}

class Log {

	public static final long MAX_LOG_SIZE = 1024 * 200; //200 Kb

	private File file;

	public long size() {
		return file.length();
	}

	public long lastActive() {
		return file.lastModified();
	}

	public boolean shouldDelete() {
		return System.currentTimeMillis() - lastActive() > LogsCleaner.INACTIVE_PERIOD;
	}

	public void resizeFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			List<String> list = new LinkedList<String>();
			String line;
			while((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
			file.delete();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = list.size() / 2; i < list.size(); i++) {
				bw.write(list.get(i));
				bw.newLine();
			}
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Log(File file) {
		this.file = file;
	}
}
