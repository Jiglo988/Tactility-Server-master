package org.hyperion.rs2.model.content.minigame;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.ShopManager;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;

/**
 * @author SaosinHax/Linus/Vegas/Flux/Tinderbox/Jack Daniels/Arsen/Jolt <- All same person
 */

public class RecipeForDisaster implements ContentTemplate {

	private static final int[] WAVES = {3493, 3494, 3495, 3496, 3491};

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c,
	                           int d) {
		if(type == 6) {
			if(a == 12356) {
				if(player.getPosition().getY() < 4000)
					startRFD(player);
				else
					leaveRFD(player);
			} else if(a == 2403) {
				if(player.RFDLevel <= 5) {
					int shopLevel = player.RFDLevel + 66;
					ShopManager.open(player, shopLevel <= 72 ? shopLevel : 72);
				}
			}
		}
		if(type == 16) {
			for(int i = 0; i < WAVES.length - 1; i++) {
				if(a == WAVES[i]) {
					if(player.RFDLevel < 4) {
						player.RFDLevel++;
						spawnWave(player, player.RFDLevel);
					}
				}
			}
			if(a == 3491) {
				player.getActionSender().sendMessage(
						"You have completed the recipe for disaster minigame!");
				player.getActionSender().sendMessage(
						"You can now leave using one of the portals!");
			}
		}
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {

	}

	@Override
	public int[] getValues(int type) {
		if(type == 6) {
			int[] objectids = {12356, 2403};
			return objectids;
		}
		if(type == 16) {
			return WAVES;
		}
		return null;
	}

	public void startRFD(Player player) {
		if(player.RFDLevel < 0) {
			player.RFDLevel = 0;
		}
		if(player.RFDLevel >= 5) {
			player.getActionSender().sendMessage(
					"You have already finished this minigame!");
			return;
		}
		player.resetPrayers();
		player.setTeleportTarget(Position.create(1899, 5363, 2));
		player.getActionSender().sendMessage(
				"Prepare Yourself, the waves will start in a few seconds.");
		spawnWave(player, player.RFDLevel);
	}

	public void leaveRFD(Player player) {
		player.setTeleportTarget(Position.create(3209, 3225, 0));
	}

	public void spawnWave(final Player player, final int rfdlevel) {
		World.submit(new Task(5000,"recipefordistaster") {
			public void execute() {
				spawnNpc(WAVES[rfdlevel], getLocation(), player);
				this.stop();
			}
		});
	}

	public NPC spawnNpc(int npcid, Position position, Player player) {
		NPC npc = NPCManager
				.addNPC(position.getX(), position.getY(), position.getZ(),
						npcid, - 1);
		npc.agressiveDis = 150;
		npc.ownerId = player.getIndex();
		return npc;
	}

	public Position getLocation() {
		return Position.create(1898 + Misc.random(4), 5352 + Misc.random(4), 2);
	}

	public static boolean inRFD(Player player) {
		if(player.getPosition().getX() > 2000 || player.getPosition().getX() < 1800) {
			return false;
		}
		return !(player.getPosition().getY() > 5400 || player.getPosition().getY() < 5300);
	}
}
