package org.hyperion.rs2.model.combat.summoning.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.combat.summoning.AbstractSummoningSpecial;

public class SteelTitanSpecial extends AbstractSummoningSpecial {
	private static SteelTitanSpecial steelTitanSpecial;

	public static SteelTitanSpecial getInstance() {
		if(steelTitanSpecial == null)
			steelTitanSpecial = new SteelTitanSpecial();
		return steelTitanSpecial;
	}

	@Override
	public int getScrollId() {
		return 12825;
	}

	/**
	 * Will add summoning special amount later :D
	 */
	@Override
	public boolean checkRequirements(Player p) {
		boolean returnValue = true;
		if(!p.getInventory().contains(getScrollId())) {
			p.getActionSender().sendMessage("You don't have the correct scroll for this special attack!");
			returnValue = false;
		}
		return returnValue;
	}

	@Override
	public void execute(final Player player) {
		player.playGraphics(Graphic.create(1316, 0));
		player.playAnimation(Animation.create(7660));
		player.getInventory().remove(new Item(getScrollId()));
	}

	@Override
	public void executeOpponent(Entity combat) throws NullPointerException {
		final CombatEntity ce = combat.getCombat();
		if(!Combat.isInMulti(ce)) {
			throw new NullPointerException();
		}

		combat.getCombat().doGfx(1449);

		World.submit(new Task(800) {
			int i = 1;
			int damage = CombatCalculation.getCalculatedDamage(ce.getOpponent().getEntity(), ce.getEntity(), (int) (Math.random() * 30), Constants.RANGE, 30);

			public void execute() {
				try {
					if(i == 1) {
						ce.hit(damage, ce.getOpponent().getEntity(), false, Constants.RANGE);
						ce.hit(damage / 2, ce.getOpponent().getEntity(), false, Constants.RANGE);
						i--;
					} else {
						ce.hit(damage / 4, ce.getOpponent().getEntity(), false, Constants.RANGE);
						ce.hit(damage / 4, ce.getOpponent().getEntity(), false, Constants.RANGE);
						this.stop();
					}
				} catch(Exception e) {
					this.stop();
				}
			}
		});
	}

	@Override
	public void executeFamiliar(NPC n) {
		n.playAnimation(Animation.create(8190));

	}

	@Override
	public boolean requiresOpponent() {
		return true;
	}

	@Override
	public int requiredSpecial() {
		return 33;
	}

}
