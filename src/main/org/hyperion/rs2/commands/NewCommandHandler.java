package org.hyperion.rs2.commands;

import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.commands.newimpl.*;
import org.hyperion.rs2.commands.util.CommandResult;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Gilles on 10/02/2016.
 */
public final class NewCommandHandler {

    /**
     * The splitter used.
     */
    public final static String SPLITTER = ",";

    /**
     * Simple static integer to keep the maximum available parts for the command.
     */
    private final static int MAXIMUM_PARTS = 5;

    /**
     * This map just keeps all the commands that got submitted in it.
     */
    private final static HashMap<String, List<NewCommand>> COMMANDS = new HashMap<>();

    public static HashMap<String, List<NewCommand>> getCommands() {
        return COMMANDS;
    }

    private static final HashMap<Rank, List<String>> COMMANDS_LIST = new HashMap<>();

    public static HashMap<Rank, List<String>> getCommandsList() {
        return COMMANDS_LIST;
    }

    private static final List<String> DISABLED = new ArrayList<>();

    public static List<String> getDisabled() {
        return DISABLED;
    }

    /**
     * This map keeps what command which player used. The CommandUsage keeps the time of it's creation,
     * therefor we can keep people from using commands multiple times in a certain time frame.
     * This map gets cleaned by an event that is submitted whenever a command is used that has a delay to it.
     */
    private final static HashMap<String, List<String>> COMMANDS_USED = new HashMap<>();

    /**
     * Initializes the commands for specific groups. The groups are all defined in the
     * COMMAND_TYPES array. Add a group to the COMMAND_TYPES array to make them initialize.
     * Command groups must implement {@link NewCommandExtension}.
     */
    static {
        final long initial = System.currentTimeMillis();
        Arrays.asList(new ServerCommands(),
                new PlayerCommands(),
                new HeroCommands(),
                new LegendCommands(),
                new VeteranCommands(),
                new DonatorCommands(),
                new SuperDonatorCommands(),
                new WikiEditorCommands(),
                new EventManagerCommands(),
                new HelperCommands(),
                new ForumModeratorCommands(),
                new ModeratorCommands(),
                new GlobalModeratorCommands(),
                new CommunityManagerCommands(),
                new HeadModeratorCommands(),
                new AdministratorCommands(),
                new DeveloperCommands(),
                new OwnerCommands()).stream().map(NewCommandExtension::init).forEach(NewCommandHandler::submit);
        Server.getLogger().info(String.format("%,d commands submitted in %,dms", COMMANDS.size(), System.currentTimeMillis() - initial));
    }

    /**
     * This class adds the submitted command to the map. It does this by submitting them one by one to a help method.
     *
     * @param commands The commands that need submitting.
     */
    public static void submit(NewCommand... commands) {
        Arrays.stream(commands).forEach(NewCommandHandler::submit);
    }

    /**
     * This class adds the submitted command to the map. It does this by submitting them one by one to a help method.
     *
     * @param commands The commands that need submitting.
     */
    public static void submit(Collection<NewCommand> commands) {
        commands.forEach(NewCommandHandler::submit);
    }

    /**
     * Adds a single command to the commands map.
     *
     * @param command The command to add to the map.
     */
    private static void submit(NewCommand command) {
        if (!COMMANDS.containsKey(command.getKey())) {
            COMMANDS.put(command.getKey(), new ArrayList<>());
        } else {
            System.out.println(String.format("\n[Already Submitted]:%s,%s", command.getKey(), command.getRank()));
            COMMANDS.get(command.getKey()).stream().forEach(value -> System.out.println(String.format("\t[Command]:%s\n", value.getKey(), value.getRank())));
        }
        COMMANDS.get(command.getKey()).add(command);
        if (!COMMANDS_LIST.containsKey(command.getRank()))
            COMMANDS_LIST.put(command.getRank(), new ArrayList<>());
        if (!COMMANDS_LIST.get(command.getRank()).contains(command))
            COMMANDS_LIST.get(command.getRank()).add(command.getKey());
    }

    private static void commandUsed(String playerName, String commandUsed, long delay) {
        if (!COMMANDS_USED.containsKey(playerName))
            COMMANDS_USED.put(playerName, new ArrayList<>());
        COMMANDS_USED.get(playerName).add(commandUsed);

        World.submit(new Task(delay) {
            @Override
            public void execute() {
                if (COMMANDS_USED.containsKey(playerName))
                    COMMANDS_USED.get(playerName).remove(commandUsed);
                stop();
            }
        });
    }

    /**
     * This method processes the command if possible.
     *
     * @param key    The command key.
     * @param player The player using the command.
     * @param input  The extra input for the command.
     * @return Whether the command was found or not. If not it'll continuing searching.
     */
    public static boolean processCommand(Player player, String key, String input) {
        //First we check if the map actually contains this command
        if (!COMMANDS.containsKey(key)) {
            player.sendf("Command '@red@%s@bla@' was not found.", key);
            return false;
        }
        if (DISABLED.contains(key)) {
            player.sendf("Command '@red@%s@bla@' is currently disabled.", key);
            return false;
        }
        if (!Rank.hasAbility(player, Rank.DEVELOPER) && COMMANDS_USED.containsKey(player.getName()) && COMMANDS_USED.get(player.getName()).stream().filter(commandUsage -> commandUsage.equalsIgnoreCase(key)).count() > 0)
            return false;
        //After we split the input, if the command is just the key we skip a part.
        List<NewCommand> fittingCommands;
        String[] parts = {};
        if (!key.equals(input)) {
            parts = input.split(SPLITTER);
            //We allow a maximum of 5 parts to prevent abuse.
            if (parts.length > MAXIMUM_PARTS)
                return false;
            int requiredLength = parts.length;
            fittingCommands = COMMANDS.get(key).stream().filter(command -> command.getRequiredInput().length == requiredLength).collect(Collectors.toList());
        } else {
            fittingCommands = COMMANDS.get(key).stream().filter(command -> command.getRequiredInput().length == 0).collect(Collectors.toList());
        }
        //Then we do a simple filter to only get the commands with the same amount of arguments.
        //If there are no commands left then we tell the player all the possible commands with that key.
        if (fittingCommands.isEmpty()) {
            player.sendMessage("The possible combinations for this command are: ");
            COMMANDS.get(key).forEach(command -> player.sendMessage(command.getModelInput()));
            return true;
        }
        //We try for each command that fits the amount of inputs if the command works or returns individual input (meaning it fit but didn't .
        for (NewCommand command : fittingCommands) {
            if (command == null)
                continue;
            CommandResult commandResult = command.doCommand(player, parts);
            if (commandResult == CommandResult.GOT_ERROR_MESSAGE)
                return true;
            if (commandResult == CommandResult.SUCCESSFUL) {
                if (command.hasDelay())
                    commandUsed(player.getName(), key, command.getDelay());
                return true;
            }
        }
        //If after trying neither of the commands worked (and they didn't give their individual message) we give them the possible inputs, but this time filtered.
        player.sendf("The possible combinations for this command with %,d arguments are: ", parts.length);
        fittingCommands.forEach(command -> player.sendMessage(command.getModelInput()));
        return true;
    }
}
