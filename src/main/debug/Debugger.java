package debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public abstract class Debugger {

	private File logFile;

	private boolean enabled;

	public Debugger(File logFile) {
		this.logFile = logFile;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean b) {
		enabled = b;
	}

	public File getLogFile() {
		return logFile;
	}

	public abstract void log(String message);

	public abstract List<String> getLogs();

	public boolean dumpLogs() {
		try {
			FileWriter fw = new FileWriter(getLogFile());
			BufferedWriter bw = new BufferedWriter(fw);
			int counter = 0;
			int nullcounter = 0;
			synchronized(getLogs()) {
				for(String line : getLogs()) {
					if(line == null) {
						nullcounter++;
						continue;
					}
					counter++;
					bw.write(line);
					bw.newLine();
				}
			}
			bw.write("Dumped " + counter + " lines. " + nullcounter + " lines were null");
			bw.newLine();
			bw.close();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
