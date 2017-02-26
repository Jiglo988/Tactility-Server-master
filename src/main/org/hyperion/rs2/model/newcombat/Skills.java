package org.hyperion.rs2.model.newcombat;

public class Skills {


	/**
	 * The skill names.
	 */
	public static final String[] SKILL_NAME = {"Attack", "Defence",
			"Strength", "Hitpoints", "Range", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching",
			"Fishing", "Firemaking", "Crafting", "Smithing",
			"Mining", "Herblore", "Agility", "Thieving",
			"Slayer", "Farming", "Runecrafting", "Construction", "Hunter", "Summoning", "Dungeoneering"};

	/**
	 * The number of skills.
	 */
	public static final int SKILL_COUNT = SKILL_NAME.length;

	/**
	 * Constants for the skill numbers.
	 */
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6,
			COOKING = 7, WOODCUTTING = 8, FLETCHING = 9,
			FISHING = 10, FIREMAKING = 11, CRAFTING = 12,
			SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18,
			FARMING = 19, RUNECRAFTING = 20, CONSTRUCTION = 21, HUNTER = 22, SUMMONING = 23, DUNGEONINEERING = 24;


	/**
	 * The levels array.
	 */
	private int[] levels = new int[SKILL_COUNT];


	public int[] getLevels() {
		return levels;
	}

	/**
	 * The experience array.
	 */
	private int[] exps = new int[SKILL_COUNT];

	public int[] getXps() {
		return exps;
	}

	/**
	 * Returns the actual levels array.
	 */
	public int[] getXpForLevels() {
		int[] levelsarray = new int[levels.length];
		for(int i = 0; i < exps.length; i++) {
			levelsarray[i] = getLevelForExp(i);
		}
		return levelsarray;
	}

	/**
	 * Creates a skills object.
	 *
	 * @param player The player whose skills this object represents.
	 */
	public Skills() {
		reset();
	}

	public void reset() {
		for(int i = 0; i < SKILL_COUNT; i++) {
			levels[i] = 1;
			exps[i] = 0;
		}
	}


	/**
	 * Sets a skill.
	 *
	 * @param skill The skill id.
	 * @param level The level.
	 * @param exp   The experience.
	 */
	public void setSkill(int skill, int level, int exp) {
		levels[skill] = level;
		exps[skill] = exp;
	}

	/**
	 * Sets a level.
	 *
	 * @param skill The skill id.
	 * @param level The level.
	 */
	public void setLevel(int skill, int level) {
		levels[skill] = level;
	}

	/**
	 * Sets experience.
	 *
	 * @param skill The skill id.
	 * @param exp   The experience.
	 */
	public void setExperience(int skill, int exp) {
		int oldLvl = getLevelForExp(skill);
		exps[skill] = exp;
		int newLvl = getLevelForExp(skill);
	}


	/**
	 * Gets a level.
	 *
	 * @param skill The skill id.
	 * @return The level.
	 */
	public int getLevel(int skill) {
		return (int) levels[skill];
	}

	/**
	 * Gets a level.
	 *
	 * @param skill The skill id.
	 * @return The level.
	 */
	public double getLevel2(int skill) {
		return levels[skill];
	}

	/**
	 * Where Xps[n] is the minimum experience for level n.
	 * e.g. Minimum xp for level 10 is Xps[10].
	 */
	private static int[] EXPERIENCE_PER_LEVEL = {- 1, 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154,
			1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470, 5018,
			5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833,
			16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224,
			41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014, 91721,
			101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254,
			224466, 247886, 273742, 302288, 333804, 368599, 407015, 449428,
			496254, 547953, 605032, 668051, 737627, 814445, 899257, 992895,
			1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068,
			2192818, 2421087, 2673114, 2951373, 3258594, 3597792, 3972294,
			4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614,
			8771558, 9684577, 10692629, 11805606, 13034431,
	};

	/**
	 * Gets a level by experience.
	 *
	 * @param skill The skill id.
	 * @return The level.
	 */
	public int getLevelForExp(int skill) {
		if(skill > SKILL_COUNT - 1)
			return 1;
		int exp = exps[skill] + 1;
		for(int i = 99; i >= 0; i--) {
			if(exp >= EXPERIENCE_PER_LEVEL[i]) {
				return i;
			}
		}
		return 1;
	}


	/**
	 * Gets a experience from the level.
	 *
	 * @param level The level.
	 * @return The experience.
	 */
	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for(int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if(lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

}
