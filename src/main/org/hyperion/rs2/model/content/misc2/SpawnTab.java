package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.packet.ActionsManager;
import org.hyperion.rs2.packet.ButtonAction;
import org.hyperion.util.Misc;

public class SpawnTab {

	private Player player;
	public final static int START_INDEX = 31412;
	private int max_index = 0;

	private int getId(int index) {
		if (index > max_index)
			max_index = index;
		index += START_INDEX;
		return index;
	}

	private int getNextIndex() {
		max_index++;
		return max_index;
	}

	public SpawnTab(Player player) {
		this.player = player;
	}

	public void createSpawnTab() {
		resetSpawnTab();
	}

	public void resetSpawnTab() {
		player.getActionSender().sendString(getId(0), "@yel@Supplies");
		player.getActionSender().sendFont(getId(0), 2);
		player.getActionSender().sendString(getId(8), "@yel@Instant sets");
		player.getActionSender().sendFont(getId(8), 2);
		updateSpawnTab();
	}

	public void updateSpawnTab() {
		sendSupplies();
		sendInstantSets();
	}

	public void sendSupplies() {
		max_index = 0;
		for(Supplies supplies : Supplies.values()) {
			player.getActionSender().sendString(getId(getNextIndex()), "@or1@" + supplies.toString());
			player.getActionSender().sendTooltip(getId(max_index), supplies.toString());
		}
	}

	public void sendInstantSets() {
		player.getActionSender().sendString(getId(9), "@or1@Dharoks");
		player.getActionSender().sendTooltip(getId(9), "Dharoks");
		player.getActionSender().sendString(getId(10), "@or1@Ranged");
		player.getActionSender().sendTooltip(getId(10), "Ranged");
		player.getActionSender().sendString(getId(11), "@or1@Hybrid");
		player.getActionSender().sendTooltip(getId(11), "Hybrid");
		player.getActionSender().sendString(getId(12), "@or1@Pure set");
		player.getActionSender().sendTooltip(getId(12), "Pure set");
		player.getActionSender().sendString(getId(13), "@or1@Zerk set");
		player.getActionSender().sendTooltip(getId(13), "Zerk set");
		player.getActionSender().sendString(getId(14), "@or1@Welfare hybrid set");
		player.getActionSender().sendTooltip(getId(14), "Welfare hybrid set");
		for(Sets sets : Sets.values()) {
			player.getActionSender().sendString(getId(getNextIndex()), "@or1@" + sets.toString());
			player.getActionSender().sendTooltip(getId(max_index), sets.toString());
		}
		player.getActionSender().sendScrollbarLength(31411, (16 * 2) + (12 * 18) + 45);
	}

	public enum Sets {
		MELEE_SET(
				Item.create(4151, 1),
				Item.create(7462, 1),
				Item.create(11732, 1),
				Item.create(6585, 1),
				Item.create(4728, 1),
				Item.create(4730, 1),
				Item.create(6524, 1),
				Item.create(10828, 1)
		),
		MAGE_SET(
				Item.create(4708, 1),
				Item.create(4712, 1),
				Item.create(4714, 1),
				Item.create(4675, 1),
				Item.create(6889, 1),
				Item.create(6920, 1),
				Item.create(1052, 1),
				Item.create(1708, 1)
		),
		RANGE_SET(
				Item.create(2581, 1),
				Item.create(2577, 1),
				Item.create(15126, 1),
				Item.create(4736, 1),
				Item.create(4738, 1),
				Item.create(10499, 1),
				Item.create(9244, 100),
				Item.create(9185, 1),
				Item.create(7462, 1)
		),
		HYBRID_SET(
				Item.create(4675, 1),
				Item.create(4151, 1),
				Item.create(6585, 1),
				Item.create(7462, 1),
				Item.create(6920, 1),
				Item.create(4712, 1),
				Item.create(4714, 1),
				Item.create(4728, 1),
				Item.create(4730, 1),
				Item.create(1052, 1),
				Item.create(10828, 1),
				Item.create(1215, 1)
		);

		@Override
		public String toString() {
			return Misc.ucFirst(name().replace("_", " ").toLowerCase());
		}

		private Item[] items;

		Sets(Item... items) {
			this.items = items;
		}
	}

	public enum Supplies {
		SHARKS(Item.create(386, 1000)),
		SUPER_RESTORES(Item.create(3025, 1000)),
		RANGING_POTIONS(Item.create(2445, 1000)),
		BARRAGE_RUNES(Item.create(555, 1000), Item.create(560, 1000), Item.create(565, 1000)),
		VENGEANCE_RUNES(Item.create(557, 1000), Item.create(9075, 1000), Item.create(560, 1000)),
		SUPER_SET(Item.create(2440, 1), Item.create(2442, 1), Item.create(2436, 1));

		@Override
		public String toString() {
			return Misc.ucFirst(name().replaceAll("_", " ").toLowerCase());
		}

		private Item[] items;

		Supplies(Item... items) {
			this.items = items;
		}
	}

	static {
		int index = 0;
		for(Supplies supplies : Supplies.values()) {
			ActionsManager.getManager().submit(START_INDEX + index + 1, new ButtonAction() {
				@Override
				public boolean handle(Player player, int id) {
					if(!Rank.hasAbility(player, Rank.DEVELOPER) && !ItemSpawning.canSpawn(player, true))
						return false;
					for(Item item : supplies.items) {
						player.getInventory().add(item);
					}
					return true;
				}
			});
			index++;
		}
		index += 8;
		for(Sets sets : Sets.values()) {
			ActionsManager.getManager().submit(START_INDEX + index + 1, new ButtonAction() {
				@Override
				public boolean handle(Player player, int id) {
					if(!Rank.hasAbility(player, Rank.DEVELOPER) && !ItemSpawning.canSpawn(player, true))
						return false;
					for(Item item : sets.items) {
						player.getInventory().add(item);
					}
					return true;
				}
			});
			index++;
		}
	}
}