package org.hyperion.rs2.model.cluescroll.util;

import java.util.Random;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.cluescroll.ClueScroll;
import org.hyperion.rs2.model.cluescroll.ClueScrollManager;
import org.hyperion.util.Misc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class ClueScrollUtils {

    private static final Random RAND = new Random();

    private ClueScrollUtils(){}

    public static int rand(final int min, final int max){
        return min + RAND.nextInt(max - min + 1);
    }

    public static boolean isChance(final int chance){
        return chance > 0 && rand(1, 1000) <= chance;
    }

    public static boolean dropClueScroll(Player player, NPC npc) {
        if(ClueScrollManager.hasClueScroll(player))
            return false;
        double clueScrollChance = 1;
        if(npc.getDefinition().combat() >= 240) {
            clueScrollChance += ((npc.getDefinition().combat() - 120)/6);
        } else if(npc.getDefinition().combat() >= 120) {
            clueScrollChance += ((npc.getDefinition().combat() - 90)/3);
        } else if(npc.getDefinition().combat() >= 80) {
            clueScrollChance += ((npc.getDefinition().combat() - 60)/3);
        }

        if(Rank.hasAbility(player, Rank.SUPER_DONATOR)) {
            clueScrollChance *= 1.25;
        } else if(Rank.hasAbility(player, Rank.DONATOR)) {
            clueScrollChance *= 1.1;
        }

        if(player.getSlayer().isTask(npc.getDefinition().getId()))
            clueScrollChance *= 2;

        if(Misc.random(1000) <= clueScrollChance*10)
            return true;
        return false;
    }

    public static Item getScroll(NPC npc) {
        Item item = null;
        if(npc.combatLevel >= 200) {
            item = Item.create(getRandomElite().getId(), 1);
        } else if(npc.combatLevel >= 110) {
            item = Item.create(getRandomHard().getId(), 1);
        } else if(npc.combatLevel >= 70) {
            item = Item.create(getRandomMedium().getId(), 1);
        } else {
            item = Item.create(getRandomEasy().getId(), 1);
        }
        return item;
    }

    public static ClueScroll getRandomEasy() {
        return getRandom(ClueScroll.Difficulty.EASY);
    }

    public static ClueScroll getRandomMedium() {
        return getRandom(ClueScroll.Difficulty.MEDIUM);
    }

    public static ClueScroll getRandomHard() {
        return getRandom(ClueScroll.Difficulty.HARD);
    }

    public static ClueScroll getRandomElite() {
        return getRandom(ClueScroll.Difficulty.ELITE);
    }

    public static ClueScroll getRandom(ClueScroll.Difficulty difficulty) {
        return ClueScrollManager.getAll(difficulty).get(Misc.random(ClueScrollManager.getAll(difficulty).size() - 1));
    }

    public static Element createElement(final Document doc, final String tag, final Object content){
        final Element element = doc.createElement(tag);
        element.setTextContent(content.toString());
        return element;
    }

    public static String getString(final Element root, final String tag){
        return root.getElementsByTagName(tag).item(0).getTextContent();
    }

    public static Integer getInteger(final Element root, final String tag){
        return Integer.parseInt(getString(root, tag));
    }
}
