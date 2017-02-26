package org.hyperion.rs2.model;

import org.hyperion.rs2.model.combat.CombatEntity;

public interface Attack {

	public String getName();

	public int[] npcIds();

	public int handleAttack(NPC n, CombatEntity attack);

}