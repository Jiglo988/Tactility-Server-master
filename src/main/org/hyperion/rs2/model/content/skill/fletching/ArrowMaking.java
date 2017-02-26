package org.hyperion.rs2.model.content.skill.fletching;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.util.Misc;

/**
 * Created by Gilles on 9/09/2015.
 */
public class ArrowMaking extends Fletching {

    public enum Arrow {
        BRONZE_ARROW(882, 39, 1, 40),
        IRON_ARROW(884, 40, 15, 57),
        STEEL_ARROW(886, 41, 30, 95),
        MITHRIL_ARROW(888, 42, 45, 133),
        ADAMANT_ARROW(890, 43, 60, 168),
        RUNE_ARROW(892, 44, 75, 207);

        public int  arrowId,
                    arrowHeadId,
                    levelReq,
                    exp;

        public int getArrowId() {
            return arrowId;
        }

        public int getArrowHeadId() {
            return arrowHeadId;
        }

        public int getLevelReq() {
            return levelReq;
        }

        public int getExp() {
            return exp;
        }

        public String getName() {
            return Misc.ucFirst(this.toString().replaceAll("_", " ").toLowerCase());
        }

        Arrow(int arrowId, int arrowHeadId, int levelReq, int exp) {
            this.arrowId = arrowId;
            this.arrowHeadId = arrowHeadId;
            this.levelReq = levelReq;
            this.exp = exp;
        }
    }

    public static Arrow getArrow(int id) {
        for(Arrow arrow : Arrow.values()) {
            if(arrow.getArrowHeadId() == id)
                return arrow;
        }
        return null;
    }

    public static boolean createArrows(Player client, int item) {

        if(client.isBusy()) {
            return false;
        }
        if(client.getRandomEvent().skillAction())
            return false;
        Arrow arrow = getArrow(item);
        if(arrow == null)
            return false;

        int amount = ContentEntity.getItemAmount(client, item);

        if(ContentEntity.freeSlots(client) < 1) {
            client.sendMessage("You have no space in your inventory");
            return false;
        }
        if(ContentEntity.returnSkillLevel(client, Skills.FLETCHING) < arrow.getLevelReq()) {
            ContentEntity.sendMessage(client, "You need a fletching level of " + arrow.getLevelReq() + " to make these arrows.");
            return false;
        }
        int am2 = ContentEntity.getItemAmount(client, 53);
        if(am2 < amount)
            amount = am2;
        ContentEntity.deleteItemA(client, 53, amount > 15 ? 15 : amount);
        ContentEntity.deleteItemA(client, item, amount > 15 ? 15 : amount);
        client.getAchievementTracker().itemSkilled(Skills.FLETCHING, arrow.getArrowId(), amount > 15 ? 15 : amount);
        ContentEntity.addItem(client, arrow.getArrowId(), amount > 15 ? 15 : amount);
        ContentEntity.addSkillXP(client,arrow.getExp() * EXPMULTIPLIER, Skills.FLETCHING);
        ContentEntity.sendMessage(client, "You make "  + (amount == 1 ? Misc.aOrAn(arrow.getName().toLowerCase()) : "some") + " " + arrow.getName().toLowerCase() + (amount > 1 ? "s" : "") + ".");
        return true;
    }
}
