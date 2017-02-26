package org.hyperion.rs2.model.content.bounty;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Time;

public class BountyHunterTask extends Task {

	public BountyHunterTask() {
		super(Time.ONE_MINUTE);
	}
	
	private int counter = 5;
	
	public void execute() {
		counter--;
		if (counter == 0) {
			counter = 5;
			//Checks if the player is in combat with his target, if not he'll reset
            World.getPlayers().stream().filter(p ->
                p.cE.getOpponent() == null || !p.getCombat().getOpponent().getEntity().equals(p.getBountyHunter().getTarget())
            ).forEach(p -> p.getBountyHunter().clearTarget());
		}
		if (counter % 2 == 0) {
			for (final Player p : World.getPlayers()) {
				//if the player has a target, but the target is not in the wilderness anymore

				if (BountyHunter.applicable(p))
					p.getBountyHunter().findTarget();
			}
		}
		for (Player p : World.getPlayers()) {
			if (p == null)
				continue;
			//This means the player has no target
			if (p.getBountyHunter().getTarget() == null) {
				p.getActionSender().sendString("@or1@Reset: @gre@" + (((counter + 1) % 2) + 1) + " @or1@min", 36503);
				continue;
			}
			//This will happen if the player or his target get out of the wilderness
			/*if (BountyHunter.applicable2(p.getBountyHunter().getTarget()) || !BountyHunter.applicable2(p)) {
				p.getActionSender().sendString("@or1@Reset: @gre@" + (((counter + 1) % 2) + 1) + " @or1@min", 36503);
				continue;
			} */
			//If neither happened, it will assume normal scenario
			p.sendMessage("Your target is at level " + p.getBountyHunter().getTarget().wildernessLevel + " wilderness.");
			p.getActionSender().sendString("@or1@Reset: @gre@" + counter + " @or1@min", 36503);
		}
	}
}
