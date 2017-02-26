package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 11/3/14
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class SlayerShop extends PointsShop {
    public SlayerShop(int id, String name, Container container) {
        super(id, name, container);
    }

    @Override
    public String getPointsName() {
        return "Slayer Points";  //To change body of implemented methods use File | Settings | File Templates.
    }



    public static final int SLAYER_HELM = 13263, FOCUS_SIGHT = 15490, HEX_CREST = 15488, FULL_HELM = 15492;
    @Override
    public int getPrice(int itemId) {
        switch(itemId) {
            case 15490:
            case 15488:
                return 300;
            case 13263:
                return 400;
            case 15492:
                return 1000;
            case 17291:
                return 500;
            case 12862:
                return 250;
            case 16639:
                return 40;
            case 16638:
                return 100;
        }
        return 5000;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected int getPointsAmount(Player player) {
        return player.getSlayer().getSlayerPoints();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void setPointsAmount(Player player, int value) {
        player.getSlayer().setPoints(value);
    }

    public static boolean isFullHelm(final Player player) {
        return player.getEquipment().getItemId(Equipment.SLOT_HELM)== 15492;
    }

    public static boolean hasHex(final Player player) {
        return isFullHelm(player) || player.getEquipment().getItemId(Equipment.SLOT_HELM) == 15488;
    }

    public static boolean hasFocus(final Player player) {
        return isFullHelm(player) || player.getEquipment().getItemId(Equipment.SLOT_HELM)== 15490;
    }

    public static boolean hasHelm(final Player player) {
        return isFullHelm(player) || player.getEquipment().getItemId(Equipment.SLOT_HELM) == 13263;
    }


}
