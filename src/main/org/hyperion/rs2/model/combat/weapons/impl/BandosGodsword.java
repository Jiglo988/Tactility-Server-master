package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.model.Player;

public class BandosGodsword extends Godsword {

	public static final int WEAPON_ID = 11696;

	public BandosGodsword(int id) {
		super(id);
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
