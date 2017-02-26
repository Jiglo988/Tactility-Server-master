package org.hyperion.rs2.model.content.clan;

import org.apache.mina.core.buffer.IoBuffer;
import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.rs2.util.TextUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ClanManager {

	public static void joinClanChat(Player player, String clanName, boolean onLogin) {
		clanName = clanName.contains("_") ? clanName.replace("_", " ") : clanName;
		if(! canEnter(player, clanName))
			return;
		Clan clan = clans.get(clanName.toLowerCase());
		if(clan == null) {
			clan = new Clan(player.getName(), clanName);
			clans.put(clanName.toLowerCase(), clan);
		}
		if(clan.isKicked(player.getName())) {
			player.sendClanMessage("You are currently kicked from this clan chat.");
			return;
		}
		leaveChat(player, true, false);
		if(! onLogin) {
			sendLoginMessage(player, clanName);
		}
		if(clan.isFull() && !Rank.hasAbility(player, Rank.ADMINISTRATOR) && ! clanName.equalsIgnoreCase(player.getName())) {
			player.sendClanMessage("This clan chat is full.");
			return;
		}
		checkClanRank(player, clan);
		clan.add(player);
		player.getActionSender().sendClanInfo();
		updateClanInfo(player, clan);

	}

	private static void updateClanInfo(final Player player, final Clan clan) {
        player.getActionSender().sendString(18139, String.format("Talking in: %s", TextUtils.titleCase(clan.getName())));
		player.getActionSender().sendString(18140, String.format("Owner: %s", TextUtils.ucFirst(clan.getOwner())));
        clan.getPlayers().forEach(member -> {
            player.getActionSender().addClanMember(member.getPlayersNameInClan());
            if (member != player) {
                member.getActionSender().addClanMember(player.getPlayersNameInClan());
            }
        });
	}

	private static void checkClanRank(final Player player, final Clan clan) {
        clan.getRankedMembers().stream().filter(member -> member.getName().equalsIgnoreCase(player.getName())).forEach(member -> player.setClanRank(member.getRank()));
        player.setClanRank(Rank.hasAbility(player, Rank.DEVELOPER) ? 7 : Rank.hasAbility(player, Rank.MODERATOR) ? 6 : player.getName().equalsIgnoreCase(clan.getOwner()) ? 5 : player.getClanRank());
	}

	private static void sendLoginMessage(final Player player, final String value) {
        player.sendf("Talking in clan '%s'.", TextUtils.titleCase(value));
    }


    public static boolean existsClan(final String value) {
        return clans.get(value.toLowerCase()) != null;
    }

	public static void joinClanChat(final Player player, final long name) {
		String clanName;
		try {
			clanName = NameUtils.longToName(name);
		} catch(Exception e) {
			return;
		}
		joinClanChat(player, clanName, false);
	}

	public static boolean canEnter(final Player player, final String nameStr) {
		if(player.getClanName().equalsIgnoreCase(nameStr)) {
			player.sendClanMessage("You are already in this clan chat.");
            return false;
		}
		if((!FightPits.teamBlue.contains(player) && nameStr.equalsIgnoreCase("Team Blue")) || 
				(!FightPits.teamRed.contains(player) && nameStr.equalsIgnoreCase("Team Red")))
			return false;
		if(nameStr.equalsIgnoreCase("staff")
				&& !Rank.hasAbility(player, Rank.HELPER)) {
			player.sendClanMessage("Only staff can join this clan chat.");
			return false;
		}
		return true;
	}

	public static void leaveChat(Player player, boolean resetClanName, boolean keepRank) {
		Clan c = clans.get(player.getClanName());
		if(c != null) {
			c.remove(player);
			player.getActionSender().sendMessage("You left your current clan chat.");
			for(Player p : c.getPlayers()) {
				p.getActionSender().removeClanMember(player.getPlayersNameInClan());
			}
		}
		clearClanChat(player);
		if(!keepRank)
			player.setClanRank(0);
		if(resetClanName)
			player.resetClanName();
	}

	/*public static void leaveChat(Player player, boolean resetClanName, boolean keepRank) {
		Clan c = clans.get(player.getClanName());
		if(c != null) {
			for(Player p : c.getPlayers()) {
		        p.getActionSender().removeClanMember(player.getPlayersNameInClan());
			}
			player.setClanRank(0);
			c.remove(player);
			player.sendClanMessage("You leave your current clan chat.");

		}
        player.getActionSender().sendString(18139, "Talking in: -");
        player.getActionSender().sendString(18140, "Owner: None");
		if(! keepRank)
			player.setClanRank(0);
		if(resetClanName)
			player.resetClanName();
	}*/

    public static void clearClanChat(Player player) {
        player.getActionSender().sendString(18139, "Talking in: Not in clan");
        player.getActionSender().sendString(18140, "Owner: None");

        for (int i = 18144; i <= 18444; i ++) {
            player.getActionSender().sendString(i, "");
        }
    }

	public static void sendClanMessage(final Player player, final String message, final boolean self) {
        if(handleInternalCommands(message.toLowerCase().trim(), player)) {
            return;
        }
        if (player.getClanName().trim().isEmpty()) {
            player.sendClanMessage("You need to join a clan chat before you can send messages.");
            return;
        }
		final Clan clan = clans.get(player.getClanName());
		if (clan != null) {
            final String value = message.contains("req:") ? message.replace("req:", "req") : String.format("%s%s:%s:clan:", player.getClanRankName().isEmpty() ? " " : player.isClanMainOwner() ? "[O] " : String.format("[%d] ", player.getClanRank()), player.getSafeDisplayName(), TextUtils.ucFirst(message.contains("@") ? message.replace("@", "") : message));
            clan.getPlayers().stream().forEach(member -> member.sendMessage(value));
        }
	}

	public static void sendDiceMessage(Player player, Clan clan, int thrown) {
		player.getActionSender().sendMessage(String.format("You roll a @red@%d@bla@ on the percentile dice.", thrown));
        final String message = String.format("Clan chat mate @369@%s@bla@ rolled @red@%d@bla@ on the percentile dice.", TextUtils.titleCase(player.getName()), thrown);
        clan.getPlayers().stream().filter(client -> !client.getName().equals(player.getName())).forEach(client -> client.sendMessage(message));
        player.forceMessage(String.format("ROLLED: %d!", thrown));
	}

    public static boolean handleInternalCommands(final String message, Player player) {
        if(message.equalsIgnoreCase("makediceclan") && Rank.hasAbility(player, Rank.OWNER)) {
            Clan clan = clans.get(player.getClanName());
            clan.makeDiceClan();
            player.sendf("Dice clan: %s", clan.isDiceClan());
            return true;
        }
        if(message.startsWith("demote"))  {
            String name = message.replace("demote ", "");
            player.getActionSender().sendMessage("Demoting " + name);
            Clan clan = ClanManager.clans.get(player.getClanName());
            if(! clan.getOwner().equalsIgnoreCase(player.getName())) {
                player.sendClanMessage("Only the main owner can demote people.");
                return true;
            }
            Player p = World.getPlayerByName(name);
            if(p == null) {
                player.getActionSender().sendMessage("This player is offline");
                return true;
            }
            if(! player.getClanName().equals(p.getClanName())) {
                player.sendClanMessage("This player is not in your clan chat");
                return true;
            }
            String clanName = p.getClanName();
            final int old = p.getClanRank();
            ClanManager.leaveChat(p, true, true);
            if(old > 0) {
                clan.addRankedMember(new ClanMember(p.getName(), 0));
                sendClanMessage(player, "@bla@ "+name+ " has been demoted", true);
            } else {
                player.getActionSender().sendMessage("This player already has the lowest");
                ClanManager.joinClanChat(p, clanName, false);
                return true;
            }
            ClanManager.joinClanChat(p, clanName, false);
            player.sendClanMessage("Player has been succesfully demoted.");
            return true;
        }

        if(message.startsWith("promote"))  {
            String name = message.replace("promote ", "");
            player.getActionSender().sendMessage("Promoting " + name);
            Clan clan = ClanManager.clans.get(player.getClanName());
            if(!player.isClanMainOwner() && player.getClanRank() != 7) {
                player.sendClanMessage("Only clan chat owners are able to give ranks.");
                return true;
            }
            Player p = World.getPlayerByName(name);
            if(p == null) {
                player.sendClanMessage("This player is offline");
                return true;
            }
            if(! player.getClanName().equals(p.getClanName())) {
                player.sendClanMessage("This player is not in your clan chat");
                return true;
            }
            String clanName = p.getClanName();
            final int old = p.getClanRank();
            if(old < 5) {
                if(Dicing.diceClans.contains(clanName) && old >= 3) {
                    player.sendClanMessage("This player has the maximum rank for a dice clan");
                    return true;
                }
                ClanManager.leaveChat(p, true, true);
                p.setClanRank(old + 1);
                clan.addRankedMember(new ClanMember(p.getName(), p.getClanRank()));
                sendClanMessage(player, String.format("%s has been promoted to %s", TextUtils.titleCase(name), p.getClanRankName()), true);
            } else {
                player.sendClanMessage("This player already has the highest rank possible");
                return true;
            }
            ClanManager.joinClanChat(p, clanName, false);
            player.sendClanMessage("Player has been succesfully promoted.");
            return true;
        }
        if (message.startsWith("kick")) {
            final Clan clan = ClanManager.clans.get(player.getClanName());
            final String name = message.replace("kick ", "");
            final Player other = World.getPlayerByName(name);
            if (player.getClanRank() < 4) {
                player.sendClanMessage("You are not a high enough rank to kick members.");
                return true;
            }
            if(other != null && other.getClanRank() >= player.getClanRank()) {
                player.sendClanMessage("You cannot do this with someone of a higher or equal rank");
                return true;
            }
            if(clan.kick(name, false)) {
                sendClanMessage(player, String.format("%s has kicked %s from the channel.", TextUtils.optimizeText(name), TextUtils.optimizeText(player.getName())), false);
            }
            return true;
        }
        if(message.startsWith("ban")) {
            String name = message.replace("ban ", "");
            Clan clan = ClanManager.clans.get(player.getClanName());
            final Player other = World.getPlayerByName(name);
            if(player.getClanRank() < 4) {
                player.sendClanMessage("You are not a high enough rank to ban members");
                return true;
            }
            if(other != null && other.getClanRank() >= player.getClanRank()) {
                player.sendClanMessage("You cannot do this with someone of a higher or equal rank");
                return true;
            }
            if(clan.kick(name, false)) {
                sendClanMessage(player, String.format("%s has kicked %s from the channel.", TextUtils.optimizeText(name), TextUtils.optimizeText(player.getName())), false);
            }
            return true;
        }

        if(message.startsWith("ipban")) {
            String name = message.replace("ipban ", "");
            Clan clan = ClanManager.clans.get(player.getClanName());
            final Player other = World.getPlayerByName(name);
            if(player.getClanRank() < 4) {
                player.sendClanMessage("You are not a high enough rank to ipban members");
                return true;
            }
            if(other != null && other.getClanRank() > player.getClanRank()) {
                player.sendClanMessage("You cannot do this with someone of a higher rank");
                return true;
            }
            if(clan.kick(name, true)) {
                player.sendClanMessage("Player has been kicked succesfully");
                sendClanMessage(player, "@bla@ "+name+ " has been IP-BANNED from the channel", true);
            }
            return true;
        }



        if(message.startsWith("unban")) {
            final String name = message.replace("unban ", "");
            final Clan clan = ClanManager.clans.get(player.getClanName());
            if(player.getClanRank() < 3) {
                player.sendClanMessage("You need to be a higher rank to unban");
                return true;
            }
            if(clan.unban(name)) {
                sendClanMessage(player, "@bla@ "+name+ " has been unbanned from the channel", true);
            }
            return true;
        }

        if(message.startsWith("listbans") && Rank.isStaffMember(player)) {
            final Clan clan = ClanManager.clans.get(player.getClanName());
            clan.listBans(player);
            return true;
        }
        return false;
    }

	public static Map<String, Clan> clans = new HashMap<>();

    private static final byte KEY = (byte)245;

    public static void save() {
        try {
            OutputStream os = new FileOutputStream("data/clanData.bin");
            IoBuffer buf = IoBuffer.allocate(1024);
            buf.setAutoExpand(true);            for(final Clan clan : clans.values())
                if(!clan.getName().toLowerCase().startsWith("party "))
                    clan.save(buf);
            buf.flip();
            byte[] data = new byte[buf.limit()];
            buf.get(data);
            os.write(data);
            os.flush();
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {

        try {
            File f = new File("./data/clanData.bin");
            InputStream is = new FileInputStream(f);
            IoBuffer buf = IoBuffer.allocate(1024);
            buf.setAutoExpand(true);
            while(true) {
                byte[] temp = new byte[1024];
                int read = is.read(temp, 0, temp.length);
                if(read == - 1) {
                    break;
                } else {
                    buf.put(temp, 0, read);
                }
            }
            buf.flip();
            while(buf.hasRemaining()) {
                try {
                    final Clan clan = Clan.read(buf);
                    clans.put(clan.getName().toLowerCase(), clan);
                } catch(Exception ex) {

                }
            }
        }catch(final Exception ex) {

        }

        if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
            Server.getLogger().log(Level.INFO, "Loaded " + clans.size() + " clans.");
    }

}
