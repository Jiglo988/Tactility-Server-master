package org.hyperion.rs2.model.content.misc2.teamboss.impl;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.content.misc2.teamboss.TeamBossSession;
import org.hyperion.rs2.model.content.specialareas.SpecialArea;
import org.hyperion.util.Misc;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/20/14
 * Time: 1:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class SkeletalTrioSession extends TeamBossSession {

    private static final int LOC_X = 0, LOC_Y = 0;
    public static final SpecialArea AREA = new SkeletalTrioArea();

    public SkeletalTrioSession(final Player[] players) {
        super(LOC_X, LOC_Y, new NPCDrop[] {
                //rewards
        },
        new NPC[] {
                NPCManager.addNPC(Position.create(LOC_X, LOC_Y, players[0].getIndex() * 4), 11255, -1),
                NPCManager.addNPC(Position.create(LOC_X, LOC_Y, players[0].getIndex() * 4), 11254, -1),
                NPCManager.addNPC(Position.create(LOC_X, LOC_Y, players[0].getIndex() * 4), 11253, -1)
        },
        players);
    }


    @Override
    public void handleReward() {
        final int distribution = players.size();
        for(Player p : players) {
            for(final NPCDrop drop : rewards) {
                if(Misc.random(100 * distribution) <= drop.getChance()) {
                    //drop item here
                }
            }
        }
    }

    @Override
    public SpecialArea getArea() {
        return AREA;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private static class SkeletalTrioArea extends SpecialArea {

        @Override
        public boolean inArea(int x, int y, int z) {
            return false;
        }

        @Override
        public String canEnter(Player player) {
            return "";
        }

        @Override
        public boolean isPkArea() {
            return false;
        }

        @Override
        public Position getDefaultLocation() {
            return Edgeville.POSITION;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean canSpawn() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}
