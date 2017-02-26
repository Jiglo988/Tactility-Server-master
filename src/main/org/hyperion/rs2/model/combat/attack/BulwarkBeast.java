package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

public class BulwarkBeast implements Attack {

    private long specialDelay;

    private static final int MAX_MELEE_DAMAGE = 65;
    private static final int MAX_QUAKE_DAMAGE = 30;

    @Override
    public String getName() {
        return "Bulwark Beast";
    }

    @Override
    public int[] npcIds() {
        return new int[] {10106};
    }

    public static void handleRecoil(Player player, int damage) {
        if(damage > 2) {
            player.inflictDamage(new Damage.Hit((int) (damage * .34), Damage.HitType.NORMAL_DAMAGE, 0));
            player.playGraphics(Graphic.create(2101));
        }
    }

    private void handleQuakeEffect(NPC npc) {
        npc.forceMessage("GGUUUUUUUAAAAAAAAHHHHHHHH!");
        npc.cE.doAnim(13003);
        for(Player player : RegionManager.getLocalPlayers(npc)) {
            int type = Combat.random(1) + 1;
            Combat.npcAttack(npc, player.cE, CombatCalculation.getCalculatedDamage(npc, player, Combat.random(MAX_QUAKE_DAMAGE), type, MAX_QUAKE_DAMAGE), 2000, type);
            type = Combat.random(1) + 1;
            Combat.npcAttack(npc, player.cE, CombatCalculation.getCalculatedDamage(npc, player, Combat.random(MAX_QUAKE_DAMAGE), type, MAX_QUAKE_DAMAGE), 2000, type);
            player.playGraphics(Graphic.create(2149, 1000));
            player.getActionSender().shakeScreen(3, 12, 25, 12, 2000);
            player.forceMessage(type == 1 ? "Is this an earthquake?" : "What is this shaking?");
        }
    }

    private void handleHealthEffect(NPC npc) {
        npc.forceMessage("YUUUUUMMMMMMM!");
        npc.cE.doAnim(13002);
        int healthToAdd = Combat.random(25 * RegionManager.getLocalPlayers(npc).size()) + 25;
        if(npc.health + healthToAdd > npc.maxHealth) {
            npc.health = npc.maxHealth;
        } else {
            npc.health += healthToAdd;
        }
        npc.cE.doGfx(2544);
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
            switch(special) {
                case 0:
                    handleHealthEffect(n);
                    break;
                case 1:
                    handleQuakeEffect(n);
                    break;
            }
            specialDelay = System.currentTimeMillis() + Combat.random(10000) + 10000;
            n.cE.predictedAtk = System.currentTimeMillis() + 3000;
            return 5;
        }
        int distance = attack.getEntity().getPosition().distance(n.getPosition());
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
