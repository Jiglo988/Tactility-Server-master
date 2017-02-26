package org.hyperion.rs2.model.content.skill.cooking;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel
 *         5/21/2016
 */
public enum Cookable {

    SHRIMPS(317, 315, 323, 1, 30, 10),
    ANCHOVIES(321, 315, 323, 1, 30, 10),
    MEAT(2136, 2142, 2146, 1, 30, 11),
    CHICKEN(2138, 2140, 2144, 1, 30, 10),
    SARDINE(327, 325, 369, 1, 40, 10),
    HERRING(345, 347, 357, 5, 50, 16),
    MACKEREL(353, 355, 357, 10, 60, 20),
    TROUT(335, 333, 343, 15, 70, 25),
    PIKE(349, 351, 343, 20, 80, 30),
    SALMON(331, 329, 343, 25, 90, 35),
    TUNA(359, 361, 367, 30, 100, 65),
    LOBSTER(377, 379, 381, 40, 120, 74),
    BASS(363, 365, 367, 43, 130, 80),
    SWORDFISH(371, 373, 375, 45, 140, 86),
    MONKFISH(7944, 7946, 7948, 62, 150, 72),
    SHARK(383, 385, 387, 80, 210, 90),
    SEA_TURTLE(395, 397, 399, 82, 211, 92),
    MANTA_RAY(389, 391, 393, 91, 216, -1),
    ROCKTAIL(15270, 15272, 15274, 95, 234, -1);

    public static final Map<Integer, Cookable> BY_ITEM_ID = Arrays.stream(values()).collect(Collectors.toMap(Cookable::getRaw, Function.identity()));

    private final int raw, cooked, burned, level, experience, success;

    Cookable(int raw, int cooked, int burned, int level, int experience, int success) {
        this.raw = raw;
        this.cooked = cooked;
        this.burned = burned;
        this.level = level;
        this.experience = experience;
        this.success = success;
    }

    public static Cookable getByItemId(final int value) {
        return BY_ITEM_ID.get(value);
    }

    public int getRaw() {
        return raw;
    }

    public int getCooked() {
        return cooked;
    }

    public int getBurned() {
        return burned;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getSuccess() {
        return success;
    }
}
