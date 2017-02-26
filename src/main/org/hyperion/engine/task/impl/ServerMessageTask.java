package org.hyperion.engine.task.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Time;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class ServerMessageTask extends Task {

    private final static long CYCLE_TIME = Time.ONE_MINUTE * 2;
    private final static String FILE_LOCATION = "./data/json/server_messages.json";
    private final static String DEFAULT_MESSAGE = "Enjoy your time on the server!";

    private static boolean enabled = true;
    private static List<String> messages = reloadMessages();
    private static int currentMessage = 0;

    public ServerMessageTask() {
        super(CYCLE_TIME);
    }

    @Override
    public void execute() {
        if(messages == null || messages.isEmpty() || !enabled) {
            stop();
            return;
        }
        if(currentMessage >= messages.size())
            currentMessage = 0;
        String message = messages.get(currentMessage++);
        World.getPlayers().stream().filter(p -> p != null).forEach(p -> p.sendServerMessage(message));
    }

    public static List<String> reloadMessages() {
        File file = new File(FILE_LOCATION);
        try(FileReader fileReader = new FileReader(file)) {
            JsonParser parser = new JsonParser();
            JsonArray object = (JsonArray)parser.parse(fileReader);
            return new Gson().fromJson(object, new TypeToken<LinkedList<String>>() {}.getType());
        } catch(FileNotFoundException e) {
            Server.getLogger().log(Level.WARNING, "Saving default server messages. Do not forget to change!");
            try (FileWriter writer = new FileWriter(file)) {
                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                List<String> messageList = new LinkedList<>();
                messageList.add(DEFAULT_MESSAGE);
                writer.write(builder.toJson(messageList, new TypeToken<LinkedList<String>>() {}.getType()));
                return messageList;
            } catch(IOException ex) {
                Server.getLogger().log(Level.WARNING, "Issue occurred while saving server messages.", ex);
            }
        } catch(Exception e) {
            Server.getLogger().log(Level.WARNING, "Issue occurred while loading server messages.", e);
        }
        return new LinkedList<>();
    }

    public static void addMessage(String... message) {
        Arrays.stream(message).forEach(messages::add);
    }

    public static boolean removeMessage(String message) {
        return messages.remove(message);
    }

    static {
        NewCommandHandler.submit(
                new NewCommand("reloadservermessages", Rank.ADMINISTRATOR) {
                    @Override
                    public boolean execute(Player player, String[] input) {
                        reloadMessages();
                        player.sendMessage("Successfully reloaded server messages.");
                        return true;
                    }
                },
                new NewCommand("toggleservermessages", Rank.ADMINISTRATOR) {
                    @Override
                    public boolean execute(Player player, String[] input) {
                        enabled = !enabled;
                        player.sendMessage(String.format("Server messages are now %s.", enabled ? "enabled" : "disabled"));
                        return true;
                    }
                },
                new NewCommand("addservermessage", Rank.ADMINISTRATOR, new CommandInput<>(object -> true, "message", "The message to add to the server message list.")) {
                    @Override
                    public boolean execute(Player player, String[] input) {
                        addMessage(filterInput(input[0]));
                        player.sendMessage("Successfully added message '" + filterInput(input[0]) + "'.");
                        return true;
                    }
                },
                new NewCommand("removeservermessage", Rank.ADMINISTRATOR, new CommandInput<>(object -> true, "message", "The message to remove from the server message list.")) {
                    @Override
                    public boolean execute(Player player, String[] input) {
                        if (removeMessage(filterInput(input[0]))) {
                            player.sendMessage("Successfully removed message '" + filterInput(input[0]) + "'.");
                            return true;
                        }
                        player.sendMessage("Could not remove message '" + filterInput(input[0]) + "'.");
                        return true;
                    }
                },
                new NewCommand("listservermessages", Rank.ADMINISTRATOR) {
                    @Override
                    public boolean execute(Player player, String[] input) {
                        messages.stream().forEach(player::sendMessage);
                        return true;
                    }
                }
        );
    }
}
