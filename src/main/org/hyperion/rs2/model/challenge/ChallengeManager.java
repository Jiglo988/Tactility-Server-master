package org.hyperion.rs2.model.challenge;

import org.hyperion.rs2.model.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ChallengeManager {

    private static final Map<String, Challenge> MAP = new HashMap<>();

    private ChallengeManager(){}

    public static void add(final Challenge c){
        MAP.put(c.getText(), c);
    }

    public static void remove(final Challenge c){
        MAP.remove(c.getText());
    }

    public static Collection<Challenge> getChallenges(){
        return MAP.values();
    }

    public static Challenge getChallenge(final String text){
        return MAP.get(text);
    }

    public static void send(final Player player, final boolean alert){
        final Collection<Challenge> challenges = getChallenges();
        if(challenges.isEmpty()){
            player.sendf("There are currently no challenges at this time!");
            return;
        }
        challenges.stream().forEach(value -> value.send(player, alert));
    }

    public static void send(final Player player){
        send(player, false);
    }
}
