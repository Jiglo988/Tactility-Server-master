package org.hyperion.rs2.model.customtrivia.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.customtrivia.CustomTriviaManager;

public class ViewCustomTriviaCommand extends Command{

    public ViewCustomTriviaCommand(){
        super("viewtrivia", Rank.PLAYER);
    }

    public boolean execute(final Player player, final String input) throws Exception{
        CustomTriviaManager.send(player);
        return true;

    }
}
