package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class GreatAxe extends Weapon {

	public static final int WEAPON_ID = 4718;

	public static final int WEAPON_SPEED = TwohandedSword.WEAPON_SPEED;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(12000, 11999, 12001, 12002, 1156);

	public GreatAxe(int id) {
		super(id, Constants.MELEE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
	}

}
