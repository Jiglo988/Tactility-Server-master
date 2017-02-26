package org.hyperion.rs2.model.content.jge.entry;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.jge.entry.claim.Claims;
import org.hyperion.rs2.model.content.jge.entry.progress.ProgressManager;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Administrator on 9/23/2015.
 */
public class Entry {

    public enum Type{

        BUYING("Buy Offer", "Bought", "Buy"),
        SELLING("Sell Offer", "Sold", "Sell");

        public final String entryName;
        public final String pastTense;
        public final String singleName;

        Type(final String entryName, final String pastTense, final String singleName){
            this.entryName = entryName;
            this.pastTense = pastTense;
            this.singleName = singleName;
        }

        public Type opposite(){
            return this == BUYING ? SELLING : BUYING;
        }
    }

    public enum Currency{

        PK_TICKETS("PKT", 5020),
        COINS("GP", 995);

        public final String shortName;
        public final int itemId;

        Currency(final String shortName, final int itemId){
            this.shortName = shortName;
            this.itemId = itemId;
        }

        public Item amount(final int quantity){
            return Item.create(itemId, quantity);
        }
    }

    public final OffsetDateTime date;
    public final String playerName;
    public final Type type;
    public final int slot;
    public final int itemId;
    public final int itemQuantity;
    public final int unitPrice;
    public final Currency currency;
    public final int totalPrice;

    public boolean cancelled;

    public ProgressManager progress;
    public Claims claims;

    public Entry(final OffsetDateTime date, final String playerName, final Type type, final int slot, final int itemId, final int itemQuantity, final int unitPrice, final Currency currency){
        this.date = date;
        this.playerName = playerName;
        this.type = type;
        this.slot = slot;
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
        this.unitPrice = unitPrice;
        this.currency = currency;

        totalPrice = unitPrice * itemQuantity;

        progress = new ProgressManager(this);
        claims = new Claims(this);
    }

    public boolean finished(){
        return progress.completed()
                && claims.empty();
    }

    public Item item(){
        return Item.create(itemId, itemQuantity);
    }

    public Optional<Player> playerOpt(){
        return Optional.ofNullable(World.getPlayerByName(playerName));
    }

    public void ifPlayer(final Consumer<Player> action){
        playerOpt().ifPresent(action);
    }

    public Player player(){
        return playerOpt().orElse(null);
    }

    public static EntryBuilder build(final Player player, final Type type, final int slot, final Currency currency){
        return new EntryBuilder(player, type, slot, currency);
    }
}
