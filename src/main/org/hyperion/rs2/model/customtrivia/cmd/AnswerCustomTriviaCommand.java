package org.hyperion.rs2.model.customtrivia.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.customtrivia.CustomTriviaManager;

public class AnswerCustomTriviaCommand extends Command{

    public AnswerCustomTriviaCommand(){
        super("answertrivia", Rank.PLAYER);
    }

    public boolean execute(final Player player, final String input) throws Exception{
        final String answer = filterInput(input).trim();
        if(answer.isEmpty())
            return false;
        CustomTriviaManager.processAnswer(player, answer);
        return true;
    }
}
