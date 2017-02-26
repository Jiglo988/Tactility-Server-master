package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.content.misc.ItemSpawning;

public abstract class PointsShop extends Shop {

	public PointsShop(int id, String name, Container container) {
		super(id, name, container, false);
	}

	@Override
	public void sellToShop(Player player, Item item) {
		if(player.needsNameChange() || player.doubleChar()) {
			return;
		}
		player.getActionSender().sendMessage("You can't sell to this shop.");
	}

	@Override
	public void buyFromShop(Player player, Item item) {
		if(player.needsNameChange() || player.doubleChar()) {
			return;
		}
        if(!ItemSpawning.canSpawn(player)) {
            return;
        }
		int price = item.getCount() * getPrice(item.getId());
		if(getPointsAmount(player) >= price) {
			setPointsAmount(player, getPointsAmount(player) - price);
			this.getContainer().remove(item);
			player.getExpectedValues().buyFromStore(item);
			player.getInventory().add(item);
			player.getActionSender().sendUpdateItems(3823, player.getInventory().toArray());
			updatePlayers();
		} else {
			player.getActionSender().sendMessage(
					"You don't have enough " + getPointsName().toLowerCase() + " to buy this item.");
		}
	}

	@Override
	public void valueBuyItem(Player player, Item item) {
		int price = getPrice(item.getId());

		String message = "The shop will sell a '@dre@"
				+ item.getDefinition().getProperName() + "@bla@' for " + price + " " + getPointsName().toLowerCase() + ".";
		if(price == 1) {
			message = message.replace("points", "point");
		}
		player.getActionSender().sendMessage(message);
        if(item.getId() == LEGENDARY_TICKET)
            player.sendImportantMessage("You cannot sell this item back to the shop");
	}

	@Override
	public void valueSellItem(Player player, Item item) {
		player.getActionSender().sendMessage("You can't sell to this shop.");
	}

	@Override
	public void process() {
		for(Item item : getStaticItems()) {
			if(item == null)
				continue;
			if(getContainer().contains(item.getId())) {
				Item shopItem = getContainer().getById(item.getId());
				int delta = item.getCount() - shopItem.getCount();
				if(delta > 0) {
					int addCount = 1;
					getContainer().add(new Item(item.getId(), addCount));
				} else if(delta < 0) {
					getContainer().remove(new Item(item.getId()));
				}
			} else {
				getContainer().add(new Item(item.getId()));
			}
		}
		updatePlayers();
	}

	public abstract String getPointsName();

	public abstract int getPrice(int itemId);

	protected abstract int getPointsAmount(Player player);

	protected abstract void setPointsAmount(Player player, int value);

}
