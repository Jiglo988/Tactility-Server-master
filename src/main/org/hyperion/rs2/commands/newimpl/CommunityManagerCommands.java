package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.challenge.Challenge;
import org.hyperion.rs2.model.challenge.ChallengeManager;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.publicevent.EventCountdownTask;
import org.hyperion.rs2.model.content.publicevent.ServerEventTask;
import org.hyperion.rs2.model.customtrivia.CustomTrivia;
import org.hyperion.rs2.model.customtrivia.CustomTriviaManager;
import org.hyperion.rs2.util.TextUtils;

import java.util.Arrays;
import java.util.List;
//</editor-fold>
/**
 * Created by DrHales on 2/29/2016.
 */
public class CommunityManagerCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.COMMUNITY_MANAGER, delay, requiredInput);
        }

        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.COMMUNITY_MANAGER, requiredInput);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new Command("startminigame", new CommandInput<Integer>(integer -> integer > -1 && integer < 8, "Integer", "Integer between -1 & 8")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.submit(new EventCountdownTask(ServerEventTask.builders[Integer.parseInt(input[0].trim())]));
                        return true;
                    }
                },
                new Command("createtrivia", new CommandInput<Object>(object -> object != null && (String.valueOf(object) != null || Integer.parseInt(String.valueOf(object)) > Integer.MIN_VALUE && Integer.parseInt(String.valueOf(object)) < Integer.MAX_VALUE), "Object", "Trivia Question"), new CommandInput<Object>(object -> object != null && (String.valueOf(object) != null || Integer.parseInt(String.valueOf(object)) > Integer.MIN_VALUE && Integer.parseInt(String.valueOf(object)) < Integer.MAX_VALUE), "Object", "Trivia Answer"), new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Integer", "Item ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Integer", "Item Amount")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String question = input[0].trim();
                        final String answer = input[1].trim();
                        final int id = Integer.parseInt(input[2].trim());
                        int amount = Integer.parseInt(input[3].trim());
                        if (ItemSpawning.canSpawn(id)) {
                            player.sendMessage("Cannot do this with spawnables.");
                            return true;
                        }
                        if (!player.getInventory().contains(id)) {
                            player.sendf("Your inventory does not contain item '%s'.", TextUtils.optimizeText(ItemDefinition.forId(id).getName()));
                            return true;
                        }
                        final int real = player.getInventory().getCount(id);
                        if (amount > real) {
                            player.sendf("Quantity lowered; %,d -> %,d", amount, real);
                            amount = real;
                        }
                        final Item item = new Item(id, amount);
                        player.getInventory().remove(item);
                        final CustomTrivia trivia = new CustomTrivia(player, question, answer, item);
                        CustomTriviaManager.addNew(trivia);
                        return true;
                    }
                },
                new Command("createchallenge", new CommandInput<Integer>(integer -> integer > 0, "Length", "Challenge Length"), new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Integer", "Item ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Integer", "Item Amount")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int length = Integer.parseInt(input[0].trim());
                        final int id = Integer.parseInt(input[1].trim());
                        int amount = Integer.parseInt(input[2].trim());
                        if (ItemSpawning.canSpawn(id)) {
                            player.sendMessage("Cannot do this with spawnables.");
                            return true;
                        }
                        final Item item = player.getInventory().getById(id);
                        if (item == null) {
                            player.sendf("Item '%s' is not in your inventory.", TextUtils.optimizeText(ItemDefinition.forId(id).getName()));
                            return true;
                        }
                        if (item.getDefinition().isStackable()) {
                            if (amount > item.getCount()) {
                                player.sendf("Lowered amount; %,d -> %,d", amount, amount = item.getCount());
                            }
                        } else {
                            int max = 0;
                            for (final Item items : player.getInventory().toArray()) {
                                if (items != null && items.getId() == id) {
                                    max++;
                                }
                            }
                            if (amount > max) {
                                player.sendf("Lowered amount from %,d to %,d", amount, amount = item.getCount());
                            }
                        }
                        player.getInventory().remove(new Item(id, amount));
                        final Challenge challenge = Challenge.create(player, length, id, amount);
                        ChallengeManager.add(challenge);
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> challenge.send(target, true));
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
