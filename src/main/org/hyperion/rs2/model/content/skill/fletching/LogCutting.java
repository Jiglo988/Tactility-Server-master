package org.hyperion.rs2.model.content.skill.fletching;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.skill.ClickSkillingInterface;
import org.hyperion.util.Misc;

/**
 * Created by Gilles on 8/09/2015.
 */
public class LogCutting extends Fletching {

    public static int knife = 946;

    public enum Log {
        NORMAL(1511, cutItems.ARROW_SHAFT, cutItems.NORMAL_SHORTBOW, cutItems.NORMAL_LONGBOW),
        OAK(1521, cutItems.OAK_SHORTBOW, cutItems.OAK_LONGBOW),
        WILLOW(1519, cutItems.WILLOW_SHORTBOW, cutItems.WILLOW_LONGBOW),
        MAPLE(1517, cutItems.MAPLE_SHORTBOW, cutItems.MAPLE_LONGBOW),
        YEW(1515, cutItems.YEW_SHORTBOW, cutItems.YEW_LONGBOW),
        MAGIC(1513, cutItems.MAGIC_SHORTBOW, cutItems.MAGIC_LONGBOW);

        private int logId;
        private cutItems[] items;

        public int getLogId() {
            return logId;
        }

        public cutItems[] getItems() {
            return items;
        }

        public String getName() {
            return Misc.ucFirst((this.toString() + " LOGS").toLowerCase());
        }

        Log(int logId, cutItems... items) {
            this.logId = logId;
            this.items = items;
        }
    }

    public enum cutItems {
        ARROW_SHAFT(52, 1, 1),
        NORMAL_SHORTBOW(50, 5, 10),
        NORMAL_LONGBOW(48, 10, 20),
        OAK_SHORTBOW(54, 20, 33),
        OAK_LONGBOW(56, 25, 50),
        WILLOW_SHORTBOW(60, 35, 66),
        WILLOW_LONGBOW(58, 40, 83),
        MAPLE_SHORTBOW(64, 50, 100),
        MAPLE_LONGBOW(62, 55, 117),
        YEW_SHORTBOW(68, 65, 133),
        YEW_LONGBOW(66, 70, 150),
        MAGIC_SHORTBOW(72, 80, 167),
        MAGIC_LONGBOW(70, 85, 183);

        private int itemId,
                levelReq,
                exp;

        public int getItemId() {
            return itemId;
        }

        public int getLevelReq() {
            return levelReq;
        }

        public int getExp() {
            return exp;
        }

        public String getName() {
            return Misc.ucFirst(this.toString().replaceAll("_", " ").replaceAll("BOW", "BOW (U)").toLowerCase());
        }

        cutItems(int itemId, int levelReq, int exp) {
            this.itemId = itemId;
            this.levelReq = levelReq;
            this.exp = exp;
        }
    }

    public static Log getLog(int i) {
        for(Log log : Log.values()) {
            if(log.getLogId() == i)
                return log;
        }
        return null;
    }

    private static cutItems getCutItem(int i) {
        for(cutItems item : cutItems.values()) {
            if(item.getItemId() == i)
                return item;
        }
        return null;
    }

    public static boolean chooseItem(Player client, int logId) {
        if(client.isBusy()) {
            return true;
        }

        ContentEntity.removeAllWindows(client);
        client.getActionSender().sendPacket164(8880);

        Log log = getLog(logId);

        cutItems[] items = log.getItems();
        if(items.length > 1) {
            ContentEntity.sendString(client, "What would you like to make?", ClickSkillingInterface.frameId[items.length][0]);
            client.getActionSender().sendPacket164(ClickSkillingInterface.frameId[items.length][0]);
            for (int i = 0; i < items.length; i++) {
                ContentEntity.sendString(client, items[i].getName(), ClickSkillingInterface.frameId[items.length][i + 1]);
                ContentEntity.sendInterfaceModel(client, ClickSkillingInterface.frameId[items.length][items.length + i + 1], 250, items[i].getItemId());
            }
        } else {
        }
        client.getExtraData().put("logId", log.getLogId());
        return true;
    }

    public static boolean startFletching(final Player client, int amount, final int clicked) {
        if(client.isBusy()) {
            return true;
        }
        client.getActionSender().removeAllInterfaces();
        client.setBusy(true);
        Log log = getLog(client.getExtraData().getInt("logId"));

        if(log == null)
            return false;

        cutItems item = log.getItems()[clicked];
        if(item == null)
            return false;

        int amount2;

        if (ContentEntity.getItemAmount(client, log.getLogId()) < amount) {
            amount2 = ContentEntity.getItemAmount(client, log.getLogId());
        } else {
            amount2 = amount;
        }
        if (amount2 == 0 || log == null) {
            return false;
        }
        if (ContentEntity.returnSkillLevel(client, 9) < item.getLevelReq()) {
            ContentEntity.sendMessage(client, "You need a fletching level of " + item.getLevelReq() + " to cut this item.");
            return false;
        }
        client.sendMessage("You begin cutting the log...");

        ContentEntity.startAnimation(client, 1248);

        World.submit(new Task(5000) {
            int craftAm = amount2;

            @Override
            public void execute() {
                if (!client.isBusy()) {
                    stop();
                    return;
                }
                if (craftAm <= 0 || log == null || ContentEntity.getItemAmount(client, log.getLogId()) <= 0) {
                    stop();
                    return;
                }
                if(client.getRandomEvent().skillAction(2))
                    stop();

                client.getAchievementTracker().itemSkilled(Skills.FLETCHING, item.getItemId(), item == cutItems.ARROW_SHAFT ? 15 : 1);
                ContentEntity.deleteItemA(client, log.getLogId(), 1);
                ContentEntity.addItem(client, item.getItemId(), item == cutItems.ARROW_SHAFT ? 15 : 1);
                ContentEntity.addSkillXP(client, item.getExp() * Fletching.EXPMULTIPLIER, 9);
                client.sendMessage("You cut the " + log.getName().toLowerCase() + " into " + (item.getName().contains("bow") ? Misc.aOrAn(item.getName()) + " " + item.getName().toLowerCase() : "some " + item.getName().toLowerCase() + "s") + ".");
                if (craftAm > 1) {
                    ContentEntity.startAnimation(client, 1248);
                    client.sendMessage("You begin cutting the log...");
                }
                craftAm--;
            }

            @Override
            public void stop() {
                client.getExtraData().remove("logId");
                client.setBusy(false);
                ContentEntity.startAnimation(client, -1);
                super.stop();
            }
        });
        return true;
    }
}
