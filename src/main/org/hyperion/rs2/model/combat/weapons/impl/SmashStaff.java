package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class SmashStaff extends Weapon {
	
	public static final int WEAPON_SPEED = 3000;
	
	public static final WeaponAnimations WEAPON_ANIMATIONS = 
			WeaponAnimations.create(808, 1146, 1210, 13047, 13046);
	
	public SmashStaff(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}
}
