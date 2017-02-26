package org.hyperion.rs2.model.content.ge;

/**
 * Created by Allen Kinzalow on 4/22/2015.
 */
public class Offer {

    private int itemId;
    private int quantity;
    private int price;
    private int amountProcessed;
    private byte type;
    private boolean cancelled = false;
    private OfferCollection collection;

    public Offer(byte type) {
        this.type = type;
    }

    public Offer(int itemId, int quantity, int price, byte type) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.cancelled = false;
        this.collection = new OfferCollection();
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getCost() {
        return price * quantity;
    }

    public int getAccumulatedCost() {
        return amountProcessed * price;
    }

    public byte getType() {
        return type;
    }

    public int getAmountProcessed() {
        return amountProcessed;
    }

    public void setItemId(int itemId) { this.itemId = itemId; }

    public void increaseQuantity() {
        quantity++;
    }
    public void decreaseQuantity() {quantity--;}

    public void increasePrice() {price++;}
    public void decreasePrice() {
        if(price - 1 <= 0) {
            price = 1;
            return;
        }
        price--;
    }

    public void setPrice(int price){
        if(price <= 0)
            price = 1;
        this.price = price;
    }
    public void setQuantity(int quantity){
        if(quantity <= 0)
            quantity = 1;
        this.quantity = quantity;
    }

    public void addPrice(int add) {
        this.price+=add;
    }
    public void addQuantity(int add) {this.quantity+=add;}

    public void setAmountProcessed(int amountProcessed) {
        this.amountProcessed = amountProcessed;
        if(this.amountProcessed > quantity)
            this.amountProcessed = quantity;
    }

    public void increaseAmountProcessed() {
        this.amountProcessed++;
        if(this.amountProcessed > quantity)
            this.amountProcessed = quantity;
    }

    public void addAmountProcessed(int amount) {
        this.amountProcessed += amount;
        if(this.amountProcessed > quantity)
            this.amountProcessed = quantity;
    }

    public boolean isComplete() {
        return amountProcessed == quantity;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public OfferCollection getCollection() {
        return collection;
    }

    public boolean isSet() {
        return itemId > 0 && quantity > 0 && price > 0 && collection != null;
    }

}
