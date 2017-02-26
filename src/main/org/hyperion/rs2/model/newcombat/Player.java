package org.hyperion.rs2.model.newcombat;

public class Player extends Entity {

	private Skills skills = new Skills();

	private Prayers prayers = new Prayers(false);

	private Container equipment = new BonusEquipment(Container.Type.STANDARD, Equipment.SIZE);

	private EquipmentStats bonus = new EquipmentStats();

	public EquipmentStats getBonus() {
		return bonus;
	}

	public Container getEquipment() {
		return equipment;
	}

	public Skills getSkills() {
		return skills;
	}

	public Prayers getPrayers() {
		return prayers;
	}

	public void set(String key, String params) {
		if(key.equals("skill")) {
			String[] values = params.split("-");
			int skill = Integer.parseInt(values[0]);
			int level = Integer.parseInt(values[1]);
			int xplevel = Integer.parseInt(values[2]);
			skills.setSkill(skill, level, skills.getXPForLevel(xplevel));
		} else if(key.equals("bonus")) {
			String[] values = params.split("-");
			int slot = Integer.parseInt(values[0]);
			int value = Integer.parseInt(values[1]);
			bonus.set(slot, value);
		} else if(key.equals("prayer")) {
			int id = Integer.parseInt(params);
			prayers.setEnabled(id, true);
		}
	}
}
