package org.hyperion.rs2.model.content.skill.crafting;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.util.Misc;

/**
 * Created by Gilles on 8/09/2015.
 */
public class GemCutting extends Crafting {

    public static int chisel = 1755;

    public enum Gem {
        SAPPHIRE_GEM(1623, 1607, 40, 20, 888),
        EMERALD_GEM(1621, 1605, 65, 27, 889),
        RUBY_GEM(1619, 1603, 80, 34, 887),
        DIAMOND_GEM(1617, 1601, 85, 43, 886),
        DRAGONSTONE_GEM(1631, 1615, 100, 55, 885),
        OPAL_GEM(1625, 1609, 70, 23, 890),
        JADE_GEM(1627, 1611, 75, 25, 891),
        RED_TOPAZ_GEM(1629, 1613, 80, 30, 892),

        SAPPHIRE_BOLT_TIPS(1607, 9189, 40, 1, 888),
        EMERALD_BOLT_TIPS(1605, 9190, 50, 20, 889),
        RUBY_BOLT_TIPS(1603, 9191, 60, 30, 887),
        DIAMOND_BOLT_TIPS(1601, 9192, 90, 40, 886),
        DRAGONSTONE_BOLT_TIPS(1615, 9193, 120, 60, 885),
        OPAL_BOLT_TIPS(1609, 9187, 90, 20, 890),
        JADE_BOLT_TIPS(1611, 45, 70, 25, 891),
        RED_TOPAZ_BOLT_TIPS(1613, 9188, 80, 30, 892);

        private int gemId;
        private int resultId;
        private int exp;
        private int levelReq;
        private int emote;

        public int getResultId() {
            return resultId;
        }

        public int getGemId() {
            return gemId;
        }

        public int getExp() {
            return exp;
        }

        public int getLevelReq() {
            return levelReq;
        }

        public String getName() {
            return Misc.ucFirst(this.toString().replaceAll("_", " ").replaceAll(" GEM", "").toLowerCase());
        }

        public int getEmote() {
            return emote;
        }

        Gem(int gemId, int resultId, int exp, int levelReq, int emote) {
            this.gemId = gemId;
            this.resultId = resultId;
            this.exp = exp;
            this.levelReq = levelReq;
            this.emote = emote;
        }
    }

    private static Gem getGem(int i) {
        for(Gem gem : Gem.values()) {
            if(gem.getGemId() == i)
                return gem;
        }
        return null;
    }


    public static boolean cutGem(Player c, int gem, int slot) {
        Gem g = getGem(gem);
        if(g == null) {
            return false;
        }

        if(ContentEntity.returnSkillLevel(c, 12) < g.getLevelReq()) {
            ContentEntity.sendMessage(c, "You need a crafting level of " + g.getLevelReq() + " to cut this gem.");
            return false;
        }

        if(c.isBusy())
            return false;

        c.setBusy(true);
        ContentEntity.startAnimation(c, g.getEmote());

        ContentEntity.sendMessage(c, "You start cutting the gem...");

        World.submit(new Task(2200) {
            @Override
            public void execute() {
                if(c.getRandomEvent().skillAction(2))
                    this.stop();
                ContentEntity.deleteItem(c, g.getGemId(), slot);
                boolean isBolt = g.getName().contains("tip");
                if (isBolt) {
                    ContentEntity.sendMessage(c, "You cut the gem into " + g.getName().toLowerCase() + ".");
                    ContentEntity.addItem(c, g.getResultId(), 15);
                } else {
                    ContentEntity.sendMessage(c, "You cut the gem into " + Misc.aOrAn(g.getName()) + " " + g.getName().toLowerCase() + ".");
                    ContentEntity.addItem(c, g.getResultId(), 1);
                }
                ContentEntity.addSkillXP(c, g.getExp(), Skills.CRAFTING);
                c.getAchievementTracker().itemSkilled(Skills.CRAFTING, g.getResultId(), 1);
                c.setBusy(false);
                this.stop();
            }
        });
        return true;
    }

}
