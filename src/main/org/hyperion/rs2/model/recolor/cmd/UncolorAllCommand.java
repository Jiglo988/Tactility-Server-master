package org.hyperion.rs2.model.recolor.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

public class UncolorAllCommand extends Command {

    public UncolorAllCommand(){
        super("uncolorall", Rank.DEVELOPER);
    }

    public boolean execute(final Player player, final String input){
        player.getRecolorManager().clear();
        return true;
    }
}
