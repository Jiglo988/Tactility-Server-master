package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.net.ActionSender;

public class Tutorial implements ContentTemplate {

    public static void getProgress(Player player) {
        if(player.getTutorialProgress() == 1) {
            DialogueManager.openDialogue(player, 10000);
        } else if(player.getTutorialProgress() >= 27) {
            player.sendMessage("You have already completed the tutorial.");
        } else {
            DialogueManager.openDialogue(player, 2098 + player.getTutorialProgress());
        }
    }

    public static Item[] rewards = {new Item(15273, 100), new Item(6570), new Item(10551)};

    public static void giveReward(Player player) {
        for(Item item : rewards) {
            player.getExpectedValues().addItemtoInventory("Tutorial", item);
            player.getInventory().add(item);
        }
    }

    @Override
    public int[] getValues(int type) {
        if(type == ClickType.DIALOGUE_MANAGER) {
            int[] values = new int[26];
            for (int i = 2100; i < 2126; i++) {
                values[i - 2100] = i;
            }
            return values;
        } else
            return new int[0];
    }

    @Override
    public boolean dialogueAction(Player player, int dialogueId) {
        switch (dialogueId) {
            case 2100:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "Welcome to TactilityPK, " + player.getSafeDisplayName() + "!",
                        "I am the Greg and I will be your guide for today.");
                player.setTutorialProgress(2);
                player.getInterfaceState().setNextDialogueId(0, 2101);
                return true;
            case 2101:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "If you get stuck at any point, just type @blu@::tutorial@bla@.");
                player.setTutorialProgress(3);
                player.getInterfaceState().setNextDialogueId(0, 2102);
                return true;
            case 2102:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "Let's get started by going to the skilling area.");
                player.setTutorialProgress(4);
                player.getInterfaceState().setNextDialogueId(0, 2103);
                return true;
            case 2103:
                Magic.teleport(player, Position.create(3803, 2836, 0), true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This is where you can practice almost every skill.",
                        "We have a wide variaty of skills available,",
                        "including summoning, dungeoneering and hunter.");
                player.setTutorialProgress(5);
                player.getInterfaceState().setNextDialogueId(0, 2104);
                return true;
            case 2104:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "You can get your slayer tasks here from Duradel too.",
                        "He also hosts the PvM store.");
                player.setTutorialProgress(6);
                player.getInterfaceState().setNextDialogueId(0, 2105);
                return true;
            case 2105:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "You can return here at any point by doing @blu@::skilling@bla@.");
                player.setTutorialProgress(7);
                player.getInterfaceState().setNextDialogueId(0, 2106);
                return true;
            case 2106:
                Magic.teleport(player, Edgeville.POSITION, true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This is Edgeville, it is the main hub for the server.",
                        "Edgeville offers most of the shops, and ofcourse Pk'ing.");
                player.setTutorialProgress(8);
                player.getInterfaceState().setNextDialogueId(0, 2107);
                return true;
            case 2107:
                Magic.teleport(player, Position.create(3087, 3512, 0), true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This is the wilderness above Edgeville,",
                        "a decent amount of the Pk activity takes place here.");
                player.setTutorialProgress(9);
                player.getInterfaceState().setNextDialogueId(0, 2108);
                return true;
            case 2108:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "You can return here at any point by doing @blu@::home@bla@.");
                player.setTutorialProgress(10);
                player.getInterfaceState().setNextDialogueId(0, 2109);
                return true;
            case 2109:
                Magic.teleport(player, Position.create(2977, 3611, 24), true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This is @blu@::13s@bla@. This is a Hybrid Pk'ing place.", "We offer multiple specialized Pk'ing places.");
                player.getInterfaceState().setNextDialogueId(0, 2110);
                player.setTutorialProgress(11);
                return true;
            case 2110:
                Magic.teleport(player, Position.create(2259, 4697, 600), true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This for example is @blu@::ospk@bla@.",
                        "A Pk'ing place that doesn't allow custom items.");
                player.setTutorialProgress(12);
                player.getInterfaceState().setNextDialogueId(0, 2111);
                return true;
            case 2111:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This for example is @blu@::ospk@bla@.",
                        "A Pk'ing place that doesn't allow custom items.");
                player.setTutorialProgress(13);
                player.getInterfaceState().setNextDialogueId(0, 2112);
                return true;
            case 2112:
                Magic.teleport(player, Position.create(2795, 3321, 0), true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "We also offer some PvM, let's go check out some bosses.");
                player.setTutorialProgress(14);
                player.getInterfaceState().setNextDialogueId(0, 2113);
                return true;
            case 2113:
                Magic.teleport(player, Position.create(2264, 4689, 4), true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This is the King Black Dragon, it's one of the easier bosses.",
                        "It has a couple of very valuable drops.");
                player.setTutorialProgress(15);
                player.getInterfaceState().setNextDialogueId(0, 2114);
                return true;
            case 2114:
                Magic.teleport(player, 2533, 4652, 4, false);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This is the Corporeal Beast. This is one of the hardest",
                        "bosses. It requires a team to fight, but also",
                        "has very valuable drops.");
                player.setTutorialProgress(16);
                player.getInterfaceState().setNextDialogueId(0, 2115);
                return true;
            case 2115:
                Magic.teleport(player, 2367, 4963, 0, false);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "This is the PvM area for donators.",
                        "Donators will soon also receive their own skilling area.");
                player.setTutorialProgress(17);
                player.getInterfaceState().setNextDialogueId(0, 2116);
                return true;
            case 2116:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "You can get donator by donating at", "least having 2000 donator points.");
                player.setTutorialProgress(18);
                player.getInterfaceState().setNextDialogueId(0, 2117);
                return true;
            case 2117:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "If you have more than 10.000 donator points,",
                        "you'll receive Super donator.");
                player.setTutorialProgress(19);
                player.getInterfaceState().setNextDialogueId(0, 2118);
                return true;
            case 2118:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "Donators have a lot of perks on the server,", "and exclusive shops. More information at", "@blu@::wiki Donator Benefits");
                player.setTutorialProgress(20);
                player.getInterfaceState().setNextDialogueId(0, 2119);
                return true;
            case 2119:
                Magic.teleport(player, Position.create(2795, 3321, 0), true);
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "If you have any more questions, feel free",
                        "to join the clanchat 'help'.",
                        "You can also always use the request help button.");
                player.setTutorialProgress(21);
                player.getInterfaceState().setNextDialogueId(0, 2120);
                return true;
            case 2120:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "Keep in mind that different staff do different things.",
                        "More info on the forums about this.");
                player.setTutorialProgress(22);
                player.getInterfaceState().setNextDialogueId(0, 2121);
                return true;
            case 2121:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "We also have serverwide chat, but since we ",
                        "have a large amount of players we have a delay on this.");
                player.setTutorialProgress(23);
                player.getInterfaceState().setNextDialogueId(0, 2122);
                return true;
            case 2122:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "If you really want to chat with random people,",
                        "just go to the clanchat 'chatting'.");
                player.setTutorialProgress(24);
                player.getInterfaceState().setNextDialogueId(0, 2123);
                return true;
            case 2123:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "We have a wiki with most of the server info on it.",
                         "Just use @blu@::wiki info @bla@for more information.");
                player.setTutorialProgress(25);
                player.getInterfaceState().setNextDialogueId(0, 2124);
                return true;
            case 2124:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "These are the basics of the server,",
                        "I'm dropping you off at Edgeville with your new Firecape.");
                player.setTutorialProgress(26);
                player.getInterfaceState().setNextDialogueId(0, 2125);
                return true;
            case 2125:
                player.getActionSender().sendDialogue("Greg the Guide", ActionSender.DialogueType.NPC, 945, Animation.FacialAnimation.DEFAULT,
                        "Have fun on the server!");
                player.getInterfaceState().setNextDialogueId(0, 10003);
                if(player.getTutorialProgress() == 26) {
                    player.setTutorialProgress(27);
                    giveReward(player);
                }
                return true;
        }
        return false;
    }

}
