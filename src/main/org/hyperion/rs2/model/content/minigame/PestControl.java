package org.hyperion.rs2.model.content.minigame;

import org.hyperion.engine.task.impl.NpcDeathTask;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PestControl implements ContentTemplate {

	/* torcher = defiler - slater - brawler = shifter - spinner
	 *  Name npc	Levels			Combat style	Attack knight?	Attacks players?

 Brawler 	51, 76, 101, 129	Melee		No		Yes directly				
														
 Defiler 	33, 50, 66, 80, 97 	Range		Yes		If they are 10tiles away from void, then not then they searchh for knight, otherwise they do players		
															
 Ravager 	36, 53, 71, 89, 106 	Melee		No		No, only fights back	
																
 Shifter 	38, 57, 90, 104		Melee		Yes		random, Mostly teles to knight		
																		
 Spinner 	36, 55, 74, 88, 92 	Melee		No		No, heals portal			
															
 Splatter	22, 33, 44, 55, 65 	Melee		No		no only fights backs or hit when they explode near fence (just like the red prayer thingy)				
																			
 Torcher 	33, 49, 79, 91, 92 	Mage		Yes		same as defiler				
	 * */

	@Override
	public void init() throws FileNotFoundException {
        /*World.submit(new Event(1000){
			@Override
			public void execute(){
				process();
			}
		});*/
	}

	@Override
	public int[] getValues(int type) {
		if(type == 6) {
			int[] j = {14314, 14315,};
			return j;
		}
		return null;
	}

	@Override
	public boolean clickObject(final Player client, final int type, final int oId, final int oX, final int oY, final int a) {
		if(type == 6) {
			if(oId == 14314) {
				waitingBoat.remove(client);
				client.setTeleportTarget(Position.create(2657, 2639, 0));
			} else if(oId == 14315) {
				if(waitingBoat.contains(client))
					return false;
				waitingBoat.add(client);
				client.setTeleportTarget(Position.create(2661, 2639, 0));
			}
		}
		return false;
	}

	public static class PestNPC {
		public NPC npc;
		public int type = 0;//brawler etc

		public PestNPC(NPC npc, int type) {
			this.npc = npc;
			this.type = type;
		}
	}

	public List<Player> waitingBoat = new ArrayList<Player>();

	public List<Player> playersInGame = new ArrayList<Player>();

	public List<PestNPC> monsters = new ArrayList<PestNPC>();

	public int waitTimer = 30;

	public int gameTimer = 3600;

	public PestNPC voidKnight = null;
	public PestNPC[] portals = new PestNPC[4];

	public void startUpGame() {
		//spawn portals etc
		voidKnight = new PestNPC(NPCManager.addNPC(2656, 2592, 0, 3782, - 1), - 1);
		voidKnight.npc.health = 200;
		voidKnight.npc.maxHealth = 200;
		portals[0] = new PestNPC(NPCManager.addNPC(2628, 2591, 0, 6150, - 1), - 1);
		portals[1] = new PestNPC(NPCManager.addNPC(2680, 2588, 0, 6151, - 1), - 1);
		portals[2] = new PestNPC(NPCManager.addNPC(2669, 2570, 0, 6152, - 1), - 1);
		portals[3] = new PestNPC(NPCManager.addNPC(2645, 2569, 0, 6153, - 1), - 1);
		for(PestNPC pn : portals) {
			pn.npc.health = 200;
			pn.npc.maxHealth = 200;
		}

		//void knight 3782
		//portals - 6154-6157
	}

	public void spawnNpc(int i, Position position, Player player) {
		NPCManager.addNPC(position.getX(), position.getY(), player.getIndex() * 4, i, - 1);
		//npc.agressiveDis = 150;
	}

	public void process() {
		if(playersInGame.size() <= 0) {
			if(waitTimer > 0)
				waitTimer--;
			else if(waitTimer == 0) {
				//tele all to arena
				startUpGame();
				for(Player player : waitingBoat) {
					player.setTeleportTarget(Position.create((player.getPosition().getX() - 4), (player.getPosition().getY() - 29), 0));
					playersInGame.add(player);
				}
				gameTimer = 3600;
				waitingBoat.clear();
			}
		} else {
			boolean gameOver = false;
			@SuppressWarnings("unused")
			boolean win = false;
			if(gameTimer > 0)
				gameTimer--;
			if(voidKnight == null || voidKnight.npc.health <= 0) {
				//lose game
				gameOver = true;
				win = false;
			} else {
				for(PestNPC npc : portals) {
					if(npc != null && npc.npc.health <= 0) {
						gameOver = true;
						win = true;
					}
				}
			}
			if(gameTimer == 0) {
				gameOver = true;
				win = true;
			}
			if(gameOver) {
				//kill npcs

				//tele players
				for(Player player : playersInGame) {
					player.setTeleportTarget(Position.create(2659, 2647, 0));
				}
				killNpcs();
				playersInGame.clear();
				waitTimer = 30;
				//give tickets if win
			} else {
				//spawn more npcs :)
			}
		}
	}

	public void killNpcs() {
		for(PestNPC pnpc : monsters) {
			killNPC(pnpc);
		}
		if(voidKnight != null)
			killNPC(voidKnight);
		if(portals[0] != null)
			killNPC(portals[0]);
		if(portals[1] != null)
			killNPC(portals[1]);
		if(portals[2] != null)
			killNPC(portals[2]);
		if(portals[3] != null)
			killNPC(portals[3]);

	}

	public void killNPC(PestNPC pnpc) {
		pnpc.npc.health = 0;
		World.submit(new NpcDeathTask(pnpc.npc));
		pnpc.npc.setDead(true);
	}

}
