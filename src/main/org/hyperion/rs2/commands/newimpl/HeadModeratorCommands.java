package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">
import org.hyperion.Server;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.util.TextUtils;

import java.util.Arrays;
import java.util.List;
//</editor-fold>
/**
 * Created by DrHales on 2/29/2016.
 */
public class HeadModeratorCommands implements NewCommandExtension {
    
    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.HEAD_MODERATOR, delay, requiredInput);
        }
        
        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.HEAD_MODERATOR, requiredInput);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new Command("resetdeaths", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.setDeathCount(0);
                        return true;
                    }
                },
                new Command("resetkills", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.setKillCount(0);
                        return true;
                    }
                },
                new Command("resetelo", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.getPoints().setEloRating(1200);
                        return true;
                    }
                },
                new Command("update", new CommandInput<>(object -> String.valueOf(object) != null, "String", "Restart Reason")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Server.update(120, String.format("%sRestart Request", player.getName()));
                        return true;
                    }
                },
                new Command("sethp", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Integer", "HP Amount")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (Rank.getPrimaryRank(player.getPlayerRank()).ordinal() < Rank.getPrimaryRank(target.getPlayerRank()).ordinal()) {
                            player.sendMessage("You cannot do this to those with a greater rank than yours.");
                            return true;
                        }
                        int level = Integer.parseInt(input[1].trim());
                        target.getSkills().setLevel(Skills.HITPOINTS, level);
                        player.sendf("You have set %s's hitpoints to %,d.", TextUtils.optimizeText(target.getName()), level);
                        target.sendf("%s set your hitpoints to %,d.", TextUtils.optimizeText(player.getName()), level);
                        return true;
                    }
                },
                new Command("unlock", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.getPermExtraData().put("passchange", System.currentTimeMillis());
                        target.getExtraData().put("needpasschange", false);
                        target.getExtraData().put("cantchangepass", false);
                        target.getExtraData().put("cantdoshit", false);
                        target.sendMessage("You have been unlocked by an admin");
                        return true;
                    }
                },
                new Command("checkclans") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ClanManager.clans.values().stream().filter(clan -> clan.getPlayers().size() > 0 && !clan.getName().toLowerCase().startsWith("party")).forEach(clan -> {
                            player.sendf("Clan: %s, Owner: %s, Members: %d", clan.getName(), clan.getOwner(), clan.getPlayers().size());
                        });
                        return true;
                    }
                },
                new Command("spawnobject", new CommandInput<Integer>(integer -> GameObjectDefinition.forId(integer) != null, "Integer", "Object ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < 21, "Integer", "Object Face"), new CommandInput<Integer>(integer -> integer > -1 && integer < 23, "Integer", "Object Type")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int id = Integer.parseInt(input[0].trim());
                        final int type = Integer.parseInt(input[1].trim());
                        final int face = Integer.parseInt(input[2].trim());
                        player.getActionSender().sendCreateObject(id, type, face, player.getPosition());
                        return true;
                    }
                },
                new Command("givekorasi", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final Item item = new Item(19780, 1);
                        if (!target.getInventory().hasRoomFor(item)) {
                            target.getBank().add(item);
                        } else {
                            target.getInventory().add(item);
                        }
                        player.sendf("a Korasi's Sword has been added to your %s.", target.getInventory().hasRoomFor(item) ? "Inventory" : "Bank");
                        return true;
                    }
                },
                new Command("givevigour", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final Item item = new Item(19669, 1);
                        if (!target.getInventory().hasRoomFor(item)) {
                            target.getBank().add(item);
                        } else {
                            target.getInventory().add(item);
                        }
                        player.sendf("a Ring of Vigour has been added to your %s.", target.getInventory().hasRoomFor(item) ? "Inventory" : "Bank");
                        return true;
                    }
                },
                new Command("resetkdr", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.setKillCount(0);
                        target.setDeathCount(0);
                        player.sendf("You have reset %s's KDR.", TextUtils.optimizeText(target.getName()));
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
