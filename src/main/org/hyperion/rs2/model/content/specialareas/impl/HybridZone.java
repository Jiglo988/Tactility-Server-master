package org.hyperion.rs2.model.content.specialareas.impl;

import org.hyperion.rs2.model.GameObject;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.specialareas.NIGGERUZ;
import org.hyperion.rs2.pf.Point;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 5/1/15
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class HybridZone extends NIGGERUZ {

    private final Point cornerSW = new Point(2970, 3605),
    cornerNE = new Point(2983, 3616);


    public HybridZone() {
        super(24);
    }

    @Override
    public int getPkLevel() {
        return -1;
    }


    public void check(final Player player) {
        final String enter = canEnter(player);
        if(inTheWild(player.getPosition()) && enter.length() > 1) {
            exit(player);
            player.sendMessage(enter);
        }
    }


    public String canEnter(Player player) {
            final Item shield = player.getEquipment().get(Equipment.SLOT_SHIELD);
            if(shield != null && (shield.getId() == 13740 || shield.getId() == 13744))
                return "You cannot bring divines to this area";
            if(!player.getSpellBook().isAncient())
                return "You must be on ancients to be here";
        return "";
    }

    @Override
    public void initObjects(final List<GameObject> list) {

        for(int x = cornerSW.getX() ;x <= cornerNE.getX(); x++) {
            if(x != 2976 && x != 2977 )
                list.add(new GameObject(DEFINITION, Position.create(x, cornerSW.getY(), height), 10, 2, false));
            list.add(new GameObject(DEFINITION, Position.create(x, cornerNE.getY(), height), 10, 0, false));
        }

        for(int y = cornerSW.getY(); y < cornerNE.getY(); y++) {
            list.add(new GameObject(DEFINITION, Position.create(cornerNE.getX(), y, height), 10, 1, false));
            list.add(new GameObject(DEFINITION, Position.create(cornerSW.getX(), y, height), 10, 3, false));

        }



    }

    @Override
    public Position getDefaultLocation() {
        return Position.create(2975, 3610, height);
    }

    @Override
    public boolean inArea(int x, int y, int z) {
        return z == height &&
                (x > cornerSW.getX() && y >= cornerSW.getY() && x <= cornerNE.getX() && y <= cornerNE.getY()) ;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean inTheWild(Position l) {
        return l.getZ() == height && l.inPvPArea();
    }

    public boolean wildInterface() {
        return false;
    }
}
