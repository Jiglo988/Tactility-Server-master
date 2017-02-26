package org.hyperion.rs2.model.log;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LogManager {

    private static final File DIR = new File(".", "playerlogs");
    private static final File STAFF_DIR = new File(".", "stafflogs");

    static{
        if(!DIR.exists())
            DIR.mkdir();
        if(!STAFF_DIR.exists())
            STAFF_DIR.mkdir();
    }

    private final File dir;

    private final Map<LogEntry.Category, Set<LogEntry>> logs;
    private final Map<LogEntry.Category, Boolean> loaded;

    private boolean enabled = true;

    public LogManager(final String name){
        this(new File(STAFF_DIR, name.toLowerCase()).exists() ? STAFF_DIR : DIR, name);
    }

    public LogManager(final Player player){
        this(Rank.hasAbility(player, Rank.MODERATOR) ? STAFF_DIR : DIR, player.getName());

        if(Rank.hasAbility(player, Rank.DEVELOPER))
            enabled = false;
    }

    public LogManager(final File dir, final String name){
        this.dir = new File(dir, name.toLowerCase());

        logs = new HashMap<>();
        loaded = new HashMap<>();
    }

    public void clear(){
        logs.clear();
    }

    public void add(final LogEntry log){
        if(!enabled)
            return;
        if(!logs.containsKey(log.category))
            logs.put(log.category, new TreeSet<>());
        logs.get(log.category).add(log);
    }

    public Set<LogEntry> getLogs(final LogEntry.Category category, final long startTime){
        if(!enabled)
            return null;
        if(!loaded.getOrDefault(category, false))
            load(category);
        final Set<LogEntry> logs = this.logs.get(category);
        if(logs == null)
            return null;
        return startTime == -1 ? logs : logs.stream().filter(
                l -> l.date.getTime() >= startTime
        ).collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<LogEntry> getLogs(final LogEntry.Category category){
        return getLogs(category, -1);
    }

    private void load(final LogEntry.Category category){
        final File file = new File(dir, category.path);
        if(!file.exists()){
            loaded.put(category, true);
            return;
        }
        try(final Scanner input = new Scanner(file, "UTF-8")){
            while(input.hasNextLine()){
                final String line = input.nextLine().trim();
                if(line.isEmpty())
                    continue;
                try{
                    final LogEntry log = LogEntry.parse(category, line);
                    add(log);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            loaded.put(category, true);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void clearExpiredLogs(){
        final long expired = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30);
        for(final Set<LogEntry> logs : this.logs.values()){
            final Iterator<LogEntry> itr = logs.iterator();
            while(itr.hasNext()){
                final LogEntry log = itr.next();
                if(log.date.getTime() < expired)
                    itr.remove();
                else
                    break;
            }
        }
    }

    public void save(){
        if(!dir.exists())
            dir.mkdir();
        for(final Map.Entry<LogEntry.Category, Set<LogEntry>> entry : logs.entrySet()){
            if(!entry.getKey().save && entry.getKey() != LogEntry.Category.PRIVATE_CHAT)
                continue;
            final File file = new File(dir, entry.getKey().path);
            try(final BufferedWriter writer = new BufferedWriter(new FileWriter(file, !loaded.containsKey(entry.getKey())))){
                for(final LogEntry log : entry.getValue()){
                    writer.write(log.toString());
                    writer.newLine();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
