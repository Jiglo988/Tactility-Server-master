package org.hyperion.rs2.model.content.ge;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;

import java.text.DecimalFormat;

/**
 * Created by Allen Kinzalow on 4/22/2015.
 */
public class GrandExchange {

    public static final int INVENTORY_SELL_INTER = 26571;

    private static DecimalFormat formatter = new DecimalFormat("#,###,###,###");

    private Player player;

    /**
     * The player's current offers.
     */
    private Offer[] offers = new Offer[6];

    /**
     * An object for a temporary new offer.
     */
    private Offer newOffer;

    /**
     * The current open offer slot.
     */
    private int openOfferIndex;

    public GrandExchange(Player player) {
        /** Temporary for testing **/
        this.player = player;
//        offers[0] = new Offer(11724, 7, 13500000, (byte)1);
//        offers[0].setAmountProcessed(3);
//
//        offers[3] = new Offer(4151, 20, 1250000, (byte)1);
//        offers[3].setAmountProcessed(20);
//        offers[3].getCollection().setFirstSlot(new Item(4152, 20));
//
//        offers[4] = new Offer(11696, 5, 7800000, (byte)1);
//        offers[4].setAmountProcessed(2);
//        offers[4].getCollection().setFirstSlot(new Item(11697, 2));
    }

    /**
     * Open all of the current offers.
     */
    public void openOffers() {
        for(int index = 0; index < offers.length; index++) {
            Offer offer = offers[index];
            if(offer != null && offer.isCancelled() && offer.getCollection().isEmpty()) {
                offers[index] = null;
                offer = null;
            }
            if(offer != null) {
                player.getActionSender().sendHideComponent(23672 + (7 * index), true);
                int offerComponent = 23714 + (9 * index);
                player.getActionSender().sendHideComponent(offerComponent, false);
                player.getActionSender().sendUpdateItems(offerComponent + 2, new Item[]{new Item(offer.getItemId())});
                if (offer.isCancelled()) {
                    player.getActionSender().sendHideComponent(offerComponent + 3, false);
                    player.getActionSender().sendHideComponent(offerComponent + 5, true);
                    player.getActionSender().sendHideComponent(offerComponent + 4, true);
                } else if (offer.isComplete()) {
                    player.getActionSender().sendHideComponent(offerComponent + 4, false);
                    player.getActionSender().sendHideComponent(offerComponent + 3, true);
                    player.getActionSender().sendHideComponent(offerComponent + 5, true);
                } else if (offer.getAmountProcessed() == 0){
                    player.getActionSender().sendHideComponent(offerComponent + 5, true);
                    player.getActionSender().sendHideComponent(offerComponent + 4, true);
                    player.getActionSender().sendHideComponent(offerComponent + 3, true);
                } else {
                    player.getActionSender().sendHideComponent(offerComponent + 5, false);
                    player.getActionSender().sendInterfaceSpriteDim(offerComponent + 5, (int)(((double)offer.getAmountProcessed() / (double)offer.getQuantity()) * 100.0), 100);
                    player.getActionSender().sendHideComponent(offerComponent + 4, true);
                    player.getActionSender().sendHideComponent(offerComponent + 3, true);
                }
                player.getActionSender().sendString(ItemDefinition.forId(offer.getItemId()).getName(), offerComponent + 6);
                player.getActionSender().sendString(formatter.format(offer.getPrice()) + " pkt", offerComponent + 7);
                player.getActionSender().sendString(offer.getType() == 0 ? "Buy" : "Sell", offerComponent + 8);
            } else {
                int offerComponent = 23714 + (9 * index);
                player.getActionSender().sendHideComponent(offerComponent, true);
                player.getActionSender().sendHideComponent(23672 + (7 * index), false);
                player.getActionSender().sendHideComponent(offerComponent + 3, true);
                player.getActionSender().sendHideComponent(offerComponent + 5, true);
                player.getActionSender().sendHideComponent(offerComponent + 4, true);
                player.getActionSender().sendString("", offerComponent + 6);
                player.getActionSender().sendString("", offerComponent + 7);
                player.getActionSender().sendString("", offerComponent + 8);
            }
        }
        player.getActionSender().showInterface(23670);
    }

    /**
     * Open a particular offer by slot
     * @param slot
     */
    public void openOffer(int slot) {
        if(offers[slot] != null) {
            Offer offer = offers[slot];
            openOfferIndex = slot;
            ItemDefinition def = ItemDefinition.forId(offer.getItemId());
            player.getActionSender().sendUpdateItems(22181, new Item[]{new Item(offer.getItemId())});
            player.getActionSender().sendUpdateItems(22182, new Item[]{offer.getCollection().getFirstSlot()});
            player.getActionSender().sendUpdateItems(22183, new Item[]{offer.getCollection().getSecondSlot()});
            player.getActionSender().sendString((offer.getType() == 0 ? "Buy" : "Sell") + " Offer", 22172);
            player.getActionSender().sendString(def.getName(), 22173);
            player.getActionSender().sendString(def.getDescription().replace("_", " "), 22174);
            player.getActionSender().sendString(formatter.format(offer.getPrice()) + " pkt", 22175);
            player.getActionSender().sendString(formatter.format(offer.getQuantity()) + "", 22176);
            player.getActionSender().sendString(formatter.format(offer.getPrice()) + " pkt", 22177);
            player.getActionSender().sendString(formatter.format(offer.getCost()) + " pkt", 22178);
            player.getActionSender().sendString("You have " + (offer.getType() == 0 ? "bought" : "sold") + " a total of " + formatter.format(offer.getAmountProcessed()) + " so far", 22190);
            player.getActionSender().sendString("for a total price of " + formatter.format(offer.getAccumulatedCost()) + " pkt.", 22191);
            player.getActionSender().sendHideComponent(22179, offer.getType() == 1);
            player.getActionSender().sendHideComponent(22180, offer.getType() == 0);
            if(offer.isCancelled()) {
                player.getActionSender().sendHideComponent(22184, false);
                player.getActionSender().sendHideComponent(22186, true);
                player.getActionSender().sendHideComponent(22185, true);
            } else if(offer.isComplete()) {
                player.getActionSender().sendHideComponent(22185, false);
                player.getActionSender().sendHideComponent(22184, true);
                player.getActionSender().sendHideComponent(22186, true);
            } else if(offer.getAmountProcessed() == 0) {
                player.getActionSender().sendHideComponent(22186, true);
                player.getActionSender().sendHideComponent(22184, true);
                player.getActionSender().sendHideComponent(22185, true);
            } else {
                player.getActionSender().sendHideComponent(22186, false);
                player.getActionSender().sendInterfaceSpriteDim(22186, (int) (((double) offer.getAmountProcessed() / (double) offer.getQuantity()) * 100.0), 100);
                player.getActionSender().sendHideComponent(22184, true);
                player.getActionSender().sendHideComponent(22185, true);
            }
            player.getActionSender().showInterface(22170);
        }
    }

    /**
     * Open a new offer
     * @param buy
     */
    public void newOffer(boolean buy, int slot) {
        openOfferIndex = slot;
        newOffer = new Offer((byte)(buy ? 0 : 1));
        player.getActionSender().sendUpdateItems(22717, new Item[]{null});
        player.getActionSender().sendString((buy ? "Buy" : "Sell") + " Offer",22672);
        for(int i = 22673; i <= 22677; i++)
            player.getActionSender().sendString("", i);
        player.getActionSender().sendString("",22683);
        player.getActionSender().sendHideComponent(22684, !buy);
        player.getActionSender().sendHideComponent(22685, buy);
        player.getActionSender().sendHideComponent(22725, !buy);
        player.getActionSender().showInterface(22670);
        if(!buy) {
            player.getActionSender().sendUpdateItems(26571, player.getInventory().getItems());
            player.getActionSender().sendInterfaceInventory(22670, 26570);
        }
    }

    public void setSellItem(int itemId) {
        ItemDefinition def = ItemDefinition.forId(itemId);
        if(def.isNoted())
            itemId = def.getNormalId();
        setNewOffer(new Offer(itemId, 1, 1337, player.getGrandExchange().getNewOffer().getType()));
        refreshNewOffer();
    }

    /**
     * Refresh an open new offer.
     */
    public void refreshNewOffer() {
        boolean buy = newOffer.getType() == 0;
        player.getActionSender().sendString((buy ? "Buy" : "Sell") + " Offer", 22662);
        player.getActionSender().sendHideComponent(22684, !buy);
        player.getActionSender().sendHideComponent(22685, buy);
        player.getActionSender().sendHideComponent(22725, !buy);
        if(newOffer.getCollection() != null) {
            ItemDefinition def = ItemDefinition.forId(newOffer.getItemId());
            player.getActionSender().sendUpdateItems(22717, new Item[]{new Item(newOffer.getItemId())});
            player.getActionSender().sendString(formatter.format(newOffer.getPrice()) + " pkt", 22683);
            player.getActionSender().sendString(def.getName(), 22673);
            player.getActionSender().sendString(def.getDescription().replace("_", " "), 22674);
            player.getActionSender().sendString(formatter.format(newOffer.getPrice()) + " pkt", 22675); // actual price
            player.getActionSender().sendString(formatter.format(newOffer.getQuantity()) + "", 22676);
            player.getActionSender().sendString(formatter.format(newOffer.getPrice()) + " pkt", 22677); // set price
            player.getActionSender().sendString(formatter.format(newOffer.getCost()) + " pkt", 22683); // cost
        } else {
            newOffer = null;
            player.getActionSender().sendUpdateItems(22717, new Item[]{});
            for(int i = 22673; i <= 22677; i++)
                player.getActionSender().sendString("", i);
            player.getActionSender().sendString("", 22683);
        }
    }

    /**
     * Finalize a new offer.
     */
    public void submitNewOffer() {

    }

    /**
     * Cancel an offer.
     */
    public void cancelOffer() {
        if(offers[openOfferIndex] != null && !offers[openOfferIndex].isComplete()) {
            offers[openOfferIndex].setCancelled(true);
            openOffer(openOfferIndex);
        }
    }

    /**
     * Confirm an offer.
     */
    public void confirmOffer() {
        offers[openOfferIndex] = new Offer(newOffer.getItemId(), newOffer.getQuantity(), newOffer.getPrice(), newOffer.getType());
        newOffer = null;
        openOffers();
    }

    public Offer getNewOffer() {
        return newOffer;
    }

    public void setNewOffer(Offer offer) {
        this.newOffer = offer;
    }

}
