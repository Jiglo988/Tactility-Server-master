package org.hyperion.rs2.commands;

import org.hyperion.Server;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.commands.util.CommandResult;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.jsoup.helper.StringUtil;

import java.util.logging.Level;
import java.util.regex.Pattern;

import static org.hyperion.rs2.commands.util.CommandResult.*;

/**
 * A command is made so only the execute part can be overwritten, the rest should be done in the constructor.
 *
 * Created by Gilles on 10/02/2016.
 */
public abstract class NewCommand {
    private final String key;
    private final Rank rank;
    private final long delay;
    private final CommandInput[] requiredInput;

    public NewCommand(String key, Rank rank, long delay, CommandInput... requiredInput) {
        this.key = key;
        this.rank = rank;
        this.requiredInput = requiredInput;
        this.delay = delay;
    }

    public NewCommand(String key, long delay, CommandInput... requiredInput) {
        this(key, null, delay, requiredInput);
    }

    public NewCommand(String key, CommandInput... requiredInput) {
        this(key, null, 0, requiredInput);
    }

    public NewCommand(String key, Rank rank, CommandInput... requiredInput) {
        this(key, rank, 0, requiredInput);
    }

    public final String getKey() {
        return key;
    }

    public final Rank getRank() {
        return rank == null ? Rank.PLAYER : rank;
    }

    public final CommandInput[] getRequiredInput() {
        return requiredInput == null ? new CommandInput[]{} : requiredInput;
    }

    public final long getDelay() {
        return delay;
    }

    public final boolean hasDelay() {
        return getDelay() != 0;
    }

    protected abstract boolean execute(Player player, String[] input);

    public final CommandResult doCommand(Player player, String[] input) {
        //First we'll check the rank requirement.
        if (!Rank.hasAbility(player, getRank())) {
            player.sendMessage("You do not have the required rank for this command.");
            return GOT_ERROR_MESSAGE;
        }
        //After this we see if each argument is valid.
        try {
            for (int i = 0; i < input.length; i++) {
                //Over here we need to test what the argument is.
                //It gives warnings, but it's nothing unwanted, we're careful with the exception and simply can't test any more.
                if (StringUtil.isNumeric(input[i]) || Pattern.matches("-?([0-9]){1,10}", input[i])) {
                    if (Pattern.matches("-?([0-9]*)\\.([0-9]*)", input[i]) && !getRequiredInput()[i].testInput(player, Double.parseDouble(input[i]))) {
                        return GOT_ERROR_MESSAGE;
                    } else if (!getRequiredInput()[i].testInput(player, Integer.parseInt(input[i]))) {
                        return GOT_ERROR_MESSAGE;
                    }
                } else if (Pattern.matches("true|false", input[i]) && !getRequiredInput()[i].testInput(player, Boolean.parseBoolean(input[i]))) {
                    return GOT_ERROR_MESSAGE;
                } else if (!getRequiredInput()[i].testInput(player, input[i])) {
                    return GOT_ERROR_MESSAGE;
                }
            }
        } catch (Exception e) {
            Server.getLogger().log(Level.WARNING, String.format("Error Testing Command:%s", key), e);
            return NEED_ERROR_MESSAGE;
        }
        //If we get here it means that it did pass the tests.
        if (execute(player, input))
            return SUCCESSFUL;
        return NEED_ERROR_MESSAGE;
    }

    public final String getModelInput() {
        String modelInput = "::" + getKey() + " ";
        for (CommandInput inputString : requiredInput)
            modelInput += inputString.getShortDescription() + NewCommandHandler.SPLITTER + " ";
        return  modelInput.trim().endsWith(",") ? modelInput.trim().substring(0, modelInput.length() - NewCommandHandler.SPLITTER.length() - 1) : modelInput; //this will remove the unnecessary comma without us having to do a lot of extra work.
    }

    public final String filterInput(String input) {
        return input.replaceFirst(String.format("%s ", key), "");
    }
}
