package org.hyperion.rs2.model.content.misc;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Misc;

public class RandomSpamming {

	/**
	 * The array holding all the spam messages.
	 */
	private static final String[] MESSAGES = {"i love DeviousPK",
			"i love DeviousPK", "i love DeviousPK", "i love DeviousPK",
			"DeviousPK best spawn ever", "DeviousPK nr 1 spawn",
			"DeviousPK join now!! , 24-7 online", "DeviousPK join now",
			"DeviousPK better than sex", "Perfect spawn server",
			"DeviousPK Better than Runescape", "We love DeviousPK",
			"DeviousPK quality over quantity", "DeviousPK always ppl online",
			"DeviousPK +300 online", "DeviousPK  over 300 on",
			"DeviousPK all skills working", "DeviousPK turmoil and curses",
			"lol", "lol", "wtf", "haha"};

	/**
	 * @return Random Spam message
	 */
	private static String getRandomMessage() {
		int r = Misc.random(MESSAGES.length - 1);
		String uitroep = "";
		for(int i = 0; i < Misc.random(5); i++) {
			uitroep += "!";
		}
		String mess = MESSAGES[r] + uitroep;
		if(Math.random() > 0.7)
			mess = mess.replace("DeviousPK", "Devious");
		else if(Math.random() > 0.6)
			mess = mess.replace("DeviousPK", "DVPK");
		if(Math.random() > 0.5)
			mess = mess.replace("Runescape", "RS");
		return mess;
	}

	/**
	 * The spam Event
	 */
	private static Task spamEvent = new Task(4000) {
		int counter = 0;

		@Override
		public void execute() {
			counter++;
			if(counter >= 10) {
				this.stop();
				counter = 0;
			}
			for(Player p : World.getPlayers()) {
				for(int i = 0; i < 10; i++) {
					if(colours)
						forceColoredChatMessage(p, getRandomMessage());
					else
						forceChatMessage(p, getRandomMessage());
				}
			}
		}
	};

	private static boolean colours = false;

	/**
	 * To start the Spamming ;)
	 */
	public static void start(boolean colors) {
		if(colors)
			colours = true;
		World.submit(spamEvent);
	}

	/**
	 * @param player , forces him to Spam a message.
	 * @param string
	 */
	private static void forceColoredChatMessage(Player player, String string) {
		player.getChatMessageQueue().add(new ChatMessage(Misc.random(5), Misc.random(11), method526(string)));
	}

	private static void forceChatMessage(Player player, String string) {
		player.getChatMessageQueue().add(new ChatMessage(0, 0, method526(string)));
	}

	private static final char validChars[] = {
			' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's',
			'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j',
			'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.',
			',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243',
			'$', '%', '"', '[', ']', '>', '<', '^', '/', '_'
	};

	private static byte[] method526(String s) {
		if(s.length() > 80)
			s = s.substring(0, 80);
		s = s.toLowerCase();
		int i = - 1;
		byte[] bytes = new byte[s.length()];
		int index = 0;
		for(int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);
			int k = 0;
			for(int l = 0; l < validChars.length; l++) {
				if(c != validChars[l])
					continue;
				k = l;
				break;
			}

			if(k > 12)
				k += 195;
			if(i == - 1) {
				if(k < 13)
					i = k;
				else
					bytes[index++] = (byte) (k);
			} else if(k < 13) {
				bytes[index++] = (byte) ((i << 4) + k);
				i = - 1;
			} else {
				bytes[index++] = (byte) ((i << 4) + (k >> 4));
				i = k & 0xf;
			}
		}
		if(i != - 1)
			bytes[index++] = (byte) (i << 4);
		return bytes;
	}

}
