package org.hyperion.rs2.model.recolor;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.UpdateFlags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecolorManager {

    private static final int DONATED_AMOUNT_REQUIRED = 5000;

    private final Player player;
    private final Map<Integer, List<Recolor>> map;

    public RecolorManager(final Player player){
        this.player = player;

        map = new HashMap<>();
    }

    public Player getPlayer(){
        return player;
    }

    public int getCount(){
        return map.size();
    }

    public int getLimit(){
        if(Rank.hasAbility(player, Rank.DEVELOPER))
            return Integer.MAX_VALUE;
        return player.getPoints().getDonatorPointsBought() / DONATED_AMOUNT_REQUIRED;
    }

    public boolean isAtLimit(){
        return map.size() == getLimit();
    }

    public int getAmountForLimitIncrease(){
        return DONATED_AMOUNT_REQUIRED - (player.getPoints().getDonatorPointsBought() % DONATED_AMOUNT_REQUIRED);
    }

    public List<Recolor> getAll(){
        final List<Recolor> list = new ArrayList<>();
        map.values().forEach(list::addAll);
        return list;
    }

    public List<Recolor> getRecolors(final int id){
        return map.get(id);
    }

    public Recolor getRecolor(final int id){
        final List<Recolor> list = getRecolors(id);
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    public void add(final Recolor recolor){
        if(!map.containsKey(recolor.getId()))
            map.put(recolor.getId(), new ArrayList<>());
        map.get(recolor.getId()).add(recolor);
        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
    }

    public List<Recolor> remove(final int id){
        final List<Recolor> list = map.remove(id);
        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
        return list;
    }

    public boolean contains(final int id){
        return map.containsKey(id);
    }

    public void clear(){
        map.clear();
        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
    }
}
