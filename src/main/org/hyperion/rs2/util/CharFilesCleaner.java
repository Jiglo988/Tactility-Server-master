package org.hyperion.rs2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author Arsen Maxyutov
 */
public class CharFilesCleaner implements Runnable {

	public static final long INACTIVE_PERIOD = 1000L * 60 * 60 * 24 * 60;

	private int counter = 0;

	@Override
	public void run() {
		File[] files = new File("./data/characters/mergedchars").listFiles();
		if(files == null)
			return;
		System.out.println("Started char files cleaner! Files count: " + files.length);
		final long currentTime = System.currentTimeMillis();
		for(File file : files) {
			if(currentTime - file.lastModified() > (INACTIVE_PERIOD * 3L)) {
				Character character = new Character(file);
				if(character.shouldDelete()) {
					file.delete();
					counter++;
				}
			}
		}
		System.out.println("Char files cleaned: " + counter);
	}

}

class Character {


	private boolean donator = false;
	private boolean hasrights = false;

	public boolean isDonator() {
		return donator;
	}

	public boolean hasRights() {
		return hasrights;
	}

	public boolean shouldDelete() {
		return !(donator || hasrights);
	}

	public Character(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null) {
				if(line.startsWith("Rights") && ! line.equalsIgnoreCase("Rights=0")) {
					hasrights = true;
					break;
				} else if(line.startsWith("DonatorsBought") && ! line.equalsIgnoreCase("DonatorsBought=0")) {
					donator = true;
					break;
				}
				if(line.startsWith("Skills"))
					break;
			}
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
