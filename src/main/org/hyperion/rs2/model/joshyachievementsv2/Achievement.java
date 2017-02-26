package org.hyperion.rs2.model.joshyachievementsv2;

import org.hyperion.rs2.model.joshyachievementsv2.reward.Rewards;
import org.hyperion.rs2.model.joshyachievementsv2.task.Tasks;
import org.hyperion.util.Misc;

public class Achievement{

    public enum Difficulty{

        EASY,
        MEDIUM,
        HARD,
        ELITE;

        @Override
        public String toString() {
            return Misc.ucFirst(name());
        }
    }

    public final int id;
    public final Difficulty difficulty;
    public final String title;
    public final Interval interval;
    public final Instructions instructions;
    public final Tasks tasks;
    public final Rewards rewards;

    public final String shortTitle;

    public Achievement(final int id, final Difficulty difficulty, final String title, final Interval interval, final Instructions instructions, final Tasks tasks, final Rewards rewards){
        this.id = id;
        this.difficulty = difficulty;
        this.title = title;
        this.interval = interval;
        this.instructions = instructions;
        this.tasks = tasks;
        this.rewards = rewards;

        shortTitle = title.length() <= 26 ? title : (title.substring(0, 26) + "...");
    }
}
