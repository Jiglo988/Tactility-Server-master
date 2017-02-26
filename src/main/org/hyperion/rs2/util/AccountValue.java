package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.rs2.model.shops.DonatorShop;

/**
 * @author Arsen Maxyutov.
 */
public class AccountValue {

	private Player player;

	public AccountValue(Player player) {
		this.player = player;
	}

    public long getPkPointValue(){
		final int ge = player.getGrandExchangeTracker().entries
				.stream(e -> !e.cancelled && !e.progress.completed() && e.type == Entry.Type.BUYING && e.currency == Entry.Currency.PK_TICKETS)
				.mapToInt(e -> e.unitPrice * e.progress.remainingQuantity())
				.sum()
				+ player.getGrandExchangeTracker().entries
				.stream(e -> e.claims.progressSlot.holding(5020) || e.claims.returnSlot.holding(5020))
				.mapToInt(e -> e.claims.progressSlot.holding(5020) ? e.claims.progressSlot.itemQuantity() : e.claims.returnSlot.itemQuantity())
				.sum();
        return ge + player.getInventory().getCount(5020)
                + player.getBank().getCount(5020)
                + (player.getPoints().getPkPoints() / 10);
    }

	public int getTotalValue() {
		int counter = 0;
		counter += getInventoryValue();
		counter += getEquipmentValue();
		counter += getBankValue();
		counter += getBobValue();
		counter += getTradeValue();
		counter += getDuelValue();
		counter += player.getPoints().getDonatorPoints();
		counter += player.getPoints().getVotingPoints();
		counter += player.getGrandExchangeTracker().entries
				.stream(e -> !e.cancelled && !e.progress.completed() && e.type == Entry.Type.SELLING)
				.mapToInt(e -> getItemValue(Item.create(e.itemId, e.progress.remainingQuantity())))
				.sum();
		counter += player.getGrandExchangeTracker().entries
				.stream(e -> !e.claims.empty())
				.mapToInt(e -> {
					int total = 0;
					if(e.claims.progressSlot.valid())
						total += getItemValue(e.claims.progressSlot.item());
					if(e.claims.returnSlot.valid())
						total += getItemValue(e.claims.returnSlot.item());
					return total;
				})
				.sum();
		return counter;
	}

	public int getTotalValueWithoutGE() {
		int counter = 0;
		counter += getInventoryValue();
		counter += getEquipmentValue();
		counter += getBankValue();
		counter += getBobValue();
		counter += getTradeValue();
		counter += getDuelValue();
		counter += player.getPoints().getDonatorPoints();
		counter += player.getPoints().getVotingPoints();
		return counter;
	}

	public int getTotalValueWithoutPointsAndGE() {
		int counter = 0;
		counter += getInventoryValue();
		counter += getEquipmentValue();
		counter += getBankValue();
		counter += getBobValue();
		counter += getTradeValue();
		counter += getDuelValue();
		return counter;
	}

	/**
	 * Gets the total account value in donator points without points.
	 *
	 * @return
	 */
	public int getTotalValueWithoutPoints() {
		int counter = 0;
		counter += getInventoryValue();
		counter += getEquipmentValue();
		counter += getBankValue();
		counter += getBobValue();
		counter += getTradeValue();
		counter += getDuelValue();
		counter += player.getGrandExchangeTracker().entries
				.stream(e -> !e.cancelled && !e.progress.completed() && e.type == Entry.Type.SELLING)
				.mapToInt(e -> getItemValue(Item.create(e.itemId, e.progress.remainingQuantity())))
				.sum();
		counter += player.getGrandExchangeTracker().entries
				.stream(e -> !e.claims.empty())
				.mapToInt(e -> {
					int total = 0;
					if(e.claims.progressSlot.valid())
						total += getItemValue(e.claims.progressSlot.item());
					if(e.claims.returnSlot.valid())
						total += getItemValue(e.claims.returnSlot.item());
					return total;
				})
				.sum();
		return counter;
	}

	public int getInventoryValue() {
		return getContainerValue(player.getInventory());
	}

	public int getEquipmentValue() {
		return getContainerValue(player.getEquipment());
	}

	public int getBankValue() {
		return getContainerValue(player.getBank());
	}

	public int getBobValue() {
		return getContainerValue(player.getBoB());
	}

	public int getTradeValue() {
		return getContainerValue(player.getTrade());
	}

	public int getDuelValue() {
		return getContainerValue(player.getDuel());
	}

	public static int getContainerValue(Container container) {
		int counter = 0;
		if(container == null)
			return counter;
        for (Item item : container.toArray()) {
            counter += getItemValue(item);
        }
		return counter;
	}
	/**
	 * Gets the account value of the item, not forgetting about the item amount/items being noted.
	 *
	 * @param item
	 * @return
	 */
	public static int getItemValue(Item item) {
		if (item == null)
			return 0;
		if (item.getId() == 11694) {
			return 0;
		}
		else {
			return DonatorShop.getPrice(item.getId()) * item.getCount();
		}
	}

	static {
	}
}
