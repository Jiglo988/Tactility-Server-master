package org.hyperion.rs2.model.combat.weapons;

import org.hyperion.rs2.model.Player;

/**
 * @author Jack Daniels.
 */
public interface SpecialWeapon {

	/**
	 * Makes the specified player do a special attack.
	 *
	 * @param attacker
	 * @param opponent
	 * @return true if succesful, false if not.
	 */
	public abstract boolean specialAttack(final Player player);

	/**
	 * Gets the special drain for a Special weapon.
	 *
	 * @param player
	 * @return
	 */
	public abstract int getSpecialDrain(final Player player);
}
