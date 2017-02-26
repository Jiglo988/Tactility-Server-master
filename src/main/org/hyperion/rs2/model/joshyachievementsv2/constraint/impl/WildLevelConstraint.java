package org.hyperion.rs2.model.joshyachievementsv2.constraint.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraint;

public class WildLevelConstraint implements Constraint{

    public final int minLevel;
    public final int maxLevel;

    private final String desc;

    public WildLevelConstraint(final int minLevel, final int maxLevel){
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;

        if(maxLevel < minLevel)
            desc = String.format("In wild at least level %,d", minLevel);
        else if(maxLevel == minLevel)
            desc = String.format("In wild at level %,d", minLevel);
        else
            desc = String.format("In wild between level %,d and %,d", minLevel, maxLevel);
    }

    public boolean constrained(final Player player){
        final int x = player.getPosition().getX();
        final int y = player.getPosition().getY();
        final int z = player.getPosition().getZ();
        final int level = Combat.getWildLevel(x, y, z);
        return level >= minLevel && level <= (maxLevel < minLevel ? level : maxLevel);
    }

    public String desc(){
        return desc;
    }
}
