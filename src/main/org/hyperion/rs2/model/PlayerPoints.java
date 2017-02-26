package org.hyperion.rs2.model;

import org.hyperion.Configuration;
import org.hyperion.rs2.model.combat.EloRating;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlayerPoints {

    /**
     * The player.
     */
    private Player player;

    /**
     * The Pk Points.
     */
    private int pkPoints = 0;

    /**
     * The voting Points.
     */
    private int votingPoints = 0;

    /**
     * The honor Points.
     */
    private int honorPoints = 0;

    /**
     * The donator Points bought.
     */
    private int donatorPointsBought = 0;

    /**
     * The donator Points at the current moment.
     */
    private int donatorPoints = 0;

    /**
     * The minigame points at the moment
     */
    private int minigamePoints = 0;

    /**
     * The Elo Rating.
     */
    private int eloRating = EloRating.DEFAULT_ELO_START_RATING;

    /**
     * ELO Max
     *
     * @param opponentRating
     * @param resultType
     */

    private int eloPeak = EloRating.DEFAULT_ELO_START_RATING;

    public void updateEloRating(int opponentRating, int resultType) {
        setEloRating(EloRating.getNewRating(eloRating, opponentRating, resultType));
        String message = "You have defeaten an opponent with PvP Rating " + opponentRating;
        String message2 = "Your new PvP Rating is: " + eloRating;
        if (resultType == EloRating.WIN) {
            player.sendPkMessage(message);
            player.sendPkMessage(message2);
        } else if (resultType == EloRating.LOSE)
            player.sendPkMessage(message.replace("have defeaten", "were defeaten by"));
    }

    public void setEloRating(int rating) {
        this.eloRating = rating;
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.PVP_RATING);
        if (this.eloPeak < eloRating)
            eloPeak = Integer.valueOf(eloRating);
        Rank newRank = eloRating >= 2200 ? Rank.LEGEND : eloRating >= 1900 ? Rank.HERO : Rank.PLAYER;
        if (newRank == Rank.PLAYER) {
            if (Rank.getPrimaryRank(player) == Rank.LEGEND || Rank.getPrimaryRank(player) == Rank.HERO)
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.PLAYER));

            if (Rank.hasAbility(player, Rank.HERO)) {
                player.setPlayerRank(Rank.removeAbility(player, Rank.LEGEND));
                player.setPlayerRank(Rank.removeAbility(player, Rank.HERO));
                player.sendPkMessage("Your PvP rating has dropped below the required threshold...");
                player.sendPkMessage("You have been stripped of your master/grandmaster title and abilities.");
            }
        } else {
            //annoying seeing this all the time
            if (!Rank.hasAbility(player, newRank))
                player.sendPkMessage("Congratulations! You have received " + newRank.toString() + "!");
            if (Rank.getPrimaryRank(player) == Rank.PLAYER) {
                player.setPlayerRank(Rank.addAbility(player, newRank));
            } else
                player.setPlayerRank(Rank.addAbility(player, newRank));
        }
    }

    /**
     * @param player
     */
    public PlayerPoints(Player player) {
        this.player = player;
    }

    /**
     * Increases the players donator points and his
     * donatorpoints bought. Incase the donators bought amount has
     * reached certain limits, the player can receive special donator status.
     * This also notifies the player that his donatorpoints have been increased
     * and updates this to his questtab.
     *
     * @param amount
     */
    public void increaseDonatorPoints(int amount, boolean bought) {
        if (amount > 200000)
            return;
        donatorPoints += amount;
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.DONATOR_POINTS);
        if (bought) {
            donatorPointsBought += amount;
            player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.DONATOR_POINTS_BOUGHT);
            checkDonator();
            try {
                final File f = new File("./data/donate.txt");
                if (!f.exists())
                    f.createNewFile();
                final FileWriter writer = new FileWriter(f, true);
                writer.write(String.format(
                        "\nplayer=%s, oldDpBought=%d, amount=%d, newDpBought=%d, time=%s",
                        player.getName(), donatorPointsBought - amount, amount, donatorPointsBought, new Date(System.currentTimeMillis())
                ));
                writer.flush();
                writer.close();
            } catch (Exception ex) {
                System.out.println("Error saving donor points change: " + ex);
            }
        }
        player.getExpectedValues().changeDeltaOther("Donator points added", amount);
        player.sendServerMessage("You have been given " + amount + " donator points.");
    }

    public void checkDonator() {
        if (donatorPointsBought >= 10000)
            player.setPlayerRank(Rank.addAbility(player, Rank.SUPER_DONATOR));
        if (donatorPointsBought >= 2000)
            player.setPlayerRank(Rank.addAbility(player, Rank.DONATOR));
    }

    /**
     * Increases the players donator points and his
     * donatorpoints bought. Incase the donators bought amount has
     * reached certain limits, the player can receive special donator status.
     * This also notifies the player that his donatorpoints have been increased
     * and updates this to his questtab.
     *
     * @param amount
     */
    public void increaseDonatorPoints(int amount) {
        increaseDonatorPoints(amount, true);
    }

    public void setDonatorPoints(int am) {
        player.getExpectedValues().changeDeltaOther("Donator points set", am - donatorPoints);
        donatorPoints = am;
        player.getActionSender().sendString(3901, String.format("Donator points: @gre@%,d", donatorPoints));
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.DONATOR_POINTS);
    }

    public void setDonatorsBought(int am) {
        donatorPointsBought = am;
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.DONATOR_POINTS_BOUGHT);
    }

    /**
     * @param value
     */
    public void setHonorPoints(int value) {
        this.honorPoints = value;
        player.getActionSender().sendString(3901, String.format("Honor points: @gre@%,d", honorPoints));
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.HONOR_POINTS);
    }

    /**
     * @param points
     */
    public void setVotingPoints(int points) {
        player.getExpectedValues().changeDeltaOther("Voting points set", points - votingPoints);
        votingPoints = points;
        player.getActionSender().sendString(3901, String.format("Voting points: @gre@%,d", votingPoints));
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.VOTING_POINTS);
    }

    public void increaseVotingPoints(int times) {
        int toAdd = 0;
        //int maxpoints = Server.getConfig().getInteger("votepoints") - 1;
        int maxpoints = 1;
        for (int i = 0; i < times; i++) {
            toAdd += Misc.random(maxpoints) + 1;
        }
        votingPoints += toAdd;
        player.getActionSender().sendString(3901, String.format("Voting points: @gre@%,d", player.getPoints().getVotingPoints()));
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.VOTING_POINTS);
        player.getExpectedValues().changeDeltaOther("Voting points added", toAdd);
        player.sendServerMessage("Your voting points have been increased by " +
                toAdd + ", you now have " + votingPoints + " voting points!");
    }


    public int increaseMinigamePoints(int times) {
        minigamePoints += times;
        return minigamePoints;
    }

    public int setMinigamePoints(int amount) {
        return minigamePoints = amount;
    }


    /**
     * Sets the amount of Pk Points, updating the quest tab.
     *
     * @param points
     */
    public void setPkPoints(int points) {
        pkPoints = points;
        player.getActionSender().sendString(3901, String.format("TactilityPk points: @gre@%,d", pkPoints));
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.PK_POINTS);
    }

    public void increasePkPoints(int points) {
        increasePkPoints(points, true);
    }

    public void increasePkPoints(int points, boolean message) {
        pkPoints += points;
        player.getActionSender().sendString(3901, String.format("TactilityPk points: @gre@%,d", pkPoints));
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.PK_POINTS);
        if (message)
            player.sendPkMessage("Your " + Configuration.getString(Configuration.ConfigurationObject.NAME) + " points have been increased by " + points + "!");
    }

    public void loginCheck() {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - player.getPreviousSessionTime();
        if (delta < Time.ONE_DAY) {
            //System.out.println("Passed through one day check");
            if (currentTime - player.getLastHonorPointsReward() > Time.ONE_HOUR * 12) {
                //System.out.println("Passed through reward check");
                double reward = 0;
                if (eloRating > 1500) {
                    reward = (1511.26 / ((1 + 1639.28 * Math.pow(Math.E, -0.00412 * eloRating)))) / 7;
                } else {
                    reward = (0.22 * eloRating + 14) / 7;
                }
                if (Rank.hasAbility(player, Rank.SUPER_DONATOR))
                    reward *= 1.25;
                reward *= 2;
                honorPoints += (int) (reward);

                player.sendPkMessage("You have been awarded " + (int) reward + " honor points!");
                player.setLastHonorPointsReward(System.currentTimeMillis());
            }
        } else if (delta > (Time.ONE_DAY * 1.5)) {
            int days = (int) (delta / Time.ONE_DAY);
            honorPoints -= days * 10;
            if (honorPoints < 0)
                honorPoints = 0;
            player.sendPkMessage("You've lost honor points due to inactivity!");
            player.setLastHonorPointsReward(System.currentTimeMillis());
        }
    }

    public void setEloPeak(int elo) {
        eloPeak = elo;
    }

    /**
     * @return
     */
    public int getEloRating() {
        return eloRating;
    }

    public int getEloPeak() {
        return eloPeak;
    }


    /**
     * Gets the amount of Pk Points.
     *
     * @return
     */
    public int getPkPoints() {
        return pkPoints;
    }

    /**
     * @return
     */
    public int getVotingPoints() {
        return votingPoints;
    }

    /**
     * @return
     */
    public int getHonorPoints() {
        return honorPoints;
    }

    /**
     * @return
     */
    public int getDonatorPointsBought() {
        return donatorPointsBought;
    }

    /**
     * @return
     */
    public int getDonatorPoints() {
        return donatorPoints;
    }


    public int getMinigamePoints() {
        return minigamePoints;
    }

    public int pkpBonus(int originalPkp) {
        long minutes = TimeUnit.MINUTES.convert(player.getTotalOnlineTime(), TimeUnit.MILLISECONDS);
        //System.out.println("Minutes: "+minutes);
        if (minutes <= 10) minutes = 10;
        if (minutes > 100)
            return originalPkp;
        double max_increase = 10.0;
        double modifier = max_increase / (minutes / 10D);
        player.sendPkMessage("You get" + String.format(".1f", modifier) + "x Pk points bonus for being new!");
        return (int) (originalPkp * modifier);
    }

}
