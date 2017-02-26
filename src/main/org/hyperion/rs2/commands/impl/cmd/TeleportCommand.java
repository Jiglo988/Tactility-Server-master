package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.combat.Magic;

/**
 * Created by DrHales on 3/3/2016.
 */
public class TeleportCommand extends NewCommand {

    private final Position location;
    private final long time;
    private final boolean force;

    public TeleportCommand(String key, Rank rank, Position location, boolean force) {
        super(key, rank);
        this.location = location;
        this.time = 0;
        this.force = force;
    }

    public TeleportCommand(String key, Rank rank, long time, Position location, boolean force) {
        super(key, rank, time);
        this.location = location;
        this.time = time;
        this.force = force;
    }

    public boolean execute(final Player player, final String[] input) {
        Magic.teleport(player, location, force, false);
        return true;
    }

}
