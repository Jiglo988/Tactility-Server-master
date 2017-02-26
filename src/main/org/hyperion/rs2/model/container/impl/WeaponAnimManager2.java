package org.hyperion.rs2.model.container.impl;

/**
 * @author Martin
 *         really should rewrite this class, but it will do for now.
 */

public class WeaponAnimManager2 {

    /**
     * @return the walking animation for that weapon
     */
    public static int getWalkAnimation(int weapon) {
        switch(weapon) {
            case 15241:
                return 12154;
            case 6528:
            case 18353:
            case 16425:
                return 1663;
            case 837:
                return 2258;
            case 4084:
                return 1468;
            case 746:
            case 667:
            case 35:
            case 2402:
            case 8100:
                return 2064;
            case 1379:
            case 3204:
            case 1381:
            case 1383:
            case 1385:
            case 1387:
            case 1389:
            case 1391:
            case 1393:
            case 1395:
            case 1397:
            case 1399:
            case 1401:
            case 1403:
            case 145:
            case 1407:
            case 1409:
            case 3053:
            case 3054:
            case 4170:
            case 4675:
            case 4710:
            case 13867:
            case 6526:
            case 4726:
            case 6562:
            case 6563:
            case 6914:
            case 5730:
            case 15486://sol
            case 16153://sol
            case 16154://sol
            case 16155://sol
            case 16156://sol
                return 1146;
            case 13905:
            case 13988:
            case 14692:
            case 17143:
            case 18369:
                return 2064;
            case 7158:
            case 1319:
            case 16909:
            case 6609:
                return 7046;
            case 4755:
                return 1830;
            case 4734:
                return 2076;
            case 4153:
	    case 17646:
                return 1663;
            case 4718:
            case 4886:
            case 4887:
            case 4888:
            case 4889:
                return 11999;
            case 4151:
            case 15441:
            case 15442:
            case 15443:
            case 15444:
                return 1660;
            case 10887:
                return 5867;
            case 11694:
            case 19605:
            case 11696:
            case 11698:
            case 11700:
            case 11730:
                return 7046;

            default:
                return 0x333;
        }
    }

    /**
     * @return the standing animation for that weapon
     */
    public static int getStandAnimation(int weapon) {
        switch(weapon) {
            case 15241:
                return 12155;
            case 837:
                return 2257;
            case 4084:
                return 1462;
            case 4718:
            case 4886:
            case 4887:
            case 4888:
            case 4889:
                return 12000;
            case 746:
            case 667:
            case 35:
            case 2402:
            case 8100:
                return 2065;
            case 7158:
            case 1319:
            case 6609:
            case 16909:
                return 7047;
            case 4755:
                return 2061;
            case 4734:
                return 2074;
            case 4151:
            case 15441:
            case 15442:
            case 15443:
            case 15444:
                return 11973;
            case 4153:
                return 1662;
            case 6528:
            case 13905:
            case 13988:
            case 14692:
            case 17143:
            case 18369:
            case 18353:
            case 16425:
                return 0x811;
            case 1379:
            case 3204:
            case 1381:
            case 1383:
            case 1385:
            case 1387:
            case 1389:
            case 1391:
            case 1393:
            case 1395:
            case 1397:
            case 1399:
            case 1401:
            case 1403:
            case 145:
            case 1407:
            case 1409:
            case 3053:
            case 3054:
            case 4170:
            case 4675:
            case 4710:
            case 6526:
            case 4726:
            case 6562:
            case 6563:
            case 6914:
            case 5730:
            case 15486:
            case 16153:
            case 16154:
            case 16155:
            case 16156:
                return 813;
            case 10887:
                return 5869;
            case 19605:
            case 11694:
            case 11696:
            case 11698:
            case 11700:
            case 11730:
                return 7047;

            default:
                return 0x328;
        }
    }

    /**
     * @return the runing animation for that weapon
     */
    public static int getRunAnimation(int weapon) {
        switch(weapon) {
            case 15241:
                return 12154;
            case 837:
                return 2251;
            case 4084:
                return 1468;
            case 4718:
            case 4886:
            case 4887:
            case 4888:
            case 4889:
                return 12001;
            case 4734:
                return 2077;
            case 4151:
            case 15441:
            case 15442:
            case 15443:
            case 15444:
                return 1661;
            case 6818:
                return 1765;
            case 4755:
                return 1831;
            case 7158:
            case 1319:
            case 6609:
            case 16909:
                return 7039;
            case 4714:
                return 2077;
            case 6528:
            case 13905:
            case 13988:
            case 14692:
            case 17143:
            case 18369:
            case 18353:
            case 16425:
                return 1664;
            case 4153:
                return 1664;
            case 10887:
                return 5868;
            case 19605:
            case 11694:
            case 11696:
            case 11698:
            case 11700:
            case 11730:
                return 7039;
            default:
                return 0x338;
        }
    }

    /**
     * @return the attacking animation for that weapon
     */
    public static int getAttackAnimation(int weapon, int type) {
        switch(weapon) {
            case 15241:
                return 12153;
            case 16955://rapiers
            case 18349:
                return 386;
            case 6528: // Obby Maul
            case 18353:// Chaotic Maul
            case 16425:
                return 2661;
            case 9174:
            case 9176:
            case 9177:
            case 9179:
            case 9181:
            case 9183:
            case 9185:
            case 18357:
                return 4230;
            case 4726: // guthan
                return 2080;
            case 4747: // torag
                return 0x814;
            case 4718: // dharok
            case 4886:
            case 4887:
            case 4888:
            case 4889:
                return 12002;
            case 4710: // ahrim
                return 406;
            case 4755: // verac
                return 2062;
            case 4734: // karil
                return 2075;
            case 4153:
                return 1665;
            case 3190:
            case 3192:
            case 3194:
            case 3196:
            case 3198:
            case 3200:
            case 3202:
            case 3204:
                return 440; // halberd
            case 4151:
            case 15441:
            case 15442:
            case 15443:
            case 15444://whip
                return 1658; // whip
            case 1215:
            case 1231:
            case 5680:
            case 5698:
                return 402; // dragon dagger
            case 1307:
            case 1309:
            case 1311:
            case 1313:
            case 1315:
            case 1317:
                if(type == 2)
                    return 406;
                return 407;
            case 1277:
            case 1279:
            case 1281:
            case 1283:
            case 1285:
            case 1287:
            case 1289:
            case 1291:
            case 1293:
            case 1295:
            case 1297:
            case 1299:
            case 1301:
            case 1303:
            case 1305:
            case 1321:
            case 1323:
            case 1325:
            case 1327:
            case 1329:
            case 1331:
            case 1333:
            case 4587:
                if(type != 3)
                    return 12029; // sword slash emote for scim, sword setc.
                return 412;
            case 1349:
            case 1351:
            case 1353:
            case 1355:
            case 1357:
            case 1359:
            case 1361:
            case 1363:
            case 1365:
            case 1367:
            case 1369:
            case 1371:
            case 1373:
            case 1375://batleaxe
            case 1377:
            case 6739:
                return 412;
            case 4214:
            case 6724:
            case 861:
            case 4212:
            case 839:
            case 841:
            case 843:
            case 845:
            case 847:
            case 849:
            case 851:
            case 853:
            case 855:
            case 857:
            case 11235:
            case 15701:
            case 15702:
            case 15703:
            case 15704:
            case 4827:
                return 426; // bows
            case 13883:
            case 13879:
                return 806;
            case 7158://2hs
            case 1319:
            case 6609:
            case 16909:
            case 19605:
            case 11694:
            case 11696:
            case 11698:
            case 11700:
            case 11730:
                return 7041;
            case 10887:
                return 5865;
	    /*if (type == 2 || type == 1)
			return 7041;
		if (type == 3)
			return 7048;
		if (type == 4)
			return 7049;*/
		/*case 4718://dharok
			if (type != 3)
				return 2067;
			return 2066;*/
            default:
                if(type == 2)
                    return 12029;
                return 12029;
        }
    }

    /**
     * @return the defend animation for that weapon
     */
    public static int getDefendAnimation(int weapon, int shield) {
        if(shield >= 8844 && shield <= 8850)
            return 4177;
        if(shield != - 1) {
            return 1156;
        }
        switch(weapon) {
            case 15486://staff of light
            case 16153://staff of light
            case 16154://staff of light
            case 16155://staff of light
            case 16156://staff of light
                return 12806;
            case 15241:
                return 1666;
            case 4151:
            case 15441: // whip
            case 15442: // whip
            case 15443: // whip
            case 15444: // whip
                return 11974;
            case 4755:
                return 2063;
            case 4747:
            case 4718://bows aswell as hammers and greataxe
            case 4886:
            case 4887:
            case 4888:
            case 4889:
                return 12004;
            case 4726://warspear
                return 415;
            case 4710://ahrim staff
                return 2079;
            case 4153:
            case 6528:
            case 13905:
            case 13988:
            case 14692:
            case 17143:
            case 18369:
            case 18353:
            case 16425:
            case 8103:
                return 1666;
            case 4675:
                return 1429;//staffs

            //return 397;//battleaxe, and daggers
            case 1307:
            case 1309:
            case 1311:
            case 1313:
            case 1315:
            case 1317:
            case 1319:
            case 16909:
                return 410;
            default:
                return 404;
        }
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
            case 16425:
            case 4718:
            case 16909:
                return 4200;
            case 4734:
                return 1800;
            case 18357:
                return 2400;
            case 18786:
            case 19780:
            case 10858:
            case 19784:
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
        else if(s.contains("whip") || s.contains("rapier"))
            return 2400;//2400
        else if(s.contains("dart"))
            return 1100;
        else if(s.contains("knife"))
            return 1100;
        else if(s.contains("javelin"))
            return 3600;
        return 3000;
    }
}
