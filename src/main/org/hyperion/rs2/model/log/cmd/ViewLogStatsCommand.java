package org.hyperion.rs2.model.log.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

/**
 * Created by Jet on 12/18/2014.
 */
public class ViewLogStatsCommand extends Command {

    public ViewLogStatsCommand(){
        super("viewlogstats", Rank.DEVELOPER);
    }

    public boolean execute(final Player player, final String input){
        /*
        final String targetName = filterInput(input).trim();
        if(!PlayerLoading.playerExists(targetName)){
            player.sendf("%s does not exist", targetName);
            return false;
        }
        final Player target = World.getPlayerByName(targetName);
        final LogManager manager = target != null ? target.getLogManager() : new LogManager(targetName);
        player.sendf("@red@%s@blu@ Log Stats", targetName);
        for(final LogEntry.Category category : LogEntry.Category.values()){
            final Set<LogEntry> logs = manager.getLogs(category);
            player.sendf("@gre@%s @blu@Logs: @red@%,d", category, logs == null ? 0 : logs.size());
        }
        return false;*/
        return true;
    }
}
