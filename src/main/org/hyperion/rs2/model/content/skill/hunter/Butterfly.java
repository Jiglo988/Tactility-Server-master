package org.hyperion.rs2.model.content.skill.hunter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel
 *         5/19/2016
 */
public enum Butterfly {
    RUBY_HARVEST(5085, 15, 10020, 1250),
    SAPPHIRE_GLACIALIS(5084, 25, 10018, 1750),
    SNOWY_KNIGHT(5083, 35, 10016, 2450),
    BLACK_WARLOCK(5082, 45, 10014, 7500);

    public final static List<Integer> IDS = Arrays.asList(10020, 10018, 10016, 10014);
    private final static Map<Integer, Butterfly> BY_NPC_ID = Stream.of(values()).collect(Collectors.toMap(Butterfly::getId, Function.identity()));
    private final int id, level, item, experience;

    Butterfly(int id, int level, int item, int experience) {
        this.id = id;
        this.level = level;
        this.item = item;
        this.experience = experience;
    }

    public static Butterfly getButteryflyForId(final int value) {
        return BY_NPC_ID.get(value);
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getItem() {
        return item;
    }

    public int getExperience() {
        return experience;
    }

}
