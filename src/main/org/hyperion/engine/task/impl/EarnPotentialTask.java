package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;


/**
 * @author SaosinHax/Linus/Vegas/Flux/Tinderbox/Jack Daniels/Arsen/Jolt <- All same person
 */

public class EarnPotentialTask extends Task {

	public static final long CYCLETIME = Time.ONE_MINUTE;

	public EarnPotentialTask() {
		super(CYCLETIME);
	}

	@Override
	public void execute() {
		for(Player p : World.getPlayers()) {
			if(p.getPosition().inPvPArea()) {
				if(System.currentTimeMillis() - p.getLastEPIncrease() > Time.ONE_HOUR) {
					p.increaseEP();
					continue;
				}
				int bonus = (p.wildernessLevel / 20) + 1;
				if(p.cE.getOpponent() != null)
					bonus *= 2;
				int risk = p.getRisk();
				if(risk > 50000)
					risk = 50000;
				bonus += risk / 5000;
				if(Misc.random(30 / bonus) == 1) {
					p.increaseEP();
				}
			}
		}
	}
}
