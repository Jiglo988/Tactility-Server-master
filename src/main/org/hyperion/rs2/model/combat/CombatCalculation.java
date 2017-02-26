package org.hyperion.rs2.model.combat;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.util.Misc;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/23/14
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public enum CombatCalculation {
    NPC_VS_PLAYER {
        @Override
        int magicAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final NPC attack = (NPC)attacker;
            final Player def = (Player)defender;
            final int deltaBonus = attack.getDefinition().getBonus()[3] - CombatAssistant.calculateMageDef(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int rangeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final NPC attack = (NPC)attacker;
            final Player def = (Player)defender;
            final int deltaBonus = (int)(attack.getDefinition().getBonus()[4] * 0.65) - CombatAssistant.calculateRangeDefence(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int meleeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final NPC attack = (NPC)attacker;
            final Player def = (Player)defender;
            final int deltaBonus = (int)(attack.getDefinition().combat() * .65) - CombatAssistant.calculateMeleeDefence(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
    },
    NPC_VS_NPC {

        @Override
        int magicAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final NPC attack = (NPC)attacker;
            final NPC def = (NPC)defender;
            final int deltaBonus = attack.getDefinition().getBonus()[3] - def.getDefinition().getBonus()[8];
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int rangeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final NPC attack = (NPC)attacker;
            final NPC def = (NPC)defender;
            final int deltaBonus = attack.getDefinition().combat() - def.getDefinition().combat();
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int meleeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final NPC attack = (NPC)attacker;
            final NPC def = (NPC)defender;
            final int deltaBonus = attack.getDefinition().combat()/2 - CombatAssistant.calculateMeleeDefence(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }

    },
    PLAYER_VS_NPC {

        @Override
        int magicAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final Player attack = (Player)attacker;
            final NPC def = (NPC)defender;
            final int deltaBonus = CombatAssistant.calculateMageAtk(attack) - (int)(def.getDefinition().getBonus()[8]);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int rangeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final Player attack = (Player)attacker;
            final NPC def = (NPC)defender;
            final int deltaBonus = (int)(CombatAssistant.calculateRangeAttack(attack)) - CombatAssistant.calculateRangeDefence(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int meleeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final Player attack = (Player)attacker;
            final NPC def = (NPC)defender;
            final int deltaBonus = CombatAssistant.calculateMeleeAttack(attack) - CombatAssistant.calculateMeleeDefence(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }

    },
    PLAYER_VS_PLAYER {

        @Override
        int magicAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final Player attack = (Player)attacker;
            final Player def = (Player)defender;
            final int deltaBonus = CombatAssistant.calculateMageAtk(attack) - CombatAssistant.calculateMageDef(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int rangeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final Player attack = (Player)attacker;
            final Player def = (Player)defender;
            final int deltaBonus = CombatAssistant.calculateRangeAttack(attack) - CombatAssistant.calculateRangeDefence(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }
        @Override
        int meleeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
            final Player attack = (Player)attacker;
            final Player def = (Player)defender;
            final int deltaBonus = CombatAssistant.calculateMeleeAttack(attack) - CombatAssistant.calculateMeleeDefence(def);
            randomDamage += Misc.random(deltaBonus/3);
            return randomDamage > maxDamage ? maxDamage : randomDamage < 0 ? 0 : randomDamage;
        }

    };

    int magicAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
        throw new AbstractMethodError("Will not occur");
    }

    int rangeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
        throw new AbstractMethodError("Will not occur");
    }

    int meleeAttack(final Entity attacker, final Entity defender, int randomDamage, final int maxDamage) {
        throw new AbstractMethodError("Will not occur");
    }

    public static int getCalculatedDamage(final Entity attacker, final Entity defender, int randomDamage, int type, int maxDamage) {
        try {
            if(attacker == null || defender == null)
                return 0;
            final CombatCalculation calculation = getCalculationFor(attacker, defender);
            if (type == Constants.MAGE)
                return calculation.magicAttack(attacker, defender, randomDamage, maxDamage);
            else if(type == Constants.RANGE)
                return calculation.rangeAttack(attacker, defender, randomDamage, maxDamage);
            else
                return calculation.meleeAttack(attacker, defender, randomDamage, maxDamage);
        }catch(Exception e) {
            return 0;
        }
    }

    private static CombatCalculation getCalculationFor(final Entity attacker, final Entity defender) {
        if(attacker instanceof NPC) {
            if(defender instanceof Player) {
                return NPC_VS_PLAYER;
            } else {
                return NPC_VS_NPC;
            }
        } else {
            if(defender instanceof Player) {
                return PLAYER_VS_PLAYER;
            } else {
                return PLAYER_VS_NPC;
            }
        }
    }





}
