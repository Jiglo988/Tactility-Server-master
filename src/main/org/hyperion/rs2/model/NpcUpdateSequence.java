package org.hyperion.rs2.model;

import org.hyperion.rs2.model.combat.Combat;

/**
 * Created by Gilles on 12/02/2016.
 */
public class NpcUpdateSequence implements UpdateSequence<NPC> {
    @Override
    public void executePreUpdate(NPC npc) {
        if(npc != null) {
            if(npc.isMapRegionChanging()) {
                npc.setLastKnownRegion(npc.getPosition());
            }

            if(npc.getWalkingQueue() != null)
                npc.getWalkingQueue().processNextMovement();

            try {
                if(npc.cE.getOpponent() != null) {
                    if(!Combat.processCombat(npc.cE))
                        Combat.resetAttack(npc.cE);
                } else if(! npc.isDead())
                    NPC.randomWalk(npc);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void executeUpdate(NPC npc) {
        throw new UnsupportedOperationException("NPC's cannot be updated.");
    }

    @Override
    public void executePostUpdate(NPC npc) {
        if(npc.getUpdateFlags().get(UpdateFlags.UpdateFlag.HIT_3)) {
            npc.getUpdateFlags().reset();
            npc.getDamage().setHit1(npc.getDamage().getHit3());
            npc.getUpdateFlags().flag(UpdateFlags.UpdateFlag.HIT);
        } else
            npc.getUpdateFlags().reset();
        if(npc.cE != null)
            npc.cE.isDoingAtk = false;
        npc.setTeleporting(false);
        npc.reset();
    }
}
