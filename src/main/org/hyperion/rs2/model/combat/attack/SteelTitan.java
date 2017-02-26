package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.combat.Constants;

public class SteelTitan implements Attack {

	@Override
	public String getName() {
		return "Steel Titan";
	}

	@Override
	public int[] npcIds() {
		return new int[] {7343, 7344};
	}

    private static final int MAX_MELEE_DAMG = 25, MAX_RANGE_DAMG = 18;

	@Override
	public int handleAttack(NPC n, CombatEntity attack) {
		int distance = n.getPosition().distance(attack.getEntity().getPosition());
		if(n.cE.predictedAtk > System.currentTimeMillis())
			return 6;
		attack.doAtkEmote();

		n.cE.predictedAtk = System.currentTimeMillis() + 2000;
		if(distance > 2 && distance < 8) {
            int tempDamage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(MAX_RANGE_DAMG), Constants.RANGE, MAX_RANGE_DAMG);
            Combat.npcAttack(n, attack, tempDamage, 1700, Constants.RANGE);
		} else if (distance < 8) {
            int tempDamage = CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Combat.random(MAX_MELEE_DAMG), Constants.MELEE, MAX_MELEE_DAMG);
            Combat.npcAttack(n, attack, tempDamage, 700, Constants.MELEE);
		} else if (distance < 11) {
            return 0;
        } else if (distance >= 11) {
            return 1;
        }
		return 5;
	}

}
