package org.hyperion.rs2.model.content.skill.crafting;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.ArrayUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * Handles crafting
 *
 * @author Glis
 */

public class Crafting implements ContentTemplate {

    public Crafting() {
    }

    public final static int EXPMULTIPLIER = 3 * Constants.XPRATE;

    public boolean attemptCraft(Player c, int useItem, int slot1, int onItem, int slot2) {
        if(onItem == GemCutting.chisel) {
            onItem = useItem;
            slot2 = slot1;
            useItem = GemCutting.chisel;
        }
        if(onItem == LeatherCrafting.needle) {
            onItem = useItem;
            useItem = LeatherCrafting.needle;
        }
        if(useItem == GemCutting.chisel)
            return GemCutting.cutGem(c, onItem, slot2);
        else if(useItem == LeatherCrafting.needle)
            return LeatherCrafting.craftLeather(c, onItem);
        return false;
    }

    private int[] craftingTools = {LeatherCrafting.needle, GemCutting.chisel};

    @Override
    public int[] getValues(int type) {
        if(type == 13) {
            List<Integer> j = new ArrayList();
            for(GemCutting.Gem gem : GemCutting.Gem.values()) {
                j.add(gem.getGemId());
            }
            for(LeatherCrafting.Leather leather : LeatherCrafting.Leather.values()) {
                j.add(leather.getItemId());
            }
            for(LeatherCrafting.Leather_Item leatherItem : LeatherCrafting.Leather_Item.values()) {
                j.add(leatherItem.getItemId());
            }
            for(int i = 0; i < craftingTools.length; i++) {
                j.add(craftingTools[i]);
            }
            return ArrayUtils.fromList(j);
        }
        if(type == 7) {
            //Flax
            int[] j = {2646, 2644};
            return j;
        }
        if(type == 14) {
            //Spinning wheel
            int[] j = {1779};
            return j;
        }
        return null;
    }

    public static boolean clickInterface(final Player client, final int id) {
        switch(id) {
            case 8909:
            case 8889:
            case 8949:
            case 8874:
                return LeatherCrafting.startAgain(client, 1, 0);
            case 8913:
            case 8893:
            case 8953:
            case 8878:
                return LeatherCrafting.startAgain(client, 1, 1);
            case 8917:
            case 8897:
            case 8957:
                return LeatherCrafting.startAgain(client, 1, 2);
            case 8921:
            case 8961:
                return LeatherCrafting.startAgain(client, 1, 3);
            case 8965:
                return LeatherCrafting.startAgain(client, 1, 4);

            case 8908:
            case 8888:
            case 8948:
            case 8873:
                return LeatherCrafting.startAgain(client, 5, 0);
            case 8912:
            case 8892:
            case 8952:
            case 8877:
                return LeatherCrafting.startAgain(client, 5, 1);
            case 8916:
            case 8896:
            case 8956:
                return LeatherCrafting.startAgain(client, 5, 2);
            case 8920:
            case 8960:
                return LeatherCrafting.startAgain(client, 5, 3);
            case 8964:
                return LeatherCrafting.startAgain(client, 5, 4);

            case 8907:
            case 8887:
            case 8947:
            case 8872:
                return LeatherCrafting.startAgain(client, 10, 0);
            case 8911:
            case 8891:
            case 8951:
            case 8876:
                return LeatherCrafting.startAgain(client, 10, 1);
            case 8915:
            case 8895:
            case 8955:
                return LeatherCrafting.startAgain(client, 10, 2);
            case 8919:
            case 8959:
                return LeatherCrafting.startAgain(client, 10, 3);
            case 8963:
                return LeatherCrafting.startAgain(client, 10, 4);

            case 8906:
            case 8946:
            case 8886:
            case 8871:
                return LeatherCrafting.startAgain(client, 28, 0);
            case 8910:
            case 8950:
            case 8890:
            case 8875:
                return LeatherCrafting.startAgain(client, 28, 1);
            case 8914:
            case 8954:
            case 8894:
                return LeatherCrafting.startAgain(client, 28, 2);
            case 8918:
            case 8958:
                return LeatherCrafting.startAgain(client, 28, 3);
            case 8962:
                return LeatherCrafting.startAgain(client, 28, 4);
        }
        return false;
    }

    @Override
    public boolean clickObject(final Player client, final int type, final int id, final int slot, final int itemId2, final int itemSlot2) {
        if(type == 13) {
            return attemptCraft(client, id, slot, itemId2, itemSlot2);
        }
        if(type == 7) {
            if(id == 2644)
                return Flax.spinFlax(client, id);
            return Flax.pickFlax(client, id);
        }
        if(type == 14) {
            return Flax.spinFlax(client, id);
        }
        return false;
    }


    @Override
    public void init() throws FileNotFoundException {
    }
}