package org.hyperion.data;

import com.thoughtworks.xstream.XStream;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.Door;
import org.hyperion.rs2.model.content.misc.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Has the xstream object.
 *
 * @author Graham
 */
public class PersistenceManager {
	private PersistenceManager() {
	}

	private static XStream xstream;

	static {
		xstream = new XStream();
		xstream.alias("foodItem", FoodItem.class);
		xstream.alias("icon", PrayerIcon.class);
		xstream.alias("herb", Herb.class);
		xstream.alias("potion", Potion.class);
		xstream.alias("unfinPotion", UnfinishedPotion.class);
		xstream.alias("smeltingItem", SmeltingItem.class);
		xstream.alias("npc", PickpocketNpc.class);
		xstream.alias("stall", Stall.class);
		xstream.alias("runes", Rune.class);
		xstream.alias("door", Door.class);
		xstream.alias("position", Position.class);
	}

	public static void save(Object object, OutputStream out) {
		xstream.toXML(object, out);
	}

	public static Object load(InputStream in) {
		return xstream.fromXML(in);
	}

}
