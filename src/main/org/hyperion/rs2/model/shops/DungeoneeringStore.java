package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 3/1/15
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DungeoneeringStore extends PointsShop {

    public DungeoneeringStore(int id, String name, Container container) {
        super(id, name, container);
    }


    @Override
    public String getPointsName() {
        return "Dungeoneering Tokens";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPrice(int itemId) {
        switch(itemId) {
            case 17985:
            case 17986:
            case 17987:
            case 17988:
            case 17989:
                return 5_000 + (itemId - 17984) * 3000;
            case 16401:
                return 500_000;
            case 16953:
                return 400_000;
            case 17135:
                return 350_000;
            case 16687:
                return 200_000;
            case 17257:
                return 300_000;
            case 16709:
                return 100_000;
            case 17359:
                return 150_000;
            case 16357:
                return 200_000;
            case 17279:
                return 100_000;
        }
        return 50_000;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void buyFromShop(Player player, Item item) {
        super.buyFromShop(player, item);
        player.getActionSender().sendString(3901, String.format("Dungeoneering tokens: @gre@%,d", player.getDungeoneering().getTokens()));
    }

    @Override
    protected int getPointsAmount(Player player) {
        return player.getDungeoneering().getTokens();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void setPointsAmount(Player player, int value) {
        player.getDungeoneering().setTokens(value);
    }

}
