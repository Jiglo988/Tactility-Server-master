package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.weapons.SpecialWeapon;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Anchor extends Weapon implements SpecialWeapon {

	public static final int WEAPON_ID = 10887;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(5869, 5867, 5868, 5865, WeaponAnimations.DEFAULT_ANIMS.getDefendAnimation());

	public Anchor(int id) {
		super(id, Constants.MELEE, 2400, true, false, WEAPON_ANIMATIONS);
	}

	@Override
	public boolean specialAttack(Player player) {
		return false;
	}

	@Override
	public int getSpecialDrain(Player player) {
		return 0;
	}

}
