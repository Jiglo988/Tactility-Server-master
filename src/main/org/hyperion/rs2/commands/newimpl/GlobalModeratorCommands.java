package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.util.TextUtils;

import java.util.Arrays;
import java.util.List;
//</editor-fold>

/**
 * Created by DrHales on 2/29/2016.
 */
public class GlobalModeratorCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.GLOBAL_MODERATOR, delay, requiredInput);
        }

        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.GLOBAL_MODERATOR, requiredInput);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new Command("setplayertag", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<String>(string -> string != null, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (Rank.hasAbility(target, Rank.ADMINISTRATOR) && target != player) {
                            return true;
                        }
                        final String value = Yelling.isValidTitle(input[1].trim());
                        target.getYelling().setYellTitle(value);
                        return true;
                    }
                },
                new Command("removeplayertag", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (Rank.hasAbility(target, Rank.ADMINISTRATOR) && target != player) {
                            return true;
                        }
                        target.getYelling().setYellTitle("");
                        return true;
                    }
                },
                new Command("givedice", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final Item item = Item.create(15098, 1);
                        final boolean space = target.getInventory().hasRoomFor(item);
                        if (space) {
                            target.getInventory().add(item);
                        } else {
                            target.getBank().add(item);
                        }
                        player.sendf("%s has been given a Dicing Bag.", TextUtils.titleCase(target.getName()));
                        target.sendf("%s has put a Dicing bag into your %s.",TextUtils.titleCase(player.getName()), space ? "Inventory" : "Bank");
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
