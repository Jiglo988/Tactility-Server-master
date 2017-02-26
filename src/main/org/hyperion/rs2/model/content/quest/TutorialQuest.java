package org.hyperion.rs2.model.content.quest;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

public class TutorialQuest implements ContentTemplate {


	public void talkToGuide(Player player, int npcSlot) {
		player.setInteractingEntity(World.getNpcs().get(npcSlot));
		if(player.tutIsland == 1) {
			DialogueManager.openDialogue(player, 37);
		} else if(player.tutIsland == 2) {
			DialogueManager.openDialogue(player, 44);
		} else if(player.tutIsland == 3) {
			DialogueManager.openDialogue(player, 45);
		} else if(player.tutIsland == 4) {
			DialogueManager.openDialogue(player, 50);
		} else if(player.tutIsland == 5) {
			DialogueManager.openDialogue(player, 52);
		} else if(player.tutIsland == 6) {
			DialogueManager.openDialogue(player, 53);
		} else if(player.tutIsland == 7) {
			DialogueManager.openDialogue(player, 57);
		}
	}

	public static void walkDialogue(Player player) {
		if(player.tutIsland == 1 || player.tutIsland == 3 || player.tutIsland == 4 || player.tutIsland == 6) {
			DialogueManager.openDialogue(player, 60);
		} else if(player.tutIsland == 2) {
			DialogueManager.openDialogue(player, 61);
		} else if(player.tutIsland == 5) {
			DialogueManager.openDialogue(player, 62);
		} else if(player.tutIsland == 7) {
			DialogueManager.openDialogue(player, 63);
		}
	}

	@Override
	public boolean clickObject(final Player player, int type, int a, int b, int c,
	                           int d) {
		// TODO Auto-generated method stub
		if(type == 10) {
	        /*if(player.tutIsland == 10)
                return false;
			else*/
			talkToGuide(player, d);
			return true;
		} else if(type == 16) {
			if(++ player.tutSubIsland >= 3) {

				GlobalItem globalItem = new GlobalItem(player, b, c, 0, new Item(2138, 1));
				GlobalItemManager.newDropItem(player, globalItem);
			}
		} else if(type == 6) {
			if(a == 1738) {
				//player.setTeleportTarget(Location.create(2897, 3513, 1));
				GlobalItem globalItem = new GlobalItem(player, 2897, 3507, 1, new Item(1856, 1));
				GlobalItemManager.addToItems(globalItem);
			} else if(a == 1740) {
				//player.setTeleportTarget(Location.create(2897, 3513, 0));
			}
		} else if(type == 1) {
            player.getActionSender().openQuestInterface("DeviousPK Guidebook", new String[]{
                    "Welcome newcomer to TactilityPk,", "created by Jesse. ", "Please be updated with all the latest news on the website", " https://tactilitypk.boards.net",
                    "Your bank has been loaded with a starter pack!",
                    "",
                    "-Master combat stats ::master",
                    "-Spawn Instant Sets in achievement (green) tab",
                    "-Make cash by pking (::edge), voting (::vote) and donating (::donate) (donate RSGP to Seven)",
                    "-Ask \"Nab\" for price checks and information",
                    "",
                    "-Skill through skill teleports in magic spells",
                    "-Tormented Demons can be found above the KBD",
                    "-Ice strykewyrmes can be found next to the wilderness agility course",
                    "-Look at ::commands for more info!",
                    "-Still need help? PM a moderator (::onlinestaff) or push a help ticket(::reqhelp reason)",
            });
		}
		/* else if(type == 14){
			ContentEntity.deleteItemA(player, 2138,1);
			ContentEntity.startAnimation(player, 897);
			if(++player.tutSubIsland >= 2){
				ContentEntity.sendMessage(player, "You cook the chicken.");
				ContentEntity.addItem(player, 2140, 1);
			} else {
				ContentEntity.sendMessage(player, "You burn the chicken.");
				ContentEntity.addItem(player, 2144, 1);
			}
		}*/
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
		// TODO Auto-generated method stub
		/*Rs2-server Guide book
		Welcome newcomer to Rs2-server, created by Scar and Martin. Please be updated with all the latest news on the website www.rs2-server.org.
		Rs2-server is a realistic Runescape Private server based off hyperion, things are not easy here unlike any other server. We offer a realistic game play which gives players a good feel of a close replica to the runescape we all love and missed.
		There are currently 12 non-combat skills to train, if you are a free player these skills will be limited as well as certain items. To get full access to the game, skills and items visit the site to see how to become a �member�.
		Once you have completed the Tutorial quest, you will be teleported to the main land, things get harder from here. You would be given basic survival gear to start off with and slowly you may build upon this and maybe become a great adventurer.
		To teleport to various monsters to train you will need to use your �Glory Amulet� which is given once you have completed the tutorial quest. To operate your Glory you need to rub it where it will open certain training locations, but beware! Don�t attempt to fight monsters which are too strong as you may die. Always keep food with you to avoid death. To start training you need to understand the basics of combat, the best option would be to start training by visiting �Barbarian Village�  as the monsters are much easier to kill.
		Also, a �Ring of Dueling� is given to you, as you would do with the glory you may also rub and operate your ring which will take you to various fun minigames to play.
		Basic shops are located around the game, they have limited items. Most items are obtained from drops or other players.
		You may also view the global highscores and compete with friends and the world, the scores are located on the website: www.highscores.rs2-server.org .
		Best of luck with your journey, if this guide was not good enough please post a message on the forums if you require further assistance.
	*/
	}

	@Override
	public int[] getValues(int type) {
		if(type == 10) {
			int[] j = {945,};
			return j;
		}
		if(type == 16) {
			int[] j = {41,};//chicken
			return j;
		}
		/*if(type == 14){
			int[] j = {1856,};//chicken
			return j;
		}*/
		if(type == 6) {
			int[] j = {1738, 1740};//stairs
			return j;
		}
		if(type == 1) {
			int[] j = {1856};//book
			return j;
		}
		// TODO Auto-generated method stub
		return null;
	}

}
