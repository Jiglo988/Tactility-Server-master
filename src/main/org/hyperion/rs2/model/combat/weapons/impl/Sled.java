package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Sled extends Weapon {

	public static final int WEAPON_SPEED = 20000;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(1462, 1468, 1468, WeaponAnimations.DEFAULT_ANIMS.getAttackAnimation(), 1156);

	public Sled(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}


}
