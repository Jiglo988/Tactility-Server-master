package org.hyperion.rs2.commands.util;

import org.hyperion.rs2.model.Player;

import java.util.function.Predicate;

/**
 * Created by Gilles on 10/02/2016.
 */
public final class CommandInput<T> {
    /**
     * This is a predicate that will tell the command input in that spot what to check.
     */
    private final Predicate<T> requirement;

    /**
     * This is the short description to the command. It will give the user a rough idea of what to put in.
     */
    private final String shortDescription;

    /**
     * This is a longer description, explaining exactly what the predicate is checking for.
     */
    private final String feedbackMessage;

    public CommandInput(Predicate<T> requirement, String shortDescription, String feedbackMessage) {
        this.requirement = requirement;
        this.feedbackMessage = feedbackMessage;
        this.shortDescription = shortDescription;
    }

    /**
     * This method will test the input if it works. If it gives any issues it will simply throw an error or
     * give the errormessage himself to the player. If it throws an error it will be caught by the lower method.
     * @param player The player that is doing the command.
     * @param input The input to this specific part of the command.
     * @return Whether the command test was successful or not.
     */
    public boolean testInput(Player player, T input) throws Exception {
        if(requirement.test(input))
            return true;
        player.sendMessage("Wrong input '" + input + "' - Required: " + getFeedbackMessage());
        return false;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }
}
