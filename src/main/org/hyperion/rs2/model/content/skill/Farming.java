package org.hyperion.rs2.model.content.skill;

import org.apache.mina.core.buffer.IoBuffer;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Farming implements ContentTemplate {

	public static class Plant {
		public int[] normalStages;
		public int[] wetStages;
		public int[] dieseasedStages;
		public int growTime;
		public int stageGrowthTime;
		public int type;
		public int level;
		public int xp;
		public int seedId;
		public Item[] items;

		public Plant(int seedId, int level, int xp, int[] normalStages, int[] wetStages, int[] diseasedStages, int growTime, int type, Item[] items) {
			this.normalStages = normalStages;
			this.wetStages = wetStages;
			this.dieseasedStages = diseasedStages;
			this.growTime = growTime;
			this.type = type;
			this.level = level;
			this.stageGrowthTime = (growTime / normalStages.length);
			this.items = items;
			this.xp = xp;
		}
	}

	public class Patch {
		//this represents a set of speeds plantable in a plot
		public Map<Integer, Plant> seeds = new HashMap<Integer, Plant>();
		public int newPatchId;

		public Patch(int newPatchId) {
			this.newPatchId = newPatchId;
			patches.put(newPatchId, this);
		}
	}

	public static class RakePatch {
		//this represents a raked plot by a player
		public int x;
		public int y;
		public int timeLeft;
		public int unRakedObjId;

		public RakePatch(int x, int y, int timeLeft, int unRakedObjId) {
			this.x = x;
			this.y = y;
			this.timeLeft = timeLeft;
			this.unRakedObjId = unRakedObjId;
		}
	}

	public static class PlayerPlant {
		//this represents a plant that was planted by a player
		public int seed;
		public int plotId;
		public int timeSinceWatered = 0;
		public int x;
		public int y;
		public boolean dead;
		public int day;
		public int hour;
		public int minute;
		public int stage = 0;

		public PlayerPlant(int x, int y, int seed, int plotId, boolean dead, int day, int hour, int minute) {
			this.x = x;
			this.y = y;
			this.seed = seed;
			this.plotId = plotId;
			this.dead = dead;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
		}
	}

	public static int ticksExecuted(int day, int hour, int minute) {
		int dayNow = calendar.get(Calendar.DAY_OF_YEAR);//2880 ticks a day
		int hourNow = calendar.get(Calendar.HOUR_OF_DAY);//120 ticks in an hour
		int minuteNow = calendar.get(Calendar.MINUTE);//2 ticks in a minute
		int ticks = 0;
		ticks = (dayNow - day) * 2880;
		if(ticks < 0)
			ticks = 0;
		ticks += (hourNow - hour) * 120;
		ticks += (minuteNow - minute) * 2;
		return ticks;
	}

	public class Farm {
		//this represents a Players "Farm" or objects created through farming
		//public List<RakePatch> rakePatches = new LinkedList<RakePatch>();
		public Map<Integer, RakePatch> rakePatches = new HashMap<Integer, RakePatch>();
		public Map<Integer, PlayerPlant> plants = new HashMap<Integer, PlayerPlant>();

		public void newRakeSpot(int x, int y, int unRakedObjId) {
			rakePatches.put(((x * 16) + y), new RakePatch(x, y, 10, unRakedObjId));
		}

		public void removeRakeSpot(int x, int y) {
			RakePatch patch = rakePatches.get(((x * 16) + y));
			if(patch != null)
				rakePatches.remove(((x * 16) + y));
		}


		public void newPlant(int x, int y, int seed, int plotId, boolean dead) {
			plants.put(((x * 16) + y), new PlayerPlant(x, y, seed, plotId, dead, calendar.get(Calendar.DAY_OF_YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
		}

		public Farm() {

		}
	}

	public void refreshFarmObjects(Player player) {

		for(Object object : player.getFarm().rakePatches.values().toArray()) {
			RakePatch rpatch = (RakePatch) object;
			Patch patch = unRakedPatches.get(rpatch.unRakedObjId);
			player.getActionSender().sendReplaceObject(rpatch.x, rpatch.y, patch.newPatchId, 0, 10);
		}
		for(Object object : player.getFarm().plants.values().toArray()) {
			PlayerPlant plant = (PlayerPlant) object;
			Plant serverPlant = patches.get(plant.plotId).seeds.get(plant.seed);
			if(plant.stage != - 1) {
				player.getActionSender().sendReplaceObject(plant.x, plant.y, plant.plotId, 0, 10);
				player.getActionSender().sendReplaceObject(plant.x + offset(serverPlant.type), plant.y + offset(serverPlant.type), serverPlant.normalStages[plant.stage], 0, 10);
			}
		}

	}

	public static void deserialize(IoBuffer buf, Player player) {//load method
		while(buf.hasRemaining()) {
			try {
				int type = buf.getUnsigned();
				if(type == 241) {
					if(buf.getUnsigned() == 231 && buf.getUnsigned() == 221) {
						//end of the data
						return;
					}
				} else if(type == 3) {
					//rake patch
					int x = buf.getUnsignedShort();
					int y = buf.getUnsignedShort();
					int timeLeft = buf.getUnsigned();
					int unRakedObjId = buf.getUnsignedShort();
					player.getFarm().rakePatches.put(((x * 16) + y), new RakePatch(x, y, timeLeft, unRakedObjId));
				} else if(type == 4) {
					//a plant
					int seed = buf.getUnsignedShort();
					int plotId = buf.getUnsignedShort();
					int timeSinceWatered = buf.getUnsignedShort();
					int x = buf.getUnsignedShort();
					int y = buf.getUnsignedShort();
					boolean dead = buf.getUnsigned() == 1 ? true : false;
					int day = buf.getUnsignedShort();
					int hour = buf.getUnsigned();
					int minute = buf.getUnsigned();
					PlayerPlant plant = new PlayerPlant(x, y, seed, plotId, dead, day, hour, minute);
					Plant serverPlant = patches.get(plant.plotId).seeds.get(plant.seed);

					plant.stage = ((ticksExecuted(plant.day, plant.hour, plant.minute) / serverPlant.stageGrowthTime));
					if(plant.stage > (serverPlant.normalStages.length - 1))
						plant.stage = (serverPlant.normalStages.length - 1);
					plant.timeSinceWatered = timeSinceWatered;

					player.getFarm().plants.put(((x * 16) + y), plant);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public  static void serialize(IoBuffer buf, Player player) {//save method
		//buf.put((byte) 251);
		//buf.put((byte) 231);
		//buf.put((byte) 221);//farming signature, this indicates start of farming data
		synchronized(player.getFarm().rakePatches) {
			for(Object object : player.getFarm().rakePatches.values().toArray()) {
				RakePatch rpatch = (RakePatch) object;
				buf.put((byte) 3);//3 is the type for a rake patch
				buf.putShort((short) rpatch.x);
				buf.putShort((short) rpatch.y);
				buf.put((byte) rpatch.timeLeft);
				buf.putShort((short) rpatch.unRakedObjId);
			}
		}
		synchronized(player.getFarm().plants) {
			for(Object object : player.getFarm().plants.values().toArray()) {
				PlayerPlant plant = (PlayerPlant) object;
				buf.put((byte) 4);//4 is the type for a plant
				buf.putShort((short) plant.seed);
				buf.putShort((short) plant.plotId);
				buf.putShort((short) plant.timeSinceWatered);
				buf.putShort((short) plant.x);
				buf.putShort((short) plant.y);
				buf.put((byte) (plant.dead ? 1 : 0));
				buf.putShort((short) plant.day);
				buf.put((byte) plant.hour);
				buf.put((byte) plant.minute);
			}
		}
		//buf.put((byte) 241);
		//buf.put((byte) 231);
		//buf.put((byte) 221);//farming signature, this indicates end of farming data
	}

	public void plantSeed(final Player player, final int seedId, final int objId, final int x, final int y) {
		if(player.isBusy())
			return;
		if(! ContentEntity.isItemInBag(player, 5343)) {
			player.getActionSender().sendMessage("You need a seed dibber to plant seeds.");
			return;
		}
		final Patch patch = patches.get(objId);
		if(patch == null) {
			player.getActionSender().sendMessage("You can't plant this seed here.");
			return;
		}
		Plant plant = patch.seeds.get(seedId);
		if(plant == null) {
			player.getActionSender().sendMessage("You can't plant this seed in this type of patch.");
			return;
		}
		if(plant.level > player.getSkills().getLevel(Skills.FARMING)) {
			player.getActionSender().sendMessage("You need " + plant.level + " farming to plant this seed.");
			return;
		}
		//player.setBusy(true);
		player.getInventory().remove(new Item(seedId, 1));
		player.getFarm().removeRakeSpot(x, y);
		player.playAnimation(Animation.create(2272));
		player.getFarm().newPlant(x, y, seedId, objId, false);
		player.getActionSender().sendReplaceObject(x + offset(plant.type), y + offset(plant.type), plant.normalStages[0], 0, 10);
	}

	public void harvest(final Player player, final int objId, int x, int y) {
		if(player.isBusy())
			return;
		PlayerPlant plant = player.getFarm().plants.get(((x * 16) + y));
		if(plant == null) {
			x--;
			y--;
			plant = player.getFarm().plants.get(((x * 16) + y));
			if(plant == null) {
				//plant has already been removed, this should be impossible to get
				return;
			}
		}
		Plant serverPlant = patches.get(plant.plotId).seeds.get(plant.seed);
		if(serverPlant.normalStages.length - 1 == plant.stage && ! plant.dead) {
			if(player.getInventory().freeSlots() < serverPlant.items.length || player.getInventory().freeSlots() < serverPlant.items[0].getCount()) {
				player.getActionSender().sendMessage("You need more space before you harvest this.");
				return;
			}
			//you can harvest
			player.playAnimation(Animation.create(2292));
			player.getActionSender().sendReplaceObject(x + offset(serverPlant.type), y + offset(serverPlant.type), 6951, 0, 10);
			player.getActionSender().sendReplaceObject(x, y, plant.plotId, 0, 10);
			player.getFarm().plants.remove(((x * 16) + y));
			player.getSkills().addExperience(Skills.FARMING, serverPlant.xp * MULTIPLIER);
			player.getActionSender().sendMessage("You harvest the plant.");
			for(Item item : serverPlant.items) {
				player.getAchievementTracker().itemSkilled(Skills.FARMING, item.getId(), item.getCount());
				player.getInventory().add(item);
			}
		}
	}

	public static final int MULTIPLIER = 50;

	public void digUp(final Player player, int x, int y) {
		if(player.isBusy())
			return;
		PlayerPlant plant = player.getFarm().plants.get(((x * 16) + y));
		if(plant == null) {
			x--;
			y--;
			plant = player.getFarm().plants.get(((x * 16) + y));
			if(plant == null) {
				//plant has already been removed, this should be impossible to get
				return;
			}
		}
		Plant serverPlant = patches.get(plant.plotId).seeds.get(plant.seed);
		//you can harvest
		player.playAnimation(Animation.create(831));
		player.getActionSender().sendReplaceObject(x + offset(serverPlant.type), y + offset(serverPlant.type), plant.plotId, 0, 10);
		player.getFarm().plants.remove(((x * 16) + y));
		player.getActionSender().sendMessage("You dig up the plot.");
		World.submit(new Task(500,"farming1") {
			@Override
			public void execute() {
				this.stop();
				player.playAnimation(Animation.create(- 1));
			}
		});

	}

	public void inspect(final Player player, int x, int y) {
		if(player.isBusy())
			return;
		PlayerPlant plant = player.getFarm().plants.get(((x * 16) + y));
		if(plant == null) {
			x--;
			y--;
			plant = player.getFarm().plants.get(((x * 16) + y));
			if(plant == null) {
				//plant has already been removed, this should be impossible to get
				return;
			}
		}
		Plant serverPlant = patches.get(plant.plotId).seeds.get(plant.seed);
		int percent = ((plant.stage * 100) / (serverPlant.normalStages.length - 1));
		if(plant.dead) {
			player.getActionSender().sendMessage("This plant is dead");
		} else {
			player.getActionSender().sendMessage("This plant is " + percent + "% done growing.");
		}
	}

	public void rakePlot(final Player player, final int objId, final int x, final int y) {
		if(player.isBusy())
			return;
		final Patch patch = unRakedPatches.get(objId);
		if(patch == null) {
			player.getActionSender().sendMessage("You cannot rake this.");
			return;
		}
		player.setBusy(true);
		player.playAnimation(Animation.create(2273));
		player.getActionSender().sendMessage("You rake the plot of land.");
		World.submit(new Task(3000, "farming") {
			@Override
			public void execute() {
				if(! player.isBusy()) {
					this.stop();
					return;
				}
				player.getActionSender().sendReplaceObject(x, y, patch.newPatchId, 0, 10);
				player.getFarm().newRakeSpot(x, y, objId);
				player.getSkills().addExperience(Skills.FARMING, 43);
				player.setBusy(false);
				player.playAnimation(Animation.create(- 1));
				this.stop();
			}
		});
	}

	public static Map<Integer, Patch> unRakedPatches = new HashMap<Integer, Patch>();
	public static Map<Integer, Patch> patches = new HashMap<Integer, Patch>();
	public static Farming farming;


	public static Farming getFarming() {
		if(farming == null)
			farming = new Farming();
		return farming;
	}

	static Calendar calendar = new GregorianCalendar();

	public static int offset(int type) {
		switch(type) {
			case 2:
				return 1;
			default:
				return 0;
		}
	}

	@Override
	public void init() throws FileNotFoundException {
		farming = this;
		//seeds.put()8562,8561,8560,8559,
		Patch patch = new Patch(8573);
		patch.seeds.put(5318/*startObjId*/, new Plant(5318/*seed*/, 1/*level*/, 50/*xp*/, new int[]{8558, 8559, 8560, 8561, 8562,}/*normal*/, new int[]{8563, 8564, 8565, 8566,}/*watered*/, new int[]{8567, 8568, 8569, 8570,}/*dieseased*/, 10, 1/*vegie*/, new Item[]{new Item(1942, 5),}/*items*/));//potatoe
		patch.seeds.put(5319/*startObjId*/, new Plant(5319/*seed*/, 5/*Lvl*/, 60/*XP*/, new int[]{8580, 8581, 8582, 8583, 8584,},/*normal*/new int[]{8585, 8586, 8587, 8588,}/*watered*/, new int[]{8589, 8590, 8591, 8592,}/*dieseased*/, 10, 1/*vegie*/, new Item[]{new Item(1957, 5),}/*items*/));//onion
		patch.seeds.put(5324/*startObjId*/, new Plant(5324/*seed*/, 7/*Lvl*/, 70/*XP*/, new int[]{8535, 8536, 8537, 8538, 8539,},/*normal*/new int[]{8540, 8541, 8542, 8543,}/*watered*/, new int[]{8544, 8545, 8546, 8547,}/*dieseased*/, 10, 1/*vegie*/, new Item[]{new Item(1965, 5),}/*items*/));//cabbage
		patch.seeds.put(7562/*startObjId*/, new Plant(7562/*seed*/, 15/*Lvl*/, 80/*XP*/, new int[]{8641, 8642, 8643, 8644, 8645,},/*normal*/new int[]{8646, 8647, 8648, 8649,}/*watered*/, new int[]{8650, 8651, 8652, 8653,}/*dieseased*/, 10, 1/*vegie*/, new Item[]{new Item(1982, 5),}/*items*/));//tomato
		patch.seeds.put(5320/*startObjId*/, new Plant(5320/*seed*/, 20/*Lvl*/, 90/*XP*/, new int[]{8619, 8620, 8621, 8622, 8623, 8624},/*normal*/new int[]{8625, 8626, 8627, 8628, 8629}/*watered*/, new int[]{8630, 8631, 8632, 8633, 8634,}/*dieseased*/, 12, 1/*vegie*/, new Item[]{new Item(5986, 5),}/*items*/));//sweetcorn
		patch.seeds.put(5323/*startObjId*/, new Plant(5323/*seed*/, 31/*Lvl*/, 100/*XP*/, new int[]{8596, 8597, 8598, 8599, 8600, 8601,},/*normal*/new int[]{8602, 8603, 8604, 8605, 8606}/*watered*/, new int[]{8607, 8608, 8609, 8610, 8611,}/*dieseased*/, 12, 1/*vegie*/, new Item[]{new Item(5504, 5),}/*items*/));//strawberry
		patch.seeds.put(5321/*startObjId*/, new Plant(5321/*seed*/, 47/*Lvl*/, 110/*XP*/, new int[]{8657, 8658, 8659, 8660, 8661, 8662, 8663, 8664,},/*normal*/new int[]{8665, 8666, 8667, 8668, 8669, 8670, 8671,}/*watered*/, new int[]{8672, 8673, 8674, 8675, 8676, 8677, 8678,}/*dieseased*/, 16, 1/*vegie*/, new Item[]{new Item(5982, 5),}/*items*/));//watermelon
		unRakedPatches.put(8550, patch);
		unRakedPatches.put(8551, patch);
		unRakedPatches.put(8552, patch);
		unRakedPatches.put(8553, patch);


		patch = new Patch(8047);//fruit

		unRakedPatches.put(7965, patch);//fruit tree

		patch = new Patch(8392);//trees
		patch.seeds.put(5313/*startObjId*/, new Plant(5313/*seed*/, 30/*Lvl*/, 700/*XP*/, new int[]{8481, 8482, 8483, 8484, 8485, 8486, 8487, 8488,},/*normal*/new int[]{8481, 8482, 8483, 8484, 8485, 8486, 8487, 8488,}/*watered*/, new int[]{8490, 8491, 8491, 8492, 8493, 8494, 8495,}/*dieseased*/, 30, 2/*vegie*/, new Item[]{new Item(1519, 5),}/*items*/));//willow tree
		patch.seeds.put(5314/*startObjId*/, new Plant(5314/*seed*/, 45/*Lvl*/, 800/*XP*/, new int[]{8437, 8438, 8439, 8440, 8441, 8442, 8443, 8444,},/*normal*/new int[]{8437, 8438, 8439, 8440, 8441, 8442, 8443, 8444,}/*watered*/, new int[]{8446, 8447, 8448, 8449, 8450, 8451, 8452,}/*dieseased*/, 30, 2/*vegie*/, new Item[]{new Item(1517, 5),}/*items*/));//maple tree
		patch.seeds.put(5315/*startObjId*/, new Plant(5315/*seed*/, 60/*Lvl*/, 900/*XP*/, new int[]{8506, 8507, 8508, 8509, 8510, 8511, 8512, 8513,},/*normal*/new int[]{8506, 8507, 8508, 8509, 8510, 8511, 8512, 8513,}/*watered*/, new int[]{8515, 8516, 8517, 8518, 8519, 8520, 8521,}/*dieseased*/, 30, 2/*vegie*/, new Item[]{new Item(1515, 5),}/*items*/));//yew tree
		patch.seeds.put(5316/*startObjId*/, new Plant(5316/*seed*/, 75/*Lvl*/, 1000/*XP*/, new int[]{8396, 8397, 8398, 8399, 8400, 8401, 8402, 8403, 8404, 8405, 8406, 8407, 8408, 8409,},/*normal*/new int[]{8396, 8397, 8398, 8399, 8400, 8401, 8402, 8403, 8404, 8405, 8406, 8407, 8408, 8409,}/*watered*/, new int[]{8411, 8412, 8413, 8414, 8415, 8416, 8417, 8418, 8419, 8420, 8421, 8422, 8423,}/*dieseased*/, 46, 2/*vegie*/, new Item[]{new Item(1513, 5),}/*items*/));//magic tree
		unRakedPatches.put(8388, patch);//tree
		unRakedPatches.put(8387, patch);//tree
		unRakedPatches.put(8386, patch);//tree
		unRakedPatches.put(8385, patch);//tree
		unRakedPatches.put(8384, patch);//tree
		unRakedPatches.put(8383, patch);//tree
		unRakedPatches.put(8382, patch);//tree
		unRakedPatches.put(8389, patch);//tree


		patch = new Patch(7573);//bush

		unRakedPatches.put(7580, patch);//bush

		patch = new Patch(8207);//hops

		unRakedPatches.put(8176, patch);//hops

		patch = new Patch(7840);//flowers
		patch.seeds.put(5096/*startObjId*/, new Plant(5096/*seed*/, 2/*Lvl*/, 60/*XP*/, new int[]{7867, 7868, 7869, 7870, 7871,},/*normal*/new int[]{7872, 7873, 7874, 7875,}/*watered*/, new int[]{7876, 7877, 7878, 7879,}/*dieseased*/, 10, 5/*vegie*/, new Item[]{new Item(6010, 5),}/*items*/));//maigold
		patch.seeds.put(5097/*startObjId*/, new Plant(5097/*seed*/, 11/*Lvl*/, 70/*XP*/, new int[]{7899, 7900, 7901, 7902, 7903,},/*normal*/new int[]{7904, 7905, 7906, 7907,}/*watered*/, new int[]{7908, 7909, 7910, 7911,}/*dieseased*/, 10, 5/*vegie*/, new Item[]{new Item(6014, 5),}/*items*/));//Rosemary
		patch.seeds.put(5098/*startObjId*/, new Plant(5098/*seed*/, 24/*Lvl*/, 80/*XP*/, new int[]{7883, 7884, 7885, 7886, 7887,},/*normal*/new int[]{7888, 7889, 7890, 7891,}/*watered*/, new int[]{7892, 7893, 7894, 7895,}/*dieseased*/, 10, 5/*vegie*/, new Item[]{new Item(6012, 5),}/*items*/));//Nasturtium
		patch.seeds.put(5099/*startObjId*/, new Plant(5099/*seed*/, 25/*Lvl*/, 100/*XP*/, new int[]{7919, 7920, 7921, 7922, 7923,},/*normal*/new int[]{7924, 7925, 7926, 7927,}/*watered*/, new int[]{7928, 7929, 7930, 7931,}/*dieseased*/, 10, 5/*vegie*/, new Item[]{new Item(1793, 5),}/*items*/));//Woad
		patch.seeds.put(5100/*startObjId*/, new Plant(5100/*seed*/, 26/*Lvl*/, 120/*XP*/, new int[]{7851, 7852, 7853, 7854, 7855,},/*normal*/new int[]{7856, 7857, 7858, 7859,}/*watered*/, new int[]{7860, 7861, 7862, 7863,}/*dieseased*/, 10, 5/*vegie*/, new Item[]{new Item(225, 5),}/*items*/));//Limpwurt
		unRakedPatches.put(7847, patch);//flower
		unRakedPatches.put(7848, patch);//flower

		patch = new Patch(8132);//herbs

		int[] normal = {8139, 8140, 8140, 8141, 8142, 8143,};
		int[] water = {8139, 8140, 8140, 8141, 8142, 8143,};
		int[] dead = {8144, 8145, 8146, 8147, 8148,};

		patch.seeds.put(5291/*startObjId*/, new Plant(5291/*seed*/, 9/*Lvl*/, 85/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(249, 5),}/*items*/));//guam
		patch.seeds.put(5292/*startObjId*/, new Plant(5292/*seed*/, 14/*Lvl*/, 100/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(251, 5),}/*items*/));//marrentill
		patch.seeds.put(5293/*startObjId*/, new Plant(5293/*seed*/, 19/*Lvl*/, 110/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(253, 5),}/*items*/));//tarromin
		patch.seeds.put(5294/*startObjId*/, new Plant(5294/*seed*/, 26/*Lvl*/, 118/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(255, 5),}/*items*/));//harralander
		patch.seeds.put(5295/*startObjId*/, new Plant(5295/*seed*/, 32/*Lvl*/, 124/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(257, 5),}/*items*/));//ranarr
		patch.seeds.put(5296/*startObjId*/, new Plant(5296/*seed*/, 38/*Lvl*/, 134/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(2998, 5),}/*items*/));//toadflax
		patch.seeds.put(5297/*startObjId*/, new Plant(5297/*seed*/, 44/*Lvl*/, 150/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(259, 5),}/*items*/));//irit
		patch.seeds.put(5298/*startObjId*/, new Plant(5298/*seed*/, 50/*Lvl*/, 160/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(261, 5),}/*items*/));//avantoe
		patch.seeds.put(5299/*startObjId*/, new Plant(5299/*seed*/, 56/*Lvl*/, 166/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(263, 5),}/*items*/));//kwuarm
		patch.seeds.put(5300/*startObjId*/, new Plant(5300/*seed*/, 62/*Lvl*/, 174/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(3000, 5),}/*items*/));//snapdragon
		patch.seeds.put(5301/*startObjId*/, new Plant(5301/*seed*/, 67/*Lvl*/, 186/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(265, 5),}/*items*/));//cadantine
		patch.seeds.put(5302/*startObjId*/, new Plant(5302/*seed*/, 73/*Lvl*/, 200/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(2481, 5),}/*items*/));//lantadyme
		patch.seeds.put(5303/*startObjId*/, new Plant(5303/*seed*/, 79/*Lvl*/, 207/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(267, 5),}/*items*/));//dwarf weed
		patch.seeds.put(5304/*startObjId*/, new Plant(5304/*seed*/, 85/*Lvl*/, 220/*XP*/, normal,/*normal*/water/*watered*/, dead/*dieseased*/, 14, 3/*vegie*/, new Item[]{new Item(269, 5),}/*items*/));//torstol

		unRakedPatches.put(8150, patch);//herb
		unRakedPatches.put(8151, patch);//herb


		//unRakedPatches.put(7771, patch);//cactus
		//unRakedPatches.put(8338, patch);//spirit tree


		World.submit(new Task(30000,"farming3") {
			@Override
			public void execute() {
				calendar = new GregorianCalendar();
				for(Player player : World.getPlayers()) {
					synchronized(player.getFarm().rakePatches) {
						for(Object object : player.getFarm().rakePatches.values().toArray()) {
							RakePatch rpatch = (RakePatch) object;
							rpatch.timeLeft--;
							if(rpatch.timeLeft == 0) {
								player.getActionSender().sendReplaceObject(rpatch.x, rpatch.y, rpatch.unRakedObjId, 0, 10);
								player.getFarm().rakePatches.remove(((rpatch.x * 16) + rpatch.y));
							}
						}
					}
					synchronized(player.getFarm().plants) {
						for(Object object : player.getFarm().plants.values().toArray()) {
							PlayerPlant plant = (PlayerPlant) object;
							Plant serverPlant = patches.get(plant.plotId).seeds.get(plant.seed);

							if(ticksExecuted(plant.day, plant.hour, plant.minute) >= (serverPlant.stageGrowthTime * (plant.stage + 1)) && (plant.stage + 1) < serverPlant.normalStages.length){
								plant.stage++;
								player.getActionSender().sendReplaceObject(plant.x + offset(serverPlant.type), plant.y + offset(serverPlant.type), serverPlant.normalStages[plant.stage], 0, 10);
							} else if(ticksExecuted(plant.day, plant.hour, plant.minute) > (8 * serverPlant.growTime)) {
								//plant dies
								plant.dead = true;
								player.getActionSender().sendReplaceObject(plant.x + offset(serverPlant.type), plant.y + offset(serverPlant.type), serverPlant.dieseasedStages[(serverPlant.dieseasedStages.length - 1)], 0, 10);
							}
						}
					}
	                /*for(Object object : player.getFarm().plants.toArray()){
                        PlayerPlant plant = (PlayerPlant) object;
						plant.timeLeft--;
						plant.ticksSinceGrowth++;
						Plant serverPlant = patches.get(plant.plotId).seeds.get(plant.seed);
						if(serverPlant.stageGrowthTime == plant.ticksSinceGrowth && plant.stage != (serverPlant.normalStages.length-1)){
							plant.ticksSinceGrowth = 0;
							plant.stage++;
							player.getActionSender().sendReplaceObject(plant.x, plant.y, serverPlant.normalStages[plant.stage], 0, 10);
						} else if(plant.timeLeft < (-2*serverPlant.growTime)){
							//plant dies
							plant.dead = true;
							player.getActionSender().sendReplaceObject(plant.x, plant.y, serverPlant.dieseasedStages[(serverPlant.dieseasedStages.length-1)], 0, 10);
						}*/
						
						
						/*plant.growTime--;
						if(rpatch.timeLeft == 0){
							player.getActionSender().sendReplaceObject(rpatch.x, rpatch.y, rpatch.unRakedObjId, 0, 10);
							player.getFarm().rakePatches.remove(rpatch);
						}*/
					//}
				}
			}
		});
	}

	@Override
	public int[] getValues(int type) {
		if(type == 19) {
			int[] j = {952, 5341, 5318, 5319, 5324, 7562, 5320, 5323, 5321, 5313, 5314, 5315, 5316, 5291, 5292, 5293, 5294, 5295, 5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304,};
			return j;
		} else if(type == 6) {
			//harvest
			int[] j = new int[150];
			int i = 0;
			for(Object object : patches.values().toArray()) {
				Patch patch = (Patch) object;
				for(Object object2 : patch.seeds.values().toArray()) {
					Plant plant = (Plant) object2;
					j[i++] = plant.normalStages[(plant.normalStages.length - 1)];
				}
			}
			return j;
		} else if(type == 7) {
			//inspec
			int[] j = new int[1000];
			int i = 0;
			for(Object object : patches.values().toArray()) {
				Patch patch = (Patch) object;
				for(Object object2 : patch.seeds.values().toArray()) {
					Plant plant = (Plant) object2;
					for(int i2 = 0; i2 < plant.normalStages.length; i2++) {
						j[i++] = plant.normalStages[i2];
					}
					for(int i2 = 0; i2 < plant.wetStages.length; i2++) {
						j[i++] = plant.wetStages[i2];
					}
					for(int i2 = 0; i2 < plant.dieseasedStages.length; i2++) {
						j[i++] = plant.dieseasedStages[i2];
					}
				}
			}
			return j;
		}
		return null;
	}


	@Override
	public boolean clickObject(final Player client, final int type, final int itemId, final int slot, final int objId, final int a) {
		if(type == 19 && itemId == 5341) {
			//raking
			rakePlot(client, slot, objId, a);
			return true;
		} else if(type == 19 && itemId == 952) {
			digUp(client, objId, a);
			return true;
		} else if(type == 19) {
			plantSeed(client, itemId, slot, objId, a);
			return true;
		} else if(type == 6) {
			harvest(client, itemId, slot, objId);
			return true;
		} else if(type == 7) {
			inspect(client, slot, objId);
			return true;
		}
		return false;
	}

}
