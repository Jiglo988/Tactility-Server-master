package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class ToragsHammers extends Weapon {

	public static final int WEAPON_ID = 4726;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 2068, 1156);

	public static final int WEAPON_SPEED = 3000;

	public ToragsHammers(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
		// TODO Auto-generated constructor stub
	}

}
