package org.hyperion.rs2.model.content.EP;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.util.Misc;

/**
 * @author SaosinHax/Linus/Vegas/Flux/Tinderbox/Jack Daniels/Arsen/Jolt <- All same person
 */

public class EPDrops {

	public static final int MAX_EP = 100;
	public static final int MIN_EP = 0;

	public static Item getEPItem(int EP) {
		if(EP < 10)
			return null;
		if(EP < 30) {
			return LowEPDrop();
		} else if(EP < 60)
			return MidEPDrop();
		else if(EP < 99)
			return HighEPDrop();
		else if(EP == 100)
			return SuperEPDrop();
		else
			return null;
	}

	public int computeWealthRisked(Player player) {
		return 0;
	}

	private static Item LowEPDrop() {
		int r = Misc.random(500);
		if(r < 5)
			return PVP_DROPS[Misc.random(PVP_DROPS.length - 1)];
		else if(r < 300)
			return STATUETTES[Misc.random(STATUETTES.length - 4) + 3];
		return null;
	}

	private static Item MidEPDrop() {
		int r = Misc.random(500);
		if(r < 20)
			return PVP_DROPS[Misc.random(PVP_DROPS.length - 1)];
		else if(r < 335)
			return STATUETTES[Misc.random(STATUETTES.length - 3)];
		return null;
	}

	private static Item HighEPDrop() {
		int r = Misc.random(500);
		if(r < 30)
			return PVP_DROPS[Misc.random(PVP_DROPS.length - 1)];
		else
			return STATUETTES[Misc.random(STATUETTES.length - 4)];
	}

	private static Item SuperEPDrop() {
		int r = Misc.random(500);
		if(r < 40)
			return PVP_DROPS[Misc.random(PVP_DROPS.length - 1)];
		else
			return STATUETTES[Misc.random(STATUETTES.length - 5)];
	}

	private static final Item[] PVP_DROPS = {new Item(13887, 1),
			new Item(13893, 1), new Item(13899, 1), new Item(13905, 1),
			new Item(13884, 1), new Item(13890, 1), new Item(13896, 1),
			new Item(13902, 1), new Item(13870, 1), new Item(13873, 1),
			new Item(13876, 1), new Item(13858, 1), new Item(13861, 1),
			new Item(13864, 1), new Item(13867, 1),

	};
	private static final Item[] STATUETTES = {new Item(14876, 1),
			new Item(14878, 1), new Item(14879, 1), new Item(14881, 1),
			new Item(14887, 1), new Item(14880, 1), new Item(14888, 1),
			new Item(14885, 1), new Item(14889, 1),};

}
