package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.Rank;

/**
 * Created by DrHales on 3/10/2016.
 */
public class CreatePositionCommand extends NewCommand {

    private final Position position;

    public CreatePositionCommand(String key, Rank rank, Position position) {
        super(key, rank);
        this.position = position;
    }

    public boolean execute(final Player player, final String[] input) {
        player.setTeleportTarget(position);
        return true;
    }

}
