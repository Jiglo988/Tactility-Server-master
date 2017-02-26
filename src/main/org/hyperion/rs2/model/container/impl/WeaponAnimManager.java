package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.weapons.Weapon;

/**
 * @author Martin
 *         really should rewrite this class, but it will do for now.
 */

public class WeaponAnimManager {

    /**
     * @return the walking animation for that weapon
     */
    public static int getWalkAnimation(Player player, int id) {
        Weapon weapon = Weapon.forId(id);
        return weapon.getWalkAnimation(player);
    }

    /**
     * @return the standing animation for that weapon
     */
    public static int getStandAnimation(Player player, int id) {
        Weapon weapon = Weapon.forId(id);
        return weapon.getStandAnimation(player);
    }

    /**
     * @return the running animation for that weapon
     */
    public static int getRunAnimation(Player player, int id) {
        Weapon weapon = Weapon.forId(id);
        return weapon.getRunAnimation(player);
    }

    /**
     * @return the attacking animation for that weapon
     */
    public static int getAttackAnimation(Player player, int id, int type) {
        Weapon weapon = Weapon.forId(id);
	    /*System.out.println("Weapon id: " + id);
		int idx = weapon.getStandAnimation(player);
		System.out.println("Atk id: " + idx + " for wep id: " + id);*/
        return weapon.getAttackAnimation(player);
    }

    /**
     * @return the defend animation for that weapon
     */
    public static int getDefendAnimation(Player player, int id, int shield) {
        if (id == 15486 && shield == 6889 || shield == 3842 || shield == 3841) //todo more
            return 12806; //if the shield is mage book+ sol equip
        if(shield >= 8844 && shield <= 8850)
            return 4177;
        if(shield != - 1) {
            return 1156;
        }
        Weapon weapon = Weapon.forId(id);
        return weapon.getDefendAnimation(player);
    }


    /**
     * @param s2 the name of the weapon being equiped
     * @return the speed of the weapon
     */
    public static int getSpeed(String s2, int weaponId) {//this method was written in a certain way order is quite important!
        switch(weaponId) {
            case 15241:
                return 5400;
            case 18353:
            case 4153:
	        case 17646:
            case 16425:
            case 4718:
            case 16909:
                return 4200;//s
            case 4734:
                return 1800;
            case 18357:
                return 2400;
            case 18786:
            case 19780:
            case 19784:
            case 10858:
                return 2400;
        }
        String s = s2.toLowerCase();
        if(s.startsWith("unarmed"))
            return 3000;
        else if(s.contains("korasi"))
            return 2400;
        else if(s.equals("dharok"))
            return 4200;
        else if(s.equals("torags hammers"))
            return 3000;
        else if(s.equals("guthans warspear"))
            return 3000;
        else if(s.equals("veracs flail"))
            return 3000;
        else if(s.equals("ahrims staff"))
            return 3600;
        else if(s.startsWith("karil"))
            return 2400;
        else if(s.contains("staff")) {
            if(s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer") || s.contains("ancient"))
                return 2400;
            else
                return 3000;
        } else if(s.contains("bow")) {
            if(s.contains("composite") || s.equals("seercull"))
                return 3000;
            else if(s.contains("ogre"))
                return 4800;
            else if(s.contains("dark"))
                return 5400;
            else if(s.contains("long") || s.contains("cross"))
                return 3600;
            else if(s.contains("short") || s.contains("hunt") || s.contains("karils") || s.contains("sword")) {
                return 1800;
            }

            return 3000;
        } else if(s.contains("rapier")) {
            return 2400;
        } else if(s.contains("dagger"))
            return 2400;
        else if(s.contains("godsword"))
            return 3600;
        else if(s.contains("longsword"))
            return 3000;
        else if(s.contains("sword"))
            return 2400;
        else if(s.contains("scimitar"))
            return 2400;
        else if(s.contains("mace"))
            return 3000;
        else if(s.contains("battleaxe"))
            return 3600;
        else if(s.contains("pickaxe"))
            return 3000;
        else if(s.contains("thrownaxe"))
            return 3000;
        else if(s.contains("axe"))
            return 3000;
        else if(s.contains("warhammer"))
            return 3600;
        else if(s.contains("2h"))
            return 4200;
        else if(s.contains("spear"))
            return 3000;
        else if(s.contains("claw"))
            return 2400;
        else if(s.contains("halberd"))
            return 4200;

            //sara sword, 2400ms
        else if(s.startsWith("granite maul"))
            return 4200;
        else if(s.equals("toktz-xil-ak"))//sword
            return 2400;
        else if(s.equals("tzhaar-ket-em"))//mace
            return 3000;
        else if(s.equals("tzhaar-ket-om"))//maul
            return 4200;
        else if(s.equals("toktz-xil-ek"))//knife
            return 2400;
        else if(s.equals("toktz-xil-ul"))//rings
            return 2400;
        else if(s.equals("toktz-mej-tal"))//staff
            return 3600;
        else if(s.contains("whip"))
            return 2400;//2400
        else if(s.contains("dart"))
            return 1100;
        else if(s.contains("knife") && weaponId < 870)
            return 1100;
        else if(s.contains("javelin"))
            return 3600;
        return 3000;
    }
}
