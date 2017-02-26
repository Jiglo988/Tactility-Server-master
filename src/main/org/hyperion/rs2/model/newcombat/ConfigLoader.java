package org.hyperion.rs2.model.newcombat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigLoader {

	public static final File LOAD_FILE = new File(System.getProperty("user.home") + "/desktop/config.cfg");

	private Player attacker;

	private Player opponent;

	public Player getAttacker() {
		return attacker;
	}

	public Player getDefender() {
		return opponent;
	}

	public ConfigLoader() throws Exception {
		attacker = new Player();
		opponent = new Player();
		BufferedReader in = new BufferedReader(new FileReader(LOAD_FILE));
		String line;
		while((line = in.readLine()) != null) {
			if(line.startsWith("//") || line.length() <= 1)
				continue;
			String[] parts = line.split(",");
			if(parts[0].equals("attacker")) {
				attacker.set(parts[1], parts[2]);
			} else if(parts[0].equals("opponent")) {
				opponent.set(parts[1], parts[2]);
			} else if(parts[0].equals("attackbonus")) {
				DamageGenerator.baseAttackBonus = Double.parseDouble(parts[1]);
			} else if(parts[0].equals("defendbonus")) {
				DamageGenerator.baseDefendBonus = Double.parseDouble(parts[1]);
			} else if(parts[0].equals("weaponbonus")) {
				DamageGenerator.weaponBonus = Double.parseDouble(parts[1]);
			} else if(parts[0].equals("armorbonus")) {
				DamageGenerator.armorBonus = Double.parseDouble(parts[1]);
			}
		}
		in.close();
	}


}
