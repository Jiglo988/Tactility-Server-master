package org.hyperion.rs2.model.content.skill;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.SummoningData;
import org.hyperion.rs2.model.container.BoB;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

/**
 * @author Vegas/Arsen/Linus/Jolt/Flux <- Same Person
 */

public class Summoning implements ContentTemplate {

	private static final int POUCH = 12155;
	private static final int SHARD = 18016;
	private static final int GOLDCHARM = 12158;
	private static final int GREENCHARM = 12159;
	private static final int CRIMSONCHARM = 12160;
	private static final int BLUECHARM = 12163;

	public void addExperience(Player p, int amount) {
		p.getSkills().addExperience(23, amount);
	}

	public boolean hasItems(Player p, int charmId, int itemId,
	                        int shardsAmount, int req) {
		if(ContentEntity.getItemAmount(p, charmId) < 1) {
			ItemDefinition ID = ItemDefinition.forId(charmId);
			p.getActionSender().sendMessage(
					"You need a " + ID.getName() + " to make this pouch.");
			return false;
		}
		if(ContentEntity.getItemAmount(p, itemId) < 1) {
			ItemDefinition ID = ItemDefinition.forId(itemId);
			p.getActionSender().sendMessage(
					"You need " + ID.getName() + " to make this pouch.");
			return false;
		}
		if(ContentEntity.getItemAmount(p, POUCH) < 1) {
			p.getActionSender().sendMessage("You need a pouch for this..");
			return false;
		}
		if(ContentEntity.getItemAmount(p, SHARD) < shardsAmount) {
			p.getActionSender().sendMessage(
					"You need " + shardsAmount + " shards to make this pouch.");
			return false;
		}

		if(p.getSkills().getLevel(23) < req) {
			p.getActionSender().sendMessage(
					"You need a Summoning Level of " + req
							+ " to make this pouch.");
			return false;
		}

		ContentEntity.deleteItem(p, itemId);
		ContentEntity.deleteItem(p, charmId);
		ContentEntity.deleteItem(p, POUCH);
		ContentEntity.deleteItemA(p, SHARD, shardsAmount);
		return true;
	}

	public void ItemonItem(Player p, int itemUsed, int useWith) {
		int charmId = 1;
		int itemId = 1;
		int shardsAmount = 1;
		int req;

		if(useWith != POUCH) {
			p.getActionSender().sendMessage("You cannot do this.");
			return;
		}

		if(p.getRandomEvent().skillAction(5)) {
			return;
		}

		switch(itemUsed) {

			case 2859: //Spirit wolf
				req = 1;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = 2859; //wolf bones
				shardsAmount = 7;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12047, 1);
					addExperience(p, 48);
				}
				break;

			case 2138: //dreadfowl
				charmId = GOLDCHARM;
				req = 4;
				itemId = 2138; //raw chicken
				shardsAmount = 8;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					addExperience(p, 93);
					ContentEntity.addItem(p, 12043);
				}

				break;

			case 6291: //spirit spider
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = 6291;//spider carcass
				shardsAmount = 8;
				req = 10;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12059, 1);
					addExperience(p, 126);
				}
				break;

			case 3369: //thorny snail
				req = 13;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = 3369; //raw thin snail
				shardsAmount = 9;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12019, 1);
					addExperience(p, 126);
				}
				break;

			case 440: //granite crab
				req = 16;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = 440; //iron ore
				shardsAmount = 7;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12009, 1);
					addExperience(p, 216);
				}
				break;

			case 6319://spirit mosquito
				req = 17;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = 6319;//proboscis
				shardsAmount = 1;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12778, 1);
					addExperience(p, 465);
				}
				break;

			case 1783://desert wyrm
				req = 18;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = 1783;//bucket of sand
				shardsAmount = 45;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12049, 1);
					addExperience(p, 312);
				}
				break;

			case 3095://Spirit scorpion
				req = 19;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = 3095;//bronze claws
				shardsAmount = 57;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12055, 1);
					addExperience(p, 832);
				}
				break;

			case 12168://spirit tz-kih
				req = 22;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = 3095;//obsidian charm
				shardsAmount = 64;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12808, 1);
					addExperience(p, 968);
				}
				break;

			case 2134://Albino rat
				req = 23;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = 2134;//raw rat meat
				shardsAmount = 75;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12067, 1);
					addExperience(p, 2024);
				}
				break;

			case 3138://spirit kalphite
				req = 25;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = 3138;//potato cactus
				shardsAmount = 51;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12063, 1);
					addExperience(p, 2200);
				}
				break;

			case 6032://Compost mound
				req = 28;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = 6032;//compost
				shardsAmount = 47;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12091, 1);
					addExperience(p, 498);
				}
				break;

			case 9976://giant chinchompa
				req = 29;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = 9976;//chinchompa
				shardsAmount = 84;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12800, 1);
					addExperience(p, 2552);
				}
				break;

			case 3325://vampire bat
				req = 31;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = 3325;//vampire dust
				shardsAmount = 81;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12053, 1);
					addExperience(p, 1360);
				}
				break;

			case 12156://honey badger
				req = 32;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = 12157;//honeycomb
				shardsAmount = 84;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12065, 1);
					addExperience(p, 1408);
				}
				break;

			case 1519://beaver
				req = 33;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;//willow logs
				shardsAmount = 72;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12021, 1);
					addExperience(p, 576);
				}
				break;

			case 12164:
				req = 34;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 74;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12818, 1);
					addExperience(p, 596);
				}
				break;

			case 12165:
				req = 34;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 74;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12814, 1);
					addExperience(p, 596);
				}
				break;

			case 12167:
				req = 34;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 74;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12798, 1);
					addExperience(p, 596);
				}
				break;

			case 2349://bronze minotaur
				req = 36;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;//bronze bar
				shardsAmount = 102;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12073, 1);
					addExperience(p, 3168);
				}
				break;

			case 6010:
				req = 40;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = itemUsed;
				shardsAmount = 11;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12087, 1);
					addExperience(p, 528);
				}
				break;

			case 249:
				req = 41;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 78;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12071, 1);
					addExperience(p, 724);
				}
				break;

			case 12153:
				req = 42;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 104;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12051, 1);
					addExperience(p, 1848);
				}
				break;

			case 2351:
				req = 46;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 125;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12075, 1);
					addExperience(p, 4048);
				}
				break;

			case 13403:
				req = 46;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 111;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12816, 1);
					addExperience(p, 2024);
				}
				break;

			case 1635:
				req = 47;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 88;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12041, 1);
					addExperience(p, 832);
				}
				break;

			case 2132:
				req = 49;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 117;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12061, 1);
					addExperience(p, 2152);
				}
				break;

			case 9978:
				req = 52;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = itemUsed;
				shardsAmount = 12;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12007, 1);
					addExperience(p, 684);
				}
				break;

			case 12161:
				req = 54;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 106;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12036, 1);
					addExperience(p, 948);
				}
				break;

			case 1937:
				req = 55;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 151;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12027, 1);
					addExperience(p, 484);
				}
				break;

			case 2353:
				req = 56;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 141;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12077, 1);
					addExperience(p, 4928);
				}
				break;

			case 311:
				req = 56;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 109;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12531, 1);
					addExperience(p, 988);
				}

				break;

			case 10099:
				req = 57;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 154;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12810, 1);
					addExperience(p, 5016);
				}
				break;

			case 10103:
				req = 57;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 153;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12812, 1);
					addExperience(p, 5016);
				}
				break;

			case 10095:
				req = 57;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 155;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12784, 1);
					addExperience(p, 5016);
				}
				break;

			case 9736:
				req = 58;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 141;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12805, 1);
					addExperience(p, 2680);
				}
				break;

			case 7801:
				req = 63;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 116;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12015, 1);
					addExperience(p, 1096);
				}
				break;

			case 8431:// stranger plant
				req = 64;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 128;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12045, 1);
					addExperience(p, 2816);
				}
				break;

			case 2359:// stranger plant
				req = 66;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 152;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12079, 1);
					addExperience(p, 5808);
				}
				break;

			case 2150:// stranger plant
				req = 66;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = itemUsed;
				shardsAmount = 11;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12123, 1);
					addExperience(p, 870);
				}
				break;

			case 7939:// stranger plant
				req = 67;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = itemUsed;
				shardsAmount = 1;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12031, 1);
					addExperience(p, 586);
				}
				break;

			case 383:// stranger plant
				req = 68;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 110;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12029, 1);
					addExperience(p, 1192);
				}
				break;

			case 1963:// stranger plant
				req = 69;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 130;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12033, 1);
					addExperience(p, 1212);
				}
				break;

			case 1933:// stranger plant
				req = 70;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 79;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12820, 1);
					addExperience(p, 1320);
				}
				break;

			case 10117:// stranger plant
				req = 71;
				useWith = POUCH;
				charmId = GOLDCHARM;
				itemId = itemUsed;
				shardsAmount = 14;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12057, 1);
					addExperience(p, 9320);
				}
				break;

			case 14616:// stranger plant
				req = 72;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 165;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 14623, 1);
					addExperience(p, 3018);
				}
				break;

			case 4188:// changed
				req = 73;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 195;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12792, 1);
					addExperience(p, 6424);
				}
				break;

			case 6979:// changed
				req = 74;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 166;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12069, 1);
					addExperience(p, 3256);
				}
				break;

			case 2460:// changed
				req = 75;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 168;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12011, 1);
					addExperience(p, 3296);
				}
				break;

			case 2361:// changed
				req = 75;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 144;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12081, 1);
					addExperience(p, 6688);
				}
				break;

			case 10020:// changed
				req = 76;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 141;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12782, 1);
					addExperience(p, 1340);
				}
				break;

			case 12162: // changed
				req = 77;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 174;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12794, 1);
					addExperience(p, 10152);
				}
				break;

			case 5933: // changed
				req = 78;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 124;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12013, 1);
					addExperience(p, 1368);
				}
				break;

			case 1442: // changed
				req = 79;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 198;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12802, 1);
					addExperience(p, 6952);
				}
				break;

			case 1438: // changed
				req = 79;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 198;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12806, 1);
					addExperience(p, 6952);
				}
				break;

			case 1440: // changed
				req = 79;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 202;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12804, 1);
					addExperience(p, 6952);
				}
				break;

			case 571: // changed
				req = 80;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 128;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12025, 1);
					addExperience(p, 1408);
				}
				break;

			case 6155: // changed
				req = 83;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 1;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12017, 1);
					addExperience(p, 3648);
				}
				break;

			case 4699: // changed lava rune
				req = 83;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 219;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12788, 1);
					addExperience(p, 7568);
				}
				break;

			case 10149: // changed
				req = 85;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 150;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12776, 1);
					addExperience(p, 3736);
				}
				break;

			case 2363: // changed
				req = 86;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 1;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12083, 1);
					addExperience(p, 7568);
				}
				break;

			case 1486: // changed
				req = 88;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 140;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12039, 1);
					addExperience(p, 1544);
				}
				break;

			case 1444: // changed
				req = 89;
				useWith = POUCH;
				charmId = BLUECHARM;
				itemId = itemUsed;
				shardsAmount = 222;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12786, 1);
					addExperience(p, 7832);
				}
				break;

			case 3228: // changed
				req = 92;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 203;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12089, 1);
					addExperience(p, 4048);
				}
				break;

			case 7979: // changed abyss head
				req = 93;
				useWith = POUCH;
				charmId = GREENCHARM;
				itemId = itemUsed;
				shardsAmount = 113;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12796, 1);
					addExperience(p, 1632);
				}
				break;

			case 1115: // changed
				req = 95;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 198;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12822, 1);
					addExperience(p, 4172);
				}
				break;

			case 10818: // changed
				req = 96;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 211;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12093, 1);
					addExperience(p, 4224);
				}
				break;

			case 1119: // changed
				req = 99;
				useWith = POUCH;
				charmId = CRIMSONCHARM;
				itemId = itemUsed;
				shardsAmount = 178;
				if(hasItems(p, charmId, itemId, shardsAmount, req)) {
					ContentEntity.addItem(p, 12790, 1);
					addExperience(p, 4352);
				}
				break;

		}
	}

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c,
	                           int slot) {
		if(type == 13) {
			ItemonItem(player, a, c);
		}
		if(type == 17) {
			SummoningMonsters.SummonNewNPC(player, SummoningData.getNpcbyPouchId(a), a);
		}
		if(type == 11) {
			Entity npc = World.getNpcs().get(slot);
			if(npc != null && ((NPC) npc).ownerId == player.getIndex()) {
				BoB.openInventory(player);
			} else {
				player.getActionSender().sendMessage("This is not mine.");
			}
		}
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
	}

	@Override
	public int[] getValues(int type) {
		//return null;

		if(type == 13) {
			int[] items = {2138, 2859, 6291, 3369, 440, 6319, 1783, 3095,
					12168, 2134, 3138, 6032, 9976, 3325, 12156, 1519, 12164,
					12165, 12167, 2349, 6010, 12153, 2351, 13403, 1635,
					2132, 9978, 12161, 1937, 2353, 311, 10099, 10103, 10095,
					9736, 7801, 8431, 2359, 2150, 7939, 383, 1963, 1933, 10117,
					14616, 4188, 6979, 2460, 2361, 10020, 12162, 5933, 1442,
					1438, 1440, 571, 6155, 4699, 10149, 2363, 237, 1444, 3228,
					7979, 1115, 10818, 1119};
			return items;
		}
		if(type == 17) {
			int[] clickitem = {12047, 12043, 12059, 12019, 12009, 12778, 12049, 12055, 12808, 12067,
					12091, 12800, 12053, 12065, 12021, 12818, 12780, 12814, 12798, 12073,
					12087, 12071, 12051, 12095, 12099, 12097, 12101, 12103, 12105, 12107,
					12816, 12075, 12041, 12061, 12007, 12035, 12027, 12077, 12531, 12810,
					12812, 12784, 12023, 12085, 12037, 12015, 12045, 12079, 12123, 12031,
					12029, 12033, 12820, 12057, 14623, 12792, 12069, 12011, 12081, 12782,
					12794, 12013, 12802, 12804, 12806, 12025, 12017, 12788, 12776, 12083,
					12039, 12786, 12089, 12796, 12822, 12093, 12790, 17989, 17988, 17987, 17986, 17985};
			return clickitem;
		}
		if(type == 11) {
			return mergeWithplusplusArray(BoBids);
		}

		return null;
	}

	private static int[] mergeWithplusplusArray(int[] arr) {
		int[] newarr = new int[arr.length * 2];
		int index = 0;
		for(int id : arr) {
			newarr[index++] = id;
			newarr[index++] = id - 1;
		}
		return newarr;
	}

	public final static int[] BoBids = {6815, 6807, 6868, 6795, 6816, 6874, 7350, 6822, 6820,6873};

	public static boolean isBoB(int npcId) {
		for(int i : mergeWithplusplusArray(BoBids))
			if(i == npcId)
				return true;
		return false;
	}

}