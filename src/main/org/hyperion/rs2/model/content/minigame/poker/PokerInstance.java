package org.hyperion.rs2.model.content.minigame.poker;

import org.hyperion.rs2.model.content.minigame.poker.card.Card;
import org.hyperion.rs2.model.content.minigame.poker.card.Deck;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 8/3/15
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class PokerInstance {

    private final PokerHolder chips;

    public Card[] cards = new Card[2];

    public PokerInstance(int chips) {
        this.chips = new PokerHolder(chips);
    }

    public void deal(final Deck deck) {
        for(int i = 0 ; i < cards.length; i++) {
            cards[i] = deck.draw();
        }
    }

}
