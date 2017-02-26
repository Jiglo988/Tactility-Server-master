package org.hyperion.rs2.model.combat.weapons;

import org.hyperion.rs2.model.combat.Constants;

public abstract class Godsword extends Weapon implements SpecialWeapon {

	public static final int WEAPON_SPEED = 5000;

	public static final WeaponAnimations WEAPON_ANIMS = new WeaponAnimations(7047, 7046, 7039, 7041, WeaponAnimations.DEFAULT_ANIMS.getDefendAnimation());

	public Godsword(int id) {
		super(id, Constants.MELEE, WEAPON_SPEED, true, false, WEAPON_ANIMS);
	}


}
