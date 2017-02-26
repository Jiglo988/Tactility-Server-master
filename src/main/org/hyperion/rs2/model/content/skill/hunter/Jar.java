package org.hyperion.rs2.model.content.skill.hunter;

import java.util.*;

/**
 * @author Daniel
 *         5/19/2016
 */
public enum Jar {
    LOW(Arrays.asList(11238, 11240, 11242), Arrays.asList(995, 1079, 1093, 1113, 1275, 4131, 2491, 2497, 2503, 1333, 1319, 450)) {
        @Override
        public int getAmount(final int value) {
            return value == 995 ? 50000 : value == 450 ? 50 : 1;
        }
    },
    MEDIUM(Arrays.asList(11244, 11246, 11248, 11250), Arrays.asList(995, 9185, 560, 565, 561, 811, 1079, 1093, 1113, 1275, 4131, 4089, 4091, 4095, 4093, 4097)) {
        @Override
        public int getAmount(final int value) {
            return value == 995 ? 200000 : (value == 560 || value == 565 || value == 561) ? 1000 : value == 811 ? 50 : 1;
        }
    },
    HIGH(Arrays.asList(11252, 13337, 11254), Arrays.asList(4151, 4153, 995, 6524, 6329, 1231, 892, 1079, 1093, 3385, 3387, 3389, 3391, 868, 4225)) {
        @Override
        public int getAmount(final int value) {
            return value == 995 ? 500000 : value == 6329 ? 3 : value == 892 ? 100 : value == 868 ? 50 : 1;
        }
    },
    DRAGON(Collections.singletonList(11256), Arrays.asList(537, 535, 4087, 4585, 9244, 9144, 11212, 1713, 11732, 9245, 995, 3140, 10564, 4214)) {
        @Override
        public int getAmount(final int value) {
            return value == 537 ? 15 : (value == 535 || value == 9244) ? 50 : value == 9144 ? 100 : (value == 11212 || value == 9245) ? 10 : value == 1713 ? 5 : value == 995 ? 2500000 : 1;
        }
    },
    KINGLY(Collections.singletonList(15517), Arrays.asList(15509, 15503, 15505, 15507, 15511, 7158, 2364, 995, 3140, 9245, 6738, 1215, 11235, 892)) {
        @Override
        public int getAmount(final int value) {
            return value == 2364 ? 100 : value == 995 ? 10000000 : value == 9245 ? 50 : value == 6738 ? 2 : value == 892 ? 500 : 1;
        }
    },
    RUBY_HARVEST(Collections.singletonList(10020), Collections.singletonList(995)) {
        @Override
        public int getAmount(final int value) {
            return 75000;
        }
    },
    SAPPHIRE_GLACIALIS(Collections.singletonList(10018), Collections.singletonList(995)) {
        @Override
        public int getAmount(final int value) {
            return 100000;
        }
    },
    SNOWY_KNIGHT(Collections.singletonList(10016), Collections.singletonList(995)) {
        @Override
        public int getAmount(final int value) {
            return 150000;
        }
    },
    BLACK_WARLOCK(Collections.singletonList(10014), Collections.singletonList(995)) {
        @Override
        public int getAmount(final int value) {
            return 200000;
        }
    };

    public final static List<Integer> IDS = Arrays.asList(11238, 11240, 11242, 11244, 11246, 11248, 11250, 11252, 13337, 11254, 11256, 15517);
    private final static Map<Integer, Jar> BY_ITEM_ID = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(jar -> jar.getJars().forEach(integer -> BY_ITEM_ID.put(integer, jar)));
    }

    private final List<Integer> jars, rewards;

    Jar(List<Integer> jars, List<Integer> rewards) {
        this.jars = jars;
        this.rewards = rewards;
    }

    public static Jar getJarForId(final int value) {
        return BY_ITEM_ID.get(value);
    }

    public List<Integer> getJars() {
        return jars;
    }

    public List<Integer> getRewards() {
        return rewards;
    }

    public abstract int getAmount(int value);
}
