package org.hyperion.rs2.model.punishment.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.rs2.model.punishment.holder.PunishmentHolder;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;

public class MyPunishmentsCommand extends Command {

    public MyPunishmentsCommand(){
        super("mypunishments", Rank.PLAYER);
    }

    public boolean execute(final Player player, final String input){
        for(final PunishmentHolder holder : PunishmentManager.getInstance().getHolders()){
            for(final Punishment p : holder.getPunishments()){
                if(p.getVictimName().equalsIgnoreCase(player.getName())
                        || p.getVictimIp().equals(player.getShortIP())
                        || p.getVictimMac() == player.getUID()){
                    p.send(player, false);
                }
            }
        }
        return true;
    }
}
