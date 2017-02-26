package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.SpecialBar;

/**
 * @author Arsen Maxyutov.
 */
public class ArmadylGodsword extends Godsword {

	/**
	 * The default weapon id.
	 */
	public static final int WEAPON_ID = 11694;

	/**
	 * The special drain.
	 */
	public static final int SPECIAL_DRAIN = SpecialBar.FULL / 2;

	/**
	 * The special damage bonus.
	 */
	public static final double SPECIAL_DAMAGE_BONUS = 1.15;

	/**
	 * The weapon gfx on special.
	 */
	public static final int PLAYER_GFX = 1222;

	/**
	 * The weapon animation on special.
	 */
	public static final int PLAYER_ANIMATION = 7074;

	/**
	 * Constructs a new ArmadylGodsword with the default weapon id for this weapon.
	 */
	public ArmadylGodsword() {
		this(WEAPON_ID);
	}

	/**
	 * Constructs a new ArmadylGodsword with the specified id.
	 *
	 * @param id
	 */
	public ArmadylGodsword(int id) {
		super(id);
	}

	@Override
	public boolean specialAttack(Player player) {
		return true;
	}

	@Override
	public int getSpecialDrain(Player player) {
		return SPECIAL_DRAIN;
	}

}
