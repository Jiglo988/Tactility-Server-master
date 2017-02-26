package org.hyperion.rs2.packet;

import org.hyperion.Configuration;
import org.hyperion.rs2.model.FriendsAssistant;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.NameUtils;

public class FriendModifier implements PacketHandler {


	public static final int REGULAR_CHAT = 4, UPDATE_CHAT_OPTIONS = 95,
			FRIEND_ADD = 188, FRIEND_REMOVE = 215, IGNORE_ADD = 133,
			IGNORE_REMOVE = 74, PRIVATE_MESSAGE = 126;

	@Override
	public void handle(Player player, Packet packet) {
		if(packet.getOpcode() == PRIVATE_MESSAGE) {//pm
			if(player.isMuted)
				return;

			int count = player.getExtraData().getInt("pmCount");
			if (count > 2) {
				player.getActionSender().sendMessage("You cannot send PM's This quickly.");
				return;
			} else
				player.getExtraData().put("pmCount", count + 1);

			long nameLong = packet.getLong();

			String name = NameUtils.longToName(nameLong);
			String ownerName = Configuration.getString(Configuration.ConfigurationObject.OWNER);

			final Player playerTo = World.getPlayerByName(name);
			//null check b4 use
			if (playerTo == null || playerTo.chatStatus == null || (playerTo.chatStatus[1] == 2 && !Rank.isStaffMember(player)))
				return;

			if(name.equalsIgnoreCase(ownerName)) {
				Player owner = World.getPlayerByName(ownerName);
				if(!Rank.isStaffMember(player) && !Rank.hasAbility(player, Rank.SUPER_DONATOR)) {
					if(owner != null) {
						if(!owner.getFriends().contains(player.getNameAsLong())) {
							player.getActionSender().sendMessage("You cannot send PM's to " + ownerName);
							return;
						}
					}
				}
			}

			int chatTextSize = (byte) (packet.getLength() - 8);
			byte[] chatText = new byte[256];
			packet.get(chatText, 0, chatTextSize);
			FriendsAssistant.sendPm(player, nameLong, chatText, chatTextSize);

		} else if(packet.getOpcode() == FRIEND_ADD) {
			long g = packet.getLong();
			FriendsAssistant.addFriend(player, g);
		} else if(packet.getOpcode() == FRIEND_REMOVE) {
			long friend = packet.getLong();
			FriendsAssistant.removeFriend(player, friend);
		} else if(packet.getOpcode() == IGNORE_ADD) {
			long g = packet.getLong();
			FriendsAssistant.addIgnore(player, g);
		} else if(packet.getOpcode() == IGNORE_REMOVE) {
			long g = packet.getLong();
			FriendsAssistant.removeIgnore(player, g);
		} else if(packet.getOpcode() == UPDATE_CHAT_OPTIONS) {
			player.chatStatus[0] = packet.get();
			player.chatStatus[1] = packet.get();
			player.chatStatus[2] = packet.get();
			FriendsAssistant.refreshGlobalList(player, player.chatStatus[1] == 2);
		}
	}


}

