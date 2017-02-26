package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Locations;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.ClueScrollManager;
import org.hyperion.rs2.model.combat.CombatAssistant;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;
import org.hyperion.rs2.model.container.EquipmentReq;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.container.duel.DuelRule.DuelRules;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.net.Packet;
import org.hyperion.util.Misc;

/**
 * Handles the 'wield' option on items.
 *
 * @author Graham Edgecombe
 */
public class WieldPacketHandler implements PacketHandler {

	@Override
	public void handle(final Player player, Packet packet) {
		//player.getWalkingQueue().reset();
		if(player.needsNameChange() || player.doubleChar())
			return;
		int id = packet.getShort() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		int interfaceId = packet.getShortA() & 0xFFFF;
	    /*if(player.cE.getAtkType() == 7){
			
		}*/
		//player.getLogging().log("Wielding item : " + id);
		switch(id) {
			case 2653:
				player.getInventory().remove(new Item(2653, 1));
				return;
			case 15098:
				Dicing.rollClanDice(player, Misc.random(100));
				return;
            case 2669:
                return;
		}
		if(slot < 0 || slot > 28 || id < 0 || id > ItemDefinition.MAX_ID)
			return;
		boolean removeShield = false;
		if(! EquipmentReq.canEquipItem(player, id))
			return;
		if(CombatAssistant.is2H(id))
			removeShield = true;
        if(id == 2422) {
            player.getInventory().set(slot, null);
            return;
        }
		if (id == 19773) {
			player.setPNpc(6747 + Misc.random(2));
			player.getCombat().doGfx(310);
		}
		if(ClueScrollManager.isClue(id)) {
			ClueScrollManager.getInInventory(player).send(player);
			return;
		}
		switch(interfaceId) {
			case Inventory.INTERFACE:
				if((player.cannotSwitch || player.duelRule[DuelRules.SWITCH.ordinal()]) && (player.duelAttackable > 0 || player.getLocation() == Locations.Location.DUEL_ARENA)) {
					player.getActionSender().sendMessage("You cannot switch in this duel!");
					return;
				}
						//switch while duel screen is open
				if(player.getPosition().inDuel())
					Duel.declineTrade(player);
				if(slot >= 0 && slot < Inventory.SIZE) {
					Item item = player.getInventory().get(slot);
					if(item != null && item.getId() == id) {
						EquipmentType type = Equipment.getType(item); //OMG FOUNDS THIS SHIT
						//System.out.println("Type " + type);
						if((player.banEquip[type.getSlot()] || (player.banEquip[Equipment.SLOT_SHIELD] && removeShield)) && player.duelAttackable > 0) {
							player.getActionSender().sendMessage("You cannot do that in this duel.");
							return;
						}
						if(player.getEquipment().get(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot()) != null) {
							if(type.getSlot() == 5 && CombatAssistant.is2H(player.getEquipment().get(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot()).getId())) {

								//equiping shield and has a 2h on
								if(player.getInventory().freeSlots() >= 1) {
									player.getInventory().add(player.getEquipment().get(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot()));
									player.getEquipment().set(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot(), null);
								} else {
									player.getActionSender().sendMessage("You currently do not have enough space in your inventory.");
									return;
								}
							}
						}
						Item oldEquip = null;
						boolean stackable = ItemDefinition.forId(id).isStackable();
						if(removeShield && player.getEquipment().get(EquipmentType.SHIELD.getSlot()) != null) {
							if(player.getInventory().freeSlots() < 1 && player.getEquipment().get(type.getSlot()) != null) {
								player.getActionSender().sendMessage("You currently do not have enough space in your inventory.");
								return;
							}
						}
						//System.out.println("Stackable " + stackable);
						if(type.getSlot() == Equipment.SLOT_WEAPON)
							player.specOn = false;
                        if(!player.autoRetailate && (id != 4153 && id != 17646))
                            player.getCombat().setOpponent(null);
						player.getSpecBar().sendSpecAmount();

						int shieldId = - 1;
						if(player.getEquipment().get(EquipmentType.SHIELD.getSlot()) != null)
							shieldId = player.getEquipment().get(EquipmentType.SHIELD.getSlot()).getId();
						if(removeShield)
							player.getEquipment().set(EquipmentType.SHIELD.getSlot(), null);

						if(player.getEquipment().isSlotUsed(type.getSlot())) {
							//System.out.println("Slot is used");
							oldEquip = player.getEquipment().get(type.getSlot());
							player.getEquipment().ignoreOnce = true;
							//player.getEquipment().set(type.getSlot(), null); Improves Switching
						}
						if(oldEquip != null && oldEquip.getId() < 0)
							oldEquip = null;
						//player.getInventory().set(slot, null); slot fixing prob
						if(oldEquip != null) {
							if(oldEquip.getId() == item.getId() && stackable) {
								long totalEquip = oldEquip.getCount() + item.getCount();
								if(totalEquip > Integer.MAX_VALUE || totalEquip < 0) {
									player.getActionSender().sendMessage("Slot full!");
									return;
								}
								player.getEquipment().set(type.getSlot(), new Item(item.getId(), (item.getCount() + oldEquip.getCount())));
								player.getInventory().set(slot, null);
								return;
							} else {
								//System.out.println("oldEquip  b4 switch : " + oldEquip);
								//System.out.println("Still stackable : " + stackable);
								if(/*stackable && */ContentEntity.isItemInBag(player, oldEquip.getId())) {
									player.getInventory().set(slot, null);
									player.getInventory().add(oldEquip);
									//System.out.println("ItemInBag");
								} else {
									player.getInventory().set(slot, oldEquip);
									//System.out.println("Not in bag?");
								}
								//player.getInventory().add(oldEquip);
							}
						} else {
							player.getInventory().set(slot, null);
							//System.out.println("Set slot to null");
						}
                        //player.getActionSender().sendMessage(type.getSlot()+" | "+item);
                        if(type.getSlot() != EquipmentType.ARROWS.getSlot() && type.getSlot() != EquipmentType.WEAPON.getSlot()) {
                            if(item.getCount() > 1) {
                                item = new Item(item.getId());
                            }
                        }
						player.getEquipment().set(type.getSlot(), item);
						//System.out.println(item);
					/*if(!stackable) {
						player.getEquipment().set(type.getSlot(), item);
					} else {
						player.getEquipment().add(item);
					}*/
						if(removeShield) {
							if(shieldId > 0)
								player.getInventory().add(new Item(shieldId, 1));
						}
					}
				}
				break;
		}
	}

	public static void wearItem(Player player, int slot) {
		int id = player.getInventory().get(slot).getId();
		player.debugMessage("player cannot switch: "+player.cannotSwitch);
		boolean removeShield = false;
		if(! EquipmentReq.canEquipItem(player, id))
			return;
		if(CombatAssistant.is2H(id))
			removeShield = true;
		if(slot >= 0 && slot < Inventory.SIZE) {
			Item item = player.getInventory().get(slot);
			if(item != null && item.getId() == id) {
				EquipmentType type = Equipment.getType(item);
				if((player.banEquip[type.getSlot()] || (player.banEquip[Equipment.SLOT_SHIELD] && removeShield)) && player.duelAttackable > 0) {
					player.getActionSender().sendMessage("You cannot do that in this duel.");
					return;
				}
				if(player.getEquipment().get(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot()) != null) {
					if(type.getSlot() == 5 && CombatAssistant.is2H(player.getEquipment().get(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot()).getId())) {
						//equiping shield and has a 2h on
						if(player.getInventory().freeSlots() >= 1) {
							player.getInventory().add(player.getEquipment().get(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot()));
							player.getEquipment().set(org.hyperion.rs2.model.container.Equipment.EquipmentType.WEAPON.getSlot(), null);
						} else {
							player.getActionSender().sendMessage("You currently do not have enough space in your inventory.");
							return;
						}
					}
				}
				Item oldEquip = null;
				boolean stackable = ItemDefinition.forId(id).isStackable();
				if(removeShield && player.getEquipment().get(EquipmentType.SHIELD.getSlot()) != null) {
					if(player.getInventory().freeSlots() < 1 && player.getEquipment().get(type.getSlot()) != null) {
						player.getActionSender().sendMessage("You currently do not have enough space in your inventory.");
						return;
					}
				}
                if(type.getSlot() == Equipment.SLOT_WEAPON)
				    player.specOn = false;
				if(id != 4153 && id != 17646)
					player.cE.setOpponent(null);
				player.getSpecBar().sendSpecAmount();
				int shieldId = - 1;
				if(player.getEquipment().get(EquipmentType.SHIELD.getSlot()) != null)
					shieldId = player.getEquipment().get(EquipmentType.SHIELD.getSlot()).getId();
				if(removeShield)
					player.getEquipment().set(EquipmentType.SHIELD.getSlot(), null);

				if(player.getEquipment().isSlotUsed(type.getSlot())) {
					oldEquip = player.getEquipment().get(type.getSlot());
					player.getEquipment().ignoreOnce = true;
					player.getEquipment().set(type.getSlot(), null);
				}
				//player.getInventory().set(slot, null); slot fixing prob
				if(oldEquip != null) {
					if(oldEquip.getId() == item.getId() && stackable) {
						player.getEquipment().set(type.getSlot(), new Item(item.getId(), (item.getCount() + oldEquip.getCount())));
						player.getInventory().set(slot, null);
						return;
					} else {
						if(stackable && ContentEntity.isItemInBag(player, oldEquip.getId())) {
							player.getInventory().set(slot, null);
							player.getInventory().add(oldEquip);
						} else
							player.getInventory().set(slot, oldEquip);
						//player.getInventory().add(oldEquip);
					}
				} else
					player.getInventory().set(slot, null);
				player.getEquipment().set(type.getSlot(), item);
					/*if(!stackable) {
						player.getEquipment().set(type.getSlot(), item);
					} else {
						player.getEquipment().add(item);
					}*/
				if(removeShield) {
					if(shieldId > 0)
						player.getInventory().add(new Item(shieldId, 1));
				}
			}
		}
	}

}
