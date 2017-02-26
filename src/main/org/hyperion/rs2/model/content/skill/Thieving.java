package org.hyperion.rs2.model.content.skill;

import org.hyperion.data.PersistenceManager;
import org.hyperion.engine.task.Task;
import org.hyperion.map.WorldMap;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc.Stall;
import org.hyperion.rs2.model.region.RegionManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Thieving implements ContentTemplate {

	/**
	 * Class constructor.
	 */

	public Thieving() {
	}

	private List<Stall> stalls;
	//private List<PickpocketNpc> pickpockets;

	private final int STUN_TIMER = 6000,
			STUN_GFX = 245,
			STEAL_ANIM = 881;

	//private final Random r = new Random();


	/**
	 * Checks if the NPC can be pickpocketed.
	 * @param client The {@link Player}.
	 * @param npc The npc that we're pickpocketing.
	 */

	/*public boolean pickpocket(Player client, int npc) {
	    for(PickpocketNpc p : pickpockets) {
			if(p.getNpcId() == npc)
				return true;
		}
		return false;
	}*/

	/**
	 * Gets the index of the stall.
	 *
	 * @param stall The stall.
	 */

	public int getStallIndex(int stall) {
		for(Stall s : stalls) {
			if(s.getStallId() == stall)
				return stalls.indexOf(s);
		}
		return - 1;
	}

	/**
	 * Get the index of the npc that is being pickpocketed.
	 * @param npc The npc we're pickpocketing.
	 */

	/*public int getPickpocketIndex(int npc) {
		for(PickpocketNpc p : pickpockets) {
			if(p.getNpcId() == npc)
				return pickpockets.indexOf(p);
		}
		return -1;
	}*/

	/**
	 * Steal from a stall.
	 * @param client The {@link Player}.
	 * @param stall The stall where we're thieving from.
	 */
	
	/*public boolean stealFromStall(final Player client, final int stall) {		
		// Check if the player is busy.
		if(client.isBusy())
			return false;
		
		// Get the info about the stall.
		int k = getStallIndex(stall);
		if(k == -1)
			return false;
		final Stall s = stalls.get(k);
		if(s == null)
			return false;
		
		// Check if the player has a high enough level to steal.
		if(ContentEntity.returnSkillLevel(client,17) < s.getLevel()) {
			ContentEntity.sendMessage(client,(new StringBuilder())
					.append("You need a Thieving level of ")
					.append(s.getLevel())
					.append("to thieve from a ")
					.append(ContentEntity.getObjectName(stall)).toString());
			return true;
		}
		
		// Check if the player has space.
		if(ContentEntity.freeSlots(client) <= 0) {
			ContentEntity.sendMessage(client,"You don't have enough space in your inventory.");
			return true;
		}
		
		// Check if the player is in combat.
		//TODO
		
		// Start the animation.
		ContentEntity.startAnimation(client,STEAL_ANIM);
		
		// Start the event.
		World.submit(new Event(750) {
			@Override
			public void execute() {			
				
				if(r.nextInt(3) == 1) {
				
					int item = s.getItems()[r.nextInt(s.getItems().length)];
					String name = ContentEntity.getItemName(item).toLowerCase();

					// Add a random amount of a random item of the stall.
					ContentEntity.addItem(client,item, 1);
					
					// Send the message.
					ContentEntity.sendMessage(client,"You steal "+
							(item == 2309 ? "some bread." : ("a "+name+".")));
					
					// Add the experience.
					ContentEntity.addSkillXP(client,s.getExperience(), 17);		
					
					// Stop the event.
					stop2();
					
				} else {*/
					/*int sDis = 8;
					NPC npc = null;
					for (Map.Entry<Integer, NPC> entry : Server.getNpcManager().npcMap.entrySet()) {
						NPC n = entry.getValue();
						if(n == null)
							continue;
						int dis = ContentEntity.getDis(client,n.getAbsX(),n.getAbsY(),client.getAbsX(),client.getAbsY());
						if(dis < sDis && n.getHP() > 0 && !n.isDead()){
							npc = n;
							sDis = dis;
						}
					}
					if(npc == null)
						return;
					npc.setShout("Hey! Get your hands off there!");
					npc.setShoutUpdateRequired(true);
					npc.setUpdateRequired(true);
					final NPC npc2 = npc;
					World.submit(new Event(2000) {
						@Override
						public void execute() {		
							
							//npc attacks player
							if(!npc2.isObject)
								npc2.faceTo(client.playerId);
				
							if (npc2.getAttacker() == null && !npc2.isObject) {
								npc2.setAttacker(client);
							}
							
							//npc.setAttacker(client);
							this.stop();
							
						}		
					});*/
				/*}
			}*/
				
			/*public void stop2() {
				ContentEntity.startAnimation(client,-1);
				client.setBusy(false);
				this.stop();
			}

		});	
		return true;
	}*/

	/**
	 * Pickpocket a npc.
	 *
	 * @param client The {@link Player}.
	 */

	public void stealFromNpc(final Player client, final NPC npc) {
		// Check if the player is busy.
		/*if(client.isBusy())
			return;
		else client.setBusy(true);
		
		// Check if the player is stunned.
		if(client.isStunned()) {
			ContentEntity.sendMessage(client,"You're stunned.");
			return;
		}
		
		// Get the info about the stall.
		final PickpocketNpc p = pickpockets.get(getPickpocketIndex(npc.getDefinition().getType()));
		
		// Check if the player has a high enough level to steal.
		if(ContentEntity.returnSkillLevel(c,17) < p.getLevel()) {
			ContentEntity.sendMessage(client,(new StringBuilder())
					.append("You need a Thieving level of ")
					.append(p.getLevel())
					.append("to thieve a ")
					.append(npc.getDefinition().getName()).toString());
			return;
		}
		
		// Check if the player has space.
		if(ContentEntity.freeSlots(client) <= 0) {
			ContentEntity.sendMessage(client,"You don't have enough space in your inventory.");
			return;
		}
		
		// Check if the player is in combat.
		//TODO: You can't pickpocket during combat.
		
		// Send the message
		ContentEntity.sendMessage(client,(new StringBuilder())
				.append("You attempt to pick the ")
				.append(npc.getDefinition().getName())
				.append("'s pocket.").toString());		
		
		// Start the animation.
		ContentEntity.startAnimation(client,STEAL_ANIM);
		
		// Start the event.
		World.submit(new Event(2000) {
			@Override
			public void execute() {				

					if(r.nextInt(3) <= 1) {
				
						// Add a random amount of a random item of the stall.
						ContentEntity.addItem(client,p.getItems()[r.nextInt(p.getItems().length)],
								p.getAmounts()[r.nextInt(p.getAmounts().length)]);
						
						// Add the experience.
						ContentEntity.addSkillXP(client,p.getExperience(), 17);		
						
						// Send the message
						ContentEntity.sendMessage(client,(new StringBuilder())
								.append("You pick the ")
								.append(npc.getDefinition().getName())
								.append("'s pocket.").toString());		

					} else {
						// Npc shout text.
						npc.setShout("What do you think that you're doing?");
						npc.setShoutUpdateRequired(true);
						npc.setUpdateRequired(true);
						//npc.setAttacker(client);
						// Stop the npc from walking.
						npc.setWalking(false);
						
						// Show the stun gfx.
						ContentEntity.playerGfx(client,STUN_GFX);
						
						// Npc attacks player.
						// TODO: test on webclient, gives npcpos error in mps, also faceto does
						//npc.setAnimNumber(422);
						//npc.setAnimUpdateRequired(true);
						//npc.setUpdateRequired(true);

						// Send the messages
						ContentEntity.sendMessage(client,(new StringBuilder())
								.append("You fail to pick the ")
								.append(npc.getDefinition().getName())
								.append("'s pocket.").toString());		
						ContentEntity.sendMessage(client,"You've been stunned!");
						
						// Stop the player from walking
						client.setCanWalk(false);		
						
						// Hit the player.
						ContentEntity.hit(client,p.getDamage(), 1);
						
						// Set the player stunned.
						client.setStunned(true);
						
						// Set the npc back to walking
						npc.setWalking(true);
						
						// Start the event.
						World.submit(new Event(4000) {
							@Override
							public void execute() {		
								// We don't have todo anything
								stop3();
							}
							public void stop3() {
								client.setStunned(false);
								client.setCanWalk(true);
								client.setBusy(false);
								this.stop();
							}
						});
					}          			
					
					// Stop the event.
					stop2();

			}
				
			public void stop2() {
				client.setBusy(false);
				this.stop();
			}

		});		*/
	}

	public static final int[] pickPocketNpcs = {1, 2, 3, 4, 5, 6, 7, 9, 15, 16, 18, 20, 21, 23, 24, 25, 26, 32, 34, 66, 67, 68, 159, 160, 161, 168, 169, 170, 187, 296, 297, 298, 299, 617, 646, 1086, 1128, 1129, 1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1528, 1670, 1714, 1715, 1757, 1758, 1880, 1881, 1883, 1884, 1888, 1889, 1890, 1892, 1893, 1894, 1896, 1897, 1898, 1905, 1926, 1931, 2082, 2234, 2235, 2256, 2363, 2364, 2365, 2366, 2367, 2675, 2683, 2684, 2699, 2700, 2701, 2702, 2703, 3112, 3222, 3223, 3224, 3225, 3226, 3227, 3228, 3229, 3230, 3231, 3232, 3233, 3237, 3238, 3239, 3240, 3241, 3407, 3408, 3915, 4307, 4308, 4309, 4310, 4311, 5589, 5590, 5591, 5592, 5606, 5752, 5753, 5754, 5755, 5756, 5757, 5758, 5759, 5760, 5761, 5762, 5763, 5764, 5765, 5766, 5767, 5768, 5769, 5825, 5919, 5920, 5923, 5924, 6002,};

	public void pickPocketNpc(final Player player, NPC npc) {
		if(player.cE.isFrozen())
			return;
		if(player.getRandomEvent().skillAction()) {
			return;
		}
		if(player.getExtraData().getLong("thievingTimer") > System.currentTimeMillis())
			return;
		boolean isOk = false;
		for(int i = 0; i < pickPocketNpcs.length; i++) {
			if(pickPocketNpcs[i] == npc.getDefinition().getId()) {
				isOk = true;
			}
		}
		if(! isOk || player.isBusy())
			return;
		if(npc.getPosition().distance(player.getPosition()) > 2)
			return;
		if(npc.cE.getOpponent() == player.cE) {
			ContentEntity.sendMessage(player, "You cant do that when the npc is looking.");
			return;
		}
		boolean successful = false;
		if(Combat.random(player.getSkills().getLevel(Skills.THIEVING) * 2) >= Combat.random(npc.getDefinition().combat()))
			successful = true;
		player.playAnimation(Animation.create(STEAL_ANIM));
		player.face(npc.getPosition());
		if(! successful) {
			player.playGraphics(Graphic.create(STUN_GFX, 6553600));
			player.cE.setFreezeTimer(STUN_TIMER);
			ContentEntity.sendMessage(player, "You get caught pickpocketing the npc");
			npc.forceMessage("Hey, what are you doing!");
			//npc.cE.setOpponent(player.cE);
			npc.cE.doAtkEmote();
			player.cE.hit(Combat.random(5), npc, false, 0);
		} else {
			ContentEntity.addItem(player, 995, 100 * npc.getDefinition().combat());
			ContentEntity.sendMessage(player, "You successfully pickpocket the npc");
			ContentEntity.addSkillXP(player, 10 * npc.getDefinition().combat() * Constants.XPRATE, Skills.THIEVING);

		}
		player.setBusy(true);
		player.getExtraData().put("thievingTimer", System.currentTimeMillis() + 2000);
		World.submit(new Task(2000,"thieving") {
			@Override
			public void execute() {
				player.getExtraData().remove("thievingTimer");
				player.setBusy(false);
				this.stop();
			}
		});
	}

	public boolean stealFromStall(final Player player, final int objectId, final int x, final int y) {
		/*if (ObjectManager.getObjectAt(x, y,player.getLocation().getZ()) != null) {
			System.out.println("Stealing from stall null");
			return true;
		}*/

		if(System.currentTimeMillis() < player.getExtraData().getLong("stallTimer"))
			return true;
		if(player.getInventory().freeSlots() == 0) {
			player.getActionSender().sendMessage("You need more space before you steal from this stall.");
			return true;
		}
		if(getStallIndex(objectId) == - 1) {
			System.out.println("Stall index is -1");
			return true;
		}
		Stall stall = stalls.get(getStallIndex(objectId));
		if(stall.getLevel() > player.getSkills().getLevel(17)) {
			player.getActionSender().sendMessage("You need a thieving level of " + stall.getLevel() + " to steal from this stall.");
			return true;
		}
		Integer face = (Integer) WorldMap.thiefstalls.get((x * 5000 + y));
		if(face == null)
			face = 10;
		int item = ContentEntity.random(stall.getItems().length - 1);
		ContentEntity.addItem(player, stall.getItems()[item], stall.getAmounts()[item]);
		//System.out.println(stall.getExperience() + "  xp given from stall");
		ContentEntity.addSkillXP(player, stall.getExperience() * Constants.XPRATE, Skills.THIEVING);
		player.getActionSender().sendMessage("You steal from the stall.");
		player.getExtraData().put("stallTimer", System.currentTimeMillis() + 1800);
		player.playAnimation(Animation.create(STEAL_ANIM));
		player.face(Position.create(x, y, 0));
		NPC guard = getGuard(player);
		if(guard != null) {
			guard.forceMessage("What are you doing over there!");
			guard.cE.setOpponent(player.cE);
			Combat.processCombat(guard.cE);
		}
		return true;
	}

	public NPC getGuard(Player player) {
		boolean caught = true;
		if(ContentEntity.random((player.getSkills().getLevel(17) * 10) + 200) > ContentEntity.random(150)) {
			caught = false;
		}
		if(! caught)
			return null;
		for(NPC npc : RegionManager.getLocalNpcs(player)) {
			boolean continue2 = false;
			for(int i : guardIds) {
				if(i == npc.getDefinition().getId())
					continue2 = true;
			}
			if(! continue2)
				continue;
			if(npc.getPosition().distance(player.getPosition()) <= 4 && npc.cE.getOpponent() == null && npc.maxHealth > 0 && npc.health > 0)
				return npc;
		}
		return null;
	}

	private static final int[] guardIds = {9, 10, 32, 21, 23, 26};

	/**
	 * Loads the XML file of thieving.
	 *
	 * @throws FileNotFoundException
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws FileNotFoundException {
		/*pickpockets = (List<PickpocketNpc>) PersistenceManager.load(new FileInputStream("./data/pickpockets.xml"));
		*/
		stalls = (List<Stall>) PersistenceManager.load(new FileInputStream("./data/stalls.xml"));

	}

	@Override
	public int[] getValues(int type) {
		if(type == 11) {
			return pickPocketNpcs;
		}
		if(type == 7) {
			int[] stall_ids = new int[stalls.size()];
			int idx = 0;
			for(Stall stall : stalls) {
				stall_ids[idx++] = stall.getStallId();
			}
			return stall_ids;
		}
		return null;
	}

	@Override
	public boolean clickObject(final Player client, final int type, final int id, final int slot, final int itemId2, final int npcSlot) {
		if(type == 11) {
			NPC npc = (NPC) World.getNpcs().get(npcSlot);
			pickPocketNpc(client, npc);
		}
		if(type == 7) {
			return stealFromStall(client, id, slot, itemId2);
		}
		return false;
	}


}