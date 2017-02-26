package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.util.Time;

import java.util.Arrays;

/**
 * Created by DrHales on 3/4/2016.
 */
public class SkillSetCommand extends NewCommand {

    private final int skill;

    private static final String[][] COMMAND_NAMES = {
            {"atk", "attk", "attack"},
            {"def", "defense", "defence"},
            {"str", "strength"},
            {"hp", "hitp", "hitpoints"},
            {"rng", "range", "ranging", "ranged"},
            {"pray", "prayer"},
            {"mage", "magic"}
    };

    public SkillSetCommand(String key, int skill) {
        super(key, Rank.PLAYER, Time.TEN_SECONDS, new CommandInput<Integer>(integer -> integer > 0 && integer < 100, "Integer", "An Integer between 0 & 100."));
        this.skill = skill;
    }

    public static void initiate() {
        for (int array = 0; array < COMMAND_NAMES.length; array++) {
            for (String name : COMMAND_NAMES[array]) {
                NewCommandHandler.submit(
                        new SkillSetCommand(name, array)
                );
            }
        }
    }

    public boolean execute(final Player player, final String[] input) {
        if (player.getPosition().cannotMax()
                || !ItemSpawning.canSpawn(player)
                || !canChangeLevel(player)) {
            player.sendMessage("You cannot do this right now.");
            return true;
        }
        if (!ItemSpawning.copyCheck(player)) {
            return true;
        }
        if (ContentEntity.getTotalAmountOfEquipmentItems(player) > 0) {
            player.sendMessage("You need to take off your armour before setting levels!");
            return true;
        }
        final int level = Integer.parseInt(input[0].trim());
        if (skill == 5 || skill == 1) {
            player.resetPrayers();
        }
        player.getSkills().setLevel(skill, level);
        player.getSkills().setExperience(skill, Skills.getXPForLevel(level));
        return true;
    }

    public static boolean canChangeLevel(Player player) {
        if (player.getPosition().inPvPArea()
                || player.duelAttackable > 0
                || FightPits.inGame(player)
                || player.getEquipment().size() > 0) {
            return false;
        } else {
            return true;
        }
    }

}
