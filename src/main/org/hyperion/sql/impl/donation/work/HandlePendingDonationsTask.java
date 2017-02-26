package org.hyperion.sql.impl.donation.work;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.impl.donation.Donation;

import java.util.List;

/**
 * Created by Gilles on 24/02/2016.
 */
public class HandlePendingDonationsTask extends Task {

    private final double MULTIPLIER = 1;
    private final String SPECIAL_EVENT_NAME = "Christmas";

    private final Player player;
    private final List<Donation> donations;

    public HandlePendingDonationsTask(Player player, List<Donation> donations) {
        super(200, true);
        this.player = player;
        this.donations = donations;
    }

    @Override
    public void execute() {
        if (!DbHub.initialized() || !DbHub.getDonationsDb().isInitialized()) {
            stop();
            return;
        }

        if (donations == null || donations.isEmpty()) {
            return;
        }
        int processedCount = 0;
        int totalDollars = 0;
        int totalPoints = 0;
        for (final Donation d : donations) {
            if (!DbHub.getDonationsDb().donations().finish(d))
                continue;
            if (d.method().equals("survey")) {
                player.getPoints().setDonatorPoints(player.getPoints().getDonatorPoints() + d.points());
                player.sendf("%,d Donator Points have been added to your account for surveys", d.points());
            } else {
                if (d.points() > 0) {
                    player.sendf("Donation Processed! Order ID: %s | $%,d (%,d Donator Points)", d.tokenId(), d.dollars(), d.points());
                    totalDollars += d.dollars();
                    totalPoints += d.points();
                } else if (d.points() != 0) {
                    player.getPoints().setDonatorPoints(player.getPoints().getDonatorPoints() - d.points());
                    player.sendf("%,d Donator Points have been deducted from your account!", d.points());
                }
            }
            ++processedCount;
        }
        if (totalPoints == 0) {
            if (processedCount != donations.size())
                player.sendf("There was an error processing all of your donations");
            else
                player.sendf("You don't have any pending donations! Type ::donate to donate");
            return;
        }
        player.getPoints().setDonatorPoints((int) Math.round(player.getPoints().getDonatorPoints() + totalPoints * MULTIPLIER));
        player.getPoints().setDonatorsBought(player.getPoints().getDonatorPointsBought() + totalPoints);
        player.sendf("Alert##Thank you for donating $%,d##%,d donator points have been added to your account", totalDollars, totalPoints);
        if (MULTIPLIER > 1)
            player.sendf(SPECIAL_EVENT_NAME + " SPECIAL! You received an extra " + (MULTIPLIER - 1) * 100 + "% Donator Points for " + SPECIAL_EVENT_NAME + "!");
        check(player, Donation.DP_FOR_DONATOR, Rank.DONATOR);
        check(player, Donation.DP_FOR_SUPER_DONATOR, Rank.SUPER_DONATOR);
        if (processedCount != donations.size()) {
            player.sendf("Note: Not all of your pending donations were processed!");
            player.sendf("Please contact an administrator if you believe there is an error");
        }
        stop();
    }

    private static void check(final Player player, final int requirement, final Rank rank) {
        if (player.getPoints().getDonatorPointsBought() < requirement || Rank.hasAbility(player, rank))
            return;
        player.setPlayerRank(Rank.addAbility(player, rank));
        player.setPlayerRank(Rank.setPrimaryRank(player, rank));
        player.sendf("You have been given %s status!", rank);
    }
}
