package org.hyperion.rs2.model.combat.weapons.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

/**
 * Created by FUZENSETH on 30.10.2014.
 */
public class ChaoticMaul extends Weapon {

    public static final int WEAPON_SPEED = 4200;

    public static final WeaponAnimations WEAPON_ANIMATIONS = new WeaponAnimations(13217,13218,18320,13055,13054);

    public ChaoticMaul(int id) {
        super(id, Constants.MELEE, WEAPON_SPEED, true, false, WEAPON_ANIMATIONS);
    }
}
