package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.util.Time;

import java.util.Arrays;

public class WildernessBossTask extends Task {

    public static NPC currentBoss;
    public static long timeStart;

    public static final int NECKLACE_ID = 19888;
    public static final int RING_ID = 20054;

    private static final Position[] SPAWN_POINTS = { Position.create(3064, 3774, 0),
            Position.create(3144, 3778, 0), Position.create(3255, 3884, 0),
            Position.create(3133, 3846, 0), Position.create(3006, 3825, 0)
    };

    private static final int[] BOSS_IDS = {10141, 9752, 10106, 10126};


    /**
     * Respawn time in minutes.
     */
    private static final int RESPAWN_TIME = 30;

    public static final long DELAY_FOR_RESPAWN = Time.THIRTY_MINUTES;

    /**
     * @param forceSpawn used so when the server is restarted, the boss will spawn immediately
     * instead of 30 minutes after the restart.
     */
    public WildernessBossTask(boolean forceSpawn) {
        super(forceSpawn ? 0 : DELAY_FOR_RESPAWN);
        timeStart = System.currentTimeMillis();
    }

    public static void init() {
        int index = 0;
        int[] bonus = new int[10];
        Arrays.fill(bonus, 365);
        NPCDefinition.getDefinitions()[BOSS_IDS[index]] =
                NPCDefinition.create(BOSS_IDS[index++], 1025, 425, bonus, 13602, 13601, new int[]{13603}, 2, "Bal'lak the Pummeller", -1);
        Arrays.fill(bonus, 370);
        NPCDefinition.getDefinitions()[BOSS_IDS[index]] =
                NPCDefinition.create(BOSS_IDS[index++], 1050, 420, bonus, 13424, 13420, new int[]{13430}, 2, "Night-gazer Khighorahk", -1);
        Arrays.fill(bonus, 380);
        NPCDefinition.getDefinitions()[BOSS_IDS[index]] =
                NPCDefinition.create(BOSS_IDS[index++], 1100, 430, bonus, 13005, 13000, new int[]{13001}, 4, "Bulwark Beast", -1);
        Arrays.fill(bonus, 365);
        NPCDefinition.getDefinitions()[BOSS_IDS[index]] =
                NPCDefinition.create(BOSS_IDS[index], 1075, 400, bonus, 13171, 13167, new int[]{13170}, 2, "Unholy Cursebearer", -1);
    }

    public static boolean isWildernessBoss(int npcId) {
        for(int id : BOSS_IDS)
            if(id == npcId)
                return true;
        return false;
    }

    @Override
    public void execute() {
        if(currentBoss == null) {
            final int spawn = Combat.random(SPAWN_POINTS.length - 1);
            final int boss = Combat.random(BOSS_IDS.length - 1);
            currentBoss = NPCManager.addNPC(SPAWN_POINTS[spawn].getX(), SPAWN_POINTS[spawn].getY(), SPAWN_POINTS[spawn].getZ(), BOSS_IDS[boss], -1);


            World.getPlayers().forEach(p -> p.sendServerMessage(currentBoss.getDefinition().getName() + " has been summoned!"));
        }
        this.stop();
    }

}
