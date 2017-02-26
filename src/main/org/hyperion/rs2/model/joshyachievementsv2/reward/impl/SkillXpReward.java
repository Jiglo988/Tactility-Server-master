package org.hyperion.rs2.model.joshyachievementsv2.reward.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.joshyachievementsv2.reward.Reward;

public class SkillXpReward implements Reward{

    public final int skill;
    public final int xp;

    public SkillXpReward(final int skill, final int xp){
        this.skill = skill;
        this.xp = xp;
    }

    public void reward(final Player player){
        final String name = Skills.SKILL_NAME[skill];
        final int current = player.getSkills().getExperience(skill);
        final int max = Math.min(current + xp, Skills.MAXIMUM_EXP);
        final int gained = max - current;
        if(gained == 0){
            player.sendf("You already have the maximum XP in %s!", name);
            return;
        }
        player.getSkills().setExperience(skill, max);
        player.sendf("You have gained %,d %s XP!", gained, name);
    }
}
