package org.hyperion.rs2.model.joshyachievementsv2.reward;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hyperion.rs2.model.Player;

public class Rewards{

    public final List<Reward> list;

    public Rewards(final Collection<Reward> collection){
        list = new ArrayList<>(collection);
    }

    public Rewards(){
        this(new ArrayList<>());
    }

    public void add(final Reward reward){
        list.add(reward);
    }

    public void reward(final Player player){
        list.forEach(r -> r.reward(player));
    }
}
