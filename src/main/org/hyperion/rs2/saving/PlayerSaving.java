package org.hyperion.rs2.saving;

import com.google.gson.*;
import org.hyperion.rs2.model.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gilles on 4/02/2016.
 */
public class PlayerSaving {
    private final static Set<String> CURRENT_SAVING_PLAYERS = new HashSet<>();

    public static boolean save(Player player) {
        Path path = Paths.get(IOData.getCharFilePath(), player.getName().toLowerCase() + ".json");
        File file = path.toFile();

        if (!file.getParentFile().exists()) {
            try {
                if(!file.getParentFile().mkdirs())
                    return false;
            } catch (SecurityException e) {
                System.out.println("Unable to create directory for player data!");
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();
            Arrays.stream(IOData.VALUES).filter(ioData -> ioData.shouldSave(player)).forEach(ioData -> {
                JsonElement toSave = ioData.saveValue(player, builder);
                if(toSave == null)
                    return;
                if(toSave.isJsonPrimitive()) {
                    JsonPrimitive toSavePrimitive = (JsonPrimitive)toSave;
                    if(toSavePrimitive.isBoolean())
                        object.addProperty(ioData.toString(), toSave.getAsBoolean());
                    if(toSavePrimitive.isString())
                        object.addProperty(ioData.toString(), toSave.getAsString());
                    if(toSavePrimitive.isNumber())
                        object.addProperty(ioData.toString(), toSave.getAsNumber());
                }
                if(toSave.isJsonArray())
                    object.add(ioData.toString(), toSave.getAsJsonArray());
                if(toSave.isJsonObject())
                    object.add(ioData.toString(), toSave.getAsJsonObject());
            });
            writer.write(builder.toJson(object));
        } catch (Exception e) {
            e.printStackTrace();
            CURRENT_SAVING_PLAYERS.remove(player.getName());
            return false;
        }
        CURRENT_SAVING_PLAYERS.remove(player.getName());
        return true;
    }

    public static void setSaving(Player player) {
        setSaving(player.getName());
    }

    public static void setSaving(String playerName) {
        CURRENT_SAVING_PLAYERS.add(playerName);
    }

    public static boolean isSaving(Player player) {
        return isSaving(player.getName());
    }

    public static boolean isSaving(String playerName) {
        return CURRENT_SAVING_PLAYERS.contains(playerName);
    }

    public static boolean replaceProperty(String playerName, String property, Object value) {
        if(playerName == null || property == null || value == null || playerName.trim().isEmpty() || property.trim().isEmpty() || !PlayerLoading.playerExists(playerName))
            return false;

        //At this point we know everything is valid. Now it's time to read the file content
        File file = new File("./data/characters/mergedchars/" + playerName + ".txt");

        String fileContent = "";
        try(BufferedReader in = new BufferedReader(new FileReader(file), 1024)) {
            String line;
            while((line = in.readLine()) != null) {
                if (line.length() <= 1 || !line.contains("=")) {
                    fileContent += line + System.lineSeparator();
                    continue;
                }
                String[] parts = line.split("=");
                String name = parts[0].trim();
                if(name.equals(property)) {
                    line = property + "=" + value;
                }
                fileContent += line + System.lineSeparator();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(fileContent);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
