package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.RunePouchContainerListener;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.util.Arrays;

/**
 * Created by Scott Perretta on 3/22/2015.
 */
public class RunePouch implements ContentTemplate {

    public static final int SIZE = 3;
    public static final int POUCH = 17999;
    public static final int INTERFACE = 28990;
    public static final int RUNE_INTERFACE = 28992;
    public static final int INVENTORY_INTERFACE = 28995;

    public static void open(Player player) {
        if (player.getBankField().isBanking()) {
            player.sendMessage("You cannot store runes and bank at the same time.");
            return;
        }
        player.getActionSender().showInterface(INTERFACE);
        player.getInterfaceState().addListener(player.getRunePouch(), new RunePouchContainerListener(player));
        player.getRunePouch().shift();
        player.getRunePouch().setFiringEvents(true);
    }

    private static boolean isRune(Item item) {
        if (item != null && ((item.getId() >= 554 && item.getId() <= 566) || item.getId() == 9075)) {
            return true;
        }
        return false;
    }

    public static void withdraw(final Player player, final int id, final int amount) {
        if (player.getBankField().isBanking() || player.openedBoB) {
            return;
        }
        final int taking = amount > player.getRunePouch().getCount(id) ? player.getRunePouch().getCount(id) : amount;
        if (taking == 0) {
            return;
        }
        final Item tranfering = Item.create(id, taking);
        if (tranfering == null || !player.getRunePouch().hasItem(tranfering)) {
            return;
        }
        if (player.getInventory().hasRoomFor(tranfering)) {
            player.getRunePouch().remove(tranfering);
            player.getInventory().add(tranfering);
            player.getRunePouch().shift();
            player.getRunePouch().fireItemsChanged();
        } else {
            player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many.");
        }
    }

    public static void deposit(final Player player, final int slot, final int id, final int amount) {
        if (player.getBankField().isBanking() || player.openedBoB) {
            return;
        }
        final int depositing = amount > player.getInventory().getCount(id) ? player.getInventory().getCount(id) : amount;
        if (depositing == 0) {
            return;
        }
        final Item transfering = Item.create(id, depositing);
        if (transfering == null || !player.getInventory().hasItem(transfering)) {
            return;
        }
        if (!isRune(transfering)) {
            player.sendMessage("You can only deposit runes in here.");
            return;
        }
        if (player.getRunePouch().hasRoomFor(transfering)) {
            player.getInventory().remove(transfering);
            player.getRunePouch().add(transfering);
            player.getRunePouch().shift();
            player.getRunePouch().fireItemsChanged();
        } else {
            player.getActionSender().sendMessage("You don't have enough space in your rune pouch.");
        }
    }

    public static void empty(Player player) {
        if (player == null || player.getRunePouch() == null)
            return;
        Arrays.asList(player.getRunePouch().toArray()).stream().filter(value -> value != null).forEach(value -> {
            withdraw(player, value.getId(), value.getCount());
        });
    }

    @Override
    public boolean clickObject2(Player player, int type, int a, int b, int c, int d) {
        if (type == ClickType.ITEM_OPTOION6) {
            if (player.getRunePouch().size() > 0) {
                RunePouch.empty(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getValues(int type) {
        return (type == ClickType.EAT || type == ClickType.ITEM_OPTOION6) ? new int[]{POUCH} : new int[0];
    }

    @Override
    public boolean itemOptionOne(Player player, int id, int slot, int interfaceId) {
        player.openedBoB = false;
        player.getBankField().setBanking(false);
        RunePouch.open(player);
        return false;
    }
}