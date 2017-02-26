package org.hyperion.rs2.model.content.skill;

import org.hyperion.data.PersistenceManager;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc.Rune;
import org.hyperion.rs2.net.ActionSender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class RuneCrafting implements ContentTemplate {

	public RuneCrafting() {
	}

	private List<Rune> runes;


	/**
	 * Loads the XML file of
	 *
	 * @throws FileNotFoundException
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws FileNotFoundException {
		runes = (List<Rune>) PersistenceManager.load(new FileInputStream("./data/runecrafting.xml"));
	}

	/**
	 * Gets the real amount of the rune if player can make more runes from one essence.
	 *
	 * @param rune   The rune.
	 * @param amount The current amount of the rune.
	 * @param level  The current Runecrafting level of the player.
	 * @return The real amount of runes.
	 */

	public int getRuneAmount(int rune, int amount, int level) {
		Rune r = runes.get(getIndex(rune));
		int prev = 1;
		for(int i = 0; i < r.getLevels().length; i++) {
			if(r.getLevels()[i] <= level)
				prev++;
		}
		return prev * amount;
	}

	public int getIndex(int id) {
		for(Rune r : runes) {
			if(r.getRuneId() == id)
				return runes.indexOf(r);
		}
		return - 1;
	}

	/**
	 * Gets the real amount of the rune if player can make more runes from one essence.
	 *
	 * @return The real amount of runes.
	 */

	/*public int getRuneAmount(int rune, int amount, int level) {
	    Runes r = runes.get(getIndex(rune));
		int prev = 1;
		for(int i = 0; i < r.getLevel().length; i++) {
			if(r.getLevel()[i] >= level)
				prev++;
		}
		return prev*amount;
	}*/
	public boolean isRunecraftable(final Player client, final int id, final int item, int slot, int itemId2) {
		if(getIndex(item) == - 1)
			return false;
		final Rune r = runes.get(getIndex(item));
		if(r == null) return false;
		if(client.isBusy() || client.getExtraData().get("runecraftingtimer") != null) {
			ContentEntity.sendMessage(client, "You are too busy to do that.");
			return false;
		}
		if(ContentEntity.getLevelForXP(client, 20) <
				r.getLevel()) {
			ContentEntity.sendMessage(client, "You need a runecrafting level of " + r.getLevel() + " to" +
					"craft " + ContentEntity.getItemName(item) + "'s.");
			return false;
		} else if(ContentEntity.getLevelForXP(client, 20) >=
				r.getLevel()) {
			int useItemId = 1436;
			if(! ContentEntity.isItemInBag(client, 1436) && ContentEntity.isItemInBag(client, 7936))
				useItemId = 7936;
			if(! ContentEntity.isItemInBag(client, useItemId)) {
				ContentEntity.sendMessage(client, "You need some rune essence in order to runecraft.");
				return false;
			}
			ContentEntity.turnTo(client, slot, itemId2);
			//Work out how many runes to craft.
			int amount = ContentEntity.getItemAmount(client, useItemId);
			if(useItemId == 7936)
				amount = amount * 2;
			/*int two = r.getLevel()*2;
			int three = r.getLevel()*3;
			int four = r.getLevel()*4;
			int five = r.getLevel()*5;
			int six = r.getLevel()*6;
			int seven = r.getLevel()*7;
			int eight = r.getLevel()*8;
			int nine = r.getLevel()*9;
			int ten = r.getLevel()*10;*/
			int xp = ContentEntity.getLevelForXP(client, 20);
			final int finalAmount = getRuneAmount(item, amount, xp);
			/*if(xp >= 99){
				amount = amount*10;
			} else if(xp >= 90) {
				amount = amount*9;
			} else if(xp >= 80) {
				amount = amount*8;
			} else if(xp >= 70) {
				amount = amount*7;
			} else if(xp >= 60) {
				amount = amount*6;
			} else if(xp >= 50) {
				amount = amount*5;
			} else if(xp >= 40) {
				amount = amount*4;
			} else if(xp >= 20) {
				amount = amount*3;
			} else if(xp >= 10) {
				amount = amount*2;
			} else if(xp >= 0) {
				amount = amount*1;
			}*/
			final int useItemId2 = useItemId;
			final double runeExp = r.getExp() * amount;
			//Start the runecrafting emote.

			if(client.getRandomEvent().skillAction(15)) {
				return false;
			}
			client.setCanWalk(false);
			client.setBusy(true);
            client.getExtraData().put("runecraftingtimer", 0);
            ContentEntity.startAnimation(client, 791);
			ContentEntity.playerGfx(client, 186);
			ContentEntity.sendMessage(client, "You bind the temple's power into " + ContentEntity.getItemName(item) + "s.");
			//The runecrafting event.
			World.submit(new Task(2300,"runecracting") {
				@Override
				public void execute() {
					ContentEntity.deleteItemAll(client, useItemId2, 1);
					ContentEntity.addItem(client, r.getRuneId(), finalAmount);
					client.getAchievementTracker().itemSkilled(Skills.RUNECRAFTING, r.getRuneId(), finalAmount);
					ContentEntity.sendMessage(client, "You craft " + finalAmount + " " + ContentEntity.getItemName(item) + "s.");
					ContentEntity.addSkillXP(client, runeExp * (Constants.XPRATE/2), 20);
					// Stop the event.
					stop2();
				}

				public void stop2() {
					client.getExtraData().remove("runecraftingtimer");
					ContentEntity.startAnimation(client, - 1);
					client.setBusy(false);
					client.setCanWalk(true);
					this.stop();
				}

			});
			return true;
		}
		return false;
	}

	@Override
	public int[] getValues(int type) {
		if(type == 6) {
			int[] j = {2478, 2479, 2480, 2481, 2482, 2483, 2484, 2485, 2486, 2487, 2488};
			return j;
		}
		if(type == 17) {
			int[] j = {1438, 1448, 1444, 1440, 1442, 1446, 1458, 1462, 1452, 1454, 1456,};
			return j;
		}
        if(type == ClickType.DIALOGUE_MANAGER)
            return new int[]{1020, 1021, 1022, 1023, 1024, 1025};
		return null;
	}


	@Override
	public boolean clickObject(final Player client, final int type, final int id, final int slot, final int itemId2, final int itemSlot2) {
		if(type == 6) {
			if(id == AIR_ALTAR) {
                if(client.getDungeoneering().inDungeon()) {
                    DialogueManager.openDialogue(client, 1020);
                } else
				if(isRunecraftable(client, id, 556, slot, itemId2)) {
				}
			} else if(id == MIND_ALTAR) {
				if(isRunecraftable(client, id, 558, slot, itemId2)) {
				}
			} else if(id == WATER_ALTAR) {
				if(isRunecraftable(client, id, 555, slot, itemId2)) {
				}
			} else if(id == EARTH_ALTAR) {
				if(isRunecraftable(client, id, 557, slot, itemId2)) {
				}
			} else if(id == FIRE_ALTAR) {
				if(isRunecraftable(client, id, 554, slot, itemId2)) {
				}
			} else if(id == BODY_ALTAR) {
				if(isRunecraftable(client, id, 559, slot, itemId2)) {
				}
			} else if(id == COSMIC_ALTAR) {
				if(isRunecraftable(client, id, 564, slot, itemId2)) {
				}
			} else if(id == LAW_ALTAR) {
				if(isRunecraftable(client, id, 563, slot, itemId2)) {
				}
			} else if(id == NATURE_ALTAR) {
				if(isRunecraftable(client, id, 561, slot, itemId2)) {
				}
			} else if(id == CHAOS_ALTAR) {
				if(isRunecraftable(client, id, 562, slot, itemId2)) {
				}
			} else if(id == 2488) {//death
				if(isRunecraftable(client, id, 560, slot, itemId2)) {
				}
			}
		}
		if(type == 17) {
			if(id == 1438) {//air
				Magic.teleport(client, 2841, 4829, 0, false);
			} else if(id == 1448) {//mind
				Magic.teleport(client, 2793, 4828, 0, false);
			} else if(id == 1444) {//water
				Magic.teleport(client, 2726, 4832, 0, false);
			} else if(id == 1440) {//earth
				Magic.teleport(client, 2655, 4830, 0, false);
			} else if(id == 1442) {//fire
				Magic.teleport(client, 2574, 4849, 0, false);
			} else if(id == 1446) {//body
				Magic.teleport(client, 2523, 4826, 0, false);
			} else if(id == 1458) {//law
				Magic.teleport(client, 2464, 4818, 0, false);
			} else if(id == 1462) {//nature
				Magic.teleport(client, 2400, 4835, 0, false);
			} else if(id == 1452) {//chasos
				Magic.teleport(client, 2281, 4837, 0, false);
			} else if(id == 1454) {//cosmic
				Magic.teleport(client, 2142, 4813, 0, false);
			} else if(id == 1456) {//death
				Magic.teleport(client, 2208, 4830, 0, false);
			}
		}
        if(type == ClickType.DIALOGUE_MANAGER) {
            switch(id) {
                case 1020:
                    client.getActionSender().sendDialogue("Crafting", ActionSender.DialogueType.OPTION, 1, Animation.FacialAnimation.DEFAULT,
                            "Air", "Water", "Earth", "Fire", "Death");
                    for(int i = 0; i < 5; i++) {
                        client.getInterfaceState().setNextDialogueId(i, 1021 + i);
                    }
                    return true;
                case 1021:
                    return isRunecraftable(client, AIR_ALTAR, 556, 2897, 9909);
                case 1022:
                    return isRunecraftable(client, WATER_ALTAR, 555, 2897, 9909);
                case 1023:
                    return isRunecraftable(client, EARTH_ALTAR, 555, 2897, 9909);
                case 1024:
                    isRunecraftable(client, FIRE_ALTAR, 554, 2897, 9909);
                case 1025:
                    return isRunecraftable(client, DEATH_ALTAR, 560, 2897, 9909);
            }
        }
		return false;
	}


	@SuppressWarnings("unused")
	private static final int AIR_ALTAR = 2478, MIND_ALTAR = 2479, WATER_ALTAR = 2480, EARTH_ALTAR = 2481,
			FIRE_ALTAR = 2482, BODY_ALTAR = 2483, COSMIC_ALTAR = 2484, LAW_ALTAR = 2485,
			NATURE_ALTAR = 2486, CHAOS_ALTAR = 2487, DEATH_ALTAR = 2488;

}
