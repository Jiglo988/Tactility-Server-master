package org.hyperion.rs2.model.content.specialareas.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.specialareas.SpecialArea;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/31/14
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewGamePK extends SpecialArea {

    public static final int HEIGHT = 448;


    @Override
    public boolean inArea(final int x, final int y, final int z) {
        return z == HEIGHT;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String canEnter(Player player) {
        if(!player.hardMode())
            return "You must be in hard mode to be in this area";
        return "";    }

    @Override
    public boolean isPkArea() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Position getDefaultLocation() {
        return Position.create(3087, 3515, HEIGHT);
    }

    @Override
    public boolean canSpawn() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
