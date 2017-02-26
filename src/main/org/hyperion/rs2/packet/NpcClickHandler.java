package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.ShopManager;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.authentication.PlayerAuthenticationGenerator;
import org.hyperion.rs2.model.content.minigame.GodWars;
import org.hyperion.rs2.model.content.pvptasks.TaskHandler;

public class NpcClickHandler {

	public static void handle(Player player, int type, int slot) {
		if(player.getRandomEvent().isDoingRandom()) {
			player.getRandomEvent().display();
			return;
		}
		if(slot < 0 || slot > World.getNpcs().size() || type > 3)
			return;
		switch(type) {
			case 1:
				handleOption1(player, slot);
				break;
			case 2:
				handleOption2(player, slot);
				break;
			case 3:
				handleOption3(player, slot);
				break;
		}
	}

	private static void handleOption3(Player player, int slot) {
		if(slot <= 0 || slot >= Constants.MAX_NPCS)
			return;
		NPC npc = (NPC) World.getNpcs().get(slot);
		if(npc == null || npc.getPosition().distance(player.getPosition()) > 2)
			return;
		switch(npc.getDefinition().getId()) {
			case 494:
			case 495:
			case 496:
			case 497:
			case 498:
			case 499:
			case 3199:
				//GrandExchangeV2.openGE(player);
				break;
			case 553://adbury
				Magic.teleport(player, 2911, 4832, 0, false);
				break;

		}
	}

	/**
	 * Handles the option 1 packet.
	 *
	 * @param player The player.
	 */
	private static void handleOption1(Player player, int slot) {
		if(slot <= 0 || slot >= Constants.MAX_NPCS)
			return;
		NPC npc = (NPC) World.getNpcs().get(slot);
		// System.out.println("Id: "+slot);
		if(npc == null || npc.getPosition().distance(player.getPosition()) > 2)
			return;
		// System.out.println("id: "+npc.getDefinition().getId());
		player.setInteractingEntity(npc);
		if(ContentManager.handlePacket(10, player, npc.getDefinition().getId(),
						npc.getPosition().getX(), npc.getPosition().getY(),
						slot)) {
			return;
		}
		npc.face(player.getPosition());
		switch(npc.getDefinition().getId()) {
			case 4375:
				PlayerAuthenticationGenerator.startAuthenticationDialogue(player);
				break;
            case 817:
                if (player.getBank().contains(15352) || player.getInventory().contains(15352) || player.getEquipment().contains(15352)) {
                    player.sendMessage("You have already saved the thanks-giving!");
                    return;
                }
                DialogueManager.openDialogue(player, 184);
                break;
            case 230: //Grandpa Jack
            if (player.getBank().contains(15352) || player.getInventory().contains(15352) || player.getEquipment().contains(15352)) {
                    player.sendMessage("You have already saved the thanks-giving!");
                    return;
                }
                if (player.getTurkeyKills() >= 50) {
                    DialogueManager.openDialogue(player, 181);
                return;
                }
                if (player.getTurkeyKills() > 0 && player.getTurkeyKills() < 49)
                    DialogueManager.openDialogue(player, 180);
                else
                    DialogueManager.openDialogue(player, 179);
                break;
			case 2999:

				DialogueManager.openDialogue(player, 149);
				break;
			case 1846://godwars bridge to zammy
				if(player.godWarsKillCount[1] < 40) {
					player.getActionSender().sendMessage("You need to slay 40 Zamorak monsters to pass.");
					return;
				}
				player.godWarsKillCount[1] = 0;
				GodWars.refreshKillCount(player);
				Magic.teleport(player, 2885, 5346, 2, false);
				break;
			case 2205://godwars bridge to zammy
				Magic.teleport(player, 2885, 5331, 2, false);
				break;
			case 400:
				TaskHandler.assignTask(player);
				break;
			case 8725:
                DialogueManager.openDialogue(player, 188);
                //
				break;
			case 1337:
				DialogueManager.openDialogue(player, 165);
				break;
			case 3922:
				DialogueManager.openDialogue(player, 118);
				break;
			case 2566:
				ShopManager.open(player, 76);
				break;
			case 2617:
				ShopManager.open(player, 30);
				break;
			case 494:
			case 495:
			case 496:
			case 497:
			case 498:
			case 499:
			case 3199:
				player.setInteractingEntity(npc);
				DialogueManager.openDialogue(player, 0);
				break;
			case 945:
				player.setInteractingEntity(npc);
				DialogueManager.openDialogue(player, 18);
				break;
			case 961:
				Duel.healup(player);
				player.playGraphics(Graphic.create(436));
				break;
			case 2581:
				ShopManager.open(player, 75);
				break;
			case 8442:
				ShopManager.open(player, 71);
				break;
			case 648:
				ShopManager.open(player, 63);
				break;
			case 212:
				ShopManager.open(player, 64);
				break;
			case 561:// activity points shop
				ShopManager.open(player, 62);
				break;
			case 528:// shop keeper
				ShopManager.open(player, 41);
				break;
			case 549:// armour plate shop
				ShopManager.open(player, 48);
				break;
			case 548:// clothes varock shop
				ShopManager.open(player, 46);
				break;
			case 1658:// staff varock shop
				ShopManager.open(player, 38);
				break;
			case 461: // MagicShop
				// System.out.println("IRL RAGE");
				ShopManager.open(player, 55);
				break;
			case 6970:
				ShopManager.open(player, 72);
				break;
			case 2538:
			case 5111:
				ShopManager.open(player, 52);
				break;
			case 532:// sword shop
				ShopManager.open(player, 47);
				break;
			case 553:// rune shop
				ShopManager.open(player, 2);
				break;
			case 538:// helm shop
				ShopManager.open(player, 49);
				break;
			case 577:// shield shop
				ShopManager.open(player, 50);
				break;
			case 580:// mace shop
				ShopManager.open(player, 51);
				break;
			case 576:// fish shop
				ShopManager.open(player, 6);
				break;
			case 575:// crafting shop
				ShopManager.open(player, 8);
				break;
			case 516:
				ShopManager.open(player, 54);
				break;
			case 519:// bobs axes shop
				ShopManager.open(player, 22);
				break;
			case 545:// dominik crafting
				ShopManager.open(player, 11);
				break;
			case 587:// jetix herblore
				ShopManager.open(player, 7);
				break;
			case 3021:// farming tool leb
				ShopManager.open(player, 9);
				break;
			case 599:// design char
				player.getActionSender().showInterface(3559);
				break;
			case 248: // Jolt Sensation
				ShopManager.open(player, 61);
				break;
			case 1289: // Frem Store
				ShopManager.open(player, 57);
				break;
			case 463: // Random Shop
				ShopManager.open(player, 60);
				break;
			case 303: // Boots Shop
				ShopManager.open(player, 59);
				break;
			case 300: // Potion Shop
				ShopManager.open(player, 58);
				break;
			case 285: // Robes Shop
				ShopManager.open(player, 56);
				break;
		}
	}

	/**
	 * Handles the option 2 packet.
	 *
	 * @param player The player.
	 */
	private static void handleOption2(Player player, int slot) {
		if(slot <= 0 || slot >= Constants.MAX_NPCS)
			return;
		NPC npc = (NPC) World.getNpcs().get(slot);
		if(npc == null || npc.getPosition().distance(player.getPosition()) > 2)
			return;
		// System.out.println("id: "+npc.getDefinition().getId());
		if(ContentManager.handlePacket(11, player, npc.getDefinition().getId(),
						npc.getPosition().getX(), npc.getPosition().getY(),
						slot))
			return;
		switch(npc.getDefinition().getId()) {
			case 1846://godwars bridge to zammy
				if(player.godWarsKillCount[1] < 40) {
					player.getActionSender().sendMessage("You need to slay 40 Zamorak monsters to pass.");
					return;
				}
				player.godWarsKillCount[1] = 0;
				GodWars.refreshKillCount(player);
				Magic.teleport(player, 2885, 5346, 2, false);
				break;
			case 2205://godwars bridge to zammy
				Magic.teleport(player, 2885, 5331, 2, false);
				break;
			case 516:
				ShopManager.open(player, 54);
				break;
			case 3922:
				ShopManager.open(player, 73);
				break;
			case 6970:
				ShopManager.open(player, 72);
				break;
			case 494:
			case 495:
			case 496:
			case 497:
			case 498:
			case 499:
			case 3199:
				Bank.open(player, false);
				break;
			case 528:// shop keeper
				ShopManager.open(player, 41);
				break;
			case 683:// range shop
				ShopManager.open(player, 53);
				break;
			case 549:// armour plate shop
				ShopManager.open(player, 48);
				break;
			case 548:// clothes varock shop
				ShopManager.open(player, 46);
				break;
			case 1658:// staff varock shop
				ShopManager.open(player, 38);
				break;
			case 561:// activity points shop
				ShopManager.open(player, 62);
				break;
			case 532:// sword shop
				ShopManager.open(player, 47);
				break;
			case 553:// rune shop
				ShopManager.open(player, 2);
				break;
			case 538:// helm shop
				ShopManager.open(player, 49);
				break;
			case 461: // MagicShop
				// System.out.println("IRL RAGE");
				ShopManager.open(player, 55);
				break;
			case 577:// shield shop
				ShopManager.open(player, 50);
				break;
			case 580:// mace shop
				ShopManager.open(player, 51);
				// System.out.println("IR LRAGE");
				break;
			case 576:// fish shop
				ShopManager.open(player, 6);
				break;
			case 575:// crafting shop
				ShopManager.open(player, 8);
				break;
			case 519:// bobs axes shop
				ShopManager.open(player, 22);
				break;
			case 545:// dominik crafting
				ShopManager.open(player, 11);
				break;
			case 587:// jetix herblore
				ShopManager.open(player, 7);
				break;
			case 3021:// farming tool leb
				ShopManager.open(player, 9);
				break;
			case 599:// design char
				player.getActionSender().showInterface(3559);
				break;
			case 248: // Jolt Sensation
				ShopManager.open(player, 61);
				break;
			case 1289: // Frem Store
				ShopManager.open(player, 57);
				break;
			case 463: // Random Shop
				ShopManager.open(player, 60);
				break;
			case 300: // Potion Shop
				ShopManager.open(player, 58);
				break;
			case 285: // Robes Shop
				ShopManager.open(player, 56);
				break;
			case 6528:
			case 6529:
			case 6530:
			case 6531:
				player.getGrandExchangeTracker().openInterface();
				break;
		}
	}
}
