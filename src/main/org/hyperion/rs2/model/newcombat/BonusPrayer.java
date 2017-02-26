package org.hyperion.rs2.model.newcombat;

public class BonusPrayer {

	private final int id;

	private final int skill;

	private final double bonus;

	/**
	 * @param id
	 * @param skill
	 * @param bonus
	 */
	public BonusPrayer(int id, int skill, double bonus) {
		this.id = id;
		this.skill = skill;
		this.bonus = bonus;
	}

	public int getSkill() {
		return skill;
	}

	public double getBonus() {
		return bonus;
	}

	public int getId() {
		return id;
	}
}

