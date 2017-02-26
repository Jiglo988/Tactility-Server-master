package org.hyperion.rs2.model.joshyachievementsv2.constraint.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraint;

public class DungeoneeringConstraint implements Constraint{

    public boolean constrained(final Player player){
        return player.getDungeoneering().inDungeon();
    }

    public String desc(){
        return "In A Dungeon";
    }

}
