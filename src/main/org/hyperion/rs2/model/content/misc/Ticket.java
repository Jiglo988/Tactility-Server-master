package org.hyperion.rs2.model.content.misc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.util.TextUtils;

public class Ticket {
	public static final HashMap<Player, TicketBuilder> tickets = new HashMap<Player, TicketBuilder>();
	
	private static final List<String> helpers = new LinkedList<String>();
	
	public static synchronized void removeRequest(Player p) {
		tickets.remove(p);
	}
	public static synchronized void putRequest(Player p, String reason) {
		tickets.put(p, new TicketBuilder(reason, System.currentTimeMillis()));
	}
	
	public static synchronized TicketBuilder getRequest(Player p) {
		return tickets.get(p);
	}
	
	public static synchronized void clearOffline() {
		for(Player p : tickets.keySet()) {
			if(p == null || !p.isActive()) {
				removeRequest(p);
			}
			long deltaMS = System.currentTimeMillis() - getRequest(p).startTime();
			if((getRequest(p).isAnswered() && deltaMS > 180000) || (deltaMS > 1200000)) {
				removeRequest(p);
			}
		}
	}
	
	public static void checkTickets(Player checker) {
		clearOffline();
		for(Player player : tickets.keySet()) {
			if(player != null) {
				helpers.add(checker.getName());
				checker.getActionSender().sendMessage(TextUtils.titleCase(player.getName()) + "| @blu@"+tickets.get(player).getReason()); 
			}
		}
	}
	
	public static boolean hasTicket(Player player) {
		if(getRequest(player) != null && !getRequest(player).isAnswered())
			return true;
		return false;
	}
}
