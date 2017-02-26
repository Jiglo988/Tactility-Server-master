package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;

/**
 * An event which increases ActivityPoints, refreshes Quest Tab , refreshes
 * Skills.
 */
public class PlayerTask1Second extends Task {

	/**
	 * The delay in milliseconds between consecutive execution.
	 */
	public static final long CYCLETIME = 1000;

	/**
	 * Creates the event each 30 seconds.
	 */
	public PlayerTask1Second() {
		super(CYCLETIME);
	}

	@Override
	public void execute() {
		for(Player p : World.getPlayers()) {


			if(! p.active)
				continue;

			if(p.getDrainRate() > 0) {
				// System.out.println("drain rate: "+getDrainRate()+" prayer level: "+getSkills().getLevel2(5));
				// Prayer.updateCurses(p);

				if(p.getSkills().getLevel(5) - p.getDrainRate() <= 0) {
					p.getActionSender().sendMessage("You've run out of Prayer points.");
					p.getSkills().detractLevel(5, p.getSkills().getLevel(5));
					p.resetPrayers();
					p.getActionSender().sendSkill(5);
					return;
				}
                if(!p.isDead())
				    p.prayerDrain += p.getDrainRate();
				if(p.prayerDrain > 1) {
					p.getSkills().detractLevel(5, (int) p.prayerDrain);
					p.prayerDrain = 0;
				}
				p.getActionSender().sendSkill(5);
			}


            for(int i = 0; i < Skills.SKILL_COUNT; i++) {
				p.skillRecoverTimer[i]++;
				if(p.skillRecoverTimer[i] == 60 && i != 5 && i != 23 && !p.isDead()) {
					p.skillRecoverTimer[i] = 0;
					p.getSkills().normalizeLevel(i);
				}
			}

			p.decreaseSkullTimer();
			if(p.duelAttackable > 0)
				p.refreshDuelTimer();

			p.getExtraData().put("pmCount", 0);
		}
		//FFARandom.cycle();
	}

}
