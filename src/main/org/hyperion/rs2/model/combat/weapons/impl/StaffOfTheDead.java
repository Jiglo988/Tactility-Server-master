package org.hyperion.rs2.model.combat.weapons.impl;


import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponAnimations;

public class StaffOfTheDead extends Weapon{

    public StaffOfTheDead() {
        super(6603, Weapon.MELEE_TYPE, 2400, false, false, WeaponAnimations.create(813, 1146, 824, 440, 13046));
    }
}
