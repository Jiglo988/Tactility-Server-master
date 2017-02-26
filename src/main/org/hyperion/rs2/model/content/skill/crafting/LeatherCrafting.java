package org.hyperion.rs2.model.content.skill.crafting;

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
public class LeatherCrafting extends Crafting {

    public static int needle = 1733;

    public enum Leather {
        LEATHER(1741, Leather_Item.LEATHER_BOOTS, Leather_Item.LEATHER_VAMBS, Leather_Item.LEATHER_CHAPS, Leather_Item.LEATHER_BODY),
        HARD_LEATHER(1743, Leather_Item.HARDLEATHER_BODY),
        GREEN_DRAGON_LEATHER(1745, Leather_Item.GREEN_DHIDE_VAMBS, Leather_Item.GREEN_DHIDE_CHAPS, Leather_Item.GREEN_DHIDE_BODY),
        BLUE_DRAGON_LEATHER(2505, Leather_Item.BLUE_DHIDE_VAMBS, Leather_Item.BLUE_DHIDE_CHAPS, Leather_Item.BLUE_DHIDE_BODY),
        RED_DRAGON_LEATHER(2507, Leather_Item.RED_DHIDE_VAMBS, Leather_Item.RED_DHIDE_CHAPS, Leather_Item.RED_DHIDE_BODY),
        BLACK_DRAGON_LEATHER(2509, Leather_Item.BLACK_DHIDE_VAMBS, Leather_Item.BLACK_DHIDE_CHAPS, Leather_Item.BLACK_DHIDE_BODY);

        private int itemId;
        private Leather_Item[] items;

        public Leather_Item[] getItems() {
            return items;
        }

        public int getItemId() {
            return itemId;
        }

        public String getName() {
            return Misc.ucFirst(this.toString().replaceAll("_", " ").toLowerCase());
        }

        Leather(int itemId, Leather_Item... items) {
            this.itemId = itemId;
            this.items = items;
        }


    }

    public enum Leather_Item {
        LEATHER_BOOTS(1061, 1, 1, 16),
        LEATHER_VAMBS(1063, 1, 9, 22),
        LEATHER_CHAPS(1095, 1, 11, 27),
        LEATHER_BODY(1129, 1, 14, 25),
        HARDLEATHER_BODY(1131, 1, 28, 35),
        GREEN_DHIDE_VAMBS(1065, 1, 57, 62),
        GREEN_DHIDE_CHAPS(1099, 2, 60, 124),
        GREEN_DHIDE_BODY(1135, 3, 63, 186),
        BLUE_DHIDE_VAMBS(2487, 1, 66, 70),
        BLUE_DHIDE_CHAPS(2493, 2, 68, 140),
        BLUE_DHIDE_BODY(2499, 3, 71, 210),
        RED_DHIDE_VAMBS(2489, 1, 73, 78),
        RED_DHIDE_CHAPS(2495, 2, 75, 156),
        RED_DHIDE_BODY(2501, 3, 77, 234),
        BLACK_DHIDE_VAMBS(2491, 1, 79, 86),
        BLACK_DHIDE_CHAPS(2497, 2, 82, 172),
        BLACK_DHIDE_BODY(2503, 3, 84, 258);

        private int
                itemId,
                levelReq,
                amountReq,
                exp;

        public int getItemId() {
            return itemId;
        }

        public int getLevelReq() {
            return levelReq;
        }

        public int getAmountReq() {
            return amountReq;
        }

        public int getExp() {
            return exp;
        }

        public String getName() {
            return Misc.ucFirst(this.toString().replaceAll("_", " ").replaceAll("DHIDE", "D'HIDE").toLowerCase());
        }

        Leather_Item(int itemId, int amountReq, int levelReq, int exp) {
            this.itemId = itemId;
            this.amountReq = amountReq;
            this.levelReq = levelReq;
            this.exp = exp;
        }
    }

    private static Leather_Item getLeatherItem(int i) {
        for(Leather_Item item : Leather_Item.values()) {
            if(item.getItemId() == i)
                return item;
        }
        return null;
    }

    private static Leather getLeather(int i) {
        for(Leather l : Leather.values()) {
            if(l.getItemId() == i)
                return l;
        }
        return null;
    }

    public static boolean craftLeather(final Player c, final int item) {
        try {
            if(c.isBusy())
                return true;
            Leather l = getLeather(item);
            if(l == null) {
                return true;
            }
            c.getExtraData().put("crafting", true);
            c.getExtraData().put("craftingFrom", l.getItemId());
            c.setBusy(true);
            Leather_Item[] items = l.getItems();
            if(items.length > 1) {
                ContentEntity.sendString(c, "What would you like to make?", ClickSkillingInterface.frameId[items.length][0]);
                c.getActionSender().sendPacket164(ClickSkillingInterface.frameId[items.length][0]);
                for (int i = 0; i < items.length; i++) {
                    ContentEntity.sendString(c, items[i].getName(), ClickSkillingInterface.frameId[items.length][i + 1]);
                    ContentEntity.sendInterfaceModel(c, ClickSkillingInterface.frameId[items.length][items.length + i + 1], 250, items[i].getItemId());
                }
            } else {
                startAgain(c, 1, 0);
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean startAgain(final Player c, final int amm, final int slot) {
        Leather leather = getLeather(c.getExtraData().getInt("craftingFrom"));

        if(leather == null)
            return false;

        Leather_Item item = leather.getItems()[slot];
        if(item != null)
            c.getExtraData().put("toCraft", item.getItemId());
        c.getActionSender().removeAllInterfaces();
        if(ContentEntity.returnSkillLevel(c, 12) < item.getLevelReq()) {
            ContentEntity.sendMessage(c, "You need a crafting level of " + item.getLevelReq() + " to craft this item.");
            return false;
        }
        if (ContentEntity.getItemAmount(c, leather.getItemId()) < item.getAmountReq()) {
            c.sendMessage("You need at least " + item.getAmountReq() + " pieces of " + leather.getName().toLowerCase() + ".");
            return false;
        }
        if (ContentEntity.getItemAmount(c, 1734) <= 0) {
            c.sendMessage("You don't have any thread.");
            return false;
        }
        if(c.getRandomEvent().skillAction(2))
            return false;
        finishCraft(c, amm);
        return true;
    }

    public static void finishCraft(final Player c, final int amm) {
        if(c == null)
            return;

        Leather_Item item = getLeatherItem(c.getExtraData().getInt("toCraft"));
        Leather leather = getLeather(c.getExtraData().getInt("craftingFrom"));

        int amm2;

        if (ContentEntity.getItemAmount(c, leather.getItemId()) < amm) {
            amm2 = ContentEntity.getItemAmount(c, leather.getItemId());
        } else {
            amm2 = amm;
        }

        if(item == null || leather == null || amm2 <= 0) {
            return;
        }

        c.setBusy(true);
        ContentEntity.startAnimation(c, 1249);

        World.submit(new Task(2200) {
            int craftAm = amm2;

            @Override
            public void execute() {
                if (craftAm <= 0 || !c.isBusy() || ContentEntity.getItemAmount(c, leather.getItemId()) <= 0) {
                    stop();
                    return;
                }
                if (ContentEntity.getItemAmount(c, 1734) <= 0) {
                    c.sendMessage("You don't have any thread.");
                    stop();
                    return;
                }
                if (ContentEntity.getItemAmount(c, leather.getItemId()) < item.getAmountReq()) {
                    c.sendMessage("You need at least " + item.getAmountReq() + " pieces of " + leather.getName().toLowerCase() + ".");
                    stop();
                    return;
                }
                if (ContentEntity.getItemAmount(c, needle) <= 0) {
                    c.sendMessage("You need a needle for this.");
                    stop();
                    return;
                }
                c.getAchievementTracker().itemSkilled(Skills.CRAFTING, item.getItemId(), 1);
                c.sendMessage("You craft the " + leather.getName().toLowerCase() +  " into " + Misc.aOrAn(item.getName()) + ((item.getName().contains("chaps") || item.getName().contains("boots") || item.getName().contains("vambs")) ? " pair of" : "") + " " + item.getName().toLowerCase() + ".");
                ContentEntity.deleteItemA(c, leather.getItemId(), item.getAmountReq());
                if (Misc.random(2) == 1)
                    ContentEntity.deleteItemA(c, 1734, 1);
                ContentEntity.addItem(c, item.getItemId(), 1);
                ContentEntity.addSkillXP(c, item.getExp() * Crafting.EXPMULTIPLIER, 12);
                if (craftAm > 1) {
                    ContentEntity.startAnimation(c, 1249);
                }
                craftAm--;
            }

            @Override
            public void stop() {
                c.getExtraData().remove("toCraft");
                c.getExtraData().remove("craftingFrom");
                c.getExtraData().remove("crafting");
                c.setBusy(false);
                ContentEntity.startAnimation(c, -1);
                super.stop();
            }

        });
    }
}
