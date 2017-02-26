package org.hyperion.rs2.model.possiblehacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author DrHales
 */
public final class PossibleHacksHolder {

    private final File FOLDER = new File("./data/");
    private final File FILE = new File(FOLDER, "PossibleHacks.json");

    private Map<String, DataSet> map = new HashMap<>();

    private PossibleHacksHolder() {
        load();
    }

    public static PossibleHacksHolder getInstance() {
        return InstanceHolder.instance != null ? InstanceHolder.instance : (InstanceHolder.instance = new PossibleHacksHolder());
    }

    public Map<String, DataSet> getMap() {
        return map;
    }

    private void load() {
        final long initial = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            map = new Gson().fromJson(new JsonParser().parse(reader), new TypeToken<HashMap<String, DataSet>>() {
            }.getType());
            reader.close();
        } catch (IOException ex) {
            Server.getLogger().log(Level.WARNING, "Error Parsing PossibleHacks.json", ex);
        }
        Server.getLogger().info(String.format("%,d Possible Hacks submitted in %,dms", map.size(), System.currentTimeMillis() - initial));
    }

    public void write() {
        final long initial = System.currentTimeMillis();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(map));
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Server.getLogger().log(Level.WARNING, "Error Writing to PossibleHacks.json", ex);
        }
        Server.getLogger().info(String.format("%,d Possible Hacks written in %,dms", map.size(), System.currentTimeMillis() - initial));
    }

    public void reload(final boolean rewrite, final boolean remap) {
        if (rewrite) {
            write();
        }
        if (remap) {
            load();
        }
    }

    public void add(final Player player, final String value, final DataType type) {
        type.process(player, value);
    }

    public void check(final Player player, final String value, final DataType type) {
        final DataSet data = DataType.getDataSet(value);
        if (data != null) {
            TaskManager.submit(new Task(500L, String.format("%s %s Check Hacks Task", value, type.toString())) {
                @Override
                public void execute() {
                    stop();
                    type.checkData(player, data);
                }
            });
        } else {
            player.sendf("Player '@red@%s@bla@' has no Possible Hack Information.", value);
        }
    }

    private static class InstanceHolder {
        private static PossibleHacksHolder instance;
    }
}
