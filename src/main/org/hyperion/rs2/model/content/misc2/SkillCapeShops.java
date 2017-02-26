package org.hyperion.rs2.model.content.misc2;

import java.io.FileNotFoundException;
import org.hyperion.rs2.model.Animation.FacialAnimation;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.net.ActionSender.DialogueType;

/**
 * @author SaosinHax/Linus/Vegas/Flux/Tinderbox/Jack Daniels/Arsen/Jolt <- All
 *         same person
 */

public class SkillCapeShops implements ContentTemplate {

	private static int[] SkillCapeId = {9747, 9753, 9750, 9768, 9756, 9759,
			9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771,
			9777, 9786, 9810, 9765};
	private static String[] SkillCapeName = {"Attack", "Defence", "Strength",
			"Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting",
			"Fletching", "Fishing", "Firemaking", "Crafting", "Smithing",
			"Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming",
			"RuneCrafting"};

	public static void openSkillCapeShop(Player player, int id) {

		NPC npc = (NPC) player.getInteractingEntity();
        if(npc == null)
            return;
		int skillid = 4;
		for(int i = 0; i < npcId.length; i++) {
			if(npcId[i] == npc.getDefinition().getId())
				skillid = i;
		}
		if(id == 3000) {
			player.getActionSender().sendDialogue(
					npc.getDefinition().getName(), DialogueType.NPC,
					npc.getDefinition().getId(), FacialAnimation.DEFAULT,
					"Hello Adventurer, How can I help you?");
			player.getInterfaceState().setNextDialogueId(0, 3002);
			return;
		} else if(id == 3001) {
			player.getActionSender().sendDialogue(
					npc.getDefinition().getName(), DialogueType.NPC,
					npc.getDefinition().getId(), FacialAnimation.DEFAULT,
					"Hi, How may I help you?");
			player.getInterfaceState().setNextDialogueId(0, 3002);
			return;
		} else if(id == 3002) {
			// TODO SECOND DIALOGUE
			//System.out.println("SkillId " + skillid);
			player.getActionSender().sendDialogue(
					npc.getDefinition().getName(),
					DialogueType.NPC,
					npc.getDefinition().getId(),
					FacialAnimation.DEFAULT,
					"Would you like to buy , a " + SkillCapeName[skillid]
							+ " Skillcape", " for 99000 gp?");
			player.getInterfaceState().setNextDialogueId(0, 3003);
			return;
		} else if(id == 3003) {
			// THIRD DIALOGUE
	        /* if(id % 3 == 0){ */
			player.getActionSender().sendDialogue("Select an Option",
					DialogueType.OPTION, - 1, FacialAnimation.DEFAULT,
					"Yes sure!.", "99000 gp?? YOU'RE CRAZY!");
			player.getInterfaceState().setNextDialogueId(0, 3004);
			// player.getInterfaceState().setNextDialogueId(1, 3005);
			return;
            /*
			 * } else {
			 * player.getActionSender().sendDialogue("Select an Option",
			 * DialogueType.OPTION, -1, FacialAnimation.DEFAULT, "Hmm, okay!.",
			 * "Nevermind"); }
			 */
		}
		player.getActionSender().removeAllInterfaces();

		/**
		 * Part where you buy the skill cape
		 */
		if(ContentEntity.getLevelForXP(player, skillid) != 99) {
			player.getActionSender().sendMessage(
					"Your " + SkillCapeName[skillid]
							+ " level must be 99 to buy this item.");
			return;
		}
		if(ContentEntity.freeSlots(player) == 0) {
			player.getActionSender().sendMessage(
					"You don't have enough free slots to buy this item.");
			return;
		}

		if(! ContentEntity.deleteItemA(player, 995, 99000)) {
			player.getActionSender().sendMessage(
					"You don't have enough money to buy this item.");
			return;
		}
		if(ContentEntity.count99Levels(player) > 1) {
			ContentEntity.addItem(player, SkillCapeId[skillid] + 1, 1);
		} else {
			ContentEntity.addItem(player, SkillCapeId[skillid], 1);
		}
	}

	@Override
	public boolean clickObject(Player player, int type, int npcId, int slot,
	                           int c, int d) {
		if(type != 20)
			openSkillCapeShop(player, 3000 + Combat.random(1));
		else
			openSkillCapeShop(player, npcId);
		return true;
	}

	@Override
	public void init() throws FileNotFoundException {
		// when the class is started, this method is called k so you inalitse
		// variables here or w.e
		// yup thats it lol initialize

	}

	private static final int[] npcId = {4288, 7950/* Melee Tutor */, 4297,
			961/* Invisible npc */, - 1/* Tobereplaced */, 802,
			1658/* Tobereplaced */, 847,/* Woodcuttingtutorunknownid */- 1, 575,
			308, 4946, 805, 604, 3295, 455, 437, 2270, 8275/* ToBeReplaced */,
			3299, 553};

	@Override
	public int[] getValues(int type) {
		if(type == 10 /* || type == 11 || type == 12 */) {
			return npcId;
		}
		if(type == 20) {
			int[] dialogueIds = {3000, 3001, 3002, 3003, 3004,};
			return dialogueIds;
		}
		return null;
	}

}