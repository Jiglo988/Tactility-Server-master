package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.*;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.rs2.model.content.jge.itf.JGrandExchangeInterface;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.DragonfireShield;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.model.content.misc2.RunePouch;
import org.hyperion.rs2.net.Packet;

/**
 * Remove item options.
 *
 * @author Graham Edgecombe
 */
public class ItemOptionPacketHandler implements PacketHandler {

	/**
	 * Eat / Bury
	 */
	private static final int OPTION_EAT = 122;
	/**
	 * Item on Item
	 */
	private static final int OPTION_ITEM_ON_ITEM = 53;
	/**
	 * Item on Magic
	 */
	private static final int OPTION_ITEM_ON_MAGIC = 237;
	/**
	 * Item on OBJECT
	 */
	private static final int OPTION_ITEM_ON_OBJECT = 192;
	/**
	 * Item on NPC
	 */
	private static final int OPTION_ITEM_ON_NPC = 57;
	/**
	 * Option 1 opcode.
	 */
	private static final int OPTION_1 = 145;

	/**
	 * Option 2 opcode.
	 */
	private static final int OPTION_2 = 117;

	/**
	 * Option 3 opcode.
	 */
	private static final int OPTION_3 = 43;

	/**
	 * Option 4 opcode.
	 */
	private static final int OPTION_4 = 129;

	/**
	 * Option 5 opcode.
	 */
	private static final int OPTION_5 = 135;

	/**
	 * Option 6 opcode.
	 */
	private static final int OPTION_6 = 75;
	/**
	 * Option 7 opcode.
	 */
	private static final int OPTION_7 = 42;

	@Override
	public void handle(Player player, Packet packet) {
		//System.out.println(packet.getOpcode() + "");
		if(player.needsNameChange() || player.doubleChar())
			return;
		switch(packet.getOpcode()) {
			case OPTION_EAT:
				handleEatOption(player, packet);
				break;
			case OPTION_1:
				handleItemOption1(player, packet);
				break;
			case OPTION_2:
				handleItemOption2(player, packet);
				break;
			case OPTION_3:
				handleItemOption3(player, packet);
				break;
			case OPTION_4:
				handleItemOption4(player, packet);
				break;
			case OPTION_5:
				handleItemOption5(player, packet);
				break;
			case OPTION_6:
				handleItemOption6(player, packet);
				break;
			case OPTION_7:
				handleItemOption7(player, packet);
				break;
			case OPTION_ITEM_ON_ITEM:
				handleItemOnItem(player, packet);
				break;
			case OPTION_ITEM_ON_OBJECT:
				handleItemOnObject(player, packet);
				break;
			case OPTION_ITEM_ON_MAGIC:
				handleItemOnMagic(player, packet);
				break;
			case OPTION_ITEM_ON_NPC:
				handleItemOnNpc(player, packet);
				break;
		}
	}

	private void handleItemOnItem(Player player, Packet packet) {
		int usedWithSlot = packet.getShort();
		int itemUsedSlot = packet.getShortA();//the item highlighted
		if(itemUsedSlot < 0 || itemUsedSlot > 27 || usedWithSlot < 0 || usedWithSlot > 27)
			return;
		if(player.getInventory().get(usedWithSlot) == null || player.getInventory().get(itemUsedSlot) == null)
			return;

		int onItem = player.getInventory().get(usedWithSlot).getId();
		int useItem = player.getInventory().get(itemUsedSlot).getId();
        if(onItem == 15707 && player.getDungeoneering().inDungeon()) {
            final Item item = player.getInventory().get(itemUsedSlot);
            if(item == null || (!FightPits.scItems.contains(item.getId()) && !ItemSpawning.canSpawn(item.getId()))) {
                player.sendMessage("You cannot bind this item");
                return;
            }
            for(final Item item1 : player.getDungeoneering().getBinds()) {
                if(item1 != null && item1.getId() == item.getId()) {
                    player.sendMessage("You already have this item binded!");
                    return;
                }
            }
            DialogueManager.openDialogue(player, 7005);
            player.getExtraData().put("binditem", item);
            return;
        }
		if(ContentManager.handlePacket(13, player, useItem, itemUsedSlot, onItem, usedWithSlot))
			return;
		//random stuff
		return;
	}

	private void handleItemOnObject(Player player, Packet packet) {
		packet.getLEShortA();
		int objectId = packet.getLEShort();
		int objectY = packet.getLEShortA();
		int itemSlot = (packet.getLEShortA() - 128);
		int objectX = packet.getLEShortA();
		int itemId = packet.getShort();
		//System.out.println("Itemid : " + itemId + " ObjId " + objectId);
		if(itemSlot < 0 || itemSlot > 27 || itemId < 0 || itemId > ItemDefinition.MAX_ID)
			return;
		if(player.getInventory().get(itemSlot) == null || player.getInventory().get(itemSlot).getId() != itemId)
			return;
		player.face(Position.create(objectX, objectY, 0));
		if(player.getPosition().distance(Position.create(objectX, objectY, 0)) > 2)
			return;
		if(ContentManager.handlePacket(14, player, itemId, itemSlot, objectId, - 1))
			return;
		if(ContentManager.handlePacket(19, player, itemId, objectId, objectX, objectY))
			return;
		//random stuff
		return;
	}

	private void handleItemOnNpc(Player player, Packet packet) {
		int itemId = packet.getShortA();
		int i = packet.getShortA();
		int invslot = packet.getLEShort();
        if(invslot >= player.getInventory().capacity()) {
            return;
        }
		Item item = player.getInventory().get(invslot);
        final NPC npc = (NPC)World.getNpcs().get(i);
        if (item != null && npc != null) {
            if(npc.getDefinition().getId() == 2999)
                Dicing.diceNpc(player, npc, item);
            else if(npc.getDefinition().getId() == 2998) {
                if(Dicing.pkpValues.containsKey(item.getId()) && player.getExtraData().getInt("dicewarn") == item.getId()) {
                    Dicing.diceNpc(player, npc, item, true);
                }else if(Dicing.pkpValues.containsKey(item.getId())) {
                    player.sendf("This item will gamble for %,d PKT", Dicing.pkpValues.get(item.getId()) * 2);
                    player.getExtraData().put("dicewarn", item.getId());
                } else {
                    player.sendf("This item cannot be gambled for PKT");
                }
            }
        }
	}

	private void handleItemOnMagic(Player player, Packet packet) {
		int castOnSlot = packet.getShort();
		int castOnItem = packet.getShortA();
	    /*int interfaceId = */
		packet.getShort();
		int castSpell = packet.getShortA();
		if(castOnSlot < 0 || castOnSlot > 27 || castOnItem < 0 || castOnItem > ItemDefinition.MAX_ID)
			return;
		if(player.getInventory().get(castOnSlot) == null || player.getInventory().get(castOnSlot).getId() != castOnItem)
			return;
		if(ContentManager.handlePacket(18, player, castSpell, castOnItem, castOnSlot, - 1))
			return;
		//random stuff
		Magic.alch(player, castOnItem, castSpell);
		return;
	}


	private void handleEatOption(Player player, Packet packet) {
		int buryA = packet.getLEShortA();
		int burySlot = (packet.getShortA());
		int buryItemID = packet.getLEShort();
        switch (buryItemID) {
            case 7510:
            case 7509:
                if(!(player.getSkills().getLevel(3) > 1)) {
                    player.sendMessage("You have too low hitpoints to eat the dwarven rock cake.");
                    return;
                }
                ContentEntity.startAnimation(player, 829);
                player.forceMessage("Ow! I nearly broke a tooth!");
                player.cE.hit(1, null, false, Constants.MELEE);
                break;
        }
		if(burySlot < 0 || burySlot > 27 || buryItemID < 0 || buryItemID > ItemDefinition.MAX_ID)
			return;
		if(player.getInventory().get(burySlot) == null || player.getInventory().get(burySlot).getId() != buryItemID)
			return;
		//System.out.println("buryA: "+buryA+" burySlot: "+burySlot+" itemId: "+buryItemID);
		if(ContentManager.handlePacket(1, player, buryItemID, burySlot, buryA, - 1))
			return;
		//random stuff
		return;
	}

	/**
	 * Handles item option 1.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption1(Player player, Packet packet) {
		int interfaceId = packet.getShortA() & 0xFFFF;
		//System.out.println("interface: "+interfaceId);
		int slot = packet.getShortA() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		ItemDefinition def = ItemDefinition.forId(id);
		if(interfaceId != 5382 || (slot <= 27 && player.getInventory().get(slot) != null && player.getInventory().get(slot).getId() == id))
			if(ContentManager.handlePacket(2, player, id, slot, interfaceId, - 1))
				return;
		switch(interfaceId) {
			case Equipment.INTERFACE:
				if(slot >= 0 && slot < Equipment.SIZE) {
					//player.getLogging().log("Option 1 Unequiping: " + def.getName());
					if((FightPits.teamBlue.contains(player) && id == FightPits.BLUE_CAPE) || 
							(FightPits.teamRed.contains(player) && id == FightPits.RED_CAPE)) {
						player.getActionSender().sendMessage("Show some team spirit!");
						break;
					}
					if(! Container.transfer(player.getEquipment(), player.getInventory(), slot, id)) {
						// indicate it failed
					}
				}
				break;
			case JGrandExchangeInterface.SELL_INTERFACE:
				player.getGrandExchangeTracker().selectItem(id, Entry.Type.SELLING);
				break;
			case RunePouch.INVENTORY_INTERFACE:
                if(player.openedBoB)
                    BoB.deposit(player, slot, id, 1);
                else if(slot >= 0 && slot < Inventory.SIZE) {
                    RunePouch.deposit(player, slot, id, 1);
                }
                break;
            case RunePouch.RUNE_INTERFACE:
                if(player.openedBoB)
                    BoB.withdraw(player, slot, id, 1);
                else if(slot >= 0 && slot < RunePouch.SIZE) {
                    RunePouch.withdraw(player, id, 1);
                }
                break;
			case Bank.PLAYER_INVENTORY_INTERFACE:
				//player.getLogging().log("Option 1 Bank deposit: " + def.getName());
				if(player.openedBoB)
					BoB.deposit(player, slot, id, 1);
				else if(slot >= 0 && slot < Inventory.SIZE) {
					Bank.deposit(player, slot, id, 1, true);
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
				//player.getLogging().log("Option 1 Bank Withdraw: " + def.getName());
				if(player.openedBoB)
					BoB.withdraw(player, slot, id, 1);
				else if(slot >= 0 && slot < Bank.SIZE) {
					Bank.withdraw(player, id, 1);
				}
				break;
			case Bank.DEPOSIT_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					//player.getLogging().log("Option 1 Bank Deposit : " + def.getName());
					Bank.deposit(player, slot, id, 1, true);
				}
				break;
			case BoB.BOB_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < BoB.SIZE) {
					//player.getLogging().log("Option 1 BoB Withdraw : " + def.getName());
					BoB.withdraw(player, slot, id, 1);
				}
				break;
			case BoB.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					//player.getLogging().log("Option 1 Bob Deposit : " + def.getName());
					BoB.deposit(player, slot, id, 1);
				}
				break;
			case Trade.PLAYER_INVENTORY_INTERFACE:

				if(player.currentInterfaceStatus == 1) {
					if(slot >= 0 && slot < Inventory.SIZE) {
						//player.getLogging().log("Option 1 Trade Deposit: " + def.getName());
						Trade.deposit(player, slot, id, 1);
					}
				} else {
					if(player.currentInterfaceStatus == 2) {
						if(slot >= 0 && slot < Inventory.SIZE) {
							//player.getLogging().log("Option 1 Duel Deposit: " + def.getName());
							Duel.deposit(player, slot, id, 1);
						}
					}
				}
				break;
			case Trade.TRADE_INVENTORY_INTERFACE:

				if(slot >= 0 && slot < Trade.SIZE) {
					//player.getLogging().log("Option 1 Trade withdraw: " + def.getName());
					Trade.withdraw(player, slot, id, 1);
				}
				break;
			case Duel.DUEL_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Duel.SIZE) {
					Duel.withdraw(player, slot, id, 5);
					//player.getLogging().log("Option 1 Duel withdraw : " + def.getName());
				}
				break;
			case ShopManager.PLAYER_INVENTORY_INTERFACE:

				if(slot >= 0 && slot < Inventory.SIZE) {
					//value
					ShopManager.valueSellItem(player, id);
					//player.getLogging().log("Option 1 Sell to shop : " + def.getName());
				}
				break;
			case ShopManager.SHOP_INVENTORY_INTERFACE:

				if(slot >= 0 && player.getShopId() == - 2) {
					//World.getGrandExchange().valueGeItem(player, slot);
				} else if(slot >= 0 && slot < ShopManager.SIZE) {
					//player.getLogging().log("Option 1 Shop Buy : " + def.getName());
					ShopManager.valueBuyItem(player, id);
				}
				break;
		}
	}

	private void handleItemOption6(Player player, Packet packet) {
		int interfaceId = packet.getShortA() & 0xFFFF;
		int slot = packet.getLEShort() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		if(interfaceId != 5382 || (slot <= 27 && player.getInventory().get(slot) != null && player.getInventory().get(slot).getId() == id))
			if(ContentManager.handlePacket(17, player, id, slot, interfaceId, - 1))
				return;
		switch(id) {
		/*case 11283:
		case 11284:
			player.debugMessage("Click'd");
			if(player.cE != null && player.getCombat().getOpponent() != null);
				DragonfireShield.handleSpecial(player, player.getCombat().getOpponent());
			break;*/
			case 1712:
				//glory
				DialogueManager.openDialogue(player, 5);
				break;
			case 2560:
				//ring dueling
				DialogueManager.openDialogue(player, 13);
				break;
		}
	}

	private void handleItemOption7(Player player, Packet packet) {
		int id = packet.getShort() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		int interfaceId = packet.getShortA() & 0xFFFF;
		//System.out.println("option 7: "+id+" "+slot+" "+interfaceId);
		if(interfaceId != 5382 || (slot <= 27 && player.getInventory().get(slot) != null && player.getInventory().get(slot).getId() == id))
			if(ContentManager.handlePacket(22, player, id, slot, interfaceId, - 1))
				return;
	}


	/**
	 * Handles item option 2.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption2(Player player, Packet packet) {
		int interfaceId = packet.getLEShortA();
		int id = packet.getLEShortA();
		int slot = packet.getLEShort();
		//System.out.println(interfaceId+" "+id+" "+slot);
		//player.getLogging().log("Item option 2 : " + id + ", " + interfaceId);
		if(interfaceId != 5382 || (slot <= 27 && player.getInventory().get(slot) != null && player.getInventory().get(slot).getId() == id))
			if(ContentManager.handlePacket(3, player, id, slot, interfaceId, - 1))
				return;
		switch(interfaceId) {
            case RunePouch.INVENTORY_INTERFACE:
            if(player.openedBoB)
                BoB.deposit(player, slot, id, 5);
            else if(slot >= 0 && slot < Inventory.SIZE) {
                RunePouch.deposit(player, slot, id, 5);
            }
                break;
            case RunePouch.RUNE_INTERFACE:
                if(player.openedBoB)
                    BoB.withdraw(player, slot, id, 5);
                else if(slot >= 0 && slot < RunePouch.SIZE) {
                    RunePouch.withdraw(player, id, 5);
                }
                break;
			case Bank.PLAYER_INVENTORY_INTERFACE:
				if(player.openedBoB)
					BoB.deposit(player, slot, id, 5);
				else if(slot >= 0 && slot < Inventory.SIZE) {
					Bank.deposit(player, slot, id, 5, true);
				}
				break;
            case -15448:
            case -15447:
            case -15446:
            case -15445:
            case -15444:
            case -15443:
            case -15442:
            case -15441:
            case -15440:
				if(player.openedBoB)
					BoB.withdraw(player, slot, id, 5);
				else if(slot >= 0 && slot < Bank.SIZE) {
					Bank.withdraw(player, id, 5);
				}
				break;
			case Bank.DEPOSIT_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					Bank.deposit(player, slot, id, 5, true);
				}
				break;
			case BoB.BOB_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < BoB.SIZE) {
					BoB.withdraw(player, slot, id, 5);
				}
				break;
			case BoB.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					BoB.deposit(player, slot, id, 5);
				}
				break;
			case Trade.PLAYER_INVENTORY_INTERFACE:
				if(player.currentInterfaceStatus == 1) {
					if(slot >= 0 && slot < Inventory.SIZE) {
						Trade.deposit(player, slot, id, 5);
					}
				} else {
					if(player.currentInterfaceStatus == 2) {
						if(slot >= 0 && slot < Inventory.SIZE) {
							Duel.deposit(player, slot, id, 5);
						}
					}
				}
				break;
			case Trade.TRADE_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Trade.SIZE) {
					Trade.withdraw(player, slot, id, 5);
				}
				break;
			case Duel.DUEL_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Duel.SIZE) {
					Duel.withdraw(player, slot, id, 5);
				}
				break;
			case ShopManager.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					ShopManager.sellItem(player, id, slot, 1);
				}
				break;
			case ShopManager.SHOP_INVENTORY_INTERFACE:
				if(slot >= 0 && player.getShopId() == - 2) {
					//World.getGrandExchange().buyItem(player, id, 1, player.geItem[slot].getName(),slot);
				} else if(slot >= 0 && slot < ShopManager.SIZE) {
					ShopManager.buyItem(player, id, slot, 1);
				}
				break;
		}
	}

	/**
	 * Handles item option 3.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption3(Player player, Packet packet) {
		int interfaceId = packet.getLEShort();
		int id = packet.getShortA();
		int slot = packet.getShortA();
		//player.getLogging().log("Item option 3 : " + id + ", " + interfaceId);
		if(interfaceId != 5382 || (slot <= 27 && player.getInventory().get(slot) != null && player.getInventory().get(slot).getId() == id))
			if(ContentManager.handlePacket(4, player, id, slot, interfaceId, - 1))
				return;
		if(player.getPvPStorage().contains(id)) {
			player.sendf("You have approximately %d%% charges left on your %s", player.getPvPStorage().get(id)/10, ItemDefinition.forId(id).getName());
		}
		switch(id) {
		case 11283:
		case 11284:
			player.debugMessage("Click'd other");
			if(player.cE != null && player.getCombat().getOpponent() != null);
				DragonfireShield.handleSpecial(player, player.getCombat().getOpponent());
			break;
			case 1712:
				//glory
				DialogueManager.openDialogue(player, 5);
				break;
			case 2560:
				//ring dueling
				DialogueManager.openDialogue(player, 13);
				break;
		}
		switch(interfaceId) {
            case RunePouch.INVENTORY_INTERFACE:
            if(player.openedBoB)
                BoB.deposit(player, slot, id, 10);
            else if(slot >= 0 && slot < Inventory.SIZE) {
                RunePouch.deposit(player, slot, id, 10);
            }
            break;
            case RunePouch.RUNE_INTERFACE:
                if(player.openedBoB)
                    BoB.withdraw(player, slot, id, 10);
                else if(slot >= 0 && slot < RunePouch.SIZE) {
                    RunePouch.withdraw(player, id, 10);
                }
                break;
			case Bank.PLAYER_INVENTORY_INTERFACE:
				if(player.openedBoB)
					BoB.deposit(player, slot, id, 10);
				else if(slot >= 0 && slot < Inventory.SIZE) {
					Bank.deposit(player, slot, id, 10, true);
				}
				break;
            case -15448:
            case -15447:
            case -15446:
            case -15445:
            case -15444:
            case -15443:
            case -15442:
            case -15441:
            case -15440:
				if(player.openedBoB)
					BoB.withdraw(player, slot, id, 10);
				else if(slot >= 0 && slot < Bank.SIZE) {
					Bank.withdraw(player, id, 10);
				}
				break;
			case Bank.DEPOSIT_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					Bank.deposit(player, slot, id, 10, true);
				}
				break;
			case BoB.BOB_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < BoB.SIZE) {
					BoB.withdraw(player, slot, id, 10);
				}
				break;
			case BoB.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					BoB.deposit(player, slot, id, 10);
				}
				break;
			case Trade.PLAYER_INVENTORY_INTERFACE:
				if(player.currentInterfaceStatus == 1) {
					if(slot >= 0 && slot < Inventory.SIZE) {
						Trade.deposit(player, slot, id, 10);
					}
				} else {
					if(player.currentInterfaceStatus == 2) {
						if(slot >= 0 && slot < Inventory.SIZE) {
							Duel.deposit(player, slot, id, 10);
						}
					}
				}
				break;
			case Trade.TRADE_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Trade.SIZE) {
					Trade.withdraw(player, slot, id, 10);
				}
				break;
			case Duel.DUEL_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Duel.SIZE) {
					Duel.withdraw(player, slot, id, 10);
				}
				break;
			case ShopManager.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					//value
					ShopManager.sellItem(player, id, slot, 5);
				}
				break;
			case ShopManager.SHOP_INVENTORY_INTERFACE:
				if(slot >= 0 && player.getShopId() == - 2) {
					//World.getGrandExchange().buyItem(player, id, 5, player.geItem[slot].getName(),slot);
				} else if(slot >= 0 && slot < ShopManager.SIZE) {
					ShopManager.buyItem(player, id, slot, 5);
				}
				break;
		}
	}

	/**
	 * Handles item option 4.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption4(Player player, Packet packet) {
		int slot = packet.getShortA() & 0xFFFF;
		int interfaceId = packet.getShort() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		//player.getLogging().log("Item option 4 : " + id + ", " + interfaceId);
		if(interfaceId != 5382 || (slot <= 27 && player.getInventory().get(slot) != null && player.getInventory().get(slot).getId() == id))
			if(ContentManager.handlePacket(5, player, id, slot, interfaceId, - 1))
				return;
		switch(interfaceId) {
		case Equipment.INTERFACE:
			/*switch(id) {
			case 11283:
			case 11284:
				player.debugMessage("Click'd 4");
				if(player.cE != null && player.getCombat().getOpponent() != null);
					DragonfireShield.handleSpecial(player, player.getCombat().getOpponent());
				break;
			}*/
			break;
            case RunePouch.INVENTORY_INTERFACE:
            if(player.openedBoB)
                BoB.deposit(player, slot, id,  player.getInventory().getCount(id));
            else if(slot >= 0 && slot < Inventory.SIZE) {
                RunePouch.deposit(player, slot, id,  player.getInventory().getCount(id));
            }
                break;
            case RunePouch.RUNE_INTERFACE:
                if(player.openedBoB)
                    BoB.withdraw(player, slot, id, player.getBoB().getCount(id));
                else if(slot >= 0 && slot < RunePouch.SIZE) {
                    RunePouch.withdraw(player, id, player.getRunePouch().getCount(id));
                }
                break;
			case Bank.PLAYER_INVENTORY_INTERFACE:
				if(player.openedBoB)
					BoB.deposit(player, slot, id, player.getInventory().getCount(id));
				else if(slot >= 0 && slot < Inventory.SIZE) {
					Bank.deposit(player, slot, id, player.getInventory().getCount(id), true);
				}
				break;
			case Bank.DEPOSIT_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					Bank.deposit(player, slot, id, player.getInventory().getCount(id), true);
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
					BoB.withdraw(player, slot, id, player.getBoB().getCount(id));
				else if(slot >= 0 && slot < Bank.SIZE) {
					Bank.withdraw(player, id, player.getBank().getCount(id));
				}
				break;
			case BoB.BOB_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < BoB.SIZE) {
					BoB.withdraw(player, slot, id, player.getBoB().getCount(id));
				}
				break;
			case BoB.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					BoB.deposit(player, slot, id, 28);
				}
				break;
			case Trade.PLAYER_INVENTORY_INTERFACE:
				if(player.currentInterfaceStatus == 1) {
					if(slot >= 0 && slot < Inventory.SIZE) {
						Trade.deposit(player, slot, id, player.getInventory().getCount(id));
					}
				} else {
					if(player.currentInterfaceStatus == 2) {
						if(slot >= 0 && slot < Inventory.SIZE) {
							Duel.deposit(player, slot, id, player.getInventory().getCount(id));
						}
					}
				}
				break;
			case Trade.TRADE_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Trade.SIZE) {
					Trade.withdraw(player, slot, id, player.getTrade().getCount(id));
				}
				break;
			case Duel.DUEL_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Duel.SIZE) {
					Duel.withdraw(player, slot, id, player.getDuel().getCount(id));
				}
				break;
			case ShopManager.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					//value
					ShopManager.sellItem(player, id, slot, 10);
				}
				break;
			case ShopManager.SHOP_INVENTORY_INTERFACE:
				if(slot >= 0 && player.getShopId() == - 2) {
					//World.getGrandExchange().buyItem(player, id, 10, player.geItem[slot].getName(),slot);
				} else if(slot >= 0 && slot < ShopManager.SIZE) {
					ShopManager.buyItem(player, id, slot, 10);
				}
				break;
		}
	}


	/**
	 * Handles item option 5.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleItemOption5(Player player, Packet packet) {
		int slot = packet.getLEShort() & 0xFFFF;
		int interfaceId = packet.getShortA() & 0xFFFF;
		int id = packet.getLEShort() & 0xFFFF;
		//player.getLogging().log("Item option 5 : " + id + ", " + interfaceId);
		if(interfaceId != 5382 || (slot <= 27 && player.getInventory().get(slot) != null && player.getInventory().get(slot).getId() == id))
			if(ContentManager.handlePacket(21, player, id, slot, interfaceId, - 1))
				return;
		switch(interfaceId) {
            case RunePouch.INVENTORY_INTERFACE:
                if(slot >= 0 && slot < Inventory.SIZE) {
                    player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
                }
                break;
            case RunePouch.RUNE_INTERFACE:
                if(slot >= 0 && slot < RunePouch.SIZE) {
                    player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
                }
                break;
			case Bank.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
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
				if(slot >= 0 && slot < Bank.SIZE) {
					player.getInterfaceState().openEnterAmountInterface(interfaceId, id);
				}
				break;
			case Bank.DEPOSIT_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
				}
				break;
			case Trade.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
				}
				break;
			case Trade.TRADE_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Trade.SIZE) {
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
				}
				break;
			case Duel.DUEL_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Duel.SIZE) {
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
				}
				break;
			case ShopManager.PLAYER_INVENTORY_INTERFACE:
				if(slot >= 0 && slot < Inventory.SIZE) {
					//value
					//ShopManager.sellItem(player, id, slot, 20);
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
				}
				break;
			case ShopManager.SHOP_INVENTORY_INTERFACE:
				if(slot >= 0 && player.getShopId() == - 2) {
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
					//World.getGrandExchange().buyItem(player, id, 20, player.geItem[slot].getName(),slot);
				} else if(slot >= 0 && slot < ShopManager.SIZE) {
					//ShopManager.buyItem(player, id, slot, 20);
					player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
				}
				break;
		}
	}

}
