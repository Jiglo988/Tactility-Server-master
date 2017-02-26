package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">

import org.hyperion.Server;
import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.impl.cmd.SpawnCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.BoB;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.content.Events;
import org.hyperion.rs2.model.content.misc.TriviaBot;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.util.AccountLogger;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.impl.log.type.IPLog;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
//</editor-fold>

/**
 * Created by Drhales on 2/29/2016.
 */
public class ModeratorCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.MODERATOR, delay, requiredInput);
        }

        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.MODERATOR, requiredInput);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new Command("mypos", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int x = player.getPosition().getX();
                        final int y = player.getPosition().getY();
                        final int z = player.getPosition().getZ();
                        final int rx = x >> 6;
                        final int ry = y >> 6;
                        player.sendf("[X]: %d, [Y]: %d, [Z]: %d, [Region]: %d, [RX]: %d, [RY]: %d", x, y, z, (rx << 8) + ry, rx, ry);
                        return true;
                    }
                },
                new Command("getinfo", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        assert target != null;
                        player.sendf("Creation Date: " + new Date(target.getCreatedTime()));
                        player.sendf("Last HP Rewards: %s", new Date(target.getLastHonorPointsReward()));
                        return true;
                    }
                },
                new Command("removeevent") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (Events.eventName == null
                                && Events.eventName.isEmpty()) {
                            player.sendMessage("There is no event active.");
                            return true;
                        }
                        final String event = Events.eventName;
                        final String name = TextUtils.titleCase(player.getName());
                        Events.resetEvent();
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> target.sendServerMessage(String.format("%s has ended the event '%s'", name, event)));
                        return true;
                    }
                },
                new Command("alts", Time.TEN_SECONDS, new CommandInput<Object>(PlayerLoading::playerExists, "Player", "An player that exists in the system.")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String targetName = input[0];
                        player.sendMessage("Getting " + Misc.formatPlayerName(targetName) + "'s alts... Please be patient.");
                        GameEngine.submitSql(new EngineTask<Boolean>("alts command", 10, TimeUnit.SECONDS) {
                            @Override
                            public void stopTask() {
                                player.sendMessage("Request timed out... Please try again at a later point.");
                            }

                            @Override
                            public Boolean call() throws Exception {
                                List<String> usedIps = DbHub.getPlayerDb().getLogs().getIpForPlayer(targetName).stream().filter(value -> value != null).map(IPLog::getIp).collect(Collectors.toList());
                                if (usedIps.isEmpty()) {
                                    player.sendf("No used Protocols for player %s.", targetName);
                                    return true;
                                }
                                usedIps = usedIps.stream().filter(ip -> !GenericWorldLoader.isIpAllowed(ip)).collect(Collectors.toList());
                                List<IPLog> alts = new ArrayList<>();
                                if (alts.isEmpty()) {
                                    player.sendf("No Alternate Accounts for player %s.", targetName);
                                    return true;
                                }
                                usedIps.forEach(entry -> DbHub.getPlayerDb().getLogs().getAltsByIp(entry).forEach(alts::add));
                                player.sendMessage("@dre@" + Misc.formatPlayerName(targetName) + " has " + alts.size() + " alt" + (alts.size() == 1 ? "" : "s") + ".");
                                alts.forEach(alt -> player.sendMessage("@dre@" + Misc.formatPlayerName(alt.getPlayerName() + " @bla@- Last login: @dre@" + alt.getFormattedTimestamp() + "@bla@ IP used: @dre@" + alt.getIp())));
                                return true;
                            }
                        });
                        return true;
                    }
                },
                new Command("createevent", new CommandInput<Integer>(integer -> integer > 0 && integer < 15000, "X", "An Amount between 0 & 15000"), new CommandInput<Integer>(integer -> integer > 0 && integer < 15000, "Y", "An Amount between 0 & 15000"), new CommandInput<Integer>(integer -> integer > -1 && integer < 100, "Z", "An Amount between -1 & 100"), new CommandInput<String>(string -> string != null, "Event", "A new Event name")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (Events.eventName != null
                                && !Events.eventName.isEmpty()) {
                            player.sendf("%s event is currently active; Remove it via ::removeevent command", Events.eventName);
                            return true;
                        }
                        final int x = Integer.parseInt(input[0].trim());
                        final int y = Integer.parseInt(input[1].trim());
                        final int z = Integer.parseInt(input[2].trim());
                        if (Combat.getWildLevel(x, y) > 0) {
                            player.sendMessage("Events cannot take place inside the wilderness.");
                            return true;
                        }
                        final String event = TextUtils.titleCase(input[3].trim());
                        Events.fireNewEvent(TextUtils.ucFirst(event.toLowerCase()), true, 0, Position.create(x, y, z));
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> {
                            target.sendServerMessage(String.format("%s has just created the event '%s'.", player.getSafeDisplayName(), Events.eventName));
                            target.sendServerMessage("Click it int the questtab to join in!");
                        });
                        return true;
                    }
                },
                new Command("altsinwildy") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Map<String, List<Player>> map = new HashMap<>();
                        World.getPlayers().stream().filter(target -> target != null &&
                                (target.getLocation().equals(Locations.Location.WILDERNESS)
                                        || target.getLocation().equals(Locations.Location.WILDERNESS_MULTI)
                                        || target.getLocation().equals(Locations.Location.WILDERNESS_DUNGEON)
                                        || target.getLocation().equals(Locations.Location.KBD_WILDERNESS_ENTRANCE))).forEach(target -> {
                            final String protocol = target.getShortIP();
                            if (!map.containsKey(protocol)) {
                                map.put(protocol, new ArrayList<>());
                            }
                            if (!map.get(protocol).contains(target)) {
                                map.get(protocol).add(target);
                            }
                        });
                        map.keySet().forEach(protocol -> {
                            final List<Player> list = map.get(protocol);
                            if (list.size() > 1) {
                                player.sendMessage("@red@----------");
                                list.forEach(target -> {
                                    player.sendf("[%s]:%s, %d, %d, %d", protocol.substring(0, protocol.lastIndexOf(".")), TextUtils.titleCase(target.getName()), target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ());
                                });
                                player.sendMessage("@red@----------");
                            }
                        });
                        return true;
                    }
                },
                new Command("checkpkstats", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        player.sendf("[%s] [Elo]: %,d - [K/D]: %d/%d - [KS]: %d", TextUtils.optimizeText(target.getName()),
                                target.getPoints().getEloRating(), target.getKillCount(), target.getDeathCount(), target.getKillStreak());
                        return true;
                    }
                },
                new Command("checkpts", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final PlayerPoints points = target.getPoints();
                        player.getActionSender().openQuestInterface(
                                TextUtils.optimizeText(target.getName()),
                                new String[]{String.format("[PK Points]: %,d", points.getPkPoints()),
                                        String.format("[Honor Points]: %,d", points.getHonorPoints()),
                                        String.format("[Voting Points]: %,d", points.getVotingPoints()),
                                        String.format("[Donator Points]: %,d", points.getDonatorPoints()),
                                        String.format("[Bounty Hunter Points]: %,d", target.getBountyHunter().getKills()),
                                        String.format("[Emblem Points]: %,d", target.getBountyHunter().getEmblemPoints()),}
                        );
                        return true;
                    }
                },
                new Command("trackdownnames") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendMessage("Executing Command.");
                        World.getPlayers().stream().filter(target -> target.getPosition().equals(player.getPosition())).forEach(target -> {
                            player.sendf("[Name]: %s", target.getSafeDisplayName().replaceAll(" ", "_ "));
                        });
                        return true;
                    }
                },
                new Command("rshu", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.getExtraData().put("rshu", true);
                        return true;
                    }
                },
                new Command("setkeyword", new CommandInput<String>(string -> string != null, "String", "Item Keyword"), new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Integer", "An Item ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String keyword = input[0].trim();
                        int id = Integer.parseInt(input[1].trim());
                        if (SpawnCommand.getId(keyword) != null) {
                            player.sendf("Keyword '%s' was already set before.", keyword);
                            if (Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
                                int old = SpawnCommand.getId(keyword);
                                if (id == old) {
                                    player.sendf("ID '%,d' was alread set.", id);
                                    return true;
                                }
                                SpawnCommand.setKeyword(keyword, id);
                                return true;
                            }
                        }
                        SpawnCommand.setKeyword(keyword, id);
                        return true;
                    }
                },
                new Command("xteleto", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getPosition().inPvPArea() && !Rank.hasAbility(player, Rank.ADMINISTRATOR)
                                || player.duelAttackable > 0 && !Rank.hasAbility(player, Rank.DEVELOPER)) {
                            player.sendf("You are currently %s.", player.getPosition().inPvPArea() ? "in a PVP area" :
                                    player.duelAttackable > 0 ? "in a Duel" : "unable to perform this command.");
                            return true;
                        }
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (target.duelAttackable > 0 && !Rank.hasAbility(player, Rank.DEVELOPER)) {
                            player.sendf("Player is currently %s.", target.duelAttackable > 0 ? "in a duel" : "unavailable");
                            return true;
                        }
                        player.setTeleportTarget(target.getPosition());
                        return true;
                    }
                },
                new Command("xteletome", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getPosition().inPvPArea() && !Rank.hasAbility(player, Rank.DEVELOPER)
                                || player.duelAttackable > 0 && !Rank.hasAbility(player, Rank.DEVELOPER)) {
                            player.sendf("You are currently %s.", player.getPosition().inPvPArea() ? "in a PVP area" :
                                    player.duelAttackable > 0 ? "in a duel" : "unable to perform this command");
                            return true;
                        }
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (target.duelAttackable > 0 && !Rank.hasAbility(player, Rank.DEVELOPER)
                                || !Rank.hasAbility(player, Rank.getPrimaryRank(target)) && Rank.isStaffMember(target)) {
                            player.sendf("Player is currently %s.", target.duelAttackable > 0 ? "in a duel" : "unavailable");
                            return true;
                        }
                        target.setTeleportTarget(player.getPosition());
                        return true;
                    }
                },
                new Command("tele", 0, new CommandInput<Integer>(integer -> integer > 0, "Integer", "X"), new CommandInput<Integer>(integer -> integer > 0, "Integer", "Y"), new CommandInput<Integer>(integer -> integer > -1, "Integer", "Z")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getPosition().inPvPArea() && !Rank.hasAbility(player, Rank.ADMINISTRATOR)
                                || player.duelAttackable > 0 && !Rank.hasAbility(player, Rank.DEVELOPER)) {
                            player.sendf("You are currently %s.", player.getPosition().inPvPArea() ? "in a PVP area" :
                                    player.duelAttackable > 0 ? "in a Duel" : "unable to perform this command.");
                            return true;
                        }
                        player.setTeleportTarget(Position.create(Integer.parseInt(input[0].trim()), Integer.parseInt(input[1].trim()), Integer.parseInt(input[2].trim())));
                        return true;
                    }
                },
                new Command("npcids") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.getNpcs().stream().filter(npc -> npc != null && (player.getPosition().distance(npc.getPosition()) < 5)).forEach(npc -> {
                            player.sendf("[NPC]: %d, %d", npc.getDefinition().getId(), npc.getDefinition().combat());
                        });
                        return true;
                    }
                },
                new Command("dumpservtimes") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ServerTimeManager.getSingleton().dumpValues();
                        player.sendMessage("Dumped all values.");
                        return true;
                    }
                },
                new Command("teletospammer") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        LinkedList<Spam> players = new LinkedList<Spam>();
                        World.getPlayers().stream().filter(target -> target != null && target.getSpam().isSpamming()).forEach(target -> {
                            players.add(target.getSpam());
                        });
                        if (players.size() > 0) {
                            Spam spam = players.get(Misc.random(players.size() - 1));
                        } else {
                            player.sendMessage("No Spammers Found.");
                        }
                        return true;
                    }
                },
                new Command("huntspammers") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getSpam().setHunting(true);
                        return true;
                    }
                },
                new Command("banallspammers") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.getPlayers().stream().filter(target -> target != null && target.getSpam().isSpamming()).forEach(target -> {
                            target.getSpam().punish();
                        });
                        return true;
                    }
                },
                new Command("howmanytrivia") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("There are currently %,d people playing trivia.", TriviaBot.getPlayersAmount());
                        return true;
                    }
                },
                new Command("setwatched", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String value = input[0].trim();
                        Player target = World.getPlayerByName(value);
                        AccountLogger.getDupers().put(value, new Object());
                        target.getLogging().setWatched(true);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AccountLogger.DUPERS_FILE, true))) {
                            writer.write(value);
                            writer.newLine();
                            writer.flush();
                            writer.close();
                        } catch (IOException ex) {
                            Server.getLogger().log(Level.WARNING, "Error Writing to DUPERS_FILE", ex);
                        }
                        player.sendf("Now watching '%s'.", TextUtils.optimizeText(target.getName()));
                        return true;
                    }
                },
                new Command("sendhome", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (Rank.hasAbility(target, Rank.ADMINISTRATOR)
                                || Rank.isStaffMember(target)) {
                            player.sendf("Cannot send this player home.");
                            return true;
                        }
                        Magic.teleport(target, Edgeville.POSITION, false);
                        player.sendf("You have sent '%s' home.", TextUtils.optimizeText(target.getName()));
                        target.sendf("'%s' has sent you home.", TextUtils.optimizeText(player.getName()));
                        return true;
                    }
                },
                new Command("viewbank", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getChecking().getBankListener() != null) {
                            player.getChecking().getBank().removeListener(player.getChecking().getBankListener());
                            player.getChecking().setBank(null);
                            player.getChecking().setBankListener(null);
                        }
                        final Player target = World.getPlayerByName(input[0].trim());
                        player.getChecking().setBankListener(new InterfaceContainerListener(player, 5382));
                        player.getChecking().setBank(target.getBank());
                        player.getActionSender().sendInterfaceInventory(5292, 5063);
                        player.getInterfaceState().addListener(player.getChecking().getBank(), player.getChecking().getBankListener());
                        int tab = 0;
                        for (; tab < target.getBankField().getTabAmount(); tab++) {
                            int from = target.getBankField().getOffset(tab);
                            int to = from + target.getBankField().getTabAmounts()[tab];
                            Item[] items = Arrays.copyOf(Arrays.copyOfRange(target.getBank().toArray(), from, to), Bank.SIZE);
                            player.getActionSender().sendUpdateItems(Bank.BANK_INVENTORY_INTERFACE + tab, items);
                        }
                        for (; tab < 9; tab++) {
                            player.getActionSender().sendUpdateItems(Bank.BANK_INVENTORY_INTERFACE + tab, new Item[Bank.SIZE]);
                        }
                        return true;
                    }
                },
                new Command("viewinv", new CommandInput<Object>(World::playerIsOnline, "Player", "An online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getChecking().getInvListener() != null) {
                            player.getChecking().getInv().removeListener(player.getChecking().getInvListener());
                            player.getChecking().setInv(null);
                            player.getChecking().setInvListener(null);
                        }
                        final Player target = World.getPlayerByName(input[0].trim());
                        player.getChecking().setInvListener(new InterfaceContainerListener(player, 5064));
                        player.getChecking().setInv(target.getInventory());
                        player.getActionSender().sendInterfaceInventory(5292, 5063);
                        player.getInterfaceState().addListener(player.getChecking().getInv(), player.getChecking().getInvListener());
                        player.getActionSender().sendUpdateItems(5064, player.getChecking().getInv().toArray());
                        return true;
                    }
                },
                new Command("tracepkp") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        TreeMap<Long, Player> map = new TreeMap<>();
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> {
                            map.put((long) target.getInventory().getCount(5020) + target.getBank().getCount(5020) + target.getPoints().getPkPoints() / 10, target);
                        });
                        map.descendingKeySet().stream().forEach(value -> player.sendf("%s - %s", map.get(value).getName(), value));
                        return true;
                    }
                },
                new Command("richest") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        TreeMap<Integer, Player> map = new TreeMap<Integer, Player>();
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> map.put(target.getAccountValue().getTotalValue(), target));
                        map.descendingKeySet().stream().forEach(value -> player.sendf("%s - %s", map.get(value).getName(), value));
                        return true;
                    }
                },
                new Command("resetviewed") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getChecking().getBank() != null) {
                            player.getChecking().getBank().removeListener(player.getChecking().getBankListener());
                            player.getChecking().setBank(null);
                            player.getChecking().setBankListener(null);
                        }
                        if (player.getChecking().getInv() != null) {
                            player.getChecking().getInv().removeListener(player.getChecking().getInvListener());
                            player.getChecking().setInv(null);
                            player.getChecking().setInvListener(null);
                        }
                        return true;
                    }
                },
                new Command("accvalue", new CommandInput<Object>(World::playerIsOnline, "Player", "An online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        player.sendf("[%s]: %,d", TextUtils.optimizeText(target.getName()), target.getAccountValue().getTotalValue());
                        return true;
                    }
                },
                new Command("resetks", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.setKillStreak(0);
                        player.sendf("%s KS set to 0.", TextUtils.optimizeText(target.getName()));
                        return true;
                    }
                },
                new Command("staff", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if ((player.getPosition().getX() >= 2934 && player.getPosition().getY() <= 3392
                                && player.getPosition().getX() < 3061 && player.getPosition().getY() >= 3326)
                                || Rank.hasAbility(player, Rank.ADMINISTRATOR)
                                || player.getName().equalsIgnoreCase("charmed")) {
                            final Player target = World.getPlayerByName(input[0].trim());
                            target.setTeleportTarget(Position.create(3165, 9635, 0));
                        }
                        return true;
                    }
                },
                new Command("bob") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        BoB.openInventory(player);
                        return true;
                    }
                },
                new Command("giles") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.getNpcs().stream().filter(npc -> npc.getDefinition().getId() == 2538).forEach(npc -> {
                            player.sendf("[Giles]: %d, %d, %d, %sDead, %sServerKilled, %sTeleporting", npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), npc.isDead() ? "=" : "!", npc.serverKilled ? "=" : "!", npc.isTeleporting() ? "=" : "!");
                            npc.vacateSquare();
                        });
                        return true;
                    }
                },
                new Command("resetslayertask", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.getSlayer().setPoints(target.getSlayer().getSlayerPoints() + 20);
                        target.getSlayer().resetTask();
                        player.sendf("You have succesfully reset '%s'; Their slayer task.", TextUtils.optimizeText(target.getName()));
                        player.sendf("Your slayer task has been reset by '%s'.", TextUtils.optimizeText(player.getName()));
                        return true;
                    }
                },
                new Command("players2") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final List<String> list = new ArrayList<>();
                        World.getPlayers().forEach(value -> list.add(String.format("%s:%d,%d,%d,%d,%d", TextUtils.titleCase(value.getName()), Rank.getPrimaryRank(value).ordinal(), value.getSkills().getCombatLevel(), value.getPosition().getX(), value.getPosition().getY(), value.getPosition().getZ())));
                        Collections.sort(list, (one, two) -> one.split(":")[0].compareTo(two.split(":")[0]));
                        list.forEach(player::sendMessage);
                        return true;
                    }
                },
                new Command("players3") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().openPlayersInterface();
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
