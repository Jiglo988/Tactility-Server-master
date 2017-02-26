package org.hyperion.rs2;

/**
 * Holds global server constants.
 *
 * @author Graham Edgecombe
 */
public class Constants {

	/**
	 * The directory for the engine scripts.
	 */
	public static final String SCRIPTS_DIRECTORY = "./data/scripts/";

	/**
	 * Difference in X coordinates for directions array.
	 */
	public static final byte[] DIRECTION_DELTA_X = new byte[]{- 1, 0, 1, - 1,
			1, - 1, 0, 1};

	/**
	 * Difference in Y coordinates for directions array.
	 */
	public static final byte[] DIRECTION_DELTA_Y = new byte[]{1, 1, 1, 0, 0,
			- 1, - 1, - 1};

	/**
	 * Default sidebar interfaces array.
	 */
	/*
	 * public static final int SIDEBAR_INTERFACES[][] = new int[][] { new int[]
	 * { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 0, 14 }, new int[] { 3917,
	 * 638, 3213, 1644, 5608, 1151, 18500, 5065, 5715, 2449, 19029, 19100, 6299,
	 * 2423, 1151 }, };
	 */
	public static final int SIDEBAR_INTERFACES[][] = new int[][]{
			new int[]{14, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 0, 7, 15},
			new int[]{32000, 3917, 33000, 3213, 1644, 5608, 1151, 5065, 5715, 2449,
					904, 147, 6299, 2423, 18128, 31400},};

	/**
	 * Incoming packet sizes array.
	 */
	public static final int PACKET_SIZES[] = {
			0, 0, 0, 1, - 1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 6, 6, 0, - 1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 0, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, - 1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, - 1, - 1, // 90
			0, 13, 0, - 1, 0, 0, 0, 0, 0, 0,// 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, - 1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			3, -1, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, - 1, - 1, 0, 0, 0, 8,// 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, - 1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, - 1, 0, - 1, 4,// 240
			0, 0, 6, 6, 0, 0, 0 // 250à
	};

	public static final int MELEE = 0;
	public static final int RANGE = 1;
	public static final int MAGE = 2;
	public static final int DEFLECT = 3;
	public static final int EMPTY = 4;


	/**
	 * The player cap.
	 */
	public static final int MAX_PLAYERS = 800;

	/**
	 * The NPC cap.
	 */
	public static final int MAX_NPCS = 3500;

	/**
	 * The NPC cap.
	 */
	public static final int MAX_NPCS_WAITING_LIST = 100;

	/**
	 * An array of valid characters in a long username.
	 */
	public static final char VALID_CHARS[] = {'_', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*',
			'(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[',
			']', '|', '?', '/', '`'};

	/**
	 * Packed text translate table.
	 */
	public static final char XLATE_TABLE[] = {' ', 'e', 't', 'a', 'o', 'i',
			'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g',
			'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(',
			')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$',
			'%', '"', '[', ']'};

	public static final String[] BONUS_NAME = {"Stab", "Slash", "Crush",
			"Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range",
			"Strength", "Prayer"};

	/**
	 * The maximum amount of items in a stack.
	 */
	public static final int MAX_ITEMS = Integer.MAX_VALUE;

	public static final int XPRATE = 20;

	/**
	 * When the server starts up, in Milliseconds.
	 */
	public static final long STARTTIME = System.currentTimeMillis();


}
