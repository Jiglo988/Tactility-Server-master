package org.hyperion.rs2.model.combat.attack;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

public class BallakThePummeller implements Attack {

    private long specialDelay;

    private static final int MAX_MELEE_DAMAGE = 60;
    private static final int MAX_MAGIC_DAMAGE = 45;
    private static final int MAX_RANGE_DAMAGE = 51;

    @Override
    public String getName() {
        return "Bal'lak the Pummeller";
    }

    @Override
    public int[] npcIds() {
        return new int[] {10141};
    }

    private void handleFlames(NPC npc) {
        npc.forceMessage("BUUUUUUURRRRRRRRRNNNNNNNN!");
        npc.cE.doAnim(13605);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            int unlucky = Combat.random(1);
            if(unlucky == 0) {
                player.playGraphics(Graphic.create(1393));
                player.forceMessage("OUCH!");
                Combat.npcAttack(npc, player.cE, CombatCalculation.getCalculatedDamage(npc, player.cE.getEntity(), Combat.random(MAX_MAGIC_DAMAGE), 2, MAX_MAGIC_DAMAGE), 1100, 2);
            }
        }
    }

    private void handleHealthSap(NPC npc) {
        npc.forceMessage("GIVE ME LIFE!");
        npc.cE.doAnim(13606);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            player.playGraphics(Graphic.create(336));
            int damage = Combat.random(30);
            Combat.npcRangeAttack(npc, player.cE, 165, 0, false);
            player.inflictDamage(new Damage.Hit(damage, null, 7));
            if(npc.health < npc.maxHealth)
                npc.health += damage / 2;
        }
    }

    private void handleFireSpell(NPC npc) {
        npc.forceMessage("FEEL THE HEAT!");
        npc.cE.doAnim(13604);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            int fireGfx = 1154;
            Combat.npcRangeAttack(npc, player.cE, 88, 0, true);
            Combat.npcAttack(npc, player.cE, CombatCalculation.getCalculatedDamage(npc, player.cE.getEntity(), Combat.random(MAX_RANGE_DAMAGE), 1, MAX_RANGE_DAMAGE), 1500, 1);
            player.cE.doGfx(fireGfx);
            player.getActionSender().sendMessage("@dre@Your body started burning alive!");
            World.submit(new Task(2000) {
                int burnTicks = Combat.random(3) + 2;
                @Override
                public void execute() {
                    if(burnTicks <= 0) {
                        this.stop();
                        return;
                    }
                    burnTicks--;
                    player.cE.doGfx(fireGfx);
                    player.inflictDamage(new Damage.Hit(Combat.random(5) + 5, null, 7));
                }

                @Override
                public void stop() {
                    super.stop();
                    player.getActionSender().sendMessage("You no longer feel yourself burning!");
                }
            });
        }
    }

    @Override
    public int handleAttack(NPC n, CombatEntity attack) {
        if(attack == null) {
            return 1;
        } else if(n.cE.predictedAtk > System.currentTimeMillis()) {
            return 6;
        }
        int distance = attack.getEntity().getPosition().distance(n.getPosition());
        if(specialDelay <= System.currentTimeMillis()) {
            int special = Combat.random(2);
            switch (special) {
                case 0:
                    handleFlames(n);
                    break;
                case 1:
                    handleHealthSap(n);
                    break;
                case 2:
                    handleFireSpell(n);
                    break;
            }
            specialDelay = System.currentTimeMillis() + 10000;
            n.cE.predictedAtk = System.currentTimeMillis() + 3000;
            return 5;
        }
        if (n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 2)) {
            n.getCombat().doAtkEmote();
            Combat.npcAttack(n, attack, CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(MAX_MELEE_DAMAGE), 0, MAX_MELEE_DAMAGE), 200, 0);
            n.cE.predictedAtk = System.currentTimeMillis() + 2500;
            return 5;
        } else if(distance <= 8) {
            return 0;
        } else {
            return 1;
        }
    }

}
