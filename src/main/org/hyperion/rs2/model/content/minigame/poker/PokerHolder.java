package org.hyperion.rs2.model.content.minigame.poker;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 8/3/15
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PokerHolder {

    private int chips;

    public PokerHolder(int chips) {
        this.chips = chips;
    }

    public void addChips(int chips) {
        this.chips += chips;
    }

    public void removeChips(int chips) {
        this.chips -= chips;
    }

    public int getChips() {
        return chips;
    }



}
