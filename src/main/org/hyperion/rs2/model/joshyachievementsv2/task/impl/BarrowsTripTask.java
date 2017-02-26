package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class BarrowsTripTask extends Task{

    private static final Filter<BarrowsTripTask> FILTER = new Filter<>(BarrowsTripTask.class);

    public BarrowsTripTask(final int id, final int trips){
        super(id, trips);

        desc = String.format("Complete %,d Barrows trips", trips);
    }
    
    public static Filter<BarrowsTripTask> filter(){
        return FILTER;
    }

}
