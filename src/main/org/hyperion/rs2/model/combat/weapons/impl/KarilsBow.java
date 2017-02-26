package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class KarilsBow extends Weapon {

	public static final int WEAPON_ID = 4734;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(2074, 2076, 2077, 2075, 1156);

	public static final int WEAPON_SPEED = 2400;

	public KarilsBow(int id) {
		super(id, Weapon.RANGED_TYPE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
		// TODO Auto-generated constructor stub
	}

}
