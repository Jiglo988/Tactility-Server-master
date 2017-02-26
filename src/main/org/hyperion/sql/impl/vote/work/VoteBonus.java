package org.hyperion.sql.impl.vote.work;

import org.hyperion.engine.task.impl.VoteBonusEndTask;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.Lock;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

/**
 * Created by Gilles on 16/02/2016.
 */
public enum VoteBonus {
    DOUBLE_EXPERIENCE {
        @Override
        public boolean applyBonus(Player player) {
            World.submit(new VoteBonusEndTask(player, 0));
            player.setVoteBonusEndTime(getTimeForStreak(player), true);
            Lock.switchLock(player, Lock.DOUBLE_EXPERIENCE);
            return true;
        }

        @Override
        public String bonusMessage(Player player) {
            return "You have received double experience for " + getTimeForStreak(player) / Time.ONE_MINUTE + " minutes.";
        }
    },
    INCREASED_DROP_RATE {
        @Override
        public boolean applyBonus(Player player) {
            World.submit(new VoteBonusEndTask(player, 0));
            player.setVoteBonusEndTime((long)(getTimeForStreak(player) * 0.666), true);
            Lock.switchLock(player, Lock.DOUBLE_EXPERIENCE);
            return true;
        }

        @Override
        public String bonusMessage(Player player) {
            return "You have received increased drop rates for " + (int)(getTimeForStreak(player) * 0.666) / Time.ONE_MINUTE + " minutes.";
        }
    },
    REDUCED_YELL_DELAY {
        @Override
        public boolean willApply(Player player) {
            return !Rank.isStaffMember(player);
        }

        @Override
        public boolean applyBonus(Player player) {
            if(Rank.isStaffMember(player))
                return false;
            World.submit(new VoteBonusEndTask(player, 0));
            player.setVoteBonusEndTime(getTimeForStreak(player), true);
            Lock.switchLock(player, Lock.REDUCED_YELL_DELAY);
            return true;
        }

        @Override
        public String bonusMessage(Player player) {
            return "You have received a lowered yell delay for " + getTimeForStreak(player) / Time.ONE_MINUTE + " minutes.";
        }
    },
    DONATOR_POINTS {
        private final static int AMOUNT = 1000;

        @Override
        public boolean willApply(Player player) {
            return Misc.random(10) == 5;
        }

        @Override
        public boolean applyBonus(Player player) {
            if(Misc.random(10) != 5)
                return false;
            player.getPoints().increaseDonatorPoints(AMOUNT);
            World.sendGlobalMessage(player.getSafeDisplayName() + " has received " + AMOUNT +" donator points as a rare voting reward!");
            return true;
        }

        @Override
        public String bonusMessage(Player player) {
            return "You have received " + AMOUNT + " donator points as a rare reward!";
        }
    };
    public final static VoteBonus[] VALUES = values();

    public boolean willApply(Player player) {
        return true;
    }
    public abstract boolean applyBonus(Player player);
    public abstract String bonusMessage(Player player);

    protected static long getTimeForStreak(Player player) {
        int streak = player.getVoteStreak();
        if(streak > 60) {
            return 2 * Time.ONE_HOUR;
        }
        if(streak > 30) {
            return Time.ONE_HOUR;
        }
        if(streak > 15) {
            return Time.THIRTY_MINUTES;
        }
        if(streak > 7) {
            return 15 * Time.ONE_MINUTE;
        }
        return 5 * Time.ONE_MINUTE;
    }
}
