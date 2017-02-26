package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import java.util.List;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.joshyachievementsv2.task.Task;
import org.hyperion.rs2.model.joshyachievementsv2.utils.AchievementUtils;

public class SkillItemTask extends Task{

    private static class MyFilter extends Filter<SkillItemTask>{

        private final int skill;
        private final int itemId;

        private MyFilter(final int skill, final int itemId){
            super(SkillItemTask.class);
            this.skill = skill;
            this.itemId = itemId;
        }

        public boolean test(final SkillItemTask t){
            return super.test(t)
                    && t.skill == skill
                    && t.itemIds.contains(itemId);
        }
    }

    public final int skill;
    public final List<Integer> itemIds;

    public SkillItemTask(final int id, final int skill, final List<Integer> itemIds, final int quantity){
        super(id, quantity);
        this.skill = skill;
        this.itemIds = itemIds;

        final String skillName = Skills.SKILL_NAME[skill];
        final String itemsJoined = AchievementUtils.joinItems(itemIds);

        desc = String.format("Obtain %,d %s through %s", quantity, itemsJoined, skillName);
    }

    public static Filter<SkillItemTask> filter(final int skill, final int itemId){
        return new MyFilter(skill, itemId);
    }
}
