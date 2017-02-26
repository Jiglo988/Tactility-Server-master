package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class HandCannon extends Weapon {

	public static final int WEAPON_ID = 15241;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(12155, 12154, 12154, 12153, 1156);

	public static final int WEAPON_SPEED = 3000;

	public HandCannon(int id) {
		super(id, Weapon.RANGED_TYPE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
	}

}
