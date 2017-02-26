package org.hyperion.rs2.model.recolor.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.recolor.Recolor;

public class ViewRecolorsCommand extends Command{

    public ViewRecolorsCommand(){
        super("viewrecolors", Rank.DEVELOPER);
    }

    public boolean execute(final Player player, final String input){
        player.sendf("You have %,d recolors!", player.getRecolorManager().getCount());
        for(final Recolor recolor : player.getRecolorManager().getAll())
            player.sendf(recolor.toReadableString());
        return false;
    }
}
