package org.hyperion.rs2.model;

import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.shops.DonatorShop;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a single item.
 *
 * @author Graham Edgecombe
 */
public class Item {

    /**
     * The id.
     */
    private int id;

    /**
     * The number of items.
     */
    private int count;

    /**
     * @param id
     * @return
     */
    public static Item create(int id) {
        return new Item(id, 1);
    }

    /**
     * @param id
     * @param count
     * @return
     */
    public static Item create(int id, int count) {
        return count <= 0 ? null : new Item(id, count);
    }

    /**
     * Creates a single item.
     *
     * @param id The id.
     */
    public Item(int id) {
        this(id, 1);
    }

    /**
     * Creates a stacked item.
     *
     * @param id    The id.
     * @param count The number of items.
     * @throws IllegalArgumentException if count is negative.
     */
    public Item(int id, int count) {
        if (count < 0) {
            System.out.println("Count is " + count);
            throw new IllegalArgumentException("Count cannot be negative.");
        }
        this.id = id;
        this.count = count;
    }

    /**
     * Compares the price between this item and the specified item
     *
     * @param item the item in which to compare prices with
     * @return the item with the highest high alchemy price
     */
    public Item comparePriceWith(Item item) {
        int dpValue1 = DonatorShop.getPrice(getId());
        int dpValue2 = item != null ? DonatorShop.getPrice(item.getId()) : -1;
        if (dpValue1 > dpValue2)
            return this;
        else if (dpValue1 < dpValue2)
            return item;
        int item1Value = getDefinition().getHighAlcValue();
        int item2Value = item == null ? -1 : item.getDefinition().getHighAlcValue();
        return item1Value > item2Value ? this : item;
    }

    /**
     * Compares the price between this item and an item with the specified id
     *
     * @param itemId the item id in which to compare prices with
     * @return the item id with the highest high alchemy price
     */
    public int comparePriceWith(int itemId) {
        ItemDefinition itemDef = ItemDefinition.forId(itemId);
        int dpValue1 = DonatorShop.getPrice(getId());
        int dpValue2 = DonatorShop.getPrice(itemId);
        if (dpValue1 > dpValue2)
            return getId();
        else if (dpValue1 < dpValue2)
            return itemId;
        int item1Value = getDefinition().getHighAlcValue();
        int item2Value = itemDef == null ? -1 : itemDef.getHighAlcValue();
        return item1Value > item2Value ? getId() : itemId;
    }


    public BankItem toBankItem(int tab) {
        return new BankItem(tab, this.id, this.count);
    }

    /**
     * Gets the definition of this item.
     *
     * @return The definition.
     */
    public ItemDefinition getDefinition() {
        return ItemDefinition.forId(id);
    }

    /**
     * Gets the item id.
     *
     * @return The item id.
     */
    public int getId() {
        return id;
    }

    public void setId(int ID) {
        id = ID;
    }

    /**
     * Gets the count.
     *
     * @return The count.
     */
    public int getCount() {
        if (count < 0) {
            count = 0;
            throw new IllegalArgumentException("Huge bug in Item class!:id,count:" + id + "," + count);
        }
        return count;
    }

    /**
     * Sets the count.
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object object) {
        Item item;
        if (object instanceof Item)
            item = (Item) object;
        else
            return false;
        return id == item.id;
    }

    @Override
    public String toString() {
        return Item.class.getName() + " [id=" + id + ", count=" + count + "]";
    }

    public static final List<Integer> SACRED_CLAY = Arrays.asList(
            14122, 14123, 14124, 14125, 14126, 14127, 14128, 14129, 14130, 14131,
            14132, 14133, 14134, 14135, 14136, 14137, 14138, 14139, 14140, 14141,
            14142, 14143, 14144, 14145, 14146, 14147, 14148, 14149, 14150, 14151,
            14152, 14153, 14154, 14155, 14156, 14157, 14158, 14159, 14160, 14161,
            14162, 14163, 14164, 14165, 14166, 14167, 14168, 14169, 14170, 14171,
            14172, 14173, 14174, 14175, 14176, 14177, 14178, 14179, 14180, 14181,
            14182, 14183, 14184, 14185, 14186, 14187, 14188, 14189, 14190, 14191,
            14192, 14193, 14194, 14195, 14196, 14197, 14198, 14199, 14200, 14201,
            14202, 14203, 14204, 14205, 14206, 14287, 14288, 14289, 14290, 14291,
            14292, 14293, 14294, 14295, 14296, 14297, 14298, 14299, 14300, 14301,
            14302, 14303, 14304, 14305, 14306, 14307, 14308, 14309, 14310, 14311,
            14312, 14313, 14314, 14315, 14316, 14317, 14318, 14319, 14320, 14321,
            14322, 14323, 14324, 14325, 14326, 14327, 14328, 14329, 14330, 14331,
            14332, 14333, 14334, 14335, 14336, 14337, 14338, 14339, 14340, 14341,
            14342, 14343, 14344, 14345, 14346, 14347, 14348, 14349, 14350, 14351,
            14352, 14353, 14354, 14355, 14356, 14357, 14358, 14359, 14360, 14361,
            14362, 14363, 14364, 14365, 14366, 14367, 14368, 14369, 14370, 14371,
            14372, 14373, 14374, 14375, 14376, 14377, 14378, 14379, 14380, 14381,
            14382, 14383, 14384, 14385, 14386, 14391, 14392, 14393, 14394, 14395,
            14396, 14397, 14398, 14399, 14400, 14401, 14402, 14403, 14404, 14405,
            14406, 14407, 14408, 14409, 14410, 14411, 14412, 14413, 14414, 14415,
            14416, 14417, 14418, 14419, 14420, 14422, 14423, 14424, 14425, 14426
    );

}
