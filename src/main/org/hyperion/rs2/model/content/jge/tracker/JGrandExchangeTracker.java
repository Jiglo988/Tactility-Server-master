package org.hyperion.rs2.model.content.jge.tracker;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.jge.JGrandExchange;
import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.rs2.model.content.jge.entry.EntryBuilder;
import org.hyperion.rs2.model.content.jge.entry.EntryManager;
import org.hyperion.rs2.model.content.jge.itf.JGrandExchangeInterface;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.iteminfo.ItemInfo;
import org.hyperion.sql.DbHub;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.hyperion.rs2.model.content.jge.itf.JGrandExchangeInterface.*;

/**
 * Created by Administrator on 9/23/2015.
 */
public class JGrandExchangeTracker {

    private static final Object ACTIONS_LOCK = new Object();

    public final Player player;

    public final EntryManager entries;

    public int activeSlot;

    private EntryBuilder newEntry;

    public JGrandExchangeTracker(final Player player){
        this.player = player;

        entries = new EntryManager(player);

        activeSlot = -1;

        loadEntries();
    }

    public void selectItem(final int itemId, final Entry.Type type){
        ifNewEntry(e -> {
            if (e.type() != type) {
                player.sendf("You are not %s an item!", type);
                return;
            }
            final ItemDefinition definition = ItemDefinition.forId(itemId);
            if (definition == null) {
                player.sendf("Invalid item: %d", itemId);
                return;
            }
            if (ItemSpawning.canSpawn(itemId)) {
                player.sendf("Spawnables aren't allowed in the Grand Exchange!");
                return;
            }
            if (!ItemsTradeable.isTradeable(itemId)) {
                player.sendf("Non-tradeables aren't allowed in the Grand Exchange");
                return;
            }
            if (ItemInfo.geBlacklist.check(player, definition))
                return;
            if (e.itemId(itemId)) {
                e.unitPrice(JGrandExchange.getInstance().defaultItemUnitPrice(e.itemId(), e.type().opposite(), e.currency()), false);
                if (e.itemQuantity() < 1) {
                    e.itemQuantity(1, false);
                    JGrandExchangeInterface.NewEntry.setQuantity(player, e.itemQuantity());
                }
                JGrandExchangeInterface.NewEntry.setItem(player, e.item());
                JGrandExchangeInterface.NewEntry.setDefaultUnitPrice(player, e.unitPrice(), e.currency());
                JGrandExchangeInterface.NewEntry.setUnitPriceAndTotalPrice(player, e.unitPrice(), e.totalPrice(), e.currency());
            }
        }, "You're not building a new entry right now");
    }

    public void loadEntries(){
        JGrandExchange.getInstance().get(player.getName().toLowerCase())
                .forEach(entries::add);
    }

    public void notifyChanges(final boolean alert){
        if(player.getGrandExchangeTracker().entries.anyMatch(e -> !e.cancelled && !e.claims.empty())){
            if(alert)
                player.sendf("Alert##Grand Exchange##One or more of your offers have been updated!");
            else
                player.sendf("One or more of your Grand Exchange offers have been updated!");
        }

    }

    public Optional<Entry> activeEntryOpt(){
        return Optional.ofNullable(activeEntry());
    }

    public Entry activeEntry(){
        return activeSlot != -1 ? entries.get(activeSlot) : null;
    }

    public boolean hasActiveEntry(){
        return activeEntry() != null;
    }

    public void ifActiveEntry(final Consumer<Entry> action, final String fmt, final String... args){
        final Entry activeEntry = activeEntry();
        if(activeEntry != null)
            action.accept(activeEntry);
        else
            player.sendf(fmt, args);
    }

    public boolean canOpenInterface(){
        return Rank.hasAbility(player, Rank.DEVELOPER) || ItemSpawning.canSpawn(player, false);
    }

    public void openInterface(final EntryManager entries){
        if(!canOpenInterface()){
            player.sendf("You cannot use the Grand Exchange right now!");
            return;
        }
        player.resetingPin = false;
        if (player.bankPin != null && !player.bankPin.equals("null")) {
            if (player.bankPin.length() >= 4 && !player.bankPin.equals(player.enterPin)) {
                BankPin.loadUpPinInterface(player, false);
                return;
            }
        }
        Entries.open(player, entries);
    }

    public void openInterface(){
        openInterface(entries);
    }

    public boolean buildingNewEntry(){
        return newEntry != null;
    }

    public void ifNewEntry(final Consumer<EntryBuilder> action, final String fmt, final Object... args){
        if(newEntry != null)
            action.accept(newEntry);
        else
            player.sendf(fmt, args);
    }

    public boolean ifNewEntry(final Predicate<EntryBuilder> condition){
        return newEntry != null && condition.test(newEntry);
    }

    public EntryBuilder newEntry(){
        return newEntry;
    }

    public void nullifyNewEntry(){
        newEntry = null;
        player.sendMessage(":stopsearch:");
    }

    public void showEntries(){
        activeSlot = -1;
        openInterface();
    }

    public boolean startNewEntry(final Entry.Type type, final int slot){
        if(!canOpenInterface()){
            player.sendf("You cannot use the Grand Exchange right now!");
            return false;
        }
        if(buildingNewEntry())
            nullifyNewEntry();
        if(entries.used(slot)){
            player.sendf("You can't create a new entry in this slot!");
            return false;
        }
        if(type == Entry.Type.BUYING)
            player.sendMessage(":search:");
        newEntry = Entry.build(player, type, slot, player.hardMode() ? Entry.Currency.COINS : Entry.Currency.PK_TICKETS);
        return true;
    }

    public boolean view(final int slot){
        if(entries.empty(slot)){
            player.sendf("Nothing to view at this slot!");
            return false;
        }
        if(ifNewEntry(e -> e.slot() == slot)){
            player.sendf("You are building an entry in this slot!");
            return false;
        }
        if(hasActiveEntry())
            activeSlot = -1;
        activeSlot = slot;
        return true;
    }

    public boolean handleInterfaceInteraction(final int id){
        if(!JGrandExchange.enabled || !DbHub.getPlayerDb().isInitialized())
            return false;
        switch(id){
            case SLOT_1_BUY:
                if(startNewEntry(Entry.Type.BUYING, 0))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_1_SELL:
                if(startNewEntry(Entry.Type.SELLING, 0))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_1_VIEW:
                if(view(0))
                    ViewingEntry.open(player, entries.get(0));
                return true;
            case SLOT_2_BUY:
                if(startNewEntry(Entry.Type.BUYING, 1))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_2_SELL:
                if(startNewEntry(Entry.Type.SELLING, 1))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_2_VIEW:
                if(view(1))
                    ViewingEntry.open(player, entries.get(1));
                return true;
            case SLOT_3_BUY:
                if(startNewEntry(Entry.Type.BUYING, 2))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_3_SELL:
                if(startNewEntry(Entry.Type.SELLING, 2))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_3_VIEW:
                if(view(2))
                    ViewingEntry.open(player, entries.get(2));
                return true;
            case SLOT_4_BUY:
                if(startNewEntry(Entry.Type.BUYING, 3))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_4_SELL:
                if(startNewEntry(Entry.Type.SELLING, 3))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_4_VIEW:
                if(view(3))
                    ViewingEntry.open(player, entries.get(3));
                return true;
            case SLOT_5_BUY:
                if(startNewEntry(Entry.Type.BUYING, 4))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_5_SELL:
                if(startNewEntry(Entry.Type.SELLING, 4))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_5_VIEW:
                if(view(4))
                    ViewingEntry.open(player, entries.get(4));
                return true;
            case SLOT_6_BUY:
                if(startNewEntry(Entry.Type.BUYING, 5))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_6_SELL:
                if(startNewEntry(Entry.Type.SELLING, 5))
                    NewEntry.open(player, newEntry);
                return true;
            case SLOT_6_VIEW:
                if(view(5))
                    ViewingEntry.open(player, entries.get(5));
                return true;
            case BACK:
                if(buildingNewEntry())
                    nullifyNewEntry();
                showEntries();
                return true;
            case DECREASE_QUANTITY:
                ifNewEntry(e -> {
                    if(e.validItem() && e.decreaseItemQuantity())
                        NewEntry.setQuantityAndTotalPrice(player, e.itemQuantity(), e.totalPrice(), e.currency());
                }, "You must create a new entry before decreasing quantity");
                return true;
            case INCREASE_QUANTITY:
                ifNewEntry(e -> {
                    if(e.validItem() && e.increaseItemQuantity())
                        NewEntry.setQuantityAndTotalPrice(player, e.itemQuantity(), e.totalPrice(), e.currency());
                }, "You must create a new entry before increasing quantity");
                return true;
            case INCREASE_QUANTITY_1:
                ifNewEntry(e -> {
                    if(e.validItem() && e.increaseItemQuantity(1))
                        NewEntry.setQuantityAndTotalPrice(player, e.itemQuantity(), e.totalPrice(), e.currency());
                }, "You must create a new entry before setting quantity");
                return true;
            case INCREASE_QUANTITY_10:
                ifNewEntry(e -> {
                    if(e.validItem() && e.increaseItemQuantity(10))
                        NewEntry.setQuantityAndTotalPrice(player, e.itemQuantity(), e.totalPrice(), e.currency());
                }, "You must create a new entry before setting quantity");
                return true;
            case INCREASE_QUANTITY_100:
                ifNewEntry(e -> {
                    if(e.validItem() && e.increaseItemQuantity(100))
                        NewEntry.setQuantityAndTotalPrice(player, e.itemQuantity(), e.totalPrice(), e.currency());
                }, "You must create a new entry before setting quantity");
                return true;
            case INCREASE_QUANTITY_500:
                ifNewEntry(e -> {
                    if(e.validItem() && e.increaseItemQuantity(500))
                        NewEntry.setQuantityAndTotalPrice(player, e.itemQuantity(), e.totalPrice(), e.currency());
                }, "You must create a new entry before setting quantity");
                return true;
            case ENTER_QUANTITY:
                ifNewEntry(e -> {
                    if (e.validItem())
                        DialogueManager.openDialogue(player, 600);
                }, "You are not building a new entry right now!");
                return true;
            case DECREASE_PRICE:
                ifNewEntry(e -> {
                    if(e.validItem() && e.decreaseUnitPrice())
                        NewEntry.setUnitPriceAndTotalPrice(player, e.unitPrice(), e.totalPrice(), e.currency());
                }, "You must create a new entry before decreasing price");
                return true;
            case INCREASE_PRICE:
                ifNewEntry(e -> {
                    if(e.validItem() && e.increaseUnitPrice())
                        NewEntry.setUnitPriceAndTotalPrice(player, e.unitPrice(), e.totalPrice(), e.currency());
                }, "You must create a new entry before increasing price");
                return true;
            case DECREASE_PRICE_PERCENT:
                ifNewEntry(e -> {
                    if(e.validItem() && e.decreaseUnitPricePercent())
                        NewEntry.setUnitPriceAndTotalPrice(player, e.unitPrice(), e.totalPrice(), e.currency());
                }, "You must create a new entry before decreasing price");
                return true;
            case EQUATE_PRICE:
                ifNewEntry(e -> {
                    if(e.validItem() && e.unitPrice(JGrandExchange.getInstance().defaultItemUnitPrice(e.itemId(), e.type().opposite(), e.currency())))
                        NewEntry.setUnitPriceAndTotalPrice(player, e.unitPrice(), e.totalPrice(), e.currency());
                }, "You must create a new entry before equating price");
                return true;
            case ENTER_PRICE:
                ifNewEntry(e -> {
                    if (e.validItem())
                        DialogueManager.openDialogue(player, 601);
                }, "You are not building a new entry right now!");
                return true;
            case INCREASE_PRICE_PERCENT:
                ifNewEntry(e -> {
                    if(e.validItem() && e.increaseUnitPricePercent())
                        NewEntry.setUnitPriceAndTotalPrice(player, e.unitPrice(), e.totalPrice(), e.currency());
                }, "You must create a new entry before increasing price");
                return true;
            case CONFIRM:
                synchronized(ACTIONS_LOCK){
                ifNewEntry(e -> {
                    if(!JGrandExchange.enabled){
                        player.sendf("The Grand Exchange has been temporarily disabled");
                        return;
                    }
                    if(!canOpenInterface()){
                        player.sendf("You cannot use the Grand Exchange right now!");
                        return;
                    }
                    if(!e.canBuild()){
                        player.sendf("Entry is not valid!");
                        return;
                    }
                    if(entries.used(e.slot())){
                        player.sendf("This slot is already in use");
                        return;
                    }
                    Item taken = null;
                        switch(e.type()){
                            case BUYING: {
                                final int max = player.getInventory().getCount(e.currency().itemId);
                                if(e.totalPrice() > max){
                                    player.sendf("You need %,d more %s to %s %,d %s!",
                                            e.totalPrice() - max, e.currency().shortName.toLowerCase(), e.type().singleName.toLowerCase(),
                                            e.itemQuantity(), e.item().getDefinition().getName());
                                    return;
                                }
                                if(e.totalPrice() <= 0){
                                    player.sendf("Change the unit price and quantity first!");
                                    return;
                                }
                                if(player.getInventory().remove(taken = Item.create(e.currency().itemId, e.totalPrice())) != e.totalPrice()){
                                    player.sendf("Something went wrong!");
                                    return;
                                }
                                player.getExpectedValues().addItemtoInventory("Grand Exchange", taken);
                                break;
                            }
                            case SELLING: {
                                final int max = player.getInventory().getCount(e.itemId());
                                if(e.itemQuantity() > max){
                                    player.sendf("You don't have that many %ss!", e.item().getDefinition().getName());
                                    return;
                                }
                                if(player.getInventory().remove(taken = Item.create(e.itemId(), e.itemQuantity())) != e.itemQuantity()){
                                    player.sendf("Something went wrong!");
                                    return;
                                }
                                player.getExpectedValues().removeItemFromInventory("Grand Exchange", taken);
                                break;
                            }
                        }
                        final Entry entry = newEntry.build();
                        if(!JGrandExchange.getInstance().insert(entry)){
                            player.getInventory().add(taken);
                            player.sendf("Please try again later!");
                            return;
                        }
                        entries.add(entry);
                        JGrandExchange.getInstance().add(entry);
                        nullifyNewEntry();
                        showEntries();
                        JGrandExchange.getInstance().submit(entry);
                        //player.getLogManager().add(LogEntry.geEntryAdded(entry));
                    }, "You are not building a new entry right now!");
                }
                return true;
            case CANCEL:
                synchronized(ACTIONS_LOCK){
                    ifActiveEntry(e -> {
                        if(!JGrandExchange.enabled){
                            player.sendf("The Grand Exchange has been temporarily disabled");
                            return;
                        }
                        if(!canOpenInterface()){
                            player.sendf("You cannot use the Grand Exchange right now!");
                            return;
                        }
                        if(e.cancelled){
                            player.sendf("This entry is already cancelled!");
                            return;
                        }
                        if(e.progress.completed()){
                            if(e.claims.empty())
                                return;
                            player.sendf("This entry is already completed!");
                            return;
                        }
                        e.cancelled = true;
                        final Item oldReturn = e.item();
                        switch(e.type){
                            case BUYING:
                                e.claims.addReturn(e.currency.itemId, e.progress.remainingQuantity() * e.unitPrice);
                                break;
                            case SELLING:
                                e.claims.addReturn(e.itemId, e.progress.remainingQuantity());
                                break;
                        }
                        if(!JGrandExchange.getInstance().updateCancelAndClaims(e)){
                            e.cancelled = false;
                            e.claims.returnSlot.set(oldReturn);
                            player.sendf("Please try again later!");
                            return;
                        }
                        ViewingEntry.setReturnClaim(player, e.claims.returnSlot.item());
                        ViewingEntry.setProgressBar(player, e);
                    }, "You are not viewing an entry right now");
                }
                return true;
            case CLAIM_PROGRESS_SLOT:
                synchronized(ACTIONS_LOCK){
                    ifActiveEntry(e -> {
                        if(!JGrandExchange.enabled){
                            player.sendf("The Grand Exchange has been temporarily disabled");
                            return;
                        }
                        if(!canOpenInterface()){
                            player.sendf("You cannot use the Grand Exchange right now!");
                            return;
                        }
                        final Item oldProgress = e.claims.progressSlot.item();
                        if(e.claims.progressSlot.valid() && e.claims.claimProgress()){
                            ViewingEntry.setProgressClaim(player, e.claims.progressSlot.item());
                            if((e.cancelled && e.claims.empty()) || e.finished()){
                                if(!JGrandExchange.getInstance().delete(e)){
                                    e.claims.progressSlot.set(oldProgress);
                                    ViewingEntry.setProgressClaim(player, e.claims.progressSlot.item());
                                    player.sendf("Please try again later!");
                                    return;
                                }
                                entries.remove(e);
                                JGrandExchange.getInstance().remove(e);
                                showEntries();
                            }
                        }
                    }, "You are not viewing an entry right now");
                }
                return true;
            case CLAIM_RETURN_SLOT:
                synchronized(ACTIONS_LOCK){
                    ifActiveEntry(e -> {
                        if(!JGrandExchange.enabled){
                            player.sendf("The Grand Exchange has been temporarily disabled");
                            return;
                        }
                        if(!canOpenInterface()){
                            player.sendf("You cannot use the Grand Exchange right now!");
                            return;
                        }
                        final Item oldReturn = e.claims.returnSlot.item();
                        if(e.claims.returnSlot.valid() && e.claims.claimReturn()){
                            ViewingEntry.setReturnClaim(player, e.claims.returnSlot.item());
                            if((e.cancelled && e.claims.empty()) || e.finished()){
                                if(!JGrandExchange.getInstance().delete(e)){
                                    e.claims.returnSlot.set(oldReturn);
                                    ViewingEntry.setReturnClaim(player, e.claims.returnSlot.item());
                                    player.sendf("Please try again later!");
                                    return;
                                }
                                entries.remove(e);
                                JGrandExchange.getInstance().remove(e);
                                showEntries();
                            }
                        }
                    }, "You are not viewing an entry right now");
                }
                return true;
            case VIEW_BACK:
                showEntries();
                return true;
            default: return false;
        }
    }


    public static boolean isGrandExchangeAction(int id) {
        switch(id) {
            case SLOT_1_BUY:
            case SLOT_1_SELL:
            case SLOT_1_VIEW:
            case SLOT_2_BUY:
            case SLOT_2_SELL:
            case SLOT_2_VIEW:
            case SLOT_3_BUY:
            case SLOT_3_SELL:
            case SLOT_3_VIEW:
            case SLOT_4_BUY:
            case SLOT_4_SELL:
            case SLOT_4_VIEW:
            case SLOT_5_BUY:
            case SLOT_5_SELL:
            case SLOT_5_VIEW:
            case SLOT_6_BUY:
            case SLOT_6_SELL:
            case SLOT_6_VIEW:
            case BACK:
            case DECREASE_QUANTITY:
            case INCREASE_QUANTITY:
            case INCREASE_QUANTITY_1:
            case INCREASE_QUANTITY_10:
            case INCREASE_QUANTITY_100:
            case INCREASE_QUANTITY_500:
            case ENTER_QUANTITY:
            case DECREASE_PRICE:
            case INCREASE_PRICE:
            case DECREASE_PRICE_PERCENT:
            case EQUATE_PRICE:
            case ENTER_PRICE:
            case INCREASE_PRICE_PERCENT:
            case CONFIRM:
            case CANCEL:
            case CLAIM_PROGRESS_SLOT:
            case CLAIM_RETURN_SLOT:
            case VIEW_BACK:
            return true;
        }
            return false;
    }
}
