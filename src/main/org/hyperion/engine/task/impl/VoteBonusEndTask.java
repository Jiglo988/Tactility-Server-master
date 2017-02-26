package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.Lock;

import java.util.Arrays;

/**
 * Created by Gilles on 16/02/2016.
 */
public class VoteBonusEndTask extends Task {

    private final static Lock[] LOCKS_TO_DISABLE = new Lock[]{Lock.DOUBLE_EXPERIENCE, Lock.INCREASED_DROP_RATE, Lock.REDUCED_YELL_DELAY};
    private final Player player;

    public VoteBonusEndTask(Player player, long delay) {
        super(delay);
        this.player = player;
    }

    @Override
    protected void execute() {
        player.setVoteBonusEndTime(0, false);
        Arrays.stream(LOCKS_TO_DISABLE).forEach(lock -> Lock.switchLock(player, lock));
        player.sendImportantMessage("Your voting bonus has ended!");
        stop();
    }
}
