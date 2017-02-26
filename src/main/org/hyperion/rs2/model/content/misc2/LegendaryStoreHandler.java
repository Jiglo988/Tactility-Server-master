package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.container.ShopManager;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.ArrayUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel
 */
public class LegendaryStoreHandler implements ContentTemplate {

    @Override
    public int[] getValues(final int type) {
        return type == ClickType.NPC_OPTION1 ? new int[]{2790} : type == ClickType.EAT ? ArrayUtils.fromList(Arrays.asList(18839, 18344, 18950)) : null;
    }

    @Override
    public boolean npcOptionOne(final Player player, final int npcId, final int npcLocationX, final int npcLocationY, final int npcSlot) {
        ShopManager.open(player, 79);
        return true;
    }

    @Override
    public boolean itemOptionOne(final Player player, final int id, final int slot, final int interfaceId) {
        if (interfaceId == Inventory.INTERFACE) {
            final Item item = Item.create(id, 1);
            if (item != null && player.getInventory().hasItem(item)) {
                final Scroll scroll = Scroll.getByIntegerValue(item.getId());
                if (scroll != null) {
                    final String name = scroll.toString();
                    final boolean data = player.getPermExtraData().getBoolean(name);
                    if (!data) {
                        player.getInventory().remove(item);
                        player.getPermExtraData().put(name, true);
                        PlayerSaving.save(player);
                        player.sendf("You unlock the @blu@%s@bla@ prayer!", TextUtils.titleCase(name));
                    } else {
                        player.sendf("You have already unlocked the @red@%s@bla@ prayer!", TextUtils.titleCase(name));
                    }
                }
            }
        }
        return true;
    }

    private enum Scroll {
        RIGOUR(18839),
        AUGURY(18344),
        WRATH(18950);

        private static final Map<Integer, Scroll> BY_INTEGER_VALUE = Stream.of(values()).collect(Collectors.toMap(Scroll::getId, Function.identity()));

        private final int id;

        Scroll(int id) {
            this.id = id;
        }

        public static Scroll getByIntegerValue(final int value) {
            return BY_INTEGER_VALUE.get(value);
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
