package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.possiblehacks.DataType;
import org.hyperion.rs2.model.possiblehacks.PossibleHacksHolder;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.util.TextUtils;

/**
 * Created by Daniel on 6/14/2016.
 */
public class CheckPossibleHacksCommand extends NewCommand {

    private final DataType TYPE;

    public CheckPossibleHacksCommand(String key, DataType type) {
        super(key, Rank.ADMINISTRATOR, new CommandInput<>(PlayerLoading::playerExists, "Player", "An Existing Player in the System"));
        this.TYPE = type;
    }

    public boolean execute(Player player, String[] input) {
        final String value = input[0].trim().toLowerCase();
        player.sendf("Checking %s's '%s' Data Please Wait...", TextUtils.titleCase(value), String.valueOf(TYPE));
        PossibleHacksHolder.getInstance().check(player, value, TYPE);
        return true;
    }

}
