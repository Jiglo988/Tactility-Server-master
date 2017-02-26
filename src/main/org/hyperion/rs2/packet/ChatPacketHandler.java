package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

/**
 * Handles public chat messages.
 *
 * @author Graham Edgecombe
 */
public class ChatPacketHandler implements PacketHandler {

	private static final int CHAT_QUEUE_SIZE = 4;

	public static boolean debug = false;

	@Override
	public void handle(Player player, Packet packet) {
		//System.out.println("ChatPacketHandler handle method");
		if(player.isMuted)
			return;
		int effects = packet.getByteA() & 0xFF;
		int colour = packet.getByteA() & 0xFF;
		int size = packet.getLength() - 2;
		byte[] rawChatData = new byte[size];
		packet.get(rawChatData);
		byte[] chatData = new byte[size];
		byte[] packed = new byte[size];
		for(int i = 0; i < size; i++) {
			chatData[i] = (byte) (rawChatData[size - i - 1] - 128);
		}
		if(player.getChatMessageQueue().size() >= CHAT_QUEUE_SIZE) {
			return;
		}

		player.getSpam().checkSpam(chatData);
		try {
			String unpacked = TextUtils.textUnpack(chatData, size);
            //player.getLogManager().add(LogEntry.publicChat(unpacked));
			/*if(unpacked.contains("pkgods")) {
				System.out.println(unpacked);
				if(System.currentTimeMillis() - player.getCreatedTime() < Time.ONE_HOUR) {
					World.getBanManager().moderate("Server", player, BanManager.BAN, true, Long.MAX_VALUE, "pkgods spammer");
				}
				return;
			} */
			if(Misc.random(1000) == 1) {
				System.out.println(unpacked);
			}
		} catch(Exception e) {
			System.out.println("Could not unpack message");
		}
		if(debug) {
			//String unpacked = TextUtils.textUnpack(chatData, size);
			//System.out.println(unpacked);

			//System.out.println(Arrays.toString(chatData));
		}
	    /*try {
			String unpacked = TextUtils.textUnpack(chatData, size);
			System.out.println("unpack : " + unpacked);
			//System.out.println(""+unpacked);
			if(unpacked.startsWith("\\")){
				Clan.sendClanMessage(player, player.getName()+": "+unpacked.substring(1),true);
				return;
			}
			World.getAbuseHandler().cacheMessage(player,player.getName()+": "+unpacked);
			unpacked = TextUtils.filterText(unpacked);
			unpacked = TextUtils.optimizeText(unpacked);
			TextUtils.textPack(packed, unpacked);
		} catch(Exception e){
			//System.out.println("Exception e");*/
		packed = chatData;
		//}

		if(System.currentTimeMillis() - player.getCreatedTime() < Time.FIVE_MINUTES) {
			int messages = player.getExtraData().getInt("chatmessages");
			player.getExtraData().put("chatmessages", messages + 1);
			if(messages > 15)
				return;
		} else if(System.currentTimeMillis() - player.getCreatedTime() < Time.ONE_HOUR) {
			int messages = player.getExtraData().getInt("chatmessages");
			player.getExtraData().put("chatmessages", messages + 1);
			if(messages > 50)
				return;
		}
		player.getChatMessageQueue().add(new ChatMessage(effects, colour, packed));
	}

	static {
	}


}
