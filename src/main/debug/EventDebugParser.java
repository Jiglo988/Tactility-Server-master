package debug;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.python.core.Py.Exception;

/**
 * @author Arsen Maxyutov.
 */
public class EventDebugParser {

	public static final String[] CHECKED = {
			"burybones", "castspell", "magicstuff", "npcattk", "pickup", "soulsplit"
			, "800leach", "1800leech", "dicenpc", "tabmote"
	};

	public static final File LOG_DIR = new File("/Users/saosinhax/Dropbox/Reckless/");
	public static final File LOG_FILE = new File(LOG_DIR + "/specific.log");

	private HashMap<String, LinkedList<Integer>> map = new HashMap<String, LinkedList<Integer>>();

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		EventDebugParser ep = new EventDebugParser();
		ep.loadMap();
		ep.writePartialLogs();
		HeapDumper.dumpHeap(LOG_DIR + "/dump.bin", true);
	}

	private boolean isChecked(String s) {
		for(String checkedString : CHECKED) {
			if(s.equalsIgnoreCase(checkedString))
				return true;
		}
		return false;
	}

	public void writePartialLogs() {
		for(Map.Entry<String, LinkedList<Integer>> entry : map.entrySet()) {
			LinkedList<Integer> list = entry.getValue();
			String name = entry.getKey();
			//System.out.println(name);
			if(! isChecked(name) && leaking(list)) {
				writeLog(name, list);
			}
		}
	}

	public void writeLog(String name, List<Integer> list) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(LOG_DIR + "/" + name + ".log")));
			for(int value : list) {
				out.write("" + value);
				out.newLine();
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean leaking(List<Integer> list) {
		if(list.size() == 0)
			return false;
		int min = list.get(0);
		int max = list.get(0);
		for(int value : list) {
			//System.out.println(value);
			if(value > max)
				max = value;
			else if(value < min)
				min = value;
		}
		if(max < 200)
			return false;
		return true;//(min*3 < max);
	}

	public void print(String key) {
		if(map.get(key) == null)
			return;
		for(int amount : map.get(key)) {
			System.out.println(amount);
		}
	}

	public void loadMap() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(LOG_FILE));
			String line;
			while((line = in.readLine()) != null) {
				if(line.contains("//")) {
					map.clear();
					continue;
				} else if(line.length() <= 1) {
					continue;
				}
				line = line.split("\t")[1];
				String[] elements = line.split(",");
				for(String element : elements) {
					String[] parts = element.split(":");
					String key = parts[0];
					int value = Integer.parseInt(parts[1]);
					if(map.get(key) != null) {
						map.get(key).add(value);
					} else {
						LinkedList<Integer> list = new LinkedList<Integer>();
						list.add(value);
						map.put(key, list);
					}
				}

			}
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}
