package org.hyperion.rs2.model.content.jge.entry.claim;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;

import java.util.Optional;

/**
 * Created by Administrator on 9/24/2015.
 */
public class ClaimSlot {

    private int itemId;
    private int itemQuantity;

    public ClaimSlot(final int itemId, final int itemQuantity){
        set(itemId, itemQuantity);
    }

    public ClaimSlot copy(){
        return new ClaimSlot(itemId, itemQuantity);
    }

    public int itemId(){
        return itemId;
    }

    public int itemQuantity(){
        return itemQuantity;
    }

    public void add(final int itemQuantity){
        this.itemQuantity += itemQuantity;
    }

    public boolean holding(final int itemId){
        return this.itemId == itemId;
    }

    public boolean valid(){
        return itemId != -1 && itemQuantity > 0;
    }

    public boolean empty(){
        return !valid();
    }

    public void set(final int itemId, final int itemQuantity){
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
    }

    public void set(final Item item){
        if(item != null)
            set(item.getId(), item.getCount());
        else reset();
    }

    public void reset(){
        itemId = -1;
        itemQuantity = 0;
    }

    public Item item(){
        return valid() ? Item.create(itemId, itemQuantity) : null;
    }

    public String toSaveString(){
        return String.format("%d:%d", itemId, itemQuantity);
    }

    public static ClaimSlot createDefault(){
        return new ClaimSlot(-1, 0);
    }

    public static ClaimSlot fromSaveString(final String claim){
        final String[] split = claim.split(":");
        return new ClaimSlot(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1])
        );
    }
}
