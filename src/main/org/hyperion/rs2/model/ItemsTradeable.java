package org.hyperion.rs2.model;

import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc2.Food;
import org.hyperion.rs2.model.shops.PvMStore;
import org.hyperion.rs2.model.shops.SlayerShop;
import org.hyperion.util.ArrayUtils;

/**
 * Since I just CAN'T seem to fix shit with itemdefinitions...
 */
public class ItemsTradeable {
	public static boolean isTradeable(int id) {
        if(id == 16639 || id == 13560 || (id >= 13195 && id <= 13205)) //emblems n shit
            return false;
        if(id == SlayerShop.SLAYER_HELM || id == SlayerShop.FULL_HELM)
            return true;
        if(id == 15426 || id == 6542)
            return false;
        final ItemDefinition itemDef;
        if((itemDef = ItemDefinition.forId(id)) != null && (itemDef.getName().toLowerCase().contains("(class") || itemDef.getName().toLowerCase().contains("clue")))
            return false;
		return !ArrayUtils.contains(id, untradeables());
	}

    public static boolean isTradeable2(int id, int gameMode) {
        if(ItemSpawning.canSpawn(id) && gameMode == 0 && Food.get(id) == null)
            return false;
        return isTradeable(id);
    }

    private static int[] untradeables() {
        return new int[]{19709,18509,15707, 2412, 2413, 2414, 2570, 2571, 2560, 2561,
                11056, 11057, 11051, 11052, 11055, 11053, 2558, 11337, 11338, 2556,
                2554, 4067, 4511, 4509, 4510, 4508, 4512, 10547, 10548, 10549,
                10550, 7806, 7807, 7808, 7809, 4566, 8850, 10551, 8839, 8840, 8842,
                11663, 11664, 11665, 3842, 3844, 3840, 8844, 8845, 8846, 8847,
                8848, 8849, 8850, 10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457,
                7456, 7455, 7454, 8839, 8840, 8842, 11663, 11664, 11665, 10499,
                9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799,
                9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749,
                9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806,
                9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 13350,
                9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804,
                9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 11793, 11794,
                11795, 11796, 11798, 6858, 6859, 6860, 6861, 6856, 6857, 15441,
                15442, 15443, 15444, 15600, 15606, 15612, 15618, 15602, 15608,
                15614, 15620, 15604, 15610, 15616, 15622, 15021, 15022, 15023,
                15024, 15025, 15026, 15027, 15028, 15029, 15030, 15031, 15032,
                15033, 15034, 15035, 15036, 15037, 15038, 15039, 15040, 15041,
                15042, 15043, 15044, 18350, 18352, 18354, 18356, 18358, 18360,
                12158, 12159, 12160, 12161, 12163, 12162, 12164, 12165, 12166,
                12167, 12168, 19780, 13351, 19669, 19111, 19713, 19716, 19719, 19815, 10858,
                19816, 19817, 2430, 2431, 15332, 15333, 15334, 15335, 17061, 17193, 17339,13263,15492,
                17215, 17317, 16887, 16337, 18349, 18351, 18353, 12747, 12744, 10025, 10026, 8195, 18746, 17279, 17280,17291, 12862,17291,17292,15834,15352
                , 13188, 13189, 13190, 13191, 13192, 13193, 13194, 18806, 14600, 14602, 14603, 14605, 17660,14595, 14602, 14603, 14604, 14605, 15420, 11949, 15426,
                19468, 19469, 17656, 17650, 6603, 6604, 2422, 13895, 13889, 12852, PvMStore.TOKEN, 16638,
                3243, 11061, 5021, 5022, 5023, 19773, 17273};
    }

}
