package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

public class PrayerRenwalTask extends Task {

    /**
     * This key will be stored inside extradata - it will represent whether renewal is active or not
     */
    public static final String KEY = "renewalactive";

    private static final int PER_CYCLE = 2;

    public int totalRenewal = 300;

    private final Player player;

    public PrayerRenwalTask(final Player player) {
        super(2500L);
        this.player = player;
        player.getExtraData().put(KEY, this);  //6568
    }


    @Override
    public void execute() {
        if(!player.getSession().isConnected()) {
            stop();
        }

        int newLevel = player.getSkills().getLevel(Skills.PRAYER) + PER_CYCLE;
        final int realLevel = player.getSkills().getRealLevels()[Skills.PRAYER];
        if(newLevel > realLevel)
            newLevel = realLevel;
        player.getSkills().setLevel(Skills.PRAYER, newLevel);
        player.playGraphics(Graphic.create(1295));

        totalRenewal -= PER_CYCLE;

        if(totalRenewal <= 0) {
            stop();
        }

    }
    @Override
    public void stop() {
        super.stop();
        player.getExtraData().remove(KEY);
        player.sendImportantMessage("Your prayer renewal has run out!");
    }
}
