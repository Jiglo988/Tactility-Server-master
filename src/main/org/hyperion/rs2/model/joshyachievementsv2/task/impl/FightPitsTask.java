package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import org.hyperion.rs2.model.joshyachievementsv2.task.Task;

public class FightPitsTask extends Task{

    private static class MyFilter extends Filter<FightPitsTask>{

        private final Result result;

        private MyFilter(final Result result){
            super(FightPitsTask.class);
            this.result = result;
        }

        public boolean test(final FightPitsTask t){
            return super.test(t)
                    && t.result == result;
        }
    }

    public enum Result{
        WIN, LOSE
    }

    public final Result result;

    public FightPitsTask(final int id, final Result result, final int times){
        super(id, times);
        this.result = result;

        desc = String.format("%s Fight Pits %,d times", result, times);
    }

    public static Filter<FightPitsTask> filter(final Result result){
        return new MyFilter(result);
    }
}
