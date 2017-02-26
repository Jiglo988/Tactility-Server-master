package org.hyperion.rs2.model;

import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.EP.EPDrops;
import org.hyperion.rs2.model.content.minigame.DangerousPK;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc2.Food;
import org.hyperion.rs2.model.content.misc2.NewGameMode;
import org.hyperion.rs2.model.shops.DonatorShop;
import org.hyperion.util.Misc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author SaosinHax
 */
public class DeathDrops {
	
	public static final List<Integer> FOOD = Arrays.asList(391, 15272, 385);
	
	private static boolean dontDrop(Player player) {
		return dontDropRank(player.getPlayerRank()) || player.getPosition().inFunPk();
	}
	private static boolean dontDropRank(long l) {
		return Rank.getPrimaryRank(l).ordinal() >= Rank.DEVELOPER.ordinal();
	}
	/**
	 * Drops player's items on normal death
	 */
	public static void dropsAtDeath(Player player, Player killer) {
		if(killer == null || player == null)
			return;
		if(dontDrop(player) || dontDrop(killer))
			return;

		/**
		 * Adds EP Drop.
		 */
		Item epItems = EPDrops.getEPItem(killer.EP);
		if(epItems != null) {
			killer.removeEP();
			GlobalItemManager.newDropItem(killer, new GlobalItem(killer, player.getPosition(), epItems));
		}

        List<Item> droppingItems = dropItems(player, DangerousPK.inDangerousPK(player));
		/**
		 * Drops the items for the killer
		 */
		for(Item item : droppingItems) {
            if(player.isNewlyCreated() && player.hardMode()) {
                player.sendMessage("You don't get any loot from a new player");
                break;
            }
            if(killer.getGameMode() <= player.getGameMode())
			    GlobalItemManager.newDropItem(killer, new GlobalItem(killer, player.getPosition(), item));
            else {
                int price = (int)(NewGameMode.getUnitPrice(item) * NewGameMode.SELL_REDUCTION);
                if(price > 1)
                    GlobalItemManager.newDropItem(killer, new GlobalItem(killer, player.getPosition(),
                        Item.create(995, price)));
            }
        }

        if(killer.hardMode() && !player.isNewlyCreated()) {
            GlobalItemManager.newDropItem(killer, new GlobalItem(killer, player.getPosition(), Item.create(995, 200_000)));
        }

		final Item[] dropped = droppingItems.toArray(new Item[droppingItems.size()]);

		player.getExpectedValues().deathDrop(dropped);
        //player.getLogManager().add(LogEntry.death(player, killer, dropped));
		//killer.getLogManager().add(LogEntry.kill(killer, player, dropped));

    }

    public static final List<Item> dropItems(Player player, boolean loseAll) {
        /**
         * Resets death variables - which slots are being protected
         * Sets which items are being kept, deletes them
         */
        player.resetDeathItemsVariables();
        List<Item> keepItems = itemsKeptOnDeath(player, false, false);

        //If in dangerous pk set shit to null again
        if(loseAll)
            player.resetDeathItemsVariables();
        /**
         * List that stores items to be dropped later
         */
        List<Item> droppingItems = new LinkedList<>();

        /**
         * {@link #processContainer(Container container, List<Item> original, boolean inv, Player player)}
         */
        droppingItems = processContainer(player.getInventory(), droppingItems, true, player);
        droppingItems = processContainer(player.getEquipment(), droppingItems, false, player);
        return droppingItems;
    }
	/**
	 * Adds unspawnables from equip/inv - only takes items that are unspawnable
	 */
	public static List<Item> processContainer(Container container, List<Item> originalDrops, boolean inv, Player player) {
		for(int slot = 0; slot < container.capacity(); slot++) {
			if((inv && player.invSlot[slot])) 
				continue;
			if((!inv && player.equipSlot[slot]))
				continue;
			Item item = container.get(slot);
			if(toDrop(item, player.getGameMode())) {
				if(ItemsTradeable.isTradeable(item.getId()))
					originalDrops.add(item);
				container.remove(slot, item);
			}	
		}
		return originalDrops;
	}
	
	private static boolean toDrop(Item item, final int gameMode) {
		if(item == null)
			return false;
		if(ItemsTradeable.isTradeable(item.getId())) {
			return !(ItemSpawning.canSpawn(item.getId()) && Food.get(item.getId()) == null && gameMode == 0);
		} else {
			switch(item.getId()) {
                case 13889:
                case 13895:
                case 19669:
                    return Misc.random(3) == 0;
                case 20000:
                case 19713://nex helms
                case 19716:
                case 19719:
                case 19817:
                case 19816: //Glacor boots
                case 19815:
                case 16887: // sagittarian shortbow
                case 16337: // sagittarian longbow
                case 17193: //sagittarian gear
                case 17339:
                case 17215:
                case 17317:
                case 18349: //chaotic weapons
                case 18351:
                case 18353:
                case 18355:
                case 18357:
                case 17660:
                    return Misc.random(9) == 0;
                case 19780: //krazi korazi!
                    return true;
                //return Misc.random(2) == 0;
			}
		}
		return false;
	}

	public static List<Item> itemsKeptOnDeath(Player player, boolean delete, boolean interfaceUse) {
		boolean[] invSlot = player.invSlot.clone();
		boolean[] equipSlot = player.equipSlot.clone();
		List<Item> keepItems = new LinkedList<Item>();
		int keeping = player.getPosition().inFunPk() ?
			player.getInventory().size() + player.getEquipment().size() :
			((player.isSkulled() ? 0 : 3) + (player.getPrayers().isProtectingItem() ? 1 : 0));
		for(int i = 0; i < keeping; i++) {
			Item item = keepItem(player, i, delete);
			if(item != null)
				keepItems.add(item);
		}
		if(interfaceUse) {
			for(int i = 0; i < player.invSlot.length; i++)
				player.invSlot[i] = invSlot[i];
			for(int i = 0; i < player.equipSlot.length; i++)
				player.equipSlot[i] = equipSlot[i];
		}
		return keepItems;
	}
	
	@Deprecated
	public static void dropAllItems(Player player, Player killer) {
		//System.out.println("Dropping items for player:" + player.getName());
		if(killer == null || player == null)
			return;
		if(Rank.getPrimaryRank(player).ordinal() >= Rank.ADMINISTRATOR.ordinal())
			return;

		if(Rank.getPrimaryRank(killer).ordinal() >= Rank.ADMINISTRATOR.ordinal())
			return;
		if(killer.getPosition().inFunPk() || player.getPosition().inFunPk() || (player.getPosition().getX() == 3221 && player.getPosition().getY() == 3218)) {
            return;
        }
		player.resetDeathItemsVariables();
		List<Item> keepItems = itemsKeptOnDeath(player, true, false);
		/**
		 * Use one list to avoid repeating code.
		 */
		LinkedList<Item> droppingItems = new LinkedList<Item>();
		for(Item item : player.getEquipment().toArray()) {
			droppingItems.add(item);
		}
		for(Item item : player.getInventory().toArray()) {
			droppingItems.add(item);
		}
		/**
		 * Clear inventory and equipment.
		 */
		player.getInventory().clear();
		player.getEquipment().clear();
		/**
		 * Adds EP Drop.
		 */
		if(killer != null && ! killer.loggedOut) {
			if(Rank.hasAbility(killer, Rank.OWNER))
				System.out.println("Killer: " + killer.getName());
			Item EPItem = EPDrops.getEPItem(killer.EP);
			if(EPItem != null) {
				killer.removeEP();
				if(Rank.hasAbility(killer, Rank.OWNER))
					System.out.println("Ep item: " + ItemDefinition.forId(EPItem.getId()).getName());
				GlobalItemManager.newDropItem(killer, new GlobalItem(killer, player.getPosition(), EPItem));
			}
		}
		LinkedList<Item> delayedDrops = new LinkedList<Item>();
		/**
		 * Clean Equipment.
		 */
		for(Item dropItem : droppingItems) {
			if(dropItem == null)
				continue;
			if(ItemsTradeable.isTradeable(dropItem.getId())) {
				if(ItemSpawning.allowedMessage(dropItem.getId()).length() <= 1)
					delayedDrops.add(dropItem);
				else
					GlobalItemManager.newDropItem(killer, new GlobalItem(killer, player.getPosition(), dropItem));
			} else {
				/**
				 * Following id's should not be dropped.
				 */
				switch(dropItem.getId()) {
					case 20000:
					case 19713://nex helms
					case 19716:
					case 19719:
					case 19817:
					case 19816: //Glacor boots
					case 19815:
					case 16887: // sagittarian shortbow
					case 16337: // sagittarian longbow
					case 17193: //sagittarian gear
					case 17339:
					case 17215:
					case 17317:
					case 18349: //chaotic weapons
					case 18351:
					case 18353:
					case 18355:
					case 18357:
						if(Misc.random(4) == 0)
							continue;
						break;
					case 19780:
					case 10858:
						continue;
				}
				keepItems.add(dropItem);
				//GlobalItemManager.newDropItem(player, new GlobalItem(player,player.getLocation(),player.getEquipment().get(i)));
			}
		}
		for(Item delayedItem : delayedDrops) {
			GlobalItemManager.newDropItem(killer, new GlobalItem(killer, player.getPosition(), delayedItem));
		}
		for(Item keepItem : keepItems) {
			if(keepItem != null)
				player.getInventory().add(keepItem);
		}
	}

	public static Item keepItem2(Player player, int keepItem, boolean deleteItem) {
		int capacity = player.getInventory().capacity() + player.getEquipment().capacity();
		int itemId = - 1, itemSlot = 0, itemCount = 1, itemContainer = 0;
		for(int i = 0; i < capacity; i++) {
			boolean slotInInv = i < player.getInventory().capacity();
			int currentSlot = slotInInv ? i : i - player.getInventory().capacity();
			Item item = (slotInInv ? player.getInventory() : player.getEquipment()).get(currentSlot);
			if(item != null && item.comparePriceWith(itemId) != itemId) {
				itemId = item.getId();
				itemContainer = slotInInv ? 0 : 1;
				itemSlot = currentSlot;
				itemCount = item.getCount();
			}
		}
		if(itemId != - 1) {
			(itemContainer == 0 ? player.invSlot : player.equipSlot)[itemSlot] = itemCount > 1;
			if(deleteItem) {
				if(itemContainer == 0)
					ContentEntity.deleteItem(player, itemId, itemSlot, 1);
				else
					player.getEquipment().set(itemSlot, null);
			}
			return new Item(player.itemKeptId[keepItem] = itemId, 1);
		}
		return null;
	}
	public static int calculateAlchValue(final int gameMode ,int id) {
        ItemDefinition def;
        if((def = ItemDefinition.forId(id)) != null && def.isNoted())
            id = def.getParentId();
        def = ItemDefinition.forId(id);
		int dpVal = DonatorShop.getPrice(id);
		int inventoryItemValue = 0;
        if(gameMode == 1)
            inventoryItemValue = (int)NewGameMode.getUnitPrice(id);
		else if(dpVal > 100)
			inventoryItemValue = dpVal * 150000;
		else {
			inventoryItemValue = ItemSpawning.canSpawn(id) ? -1 :(int) Math.floor(def == null ? -1 : def.getHighAlcValue());
        }
        if(id == 5020)
            inventoryItemValue = -5;
		return inventoryItemValue;
	}
	public static Item keepItem(Player player, int keepItem, boolean deleteItem) {
		int value = 0;
		int item = - 1;
		int slotId = 0;
		boolean itemInInventory = false, itemStackZero = true;
		for(int i = 0; i < player.getInventory().capacity(); i++) {
			if(player.getInventory().get(i) != null) {
				int dpVal = DonatorShop.getPrice(player.getInventory().get(i).getId());
				int inventoryItemValue = calculateAlchValue(player.getGameMode() ,player.getInventory().get(i).getId());
				if(inventoryItemValue > value && (! player.invSlot[i])) {
					value = inventoryItemValue;
					item = player.getInventory().get(i).getId();
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for(int i1 = 0; i1 < player.getEquipment().capacity(); i1++) {
			if(player.getEquipment().get(i1) != null) {
				int dpValue = (int)Math.floor(DonatorShop.getPrice(player.getEquipment().get(i1).getId()));
				int equipmentItemValue = calculateAlchValue(player.getGameMode() ,player.getEquipment().get(i1).getId());

				if(equipmentItemValue > value && (! player.equipSlot[i1])) {
					value = equipmentItemValue;
					item = player.getEquipment().get(i1).getId();
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if(itemInInventory) {
			if(itemStackZero)
				player.invSlot[slotId] = true;
			if(deleteItem) {
				//ContentEntity.deleteItem(player, player.getInventory().get(slotId).getId(), slotId);
				ContentEntity.deleteItem(player, item, slotId, 1);
			}
		} else {
			if(itemStackZero)
				player.equipSlot[slotId] = true;
			if(deleteItem) {
				player.getEquipment().set(slotId, null);
			}
		}
		if(keepItem < player.itemKeptId.length)
            player.itemKeptId[keepItem] = item;
		if(item == - 1)
			return null;
		else
			return itemInInventory ? player.getInventory().get(slotId) : player.getEquipment().get(slotId);
	}
}
