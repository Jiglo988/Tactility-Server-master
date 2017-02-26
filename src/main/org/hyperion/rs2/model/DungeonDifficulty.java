package org.hyperion.rs2.model;

import org.hyperion.util.Misc;
import org.hyperion.util.Time;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 2/20/15
 * Time: 9:05 AM
 * To change this template use File | Settings | File Templates.
 */

public enum DungeonDifficulty {
    EASY(500, 2, 5, 0, 13_000, Time.ONE_MINUTE * 3, new int[]{2881, 2882, 2883}, 1, 5338, 299, 255, 32, 449, 5595, 196, 119, 1677, 2627, 4940, 4693, 112, 78, 2630),
    MEDIUM(1000,2, 8, 45, 45_000, Time.FIVE_MINUTES + Time.ONE_MINUTE, new int[]{6692, 6691, 6690, 6689, 6688, 3200},  51,52,53, 55, 82, 83,941, 1582, 1583, 49, 2741),
    HARD(5000,2, 10, 80, 100_000, Time.TEN_MINUTES, new int[]{6260, 6247, 6203, 6222, 8349}, 6252, 6248, 6250, 6208, 6204, 6206, 6223, 6225, 6227, 1592, 1591, 1590, 54, 84, 2743, 5253);


    public final int min_level, spawns, rooms, xp, coins;
    private final int[] monsters, bosses;
    public final long time;

    DungeonDifficulty(final int coins, final int spawns, final int rooms, final int min_level, final int xp, final long time, final int[] bosses, final int... monsters) {
        this.coins = coins;
        this.min_level = min_level;
        this.spawns = spawns;
        this.monsters = monsters;
        this.bosses = bosses;
        this.rooms = rooms;
        this.xp = xp;
        this.time = time;

    }

    public int getRandomMonster() {
        return monsters[Misc.random(monsters.length - 1)];
    }

    public int getBoss() {
        return bosses[Misc.random(bosses.length - 1)];
    }

    public enum DungeonSize {
        SMALL(5, 0.75, .45),
        MEDIUM(15, 2.0, 1.7),
        LARGE(30, 3.8, 3.5);

        public final int size;
        public final double multiplier;
        public final double multi_time;
        DungeonSize(final int size, final double multiplier, final double multi) {
            this.size = size;
            this.multiplier = multiplier;
            this.multi_time = multi;
        }
    }

}
