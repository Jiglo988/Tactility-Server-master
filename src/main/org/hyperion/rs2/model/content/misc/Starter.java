package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.SpellBook;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.util.StarterLogging;
import org.hyperion.util.Misc;

public class Starter {

	public static void giveMain(Player player) {
		for(int i = 0; i <= 6; i++) {
				player.getSkills().setExperience(i, 13100000);
				player.getSkills().setLevel(i, 99);
		}
		ContentEntity.addItem(player, 4151, 1); // Weapon
		//ContentEntity.addItem(player, 4588, 3); // Weapon Noted
		ContentEntity.addItem(player, 1215, 1); // DDS
		ContentEntity.addItem(player, 1127, 1); // Weapon Noted
		ContentEntity.addItem(player, 1079, 1); // Weapon Noted
		ContentEntity.addItem(player, 10828, 1); // Helm
		ContentEntity.addItem(player, 392, 200); // Food
		ContentEntity.addItem(player, 7460, 1); // Bracelet
		ContentEntity.addItem(player, 3105, 1);//Climbing boots
		//ContentEntity.addItem(player, 3106, 3);
		ContentEntity.addItem(player, 158, 25);// Potions
		ContentEntity.addItem(player, 146, 25);
		ContentEntity.addItem(player, 164, 25);
		ContentEntity.addItem(player, 140, 25);
		if(Math.random() > 0.5) // Defender
			ContentEntity.addItem(player, 8850, 1);
		else
			ContentEntity.addItem(player, 8849, 1);
		ContentEntity.addItem(player, 1019 + Misc.random(6) * 2);// Cape
	}

	public static void giveHybridPure(Player player) {
		player.getSkills().setExperience(0, 275000);// 60 Attk
		player.getSkills().setLevel(0, 60);
		for(int i = 2; i <= 4; i++) {
				player.getSkills().setExperience(i, 13100000);
				player.getSkills().setLevel(i, 99);
		}
		player.getSkills().setExperience(5, 15000);
		player.getSkills().setLevel(5, 31);
			player.getSkills().setExperience(6, 13100000);
			player.getSkills().setLevel(6, 99);
		ContentEntity.addItem(player, 4587, 1); // Weapon
		ContentEntity.addItem(player, 1215, 1);// Dds
		ContentEntity.addItem(player, 4675, 1); // Ancient staff
		// ContentEntity.addItem(player, 861, 1); //Shortbow
		ContentEntity.addItem(player, 861, 1); // Shortbow noted
		ContentEntity.addItem(player, 892, 1000);// Arrows
		ContentEntity.addItem(player, 392, 200); // Food
		ContentEntity.addItem(player, 7459 - Misc.random(1), 1); // Gloves
		// ContentEntity.addItem(player, 11119, 3);
		ContentEntity.addItem(player, 2497, 1);//Dhide chaps
		//ContentEntity.addItem(player, 2498, 3);
		ContentEntity.addItem(player, 3105, 1);//Climbing boots
		//ContentEntity.addItem(player, 3106, 3);
		ContentEntity.addItem(player, 656 + Misc.random(4) * 2, 1);//Cream Hat
		//ContentEntity.addItem(player, 663, 3);
		ContentEntity.addItem(player, 577, 1);//Wizard Robe
		//ContentEntity.addItem(player, 578, 3);
		ContentEntity.addItem(player, 1033, 1);//Zamorak Robe
		//ContentEntity.addItem(player, 1034, 3);
		ContentEntity.addItem(player, 158, 25);// Potions
		ContentEntity.addItem(player, 146, 25);
		ContentEntity.addItem(player, 170, 25);
		ContentEntity.addItem(player, 8844 + Misc.random(1), 1);
		ContentEntity.addItem(player, 10499, 1);// Accumulator
		player.getSpellBook().changeSpellBook(SpellBook.ANCIENT_SPELLBOOK);
		player.getActionSender().sendSidebarInterface(6, 12855);
		player.getActionSender().sendMessage(
				"Your spellbook has been changed to ancients!");
	}

	public static void giveRangedPure(Player player) {
		//player.getBank().add(new Item(11337, 5));
		// ContentEntity.addSkillXP(player, 275000, 0); //60 Attk
		// ContentEntity.addSkillXP(player, 1210500, 2); //70 Str
			player.getSkills().setExperience(3, 13100000);// 80 Hp
			player.getSkills().setLevel(3, 99);
			player.getSkills().setExperience(4, 13100000);// 80 Range
			player.getSkills().setLevel(4, 99);
			player.getSkills().setExperience(6, 13100000); // 75 Mage
			player.getSkills().setLevel(6, 99);
		ContentEntity.addItem(player, 861, 1); // Shortbow
		ContentEntity.addItem(player, 892, 1000);// Arrows
		ContentEntity.addItem(player, 9185, 1);// Cbows
		ContentEntity.addItem(player, 9243 + Misc.random(2), 100);// Arrows
		ContentEntity.addItem(player, 868, 100);// Arrows
		ContentEntity.addItem(player, 392, 200); // Food
		// ContentEntity.addItem(player, 11118, 1); //Bracelet
		ContentEntity.addItem(player, 2497, 1);
		// ContentEntity.addItem(player, 2497, 1);//Dhide chaps
		ContentEntity.addItem(player, 7459, 1);
		ContentEntity.addItem(player, 1129, 1);
		ContentEntity.addItem(player, 3105, 1);//Climbing boots
		//ContentEntity.addItem(player, 3106, 3);
		ContentEntity.addItem(player, 3027, 25); // Prayer Pot
		ContentEntity.addItem(player, 170, 25);// Range
		ContentEntity.addItem(player, 10499, 1);// Accumulator
		ContentEntity.addItem(player, 3840 + Misc.random(2) * 2, 1); //God book
	}

	public static void giveBerserker(Player player) {
		player.getSkills().setExperience(0, 275000);
		player.getSkills().setLevel(0, 60);
		player.getSkills().setExperience(1, 62000);
		player.getSkills().setLevel(1, 45);
		for(int i = 2; i <= 4; i++) {
				player.getSkills().setExperience(i, 13100000);
				player.getSkills().setLevel(i, 99);
		}
			player.getSkills().setExperience(6, 13100000);
			player.getSkills().setLevel(6, 99);
		player.getSkills().setExperience(5, 15000);
		player.getSkills().setLevel(5, 31);
		ContentEntity.addItem(player, 4587, 1); // Weapon
		//ContentEntity.addItem(player, 4588, 3); // Weapon Noted
		ContentEntity.addItem(player, 1215, 1); // DDS
		ContentEntity.addItem(player, 1127, 1); // Weapon Noted
		ContentEntity.addItem(player, 1079, 1); // Weapon Noted
		ContentEntity.addItem(player, 3751, 1); // Weapon Noted
		ContentEntity.addItem(player, 392, 200); // Food
		ContentEntity.addItem(player, 7460, 1); // Bracelet
		ContentEntity.addItem(player, 3105, 1);//Climbing boots
		//ContentEntity.addItem(player, 3106, 3);
		ContentEntity.addItem(player, 158, 25);// Potions
		ContentEntity.addItem(player, 146, 25);
		ContentEntity.addItem(player, 164, 25);
		ContentEntity.addItem(player, 140, 25);
		if(Math.random() > 0.5) // Defender
			ContentEntity.addItem(player, 8850, 1);
		else
			ContentEntity.addItem(player, 8849, 1);
		ContentEntity.addItem(player, 1019 + Misc.random(6) * 2);// Cape
	}

	public static void giveSkiller(Player player) {
		for(int i = 7; i < 23; i++) {
			player.getSkills().setExperience(i, 13500);
			player.getSkills().setLevel(i, 30);
		}
		ContentEntity.addItem(player, 1323, 1); // Weapon
		ContentEntity.addItem(player, 841, 1); // Shortbow
		ContentEntity.addItem(player, 884, 1500); // Arrows
		ContentEntity.addItem(player, 380, 120); // Food
		ContentEntity.addItem(player, 11118, 1); // Bracelet
		if(Math.random() > 0.5) {
			ContentEntity.addItem(player, 1153, 1);// Helm
			ContentEntity.addItem(player, 1101, 1); // Top
			ContentEntity.addItem(player, 1067, 1); // Bottom
		} else {
			ContentEntity.addItem(player, 579, 1);// Helm
			ContentEntity.addItem(player, 577, 1); // Bottom
			ContentEntity.addItem(player, 1011, 1); // Top
		}
		ContentEntity.addItem(player, 1019 + Misc.random(6) * 2);
		ContentEntity.addItem(player, 6568, 1); // Cape
		if(Math.random() > 0.5) // Boots
			ContentEntity.addItem(player, 9005, 1);
		else
			ContentEntity.addItem(player, 7114, 1);
		if(Math.random() > 0.5) // Defender
			ContentEntity.addItem(player, 8844, 1);
		else
			ContentEntity.addItem(player, 8845, 1);
		ContentEntity.addItem(player, 590, 1);
		ContentEntity.addItem(player, 1511, 1);
		ContentEntity.addItem(player, 1511, 1);
		ContentEntity.addItem(player, 1511, 1);
	}

	public static void giveStarter(Player player) {
		String ip = player.getFullIP().split(":")[0].replace("/", "");
		long lastStarter = StarterLogging.getLogging().lastStarterReceived(ip);
		if(System.currentTimeMillis() - lastStarter > StarterLogging.STARTER_MAX_DELAY) {
			ContentEntity.addItem(player, 995, 25000000);
			ContentEntity.addItem(player, 6585); // Amulet
			ContentEntity.addItem(player, 555, 1000);
			ContentEntity.addItem(player, 560, 1000);//deaths
			ContentEntity.addItem(player, 565, 1000);//bloods-chaos
			ContentEntity.addItem(player, 557, 1000);
			ContentEntity.addItem(player, 9075, 1000);
			DialogueManager.openDialogue(player, 109);// temp disable
		} else if(System.currentTimeMillis() - lastStarter > StarterLogging.STARTER_MIN_DELAY) {
			ContentEntity.addItem(player, 6585); // Amulet
			ContentEntity.addItem(player, 555, 1000);
			ContentEntity.addItem(player, 560, 1000);//deaths
			ContentEntity.addItem(player, 565, 1000);//bloods-chaos
			ContentEntity.addItem(player, 557, 1000);
			ContentEntity.addItem(player, 9075, 1000);
			DialogueManager.openDialogue(player, 109);// temp disable
			StarterLogging.getLogging().save(ip, System.currentTimeMillis());
		} else {
			player.getInventory().add(new Item(995, 100000));
			DialogueManager.openDialogue(player, 109);// temp disable
			player.getActionSender().sendMessage("You have already received a starter today!");
		}

	}


}
