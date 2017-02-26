package org.hyperion.rs2.saving;

import com.google.gson.*;
import org.hyperion.Server;
import org.hyperion.rs2.model.Player;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Created by Gilles on 4/02/2016.
 */
public class PlayerLoading {

    /**
     * An enum to define the 3 types of loading.
     */
    public enum LoadingType {
        /**
         * Only load the priority objects into the player.
         */
        PRIORITY_ONLY {
            @Override
            protected boolean load(Player player) {
                return loadPlayer(player, IOData.getPriorityLoading());
            }
        },
        /**
         * Only load the non-priority objects into the player.
         * To be used after PRIORITY_ONLY
         */
        NON_PRIORITY_ONLY {
            @Override
            protected boolean load(Player player) {
                return loadPlayer(player, IOData.getNonPriorityLoading());
            }
        },
        /**
         * Will call both types.
         */
        BOTH {
            @Override
            protected boolean load(Player player) {
                return PRIORITY_ONLY.load(player) && NON_PRIORITY_ONLY.load(player);
            }
        };

        protected abstract boolean load(Player player);
    }

    /**
     *
     * @param player The player to load
     * @param loadingType Which type of loading to use.
     * @return Whether the player exists and is loaded or not
     */
    public static boolean loadPlayer(Player player, LoadingType loadingType) {
        return loadingType.load(player);
    }

    /**
     *
     * @param player The player to load
     * @param ioDataMap The map of data that this method can load off.
     * @return Whether the player exists and is loaded or not
     */
    private static boolean loadPlayer(Player player, Map<String, IOData> ioDataMap) {
        Path path = Paths.get(IOData.getCharFilePath(), String.format("%s.json", player.getName().toLowerCase()));
        File file = path.toFile();

        if (!file.exists()) {
            return false;
        }

        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder().create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);

            reader.entrySet().forEach(jsonEntry -> {
                IOData ioData = ioDataMap.get(jsonEntry.getKey());
                if(ioData == null)
                    return;
                try {
                    ioData.loadValue(player, jsonEntry.getValue(), builder);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean playerExists(final Object value) {
        return (new File(IOData.getCharFilePath(), String.format("%s.json", String.valueOf(value))).exists());
    }

    public static Optional<JsonElement> getProperty(String playerName, IOData property) {
        if(playerName == null || property == null || playerName.trim().isEmpty() ||!playerExists(playerName))
            return Optional.empty();

        File file = new File(IOData.getCharFilePath(), String.format("%s.json", playerName.toLowerCase()));

        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            JsonObject reader = (JsonObject)fileParser.parse(fileReader);
            if(reader.has(property.toString()))
                return Optional.of(reader.get(property.toString()));
        } catch(Exception e) {
            Server.getLogger().log(Level.WARNING, String.format("Something went wrong getting the property '%s' from player '%s'.", property, playerName));
        }
        return Optional.empty();
    }
}