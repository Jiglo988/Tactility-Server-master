package org.hyperion.rs2.model.joshyachievementsv2.reward.impl;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.reward.Reward;

public class ItemReward implements Reward{

    public final int itemId;
    public final int itemQuantity;
    public final boolean preferInventory;

    public ItemReward(final int itemId, final int itemQuantity, final boolean preferInventory){
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
        this.preferInventory = preferInventory;
    }

    public void reward(final Player player){
        final Item item = Item.create(itemId, itemQuantity);
        final String name = item.getDefinition().getProperName();
        if(preferInventory && player.getInventory().hasRoomFor(item) && player.getInventory().add(item)){
            player.sendf("%s x %,d has been added to your inventory!", name, itemQuantity);
        }else if(player.getBank().add(item))
            player.sendf("%s x %,d have been added to your bank!", name, itemQuantity);
    }

}
