package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class PlayerKillTask extends Task{

    private static final Filter<PlayerKillTask> FILTER = new Filter<>(PlayerKillTask.class);

    public PlayerKillTask(final int id, final int kills){
        super(id, kills);

        desc = String.format("Kill %,d players", kills);
    }

    public static Filter<PlayerKillTask> filter(){
        return FILTER;
    }
}
