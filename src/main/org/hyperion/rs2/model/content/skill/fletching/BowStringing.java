package org.hyperion.rs2.model.content.skill.fletching;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.util.Misc;

/**
 * Created by Gilles on 9/09/2015.
 */
public class BowStringing extends LogCutting {

    public enum StringItem {
        BOW_STRING(1777,
                StrungItems.NORMAL_SHORTBOW,
                StrungItems.NORMAL_LONGBOW,
                StrungItems.OAK_SHORTBOW,
                StrungItems.OAK_LONGBOW,
                StrungItems.WILLOW_SHORTBOW,
                StrungItems.WILLOW_LONGBOW,
                StrungItems.MAPLE_SHORTBOW,
                StrungItems.MAPLE_LONGBOW,
                StrungItems.YEW_SHORTBOW,
                StrungItems.YEW_LONGBOW,
                StrungItems.MAGIC_SHORTBOW,
                StrungItems.MAGIC_LONGBOW);

        int itemId;
        StrungItems[] items;

        public int getItemId() {
            return itemId;
        }

        public StrungItems[] getItems() {
            return items;
        }

        public String getName() {
            return Misc.ucFirst(this.toString().replaceAll("_", " ").toLowerCase());
        }

        StringItem(int itemId, StrungItems... items) {
            this.itemId = itemId;
            this.items = items;
        }
    }

    public enum StrungItems {
        NORMAL_SHORTBOW(cutItems.NORMAL_SHORTBOW, 841, 6678),
        NORMAL_LONGBOW(cutItems.NORMAL_LONGBOW, 839, 6684),
        OAK_SHORTBOW(cutItems.OAK_SHORTBOW, 843, 6679),
        OAK_LONGBOW(cutItems.OAK_LONGBOW, 845, 6685),
        WILLOW_SHORTBOW(cutItems.WILLOW_SHORTBOW, 849, 6680),
        WILLOW_LONGBOW(cutItems.WILLOW_LONGBOW, 847, 6686),
        MAPLE_SHORTBOW(cutItems.MAPLE_SHORTBOW, 853, 6681),
        MAPLE_LONGBOW(cutItems.MAPLE_LONGBOW, 851, 6687),
        YEW_SHORTBOW(cutItems.YEW_SHORTBOW, 857, 6682),
        YEW_LONGBOW(cutItems.YEW_LONGBOW, 855, 6688),
        MAGIC_SHORTBOW(cutItems.MAGIC_SHORTBOW, 861, 6683),
        MAGIC_LONGBOW(cutItems.MAGIC_LONGBOW, 859, 6689);

        cutItems startItem;
        int resultId,
            emote;

        public int getEmote() {
            return emote;
        }

        public int getItemId() {
            return startItem.getItemId();
        }

        public int getResultId() {
            return resultId;
        }

        public int getLevelReq() {
            return startItem.getLevelReq();
        }

        public int getExp() {
            return startItem.getExp();
        }

        int exp;

        StrungItems(cutItems startItem, int resultId, int emote) {
            this.startItem = startItem;
            this.resultId = resultId;
            this.emote = emote;
        }
    }

    public static StrungItems getItem(int i) {
        for(StrungItems item : StrungItems.values()) {
            if(item.getItemId() == i)
                return item;
        }
        return null;
    }

    public static StringItem getString(int i) {
        for(StringItem item : StringItem.values()) {
            if(item.getItemId() == i)
                return item;
        }
        return null;
    }

    public static boolean stringBow(Player client, int stringId, int index) {
        if (client.isBusy()) {
            return false;
        }
        StringItem string = getString(stringId);
        if (string == null)
            return false;
        StrungItems item = string.getItems()[index];
        if (item == null)
            return false;

        if (ContentEntity.returnSkillLevel(client, 9) < item.getLevelReq()) {
            ContentEntity.sendMessage(client, "You need a fletching level of " + item.getLevelReq() + " to string that bow.");
            return false;
        }
        client.setBusy(true);
        ContentEntity.startAnimation(client, item.getEmote());

        World.submit(new Task(5000) {
            @Override
            public void execute() {
                if(client.getRandomEvent().skillAction(2))
                    stop();
                ContentEntity.deleteItemA(client, item.getItemId(), 1);
                ContentEntity.deleteItemA(client, string.getItemId(), 1);
                client.getAchievementTracker().itemSkilled(Skills.FLETCHING, item.getResultId(), 1);
                ContentEntity.addItem(client, item.getResultId(), 1);
                ContentEntity.sendMessage(client, "You attach the " + string.getName().toLowerCase() + " to the bow.");
                ContentEntity.addSkillXP(client, item.getExp(), Skills.FLETCHING);
                stop();
            }

            @Override
            public void stop() {
                client.setBusy(false);
                ContentEntity.startAnimation(client, -1);
                super.stop();
            }
        });
        return true;
    }
}
