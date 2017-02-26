package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.util.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Daniel on 6/22/2016.
 */
public class FindListCommand extends NewCommand {

    private final ListType TYPE;

    public FindListCommand(String key, Rank rank, long delay, ListType type) {
        super(key, rank, delay, new CommandInput<>(object -> true, "String", String.format("%s value", type.NAME)));
        this.TYPE = type;
    }

    @Override
    protected boolean execute(Player player, String[] input) {
        TYPE.process(player, input[0].trim());
        return true;
    }

    public enum ListType {
        ITEM {
            @Override
            public void process(final Player player, final String value) {
                final List<ItemDefinition> LIST = Stream.of(ItemDefinition.definitions)
                        .filter(def -> def != null && def.getName().toLowerCase().contains(value))
                        .sorted((one, two) -> one.getId() - two.getId())
                        .collect(Collectors.toList());
                if (LIST.isEmpty()) {
                    player.sendf("No items found with the phrase '@red@%s@bla@'", value);
                    return;
                }
                player.getActionSender().displayItems(LIST);
            }
        },
        NPC {
            @Override
            public void process(final Player player, final String value) {
                final List<NPCDefinition> LIST = Stream.of(NPCDefinition.getDefinitions())
                        .filter(def -> def != null && def.getName().toLowerCase().contains(value))
                        .sorted((one, two) -> one.getId() - two.getId())
                        .collect(Collectors.toList());
                if (LIST.isEmpty()) {
                    player.sendf("No NPCs found containing the phrase '@red@%s@bla@'.", value);
                    return;
                }
                if (player.debug) {
                    LIST.forEach(def -> player.sendf("[@red@%,d@bla@]: %s", def.getId(), def.getName()));
                }
                player.getActionSender().displayNPCs(LIST);
            }
        },
        OBJECT {
            @Override
            public void process(final Player player, final String value) {
                final List<GameObjectDefinition> LIST = Stream.of(GameObjectDefinition.getDefinitions())
                        .filter(def -> def != null && def.getName().toLowerCase().contains(value))
                        .sorted((one, two) -> one.getId() - two.getId())
                        .collect(Collectors.toList());
                if (LIST.isEmpty()) {
                    player.sendf("No Objects found containing the phrase '@red@%s@bla@'.", value);
                    return;
                }
                if (player.debug) {
                    LIST.forEach(def -> player.sendf("[@red@%,d@bla@]: %s", def.getId(), def.getName()));
                }
                player.getActionSender().displayObjects(LIST);
            }
        },
        COMMAND {
            @Override
            public void process(final Player player, final String value) {
                final List<String> COMMANDS = new ArrayList<>();
                NewCommandHandler.getCommandsList().keySet()
                        .stream()
                        .filter(rank -> Rank.hasAbility(player, rank))
                        .forEach(rank -> NewCommandHandler.getCommandsList().get(rank)
                                .stream()
                                .filter(string -> string.toLowerCase().contains(value))
                                .forEach(string -> COMMANDS.add(string.replace(value, String.format("@red@%s@bla@", value)))));
                if (COMMANDS.isEmpty()) {
                    player.sendf("No commands found with the phrase '@red@%s@bla@'", COMMAND);
                    return;
                }
                Collections.sort(COMMANDS);
                player.getActionSender().displayCommands(COMMANDS);
            }
        };

        private final String NAME;

        ListType() {
            NAME = TextUtils.titleCase(this.toString().replace("_", " "));
        }

        public abstract void process(Player player, String value);
    }
}
