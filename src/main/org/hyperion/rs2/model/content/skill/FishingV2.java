package org.hyperion.rs2.model.content.skill;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class FishingV2 implements ContentTemplate {

	private static final int EXPMULTIPLIER = Constants.XPRATE * 2;

	private void fishType(final Player player, final FishingType type, final int x, final int y) {

		if(type == null || player.getExtraData().get("fishing") != null)
			return;

		final int minLevel = type.fishLevels[0];
		if(ContentEntity.returnSkillLevel(player, 10) < minLevel) {
			ContentEntity.sendMessage(player,
					"You need " + minLevel + " fishing to fish here.");
			return;
		}
		if(! ContentEntity.isItemInBag(player, type.itemId)) {
			ContentEntity.sendMessage(player, "You need a " + ItemDefinition.forId(type.itemId).getName() + " to fish here.");
			return;
		}
		if(ContentEntity.freeSlots(player) == 0) {
			ContentEntity.sendMessage(player,
					"There is not enough space in your inventory.");
			return;
		}
		ContentEntity.startAnimation(player, type.animationId);
		ContentEntity.sendMessage(player, "You start to fish.");
		player.getExtraData().put("fishing", 0);
		int FISHING_TIMER = 30 - ((ContentEntity.returnSkillLevel(player, 10) - type.fishLevels[0]) / 2);
		if(FISHING_TIMER < 6)
			FISHING_TIMER = 6;
		player.setBusy(true);
		World.submit(new Task(FISHING_TIMER * 50,"fishingV2") {
			int cycle = 10;

			@Override
			public void execute() {
				if(player.getExtraData().get("fishing") == null) {
					stop2();
					return;
				}
				if(! player.isBusy()) {
					stop2();
					return;
				}
				if(! ContentEntity.isItemInBag(player, type.itemId)) {
					ContentEntity.sendMessage(player, "You need a " + ItemDefinition.forId(type.itemId).getName() + " to fish here.");
					stop2();
					return;
				}
				if(ContentEntity.freeSlots(player) == 0) {
					ContentEntity.sendMessage(player,
							"There is not enough space in your inventory.");
					stop2();
					return;
				}
				if(type.baitId > 1) {
					if(! ContentEntity.isItemInBag(player, type.baitId)) {
						ContentEntity.sendMessage(player, "You need more " + ItemDefinition.forId(type.baitId).getName() + " to fish here.");
						stop2();
						return;
					}
					ContentEntity.deleteItemA(player, type.baitId, 1);
				}
				player.setBusy(true);
				if(-- cycle == 0) {
					cycle = 10;
					for(int i = type.fishLevels.length - 1; i >= 0; i--) {
						if((ContentEntity.returnSkillLevel(player, 10) < type.fishLevels[i])) {
							continue;
						}
						if(minLevel == type.fishLevels[i] || Combat.random(100) + type.fishLevels[i] <= ContentEntity.returnSkillLevel(player, 10)) {
							player.getAchievementTracker().itemSkilled(Skills.FISHING, type.fishIds[i], 1);
							ContentEntity.addItem(player, type.fishIds[i], 1);
							ContentEntity.addSkillXP(player, type.fishXp[i] * EXPMULTIPLIER, 10);
							ContentEntity.startAnimation(player, type.animationId);
							ContentEntity.turnTo(player, x, y);
							ContentEntity.sendMessage(player, "You catch a " + ItemDefinition.forId(type.fishIds[i]).getName());
							break;
						}
					}
					if(player.getRandomEvent().skillAction(6)) {
						stop2();
						return;
					}
				} else
					ContentEntity.startAnimation(player, type.animationId);
			}

			public void stop2() {
				ContentEntity.startAnimation(player, - 1);
				player.setBusy(false);
				player.getExtraData().remove("fishing");
				this.stop();
			}

		});
	}


	public enum FishingType {
		SMALL_NET(303, - 1,
				new int[]{316, 320, 319, 323, 325, 327, 326, 332, 330},
				new int[]{- 1,},
				new int[]{317, 321, 7944,/*fish ids*/},
				new int[]{1, 15, 62,/*fish levels*/},
				new int[]{10, 40, 120,/*fish xps*/},
				621
		),
		BAIT_ROD(307, 313,
				new int[]{- 1},
				new int[]{316, 320, 319, 323, 325, 327, 326, 332, 330},
				new int[]{327, 345,/*fish ids*/},
				new int[]{5, 10,/*fish levels*/},
				new int[]{20, 30,/*fish xps*/},
				622
		),
		FLY_ROD(309, 314,
				new int[]{309, 310, 311, 314, 315, 317, 318, 328, 329, 331},
				new int[]{- 1},
				new int[]{335, 331,/*fish ids*/},
				new int[]{20, 30,/*fish levels*/},
				new int[]{50, 70,/*fish xps*/},
				622
		),
		LOBSTER_POT(301, - 1,
				new int[]{324, 312},
				new int[]{- 1,},
				new int[]{377,},
				new int[]{40,},
				new int[]{90,},
				619
		),
		BIGNET(305, - 1,
				new int[]{334,},
				new int[]{- 1,},
				new int[]{389, 392,/*fish ids*/},
				new int[]{85, 90,/*fish levels*/},
				new int[]{115, 120,/*fish xps*/},
				620
		),
		HARPOON(311, - 1,
				new int[]{- 1,},
				new int[]{313, 322, 312, 324,},
				new int[]{359, 371,/*fish ids*/},
				new int[]{35, 50,/*fish levels*/},
				new int[]{80, 100,/*fish xps*/},
				618
		),
		HARPOON_SHARK(311, - 1,
				new int[]{- 1,},
				new int[]{334,},
				new int[]{383,/*fish ids*/},
				new int[]{76,/*fish levels*/},
				new int[]{110,/*fish xps*/},
				618
		);

		FishingType(int itemId, int bait, int[] spotIds1, int[] spotIds2, int[] fishIds, int[] fishLevels, int[] fishXp, int animationId) {
			this.itemId = itemId;
			this.baitId = bait;
			this.animationId = animationId;
			this.spotIds1 = spotIds1;
			this.spotIds2 = spotIds2;
			this.fishIds = fishIds;
			this.fishLevels = fishLevels;
			this.fishXp = fishXp;
		}

		public int itemId;
		public int baitId;
		public int animationId;
		public int[] spotIds1;
		public int[] spotIds2;
		public int[] fishIds;
		public int[] fishLevels;
		public int[] fishXp;
	}


	@Override
	public boolean clickObject(Player player, int type, int npc, int x, int y,
	                           int d) {
		//System.out.println("fish2 "+type);
		if(type == 11)
			fishType(player, secondClick.get(npc), x, y);
		if(type == 10)
			fishType(player, firstClick.get(npc), x, y);
		return true;
	}

	private Map<Integer, FishingType> firstClick = new HashMap<Integer, FishingType>();
	private Map<Integer, FishingType> secondClick = new HashMap<Integer, FishingType>();

	@Override
	public void init() throws FileNotFoundException {
		// TODO Auto-generated method stub
		for(FishingType fishType : FishingType.values()) {
			for(int i = 0; i < fishType.spotIds1.length; i++)
				firstClick.put(fishType.spotIds1[i], fishType);
			for(int i = 0; i < fishType.spotIds2.length; i++)
				secondClick.put(fishType.spotIds2[i], fishType);
		}
	}

    public static final int[] FISHING_SPOTS = {316, 320, 319, 323,
            325, 327, 326, 332, 330, 309, 310, 311, 314,
            315, 317, 318, 328, 329, 331, 334, 312, 324,
            309, 314, 301, 311,};

	@Override
	public int[] getValues(int type) {
		if(type == 11 || type == 10) {
			return FISHING_SPOTS;
		}
		return null;
	}

}
