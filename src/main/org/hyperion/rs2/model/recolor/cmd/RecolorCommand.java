package org.hyperion.rs2.model.recolor.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.recolor.Recolor;

public class RecolorCommand extends Command{

    public RecolorCommand(){
        super("recolor", Rank.DEVELOPER);
    }

    public boolean execute(final Player player, final String input){
        final String line = filterInput(input).trim();
        try{
            final Recolor recolor = Recolor.parse(line);
            if(recolor.getItemDefinition() == null)
                throw new Exception();
            if(player.getRecolorManager().isAtLimit()){
                player.sendf("You are at your limit! (Limit: %d)", player.getRecolorManager().getLimit());
                player.sendf("In order to recolor more items, you must buy %,d more donator points!", player.getRecolorManager().getAmountForLimitIncrease());
                return false;
            }
            player.getRecolorManager().add(recolor);
            player.sendf("Added recolor for %s", recolor.toReadableString());
        }catch(Exception ex){
            player.sendf("Error parsing your recolor: " + line);
            player.sendf("Syntax: ::recolor item_id [fill/replace/checkered] colors...");
            player.sendf("Example: ::recolor 1333 fill dragonstone");
        }
        return true;
    }
}
