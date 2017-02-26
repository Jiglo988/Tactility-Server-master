package org.hyperion.rs2.model.content.minigame.poker;

import org.hyperion.rs2.model.Player;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 8/3/15
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class PokerTable {

    private final int ante;
    private final int minChips;
    private final int maxChips;

    public PokerGame game;
    private Map<Player, PokerInstance> players;

    public PokerTable(int ante, int minChips, int maxChips) {
        this.ante = ante;
        this.minChips = minChips;
        this.maxChips = maxChips;
    }

    public synchronized boolean join(final Player player, final int chips) {
        if(chips < minChips)
            return false;
        players.put(player, new PokerInstance(chips));
        return true;
    }

    public synchronized void leave(final Player player) {
        players.remove(player);
        if(game != null)
            game.standUp(player);
    }


}
