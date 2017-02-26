package org.hyperion.rs2.model.combat.summoning;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.SummoningData;
import org.hyperion.rs2.model.combat.SummoningData.SummonType;
import org.hyperion.rs2.model.combat.summoning.impl.PackYak;
import org.hyperion.rs2.model.combat.summoning.impl.SteelTitanSpecial;
import org.hyperion.rs2.model.combat.summoning.impl.Unicorn;
import org.hyperion.rs2.model.combat.summoning.impl.WolpertingerSpecial;
import org.hyperion.rs2.model.container.BoB;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.skill.Summoning;
import org.hyperion.rs2.model.Animation;

/**
 * @author Wasay
 *         Concrete class to preform summoning specials
 */
public final class SummoningSpecial {
	public static void preformSpecial(Player p, AbstractSummoningSpecial ass) { //didn't see that abbreviation coming
		if(ass != null) {
			if(ass.checkRequirements(p)) {
				performSpecialBlock:{
					if(System.currentTimeMillis() - p.getSummBar().getLast() < SummoningBar.DELAY) {
						p.getActionSender().sendMessage("You need to wait before using another special!");
						break performSpecialBlock;
					}
					if(ass.requiredSpecial() > p.getSummBar().getAmount()) {
						p.getActionSender().sendMessage("You need "+ass.requiredSpecial()+"% special to perform this attack");
						break performSpecialBlock;
					}
					try {
						if(ass.requiresOpponent())
							ass.executeOpponent(p.getCombat().getOpponent().getEntity());
					} catch(NullPointerException e) {
						p.getActionSender().sendMessage("You can't use the special on this opponent!");
						break performSpecialBlock;
					}
					ass.execute(p);
					ass.executeFamiliar(p.getCombat().getFamiliar());
					p.getSummBar().setLast(System.currentTimeMillis());
					p.getSummBar().decrement(ass.requiredSpecial());
					p.getInventory().remove(new Item(ass.getScrollId()));
				}
			}
		} else {
			p.getActionSender().sendMessage("You do not have a familiar with a special attack!");
		}
	}

	/**
	 * @param npcId player's familiar Id
	 * @return proper instance of AbstractSummoningSpecial based on their familiar id - {@link SummonType}
	 */
	public static AbstractSummoningSpecial getCorrectSpecial(int npcId) {
		
		switch(npcId) {
			default:
				return null;
			case 7343:
				return SteelTitanSpecial.getInstance();
			case 6869:
				return WolpertingerSpecial.getInstance();
			case 6823:
				return Unicorn.getInstance();
		}
	}
}
