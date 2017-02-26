package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.weapons.SpecialWeapon;

public class StaffOfLight extends Staff implements SpecialWeapon {

	public StaffOfLight(int id) {
		super(id);
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
