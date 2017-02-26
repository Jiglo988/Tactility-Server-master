package org.hyperion.rs2.model.content.minigame.barrowsffa;

import org.hyperion.rs2.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 5/22/15
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class BarrowsFFAHolder {

    private int killStreak;
    private int kills;
    private int lives;
    private BarrowSet set;

    private boolean inGame;


    public void destroy() {
        inGame = false;
        killStreak = 0;
        lives = 0;
        kills = 0;
        set = null;
    }

    public void initialize() {
        killStreak = 0;
        kills = 0;
        lives = 2;
        inGame = true;
    }

    /**
     * @param killer Player killing you
     * @return whether you have lost all our lives
     */

    public boolean die(final Player killer) {
        killStreak = 0;
        int b;
        killer.getPoints().increasePkPoints(b = getBounty(), false);
        killer.sendf("You just received %d PKP as a bounty!", b);
        return --lives <= 0;
    }

    /**
     * @param other Player being killed
     * @return whether an update is required & rampage?
     */
    public boolean kill(final Player other) {
        kills++;
        if(++killStreak > 5) {
            if(killStreak%5 == 0)
                lives++;
            return true;
        }

        return false;
    }


    public void setBarrowsSet(final BarrowSet set) {
        this.set = set;
    }

    public int getBounty() {
        int bounty = (kills - lives) * 5 + killStreak * 10 + 5;
        return bounty < 0 ? 0 : bounty;
    }

    public boolean inGame() {
        return inGame;
    }

    public int getKills() {
        return kills;
    }

    public int getLives() {
        return lives;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public BarrowSet getBarrowSet() {
        return set;
    }



}
