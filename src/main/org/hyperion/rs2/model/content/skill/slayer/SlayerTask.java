package org.hyperion.rs2.model.content.skill.slayer;

import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 11/2/14
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */
	/*
	private static int[] level100 = {269, 749, 84, 1590, 1591, 1592, 55,};
	private static int[] level70 = {4381, 110, 3026, 3027, 3028, 6285, 52, 82, 83,};
	private static int[] level40 = {110, 1976, 125, 111, 117, 112, 119,};
	private static int[] level20 = {1265, 103, 125, 111, 117, 112,};
	private static int[] level3 = {1265, 103,};

	monsterForLevel.put(84, (byte) 1); // Black demons
		monsterForLevel.put(54, (byte) 1); // Black dragons
		monsterForLevel.put(55, (byte) 1); // Blue dragons
		monsterForLevel.put(1582, (byte) 1); // Fire giants
		monsterForLevel.put(6218, (byte) 1); // Goraks
		monsterForLevel.put(83, (byte) 1); // Greater demons
		monsterForLevel.put(6210, (byte) 1); // Hellhounds
		monsterForLevel.put(1591, (byte) 1); // Iron dragons
		monsterForLevel.put(5363, (byte) 1); // Mithril dragons
		monsterForLevel.put(1592, (byte) 1); // Steel dragons
		monsterForLevel.put(5361, (byte) 1); // Waterfiends
		monsterForLevel.put(6215, (byte) 50); // Bloodvelds
		monsterForLevel.put(1618, (byte) 50); // Bloodvelds
		monsterForLevel.put(1619, (byte) 50); // Bloodvelds
		monsterForLevel.put(1637, (byte) 52); // Jellies
		monsterForLevel.put(1607, (byte) 60); // Aberrant spectres
		monsterForLevel.put(1624, (byte) 65); // Dust devils (drops dragon chain)
		monsterForLevel.put(3068, (byte) 72); // Skeletal wyverns (drops dragonic visage)
		monsterForLevel.put(1610, (byte) 75); // Gargoyles (drops granite maul)
		monsterForLevel.put(1613, (byte) 80); // Nechryael (drops black mask)
		monsterForLevel.put(6221, (byte) 83); // Spiritual mages (drops d boots)
		monsterForLevel.put(6231, (byte) 83); // Spiritual mages (drops d boots)
		monsterForLevel.put(6257, (byte) 83); // Spiritual mages (drops d boots)
		monsterForLevel.put(6278, (byte) 83); // Spiritual mages (drops d boots)
		monsterForLevel.put(1615, (byte) 85); // Abyssal demons (drops whips)
		monsterForLevel.put(2783, (byte) 90); // Dark beast (drops dark bow) */
public enum SlayerTask {

    /** The elite tasks */
    GENERAL_GRAARDOR(27, Difficulty.ELITE, 1, 1100, 6260),
    CORPOREAL_BEAST(22, Difficulty.ELITE, 1, 1700, 8133),
    TORMENTED_DEMON(1,Difficulty.ELITE, 1, 634, 8349),
    MITHRIL_DRAGON(2, Difficulty.ELITE, 1, 622, 5363),
    KING_BLACK_DRAGON(3, Difficulty.ELITE, 1, 722, 50),

    /** The hard tasks */
    BLACK_DEMON(4,Difficulty.HARD, 1, 220,
            84),
    /*GORAK(5, Difficulty.HARD, 68, 320,
            6218),*/
    HELL_HOUND(6,Difficulty.HARD, 1, 319,
            6210, 49),
    DARK_BEAST(7,Difficulty.HARD, 95, 297,
            2783),
    ABYSSAL_DEMON(8,Difficulty.HARD, 85, 240,
            2783, 1615),
    GARGOYLE(9,Difficulty.HARD, 75, 190,
            1610),
    STEEL_DRAGON(10,Difficulty.HARD, 1, 563,
            1592),
    ICE_WYRM(11,Difficulty.HARD, 1, 522, 9463),
    BLACK_DRAGON(29, Difficulty.HARD, 1, 422, 54),
    BRONZE_DRAGON(31, Difficulty.HARD, 1, 479, 1590),

    /** Medium tasks  */
    ABERANT_SPECTRES(31, Difficulty.DIFFICULT, 60, 290, 1604),
    ORK(30, Difficulty.DIFFICULT, 1, 280, 6272, 6271, 6273),
    GREATER_DEMON(26, Difficulty.DIFFICULT, 1, 210, 83),
    JELLY(14,Difficulty.DIFFICULT, 52, 212,
            1637, 1638, 1640),
    FIRE_GIANT(12,Difficulty.DIFFICULT, 1, 233,
            1582, 110, 1583, 1584),
    BLOOD_VELD(13,Difficulty.MEDIUM, 50, 277,
             6215, 1618, 1619),
    CHAOS_DWARF(24, Difficulty.MEDIUM, 1, 150, 119),
    MAGIC_AXE(23, Difficulty.MEDIUM, 1, 180, 127),
    BLUE_DRAGON(15,Difficulty.MEDIUM, 1, 205,
             55, 52),
    LESSER_DEMON(25, Difficulty.MEDIUM, 1, 170, 82),
    POISON_SPIDERS(28, Difficulty.MEDIUM, 1, 130, 134),

    /** Easy tasks */

    SKELETON(16,Difficulty.EASY, 1, 45,
            92, 89,459),
    EXPERIMENT(17,Difficulty.EASY, 1, 42,
            1678, 1677),
    ROCK_CRAB(18,Difficulty.EASY, 1, 57,
            1265,1266),
    CHAOS_DRUID(19,Difficulty.EASY, 1, 60,
            181),
    GIANT_BAT(20,Difficulty.EASY, 1, 35,
            78);

    private final Difficulty difficulty;
    private final int slayerLevel, slayerXP, index;
    private final List<Integer> ids = new ArrayList<>();
    private static final int EXP_MULTIPLIER = 14;

    SlayerTask(final int index, final Difficulty difficulty, final int slayerLevel, final int slayerXP, final int... ids) {
        this.difficulty = difficulty;
        this.slayerLevel = slayerLevel;
        this.slayerXP = slayerXP * EXP_MULTIPLIER;
        this.index = index;
        for(int i : ids)
            this.ids.add(i);
    }

    public Difficulty getDifficulty() { return difficulty; }
    public List<Integer> getIds() { return ids; }
    public int getXP() { return slayerXP; }
    public int getId() { return index; }

    public static SlayerTask forLevel(final int slayerLevel) {
        final SlayerTask task = values()[Misc.random(values().length - 1)];
        if(slayerLevel >= task.slayerLevel && Math.abs(slayerLevel - task.difficulty.slayerLevel) <= 25 && !task.equals(CORPOREAL_BEAST)) //ensure task is not too easy and they have the level for it
            return task;
        else return forLevel(slayerLevel);
    }

    enum Difficulty {
        EASY(10, 40, 0, 4),
        MEDIUM(20, 80, 26, 6),
        DIFFICULT(40, 100, 50, 7),
        HARD(60, 150, 75, 8),
        ELITE(5, 10, 99, 8);

        private final int minAmount, maxAmount, slayerLevel, slayerPoints;
        Difficulty(final int minAmount, final int maxAmount, final int slayerLevel, final int slayerPoints) {
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.slayerLevel = slayerLevel;
            this.slayerPoints = slayerPoints;
        }

        public int getAmount() {
            return minAmount + Misc.random(maxAmount - minAmount);
        }

        public int getSlayerPoints() {
            return slayerPoints;
        }


    }

    @Override public String toString() {
        return TextUtils.titleCase(super.toString().replaceAll("_", " ").toLowerCase());
    }
    /**
     * Get all npc ids for slayer npc death for contenttemplate
     * @return all slayer task npc ids
     */
    public static final int[] getTasks() {
        final List<Integer> list = new ArrayList<>();
        for(final SlayerTask tasks : SlayerTask.values()) {
            for(final int i : tasks.ids)
                list.add(i);
        }
        final int[] n = new int[list.size()];
        for(int i = 0; i < n.length; i++) {
            n[i] = list.get(i);
        }
        return n;
    }

    public static SlayerTask taskForId(int npcID) {
        for(final SlayerTask task : SlayerTask.values()) {
            if(task.ids.contains(npcID))
                return task;
        }
        return null;
    }

    public static SlayerTask load(final int index) {
        for(SlayerTask task : SlayerTask.values())
            if(index == task.index)
                return task;
        return null;
    }

    public static int getLevelById(int npcID) {
        SlayerTask task = taskForId(npcID);
        if(task != null)
            return task.slayerLevel;
        else return 0;
    }

}