package org.hyperion.rs2.model.content.skill;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;


//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

/**
 * Woodcutting skill handler
 *
 * @author Graham
 */
public class Woodcutting implements ContentTemplate {

	private static final int EXPMULTIPLIER = Constants.XPRATE * 5;
	/**
	 * A huge collection of constants here.
	 */
	private static final int[] NORMAL_TREES = {1276, 1277, 1278, 1279, 1280,
			1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318,
			1319, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035,
			3036, 3881, 3882, 3883, 5902, 5903, 5904};
	private static final int[] WILLOW_TREES = {1308, 5551, 5552, 5553};
	private static final int[] OAK_TREES = {1281, 3037};
	private static final int[] MAGIC_TREES = {1292, 1306};
	private static final int[] MAPLE_TREES = {1307, 4677};
	private static final int[] MAHOGANY_TREES = {9034};
	private static final int[] TEAK_TREES = {9036};
	private static final int[] ACHEY_TREES = {2023};
	private static final int[] YEW_TREES = {1309};
	private static final int[] AXES = {1359, 1357, 1355, 1353, 1349/*iron*/, 1351/*bronze*/, 6739,};
	private static final int[] AXES_REQUIRED_LEVElS = {41, 31, 21, 6, 1, 1, 60};
	private static final int[] AXES_ANIMATIONS = {867, 869, 871, 875, 877, 879, 2846};
	private static final int WOODCUTTING_DELAY = 3000;
	protected static final double WOODCUTTING_FACTOR = 0.5;
	// TODO different IDs for various trees?
	private static final int TREE_STUMP = 1341;
	private static final int TREE_RESPAWN_TIME = 10000;

	private static boolean cutVine(final Player client, final int object, final int x, final int y) {
		if(client.isBusy()) {
			return true;
		}
		final int axe = hasAxe(client);
		if(axe == - 1) {
			client.getActionSender().sendMessage(
					"You do not have an axe that you can use.");
			return true;
		}
		if(ContentEntity.returnSkillLevel(client, Skills.WOODCUTTING) < 27) {
			client.getActionSender().sendMessage(
					"You need 27 woodcutting to cut vines.");
			return true;
		}
		client.getActionSender().sendMessage("You swing your axe at the vines...");
		ContentEntity.startAnimation(client, AXES_ANIMATIONS[axe]);
		client.inAction = true;
		ContentEntity.turnTo(client, x, y);

		World.submit(new Task(2000,"woodcutting1") {

			@Override
			public void execute() {

				int walktoX = x;
				int walktoY = y;
				if(client.getPosition().getX() < x)
					walktoX = x + 1;
				else if(client.getPosition().getX() > x)
					walktoX = x - 1;
				if(client.getPosition().getY() < y)
					walktoY = y + 1;
				else if(client.getPosition().getY() > y)
					walktoY = y - 1;
				// TODO Auto-generated method stub
				client.getWalkingQueue().reset();
				client.getWalkingQueue().addStep(x, y);
				client.getWalkingQueue().addStep(walktoX, walktoY);
				client.getWalkingQueue().finish();
				ObjectManager.update(new GameObject(GameObjectDefinition.forId(6951), Position.create(x, y, client.getPosition().getZ()), 10, 0));
				ContentEntity.startAnimation(client, - 1);
				this.stop();
			}

		});
		World.submit(new Task(3500,"woodcutting2") {

			@Override
			public void execute() {
				client.getWalkingQueue().reset();
				ObjectManager.update(new GameObject(GameObjectDefinition.forId(object), Position.create(x, y, client.getPosition().getZ()), 10, 0));
				this.stop();
			}

		});
		return true;
	}

	/**
	 * Click object hook.
	 *
	 * @param client
	 * @param object
	 * @param x
	 * @param y
	 * @return
	 */
	public static final boolean cut(final Player client, final int object,
	                                final int x, final int y) {
		if(! isTree(object))
			return false;
		if(ContentEntity.freeSlots(client) == 0) {
			client.getActionSender().sendMessage(
					"There is not enough space in your inventory.");
			return true;
		}
		if(client.isBusy() || client.getExtraData().get("woodcuttingtimer") != null) {
			return true;
		}
		final int axe = hasAxe(client);
		if(axe == - 1) {
			client.getActionSender().sendMessage(
					"You do not have an axe that you can use.");
			return true;
		}
		final int level = getTreeLevel(object);
		if(! (client.getSkills().getLevel(8) >= level)) {
			client.getActionSender().sendMessage("You do not have the required level to cut down that tree.");
			return true;
		}
		client.setBusy(true);
		int numberOfCycles = (int) Math.random() * 10 + 15;
		if(contains(NORMAL_TREES, object)) {
			numberOfCycles = 1;
		}
		client.getExtraData().put("woodcuttingtimer", 0);
		client.getActionSender().sendMessage("You swing your axe at the tree...");
		ContentEntity.startAnimation(client, AXES_ANIMATIONS[axe]);
		client.inAction = true;
		ContentEntity.turnTo(client, x, y);
		final int fNumberOfCycles = numberOfCycles;
		World.submit(new Task(WOODCUTTING_DELAY,"woodcutting delay") {
			public int cycle = - 1;

			@Override
			public void execute() {
				// tree cut down by another player
				/*if(ObjectManager.getObjectAt(x, y,
						client.getLocation().getZ()) == null) {
					stop2();
					return;
				}*/
				if(! client.isBusy()) {
					stop2();
					return;
				}
				ContentEntity.turnTo(client, x, y);

				if(ContentEntity.freeSlots(client) == 0) {
					client.getActionSender().sendMessage(
							"There is not enough space in your inventory.");
					stop2();
					return;
				}
				if(client.getRandomEvent().skillAction(6)) {

					stop2();
					return;
				}

				if(cycle == - 1) {
					cycle = fNumberOfCycles;
				}

				if(fNumberOfCycles == 1 || Math.random() > WOODCUTTING_FACTOR) {
					int xp = 0;
					if(Misc.random(50) == 5)
						ContentEntity.addItem(client, 6693, 1);
					if(contains(NORMAL_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 1511, 1);
						xp = 50;
						ContentEntity.addItem(client, 1511, 1);
						client.getActionSender().sendMessage(
								"You get some logs.");
					} else if(contains(WILLOW_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 1519, 1);
						xp = 135;
						ContentEntity.addItem(client, 1519, 1);
						client.getActionSender().sendMessage(
								"You get some willow logs.");
					} else if(contains(OAK_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 1521, 1);
						xp = 75;
						ContentEntity.addItem(client, 1521, 1);
						client.getActionSender().sendMessage(
								"You get some oak logs.");
					} else if(contains(MAGIC_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 1513, 1);
						xp = 500;
						ContentEntity.addItem(client, 1513, 1);
						client.getActionSender().sendMessage(
								"You get some magic logs.");
					} else if(contains(MAPLE_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 1517, 1);
						xp = 200;
						ContentEntity.addItem(client, 1517, 1);
						client.getActionSender().sendMessage(
								"You get some maple logs.");
					} else if(contains(MAHOGANY_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 6332, 1);
						xp = 250;
						ContentEntity.addItem(client, 6332, 1);
						client.getActionSender().sendMessage(
								"You get some mahogany logs.");
					} else if(contains(TEAK_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 6333, 1);
						xp = 170;
						ContentEntity.addItem(client, 6333, 1);
						client.getActionSender().sendMessage(
								"You get some tweak logs.");
					} else if(contains(ACHEY_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 2862, 1);
						xp = 50;
						ContentEntity.addItem(client, 2862, 1);
						client.getActionSender().sendMessage(
								"You get some achey logs.");
					} else if(contains(YEW_TREES, object)) {
						client.getAchievementTracker().itemSkilled(Skills.WOODCUTTING, 1515, 1);
						xp = 350;
						ContentEntity.addItem(client, 1515, 1);
						client.getActionSender().sendMessage(
								"You get some yew logs.");
					}
					// add xp
					ContentEntity.addSkillXP(client, xp * EXPMULTIPLIER,
							8);
				}

				if(cycle != 1) {
					ContentEntity.startAnimation(client,
							AXES_ANIMATIONS[axe]);
					client.inAction = true;
				}

				cycle--;
				if(cycle == 0) {
					client.setBusy(false);
					stop2();
					if(Edgeville.POSITION.distance(Position.create(x, y, 0)) > 50 && Position.create(3370, 3240, 0).distance(Position.create(x, y, 0)) > 70) {
						final GameObject stump = new GameObject(GameObjectDefinition.forId(TREE_STUMP), Position.create(x, y, client.getPosition().getZ()), 10, 0);
						final GameObject tree = new GameObject(GameObjectDefinition.forId(object), Position.create(x, y, client.getPosition().getZ()), 10, 0);
						ObjectManager.addObject(stump);
						World.submit(new Task(TREE_RESPAWN_TIME,"tree respawn time") {

							@Override
							public void execute() {
								ObjectManager.replace(stump, tree);
								ObjectManager.removeObject(stump);
								this.stop();
							}

						});
					}
				}
			}


			public void stop2() {
				client.getExtraData().remove("woodcuttingtimer");
				client.setBusy(false);
				ContentEntity.startAnimation(client, - 1);
				client.inAction = false;
				this.stop();
			}
		});
		return true;
	}

	public static int getTreeLevel(int id) {
		int level = 1;
		if(contains(NORMAL_TREES, id)) {
			level = 1;
		} else if(contains(OAK_TREES, id)) {
			level = 15;
		} else if(contains(WILLOW_TREES, id)) {
			level = 30;
		} else if(contains(MAGIC_TREES, id)) {
			level = 75;
		} else if(contains(MAPLE_TREES, id)) {
			level = 45;
		} else if(contains(MAHOGANY_TREES, id)) {
			level = 50;
		} else if(contains(TEAK_TREES, id)) {
			level = 35;
		} else if(contains(ACHEY_TREES, id)) {
			level = 1;
		} else if(contains(YEW_TREES, id)) {
			level = 60;
		}
		return level;
	}

	/**
	 * Axe check.
	 *
	 * @param client
	 * @return
	 */
	private static final int hasAxe(Player client) {
		int ct = 0;
		int level = client.getSkills().getLevel(8);
		for(int id : AXES) {
			if(level >= AXES_REQUIRED_LEVElS[ct]) {
				if(client.getEquipment().get(3) != null && client.getEquipment().get(3).getId() == id) {
					return ct;
				} else if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
			}
			ct++;
		}
		return - 1;
	}

	/**
	 * Is tree?
	 *
	 * @param id
	 * @return
	 */
	public static boolean isTree(int id) {
		return contains(NORMAL_TREES, id) || contains(OAK_TREES, id)
				|| contains(WILLOW_TREES, id) || contains(MAGIC_TREES, id)
				|| contains(MAPLE_TREES, id) || contains(MAHOGANY_TREES, id)
				|| contains(TEAK_TREES, id) || contains(ACHEY_TREES, id)
				|| contains(YEW_TREES, id);
	}

	/**
	 * Should really be moved to a utility class!
	 *
	 * @param array
	 * @param value
	 * @return
	 */
	public static boolean contains(int[] array, int value) {
		for(int i : array) {
			if(i == value) {
				return true;
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
			int[] j = {/*vines*/5103, 5104, 5105, 5106, 5107,/*trees*/1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318, 1319, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882, 3883, 5902, 5903, 5904, 1308, 5551, 5552, 5553, 1281, 3037, 1292, 1306, 1307, 4677, 9034, 9036, 2023, 1309,};
			return j;
		}
		return null;
	}

	@Override
	public boolean clickObject(final Player client, final int type, final int id, final int slot, final int itemId2, final int npcSlot) {
		if(id >= 5103 && id <= 5107)
			return cutVine(client, id, slot, itemId2);
		return cut(client, id, slot, itemId2);
	}

}
