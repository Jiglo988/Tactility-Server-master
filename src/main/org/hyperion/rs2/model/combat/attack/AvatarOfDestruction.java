package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.region.RegionManager;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/23/14
 * Time: 9:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class AvatarOfDestruction implements Attack {

    public static void loadDefinitions() {
        final int[] bonus = new int[10];
        Arrays.fill(bonus, 350);
        NPCDefinition.getDefinitions()[8596] =
                NPCDefinition.create(8596, 1200, 505, bonus, 11199, 11198, new int[]{11197}, 3, "Avatar of Destruction", 120);
    }

    @Override
    public String getName() {
        return "Avatar of Destruction";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int[] npcIds() {
        return new int[]{8596};  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static final int MAX_DAMAGE = 45;

    @Override
    public int handleAttack(NPC n, CombatEntity attack) {
        if(attack == null)
            return 1;
        if(n.cE.predictedAtk > System.currentTimeMillis()) {
            return 6;
        }
        n.getCombat().doAtkEmote();
        for(final Player player : RegionManager.getLocalPlayers(n)) {
            int hitType = Combat.random(1);
            int tempDamage = CombatCalculation.getCalculatedDamage(n, player, Combat.random(MAX_DAMAGE), hitType, MAX_DAMAGE);
            if(player.getPosition().distance(n.getPosition()) == 1)
                tempDamage = 80;
            Combat.npcAttack(n, player.getCombat(), tempDamage, 300, hitType);
            player.getSkills().detractLevel(Skills.PRAYER, tempDamage/5);

        }
        n.cE.predictedAtk = System.currentTimeMillis() + 3000;
        int distance = attack.getEntity().getPosition().distance(n.getPosition());
        if(distance <= 10) {
            return 5;
        }
        return 0;
    }
}
