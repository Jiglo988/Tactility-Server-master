package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.combat.Constants;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

/**
 * @author Jack Daniels.
 */
public class Longsword extends Weapon {

	/**
	 * The longsword weapon animations.
	 */
	public static final WeaponAnimations WEAPON_ANIMATIONS = WeaponAnimations.DEFAULT_ANIMS;

	/**
	 * The longsword weapon speed.
	 */
	public static final int WEAPON_SPEED = 3000;

	/**
	 * Constructs a new longsword for the specified id.
	 *
	 * @param id
	 */
	public Longsword(int id) {
		super(id, Constants.MELEETYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}

}
