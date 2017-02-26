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
    /*
    Sec.. back man :) can you add another source and client on here so we can test updates on before we add them to the
    game
    we need a test source else we got to update everytime to check and stuff
    SEc I could make a seperate server by hosting it on my own computer,sounds good? yea
    hS Sure just run it on localhost but do you keep your pc on? yeah
    aightI c ool man Sent oyou eta client on skype can only play on this pc my other is hella slow xd
    but how do you want to code this? or if you only want me to host it its alright. You can code on this computer if you want
    But remember when you have changed something, remember to push it to Github,but if its bad code? What do um mean? like
    i need to test first and things might go wrong before i apply them to the game since i host it here? You can push to Github and then
    we can test them on beta server ok thats cool
    I will just show you how to commit changes, it's pretty simple btw
    You see this VCS button with a green yearrow
    Ok, you basically just click it and you are ready to push changes to Github
    And if you want to update changes, you click that blue arrow.. ok ok but what if i messed up don't i need a second source folder?
    idk same as testing it if i do it on the host i have to test changes and update everytime while people are playing?
    idk bro ur the man :P
    Uhm, I will eleborate more later, going to sleep very soon
    Basically once you have coded, you just commit the changes, if you mess up something, we can easily recover our files,
    I yeah thats a good thing but i mean like people are playing and i have to restart the server to apply a change in-game?
    but we can do tomorrow if you like i go sleep soon aswell. oo it will just change? thats amazing
    I like i change something in a shop and i commit  will it change automaticly in-game? then its very nice
    Not sure, but I usually restart whenever I make changes, so I really don't know
    Let's do a commit now, so you will understand better, ok? alright bro

    */
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
 //I will show you now how to push changes, etc
//