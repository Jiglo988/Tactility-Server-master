package org.hyperion.rs2.model.content.skill;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

public class Smithing implements ContentTemplate {

	public Smithing() {
	}

	private static final int EXPMULTIPLIER = Constants.XPRATE / 2;

	// playerLevel[13]
	// item, amount, level, bar, sqamountbar, sq
	public final int SMITHING_FRAME[][][] = {
	        /* Bronze */{{1205, 1, 1, 1, 1125, 1094},
			{1351, 1, 1, 1, 1126, 1091},
			{1422, 1, 2, 1, 1129, 1093},
			{1139, 1, 3, 1, 1127, 1102},
			{1277, 1, 3, 1, 1128, 1085},
			{819, 10, 4, 1, 1124, 1107},
			{4819, 15, 4, 1, 13357, 13358},
			{39, 15, 5, 1, 1130, 1108},
			{1321, 1, 5, 2, 1116, 1087},
			{1291, 1, 6, 2, 1089, 1086},
			{1155, 1, 7, 2, 1113, 1103},
			{864, 5, 7, 1, 1131, 1106},
			{1173, 1, 8, 2, 1114, 1104},
			{1337, 1, 9, 3, 1118, 1083},
			{1375, 1, 10, 3, 1095, 1092},
			{1103, 1, 11, 3, 1109, 1098},
			{1189, 1, 12, 3, 1115, 1105},
			{3095, 1, 13, 2, 8428, 8429},
			{1307, 1, 14, 3, 1090, 1088},
			{1087, 1, 16, 3, 1111, 1100},
			{1075, 1, 16, 3, 1110, 1099},
			{1117, 1, 18, 5, 1112, 1101},/* Specials */
			{1794, 1, 4, 1, 1132, 1096}},
            /* Iron */{{1203, 1, 15, 1, 1125, 1094},
			{1349, 1, 16, 1, 1126, 1091},
			{1420, 1, 17, 1, 1129, 1093},
			{1137, 1, 18, 1, 1127, 1102},
			{1279, 1, 19, 1, 1128, 1085},
			{820, 10, 19, 1, 1124, 1107},
			{4820, 15, 19, 1, 13357, 13358},
			{40, 15, 20, 1, 1130, 1108},
			{1323, 1, 20, 2, 1116, 1087},
			{1293, 1, 21, 2, 1089, 1086},
			{1153, 1, 22, 2, 1113, 1103},
			{863, 5, 22, 1, 1131, 1106},
			{1175, 1, 23, 2, 1114, 1104},
			{1335, 1, 24, 3, 1118, 1083},
			{1363, 1, 25, 3, 1095, 1092},
			{1101, 1, 26, 3, 1109, 1098},
			{1191, 1, 27, 3, 1115, 1105},
			{3096, 1, 28, 2, 8428, 8429},
			{1309, 1, 29, 3, 1090, 1088},
			{1081, 1, 31, 3, 1111, 1100},
			{1067, 1, 31, 3, 1110, 1099},
			{1115, 1, 33, 5, 1112, 1101},/* Specials */
			{4540, 1, 26, 1, 11459, 11461}},
			/* Steel */{{1207, 1, 30, 1, 1125, 1094},
			{1353, 1, 31, 1, 1126, 1091},
			{1424, 1, 32, 1, 1129, 1093},
			{1141, 1, 33, 1, 1127, 1102},
			{1281, 1, 34, 1, 1128, 1085},
			{821, 10, 34, 1, 1124, 1107},
			{1539, 15, 34, 1, 13357, 13358},
			{41, 15, 35, 1, 1130, 1108},
			{1325, 1, 35, 2, 1116, 1087},
			{1295, 1, 36, 2, 1089, 1086},
			{1157, 1, 37, 2, 1113, 1103},
			{865, 5, 37, 1, 1131, 1106},
			{1177, 1, 38, 2, 1114, 1104},
			{1339, 1, 39, 3, 1118, 1083},
			{1365, 1, 40, 3, 1095, 1092},
			{1105, 1, 41, 3, 1109, 1098},
			{1193, 1, 42, 3, 1115, 1105},
			{3097, 1, 43, 2, 8428, 8429},
			{1311, 1, 44, 3, 1090, 1088},
			{1083, 1, 46, 3, 1111, 1100},
			{1069, 1, 46, 3, 1110, 1099},
			{1119, 1, 48, 5, 1112, 1101},/* Specials */
			{4544, 1, 49, 1, 11459, 11461},
			{2370, 1, 36, 1, 1135, 1134}},
			/* Mithril */{{1209, 1, 50, 1, 1125, 1094},
			{1355, 1, 51, 1, 1126, 1091},
			{1428, 1, 52, 1, 1129, 1093},
			{1143, 1, 53, 1, 1127, 1102},
			{1285, 1, 53, 1, 1128, 1085},
			{822, 10, 54, 1, 1124, 1107},
			{4822, 15, 54, 1, 13357, 13358},
			{42, 15, 55, 1, 1130, 1108},
			{1329, 1, 55, 2, 1116, 1087},
			{1299, 1, 56, 2, 1089, 1086},
			{1159, 1, 57, 2, 1113, 1103},
			{866, 5, 57, 1, 1131, 1106},
			{1181, 1, 58, 2, 1114, 1104},
			{1343, 1, 59, 3, 1118, 1083},
			{1369, 1, 60, 3, 1095, 1092},
			{1109, 1, 61, 3, 1109, 1098},
			{1197, 1, 62, 3, 1115, 1105},
			{3099, 1, 63, 2, 8428, 8429},
			{1315, 1, 64, 3, 1090, 1088},
			{1085, 1, 66, 3, 1111, 1100},
			{1071, 1, 66, 3, 1110, 1099},
			{1121, 1, 68, 5, 1112, 1101}},
			/* Adamant */{{1211, 1, 70, 1, 1125, 1094},
			{1357, 1, 71, 1, 1126, 1091},
			{1430, 1, 72, 1, 1129, 1093},
			{1145, 1, 73, 1, 1127, 1102},
			{1287, 1, 74, 1, 1128, 1085},
			{823, 10, 74, 1, 1124, 1107},
			{4823, 15, 74, 1, 13357, 13358},
			{43, 15, 75, 1, 1130, 1108},
			{1331, 1, 75, 2, 1116, 1087},
			{1301, 1, 76, 2, 1089, 1086},
			{1161, 1, 77, 2, 1113, 1103},
			{867, 5, 77, 1, 1131, 1106},
			{1183, 1, 78, 2, 1114, 1104},
			{1345, 1, 79, 3, 1118, 1083},
			{1371, 1, 80, 3, 1095, 1092},
			{1111, 1, 81, 3, 1109, 1098},
			{1199, 1, 82, 3, 1115, 1105},
			{3100, 1, 83, 2, 8428, 8429},
			{1317, 1, 84, 3, 1090, 1088},
			{1091, 1, 86, 3, 1111, 1100},
			{1073, 1, 86, 3, 1110, 1099},
			{1123, 1, 88, 5, 1112, 1101}},
			/* Rune */{{1213, 1, 85, 1, 1125, 1094},
			{1359, 1, 86, 1, 1126, 1091},
			{1432, 1, 87, 1, 1129, 1093},
			{1147, 1, 88, 1, 1127, 1102},
			{1289, 1, 89, 1, 1128, 1085},
			{824, 10, 89, 1, 1124, 1107},
			{4824, 15, 89, 1, 13357, 13358},
			{44, 15, 90, 1, 1130, 1108},
			{1333, 1, 90, 2, 1116, 1087},
			{1303, 1, 91, 2, 1089, 1086},
			{1163, 1, 92, 2, 1113, 1103},
			{868, 5, 92, 1, 1131, 1106},
			{1185, 1, 93, 2, 1114, 1104},
			{1347, 1, 94, 3, 1118, 1083},
			{1373, 1, 95, 3, 1095, 1092},
			{1113, 1, 96, 3, 1109, 1098},
			{1201, 1, 97, 3, 1115, 1105},
			{3101, 1, 98, 2, 8428, 8429},
			{1319, 1, 99, 3, 1090, 1088},
			{1093, 1, 99, 3, 1111, 1100},
			{1079, 1, 99, 3, 1110, 1099},
			{1127, 1, 99, 5, 1112, 1101}}
			// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
			// dagger axe mace medium sword dart tips nails arrow heads scimitar long
			// sword full helmet knives square warhammer battle axe chain kite claws
			// 2-handed skirt legs body lantern/wire studs
	};

	public boolean smithItem(final Player client, final int itemId,
	                         final int slot, int interfaceId, final int amm2) {
		//System.out.println("running method1");
		if(client.isBusy())
			return false;
		interfaceId = getInterfaceId(interfaceId);
		if(interfaceId < 1119 && interfaceId > 1123) {
			return false;
		}
		final int type = client.smithingMenu;
		if(type < 0)
			return false;
		final int k = itemExists(itemId, type);
		// System.out.println("smithing slot: "+k+" itemId: "+itemId);
		if(k <= - 1)
			return false;

		/*
		 * if(itemId != SMITHING_FRAME[type][k][0]) return false;//player is
		 * hacking stop him!!!!!!!
		 */

		if(ContentEntity.getItemAmount(client, getBar(type)) < SMITHING_FRAME[type][k][3]) {
			ContentEntity.sendMessage(client, "You dont have enough bars.");
			return true;
		}
		if(ContentEntity.returnSkillLevel(client, 13) < SMITHING_FRAME[type][k][2]) {
			ContentEntity.sendMessage(client, "You need a smithing level of "
					+ SMITHING_FRAME[type][k][2] + " to make this item.");
			return true;
		}
		if(amm2 == 0 || itemId <= 0 || slot < 0)
			return true;
		ContentEntity.removeAllWindows(client);
		if(ContentEntity.getItemAmount(client, 2347) <= 0) {
			ContentEntity
					.sendMessage(client, "You need a hammer to make this.");
			return true;
		}
		ContentEntity.startAnimation(client, 898);
		World.submit(new Task(2500,"smithing2") {
			int amm = amm2;

			@Override
			public void execute() {
				if(client.getRandomEvent().skillAction(3)) {
					stop2();
					return;
				}
				if(amm == 0) {
					stop2();
					return;
				}
				if(ContentEntity.getItemAmount(client, getBar(type)) >= SMITHING_FRAME[type][k][3]) {
					client.setBusy(true);
					client.setCanWalk(false);
					if(amm > 1)
						ContentEntity.startAnimation(client, 898);
					ContentEntity.addSkillXP(client, SMITHING_FRAME[type][k][2]
							* 50 * EXPMULTIPLIER, Skills.SMITHING);
					/*
					 * World.submit(new Event(2000) {
					 * 
					 * @Override public void execute() {
					 */
					ContentEntity.deleteItemA(client, getBar(type),
							SMITHING_FRAME[type][k][3]);
					ContentEntity.addItem(client, itemId,
							SMITHING_FRAME[type][k][1]);
					client.getAchievementTracker().itemSkilled(Skills.SMITHING, itemId, SMITHING_FRAME[type][k][1]);
					/*
					 * stop3(); } public void stop3() { this.stop(); } });
					 */
					amm--;

				} else {
					ContentEntity.sendMessage(client,
							"you dont have enough bars");
					stop2();
					return;
				}
			}

			public void stop2() {
				client.setBusy(false);
				client.setCanWalk(true);
				client.smithingMenu = - 1;
				amm = 0;
				this.stop();
			}

		});
		return true;
	}

	public int getInterfaceId(int interfaceId) {
		switch(interfaceId) {
			case 57220:
				return 1119;
			case 57476:
				return 1120;
			case 57732:
				return 1121;
			case 57988:
				return 1122;
			case 58244:
				return 1123;
			case 24452:
				return 1119;
			case 24708:
				return 1120;
			case 24964:
				return 1121;
			case 25220:
				return 1122;
			case 25476:
				return 1123;

		}
		return interfaceId;
	}

	public int itemExists(int id, int menu) {
		for(int i = 0; i < SMITHING_FRAME[menu].length; i++) {
			if(id == SMITHING_FRAME[menu][i][0]) {
				return i;
			}
		}
		return - 1;
	}

	public final int SMITHING_ITEMS[][] = new int[5][5];

	public String BAR = "bar";

	public String ENOUGH_BARS = "@red@", AVAILABLE = "@bla@";

	/**
	 * Gets the type of the bar.
	 *
	 * @param bar The bar we used on the anvil.
	 */

	public int getType(int bar) {
		switch(bar) {
			case 2349: // Bronze Bar
				return 1;
			case 2351: // Iron Bar
				return 2;
			case 2353: // Steel Bar
				return 3;
			case 2359: // Mithril Bar
				return 4;
			case 2361: // Adamantite Bar
				return 5;
			case 2363: // Runite Bar
				return 6;
		}
		return - 1;
	}

	public int getBar(int bar) {
		switch(bar) {
			case 0:
				return 2349;
			case 1:
				return 2351;
			case 2:
				return 2353;
			case 3:
				return 2359;
			case 4:
				return 2361;
			case 5:
				return 2363;
		}
		return - 1;
	}

	@SuppressWarnings("unused")
	private final int[] FIVE_BARS = {1112};
	@SuppressWarnings("unused")
	private final int[] THREE_BARS = {1090, 1095, 1109, 1110, 1111, 1115, 1118};
	@SuppressWarnings("unused")
	private final int[] TWO_BARS = {1089, 1113, 1114, 1116, 8428};
	@SuppressWarnings("unused")
	private final int[] ONE_BAR = {1124, 1125, 1126, 1127, 1128, 1129, 1130,
			1131, 11459, 13357};

	/**
	 * Opens the smithing frame for the specific bar.
	 *
	 * @param c    The {@link Player}.
	 * @param type The type of the bar.
	 */

	public void openSmithingFrame(Player c, int type) {
		int realType = type - 1;
		c.smithingMenu = realType;
		int length = 0;
		@SuppressWarnings("unused")
		String name = " ";

		if(ContentEntity.getItemAmount(c, 2347) <= 0) {
			ContentEntity.sendMessage(c, "You need a hammer to smith.");
			return;
		}

		switch(type) {
			case 1:
				name = "Bronze ";
				length = 23;
			case 2:
				name = "Iron ";
				length = 23;
			case 3:
				name = "Steel ";
				length = 24;
			case 4:
				name = "Mithril ";
				length = 22;
			case 5:
				name = "Adamant ";
				length = 22;
			case 6:
				name = "Rune ";
				length = 22;
		}

		// Sending amount of bars + make text green if lvl is highenough
		ContentEntity.sendString(c, "", 1132); // Wire
		ContentEntity.sendString(c, "", 1096);
		ContentEntity.sendString(c, "", 11459); // Lantern
		ContentEntity.sendString(c, "", 11461);
		ContentEntity.sendString(c, "", 1135); // Studs
		ContentEntity.sendString(c, "", 1134);

		for(int i = 0; i < length; i++) {
			BAR = "Bar";
			ENOUGH_BARS = "@red@";
			AVAILABLE = "@bla@";

			// Check if the item needs more then 1 bar.
			if(SMITHING_FRAME[realType][i][3] > 1)
				BAR = "bars";

			// Check if the player has an high enough level.
			if(ContentEntity.returnSkillLevel(c, 13) >= SMITHING_FRAME[realType][i][2])
				AVAILABLE = "@whi@";

			// Check if the player has enough bars for the item.
			if(ContentEntity.getItemAmount(c, getBar(realType)) >= SMITHING_FRAME[realType][i][3])
				ENOUGH_BARS = "@gre@";

			// Send the line of the bars.
			ContentEntity.sendString(c, ENOUGH_BARS + ""
					+ SMITHING_FRAME[realType][i][3] + "" + BAR,
					SMITHING_FRAME[realType][i][4]);

			// Get the item name.
			String itemName = ContentEntity
					.getItemName(SMITHING_FRAME[realType][i][0]);

			// Send the item name.
			int index = itemName.indexOf(" ");
			if(index > 0) {
				itemName = itemName.substring(index + 1);
				itemName = itemName.substring(0, 1).toUpperCase()
						+ itemName.substring(1);
				ContentEntity.sendString(c, AVAILABLE + "" + itemName,
						SMITHING_FRAME[realType][i][5]);
			}

		}

		SMITHING_ITEMS[0][0] = SMITHING_FRAME[realType][0][0]; // Dagger
		SMITHING_ITEMS[0][1] = SMITHING_FRAME[realType][0][1];
		SMITHING_ITEMS[1][0] = SMITHING_FRAME[realType][4][0]; // Sword
		SMITHING_ITEMS[1][1] = SMITHING_FRAME[realType][4][1];
		SMITHING_ITEMS[2][0] = SMITHING_FRAME[realType][8][0]; // Scimitar
		SMITHING_ITEMS[2][1] = SMITHING_FRAME[realType][8][1];
		SMITHING_ITEMS[3][0] = SMITHING_FRAME[realType][9][0]; // Long Sword
		SMITHING_ITEMS[3][1] = SMITHING_FRAME[realType][9][1];
		SMITHING_ITEMS[4][0] = SMITHING_FRAME[realType][18][0]; // 2 hand sword
		SMITHING_ITEMS[4][1] = SMITHING_FRAME[realType][18][1];
		ContentEntity.refreshSmithingScreen(c, 1119, SMITHING_ITEMS);
		SMITHING_ITEMS[0][0] = SMITHING_FRAME[realType][1][0]; // Axe
		SMITHING_ITEMS[0][1] = SMITHING_FRAME[realType][1][1];
		SMITHING_ITEMS[1][0] = SMITHING_FRAME[realType][2][0]; // Mace
		SMITHING_ITEMS[1][1] = SMITHING_FRAME[realType][2][1];
		SMITHING_ITEMS[2][0] = SMITHING_FRAME[realType][13][0]; // Warhammer
		SMITHING_ITEMS[2][1] = SMITHING_FRAME[realType][13][1];
		SMITHING_ITEMS[3][0] = SMITHING_FRAME[realType][14][0]; // Battle axe
		SMITHING_ITEMS[3][1] = SMITHING_FRAME[realType][14][1];
		SMITHING_ITEMS[4][0] = SMITHING_FRAME[realType][17][0]; // Claws
		SMITHING_ITEMS[4][1] = SMITHING_FRAME[realType][17][1];
		ContentEntity.refreshSmithingScreen(c, 1120, SMITHING_ITEMS);
		SMITHING_ITEMS[0][0] = SMITHING_FRAME[realType][15][0]; // Chain body
		SMITHING_ITEMS[0][1] = SMITHING_FRAME[realType][15][1];
		SMITHING_ITEMS[1][0] = SMITHING_FRAME[realType][20][0]; // Plate legs
		SMITHING_ITEMS[1][1] = SMITHING_FRAME[realType][20][1];
		SMITHING_ITEMS[2][0] = SMITHING_FRAME[realType][19][0]; // Plate skirt
		SMITHING_ITEMS[2][1] = SMITHING_FRAME[realType][19][1];
		SMITHING_ITEMS[3][0] = SMITHING_FRAME[realType][21][0]; // Plate body
		SMITHING_ITEMS[3][1] = SMITHING_FRAME[realType][21][1];
		SMITHING_ITEMS[4][0] = - 1; // Lantern
		SMITHING_ITEMS[4][1] = 0;
		if(type == 2 || type == 3) {
			if(SMITHING_FRAME[realType].length >= 23) {
				AVAILABLE = "@bla@";
				try {
					if(ContentEntity.returnSkillLevel(c, 13) >= SMITHING_FRAME[realType][22][2]) {
						AVAILABLE = "@whi@";
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				SMITHING_ITEMS[4][0] = SMITHING_FRAME[realType][22][0]; // Lantern
				SMITHING_ITEMS[4][1] = SMITHING_FRAME[realType][22][1];
				String itemName = ContentEntity.getItemName(SMITHING_FRAME[realType][22][0]);
				ContentEntity.sendString(c, AVAILABLE + "" + itemName, 11461);
			}
		}
		ContentEntity.refreshSmithingScreen(c, 1121, SMITHING_ITEMS);
		SMITHING_ITEMS[0][0] = SMITHING_FRAME[realType][3][0]; // Medium
		SMITHING_ITEMS[0][1] = SMITHING_FRAME[realType][3][1];
		SMITHING_ITEMS[1][0] = SMITHING_FRAME[realType][10][0]; // Full Helm
		SMITHING_ITEMS[1][1] = SMITHING_FRAME[realType][10][1];
		SMITHING_ITEMS[2][0] = SMITHING_FRAME[realType][12][0]; // Square
		SMITHING_ITEMS[2][1] = SMITHING_FRAME[realType][12][1];
		SMITHING_ITEMS[3][0] = SMITHING_FRAME[realType][16][0]; // Kite
		SMITHING_ITEMS[3][1] = SMITHING_FRAME[realType][16][1];
		SMITHING_ITEMS[4][0] = SMITHING_FRAME[realType][6][0]; // Nails
		SMITHING_ITEMS[4][1] = SMITHING_FRAME[realType][6][1];
		ContentEntity.refreshSmithingScreen(c, 1122, SMITHING_ITEMS);
		SMITHING_ITEMS[0][0] = SMITHING_FRAME[realType][5][0]; // Dart Tips
		SMITHING_ITEMS[0][1] = SMITHING_FRAME[realType][5][1];
		SMITHING_ITEMS[1][0] = SMITHING_FRAME[realType][7][0]; // Arrow Heads
		SMITHING_ITEMS[1][1] = SMITHING_FRAME[realType][7][1];
		SMITHING_ITEMS[2][0] = SMITHING_FRAME[realType][11][0]; // Knives
		SMITHING_ITEMS[2][1] = SMITHING_FRAME[realType][11][1];
		SMITHING_ITEMS[3][0] = - 1; // Wire
		SMITHING_ITEMS[3][1] = 0;
		if(type == 1) {
			AVAILABLE = "@bla@";
			if(ContentEntity.returnSkillLevel(c, 13) >= SMITHING_FRAME[realType][22][2]) {
				AVAILABLE = "@whi@";
			}
			SMITHING_ITEMS[3][0] = SMITHING_FRAME[realType][22][0]; // Wire
			SMITHING_ITEMS[3][1] = SMITHING_FRAME[realType][22][1];

			String itemName = ContentEntity
					.getItemName(SMITHING_FRAME[realType][22][0]);
			ContentEntity.sendString(c, AVAILABLE + "" + itemName, 1096);
		}
		SMITHING_ITEMS[4][0] = - 1; // Studs
		SMITHING_ITEMS[4][1] = 0;
		if(type == 3) {
			AVAILABLE = "@bla@";
			if(ContentEntity.returnSkillLevel(c, 13) >= SMITHING_FRAME[realType][23][2]) {
				AVAILABLE = "@whi@";
			}
			SMITHING_ITEMS[4][0] = SMITHING_FRAME[realType][23][0]; // Studs
			SMITHING_ITEMS[4][1] = SMITHING_FRAME[realType][23][1];
			String itemName = ContentEntity
					.getItemName(SMITHING_FRAME[realType][23][0]);
			ContentEntity.sendString(c, AVAILABLE + "" + itemName, 1134);
		}
		ContentEntity.refreshSmithingScreen(c, 1123, SMITHING_ITEMS);
		ContentEntity.showInterface(c, 994);

	}

	@Override
	public int[] getValues(int type) {
		if(type == 14) {
			int[] j = {2349, 2351, 2353, 2359, 2361, 2363, 11702, 11704,
					11706, 11708, 1540, 18350, 18352, 18354, 18356, 18358,
					18360};
			return j;
		}
		if(type == 2 || type == 3 || type == 4) {
			int[] j = new int[140];
			int index = 0;
			for(int i = 0; i < 6; i++) {
				for(int i2 = 0; i2 < SMITHING_FRAME[i].length; i2++) {
					j[index] = SMITHING_FRAME[i][i2][0];
					index++;
				}
			}
			return j;
		}
		if(type == 13) {
			int[] j = {13734, 13736, 13754, 13746, 13748, 13750, 13752,};
			return j;
		}
		return null;
	}

	@Override
	public boolean clickObject(final Player client, final int type,
	                           final int id, final int slot, final int itemId2, final int itemSlot2) {
		//System.out.println("running method0");
		if(type == 2 || type == 3 || type == 4) {
			if(type == 2)
				return smithItem(client, id, slot, itemId2, 1);
			else if(type == 3)
				return smithItem(client, id, slot, itemId2, 5);
			else if(type == 4)
				return smithItem(client, id, slot, itemId2, 10);
		}
		if(type == 13) {
			return smithSpiritShield(client, id, itemId2);
		}
		if(type == 14) {
			if(itemId2 == 2782 || itemId2 == 2783 || itemId2 == 4306 || itemId2 == 6150) {
				if(id == 18350 || id == 18352 || id == 18354 || id == 18356 || id == 18358 || id == 18360)
					return repairChaotics(client, id);
				if(id >= 11702 || id == 1540)
					return smithExtraItems(client, id);

				try {
					if(id == 2349) {
						openSmithingFrame(client, 1);
					} else if(id == 2351) {
						openSmithingFrame(client, 2);
					} else if(id == 2353) {
						openSmithingFrame(client, 3);
					} else if(id == 2359) {
						openSmithingFrame(client, 4);
					} else if(id == 2361) {
						openSmithingFrame(client, 5);
					} else if(id == 2363) {
						openSmithingFrame(client, 6);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}

	private boolean smithSpiritShield(Player client, int id, int itemId2) {
		if(client.getInventory().getCount(id) <= 0)
			return true;
		if((id == 13754 && itemId2 == 13734) || (id == 13734 && itemId2 == 13754)) {
			ContentEntity.deleteItemA(client, 13734, 1);
			ContentEntity.deleteItemA(client, 13754, 1);
			ContentEntity.addItem(client, 13736, 1);
		} else {
			if(ContentEntity.returnSkillLevel(client, 5) < 90) {
				ContentEntity.sendMessage(client, "You need 90 prayer to create a spirit shield.");
				return true;
			}
			if(ContentEntity.returnSkillLevel(client, Skills.SMITHING) < 85) {
				ContentEntity.sendMessage(client, "You need 85 smithing to create a spirit shield.");
				return true;
			}
			if(id == 13736) {
				id = itemId2;
				itemId2 = 13736;
			}
			if(id < 13746 || id > 13752)
				return true;
			ContentEntity.deleteItemA(client, id, 1);
			ContentEntity.deleteItemA(client, itemId2, 1);
			ContentEntity.addItem(client, id - 8, 1);

		}
		return true;
	}

	private static boolean repairChaotics(Player p, int id) {
		/*if (ContentEntity.returnSkillLevel(p, 13) < 90) {
			ContentEntity.sendMessage(p,
					"You need a smithing level of 90 to make this item.");
			return true;
		}*/
		if(p.getInventory().getCount(id) <= 0)
			return true;
		ContentEntity.removeAllWindows(p);
		if(ContentEntity.getItemAmount(p, 2347) <= 0) {
			ContentEntity.sendMessage(p, "You need a hammer to repair this item.");
			return true;
		}
		if(ContentEntity.returnSkillLevel(p, Skills.SMITHING) < 85) {
			ContentEntity.sendMessage(p, "You need 85 smithing to repair this item.");
			return false;
		}
		if(p.getPoints().getPkPoints() < 2500) {
			p.sendMessage("You need 2500 TactilityPk points to repair this item.");
			return true;
		}
		p.getPoints().setPkPoints(p.getPoints().getPkPoints() - 2500);
		ContentEntity.startAnimation(p, 898);
		ContentEntity.deleteItem(p, id);
		ContentEntity.addItem(p, id - 1);
		ContentEntity.sendMessage(p, "You have successfully repaired your " + ItemDefinition.forId(id).getProperName() + ".");
		return true;
	}

	private boolean smithExtraItems(Player player, int id) {
		int[] hiltIds = {11702, 11704, 11706, 11708, 1540,};
		int[] finishItem = {11694, 11696, 11698, 11700, 11283,};
		int[][] extraItemIds = {{11710, 11712, 11714,},
				{11710, 11712, 11714,}, {11710, 11712, 11714,},
				{11710, 11712, 11714,}, {11286,},// dfs
		};
		int[] level = {80, 80, 80, 80, 85};
		int index = - 1;
		for(int i = 0; i < hiltIds.length; i++) {
			if(hiltIds[i] == id)
				index = i;
		}
		if(index == - 1)
			return false;
		if(! ContentEntity.isItemInBag(player, hiltIds[index]))
			return false;
		if(ContentEntity.returnSkillLevel(player, Skills.SMITHING) < level[index]) {
			ContentEntity.sendMessage(player, "You need " + level[index]
					+ " to smith this item.");
			return false;
		}
		if(true) return false;
		for(int i = 0; i < extraItemIds[index].length; i++) {
			if(! ContentEntity.isItemInBag(player, extraItemIds[index][i]))
				return true;
		}
		ContentEntity.deleteItem(player, id);
		for(int i = 0; i < extraItemIds[index].length; i++) {
			ContentEntity.deleteItem(player, extraItemIds[index][i]);
		}
		ContentEntity.sendMessage(player, "You smith a "
				+ ItemDefinition.forId(finishItem[index]).getName() + ".");
		ContentEntity.startAnimation(player, 898);
		ContentEntity
				.addSkillXP(player, 4000 * EXPMULTIPLIER, Skills.SMITHING);
		ContentEntity.addItem(player, finishItem[index]);
		return true;
	}

	@Override
	public void init() throws FileNotFoundException {

	}
}