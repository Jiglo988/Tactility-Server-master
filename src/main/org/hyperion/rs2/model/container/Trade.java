package org.hyperion.rs2.model.container;

import org.hyperion.Server;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.ItemsTradeable;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.TradeChecker;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.rs2.util.PushMessage;

/**
 * Trading utility class.
 *
 * @author Martin
 */
public class Trade {

	/**
	 * The trade limit size.
	 */
	public static final int SIZE = 28;

	/**
	 * The player inventory interface.
	 */
	public static final int PLAYER_INVENTORY_INTERFACE = 3322;

	/**
	 * The shop inventory interface.
	 */
	public static final int TRADE_INVENTORY_INTERFACE = 3415;


	/**
	 * @param player
	 * @param player2
	 */
	public static void open(Player player, Player player2) {
		if(Server.isUpdating())
			return;
		if(player.getSkills().getLevel(3) <= 1) {
			player.sendMessage("You cannot trade players when you have low health.");
			return;
		}
        if(player.getGameMode() != player2.getGameMode()) {
            player.sendMessage("You cannot trade players in different game modes");
            return;
        }

        if(player.isNewlyCreated() && player.hardMode() || player2.isNewlyCreated() && player2.hardMode()) {
            player.sendMessage("You or your partner is too new to trade");
            return;
        }

		if(player.getName().equalsIgnoreCase(player2.getName())) {
			System.out.println("Trading yourself is not good.");
			return;
		}
		if(! player.getPosition().isWithinDistance(player.getPosition(), 3)) {
			player.getActionSender().sendMessage("You are too far away to open a trade.");
			return;
		}
		if(Server.isUpdating()) {
			player.getActionSender().sendMessage("You can't trade during an update.");
			return;
		}
		
		if(FightPits.inGame(player) || FightPits.inGame(player2)) {
			return;
		}
		//World.getAbuseHandler().cacheMessage(player,player.getName()+": opened a trade with: "+player2.getName());
		//World.getAbuseHandler().cacheMessage(player2,player2.getName()+": opened a trade with: "+player.getName());
        player.openingTrade = true;
		player2.openingTrade = true;
		player.setTradeWith(player2);
		player2.setTradeWith(player);
		player.tradeWith2 = null;
		player2.tradeWith2 = null;
		player.tradeAccept1 = false;
		player.tradeAccept2 = false;
		player.onConfirmScreen = false;
		player2.onConfirmScreen = false;
		player2.tradeAccept1 = false;
		player2.tradeAccept2 = false;
		player.currentInterfaceStatus = 1;
		player2.currentInterfaceStatus = 1;
		player.getActionSender().sendInterfaceInventory(3323, 3321);
		player.getActionSender().sendUpdateItems(3322, player.getInventory().toArray());
		player.getActionSender().sendUpdateItems(3415, player.getTrade().toArray());
		player.getActionSender().sendUpdateItems(3416, player.getTrade().toArray());
		player2.getActionSender().sendUpdateItems(3322, player.getInventory().toArray());
		player2.getActionSender().sendUpdateItems(3415, player.getTrade().toArray());
		player2.getActionSender().sendUpdateItems(3416, player.getTrade().toArray());
		player.getInterfaceState().addListener(player.getTrade(), new InterfaceContainerListener(player, TRADE_INVENTORY_INTERFACE));
		player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, PLAYER_INVENTORY_INTERFACE));
		player2.getActionSender().sendInterfaceInventory(3323, 3321);
		player2.getInterfaceState().addListener(player2.getTrade(), new InterfaceContainerListener(player2, TRADE_INVENTORY_INTERFACE));
		player2.getInterfaceState().addListener(player2.getInventory(), new InterfaceContainerListener(player2, PLAYER_INVENTORY_INTERFACE));
		player.getActionSender().sendString(19000, player2.getSafeDisplayName());
		player2.getActionSender().sendString(19000, player.getSafeDisplayName());
		player.getActionSender().sendString(19001, "has " + player2.getInventory().freeSlots() + " free");
		player2.getActionSender().sendString(19001, "has " + player.getInventory().freeSlots() + " free");
		player.getActionSender().sendString(3535, "Are you sure you want to make this trade?");
		player2.getActionSender().sendString(3535, "Are you sure you want to make this trade?");
		player.getActionSender().sendString(3431, "");
		player2.getActionSender().sendString(3431, "");
		player2.getActionSender().sendString(3417, "Trading with: " + player.getSafeDisplayName());
		player.getActionSender().sendString(3417, "Trading with: " + player2.getSafeDisplayName());
		player.openingTrade = false;
		player2.openingTrade = false;
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
		if(player.tradeAccept1 && (player.getTrader() == null || player.getTrader().tradeAccept1))
			return;
		Item item = player.getTrade().get(slot);
		if(item == null) {
			return; // invalid packet, or client out of sync
		}
		if(item.getId() != id) {
			return; // invalid packet, or client out of sync
		}
		player.openingTrade = false;
		player.getTrader().openingTrade = false;
		int transferAmount = item.getCount();
		if(transferAmount >= amount) {
			transferAmount = amount;
		} else if(transferAmount == 0) {
			return; // invalid packet, or client out of sync
		}
		int newId = item.getId(); // TODO deal with withdraw as notes!
		ItemDefinition def = ItemDefinition.forId(newId);
		if(def.isStackable()) {
			if(player.getInventory().freeSlots() <= 0 && player.getInventory().getById(newId) == null) {
				player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
			}
		} else {
			int free = player.getInventory().freeSlots();
			if(transferAmount > free) {
				player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
				transferAmount = free;
			}
		}
		// now add it to inv
		if(player.getInventory().add(new Item(newId, transferAmount))) {
			// all items in the bank are stacked, makes it very easy!
			int newAmount = item.getCount() - transferAmount;
			if(newAmount <= 0) {
				player.getTrade().set(slot, null);
			} else {
				player.getTrade().set(slot, new Item(item.getId(), newAmount));
			}
			player.getTrader().getActionSender().sendUpdateItems(3416, player.getTrade().toArray());
			player.tradeAccept1 = false;
			player.tradeAccept2 = false;
			player.getTrader().tradeAccept1 = false;
			player.getTrader().tradeAccept2 = false;
			//World.getAbuseHandler().cacheMessage(player,player.getName()+": removed: "+newId+":"+transferAmount+" from trade.");
			player.getActionSender().sendString(3431, "");
			player.getTrader().getActionSender().sendString(3431, "");
			player.getActionSender().sendString(19001, "has " + player.getTrader().getInventory().freeSlots() + " free");
			player.getTrader().getActionSender().sendString(19001, "has " + player.getInventory().freeSlots() + " free");
		} else {
			player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
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
		Player trader = player.getTrader();
/*
        if(player.getExtraData().getBoolean("cantdoshit")) {
            player.sendMessage("Please PM a moderator as your account is locked for its own safety!");
            return;
        }
        */
		if(trader == null)
			return;
        if(player.getExtraData().getBoolean("needpasschange"))
            return;
		if(FightPits.inPits(player) || FightPits.inPits(player.getTrader())) //trying to smuggle
			return;
		if(player.getTrader().getTrader() != null)
		if(!player.getTrader().getTrader().getName().toLowerCase().equals(player.getName().toLowerCase())) {
			player.sendMessage("Anti-bug, stopped");
			PushMessage.pushStaffMessage("is trying to do a 3-way trade", player);
			return;
		}
		if(player.tradeAccept1 && trader.tradeAccept1)
			return;
        if((player.getDungeoneering().inDungeon() && trader.getDungeoneering().inDungeon())) {
            for(final Item item : player.getDungeoneering().getBinds()) {
                if(item.getId() == id) {
                    player.sendMessage("You cannot trade a binded-type item");
                    return;
                }
            }
        }
        else if(! ItemsTradeable.isTradeable2(id, player.getGameMode())) {
			player.getActionSender().sendMessage("You cannot trade this item.");
			return;
		}
        if((player.hardMode()) && (player.getUID() == player.getTrader().getUID() || player.getShortIP().equalsIgnoreCase(player.getTrader().getShortIP())))
        {
            player.sendMessage("You cannot trade with this person");
            return;
        }


		boolean inventoryFiringEvents = player.getInventory().isFiringEvents();
		player.getInventory().setFiringEvents(false);
		try {
			Item item = player.getInventory().get(slot);
			if(item == null) {
				return; // invalid packet, or client out of sync
			}
			if(item.getId() != id) {
				return; // invalid packet, or client out of sync
			}
			player.openingTrade = false;
			trader.openingTrade = false;
			int transferAmount = item.getCount();
			if(! item.getDefinition().isStackable())
				transferAmount = player.getInventory().getCount(id);
			if(transferAmount >= amount) {
				transferAmount = amount;
			} else if(transferAmount == 0) {
				return; // invalid packet, or client out of sync
			}
			boolean noted = item.getDefinition().isNoted();
			if(item.getDefinition().isStackable() || noted) {
				int bankedId = item.getId();
				if(player.getTrade().freeSlots() < 1 && player.getTrade().getById(bankedId) == null) {
					player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
				}
				// we only need to remove from one stack
				int newInventoryAmount = item.getCount() - transferAmount;
				Item newItem;
				if(newInventoryAmount <= 0) {
					newItem = null;
				} else {
					newItem = new Item(item.getId(), newInventoryAmount);
				}
				if(! player.getTrade().add(new Item(bankedId, transferAmount))) {
					player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
				} else {
					player.getInventory().set(slot, newItem);
					player.getInventory().fireItemsChanged();
					player.getTrade().fireItemsChanged();
				}
			} else {
				if(player.getTrade().freeSlots() < transferAmount) {
					player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
				}
				if(! player.getTrade().add(new Item(item.getId(), transferAmount))) {
					player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
				} else {
					// we need to remove multiple items
					for(int i = 0; i < transferAmount; i++) {
	                    /* if(i == 0) {
							player.getInventory().set(slot, null);
						} else { */
						player.getInventory().set(player.getInventory().getSlotById(item.getId()), null);
						// }
					}
					player.getInventory().fireItemsChanged();
				}
			}
		} finally {
			//World.getAbuseHandler().cacheMessage(player,player.getName()+": added: "+id+":"+amount+" to trade.");
			player.getInventory().setFiringEvents(inventoryFiringEvents);
			if(trader == null) {
				player.tradeAccept1 = false;
				player.tradeAccept2 = false;
				return;
			}
			trader.getActionSender().sendUpdateItems(3416, player.getTrade().toArray());
			player.tradeAccept1 = false;
			player.tradeAccept2 = false;
			trader.tradeAccept1 = false;
			trader.tradeAccept2 = false;
			player.getActionSender().sendString(3431, "");
			trader.getActionSender().sendString(3431, "");
			player.getActionSender().sendString(19001, "has " + trader.getInventory().freeSlots() + " free");
			trader.getActionSender().sendString(19001, "has " + player.getInventory().freeSlots() + " free");
		}
	}

	public static String listConfirmScreen(Item[] items) {
		String sendTrade = "Absolutely nothing!";
		String sendAmount = "";
		int count = 0;
		for(Item item : items) {
			if(item == null)
				continue;
			if(item.getId() > 0) {
				if((item.getCount() >= 1000) && (item.getCount() < 1000000)) {
					sendAmount = "@cya@" + (item.getCount() / 1000) + "K @whi@("
							+ NameUtils.formatInt(item.getCount()) + ")";
				} else if(item.getCount() >= 1000000) {
					sendAmount = "@gre@" + (item.getCount() / 1000000)
							+ " million @whi@(" + NameUtils.formatInt(item.getCount())
							+ ")";
				} else {
					sendAmount = "" + NameUtils.formatInt(item.getCount());
				}
				if(count == 0) {
					sendTrade = "";
					count = 2;
				}
				if(count == 1) {
					sendTrade = sendTrade + "\\n" + item.getDefinition().getName();
				} else if(count == 2) {
					sendTrade = sendTrade + " " + item.getDefinition().getName();
					count = 0;
				}
				if(item.getDefinition().isStackable()) {
					sendTrade = sendTrade + " x " + sendAmount;
				}
				sendTrade = sendTrade + "     ";
				count++;
			}
		}
		return sendTrade;
	}

	public static void confirmScreen(Player player) {
		String sendTrade1 = listConfirmScreen(player.getTrade().toArray());
		String sendTrade2 = listConfirmScreen(player.getTrader().getTrade().toArray());
		player.getActionSender().sendString(3557, sendTrade1);
		player.getTrader().getActionSender().sendString(3557, sendTrade2);
		player.getActionSender().sendString(3558, sendTrade2);
		player.getTrader().getActionSender().sendString(3558, sendTrade1);
		player.getActionSender().sendInterfaceInventory(3443, 3213);
		player.getActionSender().sendUpdateItems(3214, player.getInventory().toArray());
		player.getTrader().getActionSender().sendInterfaceInventory(3443, 3213);
		player.getTrader().getActionSender().sendUpdateItems(3214, player.getTrader().getInventory().toArray());
		player.getTrader().getActionSender().sendString(3451, player.getSafeDisplayName());
		player.getActionSender().sendString(3451, player.getTrader().getSafeDisplayName());
		player.onConfirmScreen = true;
		player.getTrader().onConfirmScreen = true;
	}

	public static void finishTrade(Player player) {
        if (player != null && player.getTrader() != null) {

		if(player.getTrader().getTrader() != player) {
			player.sendMessage("Anti-bug, stopped");
			PushMessage.pushStaffMessage("is trying to do a 3-way trade", player);
			return;
		}
		if(Server.isUpdating()) {
			player.getActionSender().sendMessage("You can't trade during an update.");
			return;
		}
		if(player.tradeAccept1 && player.getTrader().tradeAccept1 && ! player.tradeAccept2 && ! player.getTrader().tradeAccept2) {
			//we open the confirm screen.
			confirmScreen(player);
		}
		if(! player.tradeAccept1 || !player.getTrader().tradeAccept1 || ! player.tradeAccept2 || ! player.getTrader().tradeAccept2) {
			//do nothing.
			int id = 3535;
			if(! player.onConfirmScreen && !player.getTrader().onConfirmScreen) {
                id = 3431;
            }
			if((player.tradeAccept1 && ! player.getTrader().tradeAccept1) || (player.tradeAccept2 && ! player.getTrader().tradeAccept2)) {
				player.getActionSender().sendString(id, "Waiting on the other player.");
				player.getTrader().getActionSender().sendString(id, "Other player has accepted.");
			} else if((! player.tradeAccept1 && player.getTrader().tradeAccept1) || (! player.tradeAccept2 && player.getTrader().tradeAccept2)) {
				player.getTrader().getActionSender().sendString(id, "Waiting on the other player.");
				player.getActionSender().sendString(id, "Other player has accepted.");
			}
			return;
		}
		if(player.getInventory().freeSlots() < player.getTrader().getTrade().size()) {
			player.getActionSender().sendMessage("You don't have enough space to make this trade.");
			player.getTrader().getActionSender().sendMessage("The other player doesn't have enough space to make this trade.");
			return;
		}
		if(player.getTrader().getInventory().freeSlots() < player.getTrade().size()) {
			player.getTrader().getActionSender().sendMessage("You don't have enough space to make this trade.");
			player.getActionSender().sendMessage("The other player doesn't have enough space to make this trade.");
			return;
		}
		if(player.getTrader().getName().equalsIgnoreCase(player.getName())) {
			System.out.println("Trading yourself is not good.");
			return;
		}
		/*if(player.getSkills().getTotalLevel() > 100){
        	TextUtils.writeToFile("./logs/accounts/"+player.getName(),(new Date())+" Trade Confirmed: "+player.getTrader().getName());
		}
	    if(player.getTrader().getSkills().getTotalLevel() > 100){
	    	TextUtils.writeToFile("./logs/accounts/"+player.getTrader().getName(),(new Date())+" Trade Confirmed: "+player.getName());
	    }*/
		//World.getAbuseHandler().cacheMessage(player,player.getName()+": completed a trade with: "+player.getTrader().getName());
		new TradeChecker(player, player.getTrader());
            /*player.getLogManager().add(
                    LogEntry.trade(
                            player.getName(),
                            player.getTrader().getName(),
                            player.getTrade().toArray(),
                            player.getTrader().getTrade().toArray()
                    )
            );
            player.getTrader().getLogManager().add(
					LogEntry.trade(
							player.getName(),
							player.getTrader().getName(),
							player.getTrade().toArray(),
							player.getTrader().getTrade().toArray()
					)
			);*/
			player.getExpectedValues().trade(player.getTrader().getTrade().getItems(), player.getTrade().getItems());
			player.getTrader().getExpectedValues().trade(player.getTrade().getItems(), player.getTrader().getTrade().getItems());

		Container.transfer(player.getTrader().getTrade(), player.getInventory());
		Container.transfer(player.getTrade(), player.getTrader().getInventory());
		//World.getWorldLoader().savePlayer(player, "trade");
		PlayerSaving.save(player);
		//World.getWorldLoader().savePlayer(player.getTrader(), "trade");
		PlayerSaving.save(player.getTrader());
		player.getActionSender().removeAllInterfaces();
		player.getTrader().getActionSender().removeAllInterfaces();
		player.getTrader().tradeAccept2 = false;
		player.tradeAccept2 = false;
		//player.getLogging().log("Finished trade with: " + player.getTrader().getName());
		player.getTrader().getLogging().log("Finished trade with: " + player.getName());
            player.sendf("Trade accepted", player.getTrader().getSafeDisplayName());
            player.getTrader().sendf("Trade accepted", player.getSafeDisplayName());
            declineTrade(player);
	}
    }

	public static synchronized void declineTrade(Player player) {
		if(player.getTrader() != null) {
			//World.getAbuseHandler().cacheMessage(player,player.getName()+": declined a trade with: "+player.getTrader().getName());
			Container.transfer(player.getTrader().getTrade(), player.getTrader().getInventory());
			if(player.duelAttackable == 0) {
				Container.transfer(player.getTrader().getDuel(), player.getTrader().getInventory());
				player.getTrader().duelAttackable = 0;
				player.getTrader().duelWith2 = null;
				player.getTrader().setTradeWith(null);
			}
			player.getTrader().getActionSender().removeAllInterfaces();
			player.getActionSender().removeAllInterfaces();
		}
		if(player.duelAttackable == 0) {
			player.setTradeWith(null);
			Container.transfer(player.getTrade(), player.getInventory());
		}
	}

}
