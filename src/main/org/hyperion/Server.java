package org.hyperion;

import org.hyperion.engine.Update;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.net.security.CharFileEncryption;
import org.hyperion.rs2.net.security.EncryptionStandard;
import org.hyperion.util.ShutdownHook;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hyperion.Configuration.ConfigurationObject.NAME;
import static org.hyperion.Configuration.ConfigurationObject.PORT;

public class Server {

    private final static Logger logger = Logger.getLogger(Configuration.getString(NAME));
    private final static GameLoader loader = new GameLoader(Configuration.getInt(PORT));
    private final static Uptime uptime = new Uptime();
    private static CharFileEncryption charFileEncryption;
    private static boolean updating = false;

    private static final String checkString = "zTlqHOomTVi1KCCLCnBKwA==";

    public static Uptime getUptime() {
        return uptime;
    }

    public static CharFileEncryption getCharFileEncryption() {
        return charFileEncryption;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static GameLoader getLoader() {
        return loader;
    }

    public static boolean isUpdating() {
        return updating;
    }

    public static void setUpdating(boolean updating) {
        Server.updating = updating;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("./data/key.dat");
        if(file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                charFileEncryption = new CharFileEncryption((Key) in.readObject());
            }
            if(!file.delete()) {
                file.deleteOnExit();
            }
        } else {
            Console console = System.console();
            if (console == null) {
                logger.log(Level.WARNING, "Using default password.");
                charFileEncryption = new CharFileEncryption("Glis1234Glis1234");
            } else {
                boolean correctPass = false;
                while (!correctPass) {
                    char passwordArray[] = console.readPassword("Enter password: ");
                    charFileEncryption = new CharFileEncryption(String.valueOf(passwordArray));
                    if (EncryptionStandard.encrypt("randomstring", charFileEncryption.getKey()).equals(checkString)) {
                        correctPass = true;
                    } else {
                        System.out.println("Password incorrect.");
                    }
                }
            }
        }

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        try {
            logger.info("Started loading the server...");
            loader.init();
            loader.finish();
            logger.info(Configuration.getString(NAME) + " is now online on port " + Configuration.getInt(PORT) + ".");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not start " + Configuration.getString(NAME) + "!", ex);
            System.exit(1);
        }
    }

    public static void update(int time, final String reason) {
        setUpdating(true);
        TaskManager.submit(new Update(time, reason));
    }
}
