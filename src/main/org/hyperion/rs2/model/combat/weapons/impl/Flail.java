package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Flail extends Weapon {

	public static final int WEAPON_SPEED = 3000;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(2061, 1830, 1831, 2062, 1156);

	public Flail(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
	}

}
