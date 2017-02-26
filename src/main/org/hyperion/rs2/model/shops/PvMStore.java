package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 5/26/15
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class PvMStore extends CurrencyShop{

    private static final int DEFAULT_PRICE = 5000;

    public static final int TOKEN = 17564;


    /**
     * @param id
     * @param name
     * @param container
     */
    public PvMStore(int id, String name, Container container) {
        super(id, name, container, TOKEN, false);
    }

    @Override
    public void valueSellItem(Player player, Item item) {
    }

    @Override
    public void sellToShop(Player player, Item item) {
        player.sendMessage("You cannot sell to this shop!");
    }

    @Override
    public int getSpecialPrice(Item item) {
        if(item == null) throw new IllegalArgumentException("Null item");
        final int id = item.getId();
        switch(id) {
            case 13870: //morrigan body
            case 13858: //zuriel's body
                return 45;
            case 13861: //zuriel's bottom
            case 13873: //morrigans chaps
                return 35;
            case 13864: //zuriel's hood
            case 13876: //morrigan coif
                return 20;
            case 13884: //stat body
                return 180;
            case 13890: //stat legs
                return 145;
            case 13896: //stat helm
                return 80;
            case 13902: //stat hammer
                return 450;
            case 13887: //vesta chain
            case 13893: //vesta legs
            case 13905: //vesta spear
                return 250;
            case 17652:
                return 3;
            case 13115:
                return 2000;
            case 13109:
                return 1000;
            case 19325:
                return 20000;
            case 19323:
                return 25000;

        }
        return DEFAULT_PRICE;
    }
}


