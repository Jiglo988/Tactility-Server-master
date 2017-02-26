package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.*;

import java.util.HashMap;
import java.util.Map;

public class EquipmentReq {

	public static Map<Integer, byte[]> itemLevels = new HashMap<Integer, byte[]>();
	public static Map<Integer, Object> memberItems = new HashMap<Integer, Object>();

	static {/*
	    memberItems.put(4151, 0);
    	memberItems.put(4675, 0);
    	//bandos armdayl
    	memberItems.put(11724, 0);memberItems.put(11726, 0);memberItems.put(11728, 0);memberItems.put(11718, 0);memberItems.put(11720, 0);memberItems.put(11722, 0);
    	//godswords
    	memberItems.put(11694, 0);memberItems.put(11696, 0);memberItems.put(11698, 0);memberItems.put(11700, 0);memberItems.put(11730, 0);
    	//dragon armour
    	memberItems.put(3140, 0);memberItems.put(4087, 0);memberItems.put(4585, 0);memberItems.put(6617, 0);
    	//black dhide
    	memberItems.put(2491, 0);memberItems.put(2497, 0);memberItems.put(2503, 0);
    	//red dhide
    	memberItems.put(2489, 0);memberItems.put(2495, 0);memberItems.put(2501, 0);
    	memberItems.put(4214, 0);memberItems.put(6570, 0);memberItems.put(6585, 0);memberItems.put(861, 0);memberItems.put(859, 0);
    	//ahrim
    	memberItems.put(4708, 0);memberItems.put(4710, 0);memberItems.put(4712, 0);memberItems.put(4714, 0);
    	//dharok
    	memberItems.put(4716, 0);memberItems.put(4718, 0);memberItems.put(4720, 0);memberItems.put(4722, 0);
    	//gutahn
    	memberItems.put(4724, 0);memberItems.put(4726, 0);memberItems.put(4728, 0);memberItems.put(4730, 0);
    	//karils
    	memberItems.put(4732, 0);memberItems.put(4734, 0);memberItems.put(4736, 0);memberItems.put(4738, 0);
    	//torag
    	memberItems.put(4745, 0);memberItems.put(4747, 0);memberItems.put(4749, 0);memberItems.put(4751, 0);
    	//verac
    	memberItems.put(4753, 0);memberItems.put(4755, 0);memberItems.put(4757, 0);memberItems.put(4759, 0);
    	*/
	}


	public static int requiredEloRating(int id) {
		switch(id) {
			case 19817:
			case 19816:
			case 19815:
				return 1500;
			case 19713:
			case 19716:
			case 19719:
				return 1800;

			case 16887: // sagittarian shortbow
			case 16337: // sagittarian longbow
				return 1800;


			case 17193: //sagittarian gear
			case 17339:
			case 17215:
			case 17317:
			case 17061:

				return 2000;
		}
		return 0;
	}

	public static int requiredHonorPoints(int id) {
		switch(id) {
			case 19817:
			case 19816:
			case 19815:
				return 1500;
		}
		return 0;
	}

	public static boolean canEquipItem(Player player, int id) {
    	/*if(memberItems.get(i) != null && !player.isMember){
    		player.getActionSender().sendMessage("You need to be a member to equip this item.");
    		return false;
    	}*/
		if((new Item(id)).getDefinition().isNoted()) {
			player.getActionSender().sendMessage("Where did you get the notion that you could wear noted items?");
			return false;
		}
		int requiredElo = requiredEloRating(id);
		if(player.getPoints().getEloPeak() < requiredElo) {
			String message = "You need a peak elo rating of " + requiredElo + " to equip this item.";
			message = message.replaceAll("an elo", "a PvP");
			player.getActionSender().sendMessage(message);
			return false;
		}
		byte abyte0[] = null;
		if(itemLevels.get(Integer.valueOf(id)) != null) {
			abyte0 = (byte[]) itemLevels.get(Integer.valueOf(id));
		}
		if(abyte0 == null) {
			abyte0 = getRequirements(ItemDefinition.forId(id).getName(), id);
			itemLevels.put(Integer.valueOf(id), abyte0);
        }
		for(int j = 0; j < Skills.SKILL_COUNT; j++) {
			if(player.getSkills().getLevelForExp(j) < abyte0[j]) {
				player.getSkills();
				player.getActionSender().sendMessage((new StringBuilder()).append("You need ").append(abyte0[j]).append(" ").append(Skills.SKILL_NAME[j]).append(" to equip this item.").toString());
				return false;
			}
		}

		return true;
	}

	public static byte[] getRequirements(String s, int ItemId) {

		byte equipReqData[] = new byte[Skills.SKILL_COUNT];
		int type = 3;
		if(Equipment.equipmentTypes.get(ItemId) != null)
			type = Equipment.equipmentTypes.get(ItemId).getSlot();
		boolean isWep = type == 3;
		equipReqData[0] = equipReqData[1] = equipReqData[2] = equipReqData[4] = equipReqData[6] = 0;
		switch(ItemId) { //Dump Here All Item Reqs for common used Items!
            case 16865: // This
            case 16866: // is
            case 16931: // all
            case 16932: // a
            case 17171: // bunch
            case 17172: // of
            case 17237: // celesital
            case 17238: // gear
            case 15796: // that
            case 15807: // needs
            case 15847: // 99
            case 15902: // magic
            case 16195: // and
            case 16755: // 99
            case 16756: // defense
                equipReqData[1] = 99;
                equipReqData[6] = 99;
                break;

            case 17017: //celestial staff
            case 17018:
            case 16173:
                equipReqData[6] = 99;
                break;
            case 10550: // Ranger hat
                equipReqData[1] = 40;
                break;
            case 19817: // Glaiven boots
                equipReqData[1] = 85;
                equipReqData[4] = 75;
            case 19816: // Steadfast boots
                equipReqData[1] = 85;
                equipReqData[0] = 75;
            case 19815: // Ragefire boots
                equipReqData[1] = 85;
                equipReqData[6] = 75;
                break;
            case 15486: // Staff of light
            case 16153: // Staff of light
            case 16154: // Staff of light
            case 16155: // Staff of light
            case 16156: // Staff of light
                equipReqData[0] = 75;
                equipReqData[6] = 75;
                break;
            case 5730:
                equipReqData[0] = 60;
                equipReqData[Skills.DUNGEONEERING] = 80;
                break;
			/**
			 * Torva, Pernix and Virtus
			 */
			case 19713:
			case 19714:
			case 19715:
				equipReqData[2] = 80;
				equipReqData[1] = 80;
				equipReqData[3] = 80;
				break;
			case 19716:
			case 19717:
			case 19718:
				equipReqData[4] = 80;
				equipReqData[1] = 80;
				equipReqData[3] = 80;
				break;
			case 19719:
			case 19720:
			case 19721:
				equipReqData[1] = 80;
				equipReqData[3] = 80;
				equipReqData[6] = 80;
				break;
            case 19605:
			case 11694:
			case 11696:
			case 11700:
				equipReqData[0] = 75;
				return equipReqData;
			case 19780:
			case 10858:
				equipReqData[0] = 78;
				equipReqData[1] = 40;
				equipReqData[2] = 78;
				return equipReqData;
			case 11283:
			case 11284:
				equipReqData[1] = 75;
				return equipReqData;
			case 15241:
				equipReqData[1] = 40;
				equipReqData[4] = 60;
				return equipReqData;
			case 4734:
				equipReqData[4] = 70;
				return equipReqData;
			case 14734:
				equipReqData[1] = 30;
				equipReqData[6] = 60;
				return equipReqData;
            case 12747:
            case 12744:
                for(int i = 0 ; i < 7 ; i++)
                    equipReqData[i] = 99;
                return equipReqData;
			case 13899:
			case 13902:
				equipReqData[0] = 78;
				return equipReqData;
            case 18353:
                equipReqData[0] = 80;
                return equipReqData;
			case 18357:
				equipReqData[4] = 80;
				return equipReqData;
			case 18363:
				equipReqData[Skills.DEFENCE] = 80;
				return equipReqData;

		}
		s = s.toLowerCase().replaceAll("_", " ");
		final SkillcapeAnim.Cape cape = SkillcapeAnim.Cape.getCapeById(ItemId);
		if (cape != null && cape.getSkillId() < equipReqData.length && cape.getSkillId() != -1) {
			equipReqData[cape.getSkillId()] = 99;
			return equipReqData;
		}
		if(s.contains("vesta") || s.contains("statius") || s.contains("morrigan") || s.contains("zuriel")) {
			if(s.contains("corrupt"))
				equipReqData[1] = 20;
			else
				equipReqData[1] = 78;
			return equipReqData;
		}

        if(s.contains("celestial")) {
            equipReqData[6] = 99;
            if(!s.contains("staff"))
                equipReqData[1] = 99;
        }
        if(s.contains("sagittarian")) {
            equipReqData[4] = 99;
            equipReqData[1] = 99;
            if(s.contains("bow"))
                equipReqData[1] = 70;
        }
		if(s.contains("mystic") || s.contains("nchanted")) {
			if(s.contains("staff")) {
				equipReqData[6] = 20;
				equipReqData[0] = 40;
			} else {
				equipReqData[6] = 20;
				equipReqData[1] = 20;
			}
		}
		if(s.contains("infinity")) {
			equipReqData[6] = 50;
			equipReqData[1] = 25;
		}
		if(s.contains("splitbark")) {
			equipReqData[6] = 40;
			equipReqData[1] = 40;
		}
		if(s.contains("godsword")) {
			equipReqData[0] = 75;
		}

		if(s.contains("armadyl") && ! isWep) {
			equipReqData[1] = 65;
			equipReqData[4] = 70;
		}
		if(s.contains("spirit shield")) {
			equipReqData[1] = 75;
			equipReqData[5] = 75;
		}
		if(s.contains("initiate")) {
			equipReqData[1] = 20;
		}
		if(s.contains("bandos") && ! isWep) {
			equipReqData[1] = 65;
		}
		if(s.contains("primal")) {
			if(isWep)
				equipReqData[0] = 99;
			else
				equipReqData[1] = 99;
		}

        if(s.contains("promethium")) {
            if(isWep)
                equipReqData[0] = 90;
            else
                equipReqData[1] = 90;
        }

        if(s.startsWith("snakeskin")) {
            equipReqData[4] = 30;
            equipReqData[1] = 30;
        }

        if(s.contains("saradomin") || s.contains("guthix") || s.contains("zamorak")) {
            if(s.contains("chap") || s.toLowerCase().endsWith(" body"))
                equipReqData[4] = 70;
            if(!s.contains("robe") && !s.contains("staff") && !s.contains("cape"))
                equipReqData[1] = 40;
            else {
                equipReqData[6] = 40;
            }

        }
        if(s.toLowerCase().startsWith("combat robe") || s.toLowerCase().startsWith("battle robe"))
            equipReqData[1] = 40;
		if(s.contains("chaotic")) {
			if(isWep)
				if(ItemId == 18355)
					equipReqData[6] = 80;
				else
					equipReqData[0] = 80;
			else
				equipReqData[1] = 80;
		}
		if(s.equals("warrior helm") || s.equals("farseer helm") || s.equals("archer helm")) {
			equipReqData[1] = 40;
		}
		if(s.contains("green") && s.contains("hide")) {
			equipReqData[4] = 40;
			if(s.contains("body")) {
				equipReqData[1] = 40;
			}
			return equipReqData;
		}
		if((s.contains("blue") || s.contains("(") ) && s.contains("hide")) {
			equipReqData[4] = 50;
			if(s.contains("body")) {
				equipReqData[1] = 40;
			}
			return equipReqData;
		}
		if(s.contains("red") && s.contains("hide")) {
			equipReqData[4] = 60;
			if(s.contains("body")) {
				equipReqData[1] = 40;
			}
			return equipReqData;
		}
		if(s.contains("black") && s.contains("hide")) {
			equipReqData[4] = 70;
			if(s.contains("body")) {
				equipReqData[1] = 40;
			}
			return equipReqData;
		}
		if(s.contains("maul")) {
			equipReqData[0] = equipReqData[2] = 50;
            if(s.contains("(i)"))
                equipReqData[0] = equipReqData[2] = 80;
			return equipReqData;
		}
		if(s.contains("granite")) {
			equipReqData[1] = 50;
			return equipReqData;
		}
		if(s.contains("bronze")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace")) {
				equipReqData[0] = 1;
			} else if(s.contains("bow") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 1;
			} else
				equipReqData[1] = 1;
			return equipReqData;
		}
		if(s.contains("iron")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace")) {
				equipReqData[0] = 1;
			} else if(s.contains("bow") || s.contains("knife") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 1;
			} else
				equipReqData[1] = 1;
			return equipReqData;
		}
		if(s.contains("steel")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace")) {
				equipReqData[0] = 5;
			} else if(s.contains("bow") || s.contains("knife") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 5;
			} else
				equipReqData[1] = 5;
			return equipReqData;
		}
		if(s.contains("black")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace")) {
				equipReqData[0] = 10;
			} else if(s.contains("bow") || s.contains("knife") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 10;
			} else
				equipReqData[1] = 10;
			return equipReqData;
		}
		if(s.contains("mithril")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace")) {
				equipReqData[0] = 20;
			} else if(s.contains("bow") || s.contains("knife") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 20;
			} else
				equipReqData[1] = 20;
			return equipReqData;
		}
		if(s.contains("adamant") || s.toLowerCase().startsWith("adam")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace")) {
				equipReqData[0] = 30;
			} else if(s.contains("bow") || s.contains("knife") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 30;
			} else
				equipReqData[1] = 30;
			return equipReqData;
		}
		if(s.contains("rune") || s.contains("gilded") || s.contains("rock-shell")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace")) {
				equipReqData[0] = 40;
			} else if(s.contains("bow") || s.contains("knife") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 40;
			} else
				equipReqData[1] = 40;
			return equipReqData;
		}
		if(s.contains("dragon") && ! s.contains("nti-") && ! s.contains("fire")) {
			if(s.contains("dagger") || s.contains("sword") || s.contains("scimitar") || s.contains("spear") || s.contains("axe") || s.contains("mace") || s.contains("claws") || s.contains("halberd")) {
				equipReqData[0] = 60;
			} else if(s.contains("bow") || s.contains("knife") || s.contains("dart") || s.contains("javelin") || s.contains("thrownaxe") || s.contains("arrow") || s.contains("bolts")) {
				equipReqData[4] = 60;
			} else
				equipReqData[1] = 60;
			return equipReqData;
		}
		if(s.contains("crystal")) {
			if(s.contains("shield")) {
				equipReqData[1] = 70;
			} else {
				equipReqData[4] = 70;
			}
			return equipReqData;
		}
		if(s.contains("ahrim")) {
			if(s.contains("staff")) {
				equipReqData[6] = 70;
				equipReqData[0] = 70;
			} else {
				equipReqData[6] = 70;
				equipReqData[1] = 70;
			}
		}
		if(s.contains("karil")) {
			if(s.contains("x-bow")) {
				equipReqData[4] = 70;
			} else {
				equipReqData[4] = 70;
				equipReqData[1] = 70;
			}
		}
		if(s.contains("3rd age") && ! s.contains("amulet")) {
			equipReqData[1] = 60;
		}
		if(s.contains("Initiate")) {
			equipReqData[1] = 20;
		}
        if(s.contains("whip"))
            equipReqData[0] = 70;
		if(s.contains("verac") || s.contains("guthan") || s.contains("dharok") || s.contains("torag")) {
			if(s.contains("hammers")) {
				equipReqData[0] = 70;
				equipReqData[2] = 70;
			} else if(s.contains("axe")) {
				equipReqData[0] = 70;
				equipReqData[2] = 70;
			} else if(s.contains("warspear")) {
				equipReqData[0] = 70;
				equipReqData[2] = 70;
			} else if(s.contains("flail")) {
				equipReqData[0] = 70;
				equipReqData[2] = 70;
			} else {
				equipReqData[1] = 70;
			}
		}
		switch(ItemId) {
			case 8839:
			case 8840:
			case 8842:
			case 11663:
			case 11664:
			case 11665:
				equipReqData[0] = 42;
				equipReqData[4] = 42;
				equipReqData[2] = 42;
				equipReqData[6] = 42;
				equipReqData[1] = 42;
				break;
			case 16425:
                equipReqData[0] = 99;
				equipReqData[2] = 99;
				break;
            case 10548:
            case 6141:
            case 10547:
            case 10549:
                equipReqData[1] = 40;
                break;

			case 1135:
			case 2499:
			case 2501:
			case 2503:
			case 10551:
				equipReqData[1] = 40;
				break;
			case 13883:
			case 13879:
				equipReqData[4] = 78;
				return equipReqData;
			case 6522:
			case 11235:
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				equipReqData[4] = 60;
				break;

			case 6524:
				equipReqData[1] = 60;
				break;

			case 11284:
				equipReqData[1] = 75;
				break;

            case 9101:
            case 9198:
            case 9096:
            case 9099:
            case 9097:
            case 9100:
                equipReqData[1] = 40;
                equipReqData[6] = 65;
                break;

			case 6889:
			case 6914:
				equipReqData[6] = 60;
				break;

			case 861:
				equipReqData[4] = 50;
				break;

			case 6528:
				equipReqData[2] = 60;
				break;

			case 10828:
            case 12680:
            case 12681:
				equipReqData[1] = 55;
				break;

			case 11724:
			case 11726:
			case 11728:
				equipReqData[1] = 65;
				break;

            case 6131:
            case 6133:
            case 6135:
                equipReqData[1] = 40;
                break;

            case 10887:
                equipReqData[0] = 50;
                equipReqData[5] = 50;
                break;

			case 3749:
			case 3751:
			case 3755:
            case 12673:
            case 12674:
            case 12672:
            case 12675:
				equipReqData[1] = 45;
				break;

			case 7461:
			case 7462:
				equipReqData[1] = 40;
				break;

			case 8846:
				equipReqData[1] = 5;
				break;

			case 8847:
				equipReqData[1] = 10;
				break;

			case 8848:
				equipReqData[1] = 20;
				break;

			case 8849:
				equipReqData[1] = 30;
				break;

			case 8850:
				equipReqData[1] = 40;
				break;

			case 7460:
				equipReqData[1] = 40;
				break;

			case 837:
				equipReqData[4] = 61;
				break;

            case 6603:
                equipReqData[0] = 75;
                equipReqData[6] = 60;
                return equipReqData;

			case 4151:
				equipReqData[0] = 70;
				return equipReqData;

			case 6724:
				equipReqData[4] = 60;
				return equipReqData;

			case 1215:
			case 5698:
				equipReqData[0] = 60;
				break;

			case 11730:
			case 17646:
				equipReqData[0] = 70;
				break;

			case 4153:
				equipReqData[0] = 50;
				equipReqData[2] = 50;
				return equipReqData;
		}
		return equipReqData;
	}

}
