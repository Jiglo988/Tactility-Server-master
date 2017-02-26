package org.hyperion.rs2.model.content.skill;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.skill.crafting.Crafting;
import org.hyperion.rs2.model.content.skill.fletching.Fletching;

/**
 * Created by Gilles on 8/09/2015.
 */
public class ClickSkillingInterface implements ContentTemplate {
    public final static int[][] frameId = {
            {}, //0 items
            {}, //1 items
            {8866, 8874, 8878, 8869, 8870},//2 items
            {8880, 8889, 8893, 8897, 8883, 8884, 8885},//3 items
            {8899, 8909, 8913, 8917, 8921, 8902, 8903, 8904, 8905},//4 items
            {8938, 8949, 8953, 8957, 8961, 8965, 8941, 8942, 8943, 8944, 8945},//5 items
    };

    @Override
    public int[] getValues(int type) {
        if (type == 0) {
            int ai1[] = {
                    8909, 8889, 8949, 8874, 8913, 8893, 8953, 8878, 8917, 8897, 8957, 8921, 8961, 8965, 8908, 8888, 8948, 8873, 8912,
                    8892, 8952, 8877, 8916, 8896, 8956, 8920, 8960, 8964, 8907, 8887, 8947, 8872, 8911, 8891, 8951, 8876, 8915, 8895,
                    8955, 8919, 8959, 8963, 8906, 8946, 8886, 8871, 8910, 8950, 8890, 8875, 8914, 8954, 8894, 8918, 8958, 8962
            };
            return ai1;
        } else {
            return null;
        }
    }

    public boolean clickObject(final Player player, final int type, final int id, final int slot, final int itemId2, final int itemSlot2) {
        if (type == 0) {
            if (player.getExtraData().getBoolean("crafting")) {
                return Crafting.clickInterface(player, id);
            }
            if (player.getExtraData().getBoolean("fletching")) {
                return Fletching.clickInterface(player, id);
            }
            return false;
        }
        return false;
    }
}
