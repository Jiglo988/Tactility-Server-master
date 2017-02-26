package org.hyperion.rs2.model.combat.weapons;

import java.util.HashMap;
import java.util.Map;

public class WeaponManager {

	private Map<Integer, Weapon> weapons = new HashMap<Integer, Weapon>();

	private static final WeaponManager manager = new WeaponManager();

	public static WeaponManager getManager() {
		return manager;
	}

	public Weapon get(int id) {
		return weapons.get(id);
	}

	public void put(int id, Weapon weapon) {
		weapons.put(id, weapon);
	}

	public int size() {
		return weapons.size();
	}

	private WeaponManager() {

	}
}
