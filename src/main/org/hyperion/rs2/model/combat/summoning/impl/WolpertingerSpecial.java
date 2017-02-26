package org.hyperion.rs2.model.combat.summoning.impl;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.combat.summoning.AbstractSummoningSpecial;


public final class WolpertingerSpecial extends AbstractSummoningSpecial {
	private static WolpertingerSpecial special;
	public static WolpertingerSpecial getInstance() {
		if(special == null)
			special = new WolpertingerSpecial();
		return special;
	}
	@Override
	public int getScrollId() {
		// TODO Auto-generated method stub
		return 12437;
	}

	@Override
	public boolean checkRequirements(Player p) {
		boolean returnValue = true;
		if(!p.getInventory().contains(this.getScrollId())) {
			p.getActionSender().sendMessage("You need a magic focus scroll to preform this!");
			returnValue = false;
		}
		return returnValue;
	}

	@Override
	public void execute(Player p) {
		p.playAnimation(Animation.create(7660));
		p.playGraphics(Graphic.create(1300));
		if(p.getSkills().getLevel(Skills.MAGIC) >= p.getSkills().getRealLevels()[Skills.MAGIC]) {
			p.getSkills().setLevel(Skills.MAGIC, p.getSkills().getRealLevels()[Skills.MAGIC] + 8);
		} else {
			p.getSkills().setLevel(Skills.MAGIC, p.getSkills().getLevels()[Skills.MAGIC] + 8);
		}
	}

	@Override
	public void executeOpponent(Entity p) throws NullPointerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeFamiliar(NPC n) {
		n.playAnimation(Animation.create(8306));
		n.playGraphics(Graphic.create(1464));
		
	}
	@Override
	public boolean requiresOpponent() {
		return false;
	}
	@Override
	public int requiredSpecial() {
		return 10;
	}
	
}
