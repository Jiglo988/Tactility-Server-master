package org.hyperion.rs2.model.content.minigame.barrowsffa;


import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Equipment;
import static org.hyperion.rs2.model.content.minigame.barrowsffa.BarrowsFFA.DIALOGUE_ID;

import java.util.stream.Stream;

public enum BarrowSet {
    DHAROK(DIALOGUE_ID + 1, Item.create(4716), Item.create(4718), Item.create(4720), Item.create(4722)),
    KARIL(DIALOGUE_ID + 2, Item.create(4732), Item.create(4734), Item.create(4736), Item.create(4738), Item.create(4740, 100)),
    AHRIM(DIALOGUE_ID + 3, Item.create(4714), Item.create(4712), Item.create(4710), Item.create(4708), Item.create(555, 1000), Item.create(560, 500), Item.create(565, 250)),
    GUTHAN(DIALOGUE_ID + 5, Item.create(4724), Item.create(4726), Item.create(4728), Item.create(4730)),
    TORAGS(DIALOGUE_ID + 6, Item.create(4745), Item.create(4747), Item.create(4749), Item.create(4751)),
    VERACS(DIALOGUE_ID + 7, Item.create(4753), Item.create(4755), Item.create(4757), Item.create(4759));

    public static final BarrowSet[] SETS = values().clone();

    private final Item[] items;
    private final int dialogueId; //dialogue id for picking the set

    private BarrowSet(int dialogueAction ,final Item... ids) {
        this.items = ids;
        this.dialogueId = dialogueAction;
    }

    public void equip(final Player player) {
        int i = 0;
        for(; i < 4; i++) {
            player.getEquipment().set(Equipment.getType(items[i]).getSlot(), Item.create(items[i].getId()));
        }
        for(; i < items.length; i++)
            player.getInventory().add(Item.create(items[i].getId(), items[i].getCount()));
    }

    public static BarrowSet forDialogue(final int id) {
        for(final BarrowSet set : SETS)
            if(set.dialogueId == id)
                return set;
        return null;
    }


}