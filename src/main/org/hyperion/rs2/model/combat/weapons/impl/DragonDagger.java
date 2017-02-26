package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.weapons.SpecialWeapon;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class DragonDagger extends Weapon implements SpecialWeapon {

	public static final int WEAPON_SPEED = 2400;

	public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(808, 819, 824, 402, 1156);

	public DragonDagger(int id) {
		super(id, Weapon.MELEE_TYPE, WEAPON_SPEED, false, false, WEAPON_ANIMATIONS);
		// TODO Auto-generated constructor stub
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
