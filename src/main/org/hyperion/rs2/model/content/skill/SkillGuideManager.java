package org.hyperion.rs2.model.content.skill;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

public class SkillGuideManager implements ContentTemplate {


	@Override
	public void init() throws FileNotFoundException {

	}

	@Override
	public int[] getValues(int type) {
		if(type == 0) {
			int[] j = {8846, 8823, 8824, 8827, 8837, 8840, 8843, 8859, 8862, 8865, 15303, 15306, 15309, 8654, 8657, 8660, 8663, 8666, 8669, 8672, 8655, 8658, 8661, 8664, 8667, 8670, 12162, 8656, 8659, 8662, 8665, 8668, 8671, 13928,};
			return j;
		}
		return null;
	}


	@Override
	public boolean clickObject(final Player client, final int type, final int buttonId, final int slot, final int objId, final int a) {
		for(int i = 0; i < miniHeadingButtonIds.length; i++) {
			if(buttonId == miniHeadingButtonIds[i]) {
				openSkillInterface(client, client.skillMenuId, i, true);
				return true;
			}
		}
		switch(buttonId) {
			case 8654:
			case 8669:
			case 8660:
			case 8657:
			case 8655:
			case 8663:
				client.skillMenuId = 0;
				break;
			case 8666:
				client.skillMenuId = 5;
				break;
			case 8665:
				client.skillMenuId = 7;
				break;
			case 8671:
				client.skillMenuId = 8;
				break;
			case 8670:
				client.skillMenuId = 9;
				break;
			case 8662:
				client.skillMenuId = 10;
				break;
			case 8668:
				client.skillMenuId = 11;
				break;
			case 8667:
				client.skillMenuId = 12;
				break;
			case 8659:
				client.skillMenuId = 13;
				break;
			case 8656:
				client.skillMenuId = 14;
				break;
			case 8661:
				client.skillMenuId = 15;
				break;
			case 8658:
				client.skillMenuId = 16;
				break;
			case 8664:
				client.skillMenuId = 17;
				break;
			case 12162:
				client.skillMenuId = 18;
				break;
			case 13928:
				client.skillMenuId = 19;
				break;
			case 8672:
				client.skillMenuId = 20;
				break;
		}
		//openSkillInterface(client, client.skillMenuId, 0, false);
		return true;
	}

	//advancement string is 8760-8799
	//level one is 8720-8759

	public static final int[] miniHeadingButtonIds = {8846, 8823, 8824, 8827, 8837, 8840, 8843, 8859, 8862, 8865, 15303, 15306, 15309,};
	public static final int[] mainHeadingIds = {8846, 8823, 8824, 8827, 8837, 8840, 8843, 8859, 8862, 8865, 15303, 15306, 15309,};
	public static final String[][] mainSkillHeading = {
			{"Combat", "Attack", "Strength", "Defence", "Hitpoints", "Range", "Magic"},//attack
			{"Combat", "Attack", "Strength", "Defence", "Hitpoints", "Range", "Magic"},//defence
			{"Combat", "Attack", "Strength", "Defence", "Hitpoints", "Range", "Magic"},//strength
			{"Combat", "Attack", "Strength", "Defence", "Hitpoints", "Range", "Magic"},//hitpoints
			{"Combat", "Attack", "Strength", "Defence", "Hitpoints", "Range", "Magic"},//range
			{"Prayer", "Bones",},//prayer
			{"Combat", "Attack", "Strength", "Defence", "Hitpoints", "Range", "Magic"},//magic
			{"Cooking", "Fish",},//cooking
			{"WoodCutting", "Trees",},//woodcutting
			{"Fletching", "",},//Fletching
			{"Fishing", "Fish",},//fishing
			{"Firemaking", "Logs",},//firemaking
			{"Crafting", "Leather",},//crafting
			{"Smithing", "Smelting", "Bronze", "Iron", "Steel", "Mithril", "Adamant", "Rune",},//smithing
			{"Mining", "Ores",},//mining
			{"Herblore", "Herbs", "Potions"},//herblore
			{"Agility", "Courses", "Obstacles",},//agility
			{"Thieving", "Stalls", "NPC's", "Chests"},//thieving
			{"Slayer", "Skill Masters", "NPC's"},//slayer
			{"Farming", "Vegtables", "Trees", "Fruit Tree's", "Bushes", "Hops", "Flowers", "Herbs"},//farming
			{"Runecrafting", "Runes", "Amounts"},//runecrafting
	};

	public void sendInfo(Player player, int level, int itemId, String string, int index, Item[] items, int[] slots) {
		player.getActionSender().sendString(8720 + index, "" + level);
		player.getActionSender().sendString(8760 + index, string);
		slots[index] = index;
		items[index] = new Item(itemId, 1);
	}

	public void finishInfoInterface(Player player, int index, Item[] items, int[] slots) {
		Item item = new Item(- 1, 0);
		for(int i = index; i < 40; i++) {
			player.getActionSender().sendString(8720 + i, "");
			player.getActionSender().sendString(8760 + i, "");
			slots[i] = i;
			items[i] = item;
		}
	}

	public void openSkillInterface(Player player, int skill, int segment, boolean refresh) {
		player.getActionSender().sendString(8716, "" + mainSkillHeading[skill][0]);
		player.getActionSender().sendString(8849, "");
		int firstLength = mainSkillHeading[skill].length;
		for(int i = 1; i < firstLength; i++) {
			player.getActionSender().sendString(mainHeadingIds[(i - 1)], mainSkillHeading[skill][i]);
		}
		firstLength--;
		for(int i = firstLength; i < mainHeadingIds.length; i++) {
			player.getActionSender().sendString(mainHeadingIds[i], "");
		}
		if(segment > mainSkillHeading[skill].length - 1)
			segment = 0;
		int i = 0;
		Item[] items = new Item[40];
		int[] addedSlots = new int[40];
		int switchId = (skill * 10) + segment;
		//player.getActionSender().sendMessage("skillMenuId: "+switchId);
		switch(switchId) {
			case 0://combat menu, attack screen
				sendInfo(player, 1, 1321, "Bronze", i, items, addedSlots);
				i++;
				sendInfo(player, 1, 1323, "Iron", i, items, addedSlots);
				i++;
				sendInfo(player, 5, 1325, "Steel", i, items, addedSlots);
				i++;
				sendInfo(player, 10, 1327, "Black", i, items, addedSlots);
				i++;
				sendInfo(player, 20, 1329, "Mithril", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 1331, "Adamant", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 1333, "Rune", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 4587, "Dragon", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 6528, "Tzhaar", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 4718, "Barrows", i, items, addedSlots);
				i++;
				sendInfo(player, 75, 11696, "Godswords", i, items, addedSlots);
				i++;
				break;
			case 1://combat menu, SStrength screen
				sendInfo(player, 50, 3122, "Granite armour", i, items, addedSlots);
				i++;
				sendInfo(player, 50, 4153, "Granite maul", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 6528, "Tzhaar", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 4718, "Barrows melee", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 11696, "Bandos's Stronghold of the God Wars Dungeon", i, items, addedSlots);
				i++;
				break;
			case 2://combat menu, def screen
				sendInfo(player, 1, 1139, "Bronze", i, items, addedSlots);
				i++;
				sendInfo(player, 1, 1137, "Iron", i, items, addedSlots);
				i++;
				sendInfo(player, 5, 1141, "Steel", i, items, addedSlots);
				i++;
				sendInfo(player, 10, 1151, "Black", i, items, addedSlots);
				i++;
				sendInfo(player, 10, 6621, "White", i, items, addedSlots);
				i++;
				//sendInfo(player,10,FSH,"Full Slayer helmet",i,items,addedSlots);i++;
				sendInfo(player, 20, 1143, "Mithril", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 1145, "Adamant", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 1147, "Rune", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 10551, "Fighter torso", i, items, addedSlots);
				i++;
				sendInfo(player, 50, 3122, "Granite", i, items, addedSlots);
				i++;
				sendInfo(player, 55, 10828, "Helm of Neitiznot", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 1149, "Dragon", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 6524, "Tzhaar", i, items, addedSlots);
				i++;
				sendInfo(player, 65, 10350, "Third-Age", i, items, addedSlots);
				i++;
				sendInfo(player, 65, 11724, "Bandos", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 4716, "Barrows", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 4224, "Crystal shield", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 10720, "Armadyl", i, items, addedSlots);
				i++;
				//sendInfo(player,75,11696,"Godswords",  i,items,addedSlots);i++;
				sendInfo(player, 75, 11283, "Dragonfire shield", i, items, addedSlots);
				i++;
				break;
			case 3://combat menu, hit screen

				break;
			case 4://combat menu, range screen

				break;
			case 5://combat menu, magic screen

				break;
			case 70://cooking
				sendInfo(player, 1, 315, "Shrimp", i, items, addedSlots);
				i++;
				sendInfo(player, 1, 325, "Sardine", i, items, addedSlots);
				i++;
				sendInfo(player, 1, 319, "Anchovies", i, items, addedSlots);
				i++;
				sendInfo(player, 5, 347, "Herring", i, items, addedSlots);
				i++;
				sendInfo(player, 15, 333, "Trout", i, items, addedSlots);
				i++;
				sendInfo(player, 25, 329, "Salmon", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 361, "Tuna", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 379, "Lobster", i, items, addedSlots);
				i++;
				sendInfo(player, 45, 373, "Swordfish", i, items, addedSlots);
				i++;
				sendInfo(player, 62, 7946, "Monkfish", i, items, addedSlots);
				i++;
				sendInfo(player, 80, 385, "Shark", i, items, addedSlots);
				i++;
				sendInfo(player, 91, 391, "Manta ray", i, items, addedSlots);
				i++;
				break;
			case 130://smithing
				sendInfo(player, 1, 2349, "Bronze bar (1 Tin ore & 1 copper ore)", i, items, addedSlots);
				i++;
				sendInfo(player, 15, 2351, "Iron bar (1 iron ore)", i, items, addedSlots);
				i++;
				sendInfo(player, 20, 2355, "Silver bar", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 2353, "Steel bar (1 Iron ore & 2coal)", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 2357, "Gold bar", i, items, addedSlots);
				i++;
				sendInfo(player, 50, 2359, "Mithril bar (1 Mithirl ore & 4coal)", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 2361, "Adamantite bar (1 Adamantite ore & 6coal)", i, items, addedSlots);
				i++;
				sendInfo(player, 85, 2363, "Runite bar (1 Runeite ore & 8coal)", i, items, addedSlots);
				i++;
				break;
			case 100://fishing
				sendInfo(player, 1, 317, "Shrimp (Net fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 5, 327, "Sardine (Bait fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 10, 345, "Herring (Bait fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 15, 321, "Anchovies (Net fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 20, 335, "Trout (Fly fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 331, "Salmon (Fly fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 35, 359, "Tuna (Harpoon fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 377, "Lobster (Cage fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 50, 371, "Swordfish (Harpoon fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 62, 7944, "Monkfish (Net fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 76, 383, "Shark (Harpoon fishing)", i, items, addedSlots);
				i++;
				sendInfo(player, 81, 389, "Manta ray", i, items, addedSlots);
				i++;
				break;
			case 90://fletching
				sendInfo(player, 1, 52, "Arrow shafts", i, items, addedSlots);
				i++;
				sendInfo(player, 5, 50, "Shortbow", i, items, addedSlots);
				i++;
				sendInfo(player, 10, 48, "Longbow", i, items, addedSlots);
				i++;
				sendInfo(player, 20, 54, "Oak shortbow", i, items, addedSlots);
				i++;
				sendInfo(player, 25, 56, "Oak longbow", i, items, addedSlots);
				i++;
				sendInfo(player, 35, 60, "Willow Shortbow", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 58, "Willow longbow", i, items, addedSlots);
				i++;
				sendInfo(player, 50, 64, "Maple shortbow", i, items, addedSlots);
				i++;
				sendInfo(player, 55, 62, "Maple longbow", i, items, addedSlots);
				i++;
				sendInfo(player, 65, 68, "Yew shortbow", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 66, "Yew longbow", i, items, addedSlots);
				i++;
				sendInfo(player, 80, 72, "Magic shortbow", i, items, addedSlots);
				i++;
				sendInfo(player, 85, 70, "Magic longbow", i, items, addedSlots);
				i++;
				break;
			case 80://woodcutting
				sendInfo(player, 1, 1511, "Normal tree", i, items, addedSlots);
				i++;
				sendInfo(player, 15, 1521, "Oak tree", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 1519, "Willow tree", i, items, addedSlots);
				i++;
				sendInfo(player, 45, 1517, "Mapple tree", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 1515, "Yew tree", i, items, addedSlots);
				i++;
				sendInfo(player, 75, 1513, "Magic tree", i, items, addedSlots);
				i++;
				break;
			case 110://firemaking
				sendInfo(player, 1, 1511, "Normal log", i, items, addedSlots);
				i++;
				sendInfo(player, 15, 1521, "Oak log", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 1519, "Willow log", i, items, addedSlots);
				i++;
				sendInfo(player, 45, 1517, "Mapple log", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 1515, "Yew log", i, items, addedSlots);
				i++;
				sendInfo(player, 75, 1513, "Magic log", i, items, addedSlots);
				i++;
				break;
			case 140://mining
				sendInfo(player, 1, 438, "Tin", i, items, addedSlots);
				i++;
				sendInfo(player, 1, 436, "Copper", i, items, addedSlots);
				i++;
				sendInfo(player, 15, 440, "Iron", i, items, addedSlots);
				i++;
				sendInfo(player, 20, 442, "Silver", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 1436, "Rune essence", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 453, "Coal", i, items, addedSlots);
				i++;
				sendInfo(player, 40, 444, "Gold", i, items, addedSlots);
				i++;
				sendInfo(player, 55, 447, "Mithril", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 449, "Adamantite", i, items, addedSlots);
				i++;
				sendInfo(player, 85, 451, "Runite", i, items, addedSlots);
				i++;
				break;
			case 200://runecrafting
				sendInfo(player, 1, 556, "Air runes", i, items, addedSlots);
				i++;
				sendInfo(player, 2, 558, "Mind runes", i, items, addedSlots);
				i++;
				sendInfo(player, 5, 555, "Water runes", i, items, addedSlots);
				i++;
				sendInfo(player, 9, 557, "Earth runes", i, items, addedSlots);
				i++;
				sendInfo(player, 14, 554, "Fire runes", i, items, addedSlots);
				i++;
				sendInfo(player, 20, 559, "Body runes", i, items, addedSlots);
				i++;
				sendInfo(player, 27, 564, "Cosmic runes", i, items, addedSlots);
				i++;
				sendInfo(player, 35, 562, "Chaos runes", i, items, addedSlots);
				i++;
				sendInfo(player, 44, 561, "Nature runes", i, items, addedSlots);
				i++;
				sendInfo(player, 54, 563, "Law runes", i, items, addedSlots);
				i++;
				sendInfo(player, 65, 560, "Death runes", i, items, addedSlots);
				i++;
				sendInfo(player, 77, 565, "Blood runes", i, items, addedSlots);
				i++;
				break;
			case 201://runecrafting
				sendInfo(player, 11, 556, "2 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 14, 558, "2 Mind runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 19, 555, "2 Water runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 22, 556, "3 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 26, 557, "2 Earth runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 28, 558, "3 Mind runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 33, 556, "4 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 35, 554, "2 Fire runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 38, 555, "3 Water runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 42, 558, "4 Mind runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 44, 556, "5 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 46, 559, "2 Body runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 52, 557, "3 Earth runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 55, 556, "6 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 56, 558, "5 Mind runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 57, 555, "4 Water runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 59, 564, "2 Cosmic runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 66, 556, "7 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 554, "3 Fire runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 70, 558, "6 Mind runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 74, 562, "2 Chaos runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 76, 555, "5 Water runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 77, 556, "8 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 78, 557, "4 Earth runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 84, 558, "7 Mind runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 88, 556, "9 Air runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 91, 561, "2 Nature runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 92, 559, "3 Body runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 95, 555, "6 Water runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 98, 558, "8 Mind runes per essence", i, items, addedSlots);
				i++;
				sendInfo(player, 99, 556, "10 Air runes per essence", i, items, addedSlots);
				i++;
				break;
			case 190://farming
				sendInfo(player, 1, 5318, "Potatoes", i, items, addedSlots);
				i++;
				sendInfo(player, 5, 5319, "Onions", i, items, addedSlots);
				i++;
				sendInfo(player, 7, 5324, "Cabbage", i, items, addedSlots);
				i++;
				sendInfo(player, 12, 7562, "Tomato", i, items, addedSlots);
				i++;
				sendInfo(player, 20, 5320, "Sweetcorn", i, items, addedSlots);
				i++;
				sendInfo(player, 31, 5323, "Strawberries", i, items, addedSlots);
				i++;
				sendInfo(player, 47, 5321, "Watermelon", i, items, addedSlots);
				i++;
				break;
			case 196://farming
				sendInfo(player, 9, 5291, "Guam", i, items, addedSlots);
				i++;
				sendInfo(player, 14, 5292, "Marrentill", i, items, addedSlots);
				i++;
				sendInfo(player, 19, 5293, "Tarromin", i, items, addedSlots);
				i++;
				sendInfo(player, 26, 5294, "Harralander", i, items, addedSlots);
				i++;
				sendInfo(player, 32, 5295, "Ranarr", i, items, addedSlots);
				i++;
				sendInfo(player, 38, 5296, "Toadflax", i, items, addedSlots);
				i++;
				sendInfo(player, 44, 5297, "Irit", i, items, addedSlots);
				i++;
				sendInfo(player, 50, 5298, "Avantoe", i, items, addedSlots);
				i++;
				sendInfo(player, 56, 5299, "Kwuarm", i, items, addedSlots);
				i++;
				sendInfo(player, 62, 5300, "Snapdragon", i, items, addedSlots);
				i++;
				sendInfo(player, 67, 5301, "Cadantine", i, items, addedSlots);
				i++;
				sendInfo(player, 73, 5302, "Lantadyme", i, items, addedSlots);
				i++;
				sendInfo(player, 79, 5303, "Dwarf weed", i, items, addedSlots);
				i++;
				sendInfo(player, 85, 5304, "Torstol", i, items, addedSlots);
				i++;
				break;
			case 191://farming
				sendInfo(player, 15, 5312, "Acorn (Oak tree)", i, items, addedSlots);
				i++;
				sendInfo(player, 30, 5313, "Willow tree", i, items, addedSlots);
				i++;
				sendInfo(player, 45, 5314, "Maple tree", i, items, addedSlots);
				i++;
				sendInfo(player, 60, 5315, "Yew tree", i, items, addedSlots);
				i++;
				sendInfo(player, 75, 5316, "Magic tree", i, items, addedSlots);
				i++;
				break;
			case 192://farming
				sendInfo(player, 27, 5283, "Apple tree", i, items, addedSlots);
				i++;
				sendInfo(player, 33, 5284, "Banana tree", i, items, addedSlots);
				i++;
				sendInfo(player, 39, 5285, "Orange tree", i, items, addedSlots);
				i++;
				sendInfo(player, 42, 5286, "Curry tree", i, items, addedSlots);
				i++;
				sendInfo(player, 51, 5287, "Pineapple tree", i, items, addedSlots);
				i++;
				sendInfo(player, 57, 5288, "Papaya tree", i, items, addedSlots);
				i++;
				sendInfo(player, 68, 5289, "Palm tree", i, items, addedSlots);
				i++;
				break;
		}
		finishInfoInterface(player, i, items, addedSlots);
		player.getActionSender().sendUpdateItems(8847, addedSlots, items);
		if(! refresh)
			player.getActionSender().showInterface(8714);
	}

}
