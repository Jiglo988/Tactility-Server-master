package org.hyperion.rs2.model.content.jge.itf;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.jge.JGrandExchange;
import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.rs2.model.content.jge.entry.EntryBuilder;
import org.hyperion.rs2.model.content.jge.entry.EntryManager;
import org.hyperion.util.Misc;

/**
 * Created by Administrator on 9/24/2015.
 */
public final class JGrandExchangeInterface {

    public static final int ID = 23670;

    public static final int SELL_INTERFACE = 26571;

    public static final int BACK = 22723;
    public static final int DECREASE_QUANTITY = 22713;
    public static final int INCREASE_QUANTITY = 22714;
    public static final int INCREASE_QUANTITY_1 = 22686;
    public static final int INCREASE_QUANTITY_10 = 22689;
    public static final int INCREASE_QUANTITY_100 = 22692;
    public static final int INCREASE_QUANTITY_500 = 22695;
    public static final int ENTER_QUANTITY = 22698;
    public static final int DECREASE_PRICE = 22715;
    public static final int INCREASE_PRICE = 22716;
    public static final int DECREASE_PRICE_PERCENT = 22701;
    public static final int EQUATE_PRICE = 22704;
    public static final int ENTER_PRICE = 22710;
    public static final int INCREASE_PRICE_PERCENT = 22707;
    public static final int CONFIRM = 22720;
    public static final int CANCEL = 22188;
    public static final int CLAIM_PROGRESS_SLOT = 22192;
    public static final int CLAIM_RETURN_SLOT = 22193;
    public static final int VIEW_BACK = 22187;

    public static final int SLOT_1_BUY = 23673;
    public static final int SLOT_1_SELL = SLOT_1_BUY + 3;
    public static final int SLOT_1_VIEW = 23715;

    public static final int SLOT_2_BUY = SLOT_1_BUY + 7;
    public static final int SLOT_2_SELL = SLOT_2_BUY + 3;
    public static final int SLOT_2_VIEW = SLOT_1_VIEW + 9;

    public static final int SLOT_3_BUY = SLOT_2_BUY + 7;
    public static final int SLOT_3_SELL = SLOT_3_BUY + 3;
    public static final int SLOT_3_VIEW = SLOT_2_VIEW + 9;

    public static final int SLOT_4_BUY = SLOT_3_BUY + 7;
    public static final int SLOT_4_SELL = SLOT_4_BUY + 3;
    public static final int SLOT_4_VIEW = SLOT_3_VIEW + 9;

    public static final int SLOT_5_BUY = SLOT_4_BUY + 7;
    public static final int SLOT_5_SELL = SLOT_5_BUY + 3;
    public static final int SLOT_5_VIEW = SLOT_4_VIEW + 9;

    public static final int SLOT_6_BUY = SLOT_5_BUY + 7;
    public static final int SLOT_6_SELL = SLOT_6_BUY + 3;
    public static final int SLOT_6_VIEW = SLOT_5_VIEW + 9;

    public static final class NewEntry{

        private NewEntry(){}

        public static void setType(final Player player, final Entry.Type type){
            player.getActionSender().sendString(type != null ? type.entryName : "", 22672);
            player.getActionSender().sendHideComponent(22684, type != null && type != Entry.Type.BUYING);
            player.getActionSender().sendHideComponent(22685, type != null && type == Entry.Type.BUYING);
            player.getActionSender().sendHideComponent(22725, type != null && type != Entry.Type.BUYING);
        }

        public static void setItem(final Player player, final Item item){
            player.getActionSender().sendUpdateItems(22717, new Item[]{item});
            player.getActionSender().sendString(item != null ? item.getDefinition().getName() : "", 22673);
            player.getActionSender().sendString(item != null ? item.getDefinition().getDescription().replace("_", " ") : "", 22674);
        }

        public static void setDefaultUnitPrice(final Player player, final int unitPrice, final Entry.Currency currency){
            final String formatted = unitPrice > 0 && currency != null ? String.format("%s %s", Misc.shortNumber(unitPrice), currency.shortName) : "";
            player.getActionSender().sendString(formatted, 22675);
        }

        public static void setUnitPrice(final Player player, final int unitPrice, final Entry.Currency currency){
            final String formatted = unitPrice > 0 && currency != null ? String.format("%,d %s", unitPrice, currency.shortName) : "";
            player.getActionSender().sendString(formatted, 22677);
        }

        public static void setTotalPrice(final Player player, final int totalPrice, final Entry.Currency currency){
            final String formatted = totalPrice > 0 && currency != null ? String.format("%,d %s", totalPrice, currency.shortName) : totalPrice < 0 ? "Too high!" : "";
            player.getActionSender().sendString(formatted, 22683);
        }

        public static void setQuantity(final Player player, final int quantity){
            final String formatted = quantity > 0 ? String.format("%,d", quantity) : "";
            player.getActionSender().sendString(formatted, 22676);
        }

        public static void setQuantityAndTotalPrice(final Player player, final int quantity, final int totalPrice, final Entry.Currency currency){
            setQuantity(player, quantity);
            setTotalPrice(player, totalPrice, currency);
        }

        public static void setUnitPriceAndTotalPrice(final Player player, final int unitPrice, final int totalPrice, final Entry.Currency currency){
            setUnitPrice(player, unitPrice, currency);
            setTotalPrice(player, totalPrice, currency);
        }

        public static void set(final Player player, final EntryBuilder entry){
            setType(player, entry != null ? entry.type() : null);
            setItem(player, entry != null ? entry.item() : null);
            setDefaultUnitPrice(player, entry != null && entry.validItem() ? JGrandExchange.getInstance().defaultItemUnitPrice(entry.itemId(), entry.type().opposite(), entry.currency()) : -1, entry != null ? entry.currency() : null);
            setUnitPrice(player, entry != null ? entry.unitPrice() : -1, entry != null ? entry.currency() : null);
            setTotalPrice(player, entry != null ? entry.totalPrice() : -1, entry != null ? entry.currency() : null);
            setQuantity(player, entry != null ? entry.itemQuantity() : -1);
        }

        public static void open(final Player player, final EntryBuilder entry){
            set(player, entry);
            player.getActionSender().showInterface(22670);
            if(entry.type() == Entry.Type.SELLING){
                player.getActionSender().sendUpdateItems(26571, player.getInventory().getItems());
                player.getActionSender().sendInterfaceInventory(22670, 26570);
            }
        }

    }

    public static final class ViewingEntry{

        private ViewingEntry(){}

        public static void setType(final Player player, final Entry.Type type){
            player.getActionSender().sendString(type != null ? type.entryName : "", 22172);
            player.getActionSender().sendHideComponent(22179, type != null && type == Entry.Type.SELLING);
            player.getActionSender().sendHideComponent(22180, type != null && type == Entry.Type.BUYING);
        }

        public static void setItem(final Player player, final Item item){
            player.getActionSender().sendUpdateItems(22181, new Item[]{item});
            player.getActionSender().sendString(item != null ? item.getDefinition().getName() : "", 22173);
            player.getActionSender().sendString(item != null ? item.getDefinition().getDescription().replace('_', ' ') : "", 22174);
        }

        public static void setUnitPrice(final Player player, final int unitPrice, final Entry.Currency currency){
            final String formatted = unitPrice > 0  && currency != null ? String.format("%,d %s", unitPrice, currency.shortName) : "";
            player.getActionSender().sendString(formatted, 22175);
            player.getActionSender().sendString(formatted, 22177);
        }

        public static void setQuantity(final Player player, final int quantity){
            final String formatted = quantity > 0 ? String.format("%,d", quantity) : "";
            player.getActionSender().sendString(formatted, 22176);
        }

        public static void setTotalPrice(final Player player, final int totalPrice, final Entry.Currency currency){
            final String formatted = totalPrice > 0 && currency != null ? String.format("%,d %s", totalPrice, currency.shortName) : "";
            player.getActionSender().sendString(formatted, 22178);
        }

        public static void setProgressClaim(final Player player, final Item item){
            player.getActionSender().sendUpdateItems(22182, new Item[]{item});
        }

        public static void setReturnClaim(final Player player, final Item item){
            player.getActionSender().sendUpdateItems(22183, new Item[]{item});
        }

        public static void setQuantityProgress(final Player player, final Entry.Type type, final int progressQuantity){
            if(type != null && progressQuantity > -1)
                player.getActionSender().sendString(String.format("You have %s a total of %,d so far", type.pastTense.toLowerCase(), progressQuantity), 22190);
            else
                player.getActionSender().sendString("", 22190);
        }

        public static void setPriceProgress(final Player player, final int progressPrice, final Entry.Currency currency){
            if(progressPrice > -1 && currency != null)
                player.getActionSender().sendString(String.format("For a total price of %,d %s", progressPrice, currency.shortName), 22191);
            else
                player.getActionSender().sendString("", 22191);
        }

        public static void setProgressBar(final Player player, final Entry entry){
            if(entry != null && entry.cancelled) {
                player.getActionSender().sendHideComponent(22184, false);
                player.getActionSender().sendHideComponent(22186, true);
                player.getActionSender().sendHideComponent(22185, true);
            } else if(entry != null && entry.progress.completed()) {
                player.getActionSender().sendHideComponent(22185, false);
                player.getActionSender().sendHideComponent(22184, true);
                player.getActionSender().sendHideComponent(22186, true);
            } else if(entry != null && entry.progress.totalQuantity() == 0) {
                player.getActionSender().sendHideComponent(22186, true);
                player.getActionSender().sendHideComponent(22184, true);
                player.getActionSender().sendHideComponent(22185, true);
            } else {
                player.getActionSender().sendHideComponent(22186, false);
                player.getActionSender().sendInterfaceSpriteDim(22186, entry != null ? (int)entry.progress.quantityPercent() : 0, 100);
                player.getActionSender().sendHideComponent(22184, true);
                player.getActionSender().sendHideComponent(22185, true);
            }
        }

        public static void set(final Player player, final Entry entry){
            setType(player, entry != null ? entry.type : null);
            setItem(player, entry != null ? entry.item() : null);
            setUnitPrice(player, entry != null ? entry.unitPrice : -1, entry != null ? entry.currency : null);
            setQuantity(player, entry != null ? entry.itemQuantity : -1);
            setTotalPrice(player, entry != null ? entry.totalPrice : -1, entry != null ? entry.currency : null);
            setProgressClaim(player, entry != null ? entry.claims.progressSlot.item() : null);
            setReturnClaim(player, entry != null ? entry.claims.returnSlot.item() : null);
            setQuantityProgress(player, entry != null ? entry.type : null, entry != null ? entry.progress.totalQuantity() : -1);
            setPriceProgress(player, entry != null ? entry.progress.totalPrice() : -1, entry != null ? entry.currency : null);
            setProgressBar(player, entry);
        }

        public static void open(final Player player, final Entry entry){
            set(player, entry);
            player.getActionSender().showInterface(22170);
        }
    }

    public static final class Entries {

        private Entries(){}

        public static void set(final Player player, final int slot, final Entry entry){
            if(entry != null) {
                player.getActionSender().sendHideComponent(23672 + (7 * slot), true);
                final int base = 23714 + (9 * slot);
                player.getActionSender().sendHideComponent(base, false);
                player.getActionSender().sendUpdateItems(base + 2, new Item[]{entry.item()});
                if (entry.cancelled) {
                    player.getActionSender().sendHideComponent(base + 3, false);
                    player.getActionSender().sendHideComponent(base + 5, true);
                    player.getActionSender().sendHideComponent(base + 4, true);
                } else if (entry.progress.completed()) {
                    player.getActionSender().sendHideComponent(base + 4, false);
                    player.getActionSender().sendHideComponent(base + 3, true);
                    player.getActionSender().sendHideComponent(base + 5, true);
                } else if (entry.progress.totalQuantity() == 0){
                    player.getActionSender().sendHideComponent(base + 5, true);
                    player.getActionSender().sendHideComponent(base + 4, true);
                    player.getActionSender().sendHideComponent(base + 3, true);
                } else {
                    player.getActionSender().sendHideComponent(base + 5, false);
                    player.getActionSender().sendInterfaceSpriteDim(base + 5, (int)entry.progress.quantityPercent(), 100);
                    player.getActionSender().sendHideComponent(base + 4, true);
                    player.getActionSender().sendHideComponent(base + 3, true);
                }
                player.getActionSender().sendString(entry.item().getDefinition().getName(), base + 6);
                player.getActionSender().sendString(String.format("%,d %s", entry.unitPrice, entry.currency.shortName), base + 7);
                player.getActionSender().sendString(entry.type.singleName, base + 8);
            } else {
                final int base = 23714 + (9 * slot);
                player.getActionSender().sendHideComponent(base, true);
                player.getActionSender().sendHideComponent(23672 + (7 * slot), false);
                player.getActionSender().sendHideComponent(base + 3, true);
                player.getActionSender().sendHideComponent(base + 5, true);
                player.getActionSender().sendHideComponent(base + 4, true);
                player.getActionSender().sendString("", base + 6);
                player.getActionSender().sendString("", base + 7);
                player.getActionSender().sendString("", base + 8);
            }
        }

        public static void setAll(final Player player, final EntryManager entries){
            for(int slot = 0; slot < EntryManager.NUMBER_OF_SLOTS; slot++)
                set(player, slot, entries.get(slot));
        }

        public static void open(final Player player, final EntryManager entries){
            setAll(player, entries);
            player.getActionSender().showInterface(23670);
        }
    }

    private JGrandExchangeInterface(){}

}
