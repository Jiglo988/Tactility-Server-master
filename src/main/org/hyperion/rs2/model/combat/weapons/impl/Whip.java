package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.weapons.SpecialWeapon;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class Whip extends Weapon implements SpecialWeapon {

	public static final int WEAPON_SPEED = 2400;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(11973, 1660, 1661, 1658, 1156);

	public Whip(int id) {
		super(id, Constants.MELEE, WEAPON_SPEED, false, true, WEAPON_ANIMATIONS);
	}

	@Override
	public boolean specialAttack(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSpecialDrain(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

}
