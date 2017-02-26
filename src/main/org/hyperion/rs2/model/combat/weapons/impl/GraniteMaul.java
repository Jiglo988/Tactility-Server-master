package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class GraniteMaul extends Weapon {

	public static final int WEAPON_SPEED = 4200;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(1662, 1663, 1664, 1665, 1156);

	public GraniteMaul(int id) {
		super(id, Constants.MELEE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
	}

}
