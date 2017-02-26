package org.hyperion.rs2.model.content.clan;

import com.google.gson.JsonElement;
import org.apache.mina.core.buffer.IoBuffer;
import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.packet.CommandPacketHandler;
import org.hyperion.rs2.saving.IOData;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.util.IoBufferUtils;
import org.hyperion.rs2.util.TextUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Clan {

	private String clanName;
	private String owner;

	private CopyOnWriteArrayList<ClanMember> rankedMembers = new CopyOnWriteArrayList<ClanMember>();
	private CopyOnWriteArrayList<String> peopleKicked = new CopyOnWriteArrayList<String>();
	private ArrayList<Player> players = new ArrayList<Player>();

    private static final int MAX_CLAN_MEMBERS = 100;

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public synchronized void add(Player player) {
		player.setClanName(this.clanName);
		players.add(player);
	}

	public synchronized void remove(Player player) {
        player.setClanName("");
		players.remove(player);
	}

	public int size() {
		return players.size();
	}

	public boolean isFull() {
		return players.size() >= MAX_CLAN_MEMBERS;
	}

	public String getName() {
		return TextUtils.titleCase(clanName);
	}

	public void setName(String newName) {
		this.clanName = newName;
		players.stream().forEach(member -> member.getActionSender().sendString(18139, String.format("Talking in: %s", TextUtils.titleCase(newName))));
	}

    public void listBans(Player player) {
		if (peopleKicked.isEmpty()) {
			player.sendf("No Banned Players in Clan '%s'.", getName());
			return;
		}
		peopleKicked.stream().filter(value -> value != null).forEach(player::sendMessage);
    }

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
		players.stream().forEach(member -> member.getActionSender().sendString(18140, String.format("Owner: %s", TextUtils.titleCase(owner))));
	}

	public Clan(String owner, String name) {
		this.clanName = name;
		this.owner = owner;
	}

    public void makeDiceClan() {
        if(!Dicing.diceClans.remove(clanName))
            Dicing.diceClans.add(clanName);
    }

    public boolean isDiceClan() {
        return Dicing.diceClans.contains(clanName);
    }

	public boolean kick(String name, boolean ip) {
		final Player player = World.getPlayerByName(name);
		if (player == null) {
			player.sendf("No Player '%s' found.", TextUtils.titleCase(name));
			return false;
		}
		ClanManager.leaveChat(player, true, false);
		peopleKicked.add(player.getName());
		player.sendClanMessage("You have been kicked from the Clan Chat.");
		if (ip) {
			peopleKicked.add(player.getShortIP());
		}
		return true;
	}

	public boolean isKicked(String name) {
        final Player player = World.getPlayerByName(name);
        if(player != null && peopleKicked.contains(player.getShortIP()))
            return true;
        return peopleKicked.contains(name);
	}

    public boolean unban(final String name) {
        if(!peopleKicked.contains(name))
            return false;
        peopleKicked.remove(name);
        final Player player = World.getPlayerByName(name);
        if(player != null)
            peopleKicked.remove(player.getShortIP());
        else {
			GameEngine.submitIO(new EngineTask<Boolean>("Getting Short IP", 4, TimeUnit.SECONDS) {
				@Override
				public Boolean call() throws Exception {
					Optional<JsonElement> playerData = PlayerLoading.getProperty(name, IOData.LAST_IP);
					if (playerData.isPresent()) {
						peopleKicked.remove(playerData.get().getAsString().split(":")[0].replace("/", ""));
					}
					return true;
				}

				@Override
				public void stopTask() {
				}
			});
        }
        return true;
    }

	public CopyOnWriteArrayList<ClanMember> getRankedMembers() {
		return rankedMembers;
	}

	public void addRankedMember(ClanMember cm) {
        for(final ClanMember mem : rankedMembers) {
            if(mem.getName().equalsIgnoreCase(cm.getName()))
                rankedMembers.remove(mem);
        }
		if(! rankedMembers.contains(cm))
			rankedMembers.add(cm);
	}

    public void save(final IoBuffer buffer) {
        IoBufferUtils.putRS2String(buffer, owner);
        IoBufferUtils.putRS2String(buffer, clanName);
        buffer.putShort((short)rankedMembers.size()); // size of rankedMembers
        rankedMembers.stream().filter(Objects::nonNull).forEach(m -> m.save(buffer));
        buffer.putShort((short) peopleKicked.size());
        peopleKicked.stream().forEach(s -> IoBufferUtils.putRS2String(buffer, s));
    }

    public static Clan read(final IoBuffer buffer) {
        final Clan clan = new Clan(IoBufferUtils.getRS2String(buffer), IoBufferUtils.getRS2String(buffer));
        int ranked = buffer.getUnsignedShort();
        for(int i = 0 ; i < ranked; i++)
            clan.addRankedMember(new ClanMember(IoBufferUtils.getRS2String(buffer), buffer.get()));
        int banned = buffer.getUnsignedShort();
        for(int i = 0; i < banned; i++) {
            clan.peopleKicked.add(IoBufferUtils.getRS2String(buffer));
        }
        return clan;
    }



}
