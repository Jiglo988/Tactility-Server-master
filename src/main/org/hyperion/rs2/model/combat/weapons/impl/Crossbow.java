package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Crossbow extends Weapon {

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 4230, 1156);

	public static final int WEAPON_SPEED = 2400;

	public Crossbow(int id) {
		super(id, Weapon.RANGED_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
		// TODO Auto-generated constructor stub
		//System.out.println("Creating crossbow with id : " + id);
	}


}
