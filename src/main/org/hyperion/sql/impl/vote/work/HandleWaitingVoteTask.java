package org.hyperion.sql.impl.vote.work;

import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.impl.vote.WaitingVote;
import org.hyperion.util.Misc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Gilles on 24/02/2016.
 */
public class HandleWaitingVoteTask extends Task {

    /**
     * The maximum amount of votes a player can do on one day.
     */
    private final static int MAXIMUM_VOTES_PER_DAY = 25;

    /**
     * The player we are executing this event for.
     */
    private final Player player;

    /**
     * The votes to execute for the player.
     */
    private final List<WaitingVote> votes;

    public HandleWaitingVoteTask(Player player, List<WaitingVote> votes) {
        super(600);
        this.player = player;
        this.votes = votes;
    }

    @Override
    protected void execute() {
        stop();
        if (votes == null || votes.isEmpty())
            return;
        if (votes.stream().filter(vote -> !vote.processed()).count() < 1)
            return;
        boolean runelocus = false;
        boolean rspslist = false;
        boolean topg = false;
        int runelocusVotes = 0;
        int rspslistVotes = 0;
        int topgVotes = 0;

        /**
         * First we'll gather for all votes if they're processed or not.
         */
        for (WaitingVote vote : votes) {
            if (!vote.processed() && DbHub.getDonationsDb().votes().process(vote)) {
                if (vote.runelocus() && !vote.runelocusProcessed()) {
                    if (DbHub.getDonationsDb().votes().processRunelocus(vote))
                        runelocusVotes++;
                }
                if (vote.topg() && !vote.topgProcessed()) {
                    if (DbHub.getDonationsDb().votes().processTopg(vote))
                        topgVotes++;
                }
                if (vote.rspslist() && !vote.rspslistProcessed()) {
                    if (DbHub.getDonationsDb().votes().processRspslist(vote))
                        rspslistVotes++;
                }
            }
            if (vote.runelocus())
                runelocus = true;
            if (vote.rspslist())
                rspslist = true;
            if (vote.topg())
                topg = true;
        }
        LocalDate lastVoteDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(player.getLastVoteStreakIncrease()), ZoneId.systemDefault()).toLocalDate();

        if (!lastVoteDate.equals(LocalDate.now())) {
            player.setTodayVotes(0);
            if (runelocus && topg && rspslist) {
                player.setVoteStreak(player.getVoteStreak() + 1);
                player.sendMessage("Your current voting streak is now " + player.getVoteStreak() + "!");
                player.setLastVoteStreakIncrease(System.currentTimeMillis());
            }
        }
        player.setTodayVotes(player.getTodayVotes() + runelocusVotes + topgVotes + rspslistVotes);

        if (player.getTodayVotes() > MAXIMUM_VOTES_PER_DAY) {
            player.getInventory().remove(Item.create(3062, player.getInventory().getCount(3062)));
            player.getBank().remove(Item.create(3062, player.getBank().getCount(3062)));
            player.getPoints().setVotingPoints(0);
            player.sendMessage("You can only vote a maximum of " + MAXIMUM_VOTES_PER_DAY + " times a day.", "Your " + ItemDefinition.forId(3062).getProperName() + "es have been cleared and your points reset.");
        }

        if (player.getTodayVotes() > 10 && Misc.random(2) == 1 && player.getTodayVotes() <= MAXIMUM_VOTES_PER_DAY) {
            player.sendMessage("You can only vote a maximum of " + MAXIMUM_VOTES_PER_DAY + " times a day without getting", "your votes cleaned, be careful!");
        }

        int votingPoints = (runelocusVotes * 2) + rspslistVotes + topgVotes;

        LocalDate lastVoteBonusDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(player.getLastVoteBonus()), ZoneId.systemDefault()).toLocalDate();
        boolean canReceiveBonus = !lastVoteBonusDate.equals(LocalDate.now());

        if (canReceiveBonus) {
            if (runelocus && topg && rspslist) {
                VoteBonus voteBonus = Misc.randomElement(VoteBonus.VALUES);
                while (!voteBonus.willApply(player)) {
                    voteBonus = Misc.randomElement(VoteBonus.VALUES);
                }
                player.sendMessage("Alert##Thank you for voting" + (player.getTodayVotes() > 1 ? " again" : "") + "!##You received " + votingPoints + " Strange Box" + (votingPoints == 1 ? "" : "es") + " ##" + voteBonus.bonusMessage(player));
                voteBonus.applyBonus(player);
                player.setLastVoteBonus(System.currentTimeMillis());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("You can still vote on ");
                if (!runelocus)
                    sb.append("Runelocus & ");
                if (!rspslist)
                    sb.append("RSPSList & ");
                if (!topg)
                    sb.append("TopG");
                if (sb.toString().endsWith(" & ")) {
                    sb.replace(sb.length() - 3, sb.length(), "");
                }
                sb.append(".");
                player.sendMessage("Alert##Thank you for voting" + (player.getTodayVotes() > 1 ? " again" : "") + "!##You received " + votingPoints + " Strange Box" + ((runelocusVotes + rspslistVotes + topgVotes) == 1 ? "" : "es") + " ##" + sb.toString());
            }
        } else {
            player.sendMessage("Alert##Thank you for voting" + (player.getTodayVotes() > 1 ? " again" : "") + "!##You received " + votingPoints + " Strange Box" + ((runelocusVotes + rspslistVotes + topgVotes) == 1 ? "" : "es") + " ##You can vote " + (MAXIMUM_VOTES_PER_DAY - player.getTodayVotes()) + " more times today!");
        }

        final int freeSlots = player.getInventory().freeSlots();
        if (freeSlots >= 1) {
            player.getInventory().add(new Item(3062, votingPoints));
        } else {
            player.getBank().add(new BankItem(0, 3062, votingPoints));
            player.sendMessage((votingPoints == 1 ? "A" : votingPoints) + " Strange Box" + (votingPoints == 1 ? " has" : "es have") + " been added to your bank.");
        }
        archiveVotes(player, runelocus && topg && rspslist, votes, runelocusVotes, rspslistVotes, topgVotes);
    }

    public static void archiveVotes(Player player, boolean deleteAllProcessed, List<WaitingVote> votes, int runelocusVotes, int rspslistVotes, int topgVotes) {
        GameEngine.submitSql(new EngineTask<Boolean>("Waitingvotes query", 5, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                List<WaitingVote> processedVotes = votes.stream().filter(vote -> vote.processed() && (deleteAllProcessed || !vote.date().toLocalDate().equals(LocalDate.now()))).collect(Collectors.toList());
                processedVotes.forEach(vote -> DbHub.getDonationsDb().votes().delete(vote));
                DbHub.getDonationsDb().votes().insertVote(player, runelocusVotes, rspslistVotes, topgVotes);
                return true;
            }
        });
    }
}
