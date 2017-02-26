package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.bank.BankItem;

import java.math.BigInteger;

/**
 * Created by User on 3/20/2015.
 */
public class TabbedContainer extends Container {

    private Player player;

    public TabbedContainer(Type type, int capacity, Player player) {
        super(type, capacity);
        this.player = player;
    }

    @Override
    public boolean add(Item item) {
        if(item == null) {
            return false;
        }
        BankItem bankItem;
        if(!(item instanceof BankItem))
            item = item.toBankItem(0);
        bankItem = (BankItem) item;
        int here = bankItem.getTabIndex();
        if(bankItem.getId() < 0)
            return false;
        if (bankItem.getDefinition() == null) {
            System.err.println("BankItem: " + bankItem.getId() + " has no definition");
            return true;
        }
        if(bankItem.getDefinition().isNoted())
            bankItem.setID(bankItem.getDefinition().getNormalId());
        if(bankItem.getDefinition().isStackable() || getType().equals(Type.ALWAYS_STACK)) {
            for(int i = 0; i < getItems().length; i++) {
                if(getItems()[i] != null && getItems()[i].getId() == bankItem.getId()) {
                    int totalCount = bankItem.getCount() + getItems()[i].getCount();
                    long fuck_all_count = BigInteger.valueOf(bankItem.getCount()).add(BigInteger.valueOf(getItems()[i].getCount())).longValueExact();
                    if(fuck_all_count >= Constants.MAX_ITEMS || totalCount < 1) {
                        return false;
                    }

                    BankItem newBankItem = new BankItem(((BankItem)get(i)).getTabIndex(), getItems()[i].getId(), getItems()[i].getCount() + bankItem.getCount());
                    set(i, newBankItem);
                    return true;
                }
            }
            return insert(bankItem, -1);
        } else {
            System.out.println("check2");
            int slots = freeSlots();
            if(slots >= bankItem.getCount()) {
                boolean b = isFiringEvents();
                setFiringEvents(false);
                try {
                    for (int i = 0; i < bankItem.getCount(); i++) {
                        set(freeSlot(), new BankItem(bankItem.getTabIndex(), bankItem.getId(), 1));
                    }
                    return true;
                } finally {
                    setFiringEvents(b);
                }
            } else {
                return false;
            }
        }
    }

    public synchronized boolean insert(final BankItem bankItem, int slot) {
        player.getBankField().calculateTabAmounts();
        if(slot == -1 && size() == Bank.SIZE)
            return false;
        int tabAmount = player.getBankField().getTabAmountsLight()[bankItem.getTabIndex()];
        if(tabAmount >= 350)
            return false;
        if(slot == -1) {
            slot = player.getBankField().getOffset(bankItem.getTabIndex()) + tabAmount;
        }
        final Item[] old = items.clone();
        for(int i = 0 ; i < old.length; i++ ){
            if ( i < slot)
                set(i, old[i]);
            else if(i == slot)
                set(i, null);
            else
                set(i, old[i - 1]);

        }

        if (slot == -1 || slot >= Bank.SIZE) {
            return false;
        } else {
            set(slot, bankItem);
            return true;
        }
    }

    /**
     * Removes an item.
     *
     * @param preferredSlot The preferred slot.
     * @param item          The item to remove.
     * @return The number of items removed.
     */
    public int remove(int preferredSlot, Item item) {
        int removed = 0;
        if(item == null || item.getDefinition() == null) {
            //System.out.println("Container null , PLEASE FIX MARTIN!");
            return removed;
        }
        //if(item.getCount() == 0)
        //	return 0;
        if(item.getDefinition().isStackable() || getType().equals(Type.ALWAYS_STACK)) {
            int slot = getSlotById(item.getId());
            if(slot == - 1)
                return removed;
            BankItem stack = (BankItem)get(slot);
            if(stack.getCount() > item.getCount()) {
                removed = item.getCount();
                set(slot, new Item(stack.getId(), stack.getCount() - item.getCount()));
            } else {
                int tab = stack.getTabIndex();
                removed = stack.getCount();
                boolean b = isFiringEvents();
                setFiringEvents(false);
                set(slot, null);
                shift();
                if (player.getBankField().getTabAmounts()[tab] <= 0) {
                    Bank.collapse(player, tab + 1, tab);
                    fireItemsChanged();
                    Bank.viewTab(player, 0);
                }
                if(b)
                    fireItemsChanged();
                setFiringEvents(b);

            }
        } else {
            for(int i = 0; i < item.getCount(); i++) {
                int slot = getSlotById(item.getId());
                if(slot == - 1)
                    continue;
                if(i == 0 && preferredSlot != - 1) {
                    Item inSlot = get(preferredSlot);
                    if(inSlot.getId() == item.getId()) {
                        slot = preferredSlot;
                    }
                }
                if(slot != - 1) {
                    removed++;
                    set(slot, null);
                    shift();
                } else {
                    break;
                }
            }
        }
        return removed;
    }

     @Override
     public void set(int slot, Item item) {
         int tab = player.getBankField().getTabForSlot(slot);
         if(!(item instanceof BankItem) && item != null)
             item = item.toBankItem(player.getBankField().getTabForSlot(slot));
         boolean fire = isFiringEvents();
         items[slot] = item;
         if(fire)
            fireItemsChanged();

     }


    @Override
    public Item get(int slot) {
        Item item = super.get(slot);
        if(item != null && !(item instanceof BankItem)) {
            return item.toBankItem(player.getBankField().getTabForSlot(slot));
        }
        return item;
    }

}
