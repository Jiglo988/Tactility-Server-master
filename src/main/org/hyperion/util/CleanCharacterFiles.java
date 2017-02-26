package org.hyperion.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.hyperion.Configuration;
import org.hyperion.rs2.saving.IOData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.hyperion.Configuration.ConfigurationObject.*;

/**
 * Created by Gilles on 18/02/2016.
 */
public class CleanCharacterFiles implements Runnable {

    /**
     * The character files that still have to be processed.
     */
    private static Queue<File> characterFiles;

    /**
     * This can be turned off by a command just to make sure we can't break things.
     */
    private static boolean enabled = true;

    /**
     * The amount of character files that got cleaned.
     */
    private static int characterFilesCleaned = 0;

    /**
     * The logger class
     */
    private final static Logger logger = Logger.getLogger("CharacterFileCleaner");

    /**
     * The start-time of the cleaning.
     */
    private static long startTime = -1;

    /**
     * Whether the cleaned already reported to the logger or not.
     */
    private static boolean reported = false;

    /**
     * The place where the files will get moved to when they're not valid.
     */
    private final static File cleanedFileDirectory = new File("./data/deletedchars/" + LocalDate.now().toString() + "/");

    /**
     * This method can be called at any time and will go through all the character files. It will use the default set directory.
     */
    public static void startup() {
        if (!Configuration.getBoolean(CHARACTER_FILE_CLEANUP) && enabled)
            return;

        File[] characterFilesFolder = new File(IOData.getCharFilePath()).listFiles();
        if (characterFilesFolder == null) {
            System.out.println("Could not find any character files in the " + IOData.getCharFilePath() + " folder.");
            return;
        }

        characterFiles = new LinkedList<>(Arrays.asList(characterFilesFolder).stream().filter(file -> file.getName().endsWith(".json")).collect(Collectors.toList()));
        startTime = System.currentTimeMillis();

        /**
         * This will start the threads, with the pre-configured settings.
         */
        ExecutorService application = Executors.newFixedThreadPool(Configuration.getInt(CHARACTER_FILE_CLEANUP_THREADS));
        for (int i = 0; i < Configuration.getInt(CHARACTER_FILE_CLEANUP_THREADS); i++)
            application.submit(new CleanCharacterFiles(i + 1));
    }

    private final int threadId;

    private CleanCharacterFiles(int threadId) {
        this.threadId = threadId;
    }

    @Override
    public void run() {
        while (characterFiles != null && !characterFiles.isEmpty() && enabled && !reported) {
            if(characterFiles.size()%1000 == 0) {
                System.out.println("Characterfile cleaner has " + characterFiles.size() + " files left to clean.");
            }
            File characterFile = characterFiles.poll();
            if (characterFile == null)
                break;
            try (FileReader fileReader = new FileReader(characterFile)) {
                JsonParser fileParser = new JsonParser();
                Gson builder = new Gson();
                JsonObject reader = (JsonObject) fileParser.parse(fileReader);

                if (!reader.has(IOData.PREVIOUS_LOGIN.toString()))
                    continue;

                if (!LocalDateTime.ofInstant(Instant.ofEpochMilli(reader.get(IOData.PREVIOUS_LOGIN.toString()).getAsLong()), ZoneId.systemDefault()).toLocalDate().isAfter(LocalDate.now().minusDays(8)))
                    continue;

                if (reader.has(IOData.RANK.toString()))
                    continue;

                if (reader.has(IOData.TUTORIAL_PROGRESS.toString()) && reader.get(IOData.TUTORIAL_PROGRESS.toString()).getAsInt() >= 28)
                    continue;

                if (reader.has(IOData.ACCOUNT_VALUE.toString()) && reader.get(IOData.ACCOUNT_VALUE.toString()).getAsInt() > 10000)
                    continue;

                if (reader.has(IOData.DONATOR_POINTS.toString()) || reader.has(IOData.DONATOR_POINTS_BOUGHT.toString()))
                    continue;

                if (reader.has(IOData.LEVELS.toString()) && Arrays.stream(builder.fromJson(reader.get(IOData.LEVELS.toString()).getAsJsonArray(), int[].class)).count() >= 800)
                    continue;

                if (reader.has(IOData.KILL_COUNT.toString()) && reader.get(IOData.KILL_COUNT.toString()).getAsInt() >= 10)
                    continue;
            } catch (Exception e) {
                logger.log(Level.WARNING, "An error occurred while trying to read a character file on thread " + threadId + " in character file " + characterFile.getName() + ".", e);
            }
            try {
                FileUtils.moveFileToDirectory(characterFile, cleanedFileDirectory, true);
                characterFilesCleaned++;
            } catch(IOException e) {
                logger.log(Level.WARNING, "An error occurred while trying to move the file " + characterFile.getName() + " to the new directory.", e);
            }
        }

        if (Configuration.getBoolean(DEBUG))
            System.out.println("Character-file cleaning thread " + threadId + " finished.");
        if (!reported)

        {
            logger.info("Done cleaning character files. Cleaned " + characterFilesCleaned + " character files in " + (System.currentTimeMillis() - startTime) + "ms");
            reported = true;
        }
    }
}
