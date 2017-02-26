package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.util.AccountValue;

import java.io.File;

/**
 * @author Arsen Maxyutov.
 */
public class TradeChecker {

	private static final File DIR = new File("suspicioustrades");

	/**
	 * @param player
	 * @param trader
	 * @throws Exception
	 */
	public TradeChecker(Player player, Player trader) {
		Container receiving = trader.getTrade();
		Container giving = player.getTrade();
		int received = AccountValue.getContainerValue(receiving);
		int gave = AccountValue.getContainerValue(giving);
		//System.out.println("Gave :" + gave + " Rec: " + received);
		if(received * 10 < gave && gave > 500) {
			writeLog(player.getName().toLowerCase(), "Traded with: " + trader.getName() + ", gave:" + gave + ", received:" + received);
		} else if(gave * 10 < received && received > 500) {
			writeLog(trader.getName().toLowerCase(), "Traded with: " + player.getName() + ", gave:" + received + ", received:" + gave);
		}
	}

	/**
	 * This method is called to write the log.
	 *
	 * @param name
	 * @param line
	 */
	private void writeLog(String name, String line) {
		FileLogging.saveGameLog(DIR + "/" + name + ".log", line);
	}
}
