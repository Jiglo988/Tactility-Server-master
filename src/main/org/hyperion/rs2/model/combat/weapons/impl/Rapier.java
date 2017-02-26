package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Rapier extends Weapon {

	public static final int WEAPON_SPEED = 2400;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 386, 1156);

	public Rapier(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}
}
