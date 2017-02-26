package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.util.Misc;

public class PlayerFiles {





	/**
	 * Saving
	 */

	private static final char[] validChars = {
			'_', ' ', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9'
	};


	public static boolean saveGame(Player p, String message) {
		char[] charArray = p.getName().toCharArray();
		for(int i = 0; i < charArray.length; i++) {
			if(! Misc.contains(charArray[i], validChars))
				return false;
		}
		PlayerSaving.save(p);
		return true;
	}


	/**
	 * Saves the player to his character file.
	 *
	 * @param p
	 * @return
	 */
	public static boolean saveGame(Player p) {
		char[] charArray = p.getName().toCharArray();
		for(int i = 0; i < charArray.length; i++) {
			if(! Misc.contains(charArray[i], validChars)) {
				System.out.println("INVALID CHARACTER NAME");
				return false;
			}
		}
		PlayerSaving.save(p);
		return true;
	}
}
