package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.ArrayUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class CombineItems implements ContentTemplate {

    private final List<Integer> usable = Arrays.asList(6585, 19333, 11335, 19346, 4087, 19348, 4585, 19348, 6617, 19350, 1187, 19352, 11335, 19354, 4087, 19356, 4585, 19356, 6617, 19358, 1187, 19360);
    private final List<Integer> splittable = Arrays.asList(19335, 19336, 19338, 19339, 19337, 19340, 19341, 19343, 19344, 19342, 19345);

    @Override
    public boolean clickObject(Player player, final int type, final int primary, final int slot, final int secondary, final int itemSlot2) {
        if (type == 13) {
            combine(player, primary, secondary);
        } else {
            split(player, primary);
        }
        return false;
    }

    private void combine(final Player player, final int a, final int b) {
        final Item primary = Item.create(a);
        final Item secondary = Item.create(b);
        if (player.getInventory().hasItem(primary)
                && player.getInventory().hasItem(secondary)) {
            final Combination combining = Combination.getCombining(primary.getId(), secondary.getId());
            if (combining != null) {
                player.getInventory().remove(primary);
                player.getInventory().remove(secondary);
                player.getInventory().add(Item.create(combining.getProduct()));
            }
        }
    }

    private void split(final Player player, final int id) {
        final Item item = Item.create(id);
        if (player.getInventory().hasItem(item)) {
            final Combination splitting = Combination.getSplitting(item.getId());
            if (splitting != null) {
                if (ContentEntity.freeSlots(player) < 2) {
                    player.sendMessage("You need at least 2 free Inventory spaces for this.");
                    return;
                }
                player.getInventory().remove(item);
                splitting.getList().stream().forEach(value -> player.getInventory().add(Item.create(value)));
            }
        }
    }

    @Override
    public void init() throws FileNotFoundException {
    }

    @Override
    public int[] getValues(int type) {
        return type == 13 ? ArrayUtils.fromList(usable) : type == 22 ? ArrayUtils.fromList(splittable) : null;
    }

    public enum Combination {
        FURY_OR(Arrays.asList(6585, 19333), 19335),
        DRAGON_FULL_OR(Arrays.asList(11335, 19346), 19336),
        DRAGON_LEGS_OR(Arrays.asList(4087, 19348), 19338),
        DRAGON_SKIRT_OR(Arrays.asList(4585, 19348), 19339),
        DRAGON_PLATE_OR(Arrays.asList(6617, 19350), 19337),
        DRAGON_SQUARE_OR(Arrays.asList(1187, 19352), 19340),
        DRAGON_FULL_SP(Arrays.asList(11335, 19354), 19341),
        DRAGON_LEGS_SP(Arrays.asList(4087, 19356), 19343),
        DRAGON_SKIRT_SP(Arrays.asList(4585, 19356), 19344),
        DRAGON_PLATE_SP(Arrays.asList(6617, 19358), 19342),
        DRAGON_SQUARE_SP(Arrays.asList(1187, 19360), 19345);

        private final List<Integer> list;
        private final int product;

        Combination(List<Integer> list, int product) {
            this.list = list;
            this.product = product;
        }

        public static Combination getCombining(final int primary, final int secondary) {
            return (FURY_OR.list.contains(primary) && FURY_OR.list.contains(secondary))
                    ? FURY_OR : DRAGON_FULL_OR.list.contains(primary) && DRAGON_FULL_OR.list.contains(secondary)
                    ? DRAGON_FULL_OR : DRAGON_LEGS_OR.list.contains(primary) && DRAGON_LEGS_OR.list.contains(secondary)
                    ? DRAGON_LEGS_OR : DRAGON_SKIRT_OR.list.contains(primary) && DRAGON_SKIRT_OR.list.contains(secondary)
                    ? DRAGON_SKIRT_OR : DRAGON_PLATE_OR.list.contains(primary) && DRAGON_PLATE_OR.list.contains(secondary)
                    ? DRAGON_PLATE_OR : DRAGON_SQUARE_OR.list.contains(primary) && DRAGON_SQUARE_OR.list.contains(secondary)
                    ? DRAGON_SQUARE_OR : DRAGON_FULL_SP.list.contains(primary) && DRAGON_FULL_SP.list.contains(secondary)
                    ? DRAGON_FULL_SP : DRAGON_LEGS_SP.list.contains(primary) && DRAGON_LEGS_SP.list.contains(secondary)
                    ? DRAGON_LEGS_SP : DRAGON_SKIRT_SP.list.contains(primary) && DRAGON_SKIRT_SP.list.contains(secondary)
                    ? DRAGON_SKIRT_SP : DRAGON_PLATE_SP.list.contains(primary) && DRAGON_PLATE_SP.list.contains(secondary)
                    ? DRAGON_PLATE_SP : DRAGON_SQUARE_SP.list.contains(primary) && DRAGON_SQUARE_SP.list.contains(secondary)
                    ? DRAGON_SQUARE_SP : null;
        }

        public static Combination getSplitting(final int value) {
            return FURY_OR.product == value
                    ? FURY_OR : DRAGON_FULL_OR.product == value
                    ? DRAGON_FULL_OR : DRAGON_LEGS_OR.product == value
                    ? DRAGON_LEGS_OR : DRAGON_SKIRT_OR.product == value
                    ? DRAGON_SKIRT_OR : DRAGON_PLATE_OR.product == value
                    ? DRAGON_PLATE_OR : DRAGON_SQUARE_OR.product == value
                    ? DRAGON_SQUARE_OR : DRAGON_FULL_SP.product == value
                    ? DRAGON_FULL_SP : DRAGON_LEGS_SP.product == value
                    ? DRAGON_LEGS_SP : DRAGON_SKIRT_SP.product == value
                    ? DRAGON_SKIRT_SP : DRAGON_PLATE_SP.product == value
                    ? DRAGON_PLATE_SP : DRAGON_SQUARE_SP.product == value
                    ? DRAGON_SQUARE_SP : null;
        }

        public List<Integer> getList() {
            return list;
        }

        public int getProduct() {
            return product;
        }
    }

}
