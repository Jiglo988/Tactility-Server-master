package org.hyperion.rs2.model.combat.attack;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

public class UnholyCursebearer implements Attack {

    private long specialDelay;
    private int special = 5;

    private static final int MAX_MAGIC_DAMAGE = 40;

    @Override
    public String getName() {
        return "Unholy Cursebearer";
    }

    @Override
    public int[] npcIds() {
        return new int[] {10126};
    }

    private void handleMagicSpell(NPC npc) {
        npc.forceMessage(Combat.random(1) == 0 ? "TASTE THE PAIN OF DEATH!" : "MY MAGIC WILL END ALL!");
        npc.cE.doAnim(13172);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            player.getSkills().setLevel(Skills.PRAYER, (int) (player.getSkills().getLevel(Skills.PRAYER) * .8));
            Combat.npcRangeAttack(npc, player.cE, 2119, 0, false);
            int damage = Combat.random(MAX_MAGIC_DAMAGE);
            Combat.npcAttack(npc, player.cE, CombatCalculation.getCalculatedDamage(npc, player.cE.getEntity(), damage, 2, MAX_MAGIC_DAMAGE), 500, 2);
            player.playGraphics(Graphic.create(305, 500));
        }
    }

    private void handleCurseEffect(NPC npc) {
        npc.forceMessage("I BEAR THE CURSE TO YOU!");
        npc.cE.doAnim(13170);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            int curseGfx = 1103;
            Combat.npcRangeAttack(npc, player.cE, 88, 0, true);
            player.cE.doGfx(curseGfx);
            player.getActionSender().sendMessage("@dre@You have been cursed by the Cursebearer!");
            World.submit(new Task(1000) {
                int curseTicks = Combat.random(2) + 1;
                @Override
                public void execute() {
                    if (curseTicks <= 0) {
                        this.stop();
                        return;
                    }
                    curseTicks--;
                    player.cE.doGfx(curseGfx);
                    player.inflictDamage(new Damage.Hit(Combat.random(9) + 1, Damage.HitType.POISON_DAMAGE, 0));
                }

                @Override
                public void stop() {
                    super.stop();
                    player.getActionSender().sendMessage("The curse has been lifted!");
                }
            });
        }
    }

    @Override
    public int handleAttack(NPC n, CombatEntity attack) {
        if(attack == null) {
            return 1;
        } else if(specialDelay > System.currentTimeMillis()) {
            return 6;
        }
        n.cE.setFreezeTimer(1000000000);
        if (n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 10)) {
            switch (special) {
                case 0:
                    handleCurseEffect(n);
                    break;
                default:
                    handleMagicSpell(n);
                    break;
            }
            if(special <= 0)
                special = Combat.random(4) + 1;
            special--;
            specialDelay = System.currentTimeMillis() + 3000;
            return 5;
        } else {
            return 1;
        }
    }

}