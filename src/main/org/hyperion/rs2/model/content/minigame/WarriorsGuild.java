package org.hyperion.rs2.model.content.minigame;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;

/**
 * @author Arsen Maxyutov.
 */
public class WarriorsGuild implements ContentTemplate {
	/**
	 * Npc Ids of Cyclops..
	 */
	public static final int[] CYCLOPSIDS = {4291, 4292};

	/**
	 * The tokens we're rewarded through all games in Warriors Guild.
	 */

	public static final int TOKENS = 8851;

	/**
	 * Set of all the Armour items used for the Animation Room. (Bronze - Rune)
	 * {helm, chest, legs}
	 */

	private static final int[][] ARMOUR_SET = {
			{1155, 1117, 1075}, //Bronze 0
			{1153, 1115, 1067}, //Iron 1
			{1157, 1119, 1069}, //Steel 2
			{1165, 1125, 1077}, //Black 3
			{1159, 1121, 1071}, //Mithril 4
			{1161, 1123, 1073}, //Adamant 5
			{1163, 1127, 1079}, //Rune 6
	};

	/**
	 * Set of all the animated Armour, with indexes corresponding with the
	 * indexes from the 2-d array above.
	 */
	private static final int[] ANIMATED_ARMOURS = {
			4278, // Animated Bronze Armour
			4279, // Animated Iron Armour
			4280, // Animated Steel Armour
			4281, // Animated Black Armour
			4282, // Animated Mithril Armour
			4283, // Animated Adamant Armour
			4284, // Animated Rune Armour
	};

	private static int getAmountForId(int id) {
		for(int i = 0; i < ANIMATED_ARMOURS.length; i++) {
			if(id == ANIMATED_ARMOURS[i]) {
				return 10 * (i + 1);
			}
		}
		return 0;
	}

	private static int[] getArmourIdsArray() {
		int[] armarray = new int[21];
		int count = 0;
		for(int i = 0; i < ARMOUR_SET.length; i++) {
			for(int j = 0; j < ARMOUR_SET[i].length; j++) {
				armarray[count++] = ARMOUR_SET[i][j];
			}
		}
		return armarray;
	}

	private static int[] getNeededItems(int id) {
		for(int i = 0; i < ARMOUR_SET.length; i++) {
			for(int j = 0; j < ARMOUR_SET[i].length; j++) {
				if(id == ARMOUR_SET[i][j]) {
					return ARMOUR_SET[i];
				}
			}
		}
		return null;
	}

	private static int getType(int id) {
		for(int i = 0; i < ARMOUR_SET.length; i++) {
			for(int j = 0; j < ARMOUR_SET[i].length; j++) {
				if(id == ARMOUR_SET[i][j]) {
					return i;
				}
			}
		}
		return - 1;
	}

	private static int[] getIdsForNpcId(int npcId) {
		for(int i = 0; i < ANIMATED_ARMOURS.length; i++) {
			if(ANIMATED_ARMOURS[i] == npcId) {
				return ARMOUR_SET[i];
			}
		}
		return null;
	}

	private static int getDefenderId(Player player) {
		if(hasDef(player, 13351))
			return 13351;
		else if(hasDef(player, 8850))
			return 13351;
		else if(hasDef(player, 8849))
			return 8850;
		else if (hasDef(player, 8848))
			return 8849;
		else if(hasDef(player, 8847))
			return 8848;
		else if(hasDef(player, 8846))
			return 8847;
		else if(hasDef(player, 8845))
			return 8846;
		else if(hasDef(player, 8844))
			return 8845;
		return 8844;
	}
	private static boolean hasDef(Player player, int id) {
		return player.getInventory().contains(id) || player.getEquipment().contains(id);
	}
	public static boolean enterCyclopsRoom(final Player p) {
		if(p.getPosition().getX() > 2846)
			return true;
		if(! (ContentEntity.getItemAmount(p, TOKENS) >= 100)) {
			p.getActionSender().sendMessage("You need at least 100 tokens to enter this room!");
			return false;
		}
		World.submit(new Task(60000,"warriorsguild") {
			public void execute() {
				if(inCyclopsRoom(p)) {
					if(ContentEntity.deleteItemA(p, TOKENS, 10)) {
						p.getActionSender().sendMessage("10 tokens disappear from your inventory.");
					} else {
						Magic.teleport(p, 2843, 3540, 2, true);
						p.getActionSender().sendMessage("You ran out of tokens!");
					}
				} else {
					this.stop();
				}
			}
		});
		return true;
	}

	/**
	 * @return Whether the Player is in the Cyclops Room or not
	 */
	public static boolean inCyclopsRoom(Player p) {
		if(p.getPosition().getZ() != 2)
			return false;
		if(p.getPosition().getX() >= 2838 && p.getPosition().getX() <= 2875) {
			if(p.getPosition().getY() >= 3543 && p.getPosition().getY() <= 3556) {
				return true;
			}
		}
		if(p.getPosition().getX() >= 2847 && p.getPosition().getX() <= 2875) {
			if(p.getPosition().getY() >= 3534 && p.getPosition().getY() <= 3542) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean clickObject(Player player, int type, int a, int x, int y,
	                           int d) {
		if(type == 6) {
			if(a == 1738 || a == 12554 || a == 3537)
				player.setTeleportTarget(Position.create(player.getPosition().getX(), player.getPosition().getY(), 2));
			else if(a == 15638)
				player.setTeleportTarget(Position.create(player.getPosition().getX(), player.getPosition().getY(), 0));
			else if(a == 15641 || a == 15644) {
				if(! enterCyclopsRoom(player)) {
					return true;
				}
			}
			return false;
		}
		if(type == 14 && y == 15621) {
			int[] needed = getNeededItems(a);
			for(int i = 0; i < needed.length; i++) {
				if(ContentEntity.getItemAmount(player, needed[i]) <= 0) {
					player.getActionSender().sendMessage("You need to have all armor pieces to summon a warrior!");
					return false;
				}
			}

			for(int i = 1; i <= World.npcs.size(); i++) {
				if(World.npcs.get(i) != null) {
					NPC npc = (NPC) World.npcs.get(i);
					if (npc.ownerId == player.getIndex() && player.cE.summonedNpc != npc) {
						npc.forceMessage("I'm not done with you " + player.getSafeDisplayName() + "!");
						return false;
					}
				}
			}

			for(int i = 0; i < needed.length; i++) {
				ContentEntity.deleteItem(player, needed[i]);
			}
			NPC n = spawnNpc(ANIMATED_ARMOURS[getType(a)], Position.create(2855, 3541, 0), player);
			n.forceMessage("I'm coming for you " + player.getSafeDisplayName() + "!");
		}
		if(type == 16) {
			if(a == CYCLOPSIDS[0] || a == CYCLOPSIDS[1]) {
				int r = 20;
				if(player.getEquipment().get(Equipment.SLOT_RING) != null)
					if(player.getEquipment().get(Equipment.SLOT_RING).getId() == 2572)
						r = 10;

				if(Misc.random(r) == 0) {
					GlobalItem defender = new GlobalItem(
							player, x, y, player.getPosition().getZ(),
							new Item(getDefenderId(player), 1)
					);
					GlobalItemManager.newDropItem(player, defender);
					if(player.WGLevel < 7) {
						player.WGLevel++;
					}
				}
			} else {
				GlobalItem[] DropItems = new GlobalItem[4];
				int[] ArmourIds = getIdsForNpcId(a);
				DropItems[0] = new GlobalItem(
						player, x, y, player.getPosition().getZ(),
						new Item(TOKENS, getAmountForId(a)));

				for(int i = 1; i < 4; i++) {
					DropItems[i] = new GlobalItem(
							player, x, y, player.getPosition().getZ(),
							new Item(ArmourIds[i - 1], 1));
				}
				for (GlobalItem DropItem : DropItems) {
					GlobalItemManager.newDropItem(player, DropItem);
				}
			}
		}
		return false;
	}

	public NPC spawnNpc(int npcId, Position position, Player player) {
		NPC npc = NPCManager.addNPC(position.getX(), position.getY(), position.getZ(), npcId, - 1);
		npc.agressiveDis = 10;
		npc.ownerId = player.getIndex();
		return npc;
	}

	@Override
	public void init() throws FileNotFoundException {

	}

	@Override
	public int[] getValues(int type) {
		if(type == 16) {
			return Misc.mergeArrays(CYCLOPSIDS, ANIMATED_ARMOURS);
		}
		if(type == 6) {
			int[] a1 = {1738, 15638, 15641, 15644};
			return a1;
		}
		if(type == 14) {
			return getArmourIdsArray();
		}
		return null;
	}

}