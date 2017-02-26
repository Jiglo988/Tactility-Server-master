package org.hyperion.rs2.model.content.specialareas.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.content.specialareas.NIGGERUZ;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 11/20/14
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurePk extends NIGGERUZ {


    public static final int HEIGHT = 444;

    public PurePk() {
        super(HEIGHT);
    }

    @Override public String canEnter(final Player player) {
        if(player.getSkills().getRealLevels()[Skills.DEFENCE] > 20)
            return "You need 20 def or less to enter this area";
        return "";
    }




}
