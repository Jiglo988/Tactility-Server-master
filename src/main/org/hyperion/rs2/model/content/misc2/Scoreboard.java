package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc.ScoreboardPlayer;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.util.TextUtils;

import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 * @author Arsen Maxyutov.
 */
public class Scoreboard implements ContentTemplate {

	/**
	 * The scoreboard information will update not more frequently
	 * than this interval.
	 */
	private static final long UPDATE_INTERVAL = 10000;

	/**
	 * The interface id of the scoreboard.
	 */
	public static final int INTERFACE_ID = 8134;

	/**
	 * Howmany players are displayed on the scoreboard.
	 */
	public static final int TOP_SIZE = 10;

	/**
	 * Holds the top.
	 */
	private static LinkedList<ScoreboardPlayer> toplist = new LinkedList<ScoreboardPlayer>();

	/**
	 * Holds the last time the toplist was updated.
	 */
	private static long lastupdate = System.currentTimeMillis() - UPDATE_INTERVAL;

	/**
	 * Gets the toplist
	 *
	 * @return the toplist
	 */
	public static LinkedList<ScoreboardPlayer> getTopList() {
	    /*
         * If recently updated, don't update again.
		 */
		if(System.currentTimeMillis() - lastupdate < UPDATE_INTERVAL) {
			return toplist;
		}
		/*
		 * Update
		 */
		lastupdate = System.currentTimeMillis();
		LinkedList<ScoreboardPlayer> fullList = new LinkedList<ScoreboardPlayer>();
		for(Player player : World.getPlayers()) {
			if(player != null)
			fullList.add(new ScoreboardPlayer(player.getName(), player.getBounty()));
		}
		toplist = top(fullList, TOP_SIZE);
		return toplist;
	}

	/**
	 * Displays the scoreboard for the specified player.
	 *
	 * @param player
	 */
	public static void sendScoreboard(Player player) {
		player.getActionSender().sendString(8144, "@dre@Most wanted:");
		int i = 0;
		for(ScoreboardPlayer sp : getTopList()) {
			player.getActionSender().sendString(ActionSender.QUEST_MENU_IDS[i++], "@dre@" + i + ". @bla@" + TextUtils.ucFirst(sp.getName()) + "            @dre@Bounty: @bla@" + sp.getBounty() + " Pk points");
		}
		for(; i < ActionSender.QUEST_MENU_IDS.length; i++) {
			player.getActionSender().sendString(ActionSender.QUEST_MENU_IDS[i], "");
		}
		player.getActionSender().showInterface(INTERFACE_ID);
	}

	public static LinkedList<ScoreboardPlayer> top(LinkedList<ScoreboardPlayer> fullList, int top) {
		if(top > fullList.size())
			return fullList;
		LinkedList<ScoreboardPlayer> toplist = new LinkedList<ScoreboardPlayer>();
		//Create toplist by storing the best in it, a variation of selection sort.
		for(int i = 0; i < top; i++) {
			ScoreboardPlayer best = fullList.get(0);
			for(ScoreboardPlayer sp : fullList) {
				if(sp.getBounty() > best.getBounty()) {
					best = sp;
				}
			}
			toplist.add(best);
			fullList.remove(best);
		}
		return toplist;
	}

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c, int d) {
		if(type == 6) {
			sendScoreboard(player);
		}
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
		ObjectManager.addObject(new GameObject(GameObjectDefinition.forId(3192), Position.create(3084, 3485, 0), 10, 0)); //ScoreBoard
	}

	@Override
	public int[] getValues(int type) {
		if(type == 6)
			return new int[]{3192};
		return null;
	}

}
