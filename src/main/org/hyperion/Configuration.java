package org.hyperion;

/**
 * Created by Gilles on 11/02/2016.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Configuration {

    public enum ConfigurationObject {
        NAME("TactilityPk", String.class),
        OWNER("Arre", String.class),
        PORT("43597", Integer.class),
        CLIENT_VERSION("15483", Integer.class),
        DEBUG("true", Boolean.class),
        VERSION("317", Integer.class),
        ENGINE_DELAY("200", Integer.class),
        LOCALHOST("false", Boolean.class),
        PLAYER_MULTIPLIER("1.2", Double.class),
        DONATION_DB_ENABLED("false", Boolean.class),
        GAME_DB_ENABLED("false", Boolean.class),
        PLAYER_DB_ENABLED("false", Boolean.class),
        DONATION_DB_URL("jdbc:mysql://localhost/ADDRESS", String.class),
        GAME_DB_URL("jdbc:mysql://localhost/ADDRESS", String.class),
        PLAYER_DB_URL("jdbc:mysql://localhost/ADDRESS", String.class),
        DONATION_DB_USER("root", String.class),
        GAME_DB_USER("root", String.class),
        PLAYER_DB_USER("root", String.class),
        DONATION_DB_PASSWORD("", String.class),
        GAME_DB_PASSWORD("", String.class),
        PLAYER_DB_PASSWORD("", String.class),
        CHARACTER_FILE_CLEANUP("false", Boolean.class),
        CHARACTER_FILE_CLEANUP_THREADS("1", Integer.class),
        MAX_PASSWORD_GRABS("25", Integer.class);

        public final static ConfigurationObject[] VALUES = values();
        private final String value;
        private final Class clazz;

        ConfigurationObject(String value, Class clazz) {
            this.value = value;
            this.clazz = clazz;
        }

        public String getValue() {
            return value;
        }

        public Class getClazz() {
            return clazz;
        }
    }

    private static final Logger logger = Logger.getLogger("Configuration");
    private static final File CONFIGURATION_FILE = new File("./config.json");
    private static final Map<ConfigurationObject, String> CONFIGURATIONS = loadMap();

    public static String getString(ConfigurationObject key) {
        assert CONFIGURATIONS != null && key.getClazz() == String.class;
        return CONFIGURATIONS.get(key);
    }

    public static boolean getBoolean(ConfigurationObject key) {
        assert CONFIGURATIONS != null && key.getClazz() == Boolean.class;
        return Boolean.parseBoolean(CONFIGURATIONS.get(key));
    }

    public static int getInt(ConfigurationObject key) {
        assert CONFIGURATIONS != null && key.getClazz() == Integer.class;
        return Integer.parseInt(CONFIGURATIONS.get(key));
    }

    public static double getDouble(ConfigurationObject key) {
        assert CONFIGURATIONS != null && key.getClazz() == Double.class;
        return Double.parseDouble(CONFIGURATIONS.get(key));
    }


    public static Map<ConfigurationObject, String> loadMap() {
        try (FileReader fileReader = new FileReader(CONFIGURATION_FILE)) {
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(fileReader);
            Map<ConfigurationObject, String> configuration = new Gson().fromJson(object, new TypeToken<HashMap<ConfigurationObject, String>>() {}.getType());
            boolean hasToSave = ConfigurationObject.VALUES.length != configuration.size();
            Arrays.stream(ConfigurationObject.VALUES).forEach(configurationObject -> {
                if (!configuration.containsKey(configurationObject)) {
                    configuration.put(configurationObject, configurationObject.getValue());
                }
            });
            if(hasToSave)
                saveConfiguration();
            return configuration;
        } catch (FileNotFoundException e) {
            logger.warning("Using default configuration file.");
            return saveConfiguration();
        } catch (Exception e) {
            logger.severe("Something went severely wrong while trying to load the configuration file.");
        }
        return null;
    }

    public static Map<ConfigurationObject, String> saveConfiguration() {
        if (!CONFIGURATION_FILE.getParentFile().exists()) {
            if (!CONFIGURATION_FILE.getParentFile().mkdirs()) {
                logger.warning("Unable to create directory for configuration file!");
                return null;
            }
        }

        //Treemap so it keeps the order in the file itself, making it cleaner to edit.
        Map<ConfigurationObject, String> defaultConfigFile = new TreeMap<>();
        if (CONFIGURATIONS != null)
            CONFIGURATIONS.forEach(defaultConfigFile::put);
        Arrays.stream(ConfigurationObject.VALUES).filter(value -> !defaultConfigFile.containsKey(value)).forEach(value -> defaultConfigFile.put(value, value.getValue()));

        try (FileWriter writer = new FileWriter(CONFIGURATION_FILE)) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            writer.write(builder.toJson(defaultConfigFile, new TypeToken<HashMap<ConfigurationObject, String>>() {}.getType()));
        } catch (Exception e) {
            logger.severe("Something went severely wrong while trying to save the default configuration file.");
        }
        return defaultConfigFile;
    }

    public static void reloadConfiguration() {
        Map<ConfigurationObject, String> newConfiguration = loadMap();
        assert CONFIGURATIONS != null && newConfiguration != null;
        newConfiguration.entrySet().forEach(entry -> CONFIGURATIONS.put(entry.getKey(), entry.getValue()));
    }
}
