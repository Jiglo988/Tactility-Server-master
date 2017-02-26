package org.hyperion.rs2.model.joshyachievementsv2.task;

import org.hyperion.rs2.model.joshyachievementsv2.Achievements;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraints;

import java.util.function.Predicate;

public abstract class Task{

    public static class Filter<T extends Task> implements Predicate<T>{

        public final Class<T> clazz;

        public Filter(final Class<T> clazz){
            this.clazz = clazz;
        }

        public boolean test(final T t){
            return t.getClass().equals(clazz);
        }
    }

    public int achievementId;

    public final int id;
    public final int threshold;

    public final int number;

    public String desc;

    public final Constraints constraints;

    public int preTaskId;

    private String shortDesc;

    protected Task(final int id, final int threshold){
        this.id = id;
        this.threshold = threshold;

        number = id+1;

        constraints = new Constraints();

        preTaskId = -1;
    }

    public boolean canProgress(final int currentProgress, final int progress){
        return true;
    }

    public boolean finished(final int progress){
        return progress >= threshold;
    }

    public boolean hasPreTask(){
        return preTask() != null;
    }

    public Task preTask(){
        return Achievements.get().get(achievementId).tasks.get(preTaskId);
    }

    public String shortDesc(){
        if(shortDesc == null)
            shortDesc = desc.length() <= 40 ? desc : (desc.substring(0, 40) + "...");
        return shortDesc;
    }
}
