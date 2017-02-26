package org.hyperion.rs2.model.container;


import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc2.Food;
import org.hyperion.rs2.util.PushMessage;


/**
 * Banking utility class.
 *
 * @author Martin
 */
public class BoB {

	/**
	 * The BoB size.
	 */
	public static final int SIZE = 30;

	/**
	 * The player inventory interface.
	 */
	public static final int PLAYER_INVENTORY_INTERFACE = 5065;

	/**
	 * The BoB Inventory interface.
	 */
	public static final int BOB_INVENTORY_INTERFACE = 7423;


	/**
	 * Opens the Bob Inventory box for the certain player
	 *
	 * @param player The player to have a opened deposit Box
	 */
	public static void openInventory(Player player) {
		if(player.cE.summonedNpc == null){
			PushMessage.pushStaffMessage("Trying to view BOB inventory with no familiar", player);
			return;
		}
		player.openedBoB = true;

		player.getBoB().shift();
		player.getActionSender().sendInterfaceInventory(4465, 5063);
		player.getActionSender().sendString("Summoning BoB", 7421);

		player.getInterfaceState().addListener(player.getBoB(),
				new InterfaceContainerListener(player, BOB_INVENTORY_INTERFACE));
		player.getInterfaceState().addListener(player.getInventory(),
				new InterfaceContainerListener(player, PLAYER_INVENTORY_INTERFACE - 1));

	}

	public static void dropBoB(Position loc, Player player) {
		if(player == null || player.getBoB() == null || player.isHidden()) return;
		for(int i = 0; i < player.getBoB().capacity(); i++) {
			if(player.getBoB().get(i) != null) {
				GlobalItemManager.newDropItem(player, new GlobalItem(player, loc, player.getBoB().get(i)));
			}
		}
        //player.getLogManager().add(LogEntry.bob(player.getName(),player.getBoB().getItems()));
		player.getBoB().clear();
	}

	/**
	 * Withdraws an item.
	 *
	 * @param player The player.
	 * @param slot   The slot in the player's inventory.
	 * @param id     The item id.
	 * @param amount The amount of the item to deposit.
	 */
	public static void withdraw(Player player, int slot, int id, int amount) {
		if(player.cE.summonedNpc == null){
			PushMessage.pushStaffMessage("@red@[Important] " + player.getSafeDisplayName() + " is trying to withdraw", player);
			PushMessage.pushStaffMessage("@red@[Important] from BoB without any summoned followers.", player);
			return;
		}
		if(player.duelAttackable > 0 || FightPits.inPits(player)) {
			player.getActionSender().sendMessage("I shouldn't be doing this here..");
			return;
		}
		if(slot < 0 || slot > player.getBoB().capacity() || id < 0 || id > ItemDefinition.MAX_ID)
			return;
		Item item = player.getBoB().get(slot);
		if(item == null) {
			return; // invalid packet, or client out of sync
		}
		if(item.getId() != id) {
			return; // invalid packet, or client out of sync
		}
		int transferAmount = item.getCount();
		if(transferAmount >= amount) {
			transferAmount = amount;
		} else if(transferAmount == 0) {
			return; // invalid packet, or client out of sync
		}
		int newId = item.getId(); // TODO deal with withdraw as notes!
		if(player.getSettings().isWithdrawingAsNotes()) {
			if(item.getDefinition().isNoteable()) {
				newId = item.getDefinition().getNotedId();
			}
		}
		ItemDefinition def = ItemDefinition.forId(newId);
		if(def.isStackable()) {
			if(player.getInventory().freeSlots() <= 0
					&& player.getInventory().getById(newId) == null) {
				player.getActionSender()
						.sendMessage(
								"You don't have enough inventory space to withdraw that many."); // this
				// is
				// the
				// real
				// message
			}
		} else {
			int free = player.getInventory().freeSlots();
			if(transferAmount > free) {
				player.getActionSender()
						.sendMessage(
								"You don't have enough inventory space to withdraw that many."); // this
				// is
				// the
				// real
				// message
				transferAmount = free;
			}
		}
		// now add it to inv
		if(player.getInventory().add(new Item(newId, transferAmount))) {
			// all items in the bank are stacked, makes it very easy!
			int newAmount = item.getCount() - transferAmount;
			if(newAmount <= 0) {
				player.getBoB().set(slot, null);
			} else {
				player.getBoB().set(slot, new Item(item.getId(), newAmount));
			}
		} else {
			player.getActionSender()
					.sendMessage(
							"You don't have enough inventory space to withdraw that many."); // this
			// is
			// the
			// real
			// message
		}
	}

	/**
	 * Deposits an item.
	 *
	 * @param player The player.
	 * @param slot   The slot in the player's inventory.
	 * @param id     The item id.
	 * @param amount The amount of the item to deposit.
	 */
	public static void deposit(Player player, int slot, int id, int amount) {
		deposit(player, slot, id, amount, player.getInventory());
	}

	public static void deposit(Player player, int slot, int id, int amount, Container container) {
		if(player.cE.summonedNpc == null){
			PushMessage.pushStaffMessage("Trying to deposit into BOB with no summoned npc", player);
			return;
		}
        if(FightPits.inPits(player) || FightPits.inGame(player) || FightPits.inPitsFightArea(player.getPosition().getX(), player.getPosition().getY()))
            return;
		if(slot < 0 || slot > container.capacity() || id < 0 || id > ItemDefinition.MAX_ID)
			return;
		if(Food.get(id) == null && !ItemSpawning.canSpawn(id)) {
			player.getActionSender().sendMessage("You cannot store this item.");
			return;
		}
		boolean inventoryFiringEvents = container.isFiringEvents();
		container.setFiringEvents(false);
		try {
			Item item = container.get(slot);
			if(item == null) {
				return; // invalid packet, or client out of sync
			}
			if(item.getId() != id || item.getDefinition().isNoted()) {
				return; // invalid packet, or client out of sync
			}
			int transferAmount = container.getCount(id);
			if(transferAmount >= amount) {
				transferAmount = amount;
			} else if(transferAmount == 0) {
				return; // invalid packet, or client out of sync
			}
			boolean noted = item.getDefinition().isNoted();
			if(item.getDefinition().isStackable() || noted) {
				int bankedId = noted ? item.getDefinition().getNormalId()
						: item.getId();
				if(player.getBoB().freeSlots() < 1
						&& player.getBoB().getById(bankedId) == null) {
					player.getActionSender()
							.sendMessage(
									"You don't have enough space in your summon's pouch."); // this
					// is
					// the
					// real
					// message
				}
				// we only need to remove from one stack
				int newInventoryAmount = item.getCount() - transferAmount;
				Item newItem;
				if(newInventoryAmount <= 0) {
					newItem = null;
				} else {
					newItem = new Item(item.getId(), newInventoryAmount);
				}
				if(! player.getBoB().add(new Item(bankedId, transferAmount))) {
					player.getActionSender()
							.sendMessage(
									"You don't have enough space in your summon's pouch."); // this
					// is
					// the
					// real
					// message
				} else {
					container.set(slot, newItem);
					container.fireItemsChanged();
					player.getBoB().fireItemsChanged();
				}
			} else {
				if(player.getBoB().freeSlots() < transferAmount) {
					player.getActionSender()
							.sendMessage(
									"You don't have enough space in your summon's pouch."); // this
					// is
					// the
					// real
					// message
				}
				if(! player.getBoB().add(
						new Item(item.getId(), transferAmount))) {
					player.getActionSender()
							.sendMessage(
									"You don't have enough space in your summon's pouch."); // this
					// is
					// the
					// real
					// message
				} else {
					// we need to remove multiple items
					for(int i = 0; i < transferAmount; i++) {
						if(i == 0) {
							container.set(slot, null);
						} else {
							container.set(container.getSlotById(item.getId()),
									null);
						}
					}
					container.fireItemsChanged();
				}
			}
		} finally {
			container.setFiringEvents(inventoryFiringEvents);
		}
	}

}
