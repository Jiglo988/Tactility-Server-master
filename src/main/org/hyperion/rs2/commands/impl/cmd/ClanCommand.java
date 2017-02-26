package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.content.clan.ClanManager;

/**
 * @author DrHales
 *         4/26/2016
 */
public class ClanCommand extends NewCommand {

    public ClanCommand(String key) {
        super(key, Rank.PLAYER, 250L, new CommandInput<>(object -> true, "String", "Clan Message"));
    }

    @Override
    public boolean execute(final Player player, final String[] input) {
        if (!player.isMuted) {
            ClanManager.sendClanMessage(player, input[0].trim(), true);
        }
        return true;
    }

}
