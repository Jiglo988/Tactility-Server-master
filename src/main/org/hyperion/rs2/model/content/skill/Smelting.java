package org.hyperion.rs2.model.content.skill;

/**
 * Smithing
 *
 * @author Lil str kid
 *
 *
 */

import org.hyperion.data.PersistenceManager;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc.SmeltingItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Smelting implements ContentTemplate {

	public Smelting() {
	}

	private List<SmeltingItem> smeltingItem;

	private final int SMELTING_ANIM = 899;

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws FileNotFoundException {
		smeltingItem = (List<SmeltingItem>) PersistenceManager.load(new FileInputStream("./data/Smelting.xml"));
	}


	public boolean isSmeltingItem(int item) {
		for(SmeltingItem c : smeltingItem) {
			if(c == null)
				continue;
			int id = c.getOreId();
			if(id == item)
				return true;
			id = c.getSecondOreId();
			if(id == item)
				return true;
		}
		return false;
	}

	public int getIndex(int item) {
		for(SmeltingItem c : smeltingItem) {
			if(c == null)
				continue;
			if(c.getBarId() == item)
				return smeltingItem.indexOf(c);
		}
		return - 1;
	}

	public int getBarIndex(int item) {
		for(SmeltingItem c : smeltingItem) {
			if(c == null)
				continue;
			if(c.getOreId() == item || c.getSecondOreId() == item)
				return c.getBarId();
		}
		return - 1;
	}

	public void smeltingItem(final Player client, final int item, final int am) {
		if(client.isBusy()) {
			return;
		}
		ContentEntity.removeAllWindows(client);
		if(getIndex(item) == - 1) {
			return;
		}
		final SmeltingItem smeltingItem = this.smeltingItem.get(getIndex(item));
		if(smeltingItem == null) {
			return;
		}

		if(ContentEntity.returnSkillLevel(client, 13) < smeltingItem.getLevel()) {
			ContentEntity.sendMessage(client, "Your Smithing level is not high enough to do this.");
			return;
		}
		if(ContentEntity.getItemAmount(client, smeltingItem.getOreId()) <= 0) {
			ContentEntity.sendMessage(client, "you need more ore to make this bar");
			return;
		}
		ContentEntity.startAnimation(client, SMELTING_ANIM);
		client.setBusy(true);
		World.submit(new Task(3000, "smelting") {
			int amount = am;

			@Override
			public void execute() {
				if(amount <= 0 || ! client.isBusy()) {
					stop2();
					return;
				}
				if(ContentEntity.getItemAmount(client, smeltingItem.getOreId()) <= 0) {
					ContentEntity.sendMessage(client, "you need more ore to make this bar");
					stop2();
					return;
				}
				if(client.getRandomEvent().skillAction(2)) {
					stop2();
					return;
				}
				if(amount > 1)
					ContentEntity.startAnimation(client, SMELTING_ANIM);
				ContentEntity.addSkillXP(client, smeltingItem.getExperience() * Constants.XPRATE, Skills.SMITHING);
				ContentEntity.deleteItemA(client, smeltingItem.getOreId(), 1);
				if(smeltingItem.getSecondOreId() > 2)
					ContentEntity.deleteItemA(client, smeltingItem.getSecondOreId(), smeltingItem.getSecondOreAmount());

				if(Combat.random(1) == 0 && item == 2351) {
					ContentEntity.sendMessage(client, "You fail to remove the iron bar form the piercing hot furnace.");
				} else {
					client.getAchievementTracker().itemSkilled(Skills.SMITHING, item, 1);
					ContentEntity.addItem(client, item, 1);
				}
				amount--;
			}

			public void stop2() {
				ContentEntity.startAnimation(client, - 1);
				client.setBusy(false);
				this.stop();
			}

		});

	}

	public void openSmelting(Player c, int itemId) {
		if(! isSmeltingItem(itemId)) {
			ContentEntity.sendMessage(c, "you cannot smelt this item.");
			return;
		}
		smeltingItem(c, getBarIndex(itemId), 1);
	}

	public void openSmelting(Player c) {
		c.closeChatInterface = true;
		ContentEntity.sendInterfaceModel(c, 2405, 150, 2349);
		ContentEntity.sendInterfaceModel(c, 2406, 150, 2351);
		ContentEntity.sendInterfaceModel(c, 2407, 150, 2355);
		ContentEntity.sendInterfaceModel(c, 2409, 150, 2353);
		ContentEntity.sendInterfaceModel(c, 2410, 150, 2357);
		ContentEntity.sendInterfaceModel(c, 2411, 150, 2359);
		ContentEntity.sendInterfaceModel(c, 2412, 150, 2361);
		ContentEntity.sendInterfaceModel(c, 2413, 150, 2363);
		c.getActionSender().sendPacket164(2400);
		ContentEntity.sendString(c, "", 4158);
	}

	@Override
	public int[] getValues(int type) {
		if(type == 14) {
			int[] j = {436, 438, 440, 442, 444, 447, 449, 451, 453,};
			return j;
		}
		if(type == 7) {
			int[] j = {11666, 2781};
			return j;
		}
		if(type == 0) {
			int[] j = {3987, 3991, 3995, 3999, 4003, 7441, 7446, 7450, 3986, 3990, 3994, 3998, 4002, 7440, 7444, 7449, 2807, 3989, 3993, 3997, 4001, 6397, 7443, 7448,};
			return j;
		}
		return null;
	}

	public static final int[] bar = {2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363,};

	@Override
	public boolean clickObject(final Player client, final int type, final int id, final int slot, final int itemId2, final int itemSlot2) {
		if(type == 14) {
			openSmelting(client, id);
			return true;
		}
		if(type == 7) {
			openSmelting(client);
			return true;
		}
		if(type == 0 /*&& (itemId2 == 2781 || itemId2 == 2785 || itemId2 == 2966 || itemId2 == 3044 || itemId2 == 3294 || itemId2 == 3413 || itemId2 == 4304 || itemId2 == 4305 || itemId2 == 6189 || itemId2 == 6190)*/) {
			if(id == 3987)
				smeltingItem(client, bar[0], 1);
			else if(id == 3991)
				smeltingItem(client, bar[1], 1);
			else if(id == 3995)
				smeltingItem(client, bar[2], 1);
			else if(id == 3999)
				smeltingItem(client, bar[3], 1);
			else if(id == 4003)
				smeltingItem(client, bar[4], 1);
			else if(id == 7441)
				smeltingItem(client, bar[5], 1);
			else if(id == 7446)
				smeltingItem(client, bar[6], 1);
			else if(id == 7450)
				smeltingItem(client, bar[7], 1);
			else if(id == 3986)
				smeltingItem(client, bar[0], 5);
			else if(id == 3990)
				smeltingItem(client, bar[1], 5);
			else if(id == 3994)
				smeltingItem(client, bar[2], 5);
			else if(id == 3998)
				smeltingItem(client, bar[3], 5);
			else if(id == 4002)
				smeltingItem(client, bar[4], 5);
			else if(id == 7440)
				smeltingItem(client, bar[5], 5);
			else if(id == 7444)
				smeltingItem(client, bar[6], 5);
			else if(id == 7449)
				smeltingItem(client, bar[7], 5);
			else if(id == 2807)
				smeltingItem(client, bar[0], 10);
			else if(id == 3989)
				smeltingItem(client, bar[1], 10);
			else if(id == 3993)
				smeltingItem(client, bar[2], 10);
			else if(id == 3997)
				smeltingItem(client, bar[3], 10);
			else if(id == 4001)
				smeltingItem(client, bar[4], 10);
			else if(id == 6397)
				smeltingItem(client, bar[5], 10);
			else if(id == 7443)
				smeltingItem(client, bar[6], 10);
			else if(id == 7448)
				smeltingItem(client, bar[7], 10);
		}
		return true;
	}

}

  



