package org.hyperion.rs2.model.joshyachievementsv2.constraint;

import org.hyperion.rs2.model.Player;

public interface Constraint {

    boolean constrained(final Player player);

    String desc();

    default String shortDesc(){
        return desc().length() <= 70 ? desc() : (desc().substring(0, 70) + "...");
    }

    default String constrainedText(final Player player){
        if(constrained(player))
            return "Requirement met";
        else
            return "Requirement not met";
    }
}
