package org.hyperion.sql.db;

import org.hyperion.Configuration;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.sql.impl.donation.Donation;
import org.hyperion.sql.impl.donation.Donations;
import org.hyperion.sql.impl.donation.work.HandlePendingDonationsTask;
import org.hyperion.sql.impl.vote.Votes;
import org.hyperion.sql.impl.vote.WaitingVote;
import org.hyperion.sql.impl.vote.work.HandleWaitingVoteTask;
import org.hyperion.util.Time;

import java.util.List;

import static org.hyperion.Configuration.ConfigurationObject.*;

public class DonationsDb extends Db {

    private Donations donations;
    private Votes votes;

    public Donations donations() {
        return donations;
    }

    public Votes votes() {
        return votes;
    }

    @Override
    protected boolean isEnabled() {
        return Configuration.getBoolean(DONATION_DB_ENABLED);
    }

    @Override
    protected String getUrl() {
        return Configuration.getString(DONATION_DB_URL);
    }

    @Override
    protected String getUsername() {
        return Configuration.getString(DONATION_DB_USER);
    }

    @Override
    protected String getPassword() {
        return Configuration.getString(DONATION_DB_PASSWORD);
    }

    @Override
    protected void postInit() {
        donations = new Donations(this);
        votes = new Votes(this);

        NewCommandHandler.submit(
                new NewCommand("voted", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final List<WaitingVote> list = votes.getWaiting(player);
                        if (list.isEmpty()) {
                            player.sendMessage("You do not have any waiting votes.");
                            return true;
                        }
                        player.sendf("You have @red@%,d@bla@ pending Votes waiting...", list.size());
                        TaskManager.submit(new HandleWaitingVoteTask(player, list));
                        return true;
                    }
                },
                new NewCommand("donated", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final List<Donation> list = donations.getActiveForPlayer(player);
                        if (list.isEmpty()) {
                            player.sendMessage("You do not have any pending donations.");
                            return true;
                        }
                        player.sendf("You have @red@%,d@bla@ pending Donations waiting...");
                        TaskManager.submit(new HandlePendingDonationsTask(player, list));
                        return true;
                    }
                }
        );
    }
}
