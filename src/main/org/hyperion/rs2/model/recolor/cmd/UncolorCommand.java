package org.hyperion.rs2.model.recolor.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.recolor.Recolor;

public class UncolorCommand extends Command{

    public UncolorCommand(){
        super("uncolor", Rank.DEVELOPER);
    }

    public boolean execute(final Player player, final String input){
        final String line = filterInput(input).trim();
        try{
            final int id = Integer.parseInt(line);
            if(!player.getRecolorManager().contains(id)){
                player.sendf("No recolors found for item id: " + id);
                return false;
            }
            for(final Recolor recolor : player.getRecolorManager().remove(id))
                player.sendf("Removed recolor for " + recolor.toReadableString());
            return true;
        }catch(Exception ex){
            player.sendf("Incorrect syntax. Usage: ::uncolor item_id");
            return false;
        }
    }
}
