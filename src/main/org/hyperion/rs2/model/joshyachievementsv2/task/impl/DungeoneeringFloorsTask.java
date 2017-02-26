package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class DungeoneeringFloorsTask extends Task{

    private static class MyFilter extends Filter<DungeoneeringFloorsTask>{

        private final Difficulty difficulty;
        private final Size size;

        private MyFilter(final Difficulty difficulty, final Size size){
            super(DungeoneeringFloorsTask.class);
            this.difficulty = difficulty;
            this.size = size;
        }

        public boolean test(final DungeoneeringFloorsTask t){
            return super.test(t)
                    && t.size == null || t.size == size
                    && t.difficulty == null || t.difficulty == difficulty;
        }
    }

    public enum Difficulty{
        EASY, MEDIUM, HARD
    }

    public enum Size{
        SMALL, MEDIUM, LARGE
    }

    public final Difficulty difficulty;
    public final Size size;

    public DungeoneeringFloorsTask(final int id, final Difficulty difficulty, final Size size, final int floors){
        super(id, floors);
        this.difficulty = difficulty;
        this.size = size;

        if(difficulty != null && size != null)
            desc = String.format("Complete %,d %s dungeons on %s difficulty", floors, size, difficulty);
        else if(difficulty != null)
            desc = String.format("Complete %,d dungeons on %s difficulty", floors, difficulty);
        else if(size != null)
            desc = String.format("Complete %,d %s dungeons", floors, size);
        else
            throw new IllegalArgumentException("DIFFICULTY AND SIZE CANT BOTH BE NULL!!!!!!!!!!!");
    }

    public static Filter<DungeoneeringFloorsTask> filter(final Difficulty difficulty, final Size size){
        return new MyFilter(difficulty, size);
    }
}
