package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.commands.impl.cmd.ClanCommand;
import org.hyperion.rs2.commands.impl.cmd.FindListCommand;
import org.hyperion.rs2.commands.impl.cmd.SkillSetCommand;
import org.hyperion.rs2.commands.impl.cmd.WikiCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.challenge.Challenge;
import org.hyperion.rs2.model.challenge.ChallengeManager;
import org.hyperion.rs2.model.color.Color;
import org.hyperion.rs2.model.combat.CombatAssistant;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.bounty.place.BountyHandler;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.minigame.Bork;
import org.hyperion.rs2.model.content.minigame.LastManStanding;
import org.hyperion.rs2.model.content.misc.*;
import org.hyperion.rs2.model.content.misc2.NewGameMode;
import org.hyperion.rs2.model.content.skill.dungoneering.DungeoneeringManager;
import org.hyperion.rs2.model.customtrivia.CustomTriviaManager;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.model.itf.impl.ChangePassword;
import org.hyperion.rs2.model.itf.impl.PlayerProfileInterface;
import org.hyperion.rs2.model.punishment.Combination;
import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.rs2.model.punishment.Target;
import org.hyperion.rs2.model.punishment.Type;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.util.PlayerFiles;
import org.hyperion.rs2.util.PushMessage;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
//</editor-fold>

/**
 * Created by DrHales on 2/29/2016.
 */
public class PlayerCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.PLAYER, delay, requiredInput);
        }

        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.PLAYER, requiredInput);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new FindListCommand("findcommand", Rank.PLAYER, Time.FIFTEEN_SECONDS, FindListCommand.ListType.COMMAND),
                new FindListCommand("nameitem", Rank.PLAYER, Time.FIVE_SECONDS, FindListCommand.ListType.ITEM),
                new ClanCommand("cc"),
                new ClanCommand("clan"),
                new Command("yaks", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Magic.teleport(player, 3051, 3515, 0, false);
                        ClanManager.joinClanChat(player, "Risk Fights", false);
                        return true;
                    }
                },
                new Command("buyshards", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendMessage("Spirit shard packs are available inside the emblem pt store");
                        return true;
                    }
                },
                new Command("changecompcolors", Time.FIFTEEN_SECONDS, new CommandInput<String>(string -> string != null, "Primary Color", "A Color Name"), new CommandInput<String>(string -> string != null, "Secondary Color", "A Color Name")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String primary_color = input[0].trim();
                        final String secondary_color = input[1].trim();
                        Color primary = null;
                        Color secondary = null;
                        for (final Color color : Color.values()) {
                            if (primary != null && secondary != null) {
                                break;
                            }
                            final String current = color.toString();
                            if (primary_color.equalsIgnoreCase(current)) {
                                primary = color;
                            }
                            if (secondary_color.equalsIgnoreCase(current)) {
                                secondary = color;
                            }
                        }
                        if (primary == null || secondary == null) {
                            player.sendf("%s is not a valid color.", primary == null ? primary_color : secondary_color);
                            return true;
                        }
                        if (!Rank.hasAbility(player, Rank.ADMINISTRATOR) && primary == Color.WHITE && primary == secondary) {
                            player.sendMessage("Ferry bitch slapped you from making both colors white");
                            return true;
                        }
                        player.compCapePrimaryColor = primary.color;
                        player.compCapeSecondaryColor = secondary.color;
                        player.getUpdateFlags().set(UpdateFlags.UpdateFlag.APPEARANCE, true);
                        player.sendf("Changed Completionist Cape colors: Primary -> %s | Secondary -> %s", primary, secondary);
                        return true;
                    }
                },
                new Command("viewprofile", Time.FIFTEEN_SECONDS, new CommandInput<Object>(PlayerLoading::playerExists, "Player", "An Existing Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String name = input[0].trim();
                        if (!InterfaceManager.<PlayerProfileInterface>get(PlayerProfileInterface.ID).view(player, name)) {
                            player.sendf("Error loading '%s' profile.", name);
                        }
                        return true;
                    }
                },
                new Command("buyrocktails", Time.FIFTEEN_SECONDS, new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Amount", String.format("An Amount between 0 & %s", Integer.MAX_VALUE))) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int amount = Math.min(Integer.parseInt(input[0].trim()), player.getPoints().getPkPoints());
                        if (player.getPoints().getPkPoints() < amount) {
                            amount = player.getPoints().getPkPoints();
                        }
                        player.getPoints().setPkPoints(player.getPoints().getPkPoints() - amount);
                        player.getBank().add(new BankItem(0, 15272, amount));
                        player.sendf("%d rocktails have been added to your bank.", amount);
                        return true;
                    }
                },
                new Command("thread", Time.THIRTY_SECONDS, new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Thread Number", "A Thread Number")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendWebpage(String.format("http://forums.tactilitypk.com/index.php?showtopic=%d", Integer.parseInt(input[0].trim())));
                        return true;
                    }
                },
                new Command("acceptyellrules", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        DialogueManager.openDialogue(player, 198);
                        return true;
                    }
                },
                new Command("forums", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendWebpage("https://tactilitypk.boards.net");
                        return true;
                    }
                },
                new Command("moneymaking", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendWebpage("http://forums.tactilitypk.com/topic/23523-money-making-guide/");
                        return true;
                    }
                },
                new Command("rules", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendWebpage("http://forums.tactilitypk.com/forum/28-in-game-rules/");
                        return true;
                    }
                },
                new Command("skullmyself", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.setSkulled(true);
                        return true;
                    }
                },
                new Command("support", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendMessage("l4unchur13 http://support.tactilitypk.com/helpdesk/");
                        return true;
                    }
                },
                new Command("dicing", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Magic.teleport(player, Position.create(3048, 4979, 1), false);
                        ClanManager.joinClanChat(player, "dicing", false);
                        return true;
                    }
                },
                new Command("tutorial", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getTutorialProgress() == 0) {
                            player.setTutorialProgress(1);
                        }
                        Tutorial.getProgress(player);
                        return true;
                    }
                },
                new Command("ks", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("You are on a '@red@%,d@bla@' killstreak!", player.getKillStreak());
                        return true;
                    }
                },
                new Command("toggleprofile", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getPermExtraData().put("disableprofile", !player.getPermExtraData().getBoolean("disableprofile"));
                        player.sendf("your public profile is currently %s@bla@.", player.getPermExtraData().getBoolean("disableprofile") ? "@red@unviewable" : "@gre@viewable");
                        return true;
                    }
                },
                new Command("top10", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        LastManStanding.loadTopTenInterface(player);
                        return true;
                    }
                },
                new Command("combine", 500L) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!Position.inAttackableArea(player)) {
                            PotionDecanting.decantPotions(player);
                        }
                        return true;
                    }
                },
                new Command("wiki", Time.THIRTY_SECONDS, new CommandInput<String>(string -> string != null, "String", "Wiki Shortcut")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String shortcut = input[0].trim();
                        if (!WikiCommand.KEY_TO_URL.containsKey(shortcut)) {
                            player.sendf("No such link '%s'.", shortcut);
                            return true;
                        }
                        player.sendf("l4unchur13 http://www.tactilitypk.wikia.com/wiki/%s", TextUtils.titleCase(WikiCommand.KEY_TO_URL.get(shortcut)).replace(" ", "%20"));
                        return true;
                    }
                },
                new Command("graves", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        DialogueManager.openDialogue(player, 194);
                        return true;
                    }
                },
                new Command("wests", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        DialogueManager.openDialogue(player, 196);
                        return true;
                    }
                },
                new Command("answertrivia", Time.FIVE_SECONDS, new CommandInput<>(object -> true, "String", "Custom Trivia Answer")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        CustomTriviaManager.processAnswer(player, input[0].trim());
                        return true;
                    }
                },
                new Command("viewtrivia", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        CustomTriviaManager.send(player);
                        return true;
                    }
                },
                new Command("vote", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().sendWebpage(String.format("http://vote.tactilitypk.com/index.php?toplist_id=0&username=%s", player.getName()));
                        return true;
                    }
                },
                new Command("viewchallenges", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        ChallengeManager.send(player, false);
                        return true;
                    }
                },
                new Command("changepass") {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getInterfaceManager().show(ChangePassword.ID);
                        /*if (player.getPassword().equalsIgnoreCase(EncryptionStandard.encryptPassword(input[0]))) {
                            player.sendMessage("Don't use the same password again!");
                            return true;
                        }
                        TextUtils.writeToFile("./data/possiblehacks.txt", String.format("Player: %s Old password: %s New password: %s By IP: %s Date: %s", player.getName(), player.getPassword(), input[0], player.getShortIP(), new Date().toString()));
                        player.setPassword(EncryptionStandard.encryptPassword(input[0].toLowerCase()));
                        player.sendImportantMessage("Your password is now " + input[0].toLowerCase());
                        player.getPermExtraData().put("passchange", System.currentTimeMillis());
                        player.getExtraData().put("needpasschange", false);*/
                        return true;
                    }
                },
                new Command("prayers", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        StringBuilder builder = new StringBuilder();
                        for (int array = 0; array < Prayers.SIZE; array++) {
                            if (player.getPrayers().isEnabled(array)) {
                                builder.append(String.format("%d,", array));
                            }
                        }
                        player.sendf("[Active Prayers]: %s", builder.toString());
                        return true;
                    }
                },
                new Command("onlinestaff", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        List<Player> online = StaffManager.getOnlineStaff();
                        online.stream().forEach(other -> {
                            final Rank rank = Rank.getPrimaryRank(other);
                            player.sendf("[%s%s@bla@] - %s%s",
                                    rank.getYellColor(), other.display == null || other.display.isEmpty() ? other.getName() : other.display,
                                    rank.getYellColor(), rank);
                        });
                        return true;
                    }
                },
                new Command("clearwalkinterface", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().showInterfaceWalkable(-1);
                        return true;
                    }
                },
                new Command("bork", Time.FIVE_MINUTES) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        long delay;
                        if ((delay = System.currentTimeMillis() - player.getPermExtraData().getLong(Bork.getTimeKey())) < Bork.getDelay()) {
                            player.sendf("You must wait %d more minutes to kill Bork", TimeUnit.MINUTES.convert(Bork.getDelay() - delay, TimeUnit.MILLISECONDS));
                            return true;
                        } else if (player.getTotalOnlineTime() < Time.ONE_HOUR * 6) {
                            player.sendf("You need at least 6 hours of online time to attempt Bork");
                            return true;
                        }
                        if (!ItemSpawning.canSpawn(player)) {
                            player.sendMessage("You can't start bork here");
                            return false;

                        }
                        final int height = player.getIndex() * 4;
                        Magic.teleport(player, Bork.getTeleportPosition().transform(0, 0, height), false);
                        World.submit(new Bork.BorkEvent(player));
                        return true;
                    }
                },
                new Command("testcolors", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Arrays.asList(Yelling.COLOUR_SUFFICES).stream().forEach(string -> {
                            player.sendf("@%s@[Owner][Arre]:Testing Message: %s", string, string);
                        });
                        return true;
                    }
                },
                new Command("guessnumber", Time.TEN_SECONDS, new CommandInput<Integer>(integer -> integer > Lottery.MIN_GUESS && integer < Lottery.MAX_GUESS, "Integer", String.format("An amount between %,d & %,d", Lottery.MIN_GUESS, Lottery.MAX_GUESS))) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int value = Integer.parseInt(input[0].trim());
                        Lottery.checkGuess(player, value);
                        return true;
                    }
                },
                new Command("lotteryinfo", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().openLotteryInformation();
                        return true;
                    }
                },
                new Command("mymail", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[Mail]: %d", player.getMail());
                        return true;
                    }
                },
                new Command("setmail", Time.ONE_MINUTE, new CommandInput<String>(string -> string != null, "String", "e-Mail")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getMail().setTempmail(input[0].trim());
                        return true;
                    }
                },
                new Command("answer", Time.FIVE_SECONDS, new CommandInput<String>(string -> string != null, "String", "Trivia Answer")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        TriviaBot.sayAnswer(player, input[0].trim());
                        return true;
                    }
                },
                new Command("getprice", Time.TEN_SECONDS, new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Integer", "Item ID")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int id = Integer.parseInt(input[0].trim());
                        player.sendf("Price of %s costs %,d coins, it sells for %,d coins.", ItemDefinition.forId(id).getName(), (int) NewGameMode.getUnitPrice(id), (int) (NewGameMode.getUnitPrice(id) * NewGameMode.SELL_REDUCTION));
                        player.sendMessage("Incorrect? Please contact an admin.");
                        return true;
                    }
                },
                new Command("sellitem", Time.FIVE_SECONDS, new CommandInput<Integer>(integer -> ItemDefinition.forId(integer) != null, "Integer", "Item ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < 1000, "Integer", "An amount between 0 & 1,000")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int id = Integer.parseInt(input[0].trim());
                        if (!ItemSpawning.canSpawn(id)) {
                            player.sendf("You cannot sell item '%s'.", ItemDefinition.forId(id).getName());
                            return true;
                        }
                        int amount = Integer.parseInt(input[1].trim());
                        int price = (int) ((NewGameMode.getUnitPrice(id)) * NewGameMode.SELL_REDUCTION);
                        int sold = player.getInventory().remove(Item.create(id, amount));
                        long value = price * sold;
                        if (value > Integer.MAX_VALUE) {
                            player.sendMessage("You have sold too much of this item.");
                            return true;
                        }
                        if (sold > 0) {
                            return player.getInventory().add(Item.create(995, price * sold));
                        }
                        return true;
                    }
                },
                new Command("resetparse", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        DungeoneeringManager.setItems(DungeoneeringManager.parse());
                        return true;
                    }
                },
                new Command("reqhelp", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.write(Interface.createStatePacket(Interface.SHOW, 3));
                        return true;
                    }
                },
                new Command("accountvalue", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[Account Value]: %,d", player.getAccountValue().getTotalValue());
                        return true;
                    }
                },
                new Command("commands", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final List<String> COMMANDS = new ArrayList<>();
                        NewCommandHandler.getCommandsList().keySet()
                                .stream()
                                .filter(rank -> Rank.hasAbility(player, rank))
                                .forEach(value -> NewCommandHandler.getCommandsList().get(value)
                                        .forEach(COMMANDS::add));
                        Collections.sort(COMMANDS);
                        player.getActionSender().displayCommands(COMMANDS);
                        return true;
                    }
                },
                new Command("settag", Time.THIRTY_SECONDS, new CommandInput<String>(string -> string != null && !(string.length() > 14) && !(Yelling.isValidTitle(string).length() > 1), "String", "Yell Tag")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getPoints().getDonatorPointsBought() < 25000) {
                            player.sendMessage("You need to donate at least $250 to be able to set your tag.");
                        } else {
                            final String value = input[0].trim();
                            player.getYelling().setYellTitle(TextUtils.ucFirst(value.toLowerCase()));
                            player.sendf("Your yell tag has been set to %s.", player.getYelling().getTag());
                        }
                        return true;
                    }
                },
                new Command("challenge", Time.TEN_SECONDS, new CommandInput<Object>(object -> object != null && (String.valueOf(object) != null || Integer.parseInt(String.valueOf(object)) > Integer.MIN_VALUE && Integer.parseInt(String.valueOf(object)) < Integer.MAX_VALUE), "Object", "Challenge Answer")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String value = input[0].trim();
                        final Challenge challenge = ChallengeManager.getChallenge(value);
                        if (challenge == null) {
                            player.sendf("No challenge found for '@dre@%s@bla@'.", value);
                            return true;
                        }
                        ChallengeManager.remove(challenge);
                        player.getBank().add(challenge.getPrize());
                        player.sendImportantMessage(String.format("%s x%,d has been added to your bank!", challenge.getPrize().getDefinition().getName(), challenge.getPrize().getCount()));
                        World.getPlayers().stream().filter(target -> target != null).forEach(target -> target.sendMessage(String.format("@blu@[Challenge] %s has beaten %s's challenge for %s x%,d!", player.getSafeDisplayName(), challenge.getName(), challenge.getPrize().getDefinition().getName(), challenge.getPrize().getCount())));
                        return true;
                    }
                },
                new Command("maxhit", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[Melee]: %d, [Range]: %d", CombatAssistant.calculateMaxHit(player), CombatAssistant.calculateRangeMaxHit(player));
                        return true;
                    }
                },
                new Command("zombies", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!player.getExtraData().getBoolean("zombietele")) {
                            player.sendMessage("@red@This zone is in deep wilderness and leads into multi combat",
                                    "@blu@Type ::zombies again if you wish to proceed");
                            player.getExtraData().put("zombietele", true);
                        } else {
                            Magic.teleport(player, Position.create(3028, 3851, 0), false, false);
                        }
                        return true;
                    }
                },
                new Command("placebounty", Time.ONE_MINUTE, new CommandInput<Object>(PlayerLoading::playerExists, "Player", "An Existing Player"), new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Integer", "PKP Amount")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String other = input[0].trim();
                        final int amount = Integer.parseInt(input[1].trim());
                        if (player.getPoints().getPkPoints() < amount) {
                            player.sendMessage("You don't have enough PK points to do this.");
                            return true;
                        }
                        if (BountyHandler.add(other, player.getName(), amount)) {
                            player.getPoints().setPkPoints(player.getPoints().getPkPoints() - amount);
                            player.sendf("You have successfully placed a bounty of %d on %s", amount, other);
                        } else {
                            player.sendMessage("Minimum bounty is 500pkp, or player's bounty is greater than yours!");
                        }
                        return true;
                    }
                },
                new Command("checkbounties", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        BountyHandler.listBounties(player);
                        return true;
                    }
                },
                new Command("setlvl", Time.FIVE_SECONDS, new CommandInput<Integer>(integer -> integer > -1 && integer < 7, "Integer", "Skill ID"), new CommandInput<Integer>(integer -> integer > 0 && integer < 100, "Integer", "Level")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!SkillSetCommand.canChangeLevel(player)
                                || player.isInCombat()) {
                            player.sendMessage("You cannot do this at this time.");
                            return true;
                        }
                        final int skill = Integer.parseInt(input[0].trim());
                        final int level = Integer.parseInt(input[1].trim());
                        player.getSkills().setLevel(skill, level);
                        player.getSkills().setExperience(skill, Skills.getXPForLevel(level) + 5);
                        return true;
                    }
                },
                new Command("rest", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.playAnimation(Animation.create(11786));
                        return true;
                    }
                },
                new Command("dismiss", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.SummoningCounter = 0;
                        player.getActionSender().sendMessage("You dismiss your familiar.");
                        return true;
                    }
                },
                new Command("resetmyappearance", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getAppearance().resetAppearance();
                        player.sendMessage("Looks reset.");
                        PlayerFiles.saveGame(player);
                        return true;
                    }
                },
                new Command("switchmode", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getExtraData().getBoolean("switchmode")) {
                            player.setGameMode(0);
                            player.sendMessage("Successfully switched to normal game mode");
                        } else {
                            player.getExtraData().put("switchmode", true);
                            player.sendMessage("Type ::switchmode again to switch to normal game mode");
                        }
                        return true;
                    }
                },
                new Command("13s", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Magic.goTo13s(player);
                        return true;
                    }
                },
                new Command("nextbonus", Time.FIVE_MINUTES) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        int day = (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 4);
                        player.sendMessage("The next 5 bonus skills will be:");
                        for (int array = 0; array < 5; array++) {
                            final int skill = ((day + array) % (Skills.SKILL_COUNT - 8)) + 7;
                            player.sendMessage(skill != 21 ? Misc.getSkillName(skill).trim() : "Random Skill");
                        }
                        return true;
                    }
                },
                new Command("vengrunes", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (ItemSpawning.canSpawn(player, false)
                                && !player.hardMode()) {
                            ContentEntity.addItem(player, 557, 1000);
                            ContentEntity.addItem(player, 560, 1000);
                            ContentEntity.addItem(player, 9075, 1000);
                        }
                        return true;
                    }
                },
                new Command("barragerunes", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (ItemSpawning.canSpawn(player, false)
                                && !player.hardMode()) {
                            ContentEntity.addItem(player, 560, 1000);
                            ContentEntity.addItem(player, 565, 1000);
                            ContentEntity.addItem(player, 555, 1000);
                        }
                        return true;
                    }
                },
                new Command("copy", Time.THIRTY_SECONDS, new CommandInput<>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!ItemSpawning.copyCheck(player) || !player.getLocation().isSpawningAllowed()) {
                            return true;
                        }
                        if (ContentEntity.getTotalAmountOfEquipmentItems(player) > 0) {
                            player.sendMessage("You need to take off your armour before copying!");
                            return true;
                        }
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (Rank.hasAbility(target, Rank.ADMINISTRATOR) && !Rank.hasAbility(player, Rank.OWNER)) {
                            return true;
                        }
                        assert target != null;
                        final List<Item> list = Arrays.asList(target.getEquipment().toArray());
                        if (list.isEmpty()) {
                            return true;
                        }
                        list.stream().filter(value -> value != null && (!ItemSpawning.copyCheck(value, player) || Rank.hasAbility(player, Rank.OWNER))).forEach(value -> player.getEquipment().set(Equipment.getType(value).getSlot(), value));
                        return true;
                    }
                },
                new Command("copyinv", Time.THIRTY_SECONDS, new CommandInput<>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!ItemSpawning.copyCheck(player) || !player.getLocation().isSpawningAllowed()) {
                            return true;
                        }
                        if (ContentEntity.getTotalAmountOfItems(player) > 0) {
                            player.getActionSender().sendMessage("You need to remove items from your inventory!");
                            return true;
                        }
                        final Player target = World.getPlayerByName(input[0].trim());
                        if (Rank.hasAbility(target, Rank.ADMINISTRATOR) && !Rank.hasAbility(player, Rank.OWNER)) {
                            return true;
                        }
                        assert target != null;
                        final List<Item> list = Arrays.asList(target.getInventory().toArray());
                        if (list.isEmpty()) {
                            return true;
                        }
                        list.stream().filter(value -> value != null && (!ItemSpawning.copyCheck(value, player) || Rank.hasAbility(player, Rank.OWNER))).forEach(value -> player.getInventory().add(value));
                        return true;
                    }
                },
                new Command("copylvl", Time.THIRTY_SECONDS, new CommandInput<>(World::playerIsOnline, "Player", "An Online Player")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (ItemSpawning.canSpawn(player, false)
                                && !player.hardMode()
                                && ItemSpawning.copyCheck(player)) {
                            player.resetPrayers();
                            if (ContentEntity.getTotalAmountOfEquipmentItems(player) > 0) {
                                player.sendMessage("You need to take off your armour before copying!");
                            } else {
                                final Player target = World.getPlayerByName(input[0].trim());
                                if (!Rank.hasAbility(target, Rank.ADMINISTRATOR)) {
                                    for (int array = 0; array < 6; array++) {
                                        player.getSkills().setLevel(array, target.getSkills().getRealLevels()[array]);
                                        player.getSkills().setExperience(array, target.getSkills().getXps()[array]);
                                    }
                                }
                            }
                        }
                        return true;
                    }
                },
                new Command("resetrfd", Time.TEN_MINUTES) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.RFDLevel = 0;
                        player.sendMessage("RFD reset.");
                        return true;
                    }
                },
                new Command("findids", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final List<Item> list = Arrays.asList(player.getEquipment().toArray());
                        if (list.isEmpty()) {
                            player.sendMessage("There are no items on your inventory.");
                            return true;
                        }
                        list.stream().filter(item -> item != null).forEach(item -> player.sendf("[Name]: %s, [ID]: %d", item.getDefinition().getName(), item.getDefinition().getId()));
                        return true;
                    }
                },
                new Command("showwildinterface", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.showEP = false;
                        player.getActionSender().sendWildLevel(player.wildernessLevel);
                        player.sendMessage("Now showing wilderness level interface.");
                        return true;
                    }
                },
                new Command("clearfriendslist", Time.TEN_MINUTES) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getFriends().clear();
                        player.sendMessage("Done clearing friends list; Relog.");
                        return true;
                    }
                },
                new Command("wildlvl", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[Wilderness Level]: %d", player.wildernessLevel);
                        return true;
                    }
                },
                new Command("myep", Time.ONE_MINUTE) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[EP Level]: %d", player.EP);
                        return true;
                    }
                },
                new Command("givemetabsplz", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!player.getLocation().isSpawningAllowed()) {
                            return true;
                        }
                        Arrays.asList(8007, 8008, 8009, 8010, 8011, 8012).stream().filter(ItemSpawning::canSpawn).forEach(value -> player.getInventory().add(Item.create(value, 1000)));
                        return true;
                    }
                },
                new Command("myopp", Time.TEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.sendf("[Opponent]: %s", player.cE.getOpponent());
                        return true;
                    }
                },
                new Command("buytickets", Time.FIFTEEN_SECONDS, new CommandInput<Integer>(integer -> integer > 0 && integer < 1000000, "Integer", "Pk Tickets Amount")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!Position.inAttackableArea(player)) {
                            int amount = Integer.parseInt(input[0].trim());
                            if (player.getPoints().getPkPoints() > (amount * 10)) {
                                player.getPoints().setPkPoints(player.getPoints().getPkPoints() - (amount * 10));
                                final Item item = Item.create(5020, amount);
                                if (player.getInventory().hasRoomFor(item)) {
                                    player.getInventory().add(item);
                                    player.sendf("%,d Pk Tickets have been added to your inventory.", item.getCount());
                                    return true;
                                } else {
                                    player.getBank().add(item);
                                    player.sendf("%,d Pk Tickets have been added to your bank.", item.getCount());
                                    return true;
                                }
                            } else {
                                player.sendMessage("You don't have enough Pkp for this.");
                            }
                        }
                        return true;
                    }
                },
                new Command("selltickets", Time.FIFTEEN_SECONDS, new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Integer", "Pk Tickets Amount")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!Position.inAttackableArea(player)) {
                            int amount = Integer.parseInt(input[0].trim());
                            if (player.getPoints().getPkPoints() + (amount * 10) < Integer.MAX_VALUE) {
                                int removed;
                                if ((removed = player.getInventory().remove(new Item(5020, amount))) > 0) {
                                    player.getPoints().increasePkPoints(removed * 10);
                                    player.sendf("You sold %,d Pk Tickets.", removed);
                                }
                            }
                        }
                        return true;
                    }
                },
                new Command("empty", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!player.getPosition().inPvPArea()) {
                            DialogueManager.openDialogue(player, 143);
                        }
                        return true;
                    }
                },
                new Command("players", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final List<Player> list = World.getPlayers().stream().filter(other -> !other.isHidden()).collect(Collectors.toList());
                        player.sendf("There is currently '%,d' player%s playing ArteroPk.", list.size(), list.size() != 1 ? "s" : "");
                        return true;
                    }
                },
                new Command("switchoption", Time.FIVE_SECONDS, new CommandInput<String>(string -> string != null, "String", "Player Option")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String option = input[0].toLowerCase().trim();
                        boolean old = player.getPermExtraData().getBoolean(String.format("%soption", option));
                        switch (option) {
                            case "moderate":
                                player.getActionSender().sendPlayerOption(old ? TextUtils.titleCase(option) : "null", 5, 0);
                                return true;
                            case "trade":
                                player.getActionSender().sendPlayerOption(old ? TextUtils.titleCase(option) : "null", 4, 0);
                                break;
                            case "follow":
                                player.getActionSender().sendPlayerOption(old ? TextUtils.titleCase(option) : "null", 3, 0);
                                break;
                            case "profile":
                                player.getActionSender().sendPlayerOption(old ? TextUtils.titleCase(option) : "null", 6, 0);
                                break;
                            default:
                                return true;
                        }
                        player.getPermExtraData().put(option + "option", !player.getPermExtraData().getBoolean(option + "option"));
                        player.sendf("You have %s your %s option", old ? "enabled" : "disabled", option);
                        return true;
                    }
                },
                new Command("reqticket", Time.ONE_MINUTE, new CommandInput<String>(string -> string != null, "String", "Help Reason")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (System.currentTimeMillis() - player.lastTickReq() < 60000) {
                            player.sendMessage("You need to wait 60 seconds to request another ticket.");
                            return true;
                        }
                        final String reason = input[0].trim();
                        if (Ticket.hasTicket(player)) {
                            Ticket.removeRequest(player);
                        }
                        Ticket.putRequest(player, reason);
                        PushMessage.pushHelpMessage(String.format("%s has just requested help for '%s'.", TextUtils.optimizeText(player.getName()), reason));
                        player.sendMessage("Your ticket was submitted. Remember to use ::help for most questions.");
                        player.refreshTickReq();
                        return true;
                    }
                },
                new Command("npclogs", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getActionSender().openQuestInterface("NPC Logs", player.getNPCLogs().getDisplay());
                        return true;
                    }
                },
                new Command("clearjunk", Time.FIVE_MINUTES, new CommandInput<Integer>(integer -> integer > 0 && integer < Integer.MAX_VALUE, "Integer", "Minimum Item Count")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final int amount = Integer.parseInt(input[0].trim());
                        final List<Item> list = Arrays.asList(player.getBank().toArray());
                        if (list.isEmpty()) {
                            return true;
                        }
                        list.stream().filter(item -> item != null && item.getCount() < amount && ItemSpawning.canSpawn(item.getId())).forEach(item -> player.getBank().remove(item));
                        player.sendMessage("Done cleaning bank.");
                        return true;
                    }
                },
                new Command("listcolors", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final Color[] colors = Color.values();
                        final String[] strings = new String[colors.length];
                        for (int array = 0; array < colors.length; array++) {
                            strings[array] = Character.toString(colors[array].toString().charAt(0)).toUpperCase() + colors[array].toString().substring(1).toLowerCase().trim();
                        }
                        player.getActionSender().openQuestInterface("Color List", strings);
                        return true;
                    }
                },
                new Command("verify", 0, new CommandInput<String>(string -> string != null, "String", "Verification Code")) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        final String value = input[0].trim();
                        if (!player.verificationCode.equals(value)) {
                            if (--player.verificationCodeAttemptsLeft == 0) {
                                Arrays.asList(new Target[]{Target.IP, Target.MAC, Target.SPECIAL}).stream().forEach(target -> {
                                    final Punishment punishment = Punishment.create("Server", player, Combination.of(target, Type.BAN), org.hyperion.rs2.model.punishment.Time.create(1, TimeUnit.DAYS), "Too many failed verification attempts.");
                                    PunishmentManager.getInstance().add(punishment);
                                    punishment.insert();
                                });
                                EntityHandler.deregister(player);
                                return false;
                            } else {
                                player.sendf("You have %,d attempts left to verify", player.verificationCodeAttemptsLeft);
                            }
                            return false;
                        }
                        player.verificationCodeEntered = true;
                        player.sendMessage("Successfully verified.");
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
