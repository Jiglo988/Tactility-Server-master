package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.util.TextUtils;

/**
 * Created by DrHales on 3/8/2016.
 */
public class PromoteCommand extends NewCommand {

    private final Rank rank;

    public PromoteCommand(String key, Rank required, Rank rank) {
        super(key, required, new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player to Promote"));
        this.rank = rank;
    }

    public boolean execute(final Player player, final String[] input) {
        final Player target = World.getPlayerByName(input[0].trim());
        if (target.getPlayerRank() > rank.ordinal()) {
            long difference = target.getPlayerRank() - rank.ordinal();
            player.sendf("This player has higher rank with a difference of %d; Demote them first.", difference);
            return true;
        }
        target.setPlayerRank(Rank.addAbility(target, rank));
        player.sendf("Player '%s' has been promoted to '%s'.", TextUtils.optimizeText(target.getName()));
        target.sendf("Your rank has been set to '%s' by '%s'.", TextUtils.optimizeText(player.getName()), rank);
        return true;
    }

}
