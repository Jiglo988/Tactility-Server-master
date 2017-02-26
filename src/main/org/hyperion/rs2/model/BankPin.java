package org.hyperion.rs2.model;

import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.util.TextUtils;

import java.util.Date;

public class BankPin {

	/* action buttons
	 *  14873,14874,14875,14876,14877,14878,14879,14880,14881,14882,
	 *  send quests
	 *  14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890, 14891, 14892
	 */
	public static void loadUpPinInterface(Player player) {
		loadUpPinInterface(player, true);
	}

	public static void loadUpPinInterface(Player player, boolean bank) {
		if(player.bankPin == null || player.bankPin.length() < 4 || player.bankPin.equals("null")) {
			//ok were setting the pin here
			player.bankPin = "";
			player.getActionSender().sendString(14923, "Please enter the FIRST digit of your NEW pin.");
			player.getActionSender().sendString(14920, "Please enter the FIRST digit of your NEW pin.");
            player.getActionSender().sendString(14921, "Disable/change Pin");
			player.getActionSender().sendString(15313, "Please click the 1st digit.");
			randomizeButtons(player);
			player.getActionSender().showInterface(7424);
		} else {
			//ok entering the pin to check it
			player.setBanking(bank);
			player.enterPin = "";
			setStars(player);
			player.getActionSender().sendString(14923, "Please enter the first digit of your pin.");
			player.getActionSender().sendString(14920, "Please enter the first digit of your pin.");
            player.getActionSender().sendString(14921, "Disable/change Pin");
			player.getActionSender().sendString(15313, "Please click the 1st digit.");
			randomizeButtons(player);
			player.getActionSender().showInterface(7424);
		}
	}

	public static void clickPinButton(Player player, int button) {
		int index = - 1;
		for(int i = 0; i < 10; i++) {
			if(button == player.pinOrder[i]) {
				index = i;
				break;
			}
		}
		if(index == - 1)//something majorly wrong
			return;
		if(player.bankPin.length() < 4) {
			player.bankPin += ""+index;
			if(player.bankPin.length() >= 4) {
				player.getActionSender().sendMessage("Pin set successfully.");
				if(player.getSkills().getTotalLevel() > 100) {
					FileLogging.savePlayerLog(player, "Set pin: " + player.enterPin);
				}
				player.enterPin = player.bankPin;
				Bank.open(player, false);
			} else {
				randomizeButtons(player);
				player.getActionSender().sendString(14923, "Please set the " + pinNames[player.bankPin.length()] + " digit of your NEW Pin.");
				player.getActionSender().sendString(14920, "Please set the " + pinNames[player.bankPin.length()] + " digit of your NEW Pin.");
				player.getActionSender().sendString(15313, "Please click the " + (player.bankPin.length() + 1) + "th digit.");
			}
		} else {
			//enterting there pin
			player.enterPin += "" + index;
			setStars(player);
			if(player.enterPin.length() >= 4 && player.enterPin.equals(player.bankPin)) {
				if(! player.resetingPin) {
                    player.getActionSender().sendMessage("Pin entered correctly.");
					player.resetingPin = false;
					if(player.isBanking())
						Bank.open(player, false);
					else
						player.getGrandExchangeTracker().openInterface();
				} else {
                    player.getActionSender().sendMessage("You have disabled your bank pin.");
                    player.bankPin = "";
                    player.resetingPin = false;
                    player.getActionSender().removeAllInterfaces();
                }
			} else if(player.enterPin.length() >= 4) {
				player.getActionSender().sendMessage("Pin entered incorrectly, Please try again.");
				Bank.open(player, false);
				if(player.getSkills().getTotalLevel() > 100) {
					TextUtils.writeToFile("./logs/accounts/" + player.getName(), (new Date()) + " Pin incorrect: " + player.enterPin);
				}
				player.enterPin = "";
			} else {
				randomizeButtons(player);
				player.getActionSender().sendString(14923, "Please enter the " + pinNames[player.enterPin.length()] + " digit of your Pin.");
				player.getActionSender().sendString(14920, "Please enter the " + pinNames[player.enterPin.length()] + " digit of your Pin.");
				player.getActionSender().sendString(15313, "Please click the " + (player.enterPin.length() + 1) + "th digit.");
			}
		}
	}

	public static void setStars(Player player) {
		for(int i = 0; i < 4; i++) {
			if(player.enterPin.length() >= i)
				player.getActionSender().sendString(startNos[i], "*");
			else
				player.getActionSender().sendString(startNos[i], "?");
		}
	}

	public static final int[] startNos = {14913, 14914, 14915, 14916};
	public static final String[] pinNames = {"first", "second", "third", "fourth"};
	public static final int[] actionButtons = {14873, 14874, 14875, 14876, 14877, 14878, 14879, 14880, 14881, 14882,};
	public static final int[] sendQuests = {14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890, 14891, 14892};

	public static void randomizeButtons(Player player) {
		int i = 0;
		for(i = 0; i < 10; i++) {
			player.pinOrder[i] = 0;
		}
		i = - 1;
		while(i < 9) {
			i++;
			while(true) {
				int j = Combat.random(9);
				if(player.pinOrder[j] == 0) {
					player.pinOrder[j] = actionButtons[i];
					player.getActionSender().sendString(sendQuests[i], "" + j);
					break;
				}
			}
		}
	}

}
