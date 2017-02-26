package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Halberd extends Weapon {

	public static final int WEAPON_SPEED = 3000;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 440, 1156);

	public Halberd(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
	}

}
