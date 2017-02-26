package org.hyperion.rs2.model.combat;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.util.Misc;

/**
 * @author DrHales
 */
public class SpiritShields {

    public static int applyEffects(final CombatEntity entity, final int value) {
        if (entity == null) {
            return 0;
        }
        if (!(entity.getEntity() instanceof Player)) {
            return value;
        }
        final Shields shield = Shields.getShield(CombatAssistant.getShieldId(entity.getPlayer().getEquipment()));
        if (shield == null) {
            return value;
        }
        return shield.value(entity, value);
    }

    private enum Shields {
        DIVINE_SPIRIT_SHIELD {
            @Override
            public int value(final CombatEntity entity, final int value) {
                if (entity.getPlayer().getSkills().getLevel(Skills.PRAYER) > 0) {
                    entity.getPlayer().getSkills().detractLevel(Skills.PRAYER, (int) (value * 0.25));
                    return (int) (value * 0.75);
                }
                return value;
            }
        },
        ELYSIAN_SPIRIT_SHIELD {
            @Override
            public int value(final CombatEntity entity, final int value) {
                return Misc.random(9) <= 6 ? (int) (value * 0.75) : value;
            }
        },
        RED_DRAGON_KITESHIELD {
            @Override
            public int value(final CombatEntity entity, final int value) {
                return (int) (value * 0.92);
            }
        };

        public static Shields getShield(final int value) {
            return value == 13740
                    ? DIVINE_SPIRIT_SHIELD : value == 13742
                    ? ELYSIAN_SPIRIT_SHIELD : value == 18739
                    ? RED_DRAGON_KITESHIELD : null;
        }

        public abstract int value(CombatEntity entity, int value);
    }
}
