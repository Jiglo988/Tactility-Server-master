package org.hyperion.rs2.model.content.skill.fletching;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.content.ContentEntity;

/**
 * Created by Gilles on 9/09/2015.
 */
public class HeadlessArrows extends Fletching {

    public static boolean createHeadlessArrows(Player client, int item) {
        if(client.isBusy()) {
            return true;
        }
        if(client.getRandomEvent().skillAction())
            return false;

        int amount = ContentEntity.getItemAmount(client, item);

        if(ContentEntity.freeSlots(client) >= 1) {
            int am2 = ContentEntity.getItemAmount(client, 314);
            if(am2 < amount)
                amount = am2;
            client.getAchievementTracker().itemSkilled(Skills.FLETCHING, 53, amount > 15 ? 15 : amount);
            ContentEntity.deleteItemA(client, 314, amount > 15 ? 15 : amount);
            ContentEntity.deleteItemA(client, item, amount > 15 ? 15 : amount);
            ContentEntity.addItem(client, 53, amount > 15 ? 15 : amount);
            ContentEntity.addSkillXP(client, 15 * EXPMULTIPLIER, 9);
            ContentEntity.sendMessage(client, "You make some headless arrows.");
        } else {
            ContentEntity.sendMessage(client,
                    "You have no space in your inventory");
        }
        return true;
    }
}
