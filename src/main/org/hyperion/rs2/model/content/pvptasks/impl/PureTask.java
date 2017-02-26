package org.hyperion.rs2.model.content.pvptasks.impl;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.pvptasks.PvPTask;
import org.hyperion.rs2.model.newcombat.Skills;

public class PureTask extends PvPTask {
    //not ready yet :p and no sql is not enabled
	@Override
	public boolean isTask(Player p, Player o) {
		return p.getPvPTask() != null && p.getPvPTask() instanceof PureTask &&
				o.getSkills().getRealLevels()[Skills.DEFENCE] <= 20;
	}

}
