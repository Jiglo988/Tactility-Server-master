package org.hyperion.rs2.model.container.bank;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.minigame.LastManStanding;
import org.hyperion.rs2.model.content.misc.ItemSpawning;

import java.util.Arrays;

/**
 * Banking utility class.
 *
 * @author Graham Edgecombe
 */
public class Bank {

    /**
     * The bank size.
     */
    public static final int SIZE = 452;

    /**
     * The player inventory interface.
     */
    public static final int PLAYER_INVENTORY_INTERFACE = 5064;

    /**
     * The bank inventory interface.
     */
    public static final int BANK_INVENTORY_INTERFACE = 50088;

    /**
     * The Deposit Box interface.
     */
    public static final int DEPOSIT_INVENTORY_INTERFACE = 7433;


    /**
     * Opens the bank for the specified player.
     *
     * @param player The player to open the bank for.
     */
    public static void open(Player player, boolean setPin) {
        if(!Rank.hasAbility(player, Rank.DEVELOPER)) {
            if(!player.getLocation().isBankingAllowed()) {
                player.sendMessage("You cannot bank in this location.");
                return;
            }
        }
        player.resetingPin = false;
        if (player.bankPin != null && !player.bankPin.equals("null")) {
            if ((player.bankPin.length() < 4 && setPin) || (player.bankPin.length() >= 4 && !player.bankPin.equals(player.enterPin))) {
                BankPin.loadUpPinInterface(player);
                return;
            }
        }
        player.getActionSender().sendInterfaceInventory(5292, PLAYER_INVENTORY_INTERFACE - 1);
        player.getInterfaceState().addListener(player.getBank(), new BankContainerListener(player));
        player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, PLAYER_INVENTORY_INTERFACE));
        player.getBank().shift();
        player.getBankField().setBanking(true);
        player.openedBoB = false;
        player.getBank().setFiringEvents(true);
    }

    /**
     * Opens the deposit box for the certin player
     *
     * @param player The player to have a opened deposit Box
     */
    public static void openDepositBox(Player player) {
        player.getBank().shift();
        player.getActionSender().sendInterfaceInventory(4465, 197);
        player.getInterfaceState().addListener(player.getInventory(),
                new InterfaceContainerListener(player, 7423));
    }

    /**
     * Withdraws an item.
     *
     * @param player The player.
     * @param id     The item id.
     * @param amount The amount of the item to deposit.
     */
    public static void withdraw(Player player, int id, int amount) {
        if(!Rank.hasAbility(player, Rank.DEVELOPER)) {
            if(!LastManStanding.inLMSArea(player.cE.getAbsX(), player.cE.getAbsY())) {
                if (player.getPosition().inPvPArea())
                    return;
                if (Position.inAttackableArea(player))
                    return;
                if (FightPits.inPits(player))
                    return;
                if (!player.getBankField().isBanking()) {
                    return;
                }
                if (!ItemSpawning.canSpawn(player)) {
                    return;
                }
            } else if(player.getExtraData().getLong("combatimmunity") < System.currentTimeMillis()) {
                player.getActionSender().sendMessage("You can only open the bank while you are invincible.");
                player.getActionSender().sendMessage("You are invincible after you die for 20 seconds, unless you attack someone.");
                return;
            }
        }
            int slot = player.getBank().getSlotById(id);
            if (slot < 0) {
                return;
        }
        BankItem bankItem = (BankItem) player.getBank().get(slot);
        if ((bankItem == null) || (bankItem.getId() != id)) {
            return;
        }

        int tab = bankItem.getTabIndex();
        int transferAmount = player.getBank().getCount(bankItem.getId());
        if (transferAmount >= amount) {
            transferAmount = amount;
        } else if (transferAmount == 0) {
            return;
        }
        int newId = bankItem.getId();
        if (player.getBankField().isWithdrawAsNote()) {
            int noteId = bankItem.getDefinition().getNotedId();
            if (noteId != -1) {
                newId = noteId;
            } else {
                player.getActionSender().sendMessage("This item cannot be witdrawn as a note.");
            }
        }
        ItemDefinition def = ItemDefinition.forId(newId);
        if (def.isStackable()) {
            if ((player.getInventory().freeSlots() <= 0)
                    && (player.getInventory().getById(newId) == null)) {
                player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many.");
            }
        } else {
            int free = player.getInventory().freeSlots();
            if (transferAmount > free) {
                player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many.");
                transferAmount = free;
            }
        }
        if (player.getInventory().add(new Item(newId, transferAmount))) {
            int newAmount = bankItem.getCount() - transferAmount;
            if (newAmount <= 0) {
                player.getBank().setFiringEvents(false);
                player.getBank().set(slot, null);
                player.getBank().shift();
                if(player.getBankField().getTabAmounts()[tab] <= 0) {
                    Bank.collapse(player, tab + 1, tab);
                    player.getBank().fireItemsChanged();
                    Bank.viewTab(player, 0);
                }

                player.getBank().fireItemsChanged();
                player.getBank().setFiringEvents(true);

            } else {
                bankItem.setCount(newAmount);
                player.getBank().set(slot, bankItem);
                player.getBank().fireItemChanged(slot);
            }
        } else {
            player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many.");
        }
    }

    /**
     * Deposits an item.
     *
     * @param player The player.
     * @param slot   The slot in the player's inventory.
     * @param id     The item id.
     * @param amount The amount of the item to deposit.
     */
    public static void deposit(Player player, int slot, int id, int amount, boolean inventory) {
        deposit(player, slot, id, amount, player.getInventory(), inventory, true);
    }

    public static void deposit(Player player, int slot, int id, int amount, Container container, boolean inventory, boolean refresh) {
        if(!Rank.hasAbility(player, Rank.DEVELOPER)) {
            if(!LastManStanding.inLMSArea(player.cE.getAbsX(), player.cE.getAbsY())) {
                if (player.getPosition().inPvPArea())
                    return;
                if (slot < 0 || slot > container.capacity() || id < 0 || id > ItemDefinition.MAX_ID)
                    return;
                if (Position.inAttackableArea(player))
                    return;
                if (FightPits.inPits(player))
                    return;
                if (!player.getBankField().isBanking()) {
                    return;
                }

                if (!ItemSpawning.canSpawn(player)) {
                    return;
                }
            } else if(player.getExtraData().getLong("combatimmunity") < System.currentTimeMillis()) {
                player.getActionSender().sendMessage("You can only open the bank while you are invincible.");
                player.getActionSender().sendMessage("You are invincible after you die for 20 seconds, unless you attack someone.");
                return;
            }
        }
        if (player.getBankField().isSearching()) {
            viewTab(player, 0);
        }
        boolean inventoryFiringEvents = inventory ? player.getInventory().isFiringEvents() : player.getEquipment().isFiringEvents();
        boolean bankFiringEvents = player.getBank().isFiringEvents();
        if (inventory) {
            player.getInventory().setFiringEvents(false);
        } else {
            player.getEquipment().setFiringEvents(false);
        }
        player.getBank().setFiringEvents(refresh);
        try {
            Item item = inventory ? player.getInventory().get(slot) : player.getEquipment()
                    .get(slot);
            if (item == null) {
                return;
            }
            if (item.getId() != id) {
                return;
            }
            if (item.getDefinition() == null) {
                player.sendMessage("Some items were unable to deposit.");
                return;
            }
            int transferAmount = container.getCount(id);
            if(transferAmount >= amount) {
                transferAmount = amount;
            } else if(transferAmount == 0) {
                return;
            }
            if (item.getDefinition().isStackable() || item.getDefinition().isNoted())
            {
                int bankedId = item.getDefinition().isNoted() ? item.getDefinition().getNormalId() : item.getId();
                if ((player.getBank().freeSlots() < 1) && (player.getBank().getById(bankedId) == null)) {
                    player.getActionSender().sendMessage("You don't have enough space in your bank account.");
                    return;
                }
                int newInventoryAmount = item.getCount() - transferAmount;
                Item newItem;
                if (newInventoryAmount <= 0) {
                    newItem = null;
                } else {
                    newItem = new Item(item.getId(), newInventoryAmount);
                }
                //boolean contains = player.getBank().contains(bankedId);
                Item t = player.getBank().getById(bankedId);
                int toTab = t != null ? ((BankItem)t).getTabIndex() : player
                        .getBankField().getTabIndex();
                BankItem toAdd = new BankItem(toTab, bankedId, transferAmount);
                player.getBank().setFiringEvents(false);
                if (!player.getBank().add(toAdd)) {
                    player.getActionSender().sendMessage("You don't have enough space in your bank account.");
                } else {
                    if (inventory) {
                        player.getInventory().set(slot, newItem);
                        if (refresh) {
                            player.getInventory().fireItemsChanged();
                        }
                    } else {
                        player.getEquipment().set(slot, newItem);
                        if (refresh) {
                            player.getEquipment().fireItemsChanged();
                        }
                    }
                    /*if (!contains) {
                        if (player.getBankField().getUsedTabs() > 1) {
                            player.getBankField().getTabAmounts()[toTab]--;
                            int to = player.getBankField().getOffset(toTab)
                                    + (player.getBankField().getTabAmounts()[toTab]);
                            int from = player.getBank().size() - 1;
                            System.out.println("OFFSET: " + player.getBankField().getOffset(toTab));
                            System.out.println("TAB AMOUNT: " + (player.getBankField().getTabAmounts()[toTab]));
                            System.out.println("TO: "+ to);
                            System.out.println("FROM: "+ from);
                            ((BankItem) player.getBank().get(from)).setTabSlot(toTab);
                            insert(player, from, to);
                            player.getBankField().getTabAmounts()[toTab]++;
                        }

                    } */
                    player.getBank().fireItemChanged(player.getBank().getSlotById(bankedId));
                }
                player.getBank().setFiringEvents(refresh);
            } else {
                int itemTab = player.getBankField().getTabIndex();
                BankItem toAdd = new BankItem(itemTab, item.getId(), transferAmount);
                boolean bankRefresh = player.getBank().isFiringEvents();
                player.getBank().setFiringEvents(false);
                if (!player.getBank().add(toAdd)) {
                    player.getActionSender().sendMessage("You don't have enough space in your bank account.");
                } else {
                    for (int i = 0; i < transferAmount; i++) {
                        if (inventory) {
                            player.getInventory().set(player.getInventory().getSlotById(id), null);
                            if (refresh) {
                                player.getInventory().fireItemsChanged();
                            }
                        } else {
                            player.getEquipment().set(slot, null);
                            if (refresh) {
                                player.getEquipment().fireItemsChanged();
                            }
                        }
                    }
                    int toTab = player.getBankField().getTabIndex();
                    /*int to = 0;
                    if (!contains) {
                        if (player.getBankField().getUsedTabs() > 1) {
                            player.getBankField().getTabAmounts()[toTab]--;
                            to = player.getBankField().getOffset(toTab) + (player.getBankField().getTabAmounts()[toTab]);
                            int from = player.getBank().size() - 1;
                            ((BankItem) player.getBank().get(from)).setTabSlot(toTab);
                            insert(player, from, to);
                            player.getBankField().getTabAmounts()[toTab]++;
                        }
                    } else {
                        to = player.getBank().getSlotById(item.getId());
                    } */
                    if (refresh) {
                        player.getBank().fireItemChanged(player.getBank().getSlotById(id));
                    }
                }
                player.getBank().setFiringEvents(bankRefresh);
            }
        } finally {
            if (inventory) {
                player.getInventory().setFiringEvents(inventoryFiringEvents);
            } else {
                player.getEquipment().setFiringEvents(inventoryFiringEvents);
            }
            player.getBank().setFiringEvents(bankFiringEvents);
        }
    }

    public static void sortItems(Player player) {
        Item[] bankList = player.getBank().toArray();
        BankItem[] bankItems = new BankItem[player.getBank().size()];
        for (int i = 0, c = 0; i < bankList.length; i++) {
            if (bankList[i] != null)
            {
                bankItems[c++] = (BankItem) bankList[i];
            }
        }
        Arrays.sort(bankItems, (first, second) -> first.getTabIndex() - second.getTabIndex());
        player.getBank().setItems(Arrays.copyOf(bankItems, SIZE));
    }

    public static void initializeBankConfigs(Player player) {
        player.getBankField().setTabIndex(0);
        if (player.getBankField().isLoadError()) {
            Bank.sortItems(player);
        }
        player.getBankField().setSearching(false);
        player.getActionSender().sendClientConfig(1011, 1);
        player.getActionSender().sendClientConfig(1009, 1);
        player.getActionSender().sendClientConfig(1010, 0);
        player.getBank().fireItemsChanged();
    }

    public static boolean bankButton(Player player, int buttonId) {
        if ((buttonId >= -15502) && (buttonId <= -15486)) {
            int tab = (buttonId + 15502) / 2;
            viewTab(player, tab);
            return true;
        }
        if ((buttonId >= -15300) && (buttonId <= -15286)) {
            int tab = ((buttonId + 15300) / 2) + 1;
            collapse(player, tab, 0);
            player.getBank().fireItemsChanged();
            viewTab(player, 0);
            return true;
        }
        if (buttonId == 21004) {
            player.getBankField().setSearching(!player.getBankField().isSearching());
            return true;
        }
        if (buttonId == 21008) {
            player.getBankField().setWithdrawAsNote(!player.getBankField().isWithdrawAsNote());
            player.getActionSender().sendClientConfig(1011, player.getBankField().isWithdrawAsNote() ? 0 : 1);
            return true;
        }
        if(buttonId == 21000) {
            player.getBankField().setInserting(!player.getBankField().isInserting());
            player.getActionSender().sendClientConfig(1009, player.getBankField().isInserting() ? 0 : 1);
            return true;
        }
        if(buttonId == 5294) {
            BankPin.loadUpPinInterface(player);
            return true;
        }
        return false;
    }

    public static void viewTab(Player player, int tab) {
        if (player.getBankField().isSearching()) {
            player.getBankField().setSearching(false);
        }
        if(tab > player.getBankField().getTabAmounts().length - 1){
            player.sendMessage("Drag an item here to create a new tab.");
            return;
        }
        if (tab == player.getBankField().getTabIndex()) {
            return;
        }
        if ((player.getBankField().getTabAmounts()[tab] <= 0) && (tab > 0)) {
            player.sendMessage("Drag an item here to create a new tab.");
            return;
        }
        player.getBankField().setTabIndex(tab);
    }

    public static void collapse(Player player, int tab, int toTab) {
        if ((tab <= 0) || (toTab == tab) || (tab >= player.getBankField().getTabAmount())) {
            return;
        }


        player.sendMessage("This may take a while!");
        boolean b = player.getBank().isFiringEvents();
        player.getBank().setFiringEvents(false);
        int start = tab + 1;
        if(player.getBankField().getTabAmounts()[toTab] > 0) {
            for(final BankItem item : player.getBankField().itemsForTab(tab)) {
                item.setTabSlot(toTab);
                if(player.getBank().remove(item) > 0) {
                    player.getBank().add(item);
                }
            }
        } else {
            start = tab;
        }

        for(int i = start; i <= player.getBankField().getUsedTabs(); i++) {
            for(final BankItem item : player.getBankField().itemsForTab(i)) {
                item.setTabSlot(item.getTabIndex() - 1);
            }
        }

       /* for (int fromSlot = 0; fromSlot < initialTabAmount; fromSlot++) {
            if (player.getBank().get(itemSlot) != null) {
                toTab(player, tab, toTab, 0);
            }
        }
        if (tab != player.getBankField().getUsedTabs()) {
            collapse(player, tab + 1, tab);
        }  */
        player.getBank().fireItemsChanged();
        player.getBank().setFiringEvents(b);
        player.getActionSender().sendClientConfig(1009, player.getBankField().isInserting() ? 0 : 1);
    }

    public static boolean toTab(Player player, int fromTab, int toTab, int slot)
    {
        if (toTab == fromTab) {
            return false;
        }
        if (toTab > player.getBankField().getUsedTabs()) {
            return false;
        }
        int from = slot + player.getBankField().getOffset(fromTab);
        int to = (player.getBankField().getOffset(toTab) + player.getBankField().getTabAmounts()[toTab])
                - (fromTab > toTab ? 0 : 1);
        if (player.getBank().get(from) == null) {
            return false;
        }
        ((BankItem) player.getBank().get(from)).setTabSlot(toTab);
        insert(player, from, to);
        return true;
    }

    public static void swapTabs(Player player, int currentSlot, int destinationSlot)
    {
        if(player != null) {

            BankItem current = ((BankItem) player.getBank().get(currentSlot)).copy();
            BankItem destination = ((BankItem) player.getBank().get(destinationSlot)).copy();
            int temp = destination.getTabIndex();
            destination.setTabSlot(current.getTabIndex());
            current.setTabSlot(temp);
            player.getBank().set(destinationSlot, current);
            player.getBank().set(currentSlot, destination);
            //player.getBank().toArray()[destinationSlot] = current;
            //player.getBank().toArray()[currentSlot] = destination;
        }
    }

    public static void moveToTab(Player player, int slot, int fromTab, int toTab)
    {
        boolean bankFiringEvents = player.getBank().isFiringEvents();
        player.getBank().setFiringEvents(false);
        if (toTab(player, fromTab, toTab, slot)) {
            if (player.getBankField().getTabAmounts()[fromTab] <= 0) {
                collapse(player, fromTab + 1, fromTab);
                viewTab(player, 0);
            }
            player.getBank().shift();
            player.getBank().fireItemsChanged();
        }
        player.getBank().setFiringEvents(bankFiringEvents);
    }

    public static void insert(Player player, int currentSlot, int destinationSlot) {
        int index = currentSlot;
        if (currentSlot > destinationSlot) {
            while (index != destinationSlot) {
                swap(player, index, --index);
            }
        } else if (currentSlot < destinationSlot) {
            while (index != destinationSlot) {
                swap(player, index, ++index);
            }
        }
    }

    public static void swap(Player player, int currentSlot, int destinationSlot) {
        BankItem current = (BankItem) player.getBank().get(currentSlot);
        BankItem destination = (BankItem)  player.getBank().get(destinationSlot);
        player.getBank().set(destinationSlot, current);
        player.getBank().set(currentSlot, destination);
    }

}
