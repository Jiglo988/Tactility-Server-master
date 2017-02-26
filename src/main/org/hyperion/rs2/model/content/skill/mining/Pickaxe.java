package org.hyperion.rs2.model.content.skill.mining;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel
 *         5/20/2016
 */
public enum Pickaxe {
    BRONZE(1265, 1, 625, 1.0),
    IRON(1267, 1, 626, 1.05),
    STEEL(1269, 6, 627, 1.1),
    MITHRIL(1273, 21, 628, 1.15),
    ADAMANT(1271, 31, 629, 1.2),
    RUNITE(1275, 41, 624, 1.25),
    DRAGON(15259, 61, 12188, 1.3),
    ADZE(13661, 80, 10226, 1.35);

    private final static Pickaxe[] VALUES = values();
    private final static List<Pickaxe> ORDINAL = Stream.of(VALUES).sorted((one, two) -> Integer.compare(one.ordinal(), two.ordinal())).collect(Collectors.toCollection(LinkedList::new));
    private final static Map<Integer, Pickaxe> MAPPED = Stream.of(VALUES).collect(Collectors.toMap(Pickaxe::getId, Function.identity()));
    private final int id, level, animation;
    private final double speed;

    Pickaxe(int id, int level, int animation, double speed) {
        this.id = id;
        this.level = level;
        this.animation = animation;
        this.speed = speed;
    }

    public static Pickaxe getPickaxe(final Player player) {
        Item item = player.getEquipment().get(3);
        if (item != null && MAPPED.containsKey(item.getId())) {
            Pickaxe pickaxe = MAPPED.get(item.getId());
            if (pickaxe.usable(player))
                return pickaxe;
        }
        for (Pickaxe array : ORDINAL) {
            if (player.getInventory().contains(array.getId()) && array.usable(player))
                return array;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getAnimation() {
        return animation;
    }

    public double getSpeed() {
        return speed;
    }

    private boolean usable(final Player player) {
        return player.getSkills().getLevel(Skills.MINING) >= getLevel();
    }
}
