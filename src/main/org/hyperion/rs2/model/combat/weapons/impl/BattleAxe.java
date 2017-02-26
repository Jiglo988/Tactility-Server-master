package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class BattleAxe extends Weapon {

	public static final int WEAPON_SPEED = 3600;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 395, 1156);

	public BattleAxe(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}

}
