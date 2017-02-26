package org.hyperion.rs2.model.content.skill;

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

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

/**
 * Fishing skill handler
 *
 * @author Ventrillo
 */
public class Fishing implements ContentTemplate {

	public Fishing() {
	}

	private static final int EXPMULTIPLIER = Constants.XPRATE;

	private static final int FISHING_DELAY = 3000;
	private static final double FISHING_FACTOR = 0.5;

	public static class FishingFirstClick {

		public FishingFirstClick() {
		}

		/**
		 * A huge collection of constants here.
		 */
		private final int[] FISHING_SPOT_NET = {316, 320, 319, 323,
				325, 327, 326, 332, 330};
		private final int[] FISHING_SPOT_FLY = {309, 310, 311, 314,
				315, 317, 318, 328, 329, 331};
		private final int[] FISHING_SPOT_LOBSTER = {334, 312, 324};
		private final int[] FISHING_SPOT_TUNA = {};

		private final int[] FISHING_NET = {303};
		@SuppressWarnings("unused")
		private final int[] BIG_FISHING_NET = {305};
		private final int[] FLY_ROD = {309};
		private final int[] FEATHER = {314};
		private final int[] LOBSTER_POT = {301};
		private final int[] HARPOON = {311};

		/**
		 * Click object hook.
		 *
		 * @param client
		 * @param object
		 * @param x
		 * @param y
		 * @return
		 */
		public final boolean clickObject(final Player client,
		                                 final int object, final int x, final int y) {

			if(! isFishNet(object) && ! isFishLobster(object)
					&& ! isFishFly(object) && ! isFishTuna(object)) {
				return false;
			}

			if(client.isBusy() || client.getExtraData().get("fishing") != null) {
				return true;
			}
			client.getExtraData().put("fishing", 1);
			client.setBusy(true);

			if(isFishNet(object)) {
				final int net = hasNet(client);
				if(net == - 1) {
					ContentEntity.sendMessage(client,
							"You need a net to fish here.");
					client.setBusy(false);
					client.getExtraData().remove("fishing");
					return true;
				}
			} else if(isFishFly(object)) {
				final int fly = hasFly(client);
				if(fly == - 1) {
					ContentEntity.sendMessage(client,
							"You need a fly fishing rod to fish here.");
					client.setBusy(false);
					client.getExtraData().remove("fishing");
					return true;
				}
			} else if(isFishFly(object)) {
				final int feather = hasFeather(client);
				if(feather == - 1) {
					ContentEntity.sendMessage(client,
							"You need feathers to fish here.");
					client.setBusy(false);
					client.getExtraData().remove("fishing");
					return true;
				}
			} else if(isFishLobster(object)) {
				final int cage = hasCage(client);
				if(cage == - 1) {
					ContentEntity.sendMessage(client,
							"You need a lobster cage to fish here.");
					client.setBusy(false);
					client.getExtraData().remove("fishing");
					return true;
				}
			} else if(isFishTuna(object)) {
				final int harpoon = hasHarpoon(client);
				if(harpoon == - 1) {
					ContentEntity.sendMessage(client,
							"You need a harpoon to fish here.");
					client.setBusy(false);
					client.getExtraData().remove("fishing");
					return true;
				}
			} else {

				return false;
			}

			if(ContentEntity.freeSlots(client) == 0) {
				ContentEntity.sendMessage(client,
						"There is not enough space in your inventory.");
				client.getExtraData().remove("fishing");
				return true;
			}

			final int level = getFishLevel(object);
			if(! (ContentEntity.returnSkillLevel(client, 10) >= level)) {
				ContentEntity.sendMessage(client,
						"You do not have the required level to fish that.");
				client.getExtraData().remove("fishing");
				return true;
			}

			int animationID = 0;
			if(contains(FISHING_SPOT_NET, object)) {
				animationID = 621;
			} else if(contains(FISHING_SPOT_LOBSTER, object)) {
				animationID = 619;
			} else if(contains(FISHING_SPOT_TUNA, object)) {
				animationID = 618;
			} else if(contains(FISHING_SPOT_FLY, object)) {
				animationID = 622;
			}
			ContentEntity.startAnimation(client, animationID);
			client.setBusy(true);
			ContentEntity.sendMessage(client, "You start to fish...");

			ContentEntity.turnTo(client, x, y);
			final int fNumberOfCycles = (int) Math.random() * 5 + 5;

			World.submit(new Task(FISHING_DELAY,"fishing1") {
				public int cycle = - 1;

				@Override
				public void execute() {
					ContentEntity.turnTo(client, x, y);
					if(! client.isBusy()) {
						stop2();
						return;
					}
					if(ContentEntity.freeSlots(client) == 0) {
						ContentEntity.sendMessage(client,
								"There is not enough space in your inventory.");
						stop2();
						return;
					}

					if(cycle == - 1) {
						cycle = fNumberOfCycles;
					}

					final int FEATHERSLOT = getFeatherSlot(client);

					if(cycle == 1 || Math.random() > FISHING_FACTOR) {
						int xp = 0;
						if(contains(FISHING_SPOT_NET, object)) {
							xp = 10;
							if(ContentEntity.returnSkillLevel(client, 10) >= 5 && Combat.random(100) + 5 <= ContentEntity.returnSkillLevel(client, 10)) {
								ContentEntity.addItem(client, 321, 1);
								ContentEntity.sendMessage(client,
										"You catch some anchovies.");
							} else {
								ContentEntity.addItem(client, 317, 1);
								ContentEntity.sendMessage(client,
										"You catch some shrimp.");
							}
						} else if(contains(FISHING_SPOT_LOBSTER, object)) {
							if(ContentEntity.returnSkillLevel(client, 10) >= 62 && Combat.random(100) + 62 <= ContentEntity.returnSkillLevel(client, 10)) {
								xp = 110;
								ContentEntity.addItem(client, 7944, 1);
								ContentEntity.sendMessage(client,
										"You catch a Monkfish.");
							} else {
								xp = 100;
								ContentEntity.addItem(client, 377, 1);
								ContentEntity.sendMessage(client,
										"You catch a lobster.");
							}
						} else if(contains(FISHING_SPOT_TUNA, object)) {
							xp = 80;
							ContentEntity.addItem(client, 359, 1);
							ContentEntity.sendMessage(client,
									"You catch a tuna.");
						} else if(contains(FISHING_SPOT_FLY, object)) {
							if(ContentEntity.isItemInBag(client,
									FEATHER[0])) {
								if(ContentEntity.returnSkillLevel(client, 10) >= 30) {
									if(Math.random() < 0.45) {
										xp = 70;
										ContentEntity.addItem(client,
												331, 1);
										ContentEntity
												.sendMessage(client,
														"You catch a salmon.");
										ContentEntity.deleteItem(client,
												314, FEATHERSLOT, 1);
									} else {
										xp = 50;
										ContentEntity.addItem(client,
												335, 1);
										ContentEntity
												.sendMessage(client,
														"You catch a trout.");
										ContentEntity.deleteItem(client,
												314, FEATHERSLOT, 1);
									}
								} else if(ContentEntity.returnSkillLevel(client, 10) < 30) {
									xp = 50;
									ContentEntity.addItem(client, 335, 1);
									ContentEntity.sendMessage(client,
											"You catch a trout.");
									ContentEntity.deleteItem(client, 314,
											FEATHERSLOT, 1);
								}
							} else {
								ContentEntity.sendMessage(client,
										"You need a feather to fish here.");
								stop2();
							}
						}
						// add xp
						ContentEntity.addSkillXP(client, xp * EXPMULTIPLIER,
								10);
					}

					int animationID = 0;
					if(contains(FISHING_SPOT_NET, object)) {
						animationID = 621;
					} else if(contains(FISHING_SPOT_LOBSTER, object)) {
						animationID = 619;
					} else if(contains(FISHING_SPOT_TUNA, object)) {
						animationID = 618;
					} else if(contains(FISHING_SPOT_FLY, object)) {
						animationID = 622;
					}
					ContentEntity.startAnimation(client, animationID);

					cycle--;
	                /*if (cycle == 0) {
                        stop2();
					}*/
				}


				public void stop2() {
					client.setBusy(false);
					ContentEntity.startAnimation(client, - 1);
					client.getExtraData().remove("fishing");
					this.stop();
				}
			});
			return true;
		}

		public int getFishLevel(int id) {
			int level = 1;
			if(contains(FISHING_SPOT_NET, id)) {
				level = 1;
			} else if(contains(FISHING_SPOT_FLY, id)) {
				level = 20;
			} else if(contains(FISHING_SPOT_LOBSTER, id)) {
				level = 40;
			} else if(contains(FISHING_SPOT_TUNA, id)) {
				level = 35;
			}
			return level;
		}

		/**
		 * Is fishing spot Net?
		 *
		 * @param id
		 * @return
		 */
		public boolean isFishNet(int id) {
			return contains(FISHING_SPOT_NET, id);
		}

		/**
		 * Is fishing spot Fly/Bait?
		 *
		 * @param id
		 * @return
		 */
		public boolean isFishFly(int id) {
			return contains(FISHING_SPOT_FLY, id);
		}

		public boolean checkFeather(Player client) {
			for(int id : FEATHER) {
				if(ContentEntity.isItemInBag(client, id)) {
					return true;
				}

			}
			return false;
		}

		/**
		 * Is fishing spot Lobster/Tuna?
		 *
		 * @param id
		 * @return
		 */
		public boolean isFishLobster(int id) {
			return contains(FISHING_SPOT_LOBSTER, id);
		}

		/**
		 * Is fishing spot Lobster/Tuna?
		 *
		 * @param id
		 * @return
		 */
		public boolean isFishTuna(int id) {
			return contains(FISHING_SPOT_TUNA, id);
		}

		/**
		 * Net check.
		 *
		 * @param client
		 * @return
		 */
		private final int hasNet(Player client) {
			int ct = 0;
			for(int id : FISHING_NET) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

		private final int getFeatherSlot(Player client) {
			return ContentEntity.getItemSlot(client, 314);
		}

		private final int hasFly(Player client) {
			int ct = 0;
			for(int id : FLY_ROD) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

		private final int hasFeather(Player client) {
			int ct = 0;
			for(int id : FEATHER) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

		private final int hasCage(Player client) {
			int ct = 0;
			for(int id : LOBSTER_POT) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

		private final int hasHarpoon(Player client) {
			int ct = 0;
			for(int id : HARPOON) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

		/**
		 * @param array
		 * @param value
		 * @return
		 */
		public boolean contains(int[] array, int value) {
			for(int i : array) {
				if(i == value) {
					return true;
				}
			}
			return false;
		}

	}

	public class FishingSecondClick {

		public FishingSecondClick() {
		}

		/**
		 * A huge collection of constants here.
		 */
		private final int[] FISHING_SPOT_BAIT = {316, 320, 319, 323,
				325, 327, 326, 332, 330};
		private final int[] FISHING_SPOT_HARPOON_SHARK = {313, 322};

		private final int[] BAIT_ROD = {307};
		private final int[] BAIT = {313};
		private final int[] HARPOON = {311};

		/**
		 * Click object hook.
		 *
		 * @param client
		 * @param object
		 * @param x
		 * @param y
		 * @return
		 */
		public final boolean clickObject(final Player client,
		                                 final int object, final int x, final int y) {

			if(! isFishBait(object) && ! isFishShark(object)) {
				return false;
			}

			if(client.isBusy() || client.getExtraData().get("fishing") != null) {
				return true;
			}
			client.getExtraData().put("fishing", 1);
			client.setBusy(true);

			if(ContentEntity.freeSlots(client) == 0) {
				ContentEntity.sendMessage(client,
						"There is not enough space in your inventory.");
				return true;
			}

			if(isFishBait(object)) {
				final int rod = hasRod(client);
				if(rod == - 1) {
					ContentEntity.sendMessage(client,
							"You need a rod to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if(isFishBait(object)) {
				final int bait = hasBait(client);
				if(bait == - 1) {
					ContentEntity.sendMessage(client,
							"You need bait to fish here.");
					client.setBusy(false);
					return true;
				}
			} else if(isFishShark(object)) {
				final int harpoon2 = hasHarpoon(client);
				if(harpoon2 == - 1) {
					ContentEntity.sendMessage(client,
							"You need a harpoon to fish here.");
					client.setBusy(false);
					return true;
				}
			} else {

				// not supported
				return false;
			}

			final int level = getFishLevel(object);
			if(! (ContentEntity.returnSkillLevel(client, 10) >= level)) {
				ContentEntity.sendMessage(client,
						"You do not have the required level to fish that.");
				return true;
			}

			int animationID = 0;
			if(contains(FISHING_SPOT_BAIT, object)) {
				animationID = 622;
			} else if(contains(FISHING_SPOT_HARPOON_SHARK, object)) {
				animationID = 618;
			}
			ContentEntity.startAnimation(client, animationID);
			client.setBusy(true);
			ContentEntity.sendMessage(client, "You start to fish...");

			ContentEntity.turnTo(client, x, y);
			final int fNumberOfCycles = (int) Math.random() * 5 + 5;

			World.submit(new Task(FISHING_DELAY,"fishing delay") {
				public int cycle = - 1;

				@Override
				public void execute() {
					ContentEntity.turnTo(client, x, y);
					if(! client.isBusy()) {
						stop2();
						return;
					}
					if(ContentEntity.freeSlots(client) == 0) {
						ContentEntity.sendMessage(client,
								"There is not enough space in your inventory.");
						stop2();
						return;
					}

					if(cycle == - 1) {
						cycle = fNumberOfCycles;
					}

					final int BAITSLOT = getBaitSlot(client);

					if(cycle == 1 || Math.random() > FISHING_FACTOR) {
						int xp = 0;
						if(contains(FISHING_SPOT_BAIT, object)) {
							if(ContentEntity.isItemInBag(client, 313)) {
								xp = 20;
								ContentEntity.addItem(client, 327, 1);
								ContentEntity.sendMessage(client,
										"You catch a sardine.");
								ContentEntity.deleteItem(client, 313,
										BAITSLOT, 1);
							} else {
								ContentEntity.sendMessage(client,
										"You need bait to fish here.");
								stop2();
							}
						} else if(contains(FISHING_SPOT_HARPOON_SHARK, object)) {
							if(ContentEntity.returnSkillLevel(client, 10) >= 76 && Combat.random(100) + 76 <= ContentEntity.returnSkillLevel(client, 10)) {
								xp = 110;
								ContentEntity.addItem(client, 383, 1);
								ContentEntity.sendMessage(client,
										"You catch a shark.");
							} else {
								xp = 80;
								ContentEntity.addItem(client, 371, 1);
								ContentEntity.sendMessage(client,
										"You catch a Swordfish.");
							}
						}
						// add xp
						ContentEntity.addSkillXP(client, xp * EXPMULTIPLIER,
								10);
					}
					int animationID = 0;
					if(contains(FISHING_SPOT_BAIT, object)) {
						animationID = 622;
					} else if(contains(FISHING_SPOT_HARPOON_SHARK, object)) {
						animationID = 618;
					}
					ContentEntity.startAnimation(client, animationID);

					cycle--;
					/*if (cycle == 0) {
						stop2();
					}*/
				}


				public void stop2() {
					client.setBusy(false);
					ContentEntity.startAnimation(client, - 1);
					client.getExtraData().remove("fishing");
					this.stop();
				}
			});
			return true;
		}

		public int getFishLevel(int id) {
			int level = 1;
			if(contains(FISHING_SPOT_BAIT, id)) {
				level = 5;
			} else if(contains(FISHING_SPOT_HARPOON_SHARK, id)) {
				level = 50;
			}
			return level;
		}

		/**
		 * Is fishing spot Bait?
		 *
		 * @param id
		 * @return
		 */
		public boolean isFishBait(int id) {
			return contains(FISHING_SPOT_BAIT, id);
		}

		public boolean checkBait(Player client) {
			for(int id : BAIT) {
				if(ContentEntity.isItemInBag(client, id)) {
					return true;
				}

			}
			return false;
		}

		/**
		 * Is fishing spot Shark?
		 *
		 * @param id
		 * @return
		 */
		public boolean isFishShark(int id) {
			return contains(FISHING_SPOT_HARPOON_SHARK, id);
		}

		private final int getBaitSlot(Player client) {
			return ContentEntity.getItemSlot(client, 313);

		}

		private final int hasRod(Player client) {
			int ct = 0;
			for(int id : BAIT_ROD) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

		private final int hasBait(Player client) {
			int ct = 0;
			for(int id : BAIT) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

		private final int hasHarpoon(Player client) {
			int ct = 0;
			for(int id : HARPOON) {
				if(ContentEntity.isItemInBag(client, id)) {
					return ct;
				}
				ct++;
			}
			return - 1;
		}

	}

	/**
	 * @param array
	 * @param value
	 * @return
	 */
	public boolean contains(int[] array, int value) {
		for(int i : array) {
			if(i == value) {
				return true;
			}
		}
		return false;
	}


	@Override
	public int[] getValues(int type) {
		/*if(type == 11 || type == 10){
			int[] j = { 316, 320, 319, 323,
				325, 327, 326, 332, 330,309, 310, 311, 314,
				315, 317, 318, 328, 329, 331, 334, 312, 324,
				303, 305, 309, 314, 301, 311, };
			return j;
		}*/
		return null;
	}

	@Override
	public boolean clickObject(final Player client, final int type, final int npc, final int x, final int y, final int a) {
		if(type == 11)
			return second.clickObject(client, npc, x, y);
		if(type == 10)
			return first.clickObject(client, npc, x, y);
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
		first = new FishingFirstClick();
		second = new FishingSecondClick();
	}

	public static FishingFirstClick first = null;
	public static FishingSecondClick second = null;

}