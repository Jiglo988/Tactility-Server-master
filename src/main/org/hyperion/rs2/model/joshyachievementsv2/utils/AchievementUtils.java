package org.hyperion.rs2.model.joshyachievementsv2.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.NPCDefinition;

public final class AchievementUtils{

    private AchievementUtils(){}

    public static String joinNpcs(final List<Integer> npcIds){
        return npcIds.stream()
                .map(NPCDefinition::forId)
                .filter(Objects::nonNull)
                .map(NPCDefinition::getName)
                .filter(s -> !s.equalsIgnoreCase("null"))
                .map(s -> s.replace("_", " "))
                .distinct()
                .collect(Collectors.joining(" or "));
    }

    public static String joinItems(final List<Integer> itemIds){
        return itemIds.stream()
                .map(ItemDefinition::forId)
                .filter(Objects::nonNull)
                .map(ItemDefinition::getProperName)
                .distinct()
                .collect(Collectors.joining(" or "));
    }
}
