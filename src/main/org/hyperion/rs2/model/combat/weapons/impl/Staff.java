package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Staff extends Weapon {

	public static final int WEAPON_SPEED = 3000;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(813, 1146, 824, 12029, 2079);

	public Staff(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}

}
