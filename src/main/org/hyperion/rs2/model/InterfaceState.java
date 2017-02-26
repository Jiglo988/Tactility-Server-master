package org.hyperion.rs2.model;

import org.hyperion.rs2.model.container.*;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.authentication.PlayerAuthenticationGenerator;
import org.hyperion.rs2.model.content.jge.itf.JGrandExchangeInterface;
import org.hyperion.rs2.model.content.misc2.RunePouch;
import org.hyperion.util.Misc;

import java.util.ArrayList;
import java.util.List;
//updatedderpusherpus

/**
 * Contains information about the state of interfaces open in the client.
 *
 * @author Graham Edgecombe
 */
public class InterfaceState {

	/**
	 * The current open interface.
	 */
	private int currentInterface = - 1;

	/**
	 * The active enter amount interface.
	 */
	private int enterAmountInterfaceId = - 1;

	/**
	 * The active enter amount id.
	 */
	private int enterAmountId;

	/**
	 * The active enter amount slot.
	 */
	private int enterAmountSlot;

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * A list of container listeners used on interfaces that have containers.
	 */
	private List<ContainerListener> containerListeners = new ArrayList<ContainerListener>();

	private int[] nextDialogueId = new int[5];

	private int openDialogueId;

	/**
	 * Creates the interface state.
	 */
	public InterfaceState(Player player) {
		this.player = player;
	}

	/**
	 * Checks if the specified interface is open.
	 *
	 * @param id The interface id.
	 * @return <code>true</code> if the interface is open, <code>false</code> if not.
	 */
	public boolean isInterfaceOpen(int id) {
		return currentInterface == id;
	}

	/**
	 * Gets the current open interface.
	 *
	 * @return The current open interface.
	 */
	public int getCurrentInterface() {
		return currentInterface;
	}

	/**
	 * Called when an interface is opened.
	 *
	 * @param id The interface.
	 */
	public void interfaceOpened(int id) {
	    /*if(currentInterface != -1) {
			interfaceClosed();
		}*/
		currentInterface = id;
	}

	/**
	 * Called when an interface is closed.
	 */
	public void interfaceClosed() {
		currentInterface = - 1;
		enterAmountInterfaceId = - 1;
		Trade.declineTrade(player);
		Duel.declineTrade(player);
		for(ContainerListener c : containerListeners) {
			player.getInventory().removeListener(c);
			player.getEquipment().removeListener(c);
			player.getBank().removeListener(c);
			player.getTrade().removeListener(c);
			player.getDuel().removeListener(c);
		}
	}

	public void resetContainers() {
		containerListeners.clear();
		player.getInventory().removeAllListeners();
		player.getEquipment().removeAllListeners();
		player.getBank().removeAllListeners();
		player.getTrade().removeAllListeners();
		player.getDuel().removeAllListeners();
	}

	public void resetInterfaces() {
		/*if(player.tutIsland != 10){
			TutorialQuest.walkDialogue(player);
		}*/
		/*else*/
		if(getCurrentInterface() > 10 || player.closeChatInterface || getOpenDialogueId() >= 0) {
			setOpenDialogueId(0);
			interfaceClosed();
			ContentEntity.removeAllWindows(player);
			player.closeChatInterface = false;
		}
	}

	/**
	 * Adds a listener to an interface that is closed when the inventory is closed.
	 *
	 * @param container         The container.
	 * @param containerListener The listener.
	 */
	public void addListener(Container container, ContainerListener containerListener) {
		container.addListener(containerListener);
		containerListeners.add(containerListener);
	}

	/**
	 * Called to open the enter amount interface.
	 *
	 * @param interfaceId The interface id.
	 * @param slot        The slot.
	 * @param id          The id.
	 */
	public void openEnterAmountInterface(int interfaceId, int slot, int id) {
		enterAmountInterfaceId = interfaceId;
		enterAmountSlot = slot;
		enterAmountId = id;
		player.getActionSender().sendEnterAmountInterface();
	}

    public void openEnterAmountInterface(int interfaceId, int id) {
        enterAmountInterfaceId = interfaceId;
        enterAmountSlot = player.getBank().getSlotById(id);
        enterAmountId = id;
        player.getActionSender().sendEnterAmountInterface();
    }

	/**
	 * Checks if the enter amount interface is open.
	 *
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isEnterAmountInterfaceOpen() {
		return enterAmountInterfaceId != - 1;
	}

	/**
	 * Called when the enter amount interface is closed.
	 *
	 * @param amount The amount that was entered.
	 */
	public void closeEnterAmountInterface(int amount) {
		try {
			if(amount <= 0)
				return;
			switch(enterAmountInterfaceId) {
				case BoB.PLAYER_INVENTORY_INTERFACE:
					BoB.deposit(player, enterAmountSlot, enterAmountId, amount);
					break;
                case RunePouch.INVENTORY_INTERFACE:
                    if(player.openedBoB)
                        BoB.deposit(player, enterAmountSlot, enterAmountId, amount);
                    else if(enterAmountSlot >= 0 && enterAmountSlot < Inventory.SIZE) {
                        RunePouch.deposit(player, enterAmountSlot, enterAmountId, amount);
                    }
                    break;
                case RunePouch.RUNE_INTERFACE:
                    if(player.openedBoB)
                        BoB.withdraw(player, enterAmountSlot, enterAmountId, amount);
                    else if(enterAmountSlot >= 0 && enterAmountSlot < RunePouch.SIZE)
                        RunePouch.withdraw(player, enterAmountId, amount);
                    break;
				case Bank.PLAYER_INVENTORY_INTERFACE:
					if(player.openedBoB)
						BoB.deposit(player, enterAmountSlot, enterAmountId, amount);
					else if(enterAmountSlot >= 0 && enterAmountSlot < Inventory.SIZE) {
						Bank.deposit(player, enterAmountSlot, enterAmountId, amount, true);
					}
					break;
                case Bank.BANK_INVENTORY_INTERFACE:
                case Bank.BANK_INVENTORY_INTERFACE + 1:
                case Bank.BANK_INVENTORY_INTERFACE + 2:
                case Bank.BANK_INVENTORY_INTERFACE + 3:
                case Bank.BANK_INVENTORY_INTERFACE + 4:
                case Bank.BANK_INVENTORY_INTERFACE + 5:
                case Bank.BANK_INVENTORY_INTERFACE + 6:
                case Bank.BANK_INVENTORY_INTERFACE + 7:
                case Bank.BANK_INVENTORY_INTERFACE + 8:
                    if(player.openedBoB)
                        BoB.withdraw(player, enterAmountSlot, enterAmountId, amount);
                    else if(enterAmountSlot >= 0 && enterAmountSlot < Bank.SIZE)
                        Bank.withdraw(player, enterAmountId, amount);
                    break;
				case BoB.BOB_INVENTORY_INTERFACE:
					if(enterAmountSlot >= 0 && enterAmountSlot < BoB.SIZE) {
						BoB.withdraw(player, enterAmountSlot, enterAmountId, amount);
					}
					break;
				case Trade.PLAYER_INVENTORY_INTERFACE:
					if(player.currentInterfaceStatus == 1) {
						if(enterAmountSlot >= 0 && enterAmountSlot < Inventory.SIZE) {
							Trade.deposit(player, enterAmountSlot, enterAmountId, amount);
						}
					} else {
						if(player.currentInterfaceStatus == 2) {
							if(enterAmountSlot >= 0 && enterAmountSlot < Inventory.SIZE) {
								Duel.deposit(player, enterAmountSlot, enterAmountId, amount);
							}
						}
					}//arsen is tard cause this wont update OJ
					break;
				case Trade.TRADE_INVENTORY_INTERFACE:
					Trade.withdraw(player, enterAmountSlot, enterAmountId, amount);
					break;
				case Duel.DUEL_INVENTORY_INTERFACE:
					Duel.withdraw(player, enterAmountSlot, enterAmountId, amount);
					break;
				case Bank.DEPOSIT_INVENTORY_INTERFACE:
					Bank.deposit(player, enterAmountSlot, enterAmountId, amount, true);
					break;
				case ShopManager.SHOP_INVENTORY_INTERFACE:
					if(enterAmountSlot >= 0 && player.getShopId() == - 2) {
						//World.getGrandExchange().buyItem(player, enterAmountId, amount, player.geItem[enterAmountSlot].getName(),enterAmountSlot);
					} else if(enterAmountSlot >= 0 && enterAmountSlot < ShopManager.SIZE) {
						ShopManager.buyItem(player, enterAmountId, enterAmountSlot, amount);
					}
					break;
				case ShopManager.PLAYER_INVENTORY_INTERFACE:
					if(enterAmountSlot >= 0 && enterAmountSlot < ShopManager.SIZE) {
						ShopManager.sellItem(player, enterAmountId, enterAmountSlot, amount);
					}
					break;
			}
		} finally {
			enterAmountInterfaceId = - 1;
		}
	}

	public int getOpenDialogueId() {
		return openDialogueId;
	}

	/**
	 * @param openDialogueId the openDialogueId to set
	 */
	public void setOpenDialogueId(int openDialogueId) {
		this.openDialogueId = openDialogueId;
	}

	/**
	 * @return the nextDialogueId
	 */
	public int getNextDialogueId(int index) {
		return nextDialogueId[index];
	}

	/**
	 * @param nextDialogueId the nextDialogueId to set
	 */
	public void setNextDialogueId(int index, int nextDialogueId) {
		this.nextDialogueId[index] = nextDialogueId;
	}

	public void destroy() {
		player = null;
	}


	public void setStringListener(String listener, String message) {
		this.string_input_listener = listener;
		player.getActionSender().sendEnterStringInterface(message);
	}

	public String string_input_listener = "";

	public boolean receiveStringListener(String result) {
		if (string_input_listener == null || string_input_listener.length() == 0) {
			return false;
		}
		result = result.replaceAll("_"," ");
		final String finalResult = result;
		switch (string_input_listener) {
			case "ge_set_quantity":
				player.getGrandExchangeTracker().ifNewEntry(e -> {
					try{
						final int quantity = Misc.expandNumber(finalResult.replace(' ', '.'));
						if(quantity < 1){
							player.sendf("Invalid quantity");
							return;
						}
						if(e.itemQuantity(quantity))
							JGrandExchangeInterface.NewEntry.setQuantityAndTotalPrice(player, e.itemQuantity(), e.totalPrice(), e.currency());
					}catch(Exception ex){
						ex.printStackTrace();
						player.sendf("Invalid quantity");
					}
				}, "You are not building a new entry right now");
				return true;
			case "ge_set_price":
				player.getGrandExchangeTracker().ifNewEntry(e -> {
					try{
						final int unitPrice = Misc.expandNumber(finalResult.replace(' ', '.'));
						if(unitPrice < 1){
							player.sendf("Invalid price");
							return;
						}
						if(e.unitPrice(unitPrice))
							JGrandExchangeInterface.NewEntry.setUnitPriceAndTotalPrice(player, e.unitPrice(), e.totalPrice(), e.currency());
					}catch(Exception ex){
						player.sendf("Invalid price");
					}
				}, "You are not building a new entry right now");
				return true;
			case "authenticator_confirmation":
				PlayerAuthenticationGenerator.confirmAuthenticator(player, result);
				return true;
			case "authenticator_removal_confirmation":
				PlayerAuthenticationGenerator.removeAuthenticator(player, result);
				return true;
			default:
				return false;
		}
	}

}
