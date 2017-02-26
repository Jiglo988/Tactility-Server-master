package org.hyperion.rs2.model.content.minigame;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class GodWars implements ContentTemplate {

	@Override
	public boolean clickObject(final Player player, int type, int objId, int b, int c,
	                           int d) {
		// TODO Auto-generated method stub
		if(type == 6) {
			if(objId == 26293) {
				player.getActionSender().sendMessage("You climb the rope.");
				player.setTeleportTarget(Position.create(3433, 2892, 0));
				player.getActionSender().showInterfaceWalkable(- 1);
			}
			if (objId == 26425) {
				if (player.getPosition().getX() == 2863) {
					player.setTeleportTarget(player.getPosition().transform(1, 0, 0));
				} else {
					player.setTeleportTarget(player.getPosition().transform(-1, 0, 0));
				}
			}

		} else if(type == 16) {
			if(bandos.get(objId) != null)
				player.godWarsKillCount[0]++;
			else if(zammy.get(objId) != null)
				player.godWarsKillCount[1]++;
			else if(sara.get(objId) != null)
				player.godWarsKillCount[2]++;
			else if(armdayl.get(objId) != null)
				player.godWarsKillCount[3]++;
			if(objId == 6222 || objId == 6247 || objId == 6203 || objId == 6260)
				killBoss(objId, b, c);
			refreshKillCount(player);
		}
		return true;
	}

	public void killBoss(int bossId, int x, int y) {

	}

	public static boolean inGodwars(Player player) {
		return player.getPosition().getX() >= 2814 && player.getPosition().getX() <= 2942 && player.getPosition().getY() >= 5250 && player.getPosition().getY() <= 5373;
	}

	public static GodWars godWars = null;

	public void checkGodWarsInterface(Player player) {
		if(inGodwars(player)) {
			player.getActionSender().showInterfaceWalkable(16210);
			refreshKillCount(player);
		}
	}

	public static void refreshKillCount(Player player) {
		player.getActionSender().sendString(16216, "" + player.godWarsKillCount[3]);
		player.getActionSender().sendString(16217, "" + player.godWarsKillCount[0]);
		player.getActionSender().sendString(16218, "" + player.godWarsKillCount[2]);
		player.getActionSender().sendString(16219, "" + player.godWarsKillCount[1]);
	}

	private Map<Integer, Object> bandos = new HashMap<Integer, Object>();
	private Map<Integer, Object> zammy = new HashMap<Integer, Object>();
	private Map<Integer, Object> sara = new HashMap<Integer, Object>();
	private Map<Integer, Object> armdayl = new HashMap<Integer, Object>();

	@Override
	public void init() throws FileNotFoundException {
		godWars = this;
		// TODO Auto-generated method stub
		bandos.put(6261, this);//generals
		bandos.put(6263, this);
		bandos.put(6265, this);
		bandos.put(6260, this);//boss
		bandos.put(6271, this);//orks
		bandos.put(6272, this);
		bandos.put(6273, this);
		bandos.put(6274, this);
		bandos.put(6270, this);//cyclops
		bandos.put(6269, this);
		bandos.put(6267, this);//ogre
		bandos.put(6268, this);//jogre
		bandos.put(6275, this);//hobgoblin
		bandos.put(6279, this);//goblin
		bandos.put(6280, this);
		bandos.put(6281, this);
		bandos.put(6282, this);
		bandos.put(6283, this);
		bandos.put(6278, this);//spiritual stuff
		bandos.put(6277, this);
		bandos.put(6276, this);
		//emotes done and checked

		zammy.put(6211, this);//imp
		zammy.put(6214, this);//vampire
		zammy.put(6216, this);//pyrefiend
		zammy.put(6217, this);//icefiend
		zammy.put(6213, this);//werewolf
		zammy.put(6212, this);//werewolf
		zammy.put(6215, this);//bloodveld
		zammy.put(6218, this);//gorak
		zammy.put(6206, this);//generals
		zammy.put(6208, this);
		zammy.put(6204, this);
		zammy.put(6203, this);//boss
		zammy.put(6221, this);//spiritual stuff
		zammy.put(6220, this);
		zammy.put(6219, this);
		//done and checked

		sara.put(6254, this);//priest
		sara.put(6258, this);//knights
		sara.put(6259, this);
		sara.put(6252, this);//generals
		sara.put(6250, this);
		sara.put(6248, this);
		sara.put(6247, this);//boss
		sara.put(6257, this);//spiritual stuff
		sara.put(6256, this);
		sara.put(6255, this);


		armdayl.put(6222, this);//boss
		armdayl.put(6223, this);//generals
		armdayl.put(6225, this);
		armdayl.put(6227, this);
		armdayl.put(6246, this);//aviansies
		armdayl.put(6245, this);
		armdayl.put(6244, this);
		armdayl.put(6243, this);
		armdayl.put(6242, this);
		armdayl.put(6241, this);
		armdayl.put(6240, this);
		armdayl.put(6239, this);
		armdayl.put(6238, this);
		armdayl.put(6237, this);
		armdayl.put(6236, this);
		armdayl.put(6235, this);
		armdayl.put(6234, this);
		armdayl.put(6233, this);
		armdayl.put(6232, this);

		armdayl.put(6231, this);//spiritual stuff
		armdayl.put(6230, this);
		armdayl.put(6229, this);

	}

	@Override
	public int[] getValues(int type) {
		// TODO Auto-generated method stub
		if(type == 6) {
			int[] j = {26293, 26425,};
			return j;
		} else if(type == 16) {
			int[] j = new int[(bandos.size() + zammy.size() + sara.size() + armdayl.size() + 1)];//monsters that count to kill count
			int i = 0;
			for(Map.Entry<Integer, Object> entry : bandos.entrySet()) {
				j[i++] = entry.getKey();
			}
			for(Map.Entry<Integer, Object> entry : zammy.entrySet()) {
				j[i++] = entry.getKey();
			}
			for(Map.Entry<Integer, Object> entry : sara.entrySet()) {
				j[i++] = entry.getKey();
			}
			for(Map.Entry<Integer, Object> entry : armdayl.entrySet()) {
				j[i++] = entry.getKey();
			}
			return j;
		}
		return null;
	}

}
