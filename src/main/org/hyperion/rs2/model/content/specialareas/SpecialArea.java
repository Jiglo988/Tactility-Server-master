package org.hyperion.rs2.model.content.specialareas;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.util.Time;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 11/20/14
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SpecialArea {

    public long eventTime = 0L;


    public void check(final Player player) {
        final String enter = canEnter(player);
        if(inArea(player) && enter.length() > 1) {
            exit(player);
            player.sendImportantMessage(enter);
        }
    }

    public void enter(final Player player) {
        final String enter = canEnter(player);
        if(enter.length() > 2)  player.sendMessage(enter);
        else {
            player.sendMessage("For better switching use the lite client: @blu@ http://play.TactilityPk.com#url#");
            Magic.teleport(player, getDefaultLocation(), false);
        }

    }

    public int getPkLevel() {
        return isPkArea() ? 12 : -1;
    }

    public void exit(final Player player) {
        Magic.teleport(player, "home");
    }

    public Command command(final String name) {
        return new Command(name, Rank.PLAYER) {
            @Override
            public boolean execute(final Player player, final String input) {
                enter(player);
                return true;
            }
        };
    }

    public void createEvent() {
        eventTime = System.currentTimeMillis();
    }

    public boolean inEvent() {
        return System.currentTimeMillis() - eventTime < Time.THIRTY_MINUTES;
    }

    public abstract boolean canSpawn();
    public abstract boolean isPkArea();
    public abstract Position getDefaultLocation();

    public boolean inArea(final Player player) { return inArea(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());}
    public boolean wildInterface() { return false; }
    public abstract boolean inArea(final int x, final int y, final int z);
    public abstract String canEnter(final Player player);
}
