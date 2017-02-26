package org.hyperion.rs2.commands.impl.cmd;

import com.google.gson.JsonElement;
import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.saving.IOData;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.util.Misc;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gilles on 16/03/2016.
 */
public class GetFromCharFileCommand extends NewCommand {

    private final IOData ioData;

    public GetFromCharFileCommand(String key, Rank rank, long delay, IOData ioData) {
        super(key, rank, delay, new CommandInput<Object>(PlayerLoading::playerExists, "player", "A player that exists in the system."));
        this.ioData = ioData;
    }

    public GetFromCharFileCommand(String key, long delay, IOData ioData) {
        this(key, Rank.ADMINISTRATOR, delay, ioData);
    }

    public GetFromCharFileCommand(String key, IOData ioData) {
        this(key, Rank.ADMINISTRATOR, ioData);
    }

    public GetFromCharFileCommand(String key, Rank rank, IOData ioData) {
        this(key, rank, 0, ioData);
    }

    @Override
    public boolean execute(Player player, String[] input) {
        String targetName = input[0];
        player.sendMessage("Getting " + Misc.formatPlayerName(targetName) + "'s " + ioData.toString().replace("_", " ").toLowerCase() + "... Please be patient.");
        GameEngine.submitIO(new EngineTask<Boolean>("Get player " + ioData, 4, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                Optional<JsonElement> playerData = PlayerLoading.getProperty(input[0], ioData);
                if(player == null)
                    return false;
                if (playerData.isPresent())
                    player.sendMessage("Player " + Misc.formatPlayerName(input[0]) + "'s " + ioData.toString().replace("_", " ").toLowerCase() +" is '" + playerData.get().getAsString() + "'.");
                else
                    player.sendMessage("Player " + Misc.formatPlayerName(input[0]) + " has no recorded " + ioData.toString().replace("_", " ").toLowerCase() + ".");
                return true;
            }

            @Override
            public void stopTask() {
                player.sendMessage("Request timed out... Please try again at a later point.");
            }
        });
        return true;
    }
}
