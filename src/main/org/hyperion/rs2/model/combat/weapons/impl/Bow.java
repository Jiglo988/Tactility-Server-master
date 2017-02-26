package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Bow extends Weapon {

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 426, 1156);

	public static final int WEAPON_SHORTBOW_SPEED = 1800;

	public static final int WEAPON_LONGBOW_SPEED = 2400;

	public static final int WEAPON_DARKBOW_SPEED = 5400;

	public Bow(int id, int weaponSpeed) {
		super(id, Weapon.RANGED_TYPE, weaponSpeed, true, false, WEAPON_ANIMATIONS);
	}

}
