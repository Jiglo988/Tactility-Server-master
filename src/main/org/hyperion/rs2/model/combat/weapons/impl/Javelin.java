package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Javelin extends Weapon {

	public static final int WEAPON_SPEED = 3000;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 806, 1156);

	public Javelin(int id) {
		super(id, Weapon.RANGED_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
		// TODO Auto-generated constructor stub
	}

}
