package org.hyperion.rs2.model.joshyachievementsv2.constraint.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraint;
import org.hyperion.rs2.model.joshyachievementsv2.utils.AchievementUtils;

public class EquipmentConstraint implements Constraint{

    private static final Map<Integer, Equipment.EquipmentType> TYPES = new HashMap<>();

    static{
        for(final Equipment.EquipmentType type : Equipment.EquipmentType.values())
            if(!TYPES.containsKey(type.getSlot()))
                TYPES.put(type.getSlot(), type);
    }

    public final int slot;
    public final List<Integer> itemIds;
    public final int itemQuantity;

    private final String desc;

    public EquipmentConstraint(final int slot, final List<Integer> itemIds, final int itemQuantity){
        this.slot = slot;
        this.itemIds = itemIds;
        this.itemQuantity = itemQuantity;

        final String slotName = TYPES.get(slot).getDescription();

        final String itemsJoined = AchievementUtils.joinItems(itemIds);

        if(itemQuantity > 1)
            desc = String.format("Wearing %s: %,d x %s", slotName, itemQuantity, itemsJoined);
        else
            desc = String.format("Wearing %s: %s", slotName, itemsJoined);
    }

    public String desc(){
        return desc;
    }

    public boolean constrained(final Player player){
        final Item equipped = player.getEquipment().get(slot);
        return equipped != null
                && itemIds.contains(equipped.getId())
                && equipped.getCount() >= itemQuantity;
    }
}
