package org.hyperion.rs2.model.cluescroll.requirement;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExperienceRequirement extends Requirement{

    private int skill;
    private int xp;

    public ExperienceRequirement(final int skill, final int xp){
        super(Type.EXPERIENCE);
        this.skill = skill;
        this.xp = xp;
    }

    public int getSkill(){
        return skill;
    }

    public void setSkill(final int skill){
        this.skill = skill;
    }

    public int getXp(){
        return xp;
    }

    public void setXp(final int xp){
        this.xp = xp;
    }

    public boolean apply(final Player player){
        return player.getSkills().getExperience(skill) >= xp;
    }

    protected void append(final Document doc, final Element root){
        root.appendChild(ClueScrollUtils.createElement(doc, "skill", skill));
        root.appendChild(ClueScrollUtils.createElement(doc, "xp", xp));
    }

    public String toString(){
        return String.format("%s: %s @ %,d XP", super.toString(), skill, xp);
    }

    public static ExperienceRequirement parse(final Element element){
        final int skill = ClueScrollUtils.getInteger(element, "skill");
        final int xp = ClueScrollUtils.getInteger(element, "xp");
        return new ExperienceRequirement(skill, xp);
    }
}
