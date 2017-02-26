package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class KillstreakTask extends Task{

    private static final Filter<KillstreakTask> FILTER = new Filter<>(KillstreakTask.class);

    public KillstreakTask(final int id, final int killstreak){
        super(id, killstreak);

        desc = String.format("Get a %,d killstreak", killstreak);
    }

    public boolean canProgress(final int currentProgress, final int progress){
        return progress > currentProgress;
    }

    public static Filter<KillstreakTask> filter(){
        return FILTER;
    }
}
