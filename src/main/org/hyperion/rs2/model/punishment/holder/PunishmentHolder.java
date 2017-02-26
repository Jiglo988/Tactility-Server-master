package org.hyperion.rs2.model.punishment.holder;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.punishment.Combination;
import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.rs2.model.punishment.Target;
import org.hyperion.rs2.model.punishment.Type;
import org.hyperion.rs2.util.NameUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PunishmentHolder {

    private final String victim;
    private final String ip;

    private final Map<Combination, Punishment> punishments;

    public PunishmentHolder(final String victim, final String ip){
        this.victim = victim;
        this.ip = ip;

        punishments = new HashMap<>();
    }

    public Collection<Punishment> getPunishments(){
        return punishments.values();
    }

    public void remove(final Punishment punishment){
        remove(punishment.getCombination());
    }

    public void remove(final Combination combination){
        punishments.remove(combination);
    }

    public Punishment get(final Target target, final Type type){
        return get(Combination.of(target, type));
    }

    public Punishment get(final Combination combination){
        return punishments.get(combination);
    }

    public void add(final Punishment punishment){
        punishments.put(punishment.getCombination(), punishment);
    }

    public long getVictimId(){
        return NameUtils.nameToLong(getVictimName());
    }

    public String getVictimName(){
        return victim;
    }

    public Player getVictim(){
        return World.getPlayerByName(getVictimName());
    }

    public String getVictimIP(){
        return ip;
    }

    public String toString(){
        final StringBuilder bldr = new StringBuilder();
        bldr.append(String.format("Victim: %s (%s)\n", getVictimName(), getVictimIP()));
        final Collection<Punishment> punishments = getPunishments();
        bldr.append(String.format("Punishments: %d\n", punishments.size()));
        for(final Punishment p : punishments){
            bldr.append("------------------------------------------------------------------------\n");
            bldr.append(String.format("Issued By: %s @ %s\n", p.getIssuerName(), p.getTime().getStartDateStamp()));
            bldr.append(String.format("Type: %s %s\n", p.getCombination().getTarget(), p.getCombination().getType()));
            bldr.append(String.format("Duration: %,d %s\n", p.getTime().getDuration(), p.getTime().getUnit()));
            bldr.append(String.format("Reason: %s\n", p.getReason()));
            bldr.append(String.format("Expires: %s\n", p.getTime().getExpirationDateStamp()));
            bldr.append("------------------------------------------------------------------------\n");
        }
        return bldr.toString();
    }

    public static PunishmentHolder create(final String victim, final String ip){
        return new PunishmentHolder(victim, ip);
    }
}
