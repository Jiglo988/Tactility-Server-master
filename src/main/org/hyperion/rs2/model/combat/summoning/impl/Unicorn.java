package org.hyperion.rs2.model.combat.summoning.impl;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.summoning.AbstractSummoningSpecial;
import org.hyperion.rs2.model.newcombat.Skills;

public class Unicorn extends AbstractSummoningSpecial {
	private static Unicorn unicorn;
	public static final Unicorn getInstance() {
		if(unicorn == null)
			unicorn = new Unicorn();
		return unicorn;
	}
	@Override
	public boolean requiresOpponent() {
		return false;
	}

	@Override
	public int getScrollId() {
		return 12434;
	}

	@Override
	public boolean checkRequirements(Player p) {
		if(p.getInventory().contains(getScrollId())) {
			return true;
		}
		p.getActionSender().sendMessage("You need a healing aura scroll for this!");
		return false;
	}

	@Override
	public void execute(Player p) {
		p.playAnimation(Animation.create(7660));
		p.playGraphics(Graphic.create(1298));
		if(p.getSkills().getLevels()[Skills.HITPOINTS] >= p.getSkills().getRealLevels()[Skills.HITPOINTS] + 12)
			p.getSkills().setLevel(Skills.HITPOINTS, p.getSkills().getRealLevels()[Skills.HITPOINTS] + 12);
		else
			p.getSkills().setLevel(Skills.HITPOINTS, p.getSkills().getLevels()[Skills.HITPOINTS] + 12);
			
	}

	@Override
	public void executeOpponent(Entity p) throws NullPointerException {
		// TODO Auto-generated method stub
	}

	@Override
	public void executeFamiliar(NPC n) {
		n.playAnimation(Animation.create(6375));
		n.playGraphics(Graphic.create(1356));
	}
	@Override
	public int requiredSpecial() {
		// TODO Auto-generated method stub
		return 25;
	}

}
