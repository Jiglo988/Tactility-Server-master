package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.Server;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by DrHales on 3/3/2016.
 */
public class WikiCommand extends NewCommand {

    public static final Map<String, String> KEY_TO_URL = new HashMap<>();

    static {
        File file = new File("./data/wikilinks.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Server.getLogger().log(Level.WARNING, "Error Creating ./data/wikilinks.txt", ex);
            }
        }
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            for (String line = ""; (line = reader.readLine()) != null; ) {
                if (line.isEmpty()) break;
                String[] split = line.split("-");
                KEY_TO_URL.put(split[0], split[1]);
            }
        } catch (Exception e) {
            Server.getLogger().log(Level.WARNING, "Error Reading ./data/wikilinks.txt", e);
        }
    }

    public WikiCommand() {
        super("wikishortcut", Rank.MODERATOR, new CommandInput<String>(string -> string != null, "String", "New Wiki Shortcut"), new CommandInput<String>(string -> string != null, "String", "New Wiki Link"));
    }

    public boolean execute(final Player player, final String[] input) {

        final String shortcut = input[0].trim();
        final String link = input[1].trim();
        WikiCommand.KEY_TO_URL.put(shortcut, link);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter("./data/wikilinks.txt", true))) {
            writer.write(String.format("%s-%s", shortcut, link));
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Server.getLogger().log(Level.WARNING, "", ex);
        }
        return true;
    }

}
