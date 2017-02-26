package org.hyperion.rs2.model.log.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

public class ViewLogsCommand extends Command {

    public ViewLogsCommand(){
        super("viewlogs", Rank.DEVELOPER);
    }

    public boolean execute(final Player player, final String input){
        /*
        try {
            final String line = filterInput(input).trim();
            final String[] parts = line.split(",");
            final String targetName = parts[0].trim();
            if (!PlayerLoading.playerExists(targetName)) {
                player.sendf("%s does not exist", targetName);
                return false;
            }
            final List<LogEntry.Category> categories = new ArrayList<>();
            if (parts.length == 1) {
                categories.addAll(Arrays.asList(LogEntry.Category.values()));
            } else {
                for (final String name : parts[1].trim().split(" +")) {
                    final LogEntry.Category category = LogEntry.Category.byName(name);
                    if (category != null)
                        categories.add(category);
                }
            }
            long startTime = -1;
            if (parts.length == 3) {
                final String[] durationParts = parts[2].split(" +");
                TimeUnit unit = TimeUnit.HOURS;
                long duration;
                try {
                    duration = Long.parseLong(durationParts[0].trim());
                    if (durationParts.length == 2) {
                        final String unitStr = durationParts[1].trim();
                        if (unitStr.contains("second"))
                            unit = TimeUnit.SECONDS;
                        else if (unitStr.contains("minute"))
                            unit = TimeUnit.MINUTES;
                        else if (unitStr.contains("hour"))
                            unit = TimeUnit.HOURS;
                        else if (unitStr.contains("day"))
                            unit = TimeUnit.DAYS;
                        else if (unitStr.contains("week")) {
                            unit = TimeUnit.DAYS;
                            duration *= 7;
                        } else if (unitStr.contains("month")) {
                            unit = TimeUnit.DAYS;
                            duration *= 30;
                        } else if (unitStr.contains("year")) {
                            unit = TimeUnit.DAYS;
                            duration *= 365;
                        }
                    }
                    startTime = System.currentTimeMillis() - unit.toMillis(duration);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    player.sendf("Error parsing time frame");
                    return false;
                }
            }
            final Player target = World.getPlayerByName(targetName);
            final LogManager manager = target != null ? target.getLogManager() : new LogManager(targetName);
            for (final LogEntry.Category category : categories) {
                final Set<LogEntry> logs = manager.getLogs(category, startTime);
                if(logs == null){
                    player.sendf("@blu@Unable to load @gre@%s@blu@'s@red@%s@blu@ Logs", targetName, category);
                    continue;
                }
                if (startTime != -1)
                    player.sendf("@red@%s@blu@ Logs Since @red@%s@blu@: @red@%,d@blu@ Results", category, LogUtils.format(new Date(startTime)), logs.size());
                else
                    player.sendf("@red@%s@blu@ Logs (All Time): @red@%,d@blu@ Results", category, logs.size());
                logs.forEach(log -> log.send(player));
            }
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        */
        return true;
    }
}
