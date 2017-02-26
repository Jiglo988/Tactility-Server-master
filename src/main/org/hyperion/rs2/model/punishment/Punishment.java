package org.hyperion.rs2.model.punishment;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.punishment.holder.PunishmentHolder;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.sql.DbHub;

import java.util.Arrays;
import java.util.StringJoiner;

public class Punishment {

    private String issuer;
    private final String victim;
    private final String victimIp;
    private final int victimMac;
    private final int[] victimSpecialUid;
    private final Combination combination;
    private final Time time;
    private String reason;

    public Punishment(final String issuer, final String victim, final String victimIp, final int victimMac, final int[] victimSpecialUid, final Combination combination, final Time time, final String reason){
        this.issuer = issuer;
        this.victim = victim;
        this.victimIp = victimIp;
        this.victimMac = victimMac;
        this.victimSpecialUid = victimSpecialUid;
        this.combination = combination;
        this.time = time;
        this.reason = reason;
    }

    public Punishment(final Player issuer, final String victim, final String victimIp, final int victimMac, final int[] victimSpecialUid, final Combination combination, final Time time, final String reason){
        this(issuer.getName(), victim, victimIp, victimMac, victimSpecialUid, combination, time, reason);
    }

    public Punishment(final Player issuer, final Player victim, final Combination combination, final Time time, final String reason){
        this(issuer, victim.getName(), victim.getShortIP(), victim.getUID(), victim.specialUid, combination, time, reason);
    }

    public Punishment(final Player victim, final Combination combination, final Time time, final String reason){
        this("Server", victim.getName(), victim.getShortIP(), victim.getUID(), victim.specialUid, combination, time, reason);
    }

    public PunishmentHolder getHolder(){
        return PunishmentManager.getInstance().get(getVictimName());
    }

    public boolean matches(final Player player){
        return getVictimName().equalsIgnoreCase(player.getName())
                || getVictimIp().equals(player.getShortIP())
                || getVictimMac() == player.getUID();
    }

    public void send(final Player player, final boolean alert){
        final Player issuer = getIssuer();
        final String issuerName = issuer != null ? issuer.getSafeDisplayName() : getIssuerName();
        if(alert){
            player.sendf(
                    "Alert##%s %s - Issued By %s (%s)##%s##Expires: %s",
                    TextUtils.titleCase(getVictimName()),
                    getCombination().toString().toLowerCase(),
                    TextUtils.titleCase(issuerName),
                    getTime().toString().toLowerCase(),
                    TextUtils.titleCase(getReason()),
                    getTime().isExpired() ? "now" : getTime().getExpirationDateStamp()
            );
        }else{
            player.sendf("@dre@----------------------------------------------------------------------------------------");
            player.sendf("@dre@%s's %s@bla@ - Issued By @dre@%s@bla@ (@dre@%s@bla@)", TextUtils.titleCase(getVictimName()), getCombination().toString().toLowerCase(), TextUtils.titleCase(issuerName), getTime().toString().toLowerCase());
            player.sendf("@dre@Reason: @bla@%s", TextUtils.titleCase(getReason()));
            player.sendf("@dre@Issued: @bla@%s", getTime().getStartDateStamp());
            player.sendf("@dre@Expires: @bla@%s", getTime().isExpired() ? "NOW!" : getTime().getExpirationDateStamp());
            if(!getTime().isExpired())
                player.sendf("@dre@Remaining: @bla@%s", getTime().getRemainingTimeStamp());
            player.sendf("@dre@----------------------------------------------------------------------------------------");
        }
    }

    public boolean apply(){
        final Player victim = getVictim();
        if(victim != null){
            getCombination().apply(victim);
            return true;
        }
        boolean applied = false;
        for(final Player p : World.getPlayers()){
            if(p == null)
                continue;
            switch(getCombination().getTarget()){
                case IP:
                    if(!p.getShortIP().equals(getVictimIp()))
                        break;
                    getCombination().getType().apply(p);
                    applied = true;
                    break;
                case MAC:
                    if(p.getUID() != getVictimMac())
                        break;
                    getCombination().getType().apply(p);
                    applied = true;
                    break;
                case SPECIAL:
                    if(!Arrays.equals(p.specialUid, getVictimSpecialUid()))
                        break;
                    getCombination().getType().apply(p);
                    applied = true;
                    break;
            }
        }
        return applied;
    }

    public boolean isApplied(){
        final Player victim = getVictim();
        if(victim != null)
            return getCombination().isApplied(victim);
        for(final Player p : World.getPlayers()){
            if(p == null)
                continue;
            switch(getCombination().getTarget()){
                case IP:
                    if(!p.getShortIP().equals(getVictimIp()))
                        break;
                    if(!getCombination().getType().isApplied(p))
                        return false;
                    break;
                case MAC:
                    if(p.getUID() != getVictimMac())
                        break;
                    if(!getCombination().getType().isApplied(p))
                        return false;
                    break;
            }
        }
        return true;
    }

    public boolean unapply(){
        final Player victim = getVictim();
        if(victim != null){
            getCombination().unapply(victim);
            return true;
        }
        boolean unapplied = false;
        for(final Player p : World.getPlayers()){
            if(p == null)
                continue;
            switch(getCombination().getTarget()){
                case IP:
                    if(!p.getShortIP().equals(getVictimIp()))
                        break;
                    getCombination().getType().unapply(p);
                    unapplied = true;
                    break;
                case MAC:
                    if(p.getUID() != getVictimMac())
                        break;
                    getCombination().getType().unapply(p);
                    unapplied = true;
                    break;
            }
        }
        return unapplied;
    }

    public String getIssuerName(){
        return issuer;
    }

    public void setIssuerName(final String issuer){
        this.issuer = issuer;
    }

    public void setIssuer(final Player issuer){
        setIssuerName(issuer.getName());
    }

    public Player getIssuer(){
        return World.getPlayerByName(getIssuerName());
    }

    public String getVictimName(){
        return victim;
    }

    public Player getVictim(){
        return World.getPlayerByName(getVictimName());
    }

    public String getVictimIp(){
        return victimIp;
    }

    public int getVictimMac(){
        return victimMac;
    }

    public int[] getVictimSpecialUid(){
        return victimSpecialUid;
    }

    public String getVictimSpecialUidAsString(){
        final StringJoiner joiner = new StringJoiner(",");
        for(final int n : getVictimSpecialUid())
            joiner.add(Integer.toString(n));
        return joiner.toString();
    }

    public Combination getCombination(){
        return combination;
    }

    public Time getTime(){
        return time;
    }

    public String getReason(){
        return reason;
    }

    public void setReason(final String reason){
        this.reason = reason;
    }

    public void insert(){
        if(!DbHub.getGameDb().isInitialized())
            return;
        DbHub.getGameDb().getPunishment().insert(this);
    }

    public void update(){
        if(!DbHub.getGameDb().isInitialized())
            return;
        DbHub.getGameDb().getPunishment().update(this);
    }

    public void setActive(final boolean isActive){
        if(!DbHub.getGameDb().isInitialized())
            return;
        DbHub.getGameDb().getPunishment().setActive(this, isActive);
    }

    public void delete(){
        if(!DbHub.getGameDb().isInitialized())
            return;
        DbHub.getGameDb().getPunishment().delete(this);
    }

    public static Punishment create(final String issuer, final String victim, final String victimIp, final int victimMac, final int[] specialUid, final Combination combination, final Time time, final String reason){
        return new Punishment(issuer, victim, victimIp, victimMac, specialUid, combination, time, reason);
    }

    public static Punishment create(final String issuer, final Player victim, final Combination combination, final Time time, final String reason){
        return create(issuer, victim.getName(), victim.getShortIP(), victim.getUID(), victim.specialUid, combination, time, reason);
    }

    public static Punishment create(final Player issuer, final String victim, final String victimIp, final int victimMac, final int[] specialUid, final Combination combination, final Time time, final String reason){
        return new Punishment(issuer.getName(), victim, victimIp, victimMac, specialUid, combination, time, reason);
    }

    public static Punishment create(final Player issuer, final Player victim, final Combination combination, final Time time, final String reason){
        return new Punishment(issuer, victim, combination, time, reason);
    }

    public static Punishment create(final Player victim, final Combination combination, final Time time, final String reason){
        return new Punishment(victim, combination, time, reason);
    }
}
