package org.hyperion.rs2.model.newcombat;

public class EquipmentStats {

	public static final int
			ATTACK_STAB = 0,
			ATTACK_SLASH = 1,
			ATTACK_CRUSH = 2,
			ATTACK_MAGIC = 3,
			ATTACK_RANGED = 4,
			DEFENCE_STAB = 5,
			DEFENCE_SLASH = 6,
			DEFENCE_CRUSH = 7,
			DEFENCE_MAGIC = 8,
			DEFENCE_RANGED = 9,
			STRENGTH = 10,
			PRAYER = 11,
			SIZE = 12;


	private int[] stats;

	public EquipmentStats() {
		stats = new int[SIZE];
	}

	public int get(int index) {
		return stats[index];
	}

	public void set(int index, int value) {
		stats[index] = value;
	}

	public void add(int index, int value) {
		stats[index] += value;
	}

	public void reset() {
		for(int i = 0; i < SIZE; i++) {
			stats[i] = 0;
		}
	}
}
