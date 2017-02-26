package org.hyperion.rs2.model.challenge.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.challenge.ChallengeManager;

public class ViewChallengesCommand extends Command {

    public ViewChallengesCommand(){
        super("viewchallenges", Rank.PLAYER);
    }

    public boolean execute(final Player player, final String input){
        ChallengeManager.send(player, false);
        return true;
    }
}
