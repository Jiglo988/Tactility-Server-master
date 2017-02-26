package org.hyperion.rs2.model.newcombat;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Arsen Maxyutov.
 */
public class DamageGenerator {

	/**
	 * The random number generator.
	 */
	public static final Random RAND = new Random();

	/**
	 * The attacker.
	 */
	private final Entity attacker;

	/**
	 * The opponent.
	 */
	private final Entity opponent;

	/**
	 * The max hit.
	 */
	private final int maxHit;

	/**
	 * The attacking skill.
	 */
	private final int skill;

	public static double baseAttackBonus = 1;

	public static double baseDefendBonus = 1;

	public static double weaponBonus = 1;

	public static double armorBonus = 1;

	/**
	 * Creates a new damage generator.
	 *
	 * @param attacker
	 * @param opponent
	 */
	public DamageGenerator(Entity attacker, Entity opponent, int skill) {
		this.attacker = attacker;
		this.opponent = opponent;
		this.skill = skill == Skills.STRENGTH ? Skills.ATTACK : skill;
		if(attacker instanceof Player && opponent instanceof Player) {
			System.out.println("[Attacker:]");
			Player player = (Player) attacker;
			printPlayerData(player);
			player = (Player) opponent;
			System.out.println("[Opponent:]");
			printPlayerData(player);

		}
		if(attacker instanceof Player) {
			this.maxHit = calculateMaxHit((Player) attacker, skill);
		} else {
			this.maxHit = calculateMaxHit((NPC) attacker);
		}

	}

	public void printPlayerData(Player player) {
		System.out.print("Skills: ");
		for(int i = Skills.ATTACK; i <= Skills.MAGIC; i++) {
			int level = player.getSkills().getLevel(i);
			if(level != 1)
				System.out.print(Skills.SKILL_NAME[i] + ": " + level + ", ");
		}
		//System.out.println();
		System.out.print("Prayers: ");
		for(int i = 0; i < Prayers.SIZE; i++) {
			if(player.getPrayers().isEnabled(i))
				System.out.println(i + ", ");
		}
		System.out.print("Bonuses:");
		for(int i = 0; i < EquipmentStats.SIZE; i++) {
			int bonus = player.getBonus().get(i);
			System.out.print(bonus + ", ");
		}
		//System.out.println();
	}

	/**
	 * Generates a random hit.
	 *
	 * @return
	 */
	public int next() {
		int delta = calculateSkillAttack(attacker, skill) - calculateSkillDefence(opponent, skill);
		return next(delta);
	}

	/**
	 * Generates a random hit with the specified difference in skill bonus.
	 *
	 * @param delta
	 * @return
	 */
	public int next(int delta) {
		int hit = RAND.nextInt(maxHit + 1);
		hit += (int) (RAND.nextDouble() * delta);
		if(hit < 0)
			hit = 0;
		while(hit > maxHit) {
			hit = next(delta);
		}
		return hit;
	}

	private static int calculateMaxHit(NPC npc) {
		return 10;
	}

	/**
	 * Calculates the max hit.
	 *
	 * @param player
	 * @return
	 */
	private static int calculateMaxHit(Entity entity, int skill) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
	        /*
			 * Calculate effective strength
			 */
			double effective = 8 + player.getSkills().getLevel(skill) *
					player.getPrayers().getBonus(skill);
			//System.out.println("Effective: " + effective);
			effective = Math.round(effective);
			if(player.getPrayers().isEnabled(Prayers.CURSE_TURMOIL))
				effective += 9;
			effective += getStyleBonus(0);
			BonusEquipment bonusEquipment = (BonusEquipment) player.getEquipment();
			effective *= bonusEquipment.getBonus(player, skill);
			/*
			 * Max hit calculation
			 */
			int bonus = 0;
			if(skill == Skills.RANGED) {
				bonus = player.getBonus().get(EquipmentStats.ATTACK_RANGED);
			} else if(skill == Skills.ATTACK) {
				bonus = player.getBonus().get(EquipmentStats.STRENGTH);
			}
			double base = 5 + effective * (1 + bonus / 64.0);
			int max = (int) Math.floor(base);
			max = max > 1 ? max : 1;
			System.out.println("Max hit " + max);
			return max;
		} else {
			return 10;
		}
	}

	/**
	 * Gets the style bonus.
	 *
	 * @param player
	 * @return
	 */
	private static int getStyleBonus(int attacktype) {
		return 1;
	}

	/**
	 * Calculates the Player's attack bonus for the specified skill.
	 *
	 * @param player
	 * @param skill
	 * @return
	 */
	public static int calculateSkillAttack(Entity entity, int skill) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
			double base = player.getSkills().getLevel(skill);
			base *= player.getPrayers().getBonus(skill);
			BonusEquipment bonusEquipment = (BonusEquipment) player.getEquipment();
			base *= bonusEquipment.getBonus(player, skill);
			int bonusSlot = EquipmentStats.ATTACK_RANGED;
			if(skill == Skills.ATTACK) {
				bonusSlot = EquipmentStats.ATTACK_STAB + RAND.nextInt(3);
			}
			return (int) (player.getBonus().get(bonusSlot) * weaponBonus + base);
		} else {
			return 0;
		}
	}

	/**
	 * Calculates the defence for the given skill.
	 *
	 * @param player
	 * @param skill
	 * @return
	 */
	public static int calculateSkillDefence(Entity entity, int skill) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
			int base = player.getSkills().getLevel(Skills.DEFENCE);
			base *= player.getPrayers().getBonus(Skills.DEFENCE);
			int bonusSlot = EquipmentStats.ATTACK_RANGED;
			switch(skill) {
				case Skills.ATTACK:
					bonusSlot = EquipmentStats.DEFENCE_STAB + RAND.nextInt(3);
				case Skills.MAGIC:
					bonusSlot = EquipmentStats.DEFENCE_MAGIC;
			}
			return player.getBonus().get(bonusSlot) + base;
		} else {
			return 10;
		}
	}

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please enter the skill you want to test: ");
			System.out.println("Enter 2 for melee, 4 for ranged and 6 for magic");
			int skill = scanner.nextInt();
			if(skill % 2 != 0 || skill > 6) {
				System.out.println("Wrong id..Shutting down");
				scanner.close();
				System.exit(0);
			}
			ConfigLoader loader = new ConfigLoader();
			DamageGenerator dg = new DamageGenerator(loader.getAttacker(), loader.getDefender(), skill);
			for(int i = 0; i < 100; i++) {
				System.out.println("Damage generated: " + dg.next());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
