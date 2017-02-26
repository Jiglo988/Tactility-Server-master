package org.hyperion.rs2.model.combat.weapons;

import org.hyperion.rs2.model.combat.Constants;

/**
 * @author Jack Daniels.
 */
public class Longsword extends Weapon {

	/**
	 * The longsword weapon animations.
	 */
	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(0, 0, 0, 0, 0);

	/**
	 * The longsword weapon speed.
	 */
	public static final int WEAPON_SPEED = 2400;

	/**
	 * Constructs a new longsword for the specified id.
	 *
	 * @param id
	 */
	public Longsword(int id) {
		super(id, Constants.MELEETYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
	}

}
