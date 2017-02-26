package org.hyperion.rs2.model.combat.summoning;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;



public abstract class AbstractSummoningSpecial {
	public abstract int requiredSpecial();
	
	public abstract boolean requiresOpponent();
	
	public abstract int getScrollId();

	public abstract boolean checkRequirements(Player p);

	public abstract void execute(Player p);

	public abstract void executeOpponent(Entity p) throws NullPointerException, ArrayIndexOutOfBoundsException, RuntimeException;

	public abstract void executeFamiliar(NPC n);
}
