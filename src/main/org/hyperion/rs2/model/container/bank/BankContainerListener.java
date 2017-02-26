package org.hyperion.rs2.model.container.bank;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;
import org.hyperion.rs2.model.container.impl.TabbedContainer;

import java.util.Arrays;
import java.util.Objects;

public class BankContainerListener implements ContainerListener {

    private final Player player;

    public BankContainerListener(Player player)
    {
        this.player = Objects.requireNonNull(player);
    }

    @Override
    public void itemChanged(Container c, int slot) {
        TabbedContainer container = (TabbedContainer) c;
        int tab = player.getBankField().getTabForSlot(slot);
        if (slot > Byte.MAX_VALUE) {
            itemsChanged(container);
        } else {
            player.getActionSender().sendUpdateItem(Bank.BANK_INVENTORY_INTERFACE + tab, slot
                    - player.getBankField().getOffset(tab), container.get(slot));
        }
    }

    @Override
    public void itemsChanged(Container container, int[] slots) {

    }

    @Override
    public void itemsChanged(Container container) {
        if (!player.getBankField().isSearching()) {
            int tab = 0;
            for (; tab < player.getBankField().getTabAmount(); tab++) {
                int from = player.getBankField().getOffset(tab);
                int to = from + player.getBankField().getTabAmounts()[tab];
                Item[] items = Arrays.copyOf(Arrays.copyOfRange(player.getBank().toArray(), from, to), Bank.SIZE);
                player.getActionSender().sendUpdateItems(Bank.BANK_INVENTORY_INTERFACE + tab, items);
            }
        }
    }

}