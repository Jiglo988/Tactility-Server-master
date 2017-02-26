package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.achievements.AchievementHandler;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.combat.SpecialAttacks;
import org.hyperion.rs2.model.combat.summoning.SummoningSpecial;
import org.hyperion.rs2.model.container.BoB;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.Events;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.jge.tracker.JGrandExchangeTracker;
import org.hyperion.rs2.model.sets.SetHandler;
import org.hyperion.rs2.net.Packet;

import java.util.LinkedList;

/**
 * Handles clicking on most buttons in the interface.
 *
 * @author Graham Edgecombe
 */
@SuppressWarnings("unused")
public class ActionButtonPacketHandler implements PacketHandler {

	static {
	}

	@Override
	public void handle(Player player, Packet packet) {
		final int button = packet.getShort();
		if(player.getLogging() != null)
			//player.getLogging().log("Player clicked on action : " + button);
		if(player.debug)
			player.getActionSender().sendMessage("Clicked on: " + button + "");
		ButtonAction action = ActionsManager.getManager().getButtonAction(button);
		if(action != null) {
			action.handle(player, button);
		} else {
			handle(player, button);
		}
	}

	public static void handle(Player player, int button) {
		if(ContentManager.handlePacket(0, player, button, - 1, - 1, - 1)) {
			return;
		}
        if(button >= 31421 && button <= 31426) {
			if (SetHandler.handleSet(player, button)) {
				return;
			}
		}
        if (Bank.bankButton(player, button)) {
            return;
        }
        if(AchievementHandler.achievementButton(player, button)) {
            return;
        }
		if(JGrandExchangeTracker.isGrandExchangeAction(button)) {
			player.getGrandExchangeTracker().handleInterfaceInteraction(button);
			return;
		}
		switch(button) {
			case 28887:
				player.getActionSender().removeAllInterfaces();
				break;
            case -29034:
                final Player opp = player.getBountyHunter().getTarget();
                if(opp != null) {
                    final int x = opp.getPosition().getX();
                    final int y = opp.getPosition().getY();
                    final int wildLevel = Combat.getWildLevel(x, y);
                    final boolean inMulti = Combat.isInMulti(opp.cE);
                    if(opp.getPosition().inPvPArea()) {
                        if(wildLevel <= 20 && !inMulti) {
                            Magic.teleport(player, opp.getPosition().getX(), opp.getPosition().getY(), opp.getPosition().getZ(), false);
                        } else {
                            DialogueManager.openDialogue(player, 171);
                        }
                    }
                }
                break;
            case 1193:
                Magic.preformCharge(player);
                break;
		 case 5003://rest button
		 //TODO Real resting added the rest option + sends action button
	           player.playAnimation(Animation.create(11786));
	      break;
			/**
			 * getInstantSet(player, HELM, AMULET, ARROWS, CAPE, BODY, LEGS, SHIELD, WEAPON, BOOTS, RING, GLOVES)
			 * 29174 - dh, 29175 - range 29176 - hybrid 29177 - pure
			 */

			case 21305:
				player.getActionSender().removeAllInterfaces();
				break;
			case - 28489:
				player.getActionSender().sendSidebarInterface(13, 31000);
				break;
			case 31054:
				player.getActionSender().sendSidebarInterface(13, 6299);
				break;
			case 28003:
				player.getActionSender().removeAllInterfaces();
				break;

            case 24589:
                player.sendMessage("Attempting to join event...");
                Events.joinEvent(player);
                break;
			case 29006:
				//GrandExchangeV2.openGE(player);
				break;
			case 29040:
				player.getExtraData().put("geshop", 1);
				player.getActionSender().sendUpdateItems(3823, player.getInventory().toArray());
				break;
			case 2461:
			case 8209:
			case 2471:
			case 8221:
				DialogueManager.openDialogue(player, player.getInterfaceState()
						.getNextDialogueId(0));
				break;
			case 2462:
			case 8210:
			case 2472:
			case 8222:
				DialogueManager.openDialogue(player, player.getInterfaceState()
						.getNextDialogueId(1));
				break;
			case 2473:
			case 8211:
			case 8223:
				DialogueManager.openDialogue(player, player.getInterfaceState()
						.getNextDialogueId(2));
				break;
			case 8212:
			case 8224:
				DialogueManager.openDialogue(player, player.getInterfaceState()
						.getNextDialogueId(3));
				break;
			case 8225:
				DialogueManager.openDialogue(player, player.getInterfaceState()
						.getNextDialogueId(4));
				break;
			case 30306:
				Magic.clickVengeance(player);
				break;
			case 30322:
				Magic.swapSpellbook(player);
				break;
			case 7455:
			case 1541:
			case 18470:
				Magic.openTeleMenu(player, 5);
				break;
			case 14873:
			case 14874:
			case 14875:
			case 14876:
			case 14877:
			case 14878:
			case 14879:
			case 14880:
			case 14881:
			case 14882:
				BankPin.clickPinButton(player, button);
				break;
			case 18132:
				ClanManager.leaveChat(player, true, false);
				break;
			case 27607:
				player.getActionSender().sendSidebarInterface(11, 904);
				break;
			case 23007:
				int i = 0;
				for(Item item : player.getInventory().toArray()) {
					if(item != null)
						Bank.deposit(player, i, item.getId(), item.getCount(), true);
					i++;
				}
				break;
			case 3041:
				if(player.getSpellBook().isAncient())
					player.getActionSender().sendSidebarInterface(6, 12855);
				else if(player.getSpellBook().isRegular())
					player.getActionSender().sendSidebarInterface(6, 1151);
				break;
	    /*
		 * case 5609: case 5610: case 5611: case 5612: case 5613: case 5614:
		 * case 5615: case 5616: case 5617: case 5618: case 5619: case 5620:
		 * case 5621: case 5622: case 5623: case 683: case 684: case 685:
		 * Prayer.actionButton(player,button); break;
		 */
			case 154:
				SkillcapeAnim.skillcapeEmote(player);
				break;
			case 9125: // Accurate
			case 6221: // range accurate
			case 22230: // kick (unarmed)
			case 48010: // flick (whip)
			case 21200: // spike (pickaxe)
			case 1080: // bash (staff)
			case 6168: // chop (axe)
			case 6236: // accurate (long bow)
			case 17102: // accurate (darts)
			case 8234: // stab (dagger)
			case 5862:// fists
			case 30088: // claws
			case 1177: // hammer
			case 2282: // stab
			case 2429:// scim
			case 12298: // Whip
			case 7768: // Claws Attack
			case 4685: // Spear Lunge
			case 3802: // Mace Pound
			case 1704: // Chop Battleaxe
			case 8468: // Halberd Jab
			case 5576:// Pickaxe Spike
			case 433:
				player.cE.setAtkType(0);
				if(player.debug)
					player.getActionSender().sendMessage(player.cE.getAtkType() + "");
				break;

			case 9126: // Defensive
			case 48008: // deflect (whip)
			case 22228: // punch (unarmed)
			case 21201: // block (pickaxe)
			case 1078: // focus - block (staff)
			case 6169: // block (axe)
			case 33019: // fend (hally)
			case 18078: // block (spear)
			case 8235: // block (dagger)
			case 1175: // accurate (darts)
			case 30089: // stab (dagger)
			case 5860:// fists
			case 2283: // stab
			case 2430:// scim
			case 12296: // Whip Def
			case 7769: // Claws Block
			case 4686: // Spear Block
			case 334: // Staff Block
			case 431: // Block Gmaul
			case 3803: // Mace Block
			case 1705: // Battleaxe block
			case 8466: // Halberd Fend
			case 5577: // Pickaxe Block
				player.cE.setAtkType(1);
				if(player.debug)
					player.getActionSender().sendMessage(player.cE.getAtkType() + "");
				break;
			case 9127: // Controlled
			case 48009: // lash (whip)
			case 33018: // jab (hally)
			case 6234: // longrange (long bow)
			case 6219: // longrange
			case 18077: // lunge (spear)
			case 18080: // swipe (spear)
			case 18079: // pound (spear)
			case 17100: // longrange (darts)

			case 2431:// scim
			case 12297:// WHip
				player.cE.setAtkType(3);
			case 1772:
			case 4454:
			case 1757:
				player.cE.setAtkType(4);
				break;
			case 1771:
			case 1756:
			case 4453:
				player.cE.setAtkType(5);
				break;
			case 1770:
			case 1755:
			case 4452:
				player.cE.setAtkType(6);
				break;
			case 2432: // Scimmy Slash
			case 2284: // stab
			case 9128: // Aggressive
			case 6220: // range rapid
			case 22229: // block (unarmed)
			case 21203: // impale (pickaxe)
			case 21202: // smash (pickaxe)
			case 1079: // pound (staff)
			case 6171: // hack (axe)
			case 6170: // smash (axe)
			case 33020: // swipe (hally)
			case 6235: // rapid (long bow)
			case 17101: // repid (darts)
			case 8237: // lunge (dagger)
			case 30091: // claws
			case 1176: // stat hammer
			case 8236: // slash (dagger)
			case 30090: // claws
			case 7771: // Claws
			case 7770: // Claws Lunge
			case 432: // Pummel
			case 4688: // Spear Swipe
			case 4687: // Spear Pound
			case 335: // Magic staff
			case 1707: // Battleaxe Hack
			case 1706: // Battleaxe Smash
			case 8467: // Halberd
			case 5579: // Pickaxe
			case 5578: // Pickaxe
			case 2285: // DDS stab
			case 5861:// fists
				player.cE.setAtkType(2);
				//player.getActionSender().sendMessage(player.cE.getAtkType() + "");
				break;

			case 1164:
			case 13035:
			case 30064:
			case 30075:
				// Magic.teleport(player,"edgeville");
				// Magic.openTeleMenu(player, 0);
				DialogueManager.openDialogue(player, 88);
				break;
			case 1167:
			case 13045:
			case 30106:
			case 30114:
				// Magic.teleport(player,"lumbridge");
				// Magic.openTeleMenu(player, 1);
				DialogueManager.openDialogue(player, 70);
				break;
			case 1170:
			case 13053:
			case 30138:
			case 30146:
				// Magic.teleport(player,"falnor");
				// Magic.openTeleMenu(player, 2);
				DialogueManager.openDialogue(player, 76);
				break;
			case 1174:
			case 13061:
			case 30162:
			case 30170:
				// Magic.teleport(player,"camelot");
				// Magic.openTeleMenu(player, 3);
				DialogueManager.openDialogue(player, 93);
				break;
			case 1540:
			case 13069:
			case 30226:
			case 30234:
				// Magic.teleport(player,"argonoue");
				// Magic.openTeleMenu(player, 4);
				DialogueManager.openDialogue(player, 81);
				break;
			case 1195:
			case 12856:
			case 30000:
				Magic.homeTeleport(player);
				break;
			case 12566:
				break;
			case 12568:
				player.getActionSender().removeAllInterfaces();
				break;
			case 3001:
			case 3002:
			case 3003:
			case 3004:
			case 3005:
			case 3006:
				Magic.clickNewTeleInterface(player, button);
				break;
			case 21012:
				synchronized(player) {
					int index = - 1;
					for(Item item : player.getInventory().toArray()) {
						index++;
						if(item != null)
							Bank.deposit(player, index, item.getId(), item.getCount(), true);
					}
				}
				break;
			case 21016:
				synchronized(player) {
					int index2 = - 1;
					for(Item item : player.getEquipment().toArray()) {
						index2++;
						if(item != null) {
							Bank.deposit(player, index2, item.getId(), item.getCount(),
									player.getEquipment(), false, true);
						}
					}
				}
				break;
			case 21304:
				synchronized(player) {
					int index2 = - 1;
					for(Item item : player.getInventory().toArray()) {
						index2++;
						if(item != null)
							BoB.deposit(player, index2, item.getId(), item.getCount());
					}
				}
				break;
			case 21308:
				synchronized(player) {
					int index2 = - 1;
                    if(player.getBoB() == null)
                        return;
					for(Item item : player.getBoB().toArray()) {
						index2++;
						if(item != null)
							BoB.withdraw(player, index2, item.getId(), item.getCount());
						if(player.getInventory().freeSlots() == 0)
							break;
					}
				}
				break;

            /** Grand Exchange **/
//            case 23715:
//                player.getGrandExchange().openOffer(0);
//                break;
//            case 23724:
//                player.getGrandExchange().openOffer(1);
//                break;
//            case 23733:
//                player.getGrandExchange().openOffer(2);
//                break;
//            case 23742:
//                player.getGrandExchange().openOffer(3);
//                break;
//            case 23751:
//                player.getGrandExchange().openOffer(4);
//                break;
//            case 23760:
//                player.getGrandExchange().openOffer(5);
//                break;
//            case 22187:
//            case 22723:
//                player.getGrandExchange().openOffers();
//                break;
//            case 23673:
//                player.getGrandExchange().newOffer(true, 0);
//                break;
//            case 23676:
//                player.getGrandExchange().newOffer(false, 0);
//                break;
//            case 23680:
//                player.getGrandExchange().newOffer(true, 1);
//                break;
//            case 23683:
//                player.getGrandExchange().newOffer(false, 1);
//                break;
//            case 23687:
//                player.getGrandExchange().newOffer(true, 2);
//                break;
//            case 23690:
//                player.getGrandExchange().newOffer(false, 2);
//                break;
//            case 23694:
//                player.getGrandExchange().newOffer(true, 3);
//                break;
//            case 23697:
//                player.getGrandExchange().newOffer(false, 3);
//                break;
//            case 23701:
//                player.getGrandExchange().newOffer(true, 4);
//                break;
//            case 23704:
//                player.getGrandExchange().newOffer(false, 4);
//                break;
//            case 23708:
//                player.getGrandExchange().newOffer(true, 5);
//                break;
//            case 23711:
//                player.getGrandExchange().newOffer(false, 5);
//                break;
//            case 22188:
//                player.getGrandExchange().cancelOffer();
//                break;
//            case 22713:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().decreaseQuantity();
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22714:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().increaseQuantity();
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22715:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().decreasePrice();
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22716:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().increasePrice();
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22686:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().addQuantity(1);
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22689:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().addQuantity(10);
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22692:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().addQuantity(100);
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22695:
//                if(player.getGrandExchange().getNewOffer() != null)
//                    player.getGrandExchange().getNewOffer().addQuantity(500);
//                player.getGrandExchange().refreshNewOffer();
//                break;
//
//            case 22701:
//                if(player.getGrandExchange().getNewOffer() != null) {
//                    int newPrice = (int)(player.getGrandExchange().getNewOffer().getPrice() - (player.getGrandExchange().getNewOffer().getPrice() * 0.02));
//                    if(newPrice <= 0)
//                        newPrice = 1;
//                    player.getGrandExchange().getNewOffer().setPrice(newPrice);
//                }
//                player.getGrandExchange().refreshNewOffer();
//                break;
//
//            case 22707:
//                if(player.getGrandExchange().getNewOffer() != null) {
//                    int newPrice = (int)(player.getGrandExchange().getNewOffer().getPrice() + (player.getGrandExchange().getNewOffer().getPrice() * 0.02));
//                    player.getGrandExchange().getNewOffer().setPrice(newPrice);
//                }
//                player.getGrandExchange().refreshNewOffer();
//                break;
//            case 22720:
//                if(player.getGrandExchange().getNewOffer() != null && player.getGrandExchange().getNewOffer().isSet())
//                    player.getGrandExchange().confirmOffer();
//                break;

            case 24595:
			case 12311:
			case 7587:
			case 8481:
			case 7537:
			case 7462:
			case 7487:
			case 7562:
			case 7687:
			case 7788:
			case 7612:
				if(player.duelAttackable > 0 && player.duelRule[10]) {
					player.getActionSender().sendMessage(
							"You cannot use special in this duel.");
					return;
				}
				player.specOn = !player.specOn;
				player.cE.deleteSpellAttack();
				SpecialAttacks.clickedSpecialButton(player);
				player.getSpecBar().sendSpecAmount();
				break;
			case 3420:
				player.tradeAccept1 = true;
				// player.openingTrade = false;
				Trade.finishTrade(player);
				break;
			case 3546:
				player.tradeAccept2 = true;
				// player.openingTrade = false;
				Trade.finishTrade(player);
				break;
			case 6674:
				player.tradeAccept1 = true;
				// player.openingTrade = false;
				Duel.finishTrade(player);
				break;
			case 6520:
				player.tradeAccept2 = true;
				// player.openingTrade = false;
				Duel.finishTrade(player);
				break;
			case 6725:// no range
				Duel.selectRule(player, 2, true, - 1);
				break;
			case 6726:// no melee
				Duel.selectRule(player, 3, true, - 1);
				break;
			case 6727:// no mage
				Duel.selectRule(player, 4, true, - 1);
				break;
			case 7816:// no spec
				Duel.selectRule(player, 10, true, - 1);
				break;
			case 670:// cannot switch
				Duel.selectRule(player, 9, true, - 1);
				break;
			case 6721:// no forfeit
				Duel.selectRule(player, 0, true, - 1);
				break;
			case 6728:// no drinks
				Duel.selectRule(player, 5, true, - 1);
				break;
			case 6729:// no food
				Duel.selectRule(player, 6, true, - 1);
				break;
			case 6730:// no prayer
				Duel.selectRule(player, 7, true, - 1);
				break;
			case 6722:// no movement
				Duel.selectRule(player, 1, true, - 1);
				break;
			case 6732:// obstacles
				Duel.selectRule(player, 8, true, - 1);
				break;
			case 13813: // no helm
				Duel.selectRule(player, 11, true, 0);
				break;

			case 13814: // no cape
				Duel.selectRule(player, 12, true, 1);
				break;

			case 13815: // no ammy
				Duel.selectRule(player, 13, true, 2);
				break;

			case 13817: // no weapon.
				Duel.selectRule(player, 14, true, 3);
				break;

			case 13818: // no body
				Duel.selectRule(player, 15, true, 4);
				break;

			case 13819: // no shield
				Duel.selectRule(player, 16, true, 5);
				break;

			case 13820: // no legs
				Duel.selectRule(player, 17, true, 7);
				break;

			case 13823: // no gloves
				Duel.selectRule(player, 18, true, 9);
				break;

			case 13822: // no boots
				Duel.selectRule(player, 19, true, 10);
				break;

			case 13821: // no rings
				Duel.selectRule(player, 20, true, 12);
				break;

			case 13816: // no arrows
				Duel.selectRule(player, 21, true, 13);
				break;

			case 15101:
				player.getActionSender().sendInterfaceInventory(15106, 3213);
				// player.getActionSender().sendUpdateItems(3823,player.getInventory().toArray());
				// player.getActionSender().showInterface(15106);
				break;
			case 16100:
				// player.getActionSender().showInterface(16460);
				player.getActionSender().sendInterfaceInventory(16460, 3213);
				// player.getActionSender().sendUpdateItems(3823,player.getInventory().toArray());
				break;
			case 16107:
				// player.getActionSender().showInterface(18100);
				player.getActionSender().sendInterfaceInventory(18100, 3213);
				// player.getActionSender().sendUpdateItems(3823,player.getInventory().toArray());
				break;

			case 27653:// equipment interface
				if(player.getTrader() == null) {
					player.getActionSender().sendInterfaceInventory(19148, 3213);
				} else {
					player.getActionSender().sendMessage("You can't do this right now!");
				}
				break;
			// item on death
			case 27654:
				player.getActionSender().sendInterfaceInventory(17100, 3213);
				LinkedList<Item> keepItems = new LinkedList<Item>();
				for(int j = 0; j < 4; j++) {
					keepItems.add(DeathDrops.keepItem(player, j, false));
				}
				for(int j = 0; j < 4; j++) {
					player.getActionSender().sendUpdateItem(10494, j, keepItems.get(j));
				}
				int indexcounter = 0;
				for(int j = 0; j < player.getInventory().capacity(); j++) {
					Item item = player.getInventory().get(j);
					if(item != null && ! keepItems.contains(item)) {
						player.getActionSender().sendUpdateItem(10600, indexcounter++, item);
					}
				}
				for(int j = 0; j < player.getEquipment().capacity(); j++) {
					Item item = player.getEquipment().get(j);
					if(item != null && ! keepItems.contains(item)) {
						player.getActionSender().sendUpdateItem(10600, indexcounter++, item);
					}
				}
				Item item = new Item(- 1, 1);
				for(; indexcounter < 28 + 11; indexcounter++) {
					player.getActionSender().sendUpdateItem(10600, indexcounter, item);
				}
				player.resetDeathItemsVariables();
				break;
            case 14921:
                if (player.bankPin.length() >= 4 && !player.bankPin.equals(player.enterPin)) {
                    player.resetingPin = true;
                    player.getActionSender().sendMessage("You need to first input your bank pin.");
                    BankPin.loadUpPinInterface(player);
                    return;
                } else {
                    player.getActionSender().sendMessage("Bank Pin successfully reset.");
                }
                break;
			case 14922:// close pin interface
			case 15110:
            case 28997:
				player.getActionSender().removeAllInterfaces();
				break;
			case 161:
				player.emoteTabPlay(Animation.CRY);
                break;
			case 19206:
			case 162:
				player.emoteTabPlay(Animation.THINKING);
                break;
			case 19207:
			case 163:
				player.emoteTabPlay(Animation.WAVE);
				break;
			case 19204:
			case 164:
				player.emoteTabPlay(Animation.BOW);
				break;
			case 19205:
			case 165:
				player.emoteTabPlay(Animation.ANGRY);
				break;
			case 19014:
			case 166:
				player.emoteTabPlay(Animation.DANCE);
				break;
			case 19010:
			case 167:
				player.emoteTabPlay(Animation.BECKON);
				break;
			case 19201:
			case 168:
				player.emoteTabPlay(Animation.YES_EMOTE);
				break;
			case 19203:
			case 169:
				player.emoteTabPlay(Animation.NO_EMOTE);
				break;
			case 19011:
			case 170:
				player.emoteTabPlay(Animation.LAUGH);
				break;
			case 19209:
			case 171:
				player.emoteTabPlay(Animation.CHEER);
				break;
			case 19107:
			case 172:
				player.emoteTabPlay(Animation.CLAP);
				break;
			case 19019:
			case 13362:
				player.emoteTabPlay(Animation.PANIC);
				break;
			case 19015:
			case 13363:
				player.emoteTabPlay(Animation.JIG);
				break;
			case 19016:
			case 13364:
				player.emoteTabPlay(Animation.SPIN);
				break;
			case 19017:
			case 13365:
				player.emoteTabPlay(Animation.HEADBANG);
				break;
			case 19012:
			case 13366:
				player.emoteTabPlay(Animation.JOYJUMP);
				break;
			case 19020:
			case 13367:
				player.emoteTabPlay(Animation.RASPBERRY);
				break;
			case 19013:
			case 13368:
				player.emoteTabPlay(Animation.YAWN);
				break;
			case 19109:
			case 13383:
				player.emoteTabPlay(Animation.GOBLIN_BOW);
				break;
			case 19110:
			case 13384:
				player.emoteTabPlay(Animation.GOBLIN_DANCE);
				break;
			case 19108:
			case 13369:
				player.emoteTabPlay(Animation.SALUTE);
				break;
			case 19208:
			case 13370:
				player.emoteTabPlay(Animation.SHRUG);
				break;
			case 19119:
			case 11100:
				player.emoteTabPlay(Animation.BLOW_KISS);
				break;
			case 19111:
			case 667:
				player.emoteTabPlay(Animation.GLASS_BOX);
				break;
			case 19112:
			case 6503:
				player.emoteTabPlay(Animation.CLIMB_ROPE);
				break;
			case 19113:
			case 6506:
				player.emoteTabPlay(Animation.LEAN);
				break;
			case 19114:
			case 666:
				player.emoteTabPlay(Animation.GLASS_WALL);
				break;
			case 2458:
				player.getActionSender().sendLogout();
				break;
			case 5387:
				player.getSettings().setWithdrawAsNotes(false);
				break;
			case 5386:
				player.getSettings().setWithdrawAsNotes(true);
				break;
			case 21008:
				if(player.getSettings().isWithdrawingAsNotes())
					player.getSettings().setWithdrawAsNotes(false);
				else
					player.getSettings().setWithdrawAsNotes(true);
				break;
			case 8130:
				player.getSettings().setSwapping(true);
				break;
			case 8131:
				player.getSettings().setSwapping(false);
				break;
			case 21000:
				if(player.getSettings().isSwapping())
					player.getSettings().setSwapping(false);
				else
					player.getSettings().setSwapping(true);
				break;
			case 19136:
			case 152:
				// player.getWalkingQueue().setRunningToggled(false);
				if(player.getWalkingQueue().isRunning() && !Rank.hasAbility(player, Rank.ADMINISTRATOR))
					player.getWalkingQueue().setRunningToggled(false);
				else if(!player.getWalkingQueue().isRunning()){
					player.getWalkingQueue().setRunningToggled(true);
				}
				if(Rank.hasAbility(player, Rank.ADMINISTRATOR) && player.getCombat().getFamiliar() != null) {
					SummoningSpecial.preformSpecial(player,
							SummoningSpecial.getCorrectSpecial(player.getCombat().getFamiliar().getDefinition().getId()));
				}
				break;
			case 153:// run
				player.getWalkingQueue().setRunningToggled(!player.getWalkingQueue().isRunningToggled());
				break;
            case 17015://summoning special
				if(player.getCombat().getFamiliar() != null)
				SummoningSpecial.preformSpecial(player,
						SummoningSpecial.getCorrectSpecial(player.getCombat().getFamiliar().getDefinition().getId()));
				player.getActionSender().sendString(38760, player.getSummBar().getAmount()+"");
				break;
            case 17023: //dismiss
				player.SummoningCounter = 0;
                player.getActionSender().sendSidebarInterface(16, -1);
				World.resetSummoningNpcs(player);
				player.getActionSender().sendMessage("You dismiss your familiar.");
				break;
            case 17038:
                //SummoningMonsters.renewFamiliar(player);
                break;
            case 17018:
                //SummoningMonsters.bobToInventory(player);
                break;
            case 17022:
                if(player.cE.summonedNpc == null) {
                    player.sendMessage("You don't have a summoned familiar");
                    break;
                }
                Position newlocation = player.getPosition().getCloseLocation();
                player.cE.summonedNpc.setTeleportTarget(newlocation);
                player.cE.summonedNpc.playGraphics(Graphic.create(1315));
                player.cE.summonedNpc.ownerId = player.getIndex();
                if(player.cE.getOpponent() != null) {
                    Combat.follow(player.cE.summonedNpc.cE, player.cE);
                    player.cE.summonedNpc.setInteractingEntity(player);
                }
                player.cE.summonedNpc.cE.setOpponent(player.cE.getOpponent());
                break;
		/* normal */
			case 1830:
			case 1831:
			case 1832:
			case 1833:
			case 1834:
			case 1835:
			case 1836:
			case 1837:
			case 1838:
			case 1839:
			case 1840:
			case 1841:
			case 1842:
			case 1843:
			case 1844:
			case 1845:
			/* ancients */
			case 13189:
			case 13241:
			case 13147:
			case 6162:
			case 13215:
			case 13267:
			case 13167:
			case 13125:
			case 13202:
			case 13254:
			case 13158:
			case 13114:
			case 13228:
			case 13280:
			case 13178:
			case 13136:
				player.cE.setAutoCastId(Magic.getAutoCastId(button));
				player.getActionSender().sendSidebarInterface(0, 328);
				player.getActionSender().sendClientConfig(108, 1);
				break;
			case 2004:
				player.cE.setAutoCastId(- 1);
				player.getActionSender().sendSidebarInterface(0, 328);
				player.getActionSender().sendClientConfig(108, 0);
				break;
			case 349:// intergrated to next case
				if(player.cE.getAutoCastId() > 0) {
					player.cE.setAutoCastId(- 1);
					player.getActionSender().sendSidebarInterface(0, 328);
					player.getActionSender().sendClientConfig(108, 0);
					break;
				}
			case 353:
			case 350:
				if(player.getSpellBook().isAncient())
					player.getActionSender().sendSidebarInterface(0, 1689);
				else
					player.getActionSender().sendSidebarInterface(0, 1829);
				break;
			case 6161:
				if(player.getSpellBook().isAncient())
					player.getActionSender().sendSidebarInterface(0, 328);
				break;
			case 151:
				player.autoRetailate = ! player.autoRetailate;
                player.getCombat().setOpponent(null);
                break;
			case 150:
				player.autoRetailate = ! player.autoRetailate;
                player.getCombat().setOpponent(null);
                break;
			case 25834:
				if(! player.splitPriv) {
					player.splitPriv = true;
					// player.getActionSender().sendClientConfig(502,1);
					player.getActionSender().sendClientConfig(287, 1);
				} else {
					player.splitPriv = false;
					// player.getActionSender().sendClientConfig(502,0);
					player.getActionSender().sendClientConfig(287, 0);
				}
				break;
			default:
				player.debugMessage("Unhandled action button : " + button);
				break;
		}
	}

}
