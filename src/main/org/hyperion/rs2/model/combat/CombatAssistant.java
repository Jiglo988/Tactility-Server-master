package org.hyperion.rs2.model.combat;

import org.hyperion.engine.task.impl.WildernessBossTask;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.skill.Prayer;

import java.util.*;


public class CombatAssistant {

	public static void init() {
		Arrays.sort(TWO_HANDED);
	}

	public static void addExperience(CombatEntity combatEntity, int bowType, int damage) {
		if(bowType != Constants.RANGEDNOARROWS && bowType != Constants.MELEETYPE)
			Combat.addXP(combatEntity.getPlayer(), damage, true);
		else
			Combat.addXP(combatEntity.getPlayer(), damage, false);
	}

	/**
	 * Use this method to check if a player needs to be skulled and skull him.
	 *
	 * @param combatEntity
	 */
	public static void checkSkull(CombatEntity combatEntity) {
		if(combatEntity.getOpponent().getEntity() instanceof NPC && WildernessBossTask.isWildernessBoss(combatEntity.getOpponent().getNPC().getDefinition().getId())) {
			combatEntity.getPlayer().setSkulled(true);
			Prayer.setHeadIcon(combatEntity.getPlayer());
			return;
		}
		if(combatEntity.getOpponent().getOpponent() == combatEntity)
			return;
		if(! (combatEntity.getOpponent().getEntity() instanceof Player))
			return;
		if(Combat.getWildLevel(combatEntity.getAbsX(), combatEntity.getAbsY(), combatEntity.getAbsZ()) == - 1)
			return;
		if(System.currentTimeMillis() - combatEntity.lastHit < 10000)
			return;
        if(combatEntity.getPlayer().getLastAttack().contains(combatEntity.getOpponent().getPlayer().getName()))
            return;
		if(! combatEntity.getPlayer().isSkulled()) {
			combatEntity.getPlayer().setSkulled(true);
			Prayer.setHeadIcon(combatEntity.getPlayer());
		}
	}

	/**
	 * Use this to get a players shield id.
	 *
	 * @param equipment
	 * @return
	 */
	public static int getShieldId(Container equipment) {
		if(equipment.get(Equipment.SLOT_SHIELD) != null) {
			return equipment.get(Equipment.SLOT_SHIELD).getId();
		}
		return - 1;
	}
	/**
	 * Stab weapons
	 */
	
	public static final List<Integer> STAB_WEAPONS = new LinkedList<Integer>(Arrays.asList(11716, 11717, 13454,5698, 13889, 14484, 18349, 17135));
	
	public static final boolean isStab(Item item) {
		if(item == null)
			return false;
		boolean isStab = STAB_WEAPONS.contains(item.getId());
        final String name = item.getDefinition().getName().toLowerCase();
		if(!isStab)
			isStab = name.contains("spear") || name.contains("rapier") || name.contains("dagger");
		return isStab;
	}

	/**
	 * Use this to get a players arrows id.
	 *
	 * @param equipment
	 * @return
	 */
	public static int getArrowsId(Container equipment) {
		if(equipment.get(Equipment.SLOT_ARROWS) != null) {
			return equipment.get(Equipment.SLOT_ARROWS).getId();
		}
		return - 1;
	}

	/**
	 * Use this to get a players weapon id;
	 *
	 * @param equipment
	 * @return
	 */
	public static int getWeaponId(Container equipment) {
		if(equipment.get(Equipment.SLOT_WEAPON) != null) {
			return equipment.get(Equipment.SLOT_WEAPON).getId();
		}
		return - 1;
	}

	/**
	 * Checks if the attacking combatEntity is valid, thus not null, not dead, etc
	 *
	 * @param combatEntity
	 * @return
	 */
	public static boolean isValid(CombatEntity combatEntity) {
		if(combatEntity.getOpponent() == null || combatEntity.getOpponent().getEntity() == null) {
			return false;
		}
		if(combatEntity.getEntity().isDead() || combatEntity.getOpponent().getEntity().isDead()) {
			return false;
		}
		return combatEntity.getOpponent().isNpcAttackAble();
	}


	/**
	 * Finds what weapon the client is using.
	 */
	public static int getCombatStyle(CombatEntity entity) {
		return getCombatStyle(entity.getPlayer().getEquipment());
	}


	/**
	 * Finds what weapon the client is using.
	 */
	public static int getCombatStyle(Container equipment) {
		/**
		 * Get the weapon
		 */
		Item Weapon = equipment.get(Equipment.SLOT_WEAPON);
		/**
		 * If Unarmed, Return MELEETYPE
		 */
		if(Weapon == null)
			return Constants.MELEETYPE;
		/**
		 * Get weaponId
		 */
		int weaponId = Weapon.getId();
		
		if(weaponId == 4212)
			return Constants.RANGEDARROWS;
		/**
		 * Check if using Knifes, Javelins etc
		 */
		for(int g : RANGED_WEAPONS) {
			if(g == weaponId)
				return Constants.RANGEDWEPSTYPE;
		}
		/**
		 * Declare Arrow
		 */
		Item Arrow = equipment.get(Equipment.SLOT_ARROWS);
		/**
		 * Declare arrowId
		 */
		int arrowId = - 1;
		if(Arrow != null) {
			arrowId = Arrow.getId();
		}
		/**
		 * Check if using Hand Cannon etc
		 */
		for(int i = 0; i < UNIQUERANGEWEPS.length; i++) {
			if(UNIQUERANGEWEPS[i][0] == weaponId) {
				if(arrowId == - 1)
					return Constants.UNIQUENOAMMO;
				if(getUniqueType(weaponId) == arrowId) {
					return Constants.UNIQUEAMMO;
				} else {
					return Constants.UNIQUEWRONG;
				}
			}
		}
		/**
		 * Check if using a Bow like Magic shotbow
		 */

		if(arrowId == DRAGON_ARROWS && darkBow(weaponId))
			return Constants.RANGEDARROWS;
		else if(arrowId == DRAGON_ARROWS)
			return Constants.MELEETYPE;

		for(int g : BOWS) {
			if(weaponId == g) {
				if(getArrowType(arrowId) != 0) {
					return Constants.RANGEDARROWS;
				} else {
					return Constants.RANGEDNOARROWS;
				}
			}
		}
		for(Item g : FightPits.rangeItems.get(FightPits.WEAPON)) {
			if(g != null) {
				if(g.getId() == weaponId && getArrowType(arrowId) != 0) {
					return Constants.RANGEDARROWS;
				}	
			}
		}
		/**
		 * Check if using Crossbow
		 */
		for(int g : CBOWS) {
			if(weaponId == g) {
				if(getBoltType(arrowId, weaponId) != 0) {
					return Constants.RANGEDBOLTS;
				} else {
					return Constants.RANGEDNOBOLTS;
				}
			}
		}
		return Constants.MELEETYPE;
	}

	public static final int DRAGON_ARROWS = 11212;


	public static boolean isControlled(int id) {
		Weapon weapon = Weapon.forId(id);
		if(weapon != Weapon.DEFAULT_WEAPON)
			return weapon.isControlled();
		switch(id) {
			case 4151:
			case 15441:
			case 15442:
			case 15443:
			case 15444:
				return true;
		}
		return false;
	}
	
	private static final ArrayList<Integer> exclusivelyNot2h = new ArrayList<Integer>() {
		{
			add(16136);
		}
	};
	public static boolean exclusivelyNotTwoHanded(int id) {
		return exclusivelyNot2h.contains(id);
	}
	public static boolean is2H(int id) {
		Weapon weapon = Weapon.forId(id);
		if(exclusivelyNotTwoHanded(id))
			return false;
		if(weapon != Weapon.DEFAULT_WEAPON)
			return weapon.isTwohanded();
		for(int i : BOWS) {
			if(i == id)
				return true;
		}
		for(int i : TWO_HANDED) {
			if(i == id)
				return true;
		}
		return false;
	}

	public static final int[] TWO_HANDED = {11716, 11717, 13454, 10887, 13905, 13988, 14692, 17143,
			14484, 16909, 18369, 18353, 16425, 4153, 17646, 1307, 1309, 1311, 1313,
			1315, 1317, 1319, 6609, 7158,/* 2h weps */4886, 4887, 4888, 4889,
			4982, 4983, 4984, 4985, 4958, 4959, 4960, 4961,
			4718, 4710, 4755, 4747, 4734, 4726,/* barrows */19605, 11694, 11696,
			11698, 11700, 11730,/* godswords */11235,/* darkbow */10113, 9016,
			6587, 3095, 3096, 3097, 3098, 3099, 3100,/* claws */15701, 15702,
			15703, 15704, 15241, 16909, 6528

	};

	public static final int[] RANGED_WEAPONS = {
	/* knives */864, 863, 865, 866, 867, 868, 869,
	/* darts */806, 807, 808, 809, 810, 811, 3093,
	/* javelins */825, 826, 827, 828, 829, 830,
	/* thorwn axes */800, 801, 802, 803, 804, 805,
			13883, 13879};

	public static final int[] CBOWS = {9174, 9176, 9177, 9179, 9181, 9183,
			9185, 18357, 14121};

	public static final int[][] UNIQUERANGEWEPS = {
			// Wep Id, Wep Ammo
			{15241, 15243}, {4734, 4740}, {14684, 8882}};

	public static final int[] BOWS = {839, 841, 843, 845, 847, 849, 851, 853,
			855, 857, 859, 861, 4212, 4214, 4827, 11235, 15701, 15702, 15703,
			15704, 16337, 16887};

	public static int getArrowType(int arrow) {
		switch(arrow) {
			case 882: // Bronze
				return 1;
			case 884: // Iron
				return 2;
			case 886: // Steel
				return 3;
			case 888: // Mithril
				return 4;
			case 890: // Adamant
				return 5;
			case 892: // Rune
				return 6;
			case 598: // Fire arrows
			case 942:
			case 2533:
			case 2535:
			case 2537:
			case 2539:
			case 2541:
				return 7;
			case 78: // Ice arrows
				return 8;
			case 2866: // Flighted ogre arrow
				return 9;
			case 11212: // dragon arrow
			case 11227: // dragon arrow
			case 11228: // dragon arrow
			case 11229: // dragon arrow
				return 10;
			case 4150: // broad arrow
				return 11;
			case 11217: // dragon fire arrow
				return 12;
			// add bolts here
			default:
				return 0;
		}
	}

	public static int getUniqueType(int wep) {
		for(int i = 0; i < UNIQUERANGEWEPS.length; i++) {
			if(UNIQUERANGEWEPS[i][0] == wep) {
				return UNIQUERANGEWEPS[i][1];
			}
		}
		return - 1;
	}

	public static int getBoltType(int arrow, int weapon) {
		if(weapon == 4734 && arrow == 4740)
			return 2;
		switch(arrow) {
			case 9140:// iron
				return 2;
			case 9141:// steel
				return 3;
			case 9142:// mith
				return 4;
			case 9143:// addy
			case 9243://Diamond
			case 9242://Ruby
				return 5;
			case 9144:// rune
				return 6;
			case 9145:// silver?
				return 2;
			case 9341:// dragon
			case 9244:
				return 7;
			case 9245:
				return 8;
			// add bolts here
			default:
				return 0;
		}
	}

	public static int getDrawback(int weapon, int arrow, int type) {
		switch(weapon) {
			case 15241:
				return 2138;
		/*case 13883:
			return 1839;
		case 13879:
			return 1837;*/
		}
		if(type == 3) {
			// bolts
			return 28;
		} else if(darkBow(weapon)) {
			switch(arrow) {
				case 882: // Bronze arrow
					return 1104;
				case 884: // Iron arrow
					return 1105;
				case 886: // Steel arrow
					return 1106;
				case 888: // Mithril arrow
					return 1107;
				case 890: // Adamant arrow
					return 1108;
				case 892: // Rune arrow
					return 1109;
				case 78: // ice arrow
					return 1110;
				case 11212: // dragon arrow
				case 11227: // dragon arrow
				case 11228: // dragon arrow
				case 11229: // dragon arrow
					return 1111;
				case 4150: // broad arrow
					return 1112;
				case 2532: // fire arrow
				case 2534: // fire arrow
				case 2536: // fire arrow
				case 2538: // fire arrow
				case 2540: // fire arrow
					return 1113;
				case 11217: // dragon fire arrow
					return 1114;
				default:
					System.out.println("Missing drawback :" + arrow);
					return - 1;
			}
		} else if(type == 2) {
			if(weapon == 4212) // Crystal bow.
				return 250;
			switch(arrow) {
				case 882: // Bronze arrow
					return 18;
				case 884: // Iron arrow
					return 19;
				case 886: // Steel arrow
					return 20;
				case 888: // Mithril arrow
					return 21;
				case 890: // Adamant arrow
					return 22;
				case 892: // Rune arrow
					return 24;
				case 11212: // dragon arrow
				case 11227: // dragon arrow
				case 11228: // dragon arrow
				case 11229: // dragon arrow
					return 1116;
				case 4740: // Bolt
					return 28;

				case 78: // ice arrow
					return 25;
				case 4150: // broad arrow
					return 325;
				case 2532: // fire arrow
				case 2534: // fire arrow
				case 2536: // fire arrow
				case 2538: // fire arrow
				case 2540: // fire arrow
				case 11217: // dragon fire arrow
					return 26;
				default:
					System.out.println("Missing drawback :" + arrow);
					return - 1;
			}
		} else if(type == 1) {
			switch(weapon) {
				case 863: // Knife
					return 220;
				case 864:
					return 219;
				case 865:
					return 221;
				case 866:
					return 223;
				case 867:
					return 224;
				case 868:
					return 225;
				case 869:
					return 222;
				case 806: // Dart
					return 233;
				case 807:
					return 232;
				case 808:
					return 234;
				case 809:
					return 235;
				case 810:
					return 236;
				case 811:
					return 237;
				case 3093:
					return 235;
				case 825:// javelins
					return 207;
				case 826:
					return 206;
				case 827:
					return 208;
				case 828:
					return 209;
				case 829:
					return 210;
				case 830:
					return 211;
				case 800:// thrown axe
					return 42;
				case 801:
					return 43;
				case 802:
					return 44;
				case 803:
					return 45;
				case 804:
					return 46;
				case 805:
					return 48;
				default:
					System.out.println("Missing throwDrawback :" + weapon);
					return - 1;
			}
		}
		return - 1;
	}

	public static int getProjectileSpeed(int weaponId) {
		/*
		 * if (c.dbowSpec) return 100;
		 */
		switch(weaponId) {
			case 15241:
				return 55;
		}
		return 70;
	}

	public static int getSlope(int weaponId) {
		switch(weaponId) {
			case 15241:
				return - 1;
		}
		return 16;
	}

	public static int getStartHeight(int weaponId) {
		switch(weaponId) {
			case 15241:
				return 22;
		}
		return 43;
	}

	public static int getEndHeight(int weaponId) {
		switch(weaponId) {
			case 15241:
				return 22;
		}
		return 31;
	}

	public static int getDelay(int weaponId) {
		switch(weaponId) {
			case 15241:
				return 30;
		}
		return 53;
	}

	public static int getArrowGfx(int weapon, int bowType, int type) {
		switch(weapon) {
			case 15241:
				return 2143;
			case 13883:
				return 1839;
			case 13879:
				return 1837;
            case 14684:
                return 2001;
		}

		if(bowType == Constants.RANGEDBOLTS) {
			switch(type) {
				default:
					return 27;// most use gfx 27
			}
		} else if(bowType == Constants.RANGEDARROWS) {
			// Check first if we're using a Crystal bow.
			if(weapon == 4212) // Crystal bow.
				return 250;

			switch(type) {
				case 1: // Bronze
					return 10;
				case 2: // Iron
					return 9;
				case 3: // Steel
					return 11;
				case 4: // Mithril
					return 12;
				case 5: // Admanant
					return 13;
				case 6: // Rune
					return 15;
				case 7: // Fire
					return 17;
				case 8: // Ice
					return 16;
				case 9: // Flighted ogre arrow
					return 471;
				case 10: // dragon arrow
					return 1120;
				case 11: // broad arrow
					return 326;
				case 12: // dragon fire arrow
					return 17;
				default:
					return 0;
			}
		} else if(bowType == Constants.RANGEDWEPSTYPE) {
			switch(weapon) {
				case 863: // Iron knife
					return 213;
				case 864: // Bronze knife
					return 212;
				case 865: // Steel knife
					return 214;
				case 866: // Mithril knife
					return 216;
				case 867: // Adamant knife
					return 217;
				case 868: // Rune knife
					return 218;
				case 869: // Black knife
					return 215;
				case 806: // Bronze dart
					return 226;
				case 807: // Iron dart
					return 227;
				case 808: // Steel dart
					return 228;
				case 809: // Mithril dart
					return 229;
				case 810: // Adamant dart
					return 230;
				case 811: // Rune dart
					return 231;
				case 3093: // Black dart
					return 229;
				default:
					System.out.println("Missing throwGfx :" + weapon);
					return 213;
			}
		}
		return - 1;
	}

	public static boolean isVeracEquiped(Player player) {
		if(player.getEquipment().get(Equipment.SLOT_HELM) == null
				|| player.getEquipment().get(Equipment.SLOT_WEAPON) == null
				|| player.getEquipment().get(Equipment.SLOT_CHEST) == null
				|| player.getEquipment().get(Equipment.SLOT_BOTTOMS) == null)
			return false;
		return player.getEquipment().get(Equipment.SLOT_HELM).getId() == 4753
				&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 4755
				&& player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 4757
				&& player.getEquipment().get(Equipment.SLOT_BOTTOMS).getId() == 4759;
	}

	private static Random random = new Random();

	private static final double DEFENCE_MODIFIER = 0.895;

	public static int damage(int maxHit, Player attacker, Player victim,
	                         int skill, boolean prayer, boolean special, boolean ignorePrayers) {
		/**
		 * Hit is automatically 0 if the attacker is already dead.
		 */
		if(attacker.isDead()) {
			return 0;
		}

		boolean veracEffect = false;
		if(skill == Skills.ATTACK) {
			if(isVeracEquiped(attacker)) {
				if(random.nextInt(8) == 3) {
					veracEffect = true;
				}
			}
		}

		double attackBonus = attacker.getBonus().get(attacker.cE.getAtkType()) == 0 ? 1
				: attacker.getBonus().get(attacker.cE.getAtkType());
		if(attackBonus < 1) {
			attackBonus = 1;
		}
		double attackCalc = attackBonus * attacker.getSkills().getLevel(skill); // +1
		// as
		// its
		// exclusive

		/**
		 * Prayer calculations.
		 */
		if(skill == Skills.ATTACK) {
			// melee attack prayer modifiers
			if(attacker.getPrayers().isEnabled(2)) {
				attackCalc *= 1.05;
			} else if(attacker.getPrayers().isEnabled(7)) {
				attackCalc *= 1.10;
			} else if(attacker.getPrayers().isEnabled(15)) {
				attackCalc *= 1.15;
			} else if(attacker.getPrayers().isEnabled(24)) {
				attackCalc *= 1.15;
			} else if(attacker.getPrayers().isEnabled(25)) {
				attackCalc *= 1.20;
			}
		} else if(skill == Skills.RANGED) {
			// range attack prayer modifiers
			if(attacker.getPrayers().isEnabled(3)) {
				attackCalc *= 1.05;
			} else if(attacker.getPrayers().isEnabled(11)) {
				attackCalc *= 1.10;
			} else if(attacker.getPrayers().isEnabled(19)) {
				attackCalc *= 1.15;
			}
		} else if(skill == Skills.MAGIC) {
			// magic attack prayer modifiers
			if(attacker.getPrayers().isEnabled(4)) {
				attackCalc *= 1.05;
			} else if(attacker.getPrayers().isEnabled(12)) {
				attackCalc *= 1.10;
			} else if(attacker.getPrayers().isEnabled(20)) {
				attackCalc *= 1.15;
			}
		}

		/**
		 * As with the melee/range max hit calcs, combat style bonuses are added
		 * AFTER the modifiers have taken place.
		 */
		if(attacker.cE.getAtkType() == 0) {
			attackCalc += 3;
		} else if(attacker.cE.getAtkType() == 3) {
			attackCalc += 1;
		}

		double defenceBonus = victim.getBonus().get(attacker.cE.getAtkType() + 5) == 0 ? 1
				: victim.getBonus().get(attacker.cE.getAtkType() + 5);
		double defenceCalc = defenceBonus
				* victim.getSkills().getLevel(Skills.DEFENCE); // +1 as its
		// exclusive
		/**
		 * Prayer calculations.
		 */
		if(attacker.getPrayers().isEnabled(0)) {
			defenceCalc *= 1.05;
		} else if(attacker.getPrayers().isEnabled(5)) {
			defenceCalc *= 1.10;
		} else if(attacker.getPrayers().isEnabled(13)) {
			defenceCalc *= 1.15;
		} else if(attacker.getPrayers().isEnabled(24)) {
			defenceCalc *= 1.20;
		} else if(attacker.getPrayers().isEnabled(25)) {
			defenceCalc *= 1.25;
		}

		/**
		 * As with the melee/range max hit calcs, combat style bonuses are added
		 * AFTER the modifiers have taken place.
		 */
		if(attacker.cE.getAtkType() == 2) {
			defenceCalc += 3;
		} else if(attacker.cE.getAtkType() == 3) {
			defenceCalc += 1;
		}

		if(veracEffect) {
			defenceCalc = 0;
		}

		/**
		 * The chance to succeed out of 1.0.
		 */
		double hitSucceed = DEFENCE_MODIFIER * (attackCalc / defenceCalc);
		if(hitSucceed > 1.0) {
			hitSucceed = 1;
		}
		attacker.getActionSender().sendMessage(
				"atk value: " + (hitSucceed * 100));
		if(hitSucceed < random.nextDouble()) {
			return 0;
		} else {
			/**
			 * Protection prayers. Note: If an NPC is hitting on a protection
			 * prayer, it is 100% blocked, where as if a player is hitting on a
			 * protection prayer, their damage is simply reduced by 40%. Also,
			 * if the attacker has the Verac effect active, it will ignore the
			 * opponent's protection prayers.
			 */
			int hit = maxHit;
			double protectionPrayer = 1;
			if(ignorePrayers) {
				protectionPrayer = 1;
			}
			if(prayer) {
				protectionPrayer = 0.4;
			}
			hit = (int) (maxHit * protectionPrayer); // +1 as its exclusive
			return hit;
		}
	}

	/**
	 * Real RuneScape Formulas.
	 */
	public static double getEffectiveStr(Player p) {
		return p.getSkills().getLevel(2) * getPrayerStr(p);
	}

	public static double getPrayerStr(Player p) {
		if(p.getPrayers().isEnabled(1)) {
			return 1.05;
		} else if(p.getPrayers().isEnabled(6)) {
			return 1.10;
		} else if(p.getPrayers().isEnabled(14)) {
			return 1.15;
		} else if(p.getPrayers().isEnabled(24)) {
			return 1.18;
		} else if(p.getPrayers().isEnabled(25)) {
			return 1.23;
			// Curses
		} else if(p.getPrayers().isEnabled(31)) {
			return 1.05;
		} else if(p.getPrayers().isEnabled(44)) {
			return 1.15;
		} else if(p.getPrayers().isEnabled(49)) {
			return 1.32;
		}
		return 1;
	}

	public static int getStyleBonus(Player p) {
		return 1;
		// TODO
	}

	public static int calculateMaxHit(Player p) {
		double base = 0;

		double effective = getEffectiveStr(p);
		double strengthBonus = p.getBonus().get(EquipmentStats.STRENGTH);

		base = (13 + effective + (strengthBonus / 10) + ((effective * strengthBonus) / 70)) / 10;


		if(p.specOn) {
			base = (base * SpecialAttacks.getSpecialBonus(p));
		}
		if(obbyZerk(p)) {
			base *= 1.20;
		}
		if(fullVoidMelee(p)) {
			base *= 1.05;
		}
		if(hasDharokEquiped(p)) {
			double ratio = (double) p.getSkills().getLevel(Skills.HITPOINTS) / (double) p.getSkills().getLevelForExp(Skills.HITPOINTS);
			base *= 1.95 - ratio;
		}
		if(base <= 0) {
			base = 1;
		}
        if(p.getEquipment().getItemId(Equipment.SLOT_RING) == 15707)
            base = p.getDungeoneering().perks.boost(Constants.MELEE, false, base);
		// p.getActionSender().sendMessage("Your max is " + Math.floor(base));
		return (int) Math.floor(base);

	}

	public static boolean obbyZerk(Player p) {
		if(p.getEquipment().get(Equipment.SLOT_AMULET) == null
				|| p.getEquipment().get(Equipment.SLOT_WEAPON) == null) {
			return false;
		}
		return p.getEquipment().get(Equipment.SLOT_AMULET).getId() == 11128
				&& p.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 6528;
	}

	public static boolean fullVoidMelee(Player player) {
		if(player.getEquipment().get(Equipment.SLOT_HELM) == null
				|| player.getEquipment().get(Equipment.SLOT_GLOVES) == null
				|| player.getEquipment().get(Equipment.SLOT_CHEST) == null
				|| player.getEquipment().get(Equipment.SLOT_BOTTOMS) == null)
			return false;
		return player.getEquipment().get(Equipment.SLOT_HELM).getId() == 11665
				&& player.getEquipment().get(Equipment.SLOT_GLOVES).getId() == 8842
				&& player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 8839
				&& player.getEquipment().get(Equipment.SLOT_BOTTOMS).getId() == 8840;
	}

	public static boolean fullVoidRange(Player player) {
		if(player.getEquipment().get(Equipment.SLOT_HELM) == null
				|| player.getEquipment().get(Equipment.SLOT_GLOVES) == null
				|| player.getEquipment().get(Equipment.SLOT_CHEST) == null
				|| player.getEquipment().get(Equipment.SLOT_BOTTOMS) == null)
			return false;
		return player.getEquipment().get(Equipment.SLOT_HELM).getId() == 11664
				&& player.getEquipment().get(Equipment.SLOT_GLOVES).getId() == 8842
				&& player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 8839
				&& player.getEquipment().get(Equipment.SLOT_BOTTOMS).getId() == 8840;
	}

    /**
     * farseer
     * @param player
     * @return
     */
    public static boolean wearingFarseer(Player player) {
        if(player.getEquipment().get(Equipment.SLOT_SHIELD) == null)
            return false;
		return player.getEquipment().get(Equipment.SLOT_SHIELD).getId() == 18363;
	}

    public static boolean fullVoidMage(Player player) {
		if(player.getEquipment().get(Equipment.SLOT_HELM) == null
				|| player.getEquipment().get(Equipment.SLOT_GLOVES) == null
				|| player.getEquipment().get(Equipment.SLOT_CHEST) == null
				|| player.getEquipment().get(Equipment.SLOT_BOTTOMS) == null)
			return false;
		return player.getEquipment().get(Equipment.SLOT_HELM).getId() == 11663
				&& player.getEquipment().get(Equipment.SLOT_GLOVES).getId() == 8842
				&& player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 8839
				&& player.getEquipment().get(Equipment.SLOT_BOTTOMS).getId() == 8840;
	}

	/**
	 * Checks if a player has the full dharok set equiped.
	 *
	 * @param player
	 * @return
	 */
	public static boolean hasDharokEquiped(Player player) {
		if(player.getEquipment().get(Equipment.SLOT_HELM) == null)
			return false;
		if(player.getEquipment().get(Equipment.SLOT_HELM).getId() != 4716)
			return false;
		if(player.getEquipment().get(Equipment.SLOT_WEAPON) == null)
			return false;
		if(player.getEquipment().get(Equipment.SLOT_WEAPON).getId() != 4718)
			return false;
		if(player.getEquipment().get(Equipment.SLOT_CHEST) == null)
			return false;
		if(player.getEquipment().get(Equipment.SLOT_CHEST).getId() != 4720)
			return false;
		if(player.getEquipment().get(Equipment.SLOT_BOTTOMS) == null)
			return false;
		return player.getEquipment().get(Equipment.SLOT_BOTTOMS).getId() == 4722;
	}

	public static int getRangedHit2(Player p) {
		int effectiveStrength = p.getSkills().getLevel(4);
		if(p.cE.bowType == 0)
			effectiveStrength += 3;
		int max = (int) (1.3 + (effectiveStrength / 10)
				+ (p.getBonus().get(EquipmentStats.ATTACK_RANGED) / 80) + ((p.getBonus().get(EquipmentStats.STRENGTH) * p.getBonus().get(EquipmentStats.ATTACK_RANGED)) / 640));
		return max;
	}

	public static boolean darkBow(int value) {
		return value == 11235
				|| value == 13405
				|| value == 15701
				|| value == 15702
				|| value == 15703
				|| value == 15704
				|| value == 16337
				|| value == 16887
				|| FightPits.isBow(value);
	}

	/**
	 * Get the ranged hit.
	 *
	 * @param maxHit the current maxhit for melee
	 * @param type   The type of the arrow.
	 */

	public static int getRangedHit(int maxHit, int type) {
		/*
		 * switch(type) { case 1: // Bronze return maxHit + (int)
		 * Math.floor(Math.random() * 1); case 2: // Iron return maxHit + (int)
		 * Math.floor(Math.random() * 2); case 3: // Steel return maxHit + (int)
		 * Math.floor(Math.random() * 3); case 4: // Mithril return maxHit +
		 * (int) Math.floor(Math.random() * 4); case 5: // Admanant return
		 * maxHit + (int) Math.floor(Math.random() * 5); case 6: // Rune return
		 * maxHit + (int) Math.floor(Math.random() * 6); case 7: // Fire return
		 * maxHit + (int) Math.floor(Math.random() * 7); case 8: // Ice return
		 * maxHit + (int) Math.floor(Math.random() * 8); case 9: // Flighted
		 * ogre arrow return maxHit + (int) Math.floor(Math.random() * 8);
		 * default: // Type is 0 return 2; }
		 */
		int am = type;
		if(am > 8)
			am = 8;
		maxHit += type;
		return maxHit;
	}

	public static final int[][] RANGEDDATA = {{877, 10}, {9140, 46},
			{9145, 36}, {9141, 64}, {9142, 82}, {9143, 100},
			{9144, 115}, {9236, 14}, {9237, 30}, {9238, 48},
			{9239, 66}, {9240, 83}, {9241, 85}, {9242, 103},
			{9243, 105}, {9244, 117}, {9245, 125}, {882, 7},
			{884, 10}, {886, 16}, {888, 22}, {890, 31}, {892, 49},
			{4740, 55}, {11212, 60}, {806, 1}, {807, 3}, {808, 4},
			{809, 7}, {810, 10}, {811, 14}, {11230, 20}, {864, 3},
			{863, 4}, {865, 7}, {866, 10}, {867, 14}, {868, 24},
			{825, 6}, {826, 10}, {827, 12}, {828, 18}, {829, 28},
			{830, 42}, {800, 5}, {801, 7}, {802, 11}, {803, 16},
			{804, 23}, {805, 36}, {9976, 0}, {9977, 15}, {4212, 70},
			{4214, 70}, {4215, 70}, {4216, 70}, {4217, 70},
			{4218, 70}, {4219, 70}, {4220, 70}, {4221, 70},
			{4222, 70}, {4223, 70}, {6522, 49}, {10034, 15},
			{15015, 130}, {15016, 110}, {4734, 49}, {4740, 49},
			{19157, 80}, {19162, 80}, {19152, 80}, {15243, 140},
			{13883, 130}, {13879, 135}, {8882, 145}
	};


	public static int getRangeStr(int i) {
		int str = 0;
		for(int l = 0; l < RANGEDDATA.length; l++) {
			if(i == RANGEDDATA[l][0]) {
				str = RANGEDDATA[l][1];
			}
		}
		return str;
	}

	public static int calculateRangeMaxHit(Player p) {
		int rangedLevel = p.getSkills().getLevelForExp(4);
        int rangedBoosted = p.getSkills().getLevel(4);
        int rangedStr = 0;

		if(CombatAssistant.getCombatStyle(p.cE) == Constants.RANGEDWEPSTYPE)
			rangedStr = getRangeStr(p.getEquipment().get(Equipment.SLOT_WEAPON)
					.getId());
		else {
			if(p.getEquipment().get(Equipment.SLOT_ARROWS) != null)
			rangedStr = getRangeStr(p.getEquipment().get(Equipment.SLOT_ARROWS)
					.getId());
            if(p.getEquipment().getItemId(Equipment.SLOT_WEAPON) != 14684 && p.getEquipment().getItemId(Equipment.SLOT_ARROWS) == 8882)
                rangedStr = 80;
        }
		double bonus = 1.00;
		if(p.getPrayers().isEnabled(3) || p.getPrayers().isEnabled(32)) {
			bonus = 1.05;
		} else if(p.getPrayers().isEnabled(11)) {
			bonus = 1.10;
		} else if(p.getPrayers().isEnabled(19) || p.getPrayers().isEnabled(41)) {
			bonus = 1.15;
		}  else if (p.getPrayers().isEnabled(26))
            bonus = 1.25;
		if(fullVoidRange(p)) {
			bonus += .15;
		}
		double e = Math.floor(rangedLevel * bonus + rangedBoosted / 8);
		if(p.getEquipment().get(Equipment.SLOT_WEAPON) != null)
			if(p.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 4212)
				rangedStr = 70;
		/*
		 * if (c.fightMode == 0) { e = (e + 3.0); }
		 */
		double specialBonus = 1.0;

		if(p.specOn && p.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			switch(p.getEquipment().get(Equipment.SLOT_WEAPON).getId()) {
				case 13883:
					specialBonus = 1.15;
					break;
				case 19149:
				case 19143:
				case 19146:
				case 859:
					specialBonus = 1.3;
					break;
				case 861:
					specialBonus = 1.3;
					break;
				case 11235:
				case 15701:
				case 15702:
				case 15703:
				case 15704:
					if(p.getEquipment().get(Equipment.SLOT_ARROWS) != null
							&& p.getEquipment().get(Equipment.SLOT_ARROWS).getId() == 11212) {
						specialBonus = 1.3;
					} else {
						specialBonus = 1.1;
					}
					break;
				case 15241:
					specialBonus = 1.05;
			}
		}
		double max = (1.3 + e / 8 + rangedStr / 80 + e * rangedStr / 640);
		//p.getActionSender().sendMessage("Range max : " + max);
		max *= specialBonus;
        if(p.getEquipment().getItemId(Equipment.SLOT_RING) == 15707)
            max = p.getDungeoneering().perks.boost(Constants.RANGE, false, max);
		return (int) max;
	}

	public static int calculateRangeAttack(Player p) {
		double rangeAtk = p.getSkills().getLevel(4);
		if(p.getPrayers().isEnabled(3) || p.getPrayers().isEnabled(32)) {
			rangeAtk *= 1.05;
		} else if(p.getPrayers().isEnabled(11)) {
			rangeAtk *= 1.10;
		} else if(p.getPrayers().isEnabled(19) || p.getPrayers().isEnabled(41)) {
			rangeAtk *= 1.15;
		}
		if(fullVoidRange(p))
			rangeAtk *= 1.15;
        if(p.getEquipment().getItemId(Equipment.SLOT_RING) == 15707)
            rangeAtk = p.getDungeoneering().perks.boost(Constants.RANGE, true, rangeAtk);
		return (int) (((rangeAtk * 0.55) + (p.getBonus().get(EquipmentStats.ATTACK_RANGED)/2.5)));
	}

	public static int calculateRangeDefence(Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player)entity;
			int rangeDef = player.getSkills().getLevel(1);
			if(player.getPrayers().isEnabled(0)) {
				rangeDef *= 1.05;
			} else if(player.getPrayers().isEnabled(5)) {
				rangeDef *= 1.10;
			} else if(player.getPrayers().isEnabled(13)) {
				rangeDef *= 1.15;
			} else if(player.getPrayers().isEnabled(24)) {
				rangeDef *= 1.20;
			} else if(player.getPrayers().isEnabled(25)) {
				rangeDef *= 1.25;
			} else if(player.getPrayers().isEnabled(43)) {
				rangeDef *= 1.20;
			} else if(player.getPrayers().isEnabled(49)) {
				rangeDef *= 1.15;
			} else if (player.getPrayers().isEnabled(27) || player.getPrayers().isEnabled(26))
                rangeDef *= 1.23;
			return (int)(rangeDef * .5) + (int)(player.getBonus().get(EquipmentStats.DEFENCE_RANGED)/2.5) + 64;
		} else //NPCs
			return (int)(entity.cE.getCombat()/2.5) + 74;
	}

    public static int calculateMageSplashDef(Entity entity) {
        if (entity instanceof Player)
			return (int) (((Player) entity).getBonus().get(EquipmentStats.DEFENCE_MAGIC) * 1.6) + (((Player) entity).getSkills().getLevel(6) / 3);
		else
            return entity.getCombat().getCombat()/2;
    }


	public static int calculateMageAtk(Player player) {
		int mageLvl = player.getSkills().getLevel(6);
		int bonus = player.getBonus().get(EquipmentStats.ATTACK_MAGIC);
		if(player.getPrayers().isEnabled(4) || player.getPrayers().isEnabled(33)) {
			mageLvl *= 1.05;
		} else if(player.getPrayers().isEnabled(12) || player.getPrayers().isEnabled(42)) {
			mageLvl *= 1.10;
		} else if(player.getPrayers().isEnabled(20)) {
			mageLvl *= 1.15;
		} else if(player.getPrayers().isEnabled(27))
            mageLvl *= 1.25;
        if(player.getEquipment().getItemId(Equipment.SLOT_RING) == 15707)
            bonus = (int)player.getDungeoneering().perks.boost(Constants.MAGE, true, bonus);
		return mageLvl +
				(int) (bonus * 1.55) + 5;
	}

	public static int calculateMageDef(Entity entity) {
		if(entity == null) return 0;
		if(entity instanceof Player) {
			Player player = (Player) entity;
			int defLevel = player.getSkills().getLevel(1);
			int mageLevel = player.getSkills().getLevel(6);
			int bonus = player.getBonus().get(EquipmentStats.DEFENCE_MAGIC);
			if(player.getPrayers().isEnabled(0)) {
				defLevel *= 1.05;
			} else if(player.getPrayers().isEnabled(5)) {
				defLevel *= 1.10;
			} else if(player.getPrayers().isEnabled(13)) {
				defLevel *= 1.15;
			} else if(player.getPrayers().isEnabled(24)) {
				defLevel *= 1.20;
			} else if(player.getPrayers().isEnabled(25)) {
				defLevel *= 1.25;
			} else if(player.getPrayers().isEnabled(43)) {
				defLevel *= 1.20;
			} else if(player.getPrayers().isEnabled(49)) {
				defLevel *= 1.15;
			}
			//Mage Prayers
			if(player.getPrayers().isEnabled(4) || player.getPrayers().isEnabled(33)) {
				mageLevel *= 1.05;
			} else if(player.getPrayers().isEnabled(12)) {
				mageLevel *= 1.10;
			} else if(player.getPrayers().isEnabled(20) || player.getPrayers().isEnabled(42)) {
				mageLevel *= 1.15;
			} else if(player.getPrayers().isEnabled(27))
                mageLevel *= 1.23;
			
			return (int)(bonus * 1.34) + defLevel / 2 + mageLevel + 13;
		} else //NPCs
			return (int)(entity.cE.getCombat()/2.2) + 20;
	}

	public static int bestMeleeDef(Player player) {
		if(player.getBonus().get(5) > player.getBonus().get(6)
				&& player.getBonus().get(5) > player.getBonus().get(7))
			return 5;
		if(player.getBonus().get(6) > player.getBonus().get(5)
				&& player.getBonus().get(6) > player.getBonus().get(7))
			return 6;
		return player.getBonus().get(7) <= player.getBonus().get(5)
				|| player.getBonus().get(7) <= player.getBonus().get(6) ? 5 : 7;
	}

	public static int bestMeleeAtk(Player player) {
		if(player.getBonus().get(0) > player.getBonus().get(1)
				&& player.getBonus().get(0) > player.getBonus().get(2))
			return 0;
		if(player.getBonus().get(1) > player.getBonus().get(0)
				&& player.getBonus().get(1) > player.getBonus().get(2))
			return 1;
		return player.getBonus().get(2) <= player.getBonus().get(0)
				|| player.getBonus().get(2) <= player.getBonus().get(1) ? 0 : 2;
	}

	/**
	 * Calculate Melee Atk Value
	 */
	public static int calculateMeleeAttack(Player player) {
		double meleeAtk = player.getSkills().getLevel(0);
		if(player.getPrayers().isEnabled(2) || player.getPrayers().isEnabled(31)) {
			meleeAtk *= 1.05;
		} else if(player.getPrayers().isEnabled(7)) {
			meleeAtk *= 1.10;
		} else if(player.getPrayers().isEnabled(15) || player.getPrayers().isEnabled(40)) {
			meleeAtk *= 1.15;
		} else if(player.getPrayers().isEnabled(24)) {
			meleeAtk *= 1.17;
		} else if(player.getPrayers().isEnabled(25)) {
			meleeAtk *= 1.20;
		} else if(player.getPrayers().isEnabled(49)) {
			meleeAtk *= 1.25;
		}


		double bonus = player.getBonus().get(0);
		for(int i = 1; i < 3; i++) {
			if(player.getBonus().get(i) > bonus) {
				bonus = player.getBonus().get(i);
			}
		}
        if(player.isNewlyCreated() && player.duelAttackable < 1 && player.getAccountValue().getTotalValue() < 10000)
            bonus *= 1.15;

		return (int) ((meleeAtk * .3 + (bonus * 0.52)));

	}

	/**
     * @return Melee Defence
	 */
	public static int calculateMeleeDefence(Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player)entity;
			int meleeDef = player.getSkills().getLevel(1);
			if(player.getPrayers().isEnabled(0)) {
				meleeDef *= 1.05;
			} else if(player.getPrayers().isEnabled(5)) {
				meleeDef *= 1.10;
			} else if(player.getPrayers().isEnabled(13)) {
				meleeDef *= 1.15;
			} else if(player.getPrayers().isEnabled(24)) {
				meleeDef *= 1.20;
			} else if(player.getPrayers().isEnabled(25)) {
				meleeDef *= 1.25;
			} else if(player.getPrayers().isEnabled(43)) {
				meleeDef *= 1.20;
			} else if(player.getPrayers().isEnabled(49)) {
				meleeDef *= 1.15;
			} else if(player.getPrayers().isEnabled(26) || player.getPrayers().isEnabled(27)) //rigour and augury
                meleeDef *= 1.23;
			double bonus = player.getBonus().get(5);
			for(int i = 6; i < 8; i++) {
				if(player.getBonus().get(i) > bonus) {
					bonus = player.getBonus().get(i);
				}
			}

            if(player.isNewlyCreated() && player.duelAttackable < 1 && player.getAccountValue().getTotalValue() < 10000)
                bonus *= 1.15;

			return (int) (meleeDef * .25 + bonus * 0.45) + 64;
		} else {
			return (int)(entity.cE.getCombat()/2.5) + 74;
		}
	}

	public static void drawBackGfx(CombatEntity combatEntity, int weaponId,
	                               int arrowId, int bowType) {
		if(weaponId != 15241)
			combatEntity.doGfx(CombatAssistant.getDrawback(weaponId, arrowId,
					bowType));
		else
			combatEntity.getPlayer().playGraphics(Graphic.create(2138));
	}

	/**
	 * Sending Projectile part.
	 */
	public static void fireProjectile(CombatEntity combatEntity, int bowType,
	                                  int arrowType) {
		if(combatEntity.getOpponent() == null)
			return;
		int weaponId = - 1;
		if(combatEntity.getPlayer().getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			weaponId = combatEntity.getPlayer().getEquipment()
					.get(Equipment.SLOT_WEAPON).getId();
		}
		int proj = CombatAssistant.getArrowGfx(weaponId, bowType, arrowType);
		int offsetY = (combatEntity.getAbsX() - (combatEntity.getOpponent()
				.getAbsX() + combatEntity.getOpponent().getOffsetX())) * - 1;
		int offsetX = (combatEntity.getAbsY() - (combatEntity.getOpponent()
				.getAbsY() + combatEntity.getOpponent().getOffsetY())) * - 1;
		// Lockon Target
		int hitId = combatEntity.getSlotId(combatEntity.getEntity());
		int speed = CombatAssistant.getProjectileSpeed(weaponId);
		int slope = CombatAssistant.getSlope(weaponId);
		int startHeight = CombatAssistant.getStartHeight(weaponId);
		int endHeight = CombatAssistant.getEndHeight(weaponId);
		int time = CombatAssistant.getDelay(weaponId);
		// create the projectile
		combatEntity
				.getPlayer()
				.getActionSender()
				.createGlobalProjectile(combatEntity.getAbsY(),
						combatEntity.getAbsX(), offsetY, offsetX, 50, speed,
						proj, startHeight, endHeight, hitId, time, slope);
		if(weaponId != 4212)
		Combat.removeArrow(combatEntity.getPlayer(), bowType, combatEntity
				.getOpponent().getEntity().getPosition());
	}

}