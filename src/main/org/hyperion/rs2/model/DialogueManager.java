package org.hyperion.rs2.model;

import org.hyperion.Configuration;
import org.hyperion.rs2.model.Animation.FacialAnimation;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.ShopManager;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.EP.EPExchange;
import org.hyperion.rs2.model.content.authentication.PlayerAuthenticationGenerator;
import org.hyperion.rs2.model.content.bounty.BountyPerkHandler;
import org.hyperion.rs2.model.content.bounty.BountyPerks.Perk;
import org.hyperion.rs2.model.content.minigame.Barrows3;
import org.hyperion.rs2.model.content.minigame.DangerousPK;
import org.hyperion.rs2.model.content.minigame.DangerousPK.ArmourClass;
import org.hyperion.rs2.model.content.minigame.RangingGuild;
import org.hyperion.rs2.model.content.misc.Starter;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.model.content.misc2.SkillCapeShops;
import org.hyperion.rs2.model.content.pvptasks.PvPTask;
import org.hyperion.rs2.model.content.specialareas.SpecialArea;
import org.hyperion.rs2.model.content.specialareas.SpecialAreaHolder;
import org.hyperion.rs2.model.content.transport.tabs.Tab;
import org.hyperion.rs2.model.sets.SetData;
import org.hyperion.rs2.model.sets.SetUtility;
import org.hyperion.rs2.net.ActionSender.DialogueType;
import org.hyperion.rs2.packet.ModerationOverride;
import org.hyperion.rs2.util.PushMessage;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.util.Calendar;
import java.util.Optional;


public class DialogueManager {

	public static void openDialogue(Player player, int dialogueId) {
		if(dialogueId == - 1 && player.tutIsland == 10) {
			player.getActionSender().removeAllInterfaces();
			return;
		}
		for(int i = 0; i < 5; i++) {
			player.getInterfaceState().setNextDialogueId(i, - 1);
		}
		player.getInterfaceState().setOpenDialogueId(dialogueId);
		if(player.getInteractingEntity() instanceof Player)
			return;
		NPC npc = (NPC) player.getInteractingEntity();
		if(ContentManager.handlePacket(20, player, dialogueId, 0, 0, 0))
			return;
		switch(dialogueId) {
			case 0:
				player.getActionSender().removeChatboxInterface();
				Bank.open(player, false);
				break;
			case 1:
				if(player.bankPin != null && !player.bankPin.equals("null")) {
					player.getActionSender().sendDialogue("Select an option", DialogueType.OPTION, -1, FacialAnimation.DEFAULT,
							"I'd like to access my bank account, please.",
							"I'd like to change my PIN please.");
				} else {
					player.getActionSender().sendDialogue("Select an option", DialogueType.OPTION, -1, FacialAnimation.DEFAULT,
							"I'd like to access my bank account, please.",
							"I'd like to set my PIN please.");
				}
				player.getInterfaceState().setNextDialogueId(0, 2);
				player.getInterfaceState().setNextDialogueId(1, 3);
				//player.getInterfaceState().setNextDialogueId(2, 4);
				break;
			case 2:
				player.getActionSender().removeChatboxInterface();
				Bank.open(player, false);
				break;
			case 3:
				if(player.bankPin != null && !player.bankPin.equals("null")) {
					player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, -1, FacialAnimation.DEFAULT,
							"I'd like to change my PIN please.");
				} else {
					player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, -1, FacialAnimation.DEFAULT,
							"I'd like to set my PIN please.");
				}
				player.getInterfaceState().setNextDialogueId(0, 4);
				break;
			case 4:
				player.getActionSender().removeAllInterfaces();
				Bank.open(player, true);
				break;
			case 5:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Bandit Camp",
						"Soul's Bane Dungeon.",
						"Master Area",
						"More"
				);
				player.getInterfaceState().setNextDialogueId(0, 10);
				player.getInterfaceState().setNextDialogueId(1, 11);
				player.getInterfaceState().setNextDialogueId(2, 12);
				player.getInterfaceState().setNextDialogueId(3, 9);
				break;
			case 10:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(800);
				Magic.teleport(player, 3172, 2980, 0, false);
				break;
			case 11:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(1400);
				Magic.teleport(player, 3300, 9825, 0, false);
				break;
			case 12:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(2000);
				Magic.teleport(player, 2717, 9803, 0, false);
				break;
			case 6:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(2000);
				Magic.teleport(player, 3007, 9550, 0, false);
				break;
			case 7:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(2000);
				Magic.teleport(player, 2703, 9564, 0, false);
				break;
			case 8:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(2000);
				Magic.teleport(player, 2884, 9798, 0, false);
				break;
			case 9://more
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Asgarnian Ice Dungeon",
						"Brimhaven Dungeon.",
						"Taverly Dungeon",
						"Next"
				);
				player.getInterfaceState().setNextDialogueId(0, 6);
				player.getInterfaceState().setNextDialogueId(1, 7);
				player.getInterfaceState().setNextDialogueId(2, 8);
				player.getInterfaceState().setNextDialogueId(3, 34);
				break;
			case 13:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Duel Arena",
						"Barrows Minigame",
						"GodWars",
						"Thzaar"
				);
				player.getInterfaceState().setNextDialogueId(0, 14);
				player.getInterfaceState().setNextDialogueId(1, 15);
				player.getInterfaceState().setNextDialogueId(2, 16);
				player.getInterfaceState().setNextDialogueId(3, 17);
				break;
			case 14:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(1000);
				player.getActionSender().sendMessage("hideru cool");
				Magic.teleport(player, 3371, 3274, 0, false);
				break;
			case 15:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(1000);
				Magic.teleport(player, 3564, 3288, 0, false);
				break;
			case 16:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(1000);
				Magic.teleport(player, 2881, 5310, 2, false);
				break;
			case 17:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(1000);
				Magic.teleport(player, 2480, 5175, 0, false);
				break;
			case 18:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Hello, welcome to " + Configuration.getString(Configuration.ConfigurationObject.NAME) + ", Please ensure you are active", "on the forums to keep updated with whats new.");
				player.getInterfaceState().setNextDialogueId(0, 19);
				break;
			case 19:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"To get to the main cities you may use your spellbook,", "however in order to train or visit minigames", "you must use your glory and ring of dueling.");
				player.getInterfaceState().setNextDialogueId(0, 20);
				break;
			case 20:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"You may begin your adventure by visiting barberian village", "and start training.");
				player.getInterfaceState().setNextDialogueId(0, 21);
				break;
			case 21:
	        /*player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
					"You can visit the highscores at: www.highscores.Jolt-Online.com");*/
				break;
			case 22:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"You already have a Slayer Task!", "Is this one too difficult?");
				player.getInterfaceState().setNextDialogueId(0, 23);
				break;
			case 23:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes I need a new one.",
						"No I just forgot what it was."
				);
				player.getInterfaceState().setNextDialogueId(0, 24);
				player.getInterfaceState().setNextDialogueId(1, 26);
				break;
			case 24:
				if(player.slayerCooldown <= 0) {
					player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
							"Haha Your incomptence mocks me,",
							"So to regain some respect heres a new task.");
					player.getInterfaceState().setNextDialogueId(0, 25);
				} else {
					player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
							"Keep trying at your current task,",
							"I'm sure you can do it!");
				}
				break;
			case 25:
				player.slayerTask = 0;
				break;
			case 26:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Your current task is to kill " + player.slayerAm,
						NPCDefinition.forId(player.slayerTask).getName().toLowerCase() + "'s.");
				player.getInterfaceState().setNextDialogueId(0, 27);
				break;
			case 27:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"Thanks for your help.");
				break;
			case 28:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Hi, How may I help you?");
				player.getInterfaceState().setNextDialogueId(0, 29);
				break;
			case 29:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"I'm looking for a master in the slayer skill.",
						"Do you know any around here?");
				player.getInterfaceState().setNextDialogueId(0, 30);
				break;
			case 30:
				if(npc != null){
					player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
							"Yes I do, Infact you've already found him.",
							"I'm the current grandmaster in slayer in all of glenior.");
					player.getInterfaceState().setNextDialogueId(0, 31);
				}
				break;
			case 31:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Could you give me a task to improve my slayer abilities?",
						"No sorry I think I have the wrong person."
				);
				player.getInterfaceState().setNextDialogueId(0, 32);
				player.getInterfaceState().setNextDialogueId(1, 27);
				break;
			case 32:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Sure no problem, Now lets find a task,",
						"worthy of a warrior like yourself.");
				player.getInterfaceState().setNextDialogueId(0, 25);
				break;
			case 33:
				player.getActionSender().sendDialogue("Slayer Master", DialogueType.NPC, 1599, FacialAnimation.DEFAULT,
						"Congratulations on completing your slayer task!",
						"Come see me whenever you would like a new one.");
				break;
			case 34://more
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Rock Crabs",
						"Greater Demons",
						"Back"
				);
				player.getInterfaceState().setNextDialogueId(0, 35);
				player.getInterfaceState().setNextDialogueId(1, 36);
				player.getInterfaceState().setNextDialogueId(2, 5);
				break;
			case 35:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2709, 3718, 0, false);
				break;
			case 36:
				player.getActionSender().removeAllInterfaces();
				player.removeAsTax(800);
				Magic.teleport(player, 2633, 9483, 2, false);
				break;
			case 37:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Hello " + player.getName() + ", I see that you are new here.");
				player.getInterfaceState().setNextDialogueId(0, 38);
				break;
			case 38:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"Yes, I am can you please tell me how can I get to the main land?");
				player.getInterfaceState().setNextDialogueId(0, 39);
				break;
			case 39:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Not so fast young one, ", "you have to know the basics before i let you leave this place");
				player.getInterfaceState().setNextDialogueId(0, 40);
				break;
			case 40:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"Sure, just tell me what i have to do and i'll get it done.");
				player.getInterfaceState().setNextDialogueId(0, 41);
				break;
			case 41:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Right, i have a small task for you....");
				player.getInterfaceState().setNextDialogueId(0, 64);
				break;
			case 64:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"I need you to find my guide book", "Its around here somewhere.");
				player.getInterfaceState().setNextDialogueId(0, 42);
				break;
			case 42:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Can you do this for me?");
				player.getInterfaceState().setNextDialogueId(0, 43);
				break;
			case 43:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"Sure, i'm on it.");
				player.tutIsland = 2;
				break;
			case 44:
				if(ContentEntity.isItemInBag(player, 1856)) {
					dialogueId = 45;
					player.tutIsland = 3;
				} else
					player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
							"Can't you find the book!?,", "well you cant leave till it's found...");
				break;
			case 45:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Well done " + player.getName() + ", now please", "read the guide to understand basic information about " + Configuration.getString(Configuration.ConfigurationObject.NAME));
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 46:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"Ok, thanks can i leave now?");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 47:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Not just yet, the main land is a dangerous place,", "you need to learn basic combat before i let you leave", "so you can defend yourself");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 48:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"ahh, fine tell me what i have to do now..");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 49:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Here take these combat items");
				ContentEntity.addItem(player, 1153, 1);
				ContentEntity.addItem(player, 1115, 1);
				ContentEntity.addItem(player, 1067, 1);
				ContentEntity.addItem(player, 1191, 1);
				ContentEntity.addItem(player, 1323, 1);
				player.tutIsland = 4;
				player.tutSubIsland = 0;
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 50:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Go next door and kill the chickens,", "once you kill them bring me the chicken");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 51:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"Right.... will do it right away");
				player.tutSubIsland = 0;
				player.tutIsland = 5;
				break;
			case 52:
				if(ContentEntity.isItemInBag(player, 2138)) {
					dialogueId = 53;
					player.tutIsland = 6;
				} else {
					player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
							"Too tough for you?", "sorry you can't leave till this is done");
				}
				break;
			case 53:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Ah, great job", "you may now leave to the main land");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 54:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"Alright cool! but before i leave can i get some food");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 55:
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"I'm kind of hungery");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 56:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"well sure, take this axe,", "tinderbox and cook the meat you got");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				ContentEntity.addItem(player, 1351, 1);
				ContentEntity.addItem(player, 590, 1);
				player.tutSubIsland = 0;
				player.tutIsland = 7;
				break;
			case 57:
				if(! ContentEntity.isItemInBag(player, 2140)) {
					player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
							"I'm still hungry, I better find some food soon.");
				} else {
					player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
							"I feel much better now that I have eaten");
					player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				}
				break;
			case 58:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Good, you may now leave,", "if you need help later on use the guide book");
				player.getInterfaceState().setNextDialogueId(0, dialogueId + 1);
				break;
			case 59:
				player.tutIsland = 10;
				Magic.teleport(player, 3105, 3420, 0, false);
				if(! player.oldFag) {
					player.oldFag = true;
					ContentEntity.addItem(player, 995, 35000);
					ContentEntity.addItem(player, 326, 50);
					ContentEntity.addItem(player, 316, 50);
					ContentEntity.addItem(player, 558, 100);
					ContentEntity.addItem(player, 556, 100);
					ContentEntity.addItem(player, 555, 100);
					ContentEntity.addItem(player, 554, 100);
					ContentEntity.addItem(player, 557, 100);
					ContentEntity.addItem(player, 841, 1);
					ContentEntity.addItem(player, 882, 100);
					ContentEntity.addItem(player, 1712, 1);
					ContentEntity.addItem(player, 2560, 1);
				}
				player.getActionSender().openQuestInterface(Configuration.getString(Configuration.ConfigurationObject.NAME) + " Guide Book", new String[]{
						"Congratulations on Completeing the begineers",
						"Tutorial you can now read your guide book",
						"To learn how to get started.",
						"on " + Configuration.getString(Configuration.ConfigurationObject.NAME) + ".com",
				});
				player.getSkills().reset();
				player.getActionSender().sendSkills();
				break;
			case 60://1,3,4,6
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"I need to talk to the guide.");
				break;
			case 61://2
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"I need to find the book to give it to the guide.");
				break;
			case 62://5
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"I need to kill some chickens", "To get some meat for the guide.");
				break;
			case 63://7
				player.getActionSender().sendDialogue(player.getName(), DialogueType.PLAYER, - 1, FacialAnimation.DEFAULT,
						"I need to use my axe to chop the trees next door,", "make a fire and cook the raw chicken i have");
				break;
			case 65:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"I found a Secret Crypt, I'm going in!",
						"This secret crypt looks scary, ill come back later."
				);
				player.getInterfaceState().setNextDialogueId(0, 66);
				player.getInterfaceState().setNextDialogueId(1, - 1);
				break;
			case 66:
				Barrows3.confirmCoffinTeleport(player);
				player.getActionSender().removeAllInterfaces();
				break;
			case 67:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Hi. How may I help you?");
				player.getInterfaceState().setNextDialogueId(0, 68);
				break;
			case 68:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Teleport me to Tzhaar!",
						"Nah, ill come back later."
				);
				player.getInterfaceState().setNextDialogueId(0, 69);
				player.getInterfaceState().setNextDialogueId(1, - 1);
				break;
			case 69:
				Magic.teleport(player, 2439, 5171, 0, false);
				player.getActionSender().removeAllInterfaces();
				break;
			/*

 Guide: "Good, you may now leave, if you need help later on use the guide book"
 the Guide then performs a animation and a gfx appears (tele another person gfx) and the players ends up in the main land
 the coords for that will be on barb village bridge to varrock

	
			 */

			case 70:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Experiments",
						"Taverly Dungeon",
						"Slayer Tower",
						"Brimhaven Dungeon",
						"Hill Giants"
				);
				player.getInterfaceState().setNextDialogueId(0, 71);
				player.getInterfaceState().setNextDialogueId(1, 72);
				player.getInterfaceState().setNextDialogueId(2, 73);
				player.getInterfaceState().setNextDialogueId(3, 74);
				player.getInterfaceState().setNextDialogueId(4, 75);
				break;
			case 71:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3570 - Misc.random(1), 9953 - Misc.random(1), 0, false);
				break;
			case 72:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2884, 9798, 0, false);
				break;
			case 73:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3428, 3535, 0, false);
				break;
			case 74:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2703, 9564, 0, false);
				break;
			case 75:
				player.getActionSender().removeAllInterfaces();
				//Magic.teleport(player,2709,3718,0);
				break;
			case 76:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Barrows",
						"Tzhaar Caves",
						"Duel Arena",
						"Warriors Guild",
						"Ranging Guild"
				);
				player.getInterfaceState().setNextDialogueId(0, 77);
				player.getInterfaceState().setNextDialogueId(1, 78);
				player.getInterfaceState().setNextDialogueId(2, 79);
				player.getInterfaceState().setNextDialogueId(3, 80);
				player.getInterfaceState().setNextDialogueId(4, 87);
				break;
			case 77:
				player.getActionSender().removeAllInterfaces();
				player.getActionSender().sendMessage("hideru cool");
				Magic.teleport(player, 3564, 3288, 0, false);
				break;
			case 78:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2480, 5175, 0, false);
				break;
			case 79:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3375, 3274, 0, false);
				break;
			case 80:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2875, 3546, 0, false);
				break;
			case 81:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Godwars", "King Black Dragon (Wild)", "Dagannoth Kings", "Chaos Elemental (Wild)", "Corporal Beast"
				);
				player.getInterfaceState().setNextDialogueId(0, 82);
				player.getInterfaceState().setNextDialogueId(1, 83);
				player.getInterfaceState().setNextDialogueId(2, 84);
				player.getInterfaceState().setNextDialogueId(3, 85);
				player.getInterfaceState().setNextDialogueId(4, 86);
				break;
			case 82:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2882, 5310, 2, false);
				break;
			case 83:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3007, 3849, 0, false);
				break;
			case 84:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 1910, 4367, 0, false);
				break;
			case 85:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3295, 3921, 0, false);
				break;
			case 86:
				player.getActionSender().removeAllInterfaces();
				//Magic.teleport(player,3242,9364,0);
				break;
			case 87:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2652, 3439, 0, false);
				break;
			case 88:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Lumbridge", "Varrock", "Edgeville", "Falador", "Camelot"
				);
				player.getInterfaceState().setNextDialogueId(0, 89);
				player.getInterfaceState().setNextDialogueId(1, 90);
				player.getInterfaceState().setNextDialogueId(2, 91);
				player.getInterfaceState().setNextDialogueId(3, 92);
				player.getInterfaceState().setNextDialogueId(4, 99);
				break;
			case 89:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3222, 3218, 0, false);
				break;
			case 90:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3210, 3424, 0, false);
				break;
			case 91:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3084, 3484, 0, false);
				break;
			case 92:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2964, 3372, 0, false);
				break;
			case 99:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2757, 3478, 0, false);
				break;

			case 93:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Mage Bank", "Edgeville Dragons", "Mid Wilderness", "Chaos Altar", "Fun Pk (Safe)"
				);
				player.getInterfaceState().setNextDialogueId(0, 94);
				player.getInterfaceState().setNextDialogueId(1, 95);
				player.getInterfaceState().setNextDialogueId(2, 96);
				player.getInterfaceState().setNextDialogueId(3, 97);
				player.getInterfaceState().setNextDialogueId(4, 106);
				break;
			case 94:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2539, 4716, 0, false);
				break;
			case 95:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2983, 3596, 0, false);
				break;
			case 96:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2982, 3733, 0, false);
				break;
			case 97:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3237, 3639, 0, false);
				break;
			case 98:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3300, 2795, 0, false);
				break;
			case 100:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Mining", "Smithing", "Fishing/Cooking", "Agility", "Farming/Woodcutting"
				);
				player.getInterfaceState().setNextDialogueId(0, 101);
				player.getInterfaceState().setNextDialogueId(1, 102);
				player.getInterfaceState().setNextDialogueId(2, 103);
				player.getInterfaceState().setNextDialogueId(3, 104);
				player.getInterfaceState().setNextDialogueId(4, 105);
				break;
			case 101:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3046, 9779, 0, false);
				break;
			case 102:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3079, 9502, 0, false);
				break;
			case 103:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2597, 3408, 0, false);
				break;
			case 104:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2724, 3484, 0, false);
				break;
			case 105:
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2812, 3463, 0, false);
				break;
			case 106:
				/*player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Multi Arena (Safe)", "Singles (Safe)"
				);
				player.getInterfaceState().setNextDialogueId(0, 107);
				player.getInterfaceState().setNextDialogueId(1, 108);*/
				player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, Position.create(2594, 3156, 0), false);
				break;
			case 107:
				/*player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 2528 + Combat.random(6), 3303 + Combat.random(5), 0, false);*/
				break;
			case 108:
				/*player.getActionSender().removeAllInterfaces();
				Magic.teleport(player, 3290 + Combat.random(6), 3025 + Combat.random(5), 0, false);*/
				break;
			case 109:

				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Ranged Pure", "Hybrid Pure", "Berserker Pure", "Main", "Skiller"
				);
				player.getInterfaceState().setNextDialogueId(0, 110);
				player.getInterfaceState().setNextDialogueId(1, 111);
				player.getInterfaceState().setNextDialogueId(2, 112);
				player.getInterfaceState().setNextDialogueId(3, 113);
				player.getInterfaceState().setNextDialogueId(4, 114);
				break;
			case 110:
				Starter.giveRangedPure(player);
				player.getActionSender().removeAllInterfaces();
				player.getActionSender().showInterface(3559);
				player.receivedStarter = true;
				break;
			case 111:
				player.getActionSender().removeAllInterfaces();
				Starter.giveHybridPure(player);
				player.getActionSender().showInterface(3559);
				player.receivedStarter = true;
				break;
			case 112:
				player.getActionSender().removeAllInterfaces();
				Starter.giveBerserker(player);
				player.getActionSender().showInterface(3559);
				player.receivedStarter = true;
				break;
			case 113:
				player.getActionSender().removeAllInterfaces();
				Starter.giveMain(player);
				player.getActionSender().showInterface(3559);
				player.receivedStarter = true;
				break;
			case 114:
				player.getActionSender().removeAllInterfaces();
				Starter.giveSkiller(player);
				player.getActionSender().showInterface(3559);
				player.receivedStarter = true;
				break;
			case 115:

				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Flux Account", "DeviousPK Account"
				);
				player.getInterfaceState().setNextDialogueId(0, 116);
				player.getInterfaceState().setNextDialogueId(1, 117);
				break;
			case 116:
				break;
			case 117:
				break;
			case 118:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Hello, Do you want to exchange your", "implings for rewards?");
				player.getInterfaceState().setNextDialogueId(0, 119);
				break;
			case 119:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes please", "No thank you"
				);
				player.getInterfaceState().setNextDialogueId(0, 120);
				player.getInterfaceState().setNextDialogueId(1, - 1);
				break;
			case 120:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"I've forgotten how to open these..", "Maybe try looting them yourself?");
				break;
			case 121:
				player.getActionSender().sendDialogue("Select a Spellbook", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Modern Spellbook", "Ancient Spellbook"
				);
				player.getInterfaceState().setNextDialogueId(0, 122);
				player.getInterfaceState().setNextDialogueId(1, 123);
				break;
			case 122:
				player.getSpellBook().changeSpellBook(SpellBook.REGULAR_SPELLBOOK);
				player.getActionSender().sendSidebarInterface(6, 1151);
				player.getActionSender().removeAllInterfaces();
				break;
			case 123:
				player.getSpellBook().changeSpellBook(SpellBook.ANCIENT_SPELLBOOK);
				player.getActionSender().sendSidebarInterface(6, 12855);
				player.getActionSender().removeAllInterfaces();
				break;
			case 124:
				player.getActionSender().sendDialogue("Talk About", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Ranging Minigame", "Ranging SkillCape"
				);
				player.getInterfaceState().setNextDialogueId(0, 125);
				player.getInterfaceState().setNextDialogueId(1, 128);//TODO
				break;
			case 125:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Hi, Do you want ", "to play a game?", "You might win cool awards!");
				player.getInterfaceState().setNextDialogueId(0, 126);
				break;
			case 126:
				player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"All you have to do ", "is fire these targets!", "To enter you must pay 5000 gp");
				player.getInterfaceState().setNextDialogueId(0, 127);
				break;
			case 127:
				player.getActionSender().sendDialogue("Do you want to play?", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes sure", "Sorry, maybe later"
				);
				player.getInterfaceState().setNextDialogueId(0, 129);
				player.getInterfaceState().setNextDialogueId(1, - 1);//TODO
				break;
			case 128:
				SkillCapeShops.openSkillCapeShop(player, 3000 + Misc.random(1));
				break;
			case 129:
				RangingGuild.buyShots(player);
				player.getActionSender().removeAllInterfaces();
				break;
			case 130:
				if(npc != null)
					player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
							"Hello there, I am the dice game hoster.", "If I throw 55 or higher I'll double your item!", "But if I throw less than 55, you'll lose your item!");
				player.getInterfaceState().setNextDialogueId(0, 131);
				break;
			case 131:
				if(npc.getDefinition() != null) {
					player.getActionSender().sendDialogue(npc.getDefinition().getName(), DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
							"To play, simply give an item to me.", "@dre@(Use the item of the gambler)");
					player.getInterfaceState().setNextDialogueId(0, -1);
				}
				break;
			case 132:
				//player.getActionSender().sendDialogue("Santa Claus", DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
				//		"Hello Adventurer", "I have a challenge for you..");
				/////player.getInterfaceState().setNextDialogueId(0, 133);
				player.getActionSender().sendDialogue("Santa Claus", DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Ho Ho Ho", "Visit me on Christmas for a special gift!");
				player.getInterfaceState().setNextDialogueId(0, -1);
				break;
			case 133:
				player.getActionSender().sendDialogue("Santa Claus", DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"I need you to catch", "20 implings", "to help the world out!");
				player.getInterfaceState().setNextDialogueId(0, 134);
				break;
			case 134:
				player.getActionSender().sendDialogue("Will you do this?", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes I will", "Sorry, maybe later"
				);
				player.getInterfaceState().setNextDialogueId(0, - 1);//TODO
				break;
			case 135:
				player.getActionSender().sendDialogue("Santa Claus", DialogueType.NPC, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Enjoy your surprise!");
				player.getInterfaceState().setNextDialogueId(0, - 1);//TODO
				break;
			case 136:
				player.getActionSender().sendDialogue("Moderation Options", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Kick",
						"Jail",
						"Mute"
				);
				player.getInterfaceState().setNextDialogueId(0, 137);
				player.getInterfaceState().setNextDialogueId(1, 138);
				player.getInterfaceState().setNextDialogueId(2, 139);
				break;
			case 137:
				if(ModerationOverride.canModerate(player) && Rank.getPrimaryRank(player).ordinal() > Rank.getPrimaryRank(player.getModeration()).ordinal())
					ModerationOverride.kickPlayer(player.getModeration());
				player.getActionSender().removeAllInterfaces();
				PushMessage.pushStaffMessage("I just kicked " + player.getModeration().getSafeDisplayName() + ".", player);
				player.setModeration(null);
				break;
			case 138:
				if(ModerationOverride.canModerate(player) && Rank.getPrimaryRank(player).ordinal() > Rank.getPrimaryRank(player.getModeration()).ordinal())
					ModerationOverride.jailPlayer(player.getModeration());
				player.getActionSender().removeAllInterfaces();
				PushMessage.pushStaffMessage("I just jailed " + player.getModeration().getSafeDisplayName() + ".", player);
				player.setModeration(null);
				break;
			case 139:
				if(ModerationOverride.canModerate(player) && Rank.getPrimaryRank(player).ordinal() > Rank.getPrimaryRank(player.getModeration()).ordinal())
					ModerationOverride.mutePlayer(player.getModeration());
				player.getActionSender().removeAllInterfaces();
				PushMessage.pushStaffMessage("I just muted " + player.getModeration().getSafeDisplayName() + ".", player);
				player.setModeration(null);
				break;
			case 140:
				player.getActionSender().sendDialogue("Sir", DialogueType.NPC, npc.getDefinition().getId() , FacialAnimation.DEFAULT,
						"Hello I'm the master of PvP Tasks!", "Would you like a task?");
				player.getInterfaceState().setNextDialogueId(0, 141);//TODO
				break;
			case 141:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes!",
						"No..."
				);
				player.getInterfaceState().setNextDialogueId(0, 142);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 142:
				if(player.getPvPTask() == null || player.getPvPTaskAmount() <= 0) {
					player.setPvPTask(null);
					player.setPvPTaskAmount(0);
					player.setPvPTask(PvPTask.toTask(Combat.random(2) + 1));
					player.setPvPTaskAmount(Combat.random(10) + 10);
					//DialogueManager.openDialogue(player, 139);
					player.getActionSender().sendDialogue("Sir", DialogueType.NPC, npc.getDefinition().getId() , FacialAnimation.DEFAULT,
							"I've assigned you "+player.getPvPTaskAmount()+" "+PvPTask.toString(player.getPvPTask())+"s to kill!");
				} else {
					player.getActionSender().sendDialogue("Sir", DialogueType.NPC, npc.getDefinition().getId() , FacialAnimation.DEFAULT,
							"You still have "+player.getPvPTaskAmount()+" "+PvPTask.toString(player.getPvPTask())+"s to kill!");
				}
				player.getInterfaceState().setNextDialogueId(0, - 1); //exit
				break;
			case 143:
				player.getActionSender().sendDialogue("Are you sure?", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes, I want to empty my inventory!",
						"Do not empty my inventory"
				);
				player.getInterfaceState().setNextDialogueId(0, 144);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 144:
				player.getSkills().stopSkilling();
				for(int i = 0; i < 28; i++) {
					player.getSkills().stopSkilling();
					Item item = player.getInventory().get(i);
					if(item == null) continue;
					int itemId = item.getId();
					if(itemId == 12747 || itemId == 12744 || itemId == 18509 || itemId == 19709 || itemId == 15707)
						continue;
					player.getExpectedValues().removeItemFromInventory("Emptying", item);
					player.getInventory().remove(item);

				}
				player.getActionSender().removeChatboxInterface();
				break;
			case 145:
				player.getActionSender().sendDialogue("You protect 0 items in here!", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes! I'm a brave warrior!",
						"No..."
				);
				player.getInterfaceState().setNextDialogueId(0, 146);
				player.getInterfaceState().setNextDialogueId(1, 147);
				//player.getInterfaceState().setNextDialogueId(2, 148);
				break;
			case 146:
				player.getActionSender().removeChatboxInterface();
				DangerousPK.toWaitArea(player);
				break;
			case 147:
				player.getActionSender().removeChatboxInterface();
				break;
			case 148:
				player.pickedClass = ArmourClass.RANGE;
				DangerousPK.toWaitArea(player);
				break;

			case 149:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Information",
						"View bank"
				);
				player.getInterfaceState().setNextDialogueId(0, 130);
				player.getInterfaceState().setNextDialogueId(1, 150);
				break;
			case 150:
				if(Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
					player.getActionSender().sendInterfaceInventory(5292, 5063);
					player.getActionSender().sendUpdateItems(5382, Dicing.getGambledItems());
				} else {
					player.getActionSender().sendMessage("You are not a high enough rank to view this");
					player.getActionSender().removeAllInterfaces();
				}
				break;
			case 151:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Hybridding Area",
						"Oldschool Pk Area",
						"Fun Pk Area",
						"Pure Pk Area"
				);
				player.getInterfaceState().setNextDialogueId(0, 152);
				player.getInterfaceState().setNextDialogueId(1, 153);
				player.getInterfaceState().setNextDialogueId(2, 154);
				player.getInterfaceState().setNextDialogueId(3, 186);
				break;
			case 152:
				Magic.goTo13s(player);
				player.getActionSender().removeChatboxInterface();
				break;
			case 153:
				SpecialAreaHolder.get("ospk").ifPresent(s -> s.enter(player));
				player.getActionSender().removeChatboxInterface();
				break;
			case 154:
				Magic.teleport(player, Position.create(2594, 3156, 0), false);
				player.getActionSender().removeChatboxInterface();
				break;
			case 155:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Set my levels please!",
						"I'd like to keep my current levels"
				);
				player.getInterfaceState().setNextDialogueId(0, 156);
				player.getInterfaceState().setNextDialogueId(1, 157);
				break;
			case 156:
				//i know i should declare it outside as prviate static final but 2 lazy
				final int[][] skillData = {
						{Skills.ATTACK, 60},
						{Skills.DEFENCE, 1},
						{Skills.PRAYER, 52},
						{Skills.RANGED, 99},
						{Skills.MAGIC, 99}
				};
				if(!player.canSpawnSet())
					return;
				for(int i = 0; i < skillData.length; i++) {
					player.getSkills().setLevel(skillData[i][0], skillData[i][1]);
					player.getSkills().setExperience(skillData[i][0], Skills.getXPForLevel(skillData[i][1]));
				}
				player.resetPrayers();
				SetUtility.getInstantSet(player, SetData.getPureSet());
				SetUtility.addSetOfItems(player, SetData.getPureItems());
				player.getActionSender().removeChatboxInterface();
				break;
			case 157:
				if(!player.canSpawnSet())
					return;
				if(player.getSkills().getLevels()[Skills.ATTACK] >= 60 && player.getSkills().getLevels()[Skills.RANGED] >= 70) {
					SetUtility.getInstantSet(player, SetData.getPureSet());
					SetUtility.addSetOfItems(player, SetData.getPureItems());
				} else {
					player.sendMessage("You need 60 attack and 70 ranged to spawn this instant set!");
				}
				break;
			case 158:
				Magic.teleport(player, Position.create(2373, 4972, 0), false, false);
				player.getActionSender().removeChatboxInterface();
				break;
			case 161:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Set my levels please!",
						"I'd like to keep my current levels"
				);
				player.getInterfaceState().setNextDialogueId(0, 162);
				player.getInterfaceState().setNextDialogueId(1, 163);
				break;
			case 162:
				//i know i should declare it outside as prviate static final but 2 lazy
				final int[][] skillDataZ = {
						{Skills.ATTACK, 75},
						{Skills.DEFENCE, 45},
						{Skills.PRAYER, 95},
				};
				if(!player.canSpawnSet())
					return;
				for(int i = 0; i < skillDataZ.length; i++) {
					player.getSkills().setLevel(skillDataZ[i][0], skillDataZ[i][1]);
					player.getSkills().setExperience(skillDataZ[i][0], Skills.getXPForLevel(skillDataZ[i][1]));
				}
				SetUtility.getInstantSet(player, SetData.getZerkSet());
				SetUtility.addSetOfItems(player, SetData.getZerkItems());
				player.getActionSender().removeChatboxInterface();
				break;
			case 163:
				if(!player.canSpawnSet())
					return;
				if(player.getSkills().getLevels()[Skills.ATTACK] >= 70 && player.getSkills().getLevels()[Skills.DEFENCE] >= 45) {
					SetUtility.getInstantSet(player, SetData.getZerkSet());
					SetUtility.addSetOfItems(player, SetData.getZerkItems());
				} else {
					player.sendMessage("You need 70 attack and 45 defense to spawn this instant set!");
				}
				break;
			case 165:
				player.getActionSender().sendDialogue("Upgrade ("+player.getBHPerks().calcNextPerkCost()+" BHPts)", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Upgrade my perks!",
						"What are perks?"
				);
				player.getInterfaceState().setNextDialogueId(0, 169);
				player.getInterfaceState().setNextDialogueId(1, 170);

				break;
			case 166:
				BountyPerkHandler.upgrade(player, Perk.SPEC_RESTORE);
				break;
			case 167:
				BountyPerkHandler.upgrade(player, Perk.VENG_REDUCTION);
				break;
			case 168:
				BountyPerkHandler.upgrade(player, Perk.PRAY_LEECH);
				break;
			case 169:
				player.getActionSender().sendDialogue("Upgrade ("+player.getBHPerks().calcNextPerkCost()+" BHPts)", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Special Restore",
						"Veng Time Reduction",
						"Prayer Leeching"
				);

				player.getInterfaceState().setNextDialogueId(0, 166);
				player.getInterfaceState().setNextDialogueId(1, 167);
				player.getInterfaceState().setNextDialogueId(2, 168);
				break;
			case 170:
				player.getActionSender().removeChatboxInterface();
				player.sendMessage("Special Perk: @blu@Increase special after a kill", "@red@(I) 10% (II) 20% (III) 40%",
						"Veng Reduction Perk: @blu@Reduce time for next vengeance", "@red@(I) 4seconds (II)8seconds (II)16seconds",
						"Prayer Leech Perk: @blu@Leech your opponent's prayer (stacks with soulsplit & smite)");
				break;
			case 171:
				player.getActionSender().sendDialogue("BH Master", DialogueType.NPC, 1337, FacialAnimation.DEFAULT,
						"Your opponent is in deep or multi wilderness...", "Are you sure you want to teleport to him/her?");
				player.getInterfaceState().setNextDialogueId(0, 172);//TODO
				break;
			case 172:
				player.getActionSender().sendDialogue("Upgrade ("+player.getBHPerks().calcNextPerkCost()+" BHPts)", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes",
						"No"
				);
				player.getInterfaceState().setNextDialogueId(0, 173);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 173:
				final Player opp = player.getBountyHunter().getTarget();
				if(opp != null) {
					final Tab tab = Tab.getByIntegerValue(18806);
					if (tab != null) {
						tab.process(player);
					}
				}
				break;
			case 174:
				player.getActionSender().sendDialogue("Select an option", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"I would like to have a new assignment.",
						"Remove current slayer task (20 pts).",
						"I would like to view the slayer store.",
						"I would like to reset my task progress (lose total task streak + 1 pt)." ,
						"I would like to view the PvM Store"
				);

				player.getInterfaceState().setNextDialogueId(0, 175);
				player.getInterfaceState().setNextDialogueId(1, 176);
				player.getInterfaceState().setNextDialogueId(2, 177);
				player.getInterfaceState().setNextDialogueId(3, 178);
				player.getInterfaceState().setNextDialogueId(4, 193);
				break;
			case 175:
				final String toDisplay;
				if(player.getSlayer().assignTask(player.getSkills().getRealLevels()[Skills.SLAYER]))
					toDisplay = String.format("You have %d %s to kill!", player.getSlayer().getTaskAmount(), player.getSlayer().getTask());
				else
					toDisplay = String.format("You still have %d %s to kill", player.getSlayer().getTaskAmount(), player.getSlayer().getTask() );
				player.getActionSender().sendDialogue("Slayer Master", DialogueType.NPC, npc.getDefinition().getId() , FacialAnimation.DEFAULT,
						toDisplay);

				break;
			case 176:
				player.getActionSender().sendDialogue("Are you sure?", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes I'm sure I want to use 20 slayer points",
						"Nevermind."
				);
				player.getInterfaceState().setNextDialogueId(0, 187);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 187:
				if(player.getSlayer().resetTask())
					player.sendMessage("Your task has been successfully reset!");
				else
					player.sendMessage("You need more slayer points to reset your task!");
				player.getActionSender().removeChatboxInterface();
				break;
			case 177:
				ShopManager.open(player, 77);
				break;
			case 178:
				if(player.getSlayer().getSlayerPoints() < 1) {
					player.getActionSender().sendMessage("You need at least 1 slayer point to do this!");
					player.getActionSender().removeChatboxInterface();
					return;
				}
				player.getActionSender().sendDialogue("Are you sure?", DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
						"Yes I'm sure. I want to reset my task progress!",
						"Nevermind."
				);
				player.getInterfaceState().setNextDialogueId(0, 200);
				player.getInterfaceState().setNextDialogueId(1, 201);
				break;
			case 200:
				player.getSlayer().removeTask();
				player.getActionSender().sendMessage("You now have 0 total tasks and your task has been reset!");
				player.getActionSender().removeChatboxInterface();
				break;
			case 201:
				player.getActionSender().removeChatboxInterface();
				break;
			case 186: //tele to pure pk
				final Optional<SpecialArea> purePk = SpecialAreaHolder.get("purepk");
				if(purePk.isPresent()) {
					final SpecialArea area = purePk.get();
					area.enter(player);
				}
				break;

			case 188:
				player.getActionSender().sendDialogue("Select an Option", DialogueType.OPTION, npc.getDefinition().getId(), FacialAnimation.DEFAULT,
						"Exchange my PvP Artifacts for Pk points",
						"Exchange my Emblems for points",
						"Open up Emblem point shop");
				player.getInterfaceState().setNextDialogueId(0, 189);
				player.getInterfaceState().setNextDialogueId(1, 190);
				player.getInterfaceState().setNextDialogueId(2, 191);
				break;
			case 189:
				if(EPExchange.exchangeDrops(player))
					player.getActionSender().sendMessage(
							"You have exchanged your statues.");
				else
					player.getActionSender().sendMessage(
							"You don't have any statues with you.");
				player.getActionSender().removeChatboxInterface();
				break;
			case 190:
				player.getActionSender().sendDialogue("Are you sure?", DialogueType.OPTION, 1, FacialAnimation.DEFAULT,
						"Exchange my emblems for " + player.getBountyHunter().emblemExchangePrice() + " emblem points",
						"Nevermind");
				player.getInterfaceState().setNextDialogueId(0, 192);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 191:
				ShopManager.open(player, 78);
				break;
			case 192:
				player.getBountyHunter().exchangeEmblems();
				player.sendf("You now have %d emblem points", player.getBountyHunter().getEmblemPoints());
				player.getActionSender().removeChatboxInterface();
				break;
			case 193:
				ShopManager.open(player, 82);
				break;
			case 194:
				player.getActionSender().sendDialogue("Pick an option", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "Teleport me to level 31 wilderness", "Stay here");
				player.getInterfaceState().setNextDialogueId(0, 195);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 195:
				Magic.teleport(player, Position.create(2975, 3745, 0), false);
				player.getActionSender().removeChatboxInterface();
				break;
			case 196:
				player.getActionSender().sendDialogue("Pick an option", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "Teleport me to level 14 wilderness", "Stay here");
				player.getInterfaceState().setNextDialogueId(0, 197);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 197:
				Magic.teleport(player, Position.create(2979, 3613, 0), false);
				player.getActionSender().removeChatboxInterface();
				break;
			case 198:
				player.getActionSender().sendDialogue("Pick an option", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "I accept the yell rules.", "I do not accept the yell rules.");
				player.getInterfaceState().setNextDialogueId(0, 199);
				player.getInterfaceState().setNextDialogueId(1, -1);
				break;
			case 199:
				player.getActionSender().removeChatboxInterface();
				player.getPermExtraData().put("yellAccepted", true);
				player.sendMessage("You can now use the yell chat. Remember that you can turn it off");
				player.sendMessage("at any time in the blue questtab.");
				break;
			case 400:
				player.getActionSender().sendDialogue("TactilityPk", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Dear Player, please read carefully...", "InstantPk and TactilityPk have merged the servers!");
				player.getInterfaceState().setNextDialogueId(0, 401);
				break;
			case 401:

				System.out.println("Running code 401");
				player.getActionSender().sendDialogue("TactilityPk", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Your account has been saved but...", "You have to change your name to activate it.", "Please enter a new name in the input box.");
				player.getInterfaceState().setNextDialogueId(0, 402);
				break;

			case 403:
				player.getActionSender().sendDialogue("TactilityPk", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Unfortunately this name is already taken,", "please choose an other name");
				player.getInterfaceState().setNextDialogueId(0, 404);
				break;
			case 405:
				player.getActionSender().sendDialogue("TactilityPk", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Please remember this username very well,", "@red@next time you have to log in with this new name!", "Please confirm this name.");
				player.getInterfaceState().setNextDialogueId(0, 404);
				break;
			case 406:
				player.getActionSender().sendDialogue("TactilityPk", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"You have entered different usernames.", "Please re-enter your name.");
				player.getExtraData().remove("rename");
				player.getInterfaceState().setNextDialogueId(0, 404);
				break;

			case 500:
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Dear Player, please read this very carefully...", "You have 2 accounts, one on both TactilityPk and InstantPk.","One of them can be kept, one must be renamed.");
				player.getInterfaceState().setNextDialogueId(0, 501);
				break;
			case 501:
				player.getActionSender().sendDialogue("Choose an option", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "Rename TactilityPk account", "Rename InstantPk account");
				player.getInterfaceState().setNextDialogueId(0, 502);
				player.getInterfaceState().setNextDialogueId(1, 507);
				break;
			case 502:
				//Means player wanted to rename Artero acc
				player.getActionSender().sendDialogue("Confirm", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "Rename InstantPk account", "Rename TactilityPk account");
				player.getInterfaceState().setNextDialogueId(0, 506);
				player.getInterfaceState().setNextDialogueId(1, 504);
				break;

			case 504:
				//Confirmed he wanted to rename Artero acc
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Please enter a new name for your","TactilityPk account.");
				player.getInterfaceState().setNextDialogueId(0, 505);
				break;
			case 506:
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"You have made an invalid choice, please try again.");
				player.getInterfaceState().setNextDialogueId(0, 501);

				break;
			case 507:
				//Player wanted to rename InstantPk
				player.getActionSender().sendDialogue("Confirm", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "Rename InstantPk account", "Rename TactilityPk account");
				player.getInterfaceState().setNextDialogueId(0, 508);
				player.getInterfaceState().setNextDialogueId(1, 510);
				break;
			case 508:
				//Confirmed he wanted to rename InstantPk acc
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Please enter a new name for your","InstantPk account.");
				player.getInterfaceState().setNextDialogueId(0, 509);
				break;
			case 510:
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"You have made an invalid choice, please try again.");
				player.getInterfaceState().setNextDialogueId(0, 501);
				break;
			case 511:
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"Thank you for changing your name!","You will be logged out now.");
				player.getInterfaceState().setNextDialogueId(0, 512);
				break;
			case 512:
				World.getLogoutQueue().add(player);
				break;
			case 513:
				//InstantPk
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"This name is already in use, please choose an other name.");
				player.getInterfaceState().setNextDialogueId(0, 509);
				break;
			case 514:
				//Artero
				player.getActionSender().sendDialogue("Server", DialogueType.NPC, 2611, FacialAnimation.DEFAULT,
						"This name is already in use, please choose an other name.");
				player.getInterfaceState().setNextDialogueId(0, 505);
				break;
			case 530:
				player.getActionSender().sendDialogue("Click an option", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "Check time for next honor points", "Yell out honor points");
				player.getInterfaceState().setNextDialogueId(0, 531);
				player.getInterfaceState().setNextDialogueId(1, 532);
				break;
			case 531:
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis((player.getLastHonorPointsReward()  + (Time.ONE_HOUR * 12)) - System.currentTimeMillis());
				if(System.currentTimeMillis() - player.getLastHonorPointsReward() < 0) {
					player.sendMessage("You will receive honor points again if you relog.");
				} else {
					StringBuilder output = new StringBuilder();
					if (cal.get(Calendar.HOUR) > 0) {
						output.append(cal.get(Calendar.HOUR));
						if (cal.get(Calendar.HOUR) == 1) {
							output.append(" hour");
						} else {
							output.append(" hours");
						}
						if (cal.get(Calendar.MINUTE) > 0) {
							output.append(" and ");
						}
					}
					if (cal.get(Calendar.MINUTE) > 0) {
						output.append(cal.get(Calendar.MINUTE));
						if (cal.get(Calendar.MINUTE) == 1) {
							output.append(" minute");
						} else {
							output.append(" minutes");
						}
					}
					player.sendMessage("You will receive honor points again in " + output.toString() + ".");
				}
				player.getActionSender().removeChatboxInterface();
				break;
			case 532:
				player.forceMessage("I have " + (player.getPoints().getHonorPoints() == 0 ? "no" : player.getPoints().getHonorPoints()) + " " + (player.getPoints().getHonorPoints() == 1 ? "honor point" : "honor points") + ".");
				player.getActionSender().removeChatboxInterface();
				break;
			case 540:
				player.getActionSender().sendDialogue("Click an option", DialogueType.OPTION, 1, FacialAnimation.DEFAULT, "Yell out voting streak", "Yell out voting points");
				player.getInterfaceState().setNextDialogueId(0, 541);
				player.getInterfaceState().setNextDialogueId(1, 542);
				break;
			case 541:
				int currentStreak = player.getPermExtraData().getInt("votingStreak");
				player.forceMessage("I " + (currentStreak == 0 ? "have no streak going right now" : "am on a " + currentStreak + " " + (currentStreak == 1 ? "day" : "days") + " voting streak") + ".");
				player.getActionSender().removeChatboxInterface();
				break;
			case 542:
				player.forceMessage("I have " + (player.getPoints().getVotingPoints() == 0 ? "no" : player.getPoints().getVotingPoints()) + " " + (player.getPoints().getVotingPoints() == 1 ? "voting point" : "voting points") + ".");
				player.getActionSender().removeChatboxInterface();
				break;
			case 600:
				player.getInterfaceState().setNextDialogueId(0, -1);
				player.getInterfaceState().setStringListener("ge_set_quantity", "Enter the quantity");
				break;
			case 601:
				player.getInterfaceState().setNextDialogueId(0, -1);
				player.getInterfaceState().setStringListener("ge_set_price", "Enter the price");
				break;
			case 610:
			case 611:
			case 612:
			case 613:
				player.getRandomEvent().answer(dialogueId - 610);
				break;
			case 650:
				PlayerAuthenticationGenerator.setupAuthenticator(player);
				break;
			case 651:
				//TODO MAKE INFO PAGE
				break;
			case 652:
				PlayerAuthenticationGenerator.disableAuthenticator(player);
				break;
			case 654:
				player.getInterfaceState().setNextDialogueId(0, -1);
				player.getInterfaceState().setStringListener("authenticator_confirmation", "Enter your current key");
				break;
			case 655:
				player.getInterfaceState().setNextDialogueId(0, -1);
				player.getInterfaceState().setStringListener("authenticator_removal_confirmation", "Enter your current key");
				break;
			case 6000:
				player.getActionSender().removeChatboxInterface();
				break;
			default:
				player.getActionSender().removeChatboxInterface();
				break;
		}
	}

	public static void advanceDialogue(Player player, int index) {
		int dialogueId = player.getInterfaceState().getNextDialogueId(index);
		if(dialogueId == - 1) {
			player.getActionSender().removeChatboxInterface();
			return;
		}
		openDialogue(player, dialogueId);
	}

}
