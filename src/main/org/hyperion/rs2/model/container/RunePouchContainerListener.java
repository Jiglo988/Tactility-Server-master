package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.misc2.RunePouch;

/**
 * Created by User on 3/22/2015.
 */
public class RunePouchContainerListener implements ContainerListener {

    private Player player;

    public RunePouchContainerListener(Player player) {
        this.player = player;
    }

    @Override
    public void itemChanged(Container container, int slot) {
        player.getActionSender().sendUpdateItem(RunePouch.RUNE_INTERFACE, slot, container.get(slot));
        player.getActionSender().sendUpdateItems(RunePouch.INVENTORY_INTERFACE, player.getInventory().toArray());
    }

    @Override
    public void itemsChanged(Container container, int[] slots) {

    }

    @Override
    public void itemsChanged(Container container) {
        player.getActionSender().sendUpdateItems(RunePouch.RUNE_INTERFACE, container.toArray());
        player.getActionSender().sendUpdateItems(RunePouch.INVENTORY_INTERFACE, player.getInventory().toArray());
    }

}
