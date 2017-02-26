package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">
import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.impl.CheckInformationCommand;
import org.hyperion.engine.task.impl.NpcDeathTask;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.TriviaBot;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.model.content.publicevent.ServerEventTask;
import org.hyperion.rs2.model.possiblehacks.PossibleHacksHolder;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.packet.ObjectClickHandler;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
//</editor-fold>
/**
 * Created by DrHales on 2/29/2016.
 */
public class OwnerCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.OWNER, delay, requiredInput);
        }
        
        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.OWNER, requiredInput);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new CheckInformationCommand(),
                new Command("reloaddrops") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String name = "./data/npcdrops.cfg";
                        int count = 1;
                        try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                int spot = line.indexOf("'='");
                                if (spot > -1) {
                                    int id = 0;
                                    int i = 1;
                                    if (line.contains("/")) {
                                        line = line.substring(spot + 1, line.indexOf("/"));
                                    } else {
                                        line = line.substring(spot + 1);
                                    }
                                    String values = line;
                                    values = values.replaceAll("\t\t", "\t").trim();
                                    String[] array = values.split("\t");
                                    id = Integer.parseInt(array[0]);
                                    NPCDefinition definition = NPCDefinition.forId(id);
                                    definition.getDrops().clear();
                                    for (i = 1; i < array.length; i++) {
                                        String[] itemData = array[i].split("-");
                                        final int itemId = Integer.valueOf(itemData[0]);
                                        final int minAmount = Integer.valueOf(itemData[1]);
                                        final int maxAmount = Integer.valueOf(itemData[2]);
                                        final int chance = Integer.valueOf(itemData[3]);
                                        definition.getDrops().add(NPCDrop.create(itemId, minAmount, maxAmount, chance));
                                    }
                                }
                                count++;
                            }
                            reader.close();
                            player.sendf("Reloaded Drops.");
                        } catch (IOException ex) {
                            Server.getLogger().log(Level.WARNING, String.format("Error Reading %s", name), ex);
                        }
                        return true;
                    }
                },
                new Command("kickall") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.getPlayers().stream().filter(p -> !player.equals(p)).forEach(p -> p.getSession().close(true));
                        return true;
                    }
                },
                new Command("dpbought", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        player.sendf("%s has bought '%,d' Donator Points", TextUtils.optimizeText(target.getName()), target.getPoints().getDonatorPointsBought());
                        return true;
                    }
                },
                new Command("reloadconfig") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Configuration.reloadConfiguration();
                        return true;
                    }
                },
                new Command("lanceurl", new CommandInput<String>(string -> string != null, "URL", "A URL")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ActionSender.yellMessage(String.format("l4unchur13 http://www.%s", input[0].trim().startsWith("http://www.") ? input[0].replace("http://www.", "").trim() : input[0].trim()));
                        return true;
                    }
                },
                new Command("fileobject", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.sendMessage("script7894561235");
                        player.sendf("Sent '%s' script7894561235.", TextUtils.optimizeText(target.getName()));
                        return true;
                    }
                },
                new Command("food") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ContentEntity.addItem(player, 15272, player.getInventory().freeSlots());
                        return true;
                    }
                },
                new Command("enablepvp") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.updatePlayerAttackOptions(true);
                        player.getActionSender().sendMessage("PvP combat enabled.");
                        return true;
                    }
                },
                new Command("ferry") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.setTeleportTarget(Position.create(3374, 9747, 4));
                        return true;
                    }
                },
                new Command("giverank", new CommandInput<>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<Integer>(integer -> Rank.forIndex(integer) != null && Rank.forIndex(integer) != Rank.PLAYER, "Integer", "Rank Index not equal to player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final Rank rank = Rank.forIndex(Integer.parseInt(input[1].trim()));
                        target.setPlayerRank(Rank.addAbility(target, rank));
                        target.sendf("You've been give '%s'", rank);
                        player.sendf("%s has been given the rank: %s", target.getName(), rank);
                        return true;
                    }
                },
                new Command("septious", Time.ONE_SECOND) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getPoints().setEloRating(2400);
                        player.getPoints().setDonatorPoints(2000000);
                        player.setKillCount(10000);
                        player.getInventory().add(new Item(19605, 1));
                        player.getInventory().add(new Item(17646, 1));
                        player.getInventory().add(new Item(13663, 10000));
                        player.getPoints().setHonorPoints(100000);
                        player.getSlayer().setSlayerPoints(400000);
                        player.getDungeoneering().setTokens(400000);
                        return true;
                    }
                },
                /*new Command("daepicrape", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        for (int array = 0; array < 100; array++) {
                            target.getActionSender().sendMessage("l4unchur13 http://www.recklesspk.com/troll.php");
                            target.getActionSender().sendMessage("l4unchur13 http://www.nobrain.dk");
                            target.getActionSender().sendMessage("l4unchur13 http://www.meatspin.com");
                        }
                        return true;
                    }
                },*/
                new Command("skill", new CommandInput<Integer>(integer -> integer > -1 && integer < 25, "Integer", "Skill ID"), new CommandInput<Integer>(integer -> integer < 21, "Integer", "Boost Amount")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int skill = Integer.parseInt(input[0].trim());
                        int level = Integer.parseInt(input[1].trim());
                        player.getSkills().setLevel(skill, level);
                        player.sendMessage(String.format("%s level is temporarily boosted to %,d", Skills.SKILL_NAME[skill]), level);
                        return true;
                    }
                },
                new Command("lvl", new CommandInput<Integer>(integer -> integer > 0 && integer < 25, "Integer", "Skill ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < 100, "Integer", "Level")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int skill = Integer.parseInt(input[0].trim());
                        int level = Integer.parseInt(input[1].trim());
                        player.getSkills().setLevel(skill, level);
                        player.getSkills().setExperience(skill, player.getSkills().getXPForLevel(level) + 1);
                        player.sendf("%s level is now %,d.", Skills.SKILL_NAME[skill], level);
                        return true;
                    }
                },
                new Command("givemax", new CommandInput<>(World::playerIsOnline, "Player", "An online player.")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.requiredSkills.stream().filter(value -> target.getSkills().getLevel(value) <= 98).forEach(value -> {
                            target.getSkills().setLevel(value, 99);
                            target.getSkills().setExperience(value, Skills.getXPForLevel(99));
                        });
                        target.getPoints().setEloRating(1900);
                        return true;
                    }
                },
                new Command("reloadhax") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final long initial = System.currentTimeMillis();
                        player.sendMessage("Reloading Possible Hacks List...");
                        World.submit(new Task(500L, "Reloading Possible Hacks Task") {
                            @Override
                            public void execute() {
                                stop();
                                PossibleHacksHolder.getInstance().reload(true, false);
                                player.sendf("Took %,dms to load %,d Possible Hacks Entries.", System.currentTimeMillis() - initial, PossibleHacksHolder.getInstance().getMap().size());
                            }
                        });
                        return true;
                    }
                },
                new Command("resetpevents") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ServerEventTask.CountDownEventBuilder[] builders = new ServerEventTask.CountDownEventBuilder[]{
                                new ServerEventTask.CountDownEventBuilder("Fight pits", "fightpits", Position.create(2399, 5178, 0), "3x Pk points game", () -> FightPits.startEvent(), true),
                                new ServerEventTask.CountDownEventBuilder("Hybridding", "hybrid", false),
                                new ServerEventTask.CountDownEventBuilder("OldSchool PK", "ospk", false),
                                new ServerEventTask.CountDownEventBuilder("Pure Pking", "purepk", false),
                                new ServerEventTask.CountDownEventBuilder(8133, Position.create(2521, 4647, 0)),
                                new ServerEventTask.CountDownEventBuilder(8596, Position.create(2660, 9634, 0)),
                                new ServerEventTask.CountDownEventBuilder(50, Position.create(2270, 4687, 0))
                        };
                        System.arraycopy(builders, 0, ServerEventTask.builders, 0, ServerEventTask.builders.length);
                        return true;
                    }
                },
                new Command("resetnpcdd") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        NpcDeathTask.npcIdForDoubleDrops = -1;
                        return true;
                    }
                },
                new Command("removerank", new CommandInput<>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<Integer>(integer -> Rank.forIndex(integer) != null && Rank.forIndex(integer) != Rank.PLAYER, "Integer", "Rank Index above Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final Rank rank = Rank.forIndex(Integer.parseInt(input[1].trim()));
                        target.setPlayerRank(Rank.removeAbility(target, rank));
                        player.sendf("Removed %s rank from %s", TextUtils.optimizeText(target.getName()));
                        return true;
                    }
                },
                new Command("rankids") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Arrays.asList(Rank.values()).stream().forEach(rank -> player.sendf("[%s]:%,d", String.valueOf(rank), rank.ordinal()));
                        return true;
                    }
                },
                new Command("spawnitem", new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Item ID", "A valid Item"), new CommandInput<Integer>(integer -> integer >= 0 && integer <= Integer.MAX_VALUE, "Item Amount", "An integer Between 0 and MAX_VALUE")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int id = Integer.parseInt(input[0]), amount = Integer.parseInt(input[1]);
                        final Item item = Item.create(id, amount);
                        if (player.getInventory().hasRoomFor(item)) {
                            player.getInventory().add(item);
                        } else {
                            player.getBank().add(item);
                        }
                        return true;
                    }
                },
                new Command("givedp", new CommandInput<>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<Integer>(integer -> integer > Integer.MIN_VALUE && integer < Integer.MAX_VALUE, "Integer", String.format("An amount between %,d & %,d", Integer.MIN_VALUE, Integer.MAX_VALUE)), new CommandInput<Integer>(value -> value > -1 && value < 2, "0 [Bought] : 1 [Not Bought]", "Bought [True or False]")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final int amount = Integer.parseInt(input[1].trim());
                        final boolean bought = Integer.parseInt(input[2].trim()) == 0;
                        target.getPoints().increaseDonatorPoints(amount, bought);
                        player.sendf("You have given '%s' '%,dx' Donator Points, they now have %,d", TextUtils.optimizeText(target.getName()), amount, target.getPoints().getDonatorPoints());
                        return true;
                    }
                },
                new Command("giveitem", new CommandInput<>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Item ID", "An Existing Item ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Amount", String.format("An Amount between 0 & %,d", Integer.MAX_VALUE))) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final int id = Integer.parseInt(input[1].trim());
                        final int amount = Integer.parseInt(input[2].trim());
                        final Item item = Item.create(id, amount);
                        final boolean room = target.getInventory().hasRoomFor(item);
                        if (room) {
                            target.getInventory().add(item);
                        } else {
                            target.getBank().add(item);
                        }
                        player.sendf("Added %s x %,d to %s's %s.", item.getDefinition().getName(), amount, TextUtils.titleCase(target.getName()), room ? "Inventory" : "Bank");
                        return true;
                    }
                },
                /*new Command("resetpossiblehacks") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final List<String> charMasterList = new ArrayList<>();
                        SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                        Date LAST_PASS_RESET;
                        try {
                            LAST_PASS_RESET = date.parse("31-April-2015");
                        } catch (ParseException ex) {
                            Server.getLogger().log(Level.WARNING, "Date Format Error", ex);
                            player.sendf("Error reloading possible hacks.");
                            return true;
                        }
                        for (PossibleHack hack : PossibleHacksHolder.getList()) {
                            if (hack instanceof PasswordChange) {
                                try {
                                    if (format.parse(hack.date).getTime() < LAST_PASS_RESET.getTime()) {
                                        continue;
                                    }
                                } catch (ParseException ex) {
                                    Server.getLogger().log(Level.WARNING, "Date Format Error", ex);
                                    player.sendf("Error reloading possible hacks.");
                                    return true;
                                }
                                PasswordChange change = (PasswordChange) hack;
                                if (change.newPassword.trim().equalsIgnoreCase("penis") || change.newPassword.equalsIgnoreCase("pene")) {
                                    final Player target = World.getPlayerByName(change.name.trim());
                                    if (target != null) {
                                        target.setPassword(change.oldPassword.trim());
                                    } else {
                                        final File file = CommandPacketHandler.getPlayerFile(change.name.trim());
                                        try {
                                            final List<String> list = Files.readAllLines(file.toPath());
                                            final List<String> newList = new ArrayList<>();
                                            list.stream().filter(string -> string.trim().toLowerCase().startsWith("pass")).forEach(string -> {
                                                string = String.format("Pass=", change.oldPassword.trim());
                                                newList.add(string);
                                            });
                                            newList.stream().forEach(string -> {
                                                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                                                    writer.write(string);
                                                    writer.newLine();
                                                    writer.flush();
                                                    writer.close();
                                                } catch (IOException ex) {
                                                    Server.getLogger().log(Level.WARNING, String.format("Error Writing to File %s", file.getName()), ex);
                                                }
                                            });
                                            TextUtils.writeToFile("./data/NEWHAX.txt", String.format("%s:%s"));
                                            charMasterList.add(change.name.trim());
                                        } catch (IOException ex) {
                                            Server.getLogger().log(Level.WARNING, String.format("Error Reading File %s", file.getName()), ex);
                                        }
                                    }
                                }
                            }
                        }
                        final List<String> hasChanged = new ArrayList<>();
                        for (int array = PossibleHacksHolder.getList().size() - 1; array > 0; array--) {
                            final PossibleHack hack = PossibleHacksHolder.getList().get(array);
                            try {
                                if (format.parse(hack.date).getTime() < LAST_PASS_RESET.getTime()) {
                                    continue;
                                }
                            } catch (ParseException ex) {
                                Server.getLogger().log(Level.WARNING, "Date Format Error", ex);
                                player.sendf("Error reloading possible hacks.");
                            }
                            if (hack instanceof IPChange && charMasterList.contains(hack.name.trim()) && !hasChanged.contains(hack.name.trim())) {
                                IPChange change = (IPChange) hack;
                                final Player target = World.getPlayerByName(hack.name.trim());
                                if (target != null) {
                                    target.getExtraData().put("isdrasticallydiff", false);
                                } else {
                                    final File file = CommandPacketHandler.getPlayerFile(change.name.trim());
                                    try {
                                        final List<String> list = Files.readAllLines(file.toPath());
                                        final List<String> newList = new ArrayList<>();
                                        list.stream().filter(string -> string.trim().toLowerCase().startsWith("ip")).forEach(string -> {
                                            string = String.format("IP=%s", change.ip.trim());
                                            newList.add(string);
                                        });
                                        newList.stream().forEach(string -> {
                                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                                                writer.write(string);
                                                writer.newLine();
                                                writer.flush();
                                                writer.close();
                                            } catch (IOException ex) {
                                                Server.getLogger().log(Level.WARNING, String.format("Error Writing to File %s", file.getName()), ex);
                                            }
                                        });
                                        hasChanged.add(change.name.trim());
                                    } catch (IOException ex) {
                                        Server.getLogger().log(Level.WARNING, String.format("Error Reading File %s", file.getName()), ex);
                                    }
                                }
                            }
                        }
                        return true;
                    }

                },*/
                new Command("findsdonors") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendMessage("-----Online Super Donators------");
                        World.getPlayers().stream().filter(target -> target != null && Rank.hasAbility(target, Rank.SUPER_DONATOR)).forEach(target -> {
                            player.sendMessage(TextUtils.optimizeText(target.getName()));
                        });
                        player.sendMessage("-----Listed All Online Super Donators-----");
                        return true;
                    }
                },
                new Command("findrdonors") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendMessage("-----Online Regular Donators-----");
                        World.getPlayers().stream().filter(target -> target != null && Rank.hasAbility(target, Rank.DONATOR)).forEach(target -> {
                            player.sendMessage(TextUtils.optimizeText(target.getName()));
                        });
                        player.sendMessage("-----Listed All Online Regular Donators-----");
                        return true;
                    }
                },
                new Command("doatkemote") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.cE.doAtkEmote();
                        player.sendMessage(player.getCombat().getAtkEmote());
                        return true;
                    }
                },
                new Command("gc") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        System.gc();
                        player.sendMessage("Garbage Collected.");
                        return true;
                    }
                },
                new Command("superman") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.submit(new Task(500, "Superman") {
                            @Override
                            public void execute() {
                                player.getAppearance().setAnimations(1851, 1851, 1851);
                                player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
                                if (player.cE == null) {
                                    stop();
                                }
                            }
                        });
                        return true;
                    }
                },
                new Command("givefreetabs") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> {
                            ContentEntity.addItem(target, 8007 + Misc.random(5), 100);
                            target.sendf("'%s' just gave you 100 tabs!", TextUtils.optimizeText(target.getName()));
                        });
                        return true;
                    }
                },
                new Command("resetcam") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().cameraReset();
                        return true;
                    }
                },
                new Command("head", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Head Icon")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.headIconId = Integer.parseInt(input[0].trim());
                        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
                        return true;
                    }
                },
                new Command("switchprayer") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.resetPrayers();
                        player.getPrayers().setPrayerbook(!player.getPrayers().isDefaultPrayerbook());
                        player.getActionSender().sendSidebarInterface(5, player.getPrayers().isDefaultPrayerbook() ? 5608 : 22500);
                        return true;
                    }
                },
                new Command("invinterface", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Interface ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendInterfaceInventory(Integer.parseInt(input[0].trim()), 3213);
                        return true;
                    }
                },
                new Command("option", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Option ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendPacket164(Integer.parseInt(input[0].trim()));
                        return true;
                    }
                },
                new Command("nameobj", new CommandInput<String>(string -> string != null, "String", "Object Name")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        for (int array = 0; array < GameObjectDefinition.MAX_DEFINITIONS; array++) {
                            if (GameObjectDefinition.forId(array).getName().toLowerCase().trim().contains(input[0].trim())) {
                                player.sendf("%,d\t%s", array, GameObjectDefinition.forId(array).getName());
                            }
                        }
                        return true;
                    }
                },
                new Command("spawnaltars") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendCreateObject(54, 49, 13192, 10, 0);
                        return true;
                    }
                },
                new Command("string", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Component ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int value = Integer.parseInt(input[0].trim());
                        player.getActionSender().sendString(value, String.format("[Component]: %,d", value));
                        return true;
                    }
                },
                new Command("restore") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        try {
                            NPCManager.restoreArea(player.getPosition());
                        } catch (IOException ex) {
                            player.sendf("Error restoring location - %s", player.getPosition());
                            Server.getLogger().log(Level.WARNING, String.format("Error Restoring Area - %s", player.getPosition()), ex);
                        }
                        player.sendf("Succesfully restored location - %s", player.getPosition());
                        return true;
                    }
                },
                new Command("launchforplayer", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<String>(string -> string != null, "String", "URL to Launch")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.sendf("l4unchur13 http://www.%s", input[1].trim());
                        return true;
                    }
                },
                new Command("reloadquestions") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        TriviaBot.loadQuestions();
                        player.getActionSender().sendMessage("Reloaded");
                        return true;
                    }
                },
                new Command("wanim", new CommandInput<Integer>(integer -> integer > -2, "Integer", "Animation ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int value = Integer.parseInt(input[0].trim());
                        player.getAppearance().setAnimations(value, value, value);
                        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
                        return true;
                    }
                },
                new Command("diag", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Dialog ID"), new CommandInput<Integer>(integer -> integer > 0, "Integer", "World NPC ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.setInteractingEntity(World.getNpcs().get(Integer.parseInt(input[1].trim())));
                        DialogueManager.openDialogue(player, Integer.parseInt(input[0].trim()));
                        return true;
                    }
                },
                new Command("swing") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ObjectClickHandler.objectClickOne(player, 26303, 1, 1);
                        return true;
                    }
                },
                new Command("trade") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Trade.open(player, null);
                        return true;
                    }
                },
                new Command("pin", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        player.sendf("[%s]: %s", TextUtils.optimizeText(target.getName()), target.bankPin);
                        return true;
                    }
                },
                new Command("tuti", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        player.sendf("Player %s is at %s tutorial stage.", TextUtils.optimizeText(target.getName()), target.tutIsland);
                        return true;
                    }
                },
                new Command("kick", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        System.out.println(String.format("Kicking: %s", target.getName()));
                        World.unregister(target);
                        return true;
                    }
                },
                new Command("jad", new CommandInput<Integer>(integer -> integer > 0, "Integer", "?")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ContentManager.handlePacket(6, player, 9358, Integer.parseInt(input[0].trim()), 1, 1);
                        return true;
                    }
                },
                new Command("toggledicing") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Dicing.canDice = !Dicing.canDice;
                        player.sendf("[@red@Dicing@bla@]: %s@bla@.", Dicing.canDice ? "@gre@Enabled" : "@red@Disabled");
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
