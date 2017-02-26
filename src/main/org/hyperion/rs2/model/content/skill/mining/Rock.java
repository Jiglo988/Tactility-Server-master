package org.hyperion.rs2.model.content.skill.mining;

import java.util.*;

/**
 * @author Daniel
 *         5/20/2016
 */
public enum Rock {
    RUNE_ESSENCE(Collections.singletonList(2491), 1, 5, 1436, 3, -1),
    CLAY(Arrays.asList(2108, 2109), 1, 5, 434, 5, 2000),
    TIN(Arrays.asList(2094, 2095, 14902), 1, 17, 438, 6, 4000),
    COPPER(Arrays.asList(2090, 2091, 14906), 1, 17, 436, 6, 4000),
    IRON(Arrays.asList(2092, 2093, 14913), 15, 35, 440, 7, 5000),
    SILVER(Arrays.asList(2100, 2101, 14902), 20, 40, 442, 7, 7000),
    COAL(Arrays.asList(2096, 2097, 14850), 30, 50, 453, 7, 7000),
    GOLD(Arrays.asList(2098, 2099), 40, 65, 444, 7, 10000),
    MITHRIL(Arrays.asList(2102, 2103, 14853), 50, 80, 447, 8, 11000),
    ADAMANTITE(Arrays.asList(2104, 2105, 14862), 70, 95, 449, 9, 14000),
    RUNITE(Arrays.asList(14859, 14860), 85, 125, 451, 9, 45000);

    private static final Map<Integer, Rock> BY_OBJECT_ID = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(rock -> rock.getRocks().forEach(integer -> BY_OBJECT_ID.put(integer, rock)));
    }

    private final List<Integer> rocks;
    private final int level, experience, item, ticks;
    private final long respawn;

    Rock(List<Integer> rocks, int level, int experience, int item, int ticks, long respawn) {
        this.rocks = rocks;
        this.level = level;
        this.experience = experience;
        this.item = item;
        this.ticks = ticks;
        this.respawn = respawn;
    }

    public static Rock getRockById(final int value) {
        return BY_OBJECT_ID.get(value);
    }

    public List<Integer> getRocks() {
        return rocks;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getItem() {
        return item;
    }

    public int getTicks() {
        return ticks;
    }

    public long getRespawn() {
        return respawn;
    }
}
