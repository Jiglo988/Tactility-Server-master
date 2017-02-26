package org.hyperion.rs2.model.content.minigame.poker;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.minigame.poker.card.Deck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 8/3/15
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PokerGame {

    private final Deck deck;
    private final Map<Player, PokerInstance> players = new HashMap<>();

    public PokerGame(final Map<Player, PokerInstance> players) {
        deck = new Deck();
        deck.shuffleDeck();


    }

    public void standUp(final Player player) {

    }

}
