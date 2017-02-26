package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;

public class CurrencyShop extends Shop {

	private int currency;

	/**
	 * @param id
	 * @param name
	 * @param container
	 * @param currency
	 * @param general
	 */
	public CurrencyShop(int id, String name, Container container, int currency,
	                    boolean general) {
		super(id, name, container, general);
		this.currency = currency;
	}


	@Override
	public void sellToShop(Player player, Item item) {
		if(player.needsNameChange() || player.doubleChar()) {
			return;
		}
        if(currency == COINS_ID && player.hardMode() && !player.getDungeoneering().inDungeon()) {
            player.sendMessage("You cannot sell items to currency shops in this mode");
            return;
        }
		int payment = calculateUnitSellPrice(item) * item.getCount();
		player.getInventory().remove(item);
		player.getExpectedValues().sellToStore(item);
		getContainer().add(item);
		if(payment > 0) {
			player.getInventory().add(new Item(currency, payment));
			player.getExpectedValues().addItemtoInventory("Selling to store", Item.create(currency, payment));
		}
		player.getActionSender().sendUpdateItems(3823, player.getInventory().toArray());
		updatePlayers();
	}

	@Override
	public void buyFromShop(Player player, Item item) {
		if(player.needsNameChange() || player.doubleChar()) {
			return;
		}
		Item coins = player.getInventory().getById(currency);
		if(coins == null) {
			player.getActionSender().sendMessage(
					"You don't have enough "
							+ ItemDefinition.forId(currency).getName().toLowerCase()
							+ " to buy this item.");
			return;
		}
        if(currency == COINS_ID && player.hardMode() && !player.getDungeoneering().inDungeon()) {
            player.sendMessage("You cannot buy from this shop in this mode");
            return;
        }
		int price = calculateUnitBuyPrice(item) * item.getCount();
		if(coins.getCount() >= price) {
			player.getInventory().remove(new Item(currency, price));
			player.getExpectedValues().removeItemFromInventory("Buying from store", Item.create(currency, price));
			this.getContainer().remove(item);
			player.getExpectedValues().buyFromStore(item);
			player.getInventory().add(item);
			player.getActionSender().sendUpdateItems(3823,
					player.getInventory().toArray());
			updatePlayers();
		} else {
			player.getActionSender().sendMessage(
					"You don't have enough "
							+ ItemDefinition.forId(currency).getName().toLowerCase()
							+ " to buy this item.");
		}

	}

	@Override
	public void valueBuyItem(Player player, Item item) {
		int price = calculateUnitBuyPrice(item);
		String message = "The shop will sell a '@dre@"
				+ item.getDefinition().getProperName() + "@bla@' for " + price + " "
				+ ItemDefinition.forId(currency).getName().toLowerCase() + ".";
		player.getActionSender().sendMessage(message);

	}

	@Override
	public void valueSellItem(Player player, Item item) {
		int price = calculateUnitSellPrice(item);
		String message = "The shop will buy a '@dre@"
				+ item.getDefinition().getProperName() + "@bla@' for " + price + " "
				+ ItemDefinition.forId(currency).getName().toLowerCase() + ".";
		player.getActionSender().sendMessage(message);
	}

	/**
	 * The sell price per unit.
	 *
	 * @param item
	 * @return
	 */
	private int calculateUnitSellPrice(Item item) {
		int price = - 1;
		if(currency == Shop.COINS_ID) {
			price = (int) (item.getDefinition().getHighAlcValue() * 0.9);
			if(this.isGeneral())
				price *= 0.9;
		} else {
			price = getSpecialPrice(item);
            price *= 0.5;
		}
		return Math.max(1, price);
	}

	private int calculateUnitBuyPrice(Item item) {
		int price = - 1;
		if(currency == Shop.COINS_ID) {
			price = item.getDefinition().getHighAlcValue();
			if(this.isGeneral())
				price *= 0.9;
		} else {
			price = getSpecialPrice(item);
		}
		return price;
	}

	protected int getSpecialPrice(Item item) {
		switch(item.getId()) {
			case 6585:
				return 20000;
		}
		return 10000;
	}

	@Override
	public void process() {
		for(Item item : getStaticItems()) {
			if(item == null)
				continue;
			if(getContainer().contains(item.getId())) {
				Item shopItem = getContainer().getById(item.getId());
				if(shopItem.getCount() < item.getCount()) {
					getContainer().add(new Item(item.getId()));
				}
			} else {
				getContainer().add(new Item(item.getId()));
			}
		}
		for(Item item : getContainer().toArray()) {
			if(item == null)
				continue;
			if(! isStatic(item.getId()))
				getContainer().remove(new Item(item.getId()));
		}
		updatePlayers();
	}

}
