package org.hyperion.rs2.model.sets;

import org.hyperion.rs2.model.Item;

public class SetData {
	private SetData() {

	}
	/**
	 * Information for sets - order doesn't matter {@link SetUtility#getInstantSet(org.hyperion.rs2.model.Player, Item...)}
	 */
	
	private static final Item WELF_SET[] = {
		Item.create(10828),
		new Item(2414),
		new Item(6585),
		new Item(7462),
		new Item(6737),
		new Item(7399),
		new Item(7398),
		new Item(4675),
		new Item(6920),
		new Item(6889)
	};
	
	private static final Item WELF_ITEMS[] = {
		new Item(2503),
		new Item(4151),
		new Item(1127),
		new Item(5698),
		new Item(2497),
		new Item(1185),
		new Item(1079),
		new Item(4131),
		new Item(6685, 2),
		new Item(3024, 3),
        new Item(3144, 2),
        new Item(11090, 1)
	};
	
	public static final Item[] getWelfSet() {
		return WELF_SET.clone();
	}

	public static final Item[] getWelfItems() {
		return WELF_ITEMS.clone();
	}
	
	private static final Item ZERK_SET[] = {
		new Item(6585),
		new Item(4131),
		new Item(7462),
		new Item(6737),
		new Item(3751),
		new Item(1079),
		new Item(1052),
		new Item(1127),
		new Item(4151),
		new Item(1201)
	};
	
	private static final Item ZERK_ITEMS[] = {
		new Item(6685, 2),
		new Item(3024, 2),
		new Item(2436, 1),
		new Item(2440, 1),
		new Item(557, 1000),
		new Item(560, 1000),
		new Item(9075, 1000),
		new Item(5698),

        new Item(3144, 2),
        new Item(11090, 1)
	};
	
	public static final Item[] getZerkSet() {
		return ZERK_SET;
	}

	public static final Item[] getZerkItems() {
		return ZERK_ITEMS;
	}
	
	private static final Item PURE_SET[] = {
		new Item(7394),
		new Item(6568),
		new Item(6585),
		new Item(7459),
		new Item(3105),
		new Item(6737),
		new Item(6107),
		new Item(3842),
		new Item(4587),
		new Item(2497),
		new Item(9244, 100)
	};
	
	private static final Item PURE_ITEMS[] = {
		new Item(9185),
		new Item(4675),
		new Item(6108),
		new Item(5698),
		new Item(868, 1000),
		new Item(555, 1000),
		new Item(560, 1000),
		new Item(565, 1000),
		new Item(6685, 1),
		new Item(3024, 2),
		new Item(3040),
		new Item(2444),
		new Item(2436),
		new Item(2440),
        new Item(3144, 2),
        new Item(11090, 1)
	};
	
	public static final Item[] getPureSet() {
		return PURE_SET;
	}

	public static final Item[] getPureItems() {
		return PURE_ITEMS;
	}
	
	private static final Item HYBRID_SET[] = {
			new Item(10828), // neit
			new Item(4712), //ah robe top
			new Item(4714), //ah robe bottom
			new Item(6914), //master wand
			new Item(2414), // zammy cape, can't expect negros to walk to that altar, nope nope
			new Item(6920), //inf boots
			new Item(7462), // b gloves, as the usual
			new Item(6737), // b ring
			new Item(6585), // fury
			new Item(6889) // mage's book

	};
	//THIS IS VERY ORDER SPECIFIC
	private static final Item HYBRID_ITEMS[] = {
			new Item(6685, 3),
			new Item(3024, 2),
			new Item(2436),
			new Item(2440),
			new Item(3040),
			new Item(555, 1000),
			new Item(560, 1000),
			new Item(565, 1000),
			new Item(5698, 1),
			new Item(4749),
			new Item(4151),
			new Item(11732),
			new Item(391), //filler
			new Item(4751),
			new Item(11283),
			new Item(4736),
            new Item(3144, 2)
	};

	public static final Item[] getHybridSet() {
		return HYBRID_SET;
	}

	public static final Item[] getHybridItems() {
		return HYBRID_ITEMS;
	}

	private static final Item RANGE_SET[] = {
			new Item(861), //MSB
			new Item(892, 100), //rune arrows
			new Item(10828), //neit
			new Item(4736), //black dhide body
			new Item(4738), //black dhide chaps
			new Item(7462), //barrows gloves
			new Item(6733), // archer ring
			new Item(2577), // ranger boots
			new Item(6585), //fury
			new Item(10499) //ava's
	};
	private static final Item RANGE_ITEMS[] = {
			new Item(9185),
			new Item(9244, 100),
			new Item(2444),
			new Item(3024, 2),
			new Item(557, 1000),
			new Item(560, 1000),
			new Item(9075, 1000),
			new Item(1434, 1),
            new Item(11090, 1)
	};

	public static final Item[] getRangeSet() {
		return RANGE_SET.clone();
	}

	public static final Item[] getRangeItems() {
		return RANGE_ITEMS.clone();
	}

	private static final Item MELEE_SET[] = {
			new Item(4716), //dh helm
			new Item(6585), //fury
			new Item(598), //random arrows :D
			new Item(1052), //cape of legends - hate damn no-capers :D
			new Item(4720), //dh plate
			new Item(4722), //dh legs
			new Item(11283), // dfs
			new Item(4151), // whip
			new Item(11732), // d boots
			new Item(6737), // bersk ring
			new Item(7462) // b gloves
	};

	private static final Item[] MELEE_ITEMS = {
			new Item(4718),
			new Item(6685, 2),
			new Item(3024, 2),
			new Item(2436, 1),
			new Item(2440, 1),
			new Item(557, 1000),
			new Item(560, 1000),
			new Item(9075, 1000),
            new Item(3144, 2),
            new Item(11090, 1)
	};

	public static final Item[] getMeleeSet() {
		return MELEE_SET.clone();
	}

	public static final Item[] getMeleeItems() {
		return MELEE_ITEMS.clone();
	}
}
