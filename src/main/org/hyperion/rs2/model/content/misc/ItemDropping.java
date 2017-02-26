package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

/**
 * @author SaosinHax
 */
public class ItemDropping {

	/**
	 * Reset the ItemDropping object after this amount of seconds.
	 */
	public static final long RESETTIMER = 20000;

	/**
	 * Used to reset the itemdropping object after <code>RESETTIMER</code> seconds of non-usage.
	 */
	private long lastCheck = 0;

	/**
	 * Holds the Id of the item that is wished to drop.
	 */
	private int currentItem = - 1;

	/**
	 * The confirmed flag.
	 */
	private boolean confirmed = false;

	/**
	 * Use this method to reset the ItemDropping object.
	 */
	public void reset() {
		currentItem = - 1;
		confirmed = false;
	}

	/**
	 * Use this method to check whether an item can be dropped or not.
	 *
	 * @param id
	 * @return
	 */
	public boolean canDrop(int id) {
		if(System.currentTimeMillis() - lastCheck > RESETTIMER)
			reset();
		lastCheck = System.currentTimeMillis();
		if(ItemSpawning.allowedMessage(id).length() == 0) {
			return true;
		}
		if(confirmed && id == currentItem) {
			return true;
		}
		currentItem = id;
		confirmed = true;
		return false;
	}

	public String toString() {
		return "Id: " + currentItem + ", confirmed: " + confirmed;
	}

	static {
	}
}
