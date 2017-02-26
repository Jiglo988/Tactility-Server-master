package org.hyperion.rs2.model;

import org.hyperion.rs2.util.AccountValue;

/**
 * Created by Gilles on 20/10/2015.
 */
public class ExpectedValues {
    private int deltaTrade = 0, deltaStake = 0, deltaDrop = 0, deltaPickup = 0, deltaGamble = 0, deltaOther = 0;
    private Player player;

    public ExpectedValues(Player player) {
        this.player = player;
    }

    public int getExpectedValue() {
        return player.getStartValue() + deltaTrade + deltaStake + deltaDrop + deltaPickup + deltaGamble + deltaOther;
    }

    public void removeItemFromInventory(String reason, Item item) {
        if(reason.equalsIgnoreCase("Gambling")) {
            changeDeltaGamble(-getValue(item));
            return;
        } else if(reason.equalsIgnoreCase("Trade")) {
            changeDeltaTrade(-getValue(item));
            return;
        } else if(reason.equalsIgnoreCase("Stake")) {
            changeDeltaStake(-getValue(item));
            return;
        }
        changeDeltaOther(reason, -getValue(item));
    }

    public void addItemtoInventory(String reason, Item item) {
        if(reason.equalsIgnoreCase("Gambling")) {
            changeDeltaGamble(getValue(item));
            return;
        } else if(reason.equalsIgnoreCase("Trade")) {
            changeDeltaTrade(getValue(item));
            return;
        } else if(reason.equalsIgnoreCase("Stake")) {
            changeDeltaStake(getValue(item));
            return;
        }
        changeDeltaOther(reason, getValue(item));
    }

    public void buyFromStore(Item item) {
        changeDeltaOther("Bought " + item.getCount() + " " + item.getDefinition().getProperName() + " from store", getValue(item));
    }

    public void sellToStore(Item item) {
        changeDeltaOther("Sold " + item.getCount() + " " + item.getDefinition().getProperName() + " to store", -getValue(item));
    }

    public void pickupItem(Item item) {
        changeDeltaPickup(getValue(item));
    }

    public void deathDrop(Item... items) {
        for(Item item : items)
            dropItem(item);
    }

    public void trade(Item[] itemsToAdd, Item[] itemsToRemove) {
        for(Item item : itemsToAdd)
            if(item != null)
                addItemtoInventory("Trade", item);
        for(Item item : itemsToRemove)
            if(item != null)
                removeItemFromInventory("Trade", item);
    }

    public void stake(Item[] items, boolean won) {
        for(Item item : items)
            if (item != null)
                if(won)
                    addItemtoInventory("Stake", item);
                else
                    removeItemFromInventory("Stake", item);

    }

    public void stakeItem(Item item) {
        player.getExpectedValues().changeDeltaStake(getValue(item));
    }

    public void dropItem(Item item) {
        player.getExpectedValues().changeDeltaDrop(-getValue(item));
    }

    public void changeDeltaTrade(int change) {
        player.debugMessage("Changed trade value for player " + player.getSafeDisplayName() + " from '" + deltaTrade + "' to '" + (deltaTrade + change) + "'");
        deltaTrade +=  change;
    }

    public void changeDeltaStake(int change) {
        player.debugMessage("Changed stake value for player " + player.getSafeDisplayName() + " from '" + deltaStake + "' to '" + (deltaStake + change) + "'");
        deltaStake +=  change;
    }

    public void changeDeltaDrop(int change) {
        player.debugMessage("Changed drop value for player " + player.getSafeDisplayName() + " from '" + deltaDrop + "' to '" + (deltaDrop + change) + "'");
        deltaDrop +=  change;
    }

    public void changeDeltaPickup(int change) {
        player.debugMessage("Changed pickup value for player " + player.getSafeDisplayName() + " from '" + deltaPickup + "' to '" + (deltaPickup + change) + "'");
        deltaPickup +=  change;
    }

    public void changeDeltaGamble(int change) {
        player.debugMessage("Changed gamble value for player " + player.getSafeDisplayName() + " from '" + deltaGamble + "' to '" + (deltaGamble + change) + "'");
        deltaGamble +=  change;
    }

    public void changeDeltaOther(String reason, int change) {
        player.debugMessage("Changed value for player " + player.getSafeDisplayName() + " from '" + deltaOther + "' to '" + (deltaOther + change) + "' for reason '" + reason + "'.");
        deltaOther +=  change;
    }

    private static int getValue(Item item) {
        return AccountValue.getItemValue(item);
    }
}
