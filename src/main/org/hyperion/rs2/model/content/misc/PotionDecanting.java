package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;

public class PotionDecanting {

    private static enum Potion {

        PRAYER(2434, 139, 141, 143),
        RANGING(2444, 169, 171, 173),
        ANTIFIRE(2452, 2454, 2456, 2458),
        MAGIC(3040, 3042, 3044, 3046),
        SUMMONING(12140, 12142, 12144, 12146),
        SUPER_ATTACK(2436, 145, 147, 149),
        SUPER_STRENGTH(2440, 157, 159, 161),
        SUPER_DEFENCE(2442, 163, 165, 167),
        SUPER_RESTORE(3024, 3026, 3028, 3030),
        RECOVER_SPECIAL(15300, 15301, 15302, 15303),
        SUPER_ANTIFIRE(15304, 15305, 15306, 15307),
        EXTREME_ATTACK(15308, 15309, 15310, 15311),
        EXTREME_STRENGTH(15312, 15313, 15314, 15315),
        EXTREME_DEFENCE(15316, 15317, 15318, 15319),
        EXTREME_MAGIC(15320, 15321, 15322, 15323),
        EXTREME_RANGING(15324, 15325, 15326, 15327),
        SUPER_PRAYER(15328, 15329, 15330, 15331),
        OVERLOAD(15332, 15333, 15334, 15335);

        private int potion1, potion2, potion3, potion4;

        private Potion(final int potion4, final int potion3, final int potion2, final int potion1) {
            this.potion1 = potion1;
            this.potion2 = potion2;
            this.potion3 = potion3;
            this.potion4 = potion4;
        }

        protected int getPotion1() {
            return potion1;
        }

        protected int getPotion2() {
            return potion2;
        }

        protected int getPotion3() {
            return potion3;
        }

        protected int getPotion4() {
            return potion4;
        }

    }

    public static void decantPotions(Player player) {
        for(Potion potion : Potion.values()) {
            final int[] potionIndex = {potion.getPotion1(), potion.getPotion2(), potion.getPotion3()};
            int doseCount = 0;
            for(int i = 0; i < potionIndex.length; i++) {
                if(player.getInventory().contains(potionIndex[i])) {
                    int count = player.getInventory().getCount(potionIndex[i]);
                    doseCount += (i + 1) * count;
                    for(int j = 0; j < count; j++) {
                        player.getInventory().remove(player.getInventory().getById(potionIndex[i]));
                    }
                }
            }
            while (doseCount >= 4) {
                player.getInventory().add(new Item(potion.getPotion4(), 1));
                doseCount -= 4;
            }
            if (doseCount > 0) {
                player.getInventory().add(new Item(potionIndex[doseCount - 1], 1));
            }
        }
        player.sendMessage("You have mixed all of your potions.");
    }

}
