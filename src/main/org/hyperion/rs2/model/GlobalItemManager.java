package org.hyperion.rs2.model;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.content.ContentEntity;

import java.util.LinkedList;
import java.util.List;

public class GlobalItemManager {

	public static void init() {
		World.submit(new Task(1000, "globalitems") {
			@Override
			public void execute() {
				process();
			}
		});
	}

	public static final List<GlobalItem> GLOBAL_ITEMS = new LinkedList<GlobalItem>();

	public static void displayItems(Player player) {
		synchronized(GLOBAL_ITEMS) {
			for(GlobalItem globalItem : GLOBAL_ITEMS) {
				if((! globalItem.itemHidden || globalItem.owner == player) && globalItem.getItem() != null && player.getPosition().isWithinDistance(globalItem.getPosition(), 64)) {
					//display the item
					if(! ItemsTradeable.isTradeable(globalItem.getItem().getId()) && globalItem.owner != player) {
						continue;
					}
					//System.out.println("displaying items " + globalItem.getItem().getDefinition().getName());
					player.getActionSender().createGlobalItem(globalItem.getPosition(), globalItem.getItem());
				}
			}
		}
	}

	public static GlobalItem getItem(int id, Position loc) {
		synchronized(GLOBAL_ITEMS) {
			for(GlobalItem g : GLOBAL_ITEMS) {
				//System.out.println(""+g.getItem().getId()+", "+g.getLocation().getX()+", "+g.getLocation().getY());
				if(g.getPosition().distance(loc) == 0) {
					if(g.getItem().getId() == id) {
						return g;
					}
				}
			}
		}
		return null;
	}

	public static void newDropItem(Player player, GlobalItem globalItem) {
		GlobalItem onGround = getItem(globalItem.getItem().getId(), globalItem.getPosition());
		if(onGround != null && onGround.getItem().getDefinition().isStackable()) {
			int count = onGround.getItem().getCount() + globalItem.getItem().getCount();
			onGround.setNewItem(new Item(onGround.getItem().getId(), count));
		} else {
			synchronized(GLOBAL_ITEMS) {
				GLOBAL_ITEMS.add(globalItem);
			}
			if(player.getPosition().isWithinDistance(globalItem.getPosition(), 64))
				player.getActionSender().createGlobalItem(globalItem.getPosition(), globalItem.getItem());
		}
	}

	public static void addToItems(GlobalItem globalItem) {
		synchronized(GLOBAL_ITEMS) {
			GLOBAL_ITEMS.add(globalItem);
		}
	}

	public static void dropItem(Player player, int itemId, int slot) {
		Item item = player.getInventory().get(slot);
		if(item == null || itemId != item.getId()) {
			player.getActionSender().sendMessage("Client is out of sync, Please logout.");
			return;
		}
		GlobalItem globalItem = new GlobalItem(player, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new Item(item.getId(), item.getCount()));
		synchronized(GLOBAL_ITEMS) {
			GLOBAL_ITEMS.add(globalItem);
		}
		player.getActionSender().createGlobalItem(globalItem.getPosition(), globalItem.getItem());
		player.getInventory().remove(item);
	}

	public static void pickupItem(Player player, int item, int x, int y) {
		if(ContentEntity.freeSlots(player) <= 0) {
			ContentEntity.sendMessage(player, "You have not got enough space to pick up that item");
			return;
		}
		GlobalItem globalItem = null;
		synchronized(GLOBAL_ITEMS) {
			for(GlobalItem g : GLOBAL_ITEMS) {
				//System.out.println(""+g.getItem().getId()+", "+g.getLocation().getX()+", "+g.getLocation().getY());
				if(g.getPosition().getX() == x && g.getPosition().getY() == y && g.getPosition().getZ() == player.getPosition().getZ()) {
					if(g.getItem().getId() == item) {
						globalItem = g;
						break;
					}
				}
			}
		}
		if(globalItem != null) {
			synchronized(GLOBAL_ITEMS) {
                if (globalItem.owner != null && globalItem.owner.getGameMode() != player.getGameMode()) {
                    player.sendMessage("You cannot pick up this item as it belongs to another game mode");
                    return;
                }
				player.getExpectedValues().pickupItem(globalItem.getItem());
				GLOBAL_ITEMS.remove(globalItem);
                if(item != 2422) {
                    //player.getLogManager().add(LogEntry.pickupItem(globalItem));
			        player.getInventory().add(globalItem.getItem());
                }
				if(globalItem.owner == null) {
					player.getAchievementTracker().pickupItemFromNpc(globalItem.getItem().getId(), globalItem.getItem().getCount());
				} else {
					player.getAchievementTracker().pickupItemFromPlayer(globalItem.getItem().getId(), globalItem.getItem().getCount());
				}
				removeItem(globalItem);
			    globalItem.destroy();
            }
		} else {
			//just remove it and pretend it was null
			player.getActionSender().removeGlobalItem(item, Position.create(x, y, 0));
			//player.getActionSender().sendMessage("That item doesn't exist.");
		}
	}

	public static void removeItem(GlobalItem globalItem) {
		for(Player p2 : World.getPlayers()) {
			if(p2.getPosition().isWithinDistance(globalItem.getPosition(), 64))
				p2.getActionSender().removeGlobalItem(globalItem.getItem(), globalItem.getPosition());
		}
	}

	public static void createItem(GlobalItem globalItem) {
		for(Player p2 : World.getPlayers()) {
			if(p2 == globalItem.owner)
				continue;
			if(p2.getPosition().isWithinDistance(globalItem.getPosition(), 64))
				p2.getActionSender().createGlobalItem(globalItem.getPosition(), globalItem.getItem());
		}
	}

	public static void process() {
		for(Object object : GLOBAL_ITEMS.toArray()) {
			GlobalItem gItem = (GlobalItem) object;
			if(System.currentTimeMillis() - gItem.createdTime >= 52000 && gItem.itemHidden) {
				if(! ItemsTradeable.isTradeable(gItem.getItem().getId())) {
					continue;
				}
				createItem(gItem);
				gItem.itemHidden = false;
			} else if(System.currentTimeMillis() - gItem.createdTime >= 120000) {
				removeItem(gItem);
				synchronized(GLOBAL_ITEMS) {
					GLOBAL_ITEMS.remove(gItem);
				}
				gItem.destroy();
			}
		}
	}
}