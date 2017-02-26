package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Dagger extends Weapon {
	
	public static final int WEAPON_SPEED = 2400;
	
	public static final WeaponAnimations WEAPON_ANIMATIONS = WeaponAnimations.create(808, 819, 824, 428, 1156);
																					//808, 819, 824, 402, 1156

	
	public Dagger(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}
}
