package org.hyperion.rs2.model.punishment.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.rs2.model.punishment.Target;
import org.hyperion.rs2.model.punishment.Type;
import org.hyperion.rs2.model.punishment.holder.PunishmentHolder;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;

/**
 * Created by Jet on 10/29/2014.
 */
public class ViewPunishmentsCommand extends Command {

    public ViewPunishmentsCommand(){
        super("viewpunishments", Rank.HELPER);
    }

    public boolean execute(final Player player, final String input){
        String issuerName = filterInput(input);

        if(issuerName.startsWith("@")){
            issuerName = issuerName.substring(1);
            for(final PunishmentHolder holder : PunishmentManager.getInstance().getHolders())
                for(final Punishment p : holder.getPunishments())
                    if(!p.getTime().isExpired() && p.getIssuerName().equalsIgnoreCase(issuerName))
                        p.send(player, false);
        }else{
            final String[] arguments = issuerName.split(" ");
            final Type type = Type.valueOf(arguments[0].toUpperCase());
            final Target target = Target.valueOf(arguments[1].toUpperCase());
            for(final PunishmentHolder holder : PunishmentManager.getInstance().getHolders())
                for(final Punishment p : holder.getPunishments())
                    if(!p.getTime().isExpired() && p.getCombination().getType().equals(type) && p.getCombination().getTarget().equals(target))
                        p.send(player, false);
        }
        return true;
    }
}
