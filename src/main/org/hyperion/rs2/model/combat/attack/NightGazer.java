package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

public class NightGazer implements Attack {

    private long specialDelay;

    private static final int MAX_MAGIC_DAMAGE = 30;
    private static final int MAX_RANGE_DAMAGE = 66;
    private static final int MAX_SHARD_DAMAGE = 58;

    @Override
    public String getName() {
        return "Night-gazer Khighorahk";
    }

    @Override
    public int[] npcIds() {
        return new int[] {9752};
    }

    private void handleIceSpecial(NPC npc) {
        npc.forceMessage("ISN'T IT GETTING A BIT... COLD?");
        npc.cE.doAnim(13429);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            int damage = Combat.random(MAX_MAGIC_DAMAGE) + 5;
            player.cE.setFreezeTimer(5000);
            player.getActionSender().sendMessage("You have been frozen and weakened!");
            for (int i = 0; i <= 6; i++) {
                if (i == 5)
                    continue;player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * .9));
            }
            player.playGraphics(Graphic.create(2545));
            player.inflictDamage(new Damage.Hit(damage, null, 7));
        }
    }

    private void handleShardSpecial(NPC npc) {
        npc.forceMessage("TRY AND DODGE THIS!");
        npc.cE.doAnim(13428);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            int skill = Combat.random(6);
            if(skill == 3 || skill == 5) {
                return;
            } else {
                player.getSkills().setLevel(skill, (int) (player.getSkills().getLevel(skill) * .67));
                player.getActionSender().sendMessage("@dre@You feel that one of your combat abilities have been severely weakened!");
            }
            int type = Combat.random(1) + 1;
            Combat.npcRangeAttack(npc, player.cE, type == 1 ? 977 : 979, 0, true);
            Combat.npcAttack(npc, player.cE, CombatCalculation.getCalculatedDamage(npc, player, Combat.random(MAX_SHARD_DAMAGE), type, MAX_SHARD_DAMAGE), 2000, type);
            player.playGraphics(Graphic.create(197, 2000));
        }
    }

    @Override
    public int handleAttack(NPC n, CombatEntity attack) {
        if(attack == null) {
            return 1;
        } else if(n.cE.predictedAtk > System.currentTimeMillis()) {
            return 6;
        }
        if(specialDelay <= System.currentTimeMillis()) {
            int special = Combat.random(1);
            switch (special) {
                case 0:
                    handleIceSpecial(n);
                    break;
                case 1:
                    handleShardSpecial(n);
                    break;
            }
            specialDelay = System.currentTimeMillis() + 10000;
            n.cE.predictedAtk = System.currentTimeMillis() + 3000;
            return 5;
        }
        int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
        if(distance < (10 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
            n.getCombat().doAtkEmote();
            Combat.npcRangeAttack(n, attack, 1067, 0, false);
            Combat.npcAttack(n, attack, CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(MAX_RANGE_DAMAGE), 1, MAX_RANGE_DAMAGE), 1000, 1);
            n.cE.predictedAtk = System.currentTimeMillis() + 2500;
            return 5;
        }
        distance = attack.getEntity().getPosition().distance(n.getPosition());
        if(distance <= 8) {
            return 0;
        }
        return 1;
    }

}
