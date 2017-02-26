package org.hyperion.rs2.model.content.minigame.poker.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 8/3/15
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Deck {

    private static final Stack<Card> ALL_CARDS;

    static {
        ALL_CARDS = getAllCards();
    }

    public final Stack<Card> cards;

    public Deck() {
        cards = new Stack() {{addAll(ALL_CARDS);}};
    }

    public synchronized void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public synchronized Card draw() {
        return cards.pop();
    }

    public static Stack<Card> getAllCards() {
        final Stack<Card> cards = new Stack<>();
        for(Type type : Type.values()) {
            for(CardNumber number : CardNumber.values()) {
                cards.add(new Card(type, number));
            }
        }
        return cards;
    }

}
