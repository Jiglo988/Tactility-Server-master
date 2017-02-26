package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class PlaceBountyTask extends Task{

    private static final Filter<PlaceBountyTask> FILTER = new Filter<>(PlaceBountyTask.class);

    public final boolean accumulative;

    public PlaceBountyTask(final int id, final int bounty, final boolean accumulative){
        super(id, bounty);
        this.accumulative = accumulative;

        if(accumulative)
            desc = String.format("Place bounties totalling %,d PKP", bounty);
        else
            desc = String.format("Place a single bounty of %,d PKP", bounty);
    }

    public boolean canProgress(final int progress){
        return accumulative || progress >= threshold;
    }

    public static Filter<PlaceBountyTask> filter(){
        return FILTER;
    }
}
