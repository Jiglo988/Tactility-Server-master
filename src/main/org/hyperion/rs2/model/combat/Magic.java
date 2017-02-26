package org.hyperion.rs2.model.combat;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.impl.WildernessBossTask;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.bounty.BountyPerkHandler;
import org.hyperion.rs2.model.content.minigame.DangerousPK;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.skill.Prayer;
import org.hyperion.rs2.model.content.skill.dungoneering.Room;
import org.hyperion.rs2.model.content.skill.slayer.SlayerTask;
import org.hyperion.rs2.model.content.specialareas.SpecialAreaHolder;
import org.hyperion.rs2.model.shops.SlayerShop;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Magic Class
 *
 * @author Martin credits to jonas++ for magic value arrays from shard evolution
 *         Martin - wrote all the methods and etc.
 */

public class Magic {

	private static HashMap<Integer, Spell> map = new HashMap<Integer, Spell>();

	private static final int EXPMULTIPLIER = 2 * Constants.XPRATE;

	public static final int[] AUTOCAST_IDS = {/* normal */1830, 1831, 1832, 1833,
			1834, 1835, 1836, 1837, 1838, 1839, 1840, 1841, 1842, 1843, 1844,
			1845,/* ancients */13189, 13241, 13147, 6162, 13215, 13267, 13167,
			13125, 13202, 13254, 13158, 13114, 13228, 13280, 13178, 13136,};

	public static final int[] AUTO_MAGIC_IDS = {1152, 1154, 1156, 1158, 1160,
			1163, 1166, 1169, 1172, 1175, 1177, 1181, 1183, 1185, 1188, 1189,
			12939, 12987, 12901, 12861, 12963, 13011, 12919, 12881, 12951,
			12999, 12911, 12871, 12975, 13023, 12929, 12891};


	static {
		map.put(1152, new Spell(1, 1152, 6, 2, 711, 90, 91, 92, 0, 556, 1, 558, 1, 0, 0, 0, 0, 0, 0, 0, 0, false));
		map.put(1153, new Spell(3, 1153, 13, - 1, 716, 102, 103, 104, 0, 555, 3, 557, 2, 559, 1, 0, 0, 0, 0, 5, 0, false));
		map.put(1154, new Spell(5, 1154, 8, 4, 711, 93, 94, 95, 0, 556, 1, 558, 1, 555, 1, 0, 0, 0, 0, 0, 0, false));
		map.put(1156, new Spell(9, 1156, 10, 6, 711, 96, 97, 98, 0, 556, 1, 558, 1, 557, 2, 0, 0, 0, 0, 0, 0, false));
		map.put(1157, new Spell(11, 1157, 21, - 1, 729, 105, 106, 107, 0, 555, 3, 557, 2, 559, 1, 0, 0, 0, 0, 5, 0, false));
		map.put(1158, new Spell(13, 1158, 12, 8, 711, 99, 100, 101, 0, 556, 2, 558, 1, 554, 3, 0, 0, 0, 0, 0, 0, false));
		map.put(1160, new Spell(17, 1160, 14, 9, 711, 117, 118, 119, 0, 556, 2, 562, 1, 0, 0, 0, 0, 0, 0, 0, 0, false));
		map.put(1161, new Spell(19, 1161, 29, - 1, 716, 108, 109, 110, 0, 555, 2, 557, 3, 559, 1, 0, 0, 0, 0, 0, 0, false));
		map.put(1572, new Spell(20, 1572, 30, 1, 711, 177, 178, 179, 0, 555, 4, 557, 4, 561, 3, 0, 0, 5, 0, 0, 0, false));
		map.put(1163, new Spell(23, 1163, 17, 10, 711, 120, 121, 122, 0, 556, 2, 562, 1, 555, 2, 0, 0, 0, 0, 0, 0, false));
		map.put(1166, new Spell(29, 1166, 20, 11, 711, 123, 124, 125, 0, 556, 2, 562, 1, 557, 3, 0, 0, 0, 0, 0, 0, false));
		map.put(1169, new Spell(35, 1169, 23, 12, 711, 126, 127, 128, 0, 556, 3, 562, 1, 554, 4, 0, 0, 0, 0, 0, 0, false));
		map.put(1172, new Spell(41, 1172, 26, 13, 711, 132, 133, 134, 0, 556, 3, 560, 1, 0, 0, 0, 0, 0, 0, 0, 0, false));
		map.put(1175, new Spell(47, 1175, 29, 14, 711, 135, 136, 137, 0, 556, 3, 560, 1, 555, 3, 0, 0, 0, 0, 0, 0, false));
		map.put(1539, new Spell(50, 1539, 43, 25, 708, 87, 88, 89, 0, 554, 5, 560, 1, 0, 0, 0, 0, 0, 0, 0, 1409, false));
		map.put(1582, new Spell(50, 1582, 61, 2, 711, 177, 178, 179, 0, 555, 4, 557, 4, 561, 3, 0, 0, 10, 0, 0, 0, false));
		map.put(12037, new Spell(50, 12037, 30, 20, 711, 327, 328, 329, 0, 558, 4, 560, 1, 0, 0, 0, 0, 0, 0, 0, 4170, false));
		map.put(1177, new Spell(53, 1177, 32, 15, 711, 138, 139, 140, 0, 556, 3, 560, 1, 557, 4, 0, 0, 0, 0, 0, 0, false));
		map.put(1181, new Spell(59, 1181, 35, 16, 711, 129, 130, 131, 0, 556, 4, 560, 1, 554, 5, 0, 0, 0, 0, 0, 0, false));
		map.put(1190, new Spell(60, 1190, 35, 22, 811, - 1, - 1, 76, 0, 565, 2, 554, 2, 556, 4, 0, 0, 0, 0, 0, 2415, false));
		map.put(1191, new Spell(60, 1191, 35, 22, 811, - 1, - 1, 77, 0, 565, 2, 554, 1, 556, 4, 0, 0, 0, 0, 0, 2416, false));
		map.put(1192, new Spell(60, 1192, 35, 22, 811, - 1, - 1, 78, 0, 565, 2, 554, 4, 556, 1, 0, 0, 0, 0, 0, 2417, false));
		map.put(1183, new Spell(62, 1183, 36, 17, 711, 158, 159, 160, 0, 556, 5, 565, 1, 0, 0, 0, 0, 0, 0, 0, 0, false));
		map.put(1185, new Spell(65, 1185, 38, 18, 711, 161, 162, 163, 0, 556, 5, 565, 1, 555, 7, 0, 0, 0, 0, 0, 0, false));
		map.put(1542, new Spell(66, 1542, 76, - 1, 729, 167, 168, 169, 0, 557, 5, 555, 5, 566, 1, 0, 0, 0, 0, 10, 0, false));
		map.put(1188, new Spell(70, 1188, 50, 19, 711, 164, 165, 166, 0, 556, 5, 565, 1, 557, 7, 0, 0, 0, 0, 0, 0, false));
		map.put(1543, new Spell(73, 1543, 83, - 1, 729, 170, 171, 172, 0, 557, 8, 555, 8, 566, 1, 0, 0, 0, 0, 10, 0, false));
		map.put(1189, new Spell(75, 1189, 43, 20, 711, 155, 156, 157, 0, 556, 5, 565, 1, 554, 7, 0, 0, 0, 0, 0, 0, false));
		map.put(1592, new Spell(79, 1592, 91, 4, 711, 177, 178, 179, 0, 555, 5, 557, 5, 561, 4, 0, 0, 15, 0, 0, 0, false));
		map.put(1562, new Spell(80, 1562, 180, - 1, 729, 173, 174, 80, 0, 557, 12, 555, 12, 563, 1, 0, 0, 0, 0, 10, 0, false));
		map.put(12445, new Spell(85, 12445, 92, 4, 10503, 1841, 1842, 1843, 0, 560, 1, 562, 1, 563, 1, 0, 0, 0, 0, 0, 0, false));
		map.put(12939, new Spell(50, 12939, 30, 15, 1978, - 1, - 1, 385, 0, 562, 2, 560, 2, 554, 1, 556, 1, 0, 5, 0, 0, false));
		map.put(12987, new Spell(52, 12987, 31, 16, 1978, - 1, - 1, 379, 0, 562, 2, 560, 2, 556, 1, 566, 1, 0, 0, 5, 0, false));
		map.put(12901, new Spell(56, 12901, 33, 17, 1978, - 1, - 1, 373, 4, 560, 2, 562, 2, 565, 1, 0, 0, 0, 0, 0, 0, false));
		map.put(12861, new Spell(58, 12861, 34, 18, 1978, - 1, - 1, 361, 0, 560, 2, 562, 2, 555, 2, 0, 0, 5, 0, 0, 0, false));
		map.put(12963, new Spell(62, 12963, 36, 19, 1979, - 1, - 1, 389, 0, 562, 4, 560, 2, 554, 2, 556, 2, 0, 10, 0, 0, true));
		map.put(13011, new Spell(64, 13011, 37, 20, 1979, - 1, - 1, 382, 0, 562, 4, 560, 2, 556, 2, 566, 2, 0, 0, 10, 0, true));
		map.put(12919, new Spell(68, 12919, 39, 21, 1979, - 1, - 1, 376, 4, 562, 2, 560, 4, 555, 2, 562, 4, 0, 0, 0, 0, true));
		map.put(12881, new Spell(70, 12881, 40, 22, 1979, - 1, - 1, 363, 0, 562, 4, 560, 2, 555, 4, 0, 0, 8, 0, 0, 0, true));
		map.put(12951, new Spell(74, 12951, 42, 23, 1978, - 1, - 1, 387, 0, 560, 2, 565, 2, 556, 2, 556, 2, 0, 15, 0, 0, false));
		map.put(12999, new Spell(76, 12999, 43, 24, 1978, - 1, - 1, 381, 0, 562, 2, 560, 2, 556, 2, 566, 2, 0, 0, 15, 0, false));
		map.put(12911, new Spell(80, 12911, 45, 25, 1978, - 1, - 1, 375, 4, 560, 2, 565, 4, 0, 0, 0, 0, 0, 0, 0, 0, false));
		map.put(12871, new Spell(82, 12871, 46, 26, 1978, 366, 368, 367, 0, 560, 2, 565, 2, 555, 3, 0, 0, 15, 0, 0, 0, false));
		map.put(12975, new Spell(86, 12975, 48, 27, 1979, - 1, - 1, 391, 0, 560, 4, 565, 2, 554, 4, 556, 4, 0, 20, 0, 0, true));
		map.put(13023, new Spell(88, 13023, 49, 28, 1979, - 1, - 1, 383, 0, 560, 4, 565, 2, 556, 4, 566, 3, 0, 0, 20, 0, true));
		map.put(12929, new Spell(92, 12929, 51, 29, 1979, - 1, - 1, 377, 4, 560, 4, 565, 4, 566, 1, 0, 0, 0, 0, 0, 0, true));
		map.put(12891, new Spell(94, 12891, 52, 30, 1979, - 1, 366, 369, 0, 565, 2, 555, 6, 560, 4, 0, 0, 20, 0, 0, 0, true));
	}


	public static int getAutoCastId(int actionButton) {
		for(int i = 0; i < AUTOCAST_IDS.length; i++) {
			if(AUTOCAST_IDS[i] == actionButton)
				return AUTO_MAGIC_IDS[i];
		}
		return - 1;
	}


	public static final int SPELL_FAIL = 0,
			SPELL_NEGATIVE = 1,
			SPELL_SUCCESFUL = 2;



	public static int castSpell(final CombatEntity attacker, final CombatEntity opp, int spellId) {
		if(attacker.getEntity().isDead() || opp.getEntity().isDead())
			return SPELL_FAIL;
		String message = Combat.canAtk(attacker, opp);
		if(message.length() > 1) {
			attacker.getPlayer().getActionSender().sendMessage(message);
			return 0;
		}
		if (opp.getEntity() instanceof NPC) {
			String FAMILIARS[] = {"wolpertinger", "steel titan", "yak", "unicorn stallion"};//temp shitfix by fuzen
			for (String familiarName : FAMILIARS)
				if (opp.getNPC().getDefinition().name().toLowerCase().contains(familiarName)) {
					ContentEntity.sendMessage((Player) attacker.getEntity(), "You cannot attack familiars.");
					return 0;
				}
		}
		if(attacker.getPlayer().duelRule[4] && attacker.getPlayer().duelAttackable > 0) {
			attacker.getPlayer().getActionSender().sendMessage("You cannot use magic in this duel.");
			return 0;
		}
		if(opp.getEntity().isDead())
			return 0;
		if(opp.getOpponent() != attacker && opp.getEntity() instanceof Player
				&& Combat.getWildLevel(attacker.getAbsX(), attacker.getAbsY()) != - 1) {
			if(System.currentTimeMillis() - attacker.lastHit > 10000) {
				attacker.getPlayer().setSkulled(true);
			}
		} else {
			if(opp.getEntity() instanceof NPC)
				if(opp.getNPC().health <= 0)
					return 0;
		}
		attacker.getPlayer().getWalkingQueue().reset();
		attacker.addSpellAttack(spellId);
		if(spellId == - 1) {
			attacker.setAutoCastId(- 1);
			return 1;
		}
		final Spell spell = map.get(spellId);
		if(spell == null)
			return 0;
		/**
		 * Checks if player has enough runes and a required staff if needed.
		 */
		int runeIdMissing = hasRunes(attacker, spell);
		if(runeIdMissing != - 1) {
			attacker.setOpponent(null);
			attacker.getPlayer().getActionSender().sendMessage("You need more " +
					ItemDefinition.forId(runeIdMissing).getName() + "'s to cast this spell.");
			return 0;
		}
		if(spell.getStaffRequired() > 0 && attacker.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON)
				.getId() != spell.getStaffRequired()) {
			attacker.getPlayer().getActionSender().sendMessage(
					"You need " + ItemDefinition.forId(spell.getStaffRequired()).getName()
							+ " to cast this spell.");
			attacker.setOpponent(null);
			return 0;
		}
		if(spell.getFreeze() > 0 && attacker.getPlayer().getPosition().disabledMagic() && !attacker.getPlayer().hasBeenInformed) {
			attacker.getPlayer().getActionSender().sendMessage("@red@The normal hybridding area is at ::13s! (Range is disabled there)");
			//attacker.getPlayerByName().getActionSender().send
			// Message("@red@To start hybridding INSTANTLY, go to \"Instant Sets\" in the spawn tab and click 'Hybrid'");
			attacker.getPlayer().hasBeenInformed = true;
		}
		if(attacker.getPlayer().getSkills().getLevel(Skills.MAGIC) < spell.getMagicLevel()) {
			attacker.getPlayer().getActionSender().sendMessage(
					"You need a magic level of " + spell.getMagicLevel() + " to cast this spell.");
			attacker.setOpponent(null);
			return 0;
		}
		for(CombatEntity opponent : getMultiPeople(spell ,attacker, opp)) {
			/**
			 * Determine damage + splashing.
			 */
			boolean splash = false;
			if(opponent.getEntity() instanceof Player)
				if (FightPits.isSameTeam(opponent.getPlayer(), attacker.getPlayer()))
					splash = true;
			int maxDamg = spell.getMaxHit();
			int AtkBonus = CombatAssistant.calculateMageAtk(attacker.getPlayer());
			if(attacker.getEntity() instanceof Player && CombatAssistant.fullVoidMage(attacker.getPlayer())) {
				AtkBonus *= 1.30;
				maxDamg *= 1.05;
			}
			if(attacker.getEntity() instanceof Player) {
				if (CombatAssistant.wearingFarseer(attacker.getPlayer())) {
					AtkBonus *= 1.13;
					maxDamg *= 1.03;
				}
			}
			int DefBonus;
			int shieldId = - 1;
			int necklaceId = - 1;
			int weaponId = - 1;
			if(attacker.getPlayer().getEquipment().get(Equipment.SLOT_AMULET) != null) {
				necklaceId = attacker.getPlayer().getEquipment()
						.get(Equipment.SLOT_AMULET).getId();

			}

			if(attacker.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				weaponId = attacker.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON)
						.getId();
			}
			if(attacker.getEntity() instanceof Player) {
				Player atk = attacker.getPlayer();
				if(atk != null) {
					int diff = atk.getSkills().getLevel(Skills.MAGIC) - atk.getSkills().getRealLevels()[Skills.MAGIC];
					if(diff >= 1) {
						//atk.getActionSender().sendMessage("Diff: "+diff );
						double multiplication = 1+ ((double)diff * .02);
						maxDamg = (int)((double)maxDamg * multiplication);
						//atk.getActionSender().sendMessage("Damage multi: "+multiplication);
					}
				}
			}

			int freeze = spell.getFreeze();
			switch(weaponId) {
				case 2415:
				case 2416:
				case 2417:
					if (((Player) attacker.getEntity()).hasCharge()) {
						if (spellId == 1190 || spellId == 1191 || spellId == 1192)
							maxDamg *= 1.4;
					}
					break;
				case 13867:
				case 17017:
					maxDamg *= 1.10;
					break;
				case 15486:
				case 16153:
				case 16154:
				case 16155:
				case 16156:
					maxDamg *= 1.15;
					break;
				case 18355:
					maxDamg *= 1.20;
					break;
				case 14117:
					maxDamg *= 1.25;
					break;
				case 19325:
					if(opponent.getEntity() instanceof NPC && freeze > 0)   {
						maxDamg *= 1.4;
						AtkBonus *= 1.4;
					} else
						maxDamg *= 1.24;
					freeze *= 1.3;
					break;
				case 19323:
					if(opponent.getEntity() instanceof NPC && spellId == 1189) {
						maxDamg = 55;
						AtkBonus *= 1.23;
					} else {
						maxDamg *= 1.24;
					}
					break;
				case 6603:
					maxDamg *= 1.15;
					break;
			}
			switch(necklaceId) {
				case 18333:
					maxDamg *= 1.05;
					break;
				case 18335:
				case 10344:
					maxDamg *= 1.10;
					break;
			}

			int Damage = Misc.random(maxDamg);
			attacker.getPlayer().debugMessage("Damage stage1: "+Damage);
			if(opponent.getEntity() instanceof Player) {

				opponent.getPlayer().getLastAttack().updateLastAttacker(attacker.getPlayer().getName());

				DefBonus = CombatAssistant.calculateMageDef(opponent.getPlayer());

				attacker.getPlayer().debugMessage("Opps def: "+DefBonus);


				if(opponent.getPlayer().getEquipment().get(Equipment.SLOT_SHIELD) != null) {
					shieldId = opponent.getPlayer().getEquipment()
							.get(Equipment.SLOT_SHIELD).getId();
				}
			} else /** NPC Part */ {
				DefBonus = opponent.getNPC().getDefinition().getBonus()[8];
				if(SlayerTask.getLevelById(opponent.getNPC().getDefinition().getId()) > attacker
						.getPlayer().getSkills().getLevel(Skills.SLAYER)) {
					splash = true;
				}
			}

			if (opp.getNPC() != null)
				opp.getNPC().lastAttacker = attacker.getPlayer().getName();
			opp.lastHit = System.currentTimeMillis();
			if(spell.isMulti())
				AtkBonus *= 0.9;
			int deltaBonus = AtkBonus - DefBonus;
			int toAdd = Misc.random(deltaBonus / 3);
			if(spellId != 12445)
				Damage += toAdd;
			attacker.getPlayer().debugMessage("Damage stage 2:"+Damage);
			if(Damage > maxDamg)
				Damage = maxDamg;
			if(attacker.getPlayer().getPrayers().isEnabled(27))
				Damage *= 1.08;
			if(attacker.getPlayer().getEquipment().getItemId(Equipment.SLOT_RING) == 15707)
				Damage = (int)attacker.getPlayer().getDungeoneering().perks.boost(Constants.MAGE, false, Damage);

			if(opponent.getEntity() instanceof NPC && attacker.getPlayer().getSlayer().isTask(opponent.getNPC().getDefinition().getId())) {
				if(SlayerShop.hasHex(attacker.getPlayer()))
					Damage *= 1.15;
			}

			Damage = SpiritShields.applyEffects(opponent, Damage);
			/**
			 * Checks if using Prayers
			 */
			if(opponent.getEntity() instanceof Player) {
				Damage = opponent.getPlayer().getInflictDamage(Damage,attacker.getEntity(), false, Constants.MAGE);
				if(spell.getSpellId() == 12445 && opponent.getPlayer().isTeleBlocked()) {
					attacker.getPlayer().getActionSender()
							.sendMessage("This player is already teleblocked.");
					return 0;
				}

			}

			double reduction = Damage <= 0 ? 1.1 : 1.3;
			if(Misc.random(AtkBonus) < Misc.random((int)(CombatAssistant.calculateMageSplashDef(opponent.getEntity())/reduction)))
				splash = true;
		/*if(spell.getMaxHit() > 0 && Damage <= 0) {
			splash = true;
			Damage = 0;
		}   */
			if(splash || Damage <= 0) {
				Damage = 0;
			}


			attacker.getPlayer().debugMessage("Damage stage 3:"+Damage);


			/**
			 * Add Experience
			 */
			int xpMulti = (attacker.getEntity() instanceof Player && attacker.getPlayer().getPosition().inPvPArea()) ? 2 : (EXPMULTIPLIER * 10);
			ContentEntity.addSkillXP(attacker.getPlayer(), (spell.getExp()) + (Damage * xpMulti), 6);
			ContentEntity.addSkillXP(attacker.getPlayer(), 0.33 * (Damage * xpMulti), 3);
			/**
			 * Determine whether damage is critical
			 */
			final boolean critical = Damage > 0.9 * maxDamg ? true : false;

			if(attacker.getPlayer().getPrayers().isEnabled(48))
				Prayer.soulSplit(attacker.getPlayer(), opponent, Damage);

			attacker.predictedAtk = (System.currentTimeMillis() + 2400);

			/**
			 * Freezing.
			 */
			if(freeze > 0 && opponent.canMove() && ! splash && opponent.canBeFrozen()) {
				opponent.setFreezeTimer(freeze * 1000);
				if(opponent.getEntity() instanceof Player)
					opponent.getPlayer().getActionSender()
							.sendMessage("You have been frozen.");
				opponent.getEntity().getWalkingQueue().reset();
			}
			/**
			 * Do Projectile + Anim + Gfx.
			 */
			attacker.doAnim(spell.getCastAnim());
			attacker.doGfx(spell.getStartGfx());
			// offset values for the projectile
			int offsetY = (attacker.getAbsX() - opponent.getAbsX()) * - 1;
			int offsetX = (attacker.getAbsY() - opponent.getAbsY()) * - 1;
			// find our lockon target
			int hitId = attacker.getSlotId(attacker.getEntity());
			// extra proj values - not to be released
			int speed = 105;
			int distance = attacker.getEntity().getPosition()
					.distance(opponent.getEntity().getPosition());
			int min = 40;
			min -= (distance - 1) * 8;
			speed -= min;
			int slope = 12 + distance;

			long timer = 1600 + (distance * 200);
			attacker.getPlayer().getActionSender()
					.createGlobalProjectile(attacker.getAbsY(), attacker.getAbsX(), offsetY,
							offsetX, 50, speed, spell.getMoveGfx(), 43, 35, hitId,
							slope);
			attacker.face(opponent.getAbsX(), opponent.getAbsY());
			Combat.processCombat(attacker);
		/*if(spell.getMoveGfx() == - 1)//dunno why this exists
			timer = 2300;*/
			final int submitDamage = Damage;
			final boolean submitSplash = splash;
			final CombatEntity opp2 = opponent;
			World.submit(new Task(timer,"magic6") {
				@Override
				public void execute() {
					boolean hitSomething = false;
					if(spell.isMulti()) {
						if(Combat.isInMulti(attacker)) {
							finishMagic(attacker, opp2, submitDamage, spell,
									opp2 == opp, submitSplash,
									critical);
							hitSomething = true;
						}
					}
					if(! hitSomething)
						finishMagic(attacker, opp2, submitDamage, spell, true, submitSplash,
								critical);
					// delete runes
					if(opp2.getCurrentAtker() == null || opp2.getCurrentAtker() == attacker) {
						opp2.face(attacker.getAbsX(), attacker.getAbsY());
						opp2.doDefEmote();
					}
					this.stop();
				}

			});
		}
		deleteRunes(attacker, spell);
		return 2;
	}

	public static void vengeance(Player player, final CombatEntity victim,
								 int hit) {
		if(player.duelRule[4] && player.duelAttackable > 0) {
			return;
		}
		if(! player.getSpellBook().isLunars())
			return;
		if(player.vengeance && hit >= 2) {
			player.forceMessage("Taste vengeance!");
			World.submit(new Task(600,"magic2") {
				@Override
				public void execute() {
					victim.hit((int) (hit * 0.75), player.isDead() ? null : player, false, 2);
					this.stop();
				}
			});
			player.vengeance = false;
		}
	}

	public static void recoil(Player p, final CombatEntity victim, int hit) {
		if(hit < 10)
			return;
		if(p.getEquipment().get(Equipment.SLOT_RING) != null) {
			if(p.getEquipment().get(Equipment.SLOT_RING).getId() == 2550) {
				victim.hit((int) (hit * 0.10), p, false, 3);
				if(Misc.random(40) == 0) {
					p.getEquipment().set(Equipment.SLOT_RING, null);
				}
			} else if(p.getEquipment().get(Equipment.SLOT_RING).getId() == WildernessBossTask.RING_ID) {
				victim.hit((int) (hit * 0.10), p, false, 3);
			}
		}
	}
	/** Basic charge spell.*/
	public static void preformCharge(Player player) {
		if((ContentEntity.getItemAmount(player, 554) < 3
				|| ContentEntity.getItemAmount(player, 556) < 3
				|| ContentEntity.getItemAmount(player, 565) < 3)) {
			ContentEntity.sendMessage(player,
					"You need more runes to cast the charge.");
			return;
		}
		if (player.hasCharge()) {
			player.sendMessage("Charge is already casted, and currently active.");
			return;
		}

		if(player.getSkills().getLevel(6) < 80) {
			ContentEntity.sendMessage(player,
					"You need a magic level of 80 to activate this spell.");
			return;
		}
		if(player.duelRule[4] && player.duelAttackable > 0) {
			player.getActionSender().sendMessage(
					"You cannot use magic in this duel.");
			return;
		}
		ContentEntity.deleteItemA(player, 554, 3);
		ContentEntity.deleteItemA(player, 556, 3);
		ContentEntity.deleteItemA(player, 565, 3);
		player.addCharge(420); //seconds
		ContentEntity.playerGfx(player, 301);
		ContentEntity.startAnimation(player, 811);
		player.sendMessage("You feel charged with magic power.");
		ContentEntity.addSkillXP(player, 255, 6);
	}
	public static void clickVengeance(Player player) {
		if(player.duelRule[4] && player.duelAttackable > 0) {
			player.getActionSender().sendMessage(
					"You cannot use magic in this duel.");
			return;
		}
		if(player.getSkills().getLevel(6) < 94) {
			ContentEntity.sendMessage(player,
					"You need 94 magic to cast Vengeance.");
			return;
		}
		if(player.getSkills().getLevelForExp(1) < 40) {
			player.getActionSender().sendMessage(
					"You need 40 defence to cast Vengeance.");
			return;
		}
		if((ContentEntity.getItemAmount(player, 9075) < 4
				|| ContentEntity.getItemAmount(player, 557) < 10
				|| ContentEntity.getItemAmount(player, 560) < 2) && !Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
			ContentEntity.sendMessage(player,
					"You need more runes to cast Vengeance.");
			return;
		}
		if(player.vengeance) {
			player.getActionSender().sendMessage(
					"You already have Vengeance casted!.");
			return;
		}
		final long vengTimer = BountyPerkHandler.getVengTimer(player);
		final long nextVeng = vengTimer - (System.currentTimeMillis() - player.lastVeng);
		if(nextVeng > 0) {
			player.getActionSender().sendMessage(
					"You can only cast Vengeance every" + (vengTimer/1000)+" seconds ("+ nextVeng +"ms remaining)");
			return;
		}
		player.lastVeng = System.currentTimeMillis();
		player.vengeance = true;
		ContentEntity.playerGfx(player, 726);
		ContentEntity.startAnimation(player, 4410);
		ContentEntity.deleteItemA(player, 9075, 4);
		ContentEntity.deleteItemA(player, 557, 10);
		ContentEntity.deleteItemA(player, 560, 2);
		ContentEntity.addSkillXP(player, 2000, 6);
	}

	public static void clickVenganceOther(Player caster, Player player2) {
		if(caster.getSkills().getLevel(6) < 93) {
			ContentEntity.sendMessage(caster,
					"You need 93 magic to cast Vengeance Other.");
			return;
		}
		if(ContentEntity.getItemAmount(caster, 9075) < 3
				|| ContentEntity.getItemAmount(caster, 557) < 10
				|| ContentEntity.getItemAmount(caster, 560) < 2) {
			ContentEntity.sendMessage(caster,
					"You need more runes to cast Vengeance Other.");
			return;
		}
		if(Combat.canAtk(caster.cE, player2.cE).length() > 1)
			return;
		if(! player2.vengeance) {
			if(System.currentTimeMillis() > player2.lastVeng + 30000) {
				player2.lastVeng = System.currentTimeMillis();
				player2.vengeance = true;
				ContentEntity.playerGfx(player2, 725);
				ContentEntity.startAnimation(caster, 4410);
				ContentEntity.deleteItemA(caster, 9075, 3);
				ContentEntity.deleteItemA(caster, 557, 10);
				ContentEntity.deleteItemA(caster, 560, 2);
				ContentEntity.addSkillXP(caster, 2000, 6);
			} else {
				caster.getActionSender().sendMessage(
						"You can only cast Vengeance every 30 seconds.");
			}
		}
	}

	public static void finishMagic(final CombatEntity c,
								   final CombatEntity p, final int Damage, final Spell spell,
								   boolean castedOn, boolean splash, boolean critical) {
		if(p.getEntity() instanceof Player) {
			if(spell.getSpellId() == 12445 && c.getPlayer().getSpellBook().isRegular() && !splash) {
				if(!(p.getPlayer().getPrayers().isEnabled(Prayers.CURSE_DEFLECT_MAGIC) || p.getPlayer().getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_MAGE)))
					p.getPlayer().setTeleBlock(
							System.currentTimeMillis() + 300000);
				else
					p.getPlayer().setTeleBlock(System.currentTimeMillis() + 120000);
				p.getPlayer().getActionSender()
						.sendMessage("You are now teleblocked");
				p.getEntity().playGraphics(Graphic.create(1843));
			}
		}
		// deal damage

		if(false/* END_GFX[spell] == 369 && !splash */)
			World.submit(new Task(500,"magic2") {
				@Override
				public void execute() {
					p.hit(Damage, c.getEntity(), false, 2);
					if(p.getEntity() instanceof Player) {
						vengeance(p.getPlayer(), c, Damage);
					}
					this.stop();
				}
			});
		else {
			int addCritical = critical ? 5 : 0;

			p.hit(Damage, c.getEntity(), false, Constants.MAGE + addCritical);
			if(p.getEntity() instanceof Player) {
				vengeance(p.getPlayer(), c, Damage);
			}
		}
		p.lastHit = System.currentTimeMillis();
		World.submit(new Task(1000, "finishmagic") {
			@Override
			public void execute() {
				if(c.getOpponent() == null) {
					if(p != null) {
						c.setOpponent(p);
						Combat.resetAttack(c);
					}
				}
				if(System.currentTimeMillis() - p.lastHit > 10000) {
					// System.out.println("resetting magic 3");
					c.setOpponent(p);
					Combat.resetAttack(c);
				}
				this.stop();
			}
		});
		int endGfx = spell.getEndGfx();
		if(! p.canMove())
			if(endGfx == 369)
				endGfx = 1677;
		// ancients effects -l0l freezeing stoped caster moving not enemy

		if(spell.getPoison() > 0 && ! p.isPoisoned()
				&& Combat.random(100) < spell.getPoison() && ! splash) {
			Combat.poisonEntity(p);
		}
		if(spell.getHpDrain() > 0 && ! splash)
			c.getPlayer().heal((Damage / spell.getHpDrain()));
		// do finish gfx on getOpponent()
		if(spell.getEndGfx() != - 1)
			if(splash)
				p.doGfx(85);
			else if(endGfx == 369 || endGfx == 363)
				p.doGfx(endGfx, 0);
			else
				p.doGfx(endGfx);
		if(p.getEntity() instanceof NPC || p.getPlayer().autoRetailate) {
			p.setOpponent(c);
		}
	}

	public static List<CombatEntity> getMultiPeople(Spell spell, CombatEntity caster,
													CombatEntity hit) {
		List<CombatEntity> k = new LinkedList<CombatEntity>();
		if(!Combat.isInMulti(caster) || !spell.isMulti()) {
			k.add(hit);
			return k;
		}
		for(Player p : hit.getEntity().getRegion().getPlayers()) {
			if(caster != p.cE
					&& Combat.canAtk(caster, p.cE).length() <= 1
					&& hit.getEntity().getPosition()
					.isWithinDistance(p.getPosition(), 1)) {
				k.add(p.cE);
			}
		}
		for(NPC n : hit.getEntity().getRegion().getNpcs()) {
			if(hit.getEntity().getPosition()
					.isWithinDistance(n.getPosition(), 1)
					&& Combat.isInMulti(n.cE) && !(n.summoned && n.ownerId > 0))
				k.add(n.cE);
		}
		return k;
	}

	public static boolean hasStaff(CombatEntity c, int rune) {
	    /*
		 * air 1381 1397 1405 water 1383 1395 1403 6562 6563 earth 1385 1399
		 * 1407 3053 3054 6562 6563 fire 1387 1393 1401 3053 3054
		 */
		int staff = - 1;
		if(c.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON) != null)
			staff = c.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON)
					.getId();
		if(rune == 556) {
			if(staff == 1381 || staff == 1397 || staff == 1405)// air
				return true;
		}
		if(rune == 555) {
			if(staff == 1383 || staff == 1395 || staff == 1403
					|| staff == 6562 || staff == 6563)// water
				return true;
		}
		if(rune == 557) {
			if(staff == 1385 || staff == 1399 || staff == 1407
					|| staff == 3053 || staff == 3054 || staff == 6562
					|| staff == 6563)// earth
				return true;
		}
		if(rune == 554) {
			if(staff == 1387 || staff == 1393 || staff == 1401
					|| staff == 3053 || staff == 3054)// fire
				return true;
		}
		return false;
	}

	public static int hasRunes(CombatEntity c, Spell spell) {
		if(c.getEntity() instanceof Player) {
			Player player = (Player)c.getEntity();
			if(Rank.hasAbility(player, Rank.ADMINISTRATOR))
				return -1;
		}
		if(spell.getFirstRune() > 0 && spell.getFirstAmount() > 0)
			if(! hasStaff(c, spell.getFirstRune())
					&& (c.getPlayer().getInventory().getById(spell.getFirstRune()) == null || c
					.getPlayer().getInventory()
					.getById(spell.getFirstRune()).getCount() < spell.getFirstAmount())
					&& (c.getPlayer().getRunePouch().getById(spell.getFirstRune()) == null || c.getPlayer().getRunePouch().getById(spell.getFirstRune()).getCount() < spell.getFirstAmount()))
				return spell.getFirstRune();
		if(spell.getSecondRune() > 0 && spell.getSecondAmount() > 0)
			if(! hasStaff(c, spell.getSecondRune())
					&& (c.getPlayer().getInventory()
					.getById(spell.getSecondRune()) == null || c
					.getPlayer().getInventory()
					.getById(spell.getSecondRune()).getCount() < spell.getSecondAmount())
					&& (c.getPlayer().getRunePouch().getById(spell.getSecondRune()) == null || c.getPlayer().getRunePouch().getById(spell.getSecondRune()).getCount() < spell.getSecondAmount()))
				return spell.getSecondRune();
		if(spell.getThirdRune() > 0 && spell.getThirdAmount() > 0)
			if(! hasStaff(c, spell.getThirdRune())
					&& (c.getPlayer().getInventory().getById(spell.getThirdRune()) == null || c
					.getPlayer().getInventory()
					.getById(spell.getThirdRune()).getCount() < spell.getThirdAmount())
					&& (c.getPlayer().getRunePouch().getById(spell.getThirdRune()) == null || c.getPlayer().getRunePouch().getById(spell.getThirdRune()).getCount() < spell.getThirdAmount()))
				return spell.getThirdRune();
		if(spell.getFourthRune() > 0 && spell.getFourthAmount() > 0)
			if(! hasStaff(c, spell.getFourthRune())
					&& (c.getPlayer().getInventory()
					.getById(spell.getFourthRune()) == null || c
					.getPlayer().getInventory()
					.getById(spell.getFourthRune()).getCount() < spell.getFourthAmount())
					&& (c.getPlayer().getRunePouch().getById(spell.getFourthRune()) == null || c.getPlayer().getRunePouch().getById(spell.getFourthRune()).getCount() < spell.getFourthAmount()))
				return spell.getFourthRune();
		return - 1;
	}

	public static boolean deleteRunes(CombatEntity c, Spell spell) {
		if(! hasStaff(c, spell.getFirstRune()))
			if(c.getPlayer().getRunePouch().getById(spell.getFirstRune()) == null || c.getPlayer().getRunePouch().getById(spell.getFirstRune()).getCount() < spell.getFirstAmount()) {
				c.getPlayer()
						.getInventory()
						.remove(-1,
								new Item(spell.getFirstRune(), spell.getFirstAmount()));
			} else {
				c.getPlayer()
						.getRunePouch()
						.remove(-1,
								new Item(spell.getFirstRune(), spell.getFirstAmount()));
			}
		if(! hasStaff(c, spell.getSecondRune()))
			if(c.getPlayer().getRunePouch().getById(spell.getSecondRune()) == null || c.getPlayer().getRunePouch().getById(spell.getSecondRune()).getCount() < spell.getSecondAmount()) {
				c.getPlayer()
						.getInventory()
						.remove(- 1,
								new Item(spell.getSecondRune(), spell.getSecondAmount()));
			} else {
				c.getPlayer()
						.getRunePouch()
						.remove(-1,
								new Item(spell.getSecondRune(), spell.getSecondAmount()));
			}
		if(! hasStaff(c, spell.getThirdRune()))
			if(c.getPlayer().getRunePouch().getById(spell.getThirdRune()) == null || c.getPlayer().getRunePouch().getById(spell.getThirdRune()).getCount() < spell.getThirdAmount()) {
				c.getPlayer()
						.getInventory()
						.remove(- 1,
								new Item(spell.getThirdRune(), spell.getThirdAmount()));
			} else {
				c.getPlayer()
						.getRunePouch()
						.remove(-1,
								new Item(spell.getThirdRune(), spell.getThirdAmount()));
			}
		if(! hasStaff(c, spell.getFourthRune()))
			if(c.getPlayer().getRunePouch().getById(spell.getFourthRune()) == null || c.getPlayer().getRunePouch().getById(spell.getFourthRune()).getCount() < spell.getFourthAmount()) {
				c.getPlayer()
						.getInventory()
						.remove(-1,
								new Item(spell.getFourthRune(), spell.getFourthAmount()));
			} else {
				c.getPlayer()
						.getRunePouch()
						.remove(-1,
								new Item(spell.getFourthRune(), spell.getFourthAmount()));
			}
		return true;
	}

	public static void alch(final Player player, int value, int spell) {
		if (System.currentTimeMillis() - player.contentTimer < 3000) {
			return;
		}
		final Item item = Item.create(value);
		if (!player.getInventory().hasItem(item)) {
			return;
		}
		if (spell == 1162 || spell == 1178) {
			final Item nature = Item.create(561);
			if (!player.getInventory().hasItem(nature)) {
				player.sendMessage("You do not have enough nature runes to cast this spell.");
				return;
			}
			final Item fire = Item.create(554, ((spell == 1162) ? 3 : 5));
			if (hasStaff(player.cE, 554)
					|| (player.getInventory().hasItem(fire))) {
				if (item.getId() == 995 || item.getId() == 12747  || item.getId() == 12744 || item.getId() == 18509 || item.getId() == 19709 || item.getId() == 15707) {
					player.sendf("%s is not alchable.", TextUtils.titleCase(item.getDefinition().getName()));
					return;
				}
				player.contentTimer = System.currentTimeMillis();
				player.getInventory().remove(nature);
				if (!hasStaff(player.cE, 554)) {
					player.getInventory().remove(fire);
				}
				player.getInventory().remove(item);
				player.playAnimation(Animation.create(spell == 1162 ? 712 : 713));
				player.getInventory().add(Item.create(995, spell == 1162 ? item.getDefinition().getLowAlcValue() : item.getDefinition().getHighAlcValue()));
				player.playGraphics(Graphic.create(spell == 1162 ? 112 : 113));
				player.getSkills().addExperience(Skills.MAGIC, spell == 1162 ? 350 : 800);
				player.getActionSender().setViewingSidebar(6);
			} else {
				player.sendMessage("You do not have enough fire runes to cast this spell");
			}
		}
	}

	public void bonesToBannanas() {

	}

	public void enchant() {

	}

	public void telegrab() {

	}

	public static void teleport(final Player player, String location) {
		int x = 3222;
		int y = 3222;
		int z = 0;
		if(location.equals("varrock")) {
			x = 3215;
			y = 3424;
		} else if(location.equals("home")) {
			x = 3087;
			y = 3501;
			z = 0;
		} else if(location.equals("lumbridge")) {
			// do nothing
		} else if(location.equals("falnor")) {
			x = 2965;
			y = 3378;
		} else if(location.equals("edgeville")) {
			x = 3087;
			y = 3501;
		} else if(location.equals("magebank")) {

		} else if(location.equals("varrockpk")) {

		} else if(location.equals("edgedungeon")) {
			x = 3097;
			y = 9881;
		} else if(location.equals("argonoue")) {
			x = 2662;
			y = 3305;
		} else if(location.equals("camelot")) {
			x = 2757;
			y = 3477;
		} else if(location.equals("yanille")) {
			x = 2606;
			y = 3093;
		} else if(location.equals("portsarmon")) {

		} else if(location.equals("watchtower")) {

		}
		teleport(player, x, y, z, false);
	}

	public static void openTeleMenu(final Player player, int menu) {
		if(player.isTeleBlocked()) {
			player.getActionSender().sendMessage("You are currently teleblocked.");
			return;
		}
		if(!player.getLocation().canTeleport(player))
			return;

		if(player.getTimeSinceLastTeleport() < 1600)
			return;
		player.getExtraData().put("teleMenu", menu);

		if(menu == 0) {
			teleport(player, "varrock");
			/*
			 * player.getActionSender().sendString(3011,"Varrock");
			 * player.getActionSender().sendString(3016,"Lumbridge");
			 * player.getActionSender().sendString(3021,"Falador");
			 * player.getActionSender().sendString(3026,"Edgeville");
			 * player.getActionSender().sendString(3031,"Camelot");
			 * player.getActionSender().sendString(3036,"Entrana (Skilling)");
			 * player.getActionSender().sendSidebarInterface(6, 3000);
			 */
		} else if(menu == 1) {
			teleport(player, "lumbridge");
			/*
			 * player.getActionSender().sendString(3011,"Noob Area");//2635,4728,
			 * 0
			 * player.getActionSender().sendString(3016,"Bandit Camp");//3172,2980
			 * ,0
			 * player.getActionSender().sendString(3021,"Karamja dungeon");//2858
			 * ,9572,0
			 * player.getActionSender().sendString(3026,"Asgarnian Ice dungeon"
			 * );//3007,9550,0
			 * player.getActionSender().sendString(3031,"Soul's Bane Dungeon"
			 * );//3300,9825,0
			 * player.getActionSender().sendString(3036,"Master Area"
			 * );//2717,9803,0 player.getActionSender().sendSidebarInterface(6,
			 * 3000);
			 */
		} else if(menu == 2) {
			teleport(player, "falnor");
			/*
			 * player.getActionSender().sendString(3011,"Duel Arena");//3360
			 * 3213
			 * player.getActionSender().sendString(3016,"Barrows Brothers");
			 * //3564 3288
			 * player.getActionSender().sendString(3021,"Clan Wars (N/A)"
			 * );//2480 5175
			 * player.getActionSender().sendString(3026,"God Wars");
			 * player.getActionSender
			 * ().sendString(3031,"Warriors guild (N/A)");//2658 Y 2649
			 * player.getActionSender
			 * ().sendString(3036,"Castle Wars (N/A)");//2400 3103
			 * player.getActionSender().sendSidebarInterface(6, 3000);
			 */
		} else if(menu == 3) {
			teleport(player, "camelot");
			/*
			 * Pking: -Edgeville 3086,3491,0 -Chaos altar 3239,3611,0 -Bandit
			 * camp 3034,3700,0 -Graveyard 3158,3669,0 -Mage bank 2538,4716,0
			 * lever teleports to 3090,3956,0 -Rogues castle 3286,3922,0
			 */
			/*
			 * player.getActionSender().sendString(3011,"Edgeville PKing - "+
			 * getPlayersInArea(3085,3519,0)+" PKers");
			 * player.getActionSender().
			 * sendString(3016,"Varrock PKing - "+getPlayersInArea
			 * (3243,3519,0)+" PKers");
			 * player.getActionSender().sendString(3021,
			 * "Mage Arena - "+getPlayersInArea(3088,3960,0)+" PKers");
			 * player.getActionSender
			 * ().sendString(3026,"Deep Wilderness - "+getPlayersInArea
			 * (3288,3886,0)+" PKers");
			 * player.getActionSender().sendString(3031,
			 * "Chaos Tower - "+getPlayersInArea(2958,3819,0)+" PKers");
			 * player.getActionSender
			 * ().sendString(3036,"Wilderness Bandit Camp - "
			 * +getPlayersInArea(3288,3886,0)+" PKers");
			 * player.getActionSender().sendSidebarInterface(6, 3000);
			 */
		} else if(menu == 4) {
			teleport(player, "argonoue");
			/*
			 * player.getActionSender().sendString(3011,"Waterfall Dungeon");//2574
			 * ,9864
			 * player.getActionSender().sendString(3016,"Taverly Dungeon");//
			 * :tele 2884,9798
			 * player.getActionSender().sendString(3021,"Brimhaven Dungeon"
			 * );//2703,9564
			 * player.getActionSender().sendString(3026,"Tzhaar");//2480,5175
			 * player
			 * .getActionSender().sendString(3031,"Slayer Dungeon");//2808,10002
			 * player
			 * .getActionSender().sendString(3036,"Slayer Tower");//3429,3538
			 * player.getActionSender().sendSidebarInterface(6, 3000);
			 */
		} else if(menu == 5) {
			/*
			 * player.getActionSender().sendString(3011,"Edgeville PKing - "+
			 * getPlayersInArea(3085,3519,0)+" PKers");
			 * player.getActionSender().
			 * sendString(3016,"Varrock PKing - "+getPlayersInArea
			 * (3243,3519,0)+" PKers");
			 * player.getActionSender().sendString(3021,
			 * "Mage Arena - "+getPlayersInArea(3088,3960,0)+" PKers");
			 * player.getActionSender
			 * ().sendString(3026,"Deep Wilderness - "+getPlayersInArea
			 * (3288,3886,0)+" PKers");
			 * player.getActionSender().sendString(3031,
			 * "Chaos Tower - "+getPlayersInArea(2958,3819,0)+" PKers");
			 * player.getActionSender
			 * ().sendString(3036,"Wilderness Bandit Camp - "
			 * +getPlayersInArea(3288,3886,0)+" PKers");
			 * player.getActionSender().sendSidebarInterface(6, 3000);
			 */
		}
	}

	public static void clickNewTeleInterface(final Player player, int button) {
		if(player.getSpellBook().toInteger() == SpellBook.ANCIENT_SPELLBOOK)
			player.getActionSender().sendSidebarInterface(6, 12855);
		else if(player.getSpellBook().toInteger() == SpellBook.REGULAR_SPELLBOOK)
			player.getActionSender().sendSidebarInterface(6, 1151);
		else if(player.getSpellBook().toInteger() == SpellBook.LUNAR_SPELLBOOK)
			player.getActionSender().sendSidebarInterface(6, 29999);
		if(player.isTeleBlocked()) {
			player.getActionSender().sendMessage("You are currently teleblocked.");
			return;
		}
		if(player.getTimeSinceLastTeleport() < 1600)
			return;

		if(!player.getLocation().canTeleport(player)) {
			return;
		}
		button = button - 3001;
		int menu = (Integer) player.getExtraData().get("teleMenu");
		if(menu == 0) {
			if(button == 0)
				teleport(player, "varrock");
			if(button == 1)
				teleport(player, "lumbridge");
			if(button == 2)
				teleport(player, "falnor");
			if(button == 3)
				teleport(player, "edgeville");
			if(button == 4)
				teleport(player, "camelot");
			if(button == 5)
				teleport(player, 2834, 3335, 0, false);
		}
		if(menu == 1) {
			if(button == 0)
				teleport(player, 2635, 4728, 0, false);
			if(button == 1)
				teleport(player, 3172, 2980, 0, false);
			if(button == 2)
				teleport(player, 2858, 9572, 0, false);
			if(button == 3)
				teleport(player, 3007, 9550, 0, false);
			if(button == 4)
				teleport(player, 3300, 9825, 0, false);
			if(button == 5)
				teleport(player, 2717, 9803, 0, false);
		}
		if(menu == 2) {
			if(button == 0)
				teleport(player, 3371, 3274, 0, false);
			if(button == 1)
				teleport(player, 3564, 3288, 0, false);
			/*
			 * if(button == 2) teleport(player,2442,5170,0);
			 */
			if(button == 3)
				teleport(player, 2881, 5310, 2, false);// godwars
			/*
			 * if(button == 4) teleport(player,2658,2649,0); if(button == 5)
			 * teleport(player,2441,3090,0);
			 */
		}
		if(menu == 3 || menu == 5) {
			if(button == 0)
				teleport(player, 3085, 3519, 0, false);
			if(button == 1)
				teleport(player, 3243, 3519, 0, false);
			if(button == 2)
				teleport(player, 3088, 3960, 0, false);
			if(button == 3)
				teleport(player, 3288, 3886, 0, false);
			if(button == 4)
				teleport(player, 2958, 3819, 0, false);
			if(button == 5)
				teleport(player, 3288, 3886, 0, false);
		}
		if(menu == 4) {
			if(button == 0)
				teleport(player, 2574, 9864, 0, false);
			if(button == 1)
				teleport(player, 2884, 9798, 0, false);
			if(button == 2)
				teleport(player, 2703, 9564, 0, false);
			if(button == 3)
				teleport(player, 2480, 5175, 0, false);
			if(button == 4)
				teleport(player, 2808, 10002, 0, false);
			if(button == 5)
				teleport(player, 3429, 3538, 0, false);
		}
	}

	public static void homeTeleport(final Player player) {
		player.getWalkingQueue().reset();
		player.getActionSender().resetFollow();
		final int x = (3085 + Misc.random(2));
		final int y = (3491 + Misc.random(2));

		if(player.isTeleBlocked()) {
			player.getActionSender().sendMessage("You are currently teleblocked.");
			return;
		}
		if(player.isDead())
			return;

		if(player.getTimeSinceLastTeleport() < 1600)
			return;

		if(!player.getLocation().canTeleport(player))
			return;

		player.updateTeleportTimer();
		if((player.getPosition().getX() >= 2814
				&& player.getPosition().getX() <= 2942
				&& player.getPosition().getY() >= 5250 && player.getPosition()
				.getY() <= 5373)
				&& (x < 2814 || x > 2942 || y < 5250 || y > 5373)) {
			player.getActionSender().showInterfaceWalkable(- 1);
		}
		player.inAction = ! player.inAction;
		World.submit(new Task(600,"magic4") {
			int index = 0;

			public void execute() {
				if(! player.inAction) {
					player.cE.doGfx(- 1);
					this.stop();
					return;
				}
				if(player.cE.getOpponent() != null) {
					player.getActionSender().sendMessage(
							"You cannot teleport while fighting!");
					this.stop();
					player.inAction = false;
					return;
				}
				if(index >= 17) {
					if(player.getDungeoneering().inDungeon()) {
						final Room room = player.getDungeoneering().getCurrentDungeon().getStartRoom();
						player.setTeleportTarget(room.getSpawnLocation());
						player.getDungeoneering().setCurrentRoom(room);
					} else {
						player.setTeleportTarget(Position.create(x, y, 0));
					}
					this.stop();
					player.inAction = false;
					return;
				}
				ContentEntity.startAnimation(player,
						Misc.HomeTeleportAnimations[index]);
				player.cE.doGfx(Misc.HomeTeleportGfx[index], 0);
				index++;
			}
		});
	}

	public static void teleport(Player player, Position loc, boolean force) {
		teleport(player, loc.getX(), loc.getY(), loc.getZ(), force, true);
	}

	public static void teleport(Player player, Position loc, boolean force, boolean random) {
		teleport(player, loc.getX(), loc.getY(), loc.getZ(), force, random);
	}

	public static void teleport(final Player player, int x, int y, int z,
								boolean force) {
		teleport(player, x, y, z, force, true);
	}

	public static void teleport(final Player player, int x, int y, int z,
								boolean force, boolean random) {
		if(! force) {
			if(player.getAgility().isBusy())
				return;

			if(DangerousPK.inDangerousPK(player)) {
				if(player.getPoints().getPkPoints() > 75) {
					player.sendMessage("You lose 75 PKT upon teleporting!");
					player.getPoints().setPkPoints(player.getPoints().getPkPoints() - 75);
				} else {
					player.sendMessage("You need 75 PKT to teleport!");
					return;
				}
			}
			if(random) {
				x += Combat.random(2);
				y += Combat.random(2);
			}

			if(player.isDead())
				return;

			if(player.onConfirmScreen) {
				player.sendMessage("You can't teleport right now.");
				return;
			}

			if(player.isTeleBlocked()) {
				player.sendMessage("You are currently teleblocked.");
				return;
			}

			if(player.getTimeSinceLastTeleport() < 1600)
				return;

			if(!player.getLocation().canTeleport(player))
				return;

			player.updateTeleportTimer();
		}
		player.getSkills().stopSkilling();
		Trade.declineTrade(player);
		Duel.declineTrade(player);

		final int x1 = x;
		final int y1 = y;
		final int z1 = z;
		int delay = 1400;

		if(player.getSpellBook().isRegular()) {
			player.playGraphics(Graphic.create(1576, 6553635));// perfect !
			player.playAnimation(Animation.create(8939, 0));
		} else if(player.getSpellBook().isAncient()) {
			player.playGraphics(Graphic.create(1576, 0));// perfect !
			player.playAnimation(Animation.create(8939, 0));// anim id? lemmee
			// // check
			delay = 1800;
		} else {
			player.playGraphics(Graphic.create(1685, 0));// perfect !
			player.playAnimation(Animation.create(9606, 0));
			delay = 4200;
		}
		player.inAction = false;
		World.submit(new Task(delay,"magic5") {
			@Override
			public void execute() {
				player.setTeleportTarget(Position.create(x1, y1, z1));
				if(player.getSpellBook().isRegular())
					player.playAnimation(Animation.create(8941, 0));
				else
					player.playAnimation(Animation.create(- 1, 0));
				if(player.getDungeoneering().inDungeon())
					player.getDungeoneering().getCurrentDungeon().remove(player, false);
				this.stop();
			}
		});
		// 392 for ancients tele 308 for normal tele
		// anim 715
		// 1400 timer
	}


	private static boolean canGoTo13s(final Player player) {
		final Item shield = player.getEquipment().get(Equipment.SLOT_SHIELD);
		if(shield != null && (shield.getId() == 13740 || shield.getId() == 13744))
			return false;
		for(final Item item : player.getInventory().toArray()) {
			if(item == null) continue;
			if(item.getId() == 13740 || item.getId() == 13744)
				return false;
		}
		return player.getSpellBook().isAncient();
	}

	public static void goTo13s(final Player player) {
		if(canGoTo13s(player)) {
			SpecialAreaHolder.get("hybrid").ifPresent(area -> area.enter(player));
		} else {
			player.sendImportantMessage("You have to be on the Ancient Spellbooks to go to 13s");
			player.sendImportantMessage("You cannot bring Divine or Elysian Spirit Shields with you here");
		}
	}



	public static void swapSpellbook(Player p) {
		if(p.getSkills().getLevel(6) < 96) {
			ContentEntity.sendMessage(p,
					"You need 96 magic to cast Spellbook Swap.");
			return;
		}
		if(p.getSkills().getLevelForExp(1) < 40) {
			p.getActionSender().sendMessage(
					"You need 40 defence to cast this spell.");
			return;
		}
		if(ContentEntity.getItemAmount(p, 9075) < 3
				|| ContentEntity.getItemAmount(p, 564) < 2
				|| ContentEntity.getItemAmount(p, 563) < 1) {
			ContentEntity.sendMessage(p,
					"You need more runes to cast Spellbook Swap.");
			return;
		}
		p.playGraphics(Graphic.create(1062, 0));// perfect !
		p.playAnimation(Animation.create(6299, 0));
		DialogueManager.openDialogue(p, 121);

	}

	public static final boolean in13sArea(int x, int y) {
		return x <= 2983 && y <= 3615 && y >= 3605 && x >= 2970;
	}

}