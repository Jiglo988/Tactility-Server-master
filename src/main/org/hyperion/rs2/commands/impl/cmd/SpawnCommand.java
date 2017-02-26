package org.hyperion.rs2.commands.impl.cmd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.util.Time;
import org.jsoup.helper.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DrHales on 3/3/2016.
 */
public class SpawnCommand extends NewCommand {

    private static final Map<String, Integer> keywords = loadKeywords();

    public SpawnCommand(String key) {
        super(key, Rank.PLAYER, 250L, new CommandInput<Object>(object -> object != null && (keywords.get(String.valueOf(object)) != null || (StringUtil.isNumeric(String.valueOf(object)) && ItemSpawning.canSpawn(Integer.parseInt(String.valueOf(object))))), "Item ID or Keyword", "A valid Item ID or Keyword"), new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Amount", String.format("An Amount between 0 & %d", Integer.MAX_VALUE)));
    }

    public static void setKeyword(String keyword, int id) {
        keywords.put(keyword, id);
        saveKeywords();
    }

    private static Map<String, Integer> loadKeywords() {
        File file = new File("./data/json/keywords.json");
        try (FileReader fileReader = new FileReader(file)) {
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(fileReader);
            return new Gson().fromJson(object, new TypeToken<Map<String, Integer>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            return saveKeywords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static Map<String, Integer> saveKeywords() {
        Map<String, Integer> mapToSave = keywords == null ? new HashMap<>() : keywords;

        File fileToWrite = new File("./data/json/keywords.json");

        if (!fileToWrite.getParentFile().exists()) {
            try {
                if (!fileToWrite.getParentFile().mkdirs())
                    return mapToSave;
            } catch (SecurityException e) {
                System.out.println("Unable to create directory for keywords saving.");
            }
        }
        try (FileWriter writer = new FileWriter(fileToWrite)) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            writer.write(builder.toJson(mapToSave, new TypeToken<Map<String, Integer>>() {
            }.getType()));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapToSave;
    }

    public static Integer getId(String keyword) {
        return keywords.get(keyword);
    }

    public boolean execute(final Player player, final String[] input) {
        if (player.getTrader() != null) {
            player.sendMessage("You cannot spawn items while inside a trade.");
            return true;
        }
        final String value = input[0].trim();
        final int amount = Integer.parseInt(input[1].trim());
        if (StringUtil.isNumeric(value)) {
            ItemSpawning.spawnItem(player, Integer.parseInt(value), amount);
            if (keywords.containsValue(value)) {
                final String possible = keywords.entrySet().stream().filter(string -> string.getValue() == Integer.parseInt(value)).map(Map.Entry::getKey).findAny().orElse(null);
                if (possible != null) {
                    player.sendf("You could also have use the command ::item %s,%d", possible, amount);
                }
            }
        } else {
            ItemSpawning.spawnItem(player, keywords.get(value), amount);
        }
        return true;
    }

}
