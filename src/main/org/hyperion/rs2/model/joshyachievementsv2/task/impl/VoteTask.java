package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class VoteTask extends Task{

    private static final Filter<VoteTask> FILTER = new Filter<>(VoteTask.class);

    public VoteTask(final int id, final int votes){
        super(id, votes);

        desc = String.format("Vote %,d times", votes);
    }

    public static Filter<VoteTask> filter(){
        return FILTER;
    }
}
