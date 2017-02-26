package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class ZSpear extends Weapon {
	public static final int WEAPON_SPEED = 2400;

	public static final int WEAPON_ID = 11716;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 12006, 1156);

	public ZSpear(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
		// TODO Auto-generated constructor stub
	}
}
