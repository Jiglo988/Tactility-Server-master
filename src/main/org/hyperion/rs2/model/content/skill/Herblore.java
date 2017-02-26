package org.hyperion.rs2.model.content.skill;

import org.hyperion.data.PersistenceManager;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc.Herb;
import org.hyperion.rs2.model.content.misc.Potion;
import org.hyperion.rs2.model.content.misc.UnfinishedPotion;
import org.hyperion.rs2.model.newcombat.Skills;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Handles Herbloring
 *
 * @author Jonas(++)
 */
//c.getPA().sendFrame164(4429);
//c.getPA().sendFrame246(1746, 150, data1[i][1]);
public class Herblore implements ContentTemplate {

	private static final int EXPMULTIPLIER = Constants.XPRATE * 2;

	public Herblore() {
	}

	private List<Herb> herbs;
	private List<UnfinishedPotion> unfinishedPotions;
	private List<Potion> potions;


	/**
	 * Gets the index of the item
	 *
	 */

	public int getIndex(int herb) {
		for(Herb c : herbs) {
			if(c.getHerbId() == herb)
				return herbs.indexOf(c);
		}
		return - 1;
	}

	/**
	 * Gets the index of the item
	 *
	 */

	public int getPotionIndex(int herb) {
		for(UnfinishedPotion u : unfinishedPotions) {
			if(u.getHerb() == herb)
				return unfinishedPotions.indexOf(u);
		}
		return - 1;
	}

	/**
	 * Identifies an herb
	 *
	 * @param herb The herb that needs to be indentified.
	 */

	public void identifyHerb(Player c, int herb, int slot) {
		Herb h = herbs.get(getIndex(herb));

		if(ContentEntity.returnSkillLevel(c, 15) >= h.getLevel()) {
			if(c.getRandomEvent().skillAction(2)) {
				return;
			}
			if(c.getRandomEvent().isDoingRandom())
				return;
			ContentEntity.sendMessage(c, "You identify the herb.");
			ContentEntity.deleteItem(c, herb, slot, 1);
			ContentEntity.addItem(c, (h.getCleanHerb()), 1, slot);
			ContentEntity.addSkillXP(c, h.getExperience() * EXPMULTIPLIER, 15);
		} else {
			ContentEntity.sendMessage(c, "You need a Herbloring level of " +
					"" + h.getLevel() + " to identify this herb.");
		}
	}

	/**
	 * Makes an unfinished potion
	 *
	 * @param herb   The herb.
	 * @return
	 */

	public void mixPotion(final Player c, int herb, int itemId, int slot, int slot2, boolean first) {
		if(herb == 269 || itemId == 269) {
			//makeOverload(c);
			return;
		}
		if(getPotionIndex(herb) < 0) {
			final int tempHerb = herb;
			herb = itemId;
			itemId = tempHerb;

			final int tempHerbSlot = slot;
			slot = slot2;
			slot2 = tempHerbSlot;
			if(getPotionIndex(herb) < 0)
				return;
		}
		final UnfinishedPotion u = unfinishedPotions.get(getPotionIndex(herb));
		final int herb2 = herb;
		int amount = ContentEntity.getItemAmount(c, itemId);
		if(amount > ContentEntity.getItemAmount(c, herb))
			amount = ContentEntity.getItemAmount(c, herb);
		final int amount2 = amount;

		if(itemId != 227 && first) {
			mixPotion(c, itemId, herb, slot, slot2, false);
			return;
		}
		if(ContentEntity.returnSkillLevel(c, 15) >= u.getPotionLevel()) {
			ContentEntity.startAnimation(c, 0x378);
			c.inAction = true;
			World.submit(new Task(3000, "herblore") {
				int amountLeft = amount2;

				@Override
				public void execute() {
					if(! c.inAction)
						return;
					if(amountLeft <= 0) {
						this.stop();
						return;
					}
					if(c.getRandomEvent().skillAction(3)) {
						this.stop();
						return;
					}
					if(! ContentEntity.isItemInBag(c, 227) || ! ContentEntity.isItemInBag(c, herb2)) {
						this.stop();
						return;
					}
					amountLeft--;
					c.getAchievementTracker().itemSkilled(Skills.HERBLORE, u.getPotion(), 1);
					ContentEntity.sendMessage(c, "You make an unfinished potion.");
					ContentEntity.deleteItem(c, herb2);
					ContentEntity.deleteItem(c, 227);
					//ContentEntity.deleteItem(c,227, 1);
					ContentEntity.addItem(c, u.getPotion());
					ContentEntity.addSkillXP(c, u.getPotionExp() * EXPMULTIPLIER, 15);
					if(amountLeft != 0)
						ContentEntity.startAnimation(c, 0x378);
				}
			});
		} else {
			ContentEntity.sendMessage(c, "You need a Herbloring level of " +
					"" + u.getPotionLevel() + " to make this potion.");
		}
	}

	/**
	 * Gets the index of the unfinished potion, and will return everything of the finised potion.
	 *
	 * @param potion The unfinished potion of which index has to be returned.
	 */

	public int getEndPotionIndex(int potion) {
		for(Potion pot : potions) {
			if(pot.getPotionId() == potion)
				return potions.indexOf(pot);
		}
		return - 1;
	}

	public Potion getEndPotionIndex2(int potion) {
		for(Potion pot : potions) {
			if(pot.getPotionId() == potion)
				return pot;
		}
		return null;
	}

	/**
	 * Checks if we're using a unfinished potion with one of their second ingredients.
	 *
	 * @param potion The potion we're using.
	 * @param item   The item we're using.
	 */

	public boolean checkPotion(Player c, int potion, int item) {
		for(Potion pot : potions) {
			if(pot.getPotionId() == potion) {
				int[] items = pot.getSecondItems();
				for(int i = 0; i < items.length; i++) {
					if(items[i] == item)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the place in the array of the 2nd ingredients.
	 *
	 * @param potion The unfinished potion.
	 * @param item   The 2nd ingredient we're using.
	 */

	public int getPlace(int potion, int item) {
		int index = getEndPotionIndex(potion);
		if(index == - 1)
			return - 1;
		Potion p = potions.get(index);

		int[] items = p.getSecondItems();

		for(int i = 0; i < items.length; i++) {
			if(items[i] == item)
				return i;
		}
		return - 1;
	}

	/**
	 * Ends a unfinished potion by using the 2nd ingredient on it.
	 *
	 * @param potion The unfinished potion.
	 * @param item   The 2nd ingredient.
	 */

	public boolean endPotion(final Player c, int potion, int item, int slot1, int slot2) {
		Potion p = getEndPotionIndex2(potion);
		if(p == null) {
			p = getEndPotionIndex2(item);
			if(p != null) {
				int a = potion;
				potion = item;
				item = a;
				a = slot1;
				slot1 = slot2;
				slot2 = a;
			} else
				return false;
		}
		int i = getPlace(potion, item);
		if(i == - 1) {
			i = getPlace(item, potion);
			if(i == - 1)
				return false;
		}
		final Potion p2 = p;
		final int potion2 = potion;
		final int item2 = item;
		final int i2 = i;

		if(ContentEntity.returnSkillLevel(c, 15) >= p.getPotionLevel()[i]) {
			ContentEntity.startAnimation(c, 0x378);
			c.inAction = true;
			World.submit(new Task(3000, "herblore") {
				@Override
				public void execute() {
					if(! c.inAction) {
						this.stop();
						return;
					}
					int amount = ContentEntity.getItemAmount(c, item2);
					if(amount > ContentEntity.getItemAmount(c, potion2))
						amount = ContentEntity.getItemAmount(c, potion2);
					if(amount <= 0) {
						this.stop();
						return;
					}
					if(! ContentEntity.isItemInBag(c, potion2) || ! ContentEntity.isItemInBag(c, item2)) {
						this.stop();
						return;
					}
					ContentEntity.sendMessage(c, "You make a " + ItemDefinition.forId(p2.getFinishedPotion()[i2]).getName());
					ContentEntity.deleteItem(c, potion2);
					ContentEntity.deleteItem(c, item2);
					ContentEntity.addItem(c, p2.getFinishedPotion()[i2], 1);
					ContentEntity.addSkillXP(c, p2.getPotionExp()[i2] * EXPMULTIPLIER, 15);
					if(amount - 1 != 0)
						ContentEntity.startAnimation(c, 0x378);
				}
			});
		} else {
			ContentEntity.sendMessage(c, "You need a herblore level of " +
					"" + p.getPotionLevel()[i] + " to make this potion.");
		}
		return true;
	}
	    /*
         * <!-- Overload atk potion -->
	<potion>
		<potionId>269</potionId>
		<secondItem>
			<int>15325</int>
			<int>15321</int>
			<int>15327</int>
			<int>15313</int>
			<int>15309</int>
		</secondItem>
		<finishedPotion>
			<int>15333</int>
		</finishedPotion>
		<level>
			<int>94</int>
		</level>
		<experience>
			<int>270</int>
		</experience>
	</potion>
		 */
		
		/*public void makeOverload(Player c){
			if(ContentEntity.returnSkillLevel(c,15) < 96) {
				ContentEntity.sendMessage(c,"You need a herblore level of 96 to make this potion.");
				return;
			}
			int[] items = {15309,15313,15317,15321,15325,269};
			for(int i = 0; i < items.length; i++){
				if(!ContentEntity.isItemInBag(c, items[i])){
					ContentEntity.sendMessage(c,"You don't have the correct ingredients to make an overload potion.");
					return;
				}	
			}
			for(int i = 0; i < items.length; i++){
				ContentEntity.deleteItem(c,items[i]);
			}
			ContentEntity.addSkillXP(c,1000*EXPMULTIPLIER, 15);		
			ContentEntity.startAnimation(c,0x378);
			ContentEntity.addItem(c,15333, 1);
		}*/

	private static final int[] ovlStuff = {15309, 15313, 15317, 15321, 15325, 269};

	@Override
	public boolean clickObject(final Player client, final int type, int id, final int slot, int itemId2, final int itemSlot2) {
		if(type == 0) {
			client.getInterfaceState().resetInterfaces();
			int id1 = client.getSkillingData().getFirstId();
			int id2 = client.getSkillingData().getSecondId();
			int slot1 = client.getSkillingData().getFirstSlot();
			int slot2 = client.getSkillingData().getSecondSlot();
			if(! endPotion(client, id1, id2, slot1, slot2) && ! endPotion(client, id2, id1, slot2, slot1))
				mixPotion(client, id1, id2, slot1, slot2, true);
		} else if(type == 1) {
			identifyHerb(client, id, slot);
		} else if(type == 13) {
			//ContentEntity.sendMessage(client,id+" : "+slot+" : "+itemId2+" : "+itemSlot2);
			boolean overload = false;
			for(int i = 0; i < ovlStuff.length; i++) {
				if(ovlStuff[i] == id || ovlStuff[i] == itemId2) {
					overload = true;
					break;
				}
			}
			int i = 227;
			if(overload)
				i = 15333;
			else {
				boolean finishedPotion = false;
				if(getPlace(id, itemId2) != - 1) {
					i = getPlace(id, itemId2);
					//System.out.println("I is " + i);
					finishedPotion = true;
				}
				if(getPlace(itemId2, id) != - 1) {
					i = getPlace(itemId2, id);
					//System.out.println("I2 is " + i);
					finishedPotion = true;
				}
				if(! finishedPotion) {
					if(getPotionIndex(id) != - 1) {
						i = getPotionIndex(id);
					}
					if(getPotionIndex(itemId2) != - 1) {
						i = getPotionIndex(itemId2);
					}
				}
				try {
					if(! finishedPotion) {
						if(i >= unfinishedPotions.size())
							return false;
						UnfinishedPotion u = unfinishedPotions.get(i);
						i = u.getPotion();
					} else {
						Potion p = getEndPotionIndex2(id);
						if(p == null) {
							p = getEndPotionIndex2(itemId2);
							if(p != null) {
								int a = id;
								id = itemId2;
								itemId2 = a;
							}
						}
						//System.out.println("i :" + i);
						if(i >= p.getFinishedPotion().length)
							i = getPlace(itemId2, id);
						i = p.getFinishedPotion()[i];
					}
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("I is " + i + " id : " + id + " itemid2 " + itemId2);
					return true;
				}
			}
			//System.out.println("I is " + i);
			client.getSkillingData().setFirstId(id);
			client.getSkillingData().setSecondId(itemId2);
			client.getSkillingData().setFirstSlot(slot);
			client.getSkillingData().setSecondSlot(itemSlot2);
			client.getActionSender().sendChatboxInterface(4429);
			client.getActionSender().sendInterfaceModel(1746, 150, i);
			client.getActionSender().sendString(2799, "\\n\\n\\n\\n\\n" + ItemDefinition.forId(i).getProperName());

		}
		return true;
	}

	private static final int[] ActionButtonIds = {2799, 2798, 1748, 1747};

	/**
	 * Loads the XML file of herbloring.
	 *
	 * @throws FileNotFoundException
	 */

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws FileNotFoundException {
		herbs = (List<Herb>) PersistenceManager.load(new FileInputStream("./data/herbs.xml"));
		unfinishedPotions = (List<UnfinishedPotion>) PersistenceManager.load(new FileInputStream("./data/unfinishedPotions.xml"));
		potions = (List<Potion>) PersistenceManager.load(new FileInputStream("./data/potions.xml"));
	}

	@Override
	public int[] getValues(int type) {
		if(type == 0) {
			return ActionButtonIds;
		} else if(type == 1) {
			int[] j = new int[herbs.size()];
			int i2 = 0;
			for(Herb i : herbs) {
				j[i2] = i.getHerbId();
				i2++;
			}
			return j;
		} else if(type == 13) {
				/*int[] j = {227,249,251,253,255,257,259,261,263,265,267,269,
						91,93,95,97,99,101,103,105,3004,3002,107,2483,109,111,
						221,235,225,223,1975,239,231,2970,225,241,223,2152,6693,239,3138,241,245,247,};*/
			int size = 10;
			for(Potion p : potions) {
				size += p.getSecondItems().length + 1;
			}
			for(UnfinishedPotion p : unfinishedPotions) {
				size += 2;
			}
			int[] k = new int[size];
			int i = 0;
			for(Potion p : potions) {
				k[i++] = p.getPotionId();
				for(int i5 = 0; i5 < p.getSecondItems().length; i5++)
					k[i++] = p.getSecondItems()[i5];
			}
			for(UnfinishedPotion p : unfinishedPotions) {
				k[i++] = p.getHerb();
				k[i++] = p.getPotion();
			}
			k[i++] = 269;
			k[i++] = 15309;
			k[i++] = 15313;
			k[i++] = 15317;
			k[i++] = 15321;
			k[i++] = 15325;
			k[i++] = 227;
			return k;
		}
		return null;
	}

	static {
	}
}
