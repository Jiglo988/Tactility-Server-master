package org.hyperion.rs2.model.content.skill.hunter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel
 *         5/19/2016
 */
public enum Impling {
    BABY(Arrays.asList(1028, 6055), 1, 11238, 1000),
    YOUNG(Arrays.asList(1029, 6056), 22, 11240, 1500),
    GOURMET(Arrays.asList(1030, 6057), 28, 11242, 2100),
    EARTH(Arrays.asList(1031, 6058), 36, 11244, 2500),
    ESSENCE(Arrays.asList(1032, 6059), 42, 11246, 3600),
    ELECTRIC(Arrays.asList(1033, 6060), 50, 11248, 7503),
    NATURE(Arrays.asList(1034, 6061), 58, 11250, 15500),
    MAGPIE(Arrays.asList(1035, 6062), 65, 11252, 26000),
    NINJA(Arrays.asList(6053, 6063), 74, 11254, 56000),
    PIRATE(Arrays.asList(7845, 7846), 76, 13337, 61000),
    DRAGON(Arrays.asList(6054, 6064), 83, 11256, 85400),
    KINGLY(Arrays.asList(7903, 7906), 91, 15517, 104000);

    private final static Map<Integer, Impling> BY_NPC_ID = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(impling -> impling.getImps().forEach(integer -> BY_NPC_ID.put(integer, impling)));
    }

    private final List<Integer> imps;
    private final int level, item, experience;

    Impling(List<Integer> imps, int level, int item, int experience) {
        this.imps = imps;
        this.level = level;
        this.item = item;
        this.experience = experience;
    }

    public static Impling getImplingForId(int value) {
        return BY_NPC_ID.get(value);
    }

    public List<Integer> getImps() {
        return imps;
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