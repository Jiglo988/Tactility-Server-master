package org.hyperion.rs2.model.content.minigame.lastmanstanding;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ObjectManager;
import org.hyperion.rs2.model.Position;
import org.hyperion.util.Misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Constants {

	public static final Set<ChestObject> OBJECTS = new HashSet<>();

	/** Chest constants **/
	public static final int CHEST_DEFAULT_AVAILABLE = 5;
	public static final List<Item> ITEMS = new ArrayList<>();

	/** Location constants **/
	public static final Position LOCATION = Position.create(3272, 2785, 8);
	public static final Position FINISHED_LOCATION = Position.create(3222, 3222, 0);
	public static final Position LOBBY = Position.create(3313, 2800, 8);

	/** Misc **/
	public static final int ENOUGH_PLAYERS = 2;
	protected static final int TIME_TO_START = 70;

	public static final int[] DISABLED_LADDERS = new int[] { 1747 };

	/** Gas constants **/
	public static final int ELAPSED_TO_GAS = 120; // 120 secs

	private Constants() {

	}

	static {
		addChests();
		addItemContents();

	}

	private static void addItemContents() {
		add(4151);
		add(5698);
		add(1305);
		add(7158);
		add(4587);
		add(1289);
		add(1303);
		add(1319);
		add(1333);
		add(1432);
		add(1287);
		add(1301);
		add(1317);
		add(1331);
		add(1430);
		add(1434);
		add(1149);
		add(3140);
		add(4087);
		add(3486);
		add(3481);
		add(3283);
		add(1163);
		add(1079);
		add(1093);
		add(1127);
		add(1161);
		add(1123);
		add(1073);
		add(11694);
		add(4753);
		add(4755);
		add(4757);
		add(4759);
		add(4716);
		add(4718);
		add(4720);
		add(4722);
		add(1231);
		add(11724);
		add(11726);
		add(11696);
		add(4087);
		add(4585);
		add(14479);
		add(11335);
		add(7462);
		add(1725);
		add(11283);
		
		

		for (int i = 0; i < 10; i++) { // Fill 10 slots
			add(15272, Misc.random(3) + 1); // 1-4 Rocktails
		}
	}

	private static void addChests() {
		
		
			addObj(Position.create(3042, 2864, 8), 0);
			
			addObj(Position.create(3037, 2864, 8), 0);
			
			addObj(Position.create(3021, 2855, 8), 1);
			
			addObj(Position.create(3021, 2838, 8), 1);
			
			addObj(Position.create(3034, 2829, 8), 0);
			
			addObj(Position.create(3050, 2829, 8), 0);
			
			addObj(Position.create(3059, 2842, 8), 4);
			
			addObj(Position.create(3059, 2859, 8), 4);
			
			addObj(Position.create(3035, 2844, 8), 0);
			
			addObj(Position.create(3044, 2853, 8), 0);
			
			addObj(Position.create(3068, 2877, 8), 0);
			
			addObj(Position.create(3011, 2877, 8), 0);
			
			addObj(Position.create(3011, 2819, 8), 0);
			
			addObj(Position.create(3068, 2819, 8), 0);


	}

	private static void add(Position location, int rotation, int available) {
		ChestObject obj = new ChestObject(location, rotation, available);

		OBJECTS.add(obj);
		ObjectManager.addObject(obj);
	}

	private static void addObj(Position location, int rotation) {
		add(location, rotation, CHEST_DEFAULT_AVAILABLE);
	}

	private static void add(int id, int amount) {
		ITEMS.add(new Item(id, amount));
	}

	private static void add(int id) {
		add(id, 1);
	}
}
