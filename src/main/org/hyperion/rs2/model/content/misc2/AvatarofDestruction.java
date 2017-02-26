package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.net.ActionSender;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 1/30/15
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class AvatarofDestruction implements ContentTemplate {


    private static final int NPC_ID = 7571;


    @Override
    public int[] getValues(int type) {
        if(type == ClickType.NPC_OPTION1)
            return new int[]{NPC_ID};  //To change body of implemented methods use File | Settings | File Templates.
        else if(type == ClickType.DIALOGUE_MANAGER)
            return new int[]{1889, 1890, 1891};
        return new int[0];
    }

    @Override
    public boolean npcOptionOne(Player player, int npcId, int npcLocationX, int npcLocationY, int npcSlot) {

        DialogueManager.openDialogue(player, 1889);

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean dialogueAction(Player player, int dialogueId) {

        switch(dialogueId) {
            case 1889:
                player.getActionSender().sendDialogue("Zoo keeper", ActionSender.DialogueType.NPC, NPC_ID, Animation.FacialAnimation.HAPPY,
                        "I've managed to capture the avatar of destruction!", "I'll lead you to him for a small fee of 100 pkp");
                player.getInterfaceState().setNextDialogueId(0, 1890);
                break;
            case 1890:
                player.getActionSender().sendDialogue("Select an option", ActionSender.DialogueType.OPTION, NPC_ID, Animation.FacialAnimation.HAPPY,
                        "Yes, sounds interesting!", "No");
                player.getInterfaceState().setNextDialogueId(0, 1891);
                player.getInterfaceState().setNextDialogueId(1, -1);
                break;
            case 1891:
                if(player.getPoints().getPkPoints() > 100) {
                    player.getPoints().setPkPoints(player.getPoints().getPkPoints() - 100);
                    Magic.teleport(player, Position.create(2661, 9634, 0), false);
                    player.getActionSender().removeChatboxInterface();
                } else {
                    player.getActionSender().sendDialogue("Zoo keeper", ActionSender.DialogueType.NPC, NPC_ID, Animation.FacialAnimation.HAPPY,
                            "I don't let people go on credit!", "Talk to me when you have the points");
                    player.getInterfaceState().setNextDialogueId(0, -1);
                }

                break;
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
