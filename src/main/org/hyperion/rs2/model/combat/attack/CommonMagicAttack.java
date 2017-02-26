package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.model.Attack;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.util.Misc;

public class CommonMagicAttack implements Attack {

	public String getName() {
		return "CommonMagicAttack";
	}

	public int handleAttack(final NPC n, final CombatEntity attack) {
		int distance = attack.getEntity().getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));
		if(distance < (8 + ((n.getDefinition().sizeX() + n.getDefinition().sizeY()) / 2))) {
            final int type = Misc.random(2);
			Combat.npcAttack(n, attack, CombatCalculation.getCalculatedDamage(n, attack.getEntity(), Misc.random(28), type, 28), 1000, type);
			if(Misc.random(3) == 0 && attack.getEntity() instanceof Player) {
				handleEffect(attack.getPlayer());
			}
            n.cE.predictedAtk = System.currentTimeMillis() + 2400;
			return 5;
		} else if(n.getPosition().isWithinDistance(n.cE.getOpponent().getEntity().getPosition(), 14)) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int[] npcIds() {
		int[] j = {3200,};
		return j;
	}
	
	public void handleEffect(final Player player) {

	}
}
