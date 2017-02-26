package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.util.Time;

/**
 * Created by DrHales on 3/3/2016.
 */
public class MaxCommand extends NewCommand {

    private static boolean submitted = false;

    public MaxCommand(String key) {
        super(key, Rank.PLAYER, Time.TEN_SECONDS);
        if (!submitted) {
            SkillSetCommand.initiate();
            submitted = true;
        }
    }

    public boolean execute(final Player player, final String[] input) {
        if (!ItemSpawning.canSpawn(player)
                || player.getPosition().cannotMax()
                || FightPits.inGame(player)
                || player.getPosition().inPvPArea()
                || player.getPosition().inCorpBeastArea()
                || player.getPosition().inFunPk()
                || player.duelAttackable > 0) {
            player.sendMessage("You cannot do that here.");
            return true;
        }
        if (System.currentTimeMillis() - player.cE.lastHit < 5000) {
            player.sendMessage("You are currently busy.");
            return true;
        }
        for (int array = 0; array <= 6; array++) {
            player.getSkills().setLevel(array, 99);
            player.getSkills().setExperience(array, Math.max(13100000, player.getSkills().getExperience(array)));
        }
        return true;
    }

}
