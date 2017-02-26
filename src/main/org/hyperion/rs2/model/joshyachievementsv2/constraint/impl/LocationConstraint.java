package org.hyperion.rs2.model.joshyachievementsv2.constraint.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraint;

public class LocationConstraint implements Constraint{

    public final int minX;
    public final int minY;

    public final int maxX;
    public final int maxY;

    public final int height;

    private final String desc;

    public LocationConstraint(final int minX, final int minY, final int maxX, final int maxY, final int height){
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.height = height;

        if(height > -1)
            desc = String.format("Between location (%d, %d) and (%d, %d) at a height of %d", minX, minY, maxX, maxY, height);
        else
            desc = String.format("Between location (%d, %d) and (%d, %d)", minX, minY, maxX, maxY);
    }

    public boolean constrained(final Player player){
        final int x = player.getPosition().getX();
        final int y = player.getPosition().getY();
        return (height == -1 || player.getPosition().getZ() == height)
                && x >= minX && y >= minY
                && x <= maxX && y <= maxY;
    }

    public String desc(){
        return desc;
    }
}
