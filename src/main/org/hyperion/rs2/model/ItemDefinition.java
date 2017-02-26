package org.hyperion.rs2.model;

import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.combat.weapons.WeaponManager;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;
import org.hyperion.rs2.model.container.impl.WeaponAnimManager;
import org.hyperion.rs2.model.content.minigame.FightPits;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The item definition manager.
 *
 * @author Vastico
 * @author Graham Edgecombe
 */
public class ItemDefinition {

	/**
	 * The configuration file.
	 */
	public static final File CONFIG_FILE = new File("./data/itemconfigs.cfg");

	/**
	 * The maximum item id.
	 */
	public static final int MAX_ID = 23000;

	/**
	 * Dumps the configuration file into the <code>CONFIG_FILE</code>
	 */
	public static void dumpItemDefinitions() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(CONFIG_FILE));
			for(ItemDefinition def : definitions) {
				if(def == null) continue;
				out.write(def.toString());
				out.newLine();
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The definition array.
	 */
	public static ItemDefinition[] definitions;

	/**
	 * Gets a definition for the specified id.
	 *
	 * @param id The id.
	 * @return The definition.
	 */
	public static ItemDefinition forId(int id) {
		if(id >= 0 && id < definitions.length)
			return definitions[id];
		return null;
	}

	private static final int[] ITEM_NON_TRADEABLE = {2412, 2413, 2414, 2570, 2571, 2560, 2561,
			11056, 11057, 11051, 11052, 11055, 11053, 2558, 11337, 11338, 2556,
			2554, 4067, 4511, 4509, 4510, 4508, 4512, 10547, 10548, 10549,
			10550, 7806, 7807, 7808, 7809, 4566, 8850, 10551, 8839, 8840, 8842,
			11663, 11664, 11665, 3842, 3844, 3840, 8844, 8845, 8846, 8847,
			8848, 8849, 8850, 10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457,
			7456, 7455, 7454, 8839, 8840, 8842, 11663, 11664, 11665, 10499,
			9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799,
			9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749,
			9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806,
			9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 13350,
			9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804,
			9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 11793, 11794,
			11795, 11796, 11798, 6858, 6859, 6860, 6861, 6856, 6857, 15441,
			15442, 15443, 15444, 15600, 15606, 15612, 15618, 15602, 15608,
			15614, 15620, 15604, 15610, 15616, 15622, 15021, 15022, 15023,
			15024, 15025, 15026, 15027, 15028, 15029, 15030, 15031, 15032,
			15033, 15034, 15035, 15036, 15037, 15038, 15039, 15040, 15041,
			15042, 15043, 15044, 18350, 18352, 18354, 18356, 18358, 18360,
			12158, 12159, 12160, 12161, 12163, 12162, 12164, 12165, 12166,
			12167, 12168, 19780, 13351, 19669, 19111, 19713, 19716, 19719, 19815, 10858,
			19816, 19817, 19815, 2430, 15332, 15333, 15334, 15335, 15334, 17061, 17193, 17339,
			17215, 17317, 16887, 16337, 18349, 18351, 18353, 12747, 12744, 10025, 10026, 17999};

	public static Map<Integer, Object> nonTradesables = new HashMap<Integer, Object>();

	/**
	 * Loads the item definitions.
	 *
	 * @throws IOException           if an I/O error occurs.
	 * @throws IllegalStateException if the definitions have been loaded already.
	 */


	public static void init() {
		if(definitions != null) {
			throw new IllegalStateException("Definitions already loaded.");
		}
		definitions = new ItemDefinition[MAX_ID];
		loadItems();
	}

	public static void loadItems() {
		try (BufferedReader in = new BufferedReader(new FileReader(CONFIG_FILE))) {
			String line;
			while ((line = in.readLine()) != null) {
				try {
					ItemDefinition definition = ItemDefinition.forString(line);
					definitions[definition.getId()] = definition;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error reading config file: " + line);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Id.
	 */
	private final int id;

	/**
	 * Name.
	 */
	private String name;

	/**
	 * Description.
	 */
	private String examine;

	/**
	 * Noted flag.
	 */
	private final boolean noted;

	/**
	 * Noteable flag.
	 */
	private final boolean noteable;

	/**
	 * Stackable flag.
	 */
	private boolean stackable;

	/**
	 * Non-noted id.
	 */
	private final int parentId;

	/**
	 * Noted id.
	 */
	private final int notedId;

	/**
	 * High alc value.
	 */
	private int highAlcValue;

	/**
	 * The armour slot.
	 */
	private int armourSlot;

	/**
	 * The item bonuses.
	 */
	private int[] bonus = new int[12];

	/**
	 * Item Speed (If Weapon)
	 */
	private int weaponSpeed;

	/**
	 * Creates the item definition.
	 *
	 * @param id            The id.
	 * @param name          The name.
	 * @param examine       The description.
	 * @param noted         The noted flag.
	 * @param noteable      The noteable flag.
	 * @param stackable     The stackable flag.
	 * @param parentId      The non-noted id.
	 * @param notedId       The noted id.
	 * @param highAlcValue  The high alc value.
	 * @param armourSlot    The armour slot.
	 * @param weaponSpeed   The weapon speed
	 * @param bonus The item bonuses
	 */
	private ItemDefinition(int id, String name, String examine, boolean noted,
	                       boolean noteable, boolean stackable, int parentId, int notedId, int highAlcValue,
	                       int armourSlot, int weaponSpeed, int[] bonus) {
		//System.out.println("New itemdef for id : " + id);
		this.id = id;
		this.name = name;
		this.examine = examine;
		this.noted = noted;
		this.noteable = noteable;
		this.stackable = stackable;
		this.parentId = parentId;
		this.notedId = notedId;
		this.highAlcValue = highAlcValue > 0 ? highAlcValue : 1;
		this.armourSlot = armourSlot;
		this.bonus = bonus;
		this.weaponSpeed = weaponSpeed;
		if(Equipment.equipmentTypes.get(id) != null)
			return;
		Weapon weapon = Weapon.getWeapon(name, id);

		if(weapon != null) {
			WeaponManager.getManager().put(id, weapon);

		}
		if(this.armourSlot == 16) {
			Equipment.equipmentTypes.put(id, EquipmentType.FULL_HELM);
		}
		if(this.armourSlot == 17) {
			Equipment.equipmentTypes.put(id, EquipmentType.FULL_MASK);
		}
		if(this.armourSlot == 1) {
			Equipment.equipmentTypes.put(id, EquipmentType.CAPE);
		}
		if(this.armourSlot == 10) {
			Equipment.equipmentTypes.put(id, EquipmentType.BOOTS);
		}
		if(this.armourSlot == 9) {
			Equipment.equipmentTypes.put(id, EquipmentType.GLOVES);
		}
		if(this.armourSlot == 5) {
			Equipment.equipmentTypes.put(id, EquipmentType.SHIELD);
		}
		if(this.armourSlot == 0) {
			Equipment.equipmentTypes.put(id, EquipmentType.HAT);
		}
		if(this.armourSlot == 2) {
			Equipment.equipmentTypes.put(id, EquipmentType.AMULET);
		}
		if(this.armourSlot == 13) {
			Equipment.equipmentTypes.put(id, EquipmentType.ARROWS);
		}
		if(this.armourSlot == 12) {
			Equipment.equipmentTypes.put(id, EquipmentType.RING);
		}
		if(this.armourSlot == 4) {
			Equipment.equipmentTypes.put(id, EquipmentType.BODY);
		}
		if(this.armourSlot == 7) {
			Equipment.equipmentTypes.put(id, EquipmentType.LEGS);
		}
		if(this.armourSlot == 15) {
			Equipment.equipmentTypes.put(id, EquipmentType.PLATEBODY);
		}

	}

	/**
	 * Gets the weapon speed.
	 *
	 * @return
	 */
	public int getWeaponSpeed() {
		return weaponSpeed;
	}

	/**
	 * Gets the id.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	public String getProperName() {
		return name.replace('_', ' ');
	}

	public void setName(String s) {
		name = s;
	}

	/**
	 * Gets the description.
	 *
	 * @return The description.
	 */
	public String getDescription() {
		return examine;
	}

    public void setDescription(final String examine){
        this.examine = examine;
    }

	/**
	 * Gets the noted flag.
	 *
	 * @return The noted flag.
	 */
	public boolean isNoted() {
		return noted;
	}

	/**
	 * Gets the noteable flag.
	 *
	 * @return The noteable flag.
	 */
	public boolean isNoteable() {
		return noteable;
	}

	/**
	 * Gets the stackable flag.
	 *
	 * @return The stackable flag.
	 */
	public boolean isStackable() {
		return stackable || noted;
	}

    public int getParentId() {
        return parentId;
    }

	/**
	 * Gets the normal id.
	 *
	 * @return The normal id.
	 */
	public int getNormalId() {
		if(noted) {
			return parentId;
		} else {
			return id;
		}
	}

	/**
	 * Gets the noted id.
	 *
	 * @return The noted id.
	 */
	public int getNotedId() {
		return notedId;
	}


	/**
	 * Gets the low alc value.
	 *
	 * @return The low alc value.
	 */
	public int getLowAlcValue() {
		return highAlcValue * 2 / 3;
	}

	/**
	 * Gets the high alc value.
	 *
	 * @return The high alc value.
	 */
	public int getHighAlcValue() {
		if(FightPits.rewardItems.contains(id))
			return 3000000;
		return highAlcValue;
	}

	/**
	 * Sets the high alc value.
	 *
	 * @param value
	 */
	public void setHighAlcValue(int value) {
		highAlcValue = value;
	}

	/**
	 * @param b
	 * @return
	 */
	public void setStackable(boolean b) {
		stackable = b;
	}

	public int getArmourSlot() {
		return armourSlot;
	}

    public void setArmourSlot(final int armourSlot){
        this.armourSlot = armourSlot;
    }

	public int[] getBonus() {
		return bonus;
	}

    public void setBonus(final int idx, final int value){
        bonus[idx] = value;
    }

	static {
		Object o = new Object();
		for(int i : ITEM_NON_TRADEABLE) {
			nonTradesables.put(i, o);
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id = " + this.getId() + ", ");
		sb.append("name = " + this.getName().replaceAll(",", "") + ", ");
		sb.append("examine = " + this.getDescription().replaceAll(",", "") + ", ");
		sb.append("noted = " + this.isNoted() + ", ");
		sb.append("noteable = " + this.isNoteable() + ", ");
		sb.append("stackable = " + this.isStackable() + ", ");
		sb.append("parentid = " + this.getNormalId() + ", ");
		sb.append("notedid = " + this.getNotedId() + ", ");
		sb.append("highalc = " + this.getHighAlcValue() + ", ");
		sb.append("armourslot = " + this.getArmourSlot() + ", ");
		int idx = 0;
		for(int bonus : this.getBonus()) {
			sb.append("bonus" + idx + " = " + bonus + ", ");
			idx++;
		}
		return sb.toString();
	}

	public static ItemDefinition forString(String line) {
		line = line.replaceAll(", ", ",");
		line = line.replaceAll(" ,", ",");
		line = line.replaceAll(" = ", "=");
		//System.out.println(line);
		String[] parts = line.split(",");
		String idString = getValue(parts[0]);
		int id = Integer.parseInt(idString);
		String name = getValue(parts[1]);
		String description = getValue(parts[2]);
		String notedStr = getValue(parts[3]);
		boolean noted = Boolean.parseBoolean(notedStr);
		String noteableStr = getValue(parts[4]);
		boolean noteable = Boolean.parseBoolean(noteableStr);
		String stackableStr = getValue(parts[5]);
		boolean stackable = Boolean.parseBoolean(stackableStr);
		String parentStr = getValue(parts[6]);
		int parent = Integer.parseInt(parentStr);
		String notedIdStr = getValue(parts[7]);
		int notedId = Integer.parseInt(notedIdStr);
		String highAlcStr = getValue(parts[8]);
		int highAlc = Integer.parseInt(highAlcStr);
		String armourSlotStr = getValue(parts[9]);
		int armourslot = Integer.parseInt(armourSlotStr);
		int bonus[] = new int[12];
		for(int i = 0; i < bonus.length; i++) {
			String bonusStr = getValue(parts[10 + i]);
			bonus[i] = Integer.parseInt(bonusStr);
		}
		int weaponSpeed = WeaponAnimManager.getSpeed(name, id);
		ItemDefinition definition = new ItemDefinition(id, name, description, noted, noteable, stackable, parent, notedId, highAlc, armourslot, weaponSpeed, bonus);
		return definition;
	}

	private static String getValue(String part) {
		return part.split("=")[1];
	}


	static {
	}

}