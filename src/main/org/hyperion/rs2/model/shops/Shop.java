package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ShopManager;

/**
 * @author Arsen Maxyutov.
 */
public abstract class Shop {

    public static final int LEGENDARY_TICKET = 13663;

	public static final int COINS_ID = 995;

	public static final int MAX_STATIC_ITEMS = 40;

	public static final int SHOP_INTERFACE_ID = 3900;

	private Item[] static_items = new Item[MAX_STATIC_ITEMS];

	private String name;

	private Container container;

	private int id;

	private boolean general;

	public boolean isGeneral() {
		return general;
	}

	public Item[] getStaticItems() {
		return static_items;
	}

	/**
	 * Gets the shop for the id.
	 *
	 * @param id
	 * @return
	 */
	public static Shop forId(int id) {
		return ShopManager.forId(id);
	}

	/**
	 * Gets the item at the specified index.
	 *
	 * @param index
	 * @return
	 */
	public Item get(int index) {
		return static_items[index];
	}

	/**
	 * Gets the static item with the specified id
	 *
	 * @param id
	 * @return the item if found, null if not.
	 */
	public Item getStaticItem(int id) {
		for(Item item : static_items) {
			if(item != null) {
				if(item.getId() == id)
					return item;
			}
		}
		return null;
	}

	/**
	 * Constructs a new shop.
	 *
	 * @param name
	 * @param container
	 */
	public Shop(int id, String name, Container container, boolean general) {
		this.id = id;
		this.container = container;
		this.name = name.replace("_", " ");
		this.general = general;
	}

	public boolean isStatic(int id) {
		for(Item item : static_items) {
			if(item != null) {
				if(item.getId() == id)
					return true;
			}
		}
		return false;
	}

	public void addStaticItem(Item item) {
		if(item == null)
			return;
		if(item.getId() < 1)
			return;
		for(int i = 0; i < static_items.length; i++) {
			if(static_items[i] == null) {
				static_items[i] = item;
				break;
			}
		}
	}

	public Container getContainer() {
		return container;
	}

	public String getName() {
		return name;
	}

	public void updatePlayers() {
		for(Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(this.id == player.getShopId()) {
				player.getActionSender().sendUpdateItems(SHOP_INTERFACE_ID, container.toArray());
			}
		}
	}

	public abstract void sellToShop(Player player, Item item);

	public abstract void buyFromShop(Player player, Item item);

	public abstract void valueBuyItem(Player player, Item item);

	public abstract void valueSellItem(Player player, Item item);

	public abstract void process();

}
