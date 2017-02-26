package org.hyperion.rs2.model.content.misc2;

import org.hyperion.engine.task.impl.WildernessBossTask;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 8/28/15
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class WildyBossTracker implements ContentTemplate {


    @Override
    public int[] getValues(int type) {
        if(type == ClickType.EAT)
            return new int[]{15008};
        return new int[0];
    }

    @Override
    public boolean itemOptionOne(Player player, int id, int slot, int interfaceId) {
        if(WildernessBossTask.currentBoss != null) {
            if (Combat.getWildLevel(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()) > 0) {
                player.sendMessage(compassDirection(player.getPosition().getX(), player.getPosition().getY(), WildernessBossTask.currentBoss.getPosition().getX(), WildernessBossTask.currentBoss.getPosition().getY()));
            } else {
                player.sendMessage("This item only works in the wilderness.");
            }
        } else {
            player.sendf("Wilderness boss spawning in %s minutes.", TimeUnit.MINUTES.convert(
                            (WildernessBossTask.DELAY_FOR_RESPAWN - (System.currentTimeMillis() - WildernessBossTask.timeStart)), TimeUnit.MILLISECONDS)
            );
        }
        return true;
    }

    public static String compassDirection(int xPos, int yPos, int xDest, int yDest) {

        final StringBuilder builder = new StringBuilder();
        if(yPos != yDest && (yPos + 7 < yDest || yPos - 7 > yDest)) {
            builder.append(yPos > yDest ? "south" : "north");
        }
        if(xPos != xDest && (xPos + 7 < xDest || xPos - 7 > xDest)) {
            builder.append(xPos > xDest ? "west" : "east");
        }
        if(builder.length() == 0)
            return "The boss should be here somewhere...";
        return "The boss is to the " + builder.toString() + " of you.";

    }

}
