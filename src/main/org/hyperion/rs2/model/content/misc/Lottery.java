package org.hyperion.rs2.model.content.misc;

import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.util.Misc;

import java.util.logging.Level;

public class Lottery {

	public static final int MAX_GUESSES = 3;

	public static final int MIN_GUESS = 0;

	public static final int MAX_GUESS = 5000;

	public static final int REWARD = 2000;

	private static int guessesCounter = 0;

	private static int correctGuess = Misc.random(MAX_GUESS);

	public static int getGuessesCounter() {
		return guessesCounter;
	}

	public static void checkGuess(Player player, int guess) {
		if(player.getPoints().getDonatorPoints() < 1) {
			player.getActionSender().sendMessage("@blu@You need at least 1 donator point to gamble.");
			return;
		}
		player.getPoints().setDonatorPoints(player.getPoints().getDonatorPoints() - 1);
		player.getActionSender().sendMessage("@blu@1 Donator Point was taken from your account.");
		guessesCounter++;
		if(guess == correctGuess) {
			correctGuess = Misc.random(MAX_GUESS);
			player.getActionSender().sendMessage("Congratulations, you've just guessed the correct number!");
			player.getPoints().setDonatorPoints(player.getPoints().getDonatorPoints() + REWARD);
			ActionSender.yellMessage("@blu@" + player.getName() + " has just guessed the correct number: " + guess + "!");
			ActionSender.yellMessage("@blu@" + player.getName() + " was rewarded 2000 donator points.");
			guessesCounter = 0;
		} else {
			player.getActionSender().sendMessage("@blu@Unfortunately, you haven't guessed the correct number..");
		}
	}

	public static void init() {
		if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
			Server.getLogger().log(Level.INFO, "Lottery has successfully loaded.");
	}
}
