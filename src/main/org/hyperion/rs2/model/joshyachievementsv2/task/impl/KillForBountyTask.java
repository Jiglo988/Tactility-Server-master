package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class KillForBountyTask extends Task{

    private static final Filter<KillForBountyTask> FILTER = new Filter<>(KillForBountyTask.class);

    public final boolean accumulative;

    public KillForBountyTask(final int id, final int bounty, final boolean accumulative){
        super(id, bounty);
        this.accumulative = accumulative;

        if(accumulative)
            desc = String.format("Obtain a total bounty of %,d for killing players", bounty);
        else
            desc = String.format("Obtain a bounty of %,d from killing a single player", bounty);
    }

    public boolean canProgress(final int progress){
        return accumulative || progress >= threshold;
    }

    public static Filter<KillForBountyTask> filter(){
        return FILTER;
    }
}
