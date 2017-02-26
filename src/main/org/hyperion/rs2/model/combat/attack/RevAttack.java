package org.hyperion.rs2.model.combat.attack;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatCalculation;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.combat.pvp.PvPArmourStorage;
import org.hyperion.util.ArrayUtils;
import org.hyperion.util.Misc;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/10/14
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RevAttack implements Attack {

    public String getName() {
        return "";
    }

    static List<NPCDrop> drops = new ArrayList<>();

    private static final Map<Integer, NPCDefinition> revs;
    private static final int[] revIds;

    static {
        final int[] bonus = new int[10];
        Arrays.fill(bonus, 400);
        revs = new HashMap<>();
        int id = 6692;
        revs.put(id, NPCDefinition.create(id--, 500, 126, bonus, 7442, 7443, new int[]{7441, 7508, 7522}, 1, "Revenant Knight", 50));
        revs.put(id, NPCDefinition.create(id--, 500, 120, bonus, 7468, 7469, new int[]{7467, 7515, 7514}, 1, "Revenant Dark Beast", 49));
        revs.put(id, NPCDefinition.create(id--, 450, 105, bonus, 7412, 7413, new int[]{7411, 7505, 7518}, 2, "Revenant Ork", 48));
        revs.put(id, NPCDefinition.create(id--, 420, 98, bonus, 7475, 7476, new int[]{7474, 7498, 7512}, 2, "Revenant Demon", 47));
        revs.put(id, NPCDefinition.create(id--, 410, 90, bonus, 7461, 7462, new int[]{7460, 7515, 7501}, 2, "Revenant Hellhound", 45));
        revIds = ArrayUtils.fromInteger(revs.keySet().toArray(new Integer[revs.keySet().size()]));

        for (final NPCDefinition def : revs.values()) {
            if (def != null) {
                for (final int i : PvPArmourStorage.getArmours())
                    def.getDrops().add(NPCDrop.create(i, 1, 1, (int) (Math.pow(def.combat(), 1.9) / 1000)));

            }
        }
    }

    public static int[] getRevs() {
        return revIds;
    }

    public static boolean isRev(final int id) {
        return ArrayUtils.contains(id, revIds);
    }

    public static NPCDefinition loadDefinition(final int id) {
        return revs.get(id);
    }


    @Override
    public int[] npcIds() {
        return revIds;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int handleAttack(NPC n, CombatEntity attack) {
        if (n.cE.predictedAtk > System.currentTimeMillis()) {
            //System.out.println("Predicted attack waiting.");
            return 6;//we dont want to reset attack but just wait another 500ms or so...
        }
        final int distance = n.getPosition().distance(attack.getEntity().getPosition());
        if (Misc.random(5) == 1 && n.health < n.maxHealth / 2) {
            if (attack._getPlayer().isPresent() && attack.getPlayer().getDungeoneering().inDungeon())
                return 5;
            n.health += 18;
            n.cE.predictedAtk = System.currentTimeMillis() + 2400;
            return 5;
        }

        boolean hasPrayMagic = false;
        boolean hasPrayMelee = false;
        boolean hasPrayRange = false;

        final Entity entity = attack.getEntity();

        if (attack.getEntity() instanceof Player) {

            final Player player = attack.getPlayer();
            if (player == null)
                return 1;
            hasPrayMagic = player.getPrayers().isEnabled(Prayers.CURSE_DEFLECT_MAGIC) || player.getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_MAGE);
            hasPrayMelee = player.getPrayers().isEnabled(Prayers.CURSE_DEFLECT_MELEE) || player.getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_MELEE);
            hasPrayRange = player.getPrayers().isEnabled(Prayers.CURSE_DEFLECT_RANGED) || player.getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_RANGE);

            if (!player.isSkulled() && player.cE.getOpponent() != null && player.cE.getOpponent().equals(n.cE)) {
                player.setSkulled(true);
            }
        }

        if (distance > 10) {
            n.cE.setOpponent(null);
            return 1;
        } else if (distance > 8)
            return 0;
        else if (distance > 2) {
            if (hasPrayMagic)
                handleRangeAttack(n, entity);
            else if (hasPrayRange)
                handleMagicAttack(n, entity);
            else {
                if (Misc.random(1) == 0)
                    handleRangeAttack(n, entity);
                else
                    handleMagicAttack(n, entity);

            }
        } else {
            if (hasPrayMelee) {
                if (Misc.random(1) == 0) {
                    handleRangeAttack(n, entity);
                } else {
                    handleMagicAttack(n, entity);
                }
            } else if (hasPrayMagic) {
                if (Misc.random(1) == 0) {
                    handleRangeAttack(n, entity);
                } else {
                    handleMeleeAttack(n, entity);
                }
            } else {
                switch (Misc.random(2)) {
                    case Constants.MELEE:
                        handleMeleeAttack(n, entity);
                        break;
                    case Constants.RANGE:
                        handleRangeAttack(n, entity);
                        break;
                    case Constants.MAGE:
                        handleMagicAttack(n, entity);
                        break;
                }
            }
        }
        return 5;
    }

    public void handleMagicAttack(NPC n, Entity attack) {
        n.cE.doAnim(n.getDefinition().getAtkEmote(1));
        final int maxHit = n.getDefinition().combat() / 5;
        int damage = CombatCalculation.getCalculatedDamage(n, attack, Misc.random(maxHit), Constants.MAGE, maxHit);
        //attack.cE.hit(Combat.random(maxHit), n, false, Constants.MAGE);
        if (Misc.random(8) == 1 && attack.cE.canBeFrozen()) {
            attack.cE.doGfx(1279);
            attack.cE.setFreezeTimer(10000);
            if (attack instanceof Player)
                ((Player) attack).sendMessage("You have been frozen!");
        }
        final int distance = attack.getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));

        Combat.npcAttack(n, attack.getCombat(), damage, 300 + distance * 200, Constants.MAGE);
        Combat.npcRangeAttack(n, attack.getCombat(), 1276, 35, false);

        n.cE.predictedAtk = System.currentTimeMillis() + 2400L;

    }

    public void handleRangeAttack(NPC n, Entity attack) {

        n.cE.doAnim(n.getDefinition().getAtkEmote(2));
        final int maxHit = n.getDefinition().combat() / 4;
        int damage = CombatCalculation.getCalculatedDamage(n, attack, Misc.random(maxHit), Constants.RANGE, maxHit);

        final int distance = attack.getPosition().distance((Position.create(n.cE.getEntity().getPosition().getX() + n.cE.getOffsetX(), n.cE.getEntity().getPosition().getY() + n.cE.getOffsetY(), n.cE.getEntity().getPosition().getZ())));

        Combat.npcAttack(n, attack.getCombat(), damage, 300 + distance * 200, Constants.RANGE);
        Combat.npcRangeAttack(n, attack.getCombat(), 1278, 35, false);

        n.cE.predictedAtk = System.currentTimeMillis() + 2400L;

    }

    public void handleMeleeAttack(NPC n, Entity attack) {
        n.cE.doAnim(n.getDefinition().getAtkEmote(0));
        final int maxHit = n.getDefinition().combat() / 6;
        int damage = CombatCalculation.getCalculatedDamage(n, attack, Misc.random(maxHit), Constants.MELEE, maxHit);
        Combat.npcAttack(n, attack.getCombat(), damage, 300, Constants.MELEE);
        n.cE.predictedAtk = System.currentTimeMillis() + 1800L;
    }
}
