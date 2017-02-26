package org.hyperion.rs2.model.content.pvptasks.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.pvptasks.PvPTask;
import org.hyperion.rs2.model.newcombat.Skills;

public class ZerkTask extends PvPTask {

	@Override
	public boolean isTask(Player p, Player o) {
		int defense = o.getSkills().getLevelForExp(Skills.DEFENCE);
		return p.getPvPTask() != null && p.getPvPTask() instanceof ZerkTask && 
				defense >= 40 && defense <= 50;
	}

}
