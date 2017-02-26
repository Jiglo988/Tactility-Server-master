package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.Configuration;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.Lock;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.util.PushMessage;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Time;

/**
 * Created by DrHales on 3/3/2016.
 */
public class YellCommand extends NewCommand {

    public static final int NORMAL_YELL_DELAY = 120000;//240000;
    public static final int DONATOR_YELL_DELAY = 60000;//180000;
    public static final int SUPER_YELL_DELAY = 30000;//120000;

    public static int minYellRank = 0;

    public YellCommand() {
        super("yell", Rank.PLAYER, new CommandInput<>(object -> true, "String", "Input Message"));
    }

    private int getYellDelay(Player player) {
        if (player.getPermExtraData().getLong("loweredYellTimer") >= System.currentTimeMillis() && player.getPermExtraData().getLong("loweredYellTimer") != 0) {
            double yellReducement = 1.0;
            if (player.getPermExtraData().get("yellReduction") != null) {
                try {
                    yellReducement = Double.parseDouble((String) player.getPermExtraData().get("yellReduction"));
                } catch (Exception e) {
                    yellReducement = (double) player.getPermExtraData().get("yellReduction");
                }
            }
            if (Rank.hasAbility(player, Rank.SUPER_DONATOR))
                return (int) (SUPER_YELL_DELAY * yellReducement);
            else if (Rank.hasAbility(player, Rank.DONATOR))
                return (int) (DONATOR_YELL_DELAY * yellReducement);
            return (int) (NORMAL_YELL_DELAY * yellReducement);
        } else if (player.getPermExtraData().getLong("loweredYellTimer") < System.currentTimeMillis() && player.getPermExtraData().getLong("loweredYellTimer") != 0) {
            player.getPermExtraData().remove("loweredYellTimer");
            player.getPermExtraData().remove("yellReduction");
        }
        if (Rank.hasAbility(player, Rank.SUPER_DONATOR))
            return SUPER_YELL_DELAY;
        else if (Rank.hasAbility(player, Rank.DONATOR))
            return DONATOR_YELL_DELAY;
        return NORMAL_YELL_DELAY;
    }

    public boolean execute(final Player player, final String[] input) {
        if (Rank.getPrimaryRank(player).ordinal() < minYellRank) {
            player.sendMessage("An administrator has set the minimum yell rank higher temporarily");
            return true;
        }
        if (player.isMuted || player.yellMuted) {
            player.sendMessage("Muted players cannot use the yell command.");
            return true;
        }
        if (!player.getPermExtraData().getBoolean("yellAccepted")) {
            player.getActionSender().yellRules();
            return false;
        }
        String message = input[0].trim();
        if (!Rank.hasAbility(player, Rank.MODERATOR) && message.trim().contains("@")) {
            return true;
        }
        message = PushMessage.filteredString(message);
        message = message.replace("tradereq", "").replace("duelreq", "").replace(":clan:", "").replace("@", "");
        long yellMilliseconds = System.currentTimeMillis() - player.getPermExtraData().getLong("yelltimur");
        long yellDelay = getYellDelay(player);
        if (!Rank.isStaffMember(player) && !Configuration.getString(Configuration.ConfigurationObject.NAME).equalsIgnoreCase("ArteroBeta")) {
            if ((player.getSkills().getTotalLevel() >= 1800 || player.getPoints().getEloPeak() >= 1800) || Rank.hasAbility(player, Rank.SUPER_DONATOR) || Rank.hasAbility(player, Rank.DONATOR)) {
                if (yellMilliseconds < getYellDelay(player)) {
                    player.sendMessage("Please wait " + (int) ((yellDelay - yellMilliseconds) / 1000) + " seconds before yelling.");
                    if (player.getClanName().equalsIgnoreCase(""))
                        ClanManager.joinClanChat(player, "chatting", false);
                    return true;
                }
                player.getYelling().updateYellTimer();
                player.getPermExtraData().put("yelltimur", player.getYelling().getYellTimer());
            } else {
                player.sendMessage("You need at least 1,800 PvP Rating peak, 1800 total level or purchase donator", "to start yelling");
                if (player.getClanName().equalsIgnoreCase(""))
                    ClanManager.joinClanChat(player, "chatting", false);
                return true;
            }
        }

        final String colors = Rank.getPrimaryRank(player).getYellColor();
        final String tag = getTag(player);
        final String suffix = (player.hardMode() ? "[I]" : "") + "[" + colors + tag + "@bla@] " + player.getSafeDisplayName() + "@bla@: " + (Rank.getPrimaryRank(player) == Rank.OWNER ? colors : "@bla@");
        final String suffixWithoutTitles = (player.hardMode() ? "[I]" : "") + "[" + colors + Rank.getPrimaryRank(player).toString() + "@bla@] " + player.getSafeDisplayName() + "@bla@: " + (Rank.getPrimaryRank(player) == Rank.OWNER ? colors : "@bla@");
        if (!Rank.isStaffMember(player) && !Configuration.getString(Configuration.ConfigurationObject.NAME).equalsIgnoreCase("ArteroBeta")) {
            World.submit(
                    new Task(yellDelay, "yell reminder") {
                        public void execute() {
                            player.sendMessage("[B] Nab: Hey " + player.getSafeDisplayName() + ", you can yell again!");
                            stop();
                        }
                    }
            );
        }
        final String finalMessage = message.length() > 1 ? Character.toString(message.charAt(0)).toUpperCase() + message.substring(1).toLowerCase() : message.toUpperCase();
        World.getPlayers().stream().filter(target -> target != null).forEach(target -> {
            if (!Lock.isEnabled(target, Lock.YELL)) {
                final String value = Lock.isEnabled(target, Lock.YELL_TITLES) ? suffixWithoutTitles + finalMessage : suffix + finalMessage;
                target.sendMessage(value);
            }
        });
        return true;
    }

    public String getTag(Player player) {
        if (player.getName().equalsIgnoreCase("nab")) {
            return "B";
        }
        if (player.getPoints().getDonatorPointsBought() < 25000 || Rank.isStaffMember(player) || player.getYelling().getTag().equals("")) {
            return Rank.getPrimaryRank(player).toString();
        }
        return player.getYelling().getTag();
    }

}
