package org.hyperion.rs2.model.content.skill.hunter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel
 *         5/19/2016
 */
public enum Net {
    REGULAR(10010, 0),
    MAGIC(11259, 0),
    VOLATILE(14102, 20),
    SACRED(14110, 10);

    private final static Map<Integer, Net> BY_ITEM_ID = Stream.of(values()).collect(Collectors.toMap(Net::getId, Function.identity()));
    final int id, bonus;

    Net(int id, int bonus) {
        this.id = id;
        this.bonus = bonus;
    }

    public static Net getNetById(final int value) {
        return BY_ITEM_ID.get(value);
    }

    public int getId() {
        return id;
    }

    public int getBonus() {
        return bonus;
    }

    public int getBonus(final int value) {
        return bonus > 0 ? value + (value / bonus) : value;
    }
}
