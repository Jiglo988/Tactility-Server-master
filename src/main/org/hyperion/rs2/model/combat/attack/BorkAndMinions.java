package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.NPCDefinition;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 6/6/15
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class BorkAndMinions implements Attack {

    public static final int BORK_ID = 7134;
    public static final int MINION_ID = 7135;

    public static void init() {
        final int[] bonus = new int[10];
        Arrays.fill(bonus, 330);
        NPCDefinition.getDefinitions()[BORK_ID] =
                NPCDefinition.create(BORK_ID, 600, 425, bonus, 8756, 8755, new int[]{8754}, 3, "Bork", 120);
        Arrays.fill(bonus, 175);
        NPCDefinition.getDefinitions()[MINION_ID] =
                NPCDefinition.create(MINION_ID, 155, 125, bonus, 8761, 8762, new int[]{8760}, 2, "Borklets", 120);
    }
    @Override
    public String getName() {
        return "Bork";
    }

    @Override
    public int[] npcIds() {
        return new int[]{BORK_ID,MINION_ID};  //To change body of implemented methods use File | Settings | File Templates.
    }

    private static int MAX_BORK_DAMAGE = 45;

    private static int MAX_MINION_DAMAGE = 27;

    @Override
    public int handleAttack(NPC n, CombatEntity attack) {
        if(attack == null)
            return 1;
        if(n.cE.predictedAtk > System.currentTimeMillis()) {
            return 6;
        }
        n.getCombat().doAtkEmote();
        int tempDamage;

        int distance = attack.getEntity().getPosition().distance(n.getPosition());
        if(n.getDefinition().getId() == BORK_ID) {
            if(distance > 2)
                return 0;
            tempDamage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(MAX_BORK_DAMAGE), Constants.MELEE, MAX_BORK_DAMAGE);
            Combat.npcAttack(n, attack, tempDamage, 300, Constants.MELEE);
            n.cE.predictedAtk = System.currentTimeMillis() + 3000;
        } else {
            if(distance > 5)
                return 0;
            tempDamage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(MAX_MINION_DAMAGE), Constants.RANGE, MAX_MINION_DAMAGE);
            Combat.npcAttack(n, attack, tempDamage, 1200, Constants.RANGE);
            n.cE.predictedAtk = System.currentTimeMillis() + 2500;
        }

        if(distance <= 10) {
            return 5;
        }
        return 0;
    }
}
