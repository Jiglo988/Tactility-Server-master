package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.combat.attack.CorporealBeast;
import org.hyperion.rs2.model.region.RegionManager;

/**
 * Handles all events related to combat.
 *
 * @author Martin
 */
public class NpcCombatTask extends Task {
	//TODO TRANSFORM THIS EVENT TO NPC UPDATING
	/**
	 * The cycle time, in milliseconds.
	 */
	public static final long CYCLE_TIME = 600;//

	/**
	 * Creates the update event to cycle every 600 milliseconds.
	 */
	public NpcCombatTask() {
		super(CYCLE_TIME);
	}

	public static long lastTimeDid = System.currentTimeMillis();

	@Override
	public void execute() {
        final long startTime = System.currentTimeMillis();
		NpcCombatTask.aggressiveNPCS();
		for(NPC npc : World.getNpcs()) {
		}
        long deltaMs = System.currentTimeMillis() - startTime;
        //corpHeal();
        if(deltaMs > 50)
            System.err.println("[NPC COMBAT EVENT] took: "+(deltaMs) + "ms");


    }

	public static void corpHeal() {
		boolean willHeal = true;
		for(NPC npc : World.getNpcs()) {
			try{
				if(npc.getDefinition().getId() == 8133) {
					for(Player p : RegionManager.getLocalPlayers(npc)) {
						if(p != null) {
						CombatEntity combatEntity = p.getCombat();
						if(combatEntity.getAbsX() >= 2505 && combatEntity.getAbsY() >= 4630 &&
								combatEntity.getAbsX() <= 2536 && combatEntity.getAbsY() <= 4658) {
							if(p.getPosition().getY() <= 4636 || p.getPosition().getY() >= 4655) {
								CorporealBeast.stomp(npc, p.cE, true);
							}
							willHeal = false;
						}
					}
					}
					if(willHeal) {
						npc.health = npc.maxHealth;
						Player.resetCorpDamage();
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void aggressiveNPCS() {
		for(NPC npc : World.getNpcs()) {
			try {
                if(npc.ownerId < 1 && npc.agressiveDis < 1 && Combat.getWildLevel(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ()) > 20)
                    npc.agressiveDis = 3;
				if(npc.agressiveDis > 0 && npc.getCombat().getOpponent() == null) {
					//complicated agressecode used for all players
					int dis = 1000;
					Player player2 = null;
					for(Player player4 : RegionManager.getLocalPlayers(npc)) {
						if(player4 != null && player4.getPosition().distance(npc.getPosition()) < dis && player4.getPosition().distance(npc.getPosition()) < npc.agressiveDis) {
							dis = player4.getPosition().distance(npc.getPosition());
							player2 = player4;
						}
					}
					if(player2 != null) {
						npc.cE.setOpponent(player2.cE);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
