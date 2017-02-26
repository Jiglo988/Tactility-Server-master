package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.shops.DonatorShop;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MysteryBox implements ContentTemplate {

	public static void main(String... args) throws Exception{
//        ItemDefinition.init();

        final Map<Integer, Integer> map = new HashMap<>();
        final int totalMoney = 100_000_000;
		int money = totalMoney;
        int moneyMade = 0;
		int counter = 0;
		while(money > 99) {
			money -= 99;
			counter++;
			Item reward = generateRewardItem();
			int price = DonatorShop.getPrice(reward.getId());
            map.put(reward.getId(), map.getOrDefault(reward.getId(), 0) + 1);
			moneyMade += price;

		}
		System.out.printf("Boxes opened: %,d DP Made: %,d DP Lost: %,d Difference: %,d\n\n", counter, moneyMade, totalMoney, (totalMoney - moneyMade));
        final int total = map.values().stream().mapToInt(Integer::valueOf).sum();
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.printf("[%d] %s - Collected: %d Percent: %.2f%%\n", entry.getKey(), ItemDefinition.forId(entry.getKey()).getName(), entry.getValue(), (double)entry.getValue()/total * 100D);
        }

	}

	public static final int ID = 18768;

	public static void openBox(Player player, int i) {
		if(player.getInventory().getCount(ID) <= 0)
			return;
		if(player.getInventory().remove(new Item(i)) > 0) {
			player.getAchievementTracker().itemOpened(ID);
			addReward(player);
		}
	}

	private static Item generateRewardItem() {
		Item rewardItem = donatorReward();
		if(rewardItem == null) {
			rewardItem = new Item(CHEAP_ITEMS[Misc.random(CHEAP_ITEMS.length - 1)], 1);
		}
		return rewardItem;
	}

	private static void addReward(Player player) {
		Item rewardItem = donatorReward();
		if(rewardItem == null) {
			rewardItem = new Item(CHEAP_ITEMS[Misc.random(CHEAP_ITEMS.length - 1)], 1);
		}
		player.getInventory().add(rewardItem);
	}

	private static Item donatorReward() {
		int r = Misc.random(2);
		switch(r) {
			case 0:
				if(random(130) == 0) {
					return new Item(16425, 1);
				}
                if(random(22) == 0)
                    return new Item(14484, 1);
                if(random(3500) == 0)
                    return new Item(1042);
				break;
			case 1:
				if(random(4) == 0)
					return new Item(CHEAP_RARES[Misc.random(CHEAP_RARES.length - 1)], 1);
				break;
			case 2:
				if(random(12) == 0)
					return new Item(CHAOTIC_ITEMS[Misc.random(CHAOTIC_ITEMS.length - 1)], 1);
				break;
		}
		return null;
	}

	private static int random(int range) {
		return Misc.random((int) (range * 1.5));
	}


	public static int getValue(int id) {
		switch(id) {
			case 1050:
			case 1053:
			case 1055:
			case 1057:
				return 50099;
			case 18351:
			case 18349:
			case 18353:
			case 18355:
			case 18359:
			case 18357:
				return 1199;
			case 14484:
				return 1499;
		}
		return 0;
	}

	private static final int[] CHEAP_RARES = {
			13736, 13744, 18335, 13734, 13738
	};

	private static final int[] CHAOTIC_ITEMS = {
			18351, 18349, 18353, 18355, 18359, 18357,
	};

	/**
	 * Non-donator items that still have some kind of value.
	 */
	private final static int[] CHEAP_ITEMS = {14876, 14878, 14879, 14881, 14887, 14880,
			14888, 14885, 14889,// Stats
			13887, 13893, 13889, 13905, 13884, 13890, 13896, 13902, 13870,// PvP
			13858, 13861, 13864, 13867, 13873, 13876,
	};


	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c, int d) {
		if(type == 1)
			openBox(player, a);
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
	}

	@Override
	public int[] getValues(int type) {
		if(type == 1) {
			int[] ids = {ID};
			return ids;
		}
		return null;
	}

}
