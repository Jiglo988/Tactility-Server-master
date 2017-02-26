package org.hyperion.rs2.model.punishment.manager;

import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.rs2.model.punishment.Target;
import org.hyperion.rs2.model.punishment.Type;
import org.hyperion.rs2.model.punishment.holder.PunishmentHolder;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.sql.DbHub;

import java.util.*;

public final class PunishmentManager {

    private static PunishmentManager instance;
    private final Map<String, PunishmentHolder> holders;

    private PunishmentManager(){
        holders = new HashMap<>();
    }

    public boolean load(){
        if(!DbHub.getGameDb().isInitialized())
            return false;
        List<Punishment> punishments = DbHub.getGameDb().getPunishment().load();
        if(punishments.isEmpty() || punishments.isEmpty())
            return false;
        punishments.forEach(this::add);
        return true;
    }

    public void add(final Punishment p){
        PunishmentHolder holder = get(p.getVictimName());
        if(holder == null){
            holder = PunishmentHolder.create(p.getVictimName(), p.getVictimIp());
            add(holder);
        }
        holder.add(p);
    }

    public void add(final PunishmentHolder p){
        holders.put(p.getVictimName().toLowerCase(), p);
    }

    public PunishmentHolder get(final String victim){
        return holders.get(victim.toLowerCase());
    }

    public List<Punishment> getByIp(final String ip){
        final List<Punishment> list = new ArrayList<>();
        for(final PunishmentHolder holder : getHolders())
            for(final Punishment p : holder.getPunishments())
                if(ip.equals(p.getVictimIp()))
                    list.add(p);
        return list;
    }

    public List<Punishment> getByMac(final int mac){
        final List<Punishment> list = new ArrayList<>();
        for(final PunishmentHolder holder : getHolders())
            for(final Punishment p : holder.getPunishments())
                if(mac == p.getVictimMac())
                    list.add(p);
        return list;
    }

    public boolean isBanned(final String name, final String ip, final int mac, final int[] specialUid){
        return findBan(name, ip, mac, specialUid) != null;
    }

    public Punishment findBan(final String name, final String ip, final int mac, final int[] specialUid){
        if(name != null){
            final PunishmentHolder holder = get(name);
            if(holder != null){
                for(final Punishment p : holder.getPunishments()){
                    if(p.getCombination().getType() == Type.BAN){
                        return p;
                    }
                }
            }
        }
        for(final PunishmentHolder h : getHolders()){
            for(final Punishment p : h.getPunishments()){
                if(p.getCombination().getType() != Type.BAN)
                    continue;
                if(ip != null && p.getCombination().getTarget() == Target.IP && ip.equalsIgnoreCase(p.getVictimIp()))
                    return p;
                if(mac != -1 && p.getCombination().getTarget() == Target.MAC && mac == p.getVictimMac())
                    return p;
                if(specialUid != null && p.getCombination().getTarget() == Target.SPECIAL && Arrays.equals(specialUid, p.getVictimSpecialUid()))  {
                    TextUtils.writeToFile("./data/specialUidStops.txt", "Special UID ban stopped: " + name);
                    return p;
                }
            }
        }
        return null;
    }

    public boolean contains(final String victim){
        return get(victim) != null;
    }

    public Collection<PunishmentHolder> getHolders(){
        return holders.values();
    }

    public static void init(){
        instance = new PunishmentManager();
        instance.load();
    }

    public static PunishmentManager getInstance(){
        return instance;
    }
}
