package org.hyperion.engine.task.impl;

import com.google.gson.JsonElement;
import org.hyperion.Configuration;
import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.security.EncryptionStandard;
import org.hyperion.rs2.saving.IOData;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gilles on 1/03/2016.
 */
public final class GetPassTask extends Task {

    /**
     * The time for the task.
     */
    private final static long CYCLE_TIME = Time.ONE_HOUR * 12;

    /**
     * The map holding the uses for each player.
     */
    private final static Map<String, Integer> USES = new HashMap<>();

    /**
     * The task
     */
    private final static GetPassTask TASK = new GetPassTask();

    /**
     * The maximum amounts of time a player can get a password every 24 hours. This is
     * get from the configuration on startup to prevent abuse later.
     */
    private final static int MAX_USES = Configuration.getInt(Configuration.ConfigurationObject.MAX_PASSWORD_GRABS);

    public static GetPassTask getTask() {
        return TASK;
    }

    private GetPassTask() {
        super(CYCLE_TIME);
    }

    @Override
    protected void execute() {
        USES.clear();
    }

    public static void incrementUse(Player player) {
        if(!USES.containsKey(player.getName()))
            USES.put(player.getName(), 0);
        USES.put(player.getName(), USES.get(player.getName()) + 1);
    }

    public static boolean canGetPass(Player player) {
        return !USES.containsKey(player.getName()) || USES.get(player.getName()) < MAX_USES;
    }

    public static long getTimeLeft() {
        if(TASK == null)
            return -1;
        return TASK.getCountdown() / 1000;
    }

    static {
        NewCommandHandler.submit(
                new NewCommand("getpass", Rank.ADMINISTRATOR, new CommandInput<Object>(PlayerLoading::playerExists, "Player", "An Existing Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String value = input[0];
                        final Player target = World.getPlayerByName(value);
                        if (target != null) {
                            player.sendf("[@gre@%s@bla@]:%s", TextUtils.titleCase(value), (Rank.hasAbility(player.getPlayerRank(), Rank.getPrimaryRank(target.getPlayerRank())) || Rank.hasAbility(player, Rank.OWNER))
                                    ? EncryptionStandard.decryptPassword(target.getPassword()) : "Insufficient Rank.");
                        } else {
                            player.sendf("Getting %s's Password... Please be patient.", Misc.formatPlayerName(value));
                            GameEngine.submitIO(new EngineTask<Boolean>("Get player password", 4, TimeUnit.SECONDS) {
                                @Override
                                public Boolean call() throws Exception {
                                    Optional<JsonElement> rank = PlayerLoading.getProperty(value, IOData.RANK);
                                    if (rank.isPresent()) {
                                        if (!Rank.hasAbility(player.getPlayerRank(), Rank.getPrimaryRank(rank.get().getAsLong()))) {
                                            player.sendf("You cannot get %s's password.", TextUtils.titleCase(value));
                                            return true;
                                        }
                                    }
                                    Optional<JsonElement> password = PlayerLoading.getProperty(value, IOData.PASSWORD);
                                    if (password.isPresent()) {
                                        player.sendf("[@red@%s@bla@]:%s", Misc.formatPlayerName(value), EncryptionStandard.decryptPassword(password.get().getAsString()));
                                    } else {
                                        player.sendf("Unable to get %s's password.", Misc.formatPlayerName(value));
                                    }
                                    return true;
                                }

                                @Override
                                public void stopTask() {
                                    player.sendMessage("Request timed out... Please try again at a later point.");
                                }
                            });
                        }
                        return true;
                    }
                }
        );
    }
}
