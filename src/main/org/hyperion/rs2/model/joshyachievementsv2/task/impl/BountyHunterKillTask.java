package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class BountyHunterKillTask extends Task{

    private static final Filter<BountyHunterKillTask> FILTER = new Filter<>(BountyHunterKillTask.class);

    public BountyHunterKillTask(final int id, final int kills){
        super(id, kills);

        desc = String.format("Get %,d Bounty Hunter kills", kills);
    }

    public static Filter<BountyHunterKillTask> filter(){
        return FILTER;
    }
}
