package org.hyperion.rs2.commands;

import org.hyperion.Configuration;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

/**
 * @author Jack Daniels.
 */
public abstract class Command {

	/**
	 * Name of the command which would be used as ::startsWith.
	 */
	private final String key;

	/**
	 * The rights.
	 */
	private final Rank[] ranks;

	/**
	 * Flag indicating whether the command should be logged/saved/recorded.
	 */
	private boolean recorded;


	/**
	 * Executes the command
	 *
	 * @param player
	 * @param input
	 * @return true if successful, false if not
	 * @throws Exception
	 */
	public abstract boolean execute(Player player, String input) throws Exception;

	/**
	 * Gets the rights of the command.
	 *
	 * @return the rights
	 */
	public Rank[] getRanks() {
		return ranks;
	}

	/**
	 * @return the command key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return whether the command should be recorded/saved/logged.
	 */
	public boolean isRecorded() {
		return recorded;
	}

	/**
	 * Creates a new command, this command can be added to the server
	 * by submitting this command in the CommandHandler class.
	 *
	 * @param key
	 * @param ranks
	 */
	public Command(String key, Rank... ranks) {
		this.key = key.toLowerCase();
		this.ranks = ranks;
		this.recorded = shouldRecord(ranks);
	}

	public boolean isForStaff() {
		for(Rank rank : ranks) {
			if(rank.ordinal() >= Rank.MODERATOR.ordinal())
				return true;
		}
		return false;
	}

	private boolean shouldRecord(Rank[] rights) {
		if(Configuration.getString(Configuration.ConfigurationObject.NAME).equalsIgnoreCase("arterobeta"))
			return false;
		for(Rank right : rights) {
			if(right.ordinal() >= Rank.MODERATOR.ordinal())
				return true;
		}
		return false;
	}

	/**
	 * Removes the command name with space from the input
	 * e.g. converts "update 30" to "30"
	 *
	 * @param input
	 * @returns the actual input of the command, in lower case
	 */
	public String filterInput(String input) {
		return input.replace(key + " ", "").toLowerCase();
	}

	/**
	 * Removes the command name with space from the input
	 * and splits the remaining String into integer parts
	 * e.g. converts "update 30" to {30}
	 *
	 * @param input
	 * @returns Integer Array of the input
	 */
	public int[] getIntArray(String input) {
		input = filterInput(input);
		String[] parts = input.split(" ");
		int[] intArray = new int[parts.length];
		for(int i = 0; i < intArray.length; i++) {
			try {
				intArray[i] = Integer.parseInt(parts[i]);
			} catch(Exception e) {
			}
		}
		return intArray;
	}
}
