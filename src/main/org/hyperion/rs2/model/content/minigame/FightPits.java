package org.hyperion.rs2.model.content.minigame;

import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.impl.OverloadStatsTask;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.content.ClickId;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.minigame.DangerousPK.ArmourClass;
import org.hyperion.rs2.util.PushMessage;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class FightPits implements ContentTemplate {

	private static boolean ENABLED = false;

	public static void setEnabled(final boolean value) {
		ENABLED = value;
	}

	public static boolean getEnabled() {
		return ENABLED;
	}

	public static final int RED_CAPE = 1007;
	
	public static final int BLUE_CAPE = 1021;
	
	public static boolean EVENT = false;
	
	public static boolean NEXT_GAME_EVENT = false;
	
	public static int gameTimeLeft;
	
	public static CopyOnWriteArrayList<Player> waitingRoom = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<Player> playersInGame = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<Player> teamRed = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<Player> teamBlue = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<NPC> monsters;
	private static String lastChamp;
	private static int timeLeft;
	
	public static List<List<Item>> meleeItems = new ArrayList<>();
	public static List<List<Item>> rangeItems = new ArrayList<>();
	public static List<List<Item>> mageItems = new ArrayList<>();
	
	public static List<Integer> scItems = new Vector<Integer>();
	public static List<Integer> rewardItems = new ArrayList<Integer>();
	
	static int startPlayersAmount = 0;
	
	//random items given to any class at start of game
	public static final List<Integer> list = Arrays.asList(
			6585, 1712, 1731, 7462, 7461, 7460, 7459, 7458, 3024, 6685, 15330);
	
	private static List<Integer> arrows = Arrays.asList(11212, 892, 890, 888);
	
	//list storing random items for each class
	@SuppressWarnings("serial")
	public static final List<List<Integer>> classItems = new ArrayList<List<Integer>>() {
			{
				//mage
				add(Arrays.asList(6731, 2579, 6920, 3040));
				
				//melee
				add(Arrays.asList(6735, 6737, 11732, 3105, 4131, 1305, 11696, 11700, 1725));
				
				//range
				add(Arrays.asList(11235, 2577, 2444, 6733));
			}
	};
	
	public enum Spells {
		ICE_BARRAGE(true, 555, 565, 560),
		BLOOD_BARRAGE(true, 560, 565, 566),
		SHADOW_BARRAGE(true, 560, 565, 566, 556),
		SMOKE_BARRAGE(true, 560, 565, 556, 554),
		ICE_BURST(true, 555, 562, 560),
		BLOOD_BURST(true, 560, 565, 562),
		SHADOW_BURST(true, 560, 562, 566, 556),
		SMOKE_BURST(true, 560, 562, 556, 554),
		FIRE_WAVE(false, 554, 556, 565),
		EARTH_WAVE(false, 557, 556, 565),
		WATER_WAVE(false, 555, 556, 565),
		AIR_WAVE(false, 556, 565),
		FIRE_BLAST(false, 554, 556, 560),
		EARTH_BLAST(false, 557, 556, 560),
		WATER_BLAST(false, 555, 556, 560),
		AIR_BLAST(false, 556, 560);


		
		private boolean ancients;
		private int[] runes;
		Spells(boolean ancients, int... runes) {
			this.ancients = ancients;
			this.runes = runes;
		}
		public int[] getRunes() {
			return runes;
		}
		public boolean isMagik() {
			return ancients;
		}
		
		public String toString() {
			return TextUtils.titleCase(super.toString().replaceAll("_", " "));
		}
	}
	
	public static void startEvent() {
		NEXT_GAME_EVENT = true;
	}
	
	
	public static final boolean inPitsFightArea(int x, int y) {
		return x <= 2420 && y >= 5129 && y <= 5168 && x >= 2375;
	}

	private static <V> V random(List<V> list) {
		return list.get(Misc.random(list.size()- 1));
	}	
	
	
	public static boolean canJoin(Player player) {
        if(player.hardMode())
            return false;
		return !(ContentEntity.getTotalAmountOfEquipmentItems(player) > 0 
				|| ContentEntity.getTotalAmountOfItems(player) > 0);
	}
	
	public static int getLifePointBoost(Player player) {
		int boost = 0;
		Container equip = player.getEquipment();
		for(int i : rewardItems) {
			int slot = Equipment.getType(Item.create(i)).getSlot();
				if(equip.get(slot) != null && equip.get(slot).getId() == i)
					boost += getBoost(slot);
		}
		return boost;
	}
	
	public static int getBoost(int slot) {
		switch(slot) {
		case Equipment.SLOT_HELM:
			return 5;
		case Equipment.SLOT_CHEST:
			return 12;
		case Equipment.SLOT_BOTTOMS:
			return 8;
		case Equipment.SLOT_WEAPON:
			return 5;
			default: return 0;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void getClass(Player player) {
		int r = player.getExtraData().getInt("pitdeaths");
		switch(r) {
		case 0:
			player.pickedClass = ArmourClass.MELEE;
			break;
		case 1:
			player.pickedClass = ArmourClass.RANGE;
			break;
		default:
			player.pickedClass = ArmourClass.MAGE;
			break;
		}
	}
	


	public static Object[] hasItems(Player player) {
		for(Item item : player.getEquipment().toArray()) {
			if(item != null && scItems.contains(item.getId()))
				return new Object[]{Equipment.getType(item).getSlot(), true};
		}
		for(Item item : player.getInventory().toArray()) {
			if(item != null && scItems.contains(item.getId()))
				return new Object[]{player.getInventory().getSlotById(item.getId()), false};
		}
		return null;
	}
	
	public static boolean isSameTeam(Player player, Player opp) {
		return (teamBlue.contains(player) && teamBlue.contains(opp)) ||
				(teamRed.contains(player) && teamRed.contains(opp)); 
	}
	
	public static boolean isBow(int id) {
		for(Item i : FightPits.rangeItems.get(WEAPON)) {
			if(i != null && i.getId() == id)
				return true;
		}
		return false;
	}
	
	public static void fightPitsCheck(Player player) {
		if(!player.getLocation().equals(Locations.Location.FIGHT_PITS) && inGame(player) && !waitingRoom.contains(player) && !player.joiningPits
				|| Server.isUpdating()) {
			removePlayerFromGame(player, true);
		}
		if(player.joiningPits)
			player.joiningPits = false;
	}
	
	public static void dissapate(Player player) throws StackOverflowError{
		Object[] data = hasItems(player);
		if(data != null) {
			int slot = (Integer)data[0];
			boolean equip = (Boolean)data[1];
			if(equip)
				player.getEquipment().set(slot, null);
			else
				player.getInventory().set(slot, null);
			data = null;
			dissapate(player);
		}
		data = null;
	}
	
	public static int HELM = 0, BODY = 1, LEGS = 2, WEAPON = 3;
	
	public static boolean isNoted(ItemDefinition itemDef) {
		ItemDefinition previous = ItemDefinition.forId(itemDef.getId() - 1);
		return previous != null && previous.getName().equalsIgnoreCase(itemDef.getName());
	}
	public void init() throws FileNotFoundException {
		World.submit(new Task(1000,"fightpits1") {
			@Override
			public void execute() {
				process();
			}
		});
		waitingRoom = new CopyOnWriteArrayList<>();
		teamRed = new CopyOnWriteArrayList<>();
		teamBlue = new CopyOnWriteArrayList<>();
		playersInGame = new CopyOnWriteArrayList<>();
		monsters = new CopyOnWriteArrayList<>();
		lastChamp = "";
		timeLeft = 0;
		for(int i = 0; i < 4; i++) {
			meleeItems.add(new ArrayList<>());
			rangeItems.add(new ArrayList<>());
			mageItems.add(new ArrayList<>());

		}
		for(int id = 14000; id < 15000; id++) {
			ItemDefinition itemDef = ItemDefinition.forId(id);
			String name = null;
			if(itemDef != null)
				name = itemDef.getName();
				if(name == null)
					continue;
				name = name.toLowerCase();
			if(name.contains("sacred") && name.contains("clay")) {
				if(name.contains("needle") || name.contains("pouch") || name.contains("null")
						|| name.contains("axe") || name.contains("pickaxe") || name.contains("harpoon") ||
						name.contains("net") || name.contains("class") || name.contains("hammer") || name.contains("knife"))
					continue;
				rewardItems.add(itemDef.getId());
				
			}
			if(name != null && name.contains("(class")) {
				if(isNoted(itemDef))
					continue;
				//weapons
				if(name.contains("dagger") || name.contains("scim") || name.contains("warhammer"))
					meleeItems.get(WEAPON).add(Item.create(id));
				if(name.contains("bow"))
					rangeItems.get(WEAPON).add(Item.create(id));
				if(name.contains("staff"))
					mageItems.get(WEAPON).add(Item.create(id));
				//bodies
				if(name.contains("platebody"))
					meleeItems.get(BODY).add(Item.create(id));
				if(name.contains("leather body"))
					rangeItems.get(BODY).add(Item.create(id));
				if(name.contains("robe top"))
					mageItems.get(BODY).add(Item.create(id));
				//legs
				if(name.contains("platelegs"))
					meleeItems.get(LEGS).add(Item.create(id));
				if(name.contains("chap"))
					rangeItems.get(LEGS).add(Item.create(id));
				if(name.contains("robe bottom"))
					mageItems.get(LEGS).add(Item.create(id));
				//helms
				if(name.contains("helm"))
					meleeItems.get(HELM).add(Item.create(id));
				if(name.contains("coif"))
					rangeItems.get(HELM).add(Item.create(id));
				if(name.contains("hat"))
					mageItems.get(HELM).add(Item.create(id));
									
			}
			
			for(List<Item> $list : meleeItems) {
				for(Item _item : $list) {
					if(_item != null)
						scItems.add(_item.getId());
				}
			}
			for(List<Item> $list : rangeItems) {
				for(Item _item : $list) {
					if(_item != null)
						scItems.add(_item.getId());
				}
			}
			for(List<Item> $list : mageItems) {
				for(Item _item : $list) {
					if(_item != null)
						scItems.add(_item.getId());
				}
			}
		}
	}

	public int[] getValues(int i) {
		if(i == 6) {
			int responseIds[] = {
					9369, 9368, 30000, 31000, 30001
			};
			return responseIds;
		} else {
			if(i != 16) ;
			return null;
		}
	}

	public boolean clickObject(Player player, int clickType, int clickId, int k, int l, int i1) {
		if(clickType == ClickType.OBJECT_CLICK1) {
				if(clickId == 9369) { //enter lobby
					if (ENABLED) {
						firstDoor(player);
					} else {
						player.sendMessage("@red@Currently Disabled@bla@.");
					}
				} else if(clickId == 9368) { //leave fight pits in game
					if (ENABLED) {
						secondDoor(player);
					} else {
						player.sendMessage("@red@Currently Disabled@bla@.");
					}
				}
			if(clickId == ClickId.ATTACKABLE) {
				return inGame(player);
			}
			if(clickId == ClickId.CAN_TELEPORT) {
				return waitingRoom.contains(player) || inGame(player) || player.getDungeoneering().inDungeon();
			}
			if(clickId == ClickId.FIGHT_PITS_DEATH) {
			}
		}
		if(clickType != ClickType.NPC_DEATH) ;
		return true;
	}

	public static boolean pitsDeath(Player player) {
		if (!FightPits.inGame(player))
			return false;
		if (player.getExtraData().getInt("pitdeaths") >= 2) {
			removePlayerFromGame(player, true);
		} else {
			player.getExtraData().put("pitdeaths", player.getExtraData().getInt("pitdeaths") + 1);
			player.getInventory().clear();
			player.getEquipment().clear();
			spawnItems(player);
			if(teamRed.contains(player))
				player.getEquipment().set(Equipment.SLOT_CAPE, Item.create(RED_CAPE));
			else
				player.getEquipment().set(Equipment.SLOT_CAPE, Item.create(BLUE_CAPE));
			player.setTeleportTarget(getSpawnLoc(), false);
		}
		return true;
	}
	
	private static void normalize(Player player) {
		player.setOverloaded(false);
		player.getExtraData().remove(OverloadStatsTask.KEY);
		player.overloadTimer = 0;
		for(int i = 0; i < 6; i++) {
			player.getSkills().normalizeLevel(i);
		}

		player.resetPrayers();
		Duel.healup(player);
	}
	
	private void startGame() {
		startPlayersAmount = waitingRoom.size();
		for(Player player : waitingRoom) {
			
			if(player.getCombat().getFamiliar() != null) {
				if(player.getBoB() != null)
                    player.getBoB().clear();
				player.SummoningCounter = 0;
			}
			player.getActionSender().sendClientConfig(560, 1);
			player.getActionSender().sendPlayerOption("Heal", 5, 0);
			player.joiningPits = true;
			player.setTeleportTarget(getSpawnLoc());
			playersInGame.add(player);
			normalize(player);
			player.getInventory().clear();
			player.getEquipment().clear();
			final Player p2 = player;
			World.submit(new Task(600, "fightpits") {
				public void execute() {
                    spawnItems(p2);
					this.stop();
				}
			});
		}
	}
	
	
	
	public static void spawnItems(Player player) {
        player.getInventory().add(new Item(391, Misc.random(1) + 1));
        getClass(player);
		switch(player.pickedClass) {
		case MAGE:
			for(int i = 0; i < 4; i++) {
				List<Item> $list = mageItems.get(i);
				player.getInventory().add($list.get(Misc.random($list.size() - 1)));
			}
			spawnSpells(player);
			break;
		case MELEE:
			for(int i = 0; i < 4; i++) {
				List<Item> $list = meleeItems.get(i);
				player.getInventory().add($list.get(Misc.random($list.size() - 1)));
			}
			break;
		case RANGE:
			for(int i = 0; i < 4; i++) {
				List<Item> $list = rangeItems.get(i);
				player.getInventory().add($list.get(Misc.random($list.size() - 1)));
			}
			player.getInventory().add(new Item(FightPits.<Integer>random(arrows), 100));

			break;
		}
		player.getInventory().add(Item.create(random(list)));
		player.getInventory().add(Item.create(random(classItems.get(player.pickedClass.ordinal()))));
		player.sendMessage("You've become a: "+player.pickedClass.toString());
		
	}
	
	public static void spawnSpells(Player player) {
		Spells s = Spells.values()[Misc.random(Spells.values().length - 1)];
		if(!s.isMagik()) {
			player.getSpellBook().changeSpellBook(SpellBook.REGULAR_SPELLBOOK);
			player.getActionSender().sendSidebarInterface(6, 1151);
		} else {
			player.getSpellBook().changeSpellBook(SpellBook.ANCIENT_SPELLBOOK);
			player.getActionSender().sendSidebarInterface(6, 12855);
		}
		
		player.sendMessage("You've been given the spell: "+s.toString()+"!");
		
		for(int i : s.getRunes())
			player.getInventory().add(Item.create(i, 1000));
		
	}
	
	public static boolean inPits(Player player) {
		return waitingRoom.contains(player) || playersInGame.contains(player);
	}
	
	public static String getMinutes(int seconds) {
		int minutes = seconds/60;
		int secondsRemaining = seconds%60;
		StringBuilder builder = new StringBuilder().append(minutes).append(":");
		if(secondsRemaining >= 10)
			builder.append(secondsRemaining);
		else
			builder.append("0").append(secondsRemaining);
		return builder.toString();
	}
	
	
	
	private static void assignBlue(Player player) {
		player.getActionSender().sendMessage("You are part of the @blu@blue@bla@ team");
		player.getEquipment().set(Equipment.SLOT_CAPE, Item.create(BLUE_CAPE));
		teamBlue.add(player);
		ClanManager.joinClanChat(player, "Team Blue", false);
	}
	
	private static void assignRed(Player player) {
		player.getActionSender().sendMessage("You are part of the @dre@red@bla@ team");
		player.getEquipment().set(Equipment.SLOT_CAPE, Item.create(RED_CAPE));
		teamRed.add(player);
		ClanManager.joinClanChat(player, "Team Red", false);

	}
	
	public static String getFoesRemaining(Player player) {
		StringBuilder builder = new StringBuilder("Foes Remaining: ");
		if(teamBlue.contains(player))
			builder.append(teamRed.size());
		else
			builder.append(teamBlue.size());
		return builder.toString();
	}
	
	public static void getTeams() {
		for(int index = 0; index < waitingRoom.size(); index++) {
			if(index%2 == 0)
				assignRed(waitingRoom.get(index));
			else
				assignBlue(waitingRoom.get(index));
		}
		waitingRoom.stream().filter(target -> target != null).forEach(target -> target.getActionSender().sendString(2806, getFoesRemaining(target)));
	}
	
	private void process() {
		if(playersInGame.size() > 1) {
	        if(--gameTimeLeft <= 0) {
				for(Player player : playersInGame) {
					if(player != null) {
	        			player.getActionSender().sendString(2805, "Game Ending" );
						player.getCombat().hit(5, null, false, Constants.DEFLECT);
						player.getCombat().hit(5, null, false, Constants.DEFLECT);
						if(player.getCombat().getFamiliar() != null)
							player.SummoningCounter = 0;
					}
				}
	        } else {
	        	for(Player player : playersInGame) {
	        		if(player != null)
	        			player.getActionSender().sendString(2805, "Time remaining: "+getMinutes(gameTimeLeft) );
	        	}
	        }
	        
		} else if(waitingRoom.size() >= 2) {
			if(timeLeft == 0) {
				playersInGame.clear();
				gameTimeLeft = 240 + waitingRoom.size() * 15;
				if(EVENT)
					gameTimeLeft += 160;
				timeLeft = gameTimeLeft + 15 + World.getPlayers().size()/3;
				if(NEXT_GAME_EVENT)
					timeLeft += 35;
				startGame();
				getTeams();
				waitingRoom.clear();
			}
		}
		StringBuilder timeRemaining = new StringBuilder().append("Time left: ").append(getMinutes(timeLeft));
		if(EVENT)
			timeRemaining.append(" [3X EVENT]");
		for(Player player : waitingRoom) {
			player.getActionSender().sendString(2806, timeRemaining.toString());
		}
		if(--timeLeft == -1) {
			for(Player player : waitingRoom) {
				player.getActionSender().sendMessage("You need 2 players to start a game!");
			}
			timeLeft = 15 + World.getPlayers().size()/3;
		}
	}

	@SuppressWarnings("unused")
	private void spawnNpc(int i, Position position) {
		NPC npc = NPCManager.addNPC(position.getX(), position.getY(), 0, i, - 1);
		npc.agressiveDis = 150;
		monsters.add(npc);
	}

	private void firstDoor(Player player) {
		if (Server.isUpdating()) {
			player.sendMessage("You cannot do this at this time.");
			return;
		}
		if(waitingRoom.contains(player)) {
			player.getWalkingQueue().reset();
			player.getWalkingQueue().addStep(2399, 5177);
			player.getWalkingQueue().finish();
			waitingRoom.remove(player);
			player.getActionSender().showInterfaceWalkable(- 1);

		} else {
			if(player.isMagicTeleporting()) {
				return;
			}
			if(!canJoin(player)) {
				player.getActionSender().sendMessage("Please bank all of your items before joining!");
				return;
			}
			if (waitingRoom.stream().filter(target -> target != null).anyMatch(target -> (target.getUID() == player.getUID()) || (target.getShortIP() == player.getShortIP()) || target.getLastMac() == player.getLastMac())) {
				player.sendf("You can only have one account in here at a time!");
				return;
			}
			player.SummoningCounter = 0;
			player.getWalkingQueue().reset();
			player.getWalkingQueue().addStep(2399, 5175);
			player.getWalkingQueue().finish();
			waitingRoom.add(player);
			player.getActionSender().showInterfaceWalkable(2804);
			player.getActionSender().sendClientConfig(560, 1);
			player.getActionSender().sendString(2805, (new StringBuilder()).append("Current Champion: ").append(lastChamp).toString());
			int i = timeLeft;
			if(i == - 1) {
				player.getActionSender().sendString(2806, "Not enough players.");
			} else {
				if(i > 60) {
					i /= 60;
				}
				player.getActionSender().sendString(2806, (new StringBuilder()).append("Time left: ").append(i).toString());
			}
		}
		player.getActionSender().sendMessage("You pass through the hot vent.");
	}

	private void secondDoor(Player player) {
		if(playersInGame.contains(player)) {
			player.getWalkingQueue().reset();
			player.getWalkingQueue().addStep(2399, 5169);
			player.getWalkingQueue().finish();
			player.getActionSender().sendMessage("You pass through the hot vent.");
			removePlayerFromGame(player, false);
			waitingRoom.add(player);
		}
	}
	
	
	public static void removePlayerFromGame(Player player, boolean flag) {
		player.getActionSender().showInterfaceWalkable(- 1);
        player.getExtraData().put("pitdeaths", 0);
		boolean getReward = player.getPitsDamage() > 50;
		if(waitingRoom.contains(player)) {
			waitingRoom.remove(player);
			if(flag) {
				player.setTeleportTarget(Position.create(2399, 5177, 0), false);
			}
		}
		if(playersInGame.contains(player)) {
			playersInGame.remove(player);
			if(!teamRed.remove(player))
				teamBlue.remove(player);
			if(flag) {
				player.setTeleportTarget(Position.create(2399, 5177, 0), false);
				int timeStood = ((400 + startPlayersAmount * 15) - gameTimeLeft)/50;
				if(timeStood > 12)
					timeStood = 12;
				if(getReward) {
                    player.getBank().add(new BankItem(0, 5020, timeStood));
					player.sendMessage(String.format("%d PK Tickets were sent to your bank.", timeStood));
                    //player.getPoints().increaseMinigamePoints(1);
				} else {
					player.sendMessage("You don't get any reward due to lack of participation");
				}
			}
			for(Player player2 : playersInGame) {
				player2.getActionSender().sendString(2806, getFoesRemaining(player2));
			}
			ClanManager.leaveChat(player, true,false);
			if(Rank.hasAbility(player, Rank.MODERATOR))
				player.getActionSender().sendPlayerOption("Moderate", 5, 0);
			else
				player.getActionSender().sendPlayerOption("null", 5, 0);
			player.getEquipment().clear();
			player.getInventory().clear();
			player.setPitsDamage(0);

		}
		if((teamRed.size() == 0 || teamBlue.size() == 0) && playersInGame.size() >= 1) {
			String winningTeam = "";
			List<Player> winner = null;
			List<Player> loser = null;
			if(teamRed.size() == 0) {
				winner = teamBlue;
				loser = teamRed;
				winningTeam = "Team Blue";
			} else {
				winner = teamRed;
				loser = teamBlue;
				winningTeam = "Team Red";
			}
			int size = winner.size();
			if(winner.size() == 0)
				return;
			if(EVENT)
				PushMessage.pushGlobalMessage(
					String.format("%s has just won fight pits with only %d player " + (size > 0 ? "s" : "") +  " left!", winningTeam, size));
			for(Player player2 : loser) {
				player2.getAchievementTracker().fightPitsLose();
			}
			for(Player player1 : winner) {
				if(player1 == null)
					continue;
				getReward = player1.getPitsDamage() > 50;
				ClanManager.leaveChat(player1, true,false);
				if(Rank.hasAbility(player1, Rank.MODERATOR))
					player1.getActionSender().sendPlayerOption("Moderate", 5, 0);
				else
					player1.getActionSender().sendPlayerOption("null", 5, 0);
				if(startPlayersAmount > 50)
					startPlayersAmount = 50;
				if(startPlayersAmount <= 0)
					startPlayersAmount = 1;
				int rewardCount =  8 + (startPlayersAmount/size);
				if(EVENT)
					rewardCount *= 3;
				lastChamp = winningTeam;
				player1.getEquipment().clear();
				player1.getInventory().clear();
				player1.setTeleportTarget(Position.create(2399, 5177, 0));
				player1.getActionSender().showInterfaceWalkable(- 1);
				if(getReward) {
					player1.getBank().add(new Item(5020, rewardCount));
					player1.getActionSender().sendMessage(String.format("%d Pk tickets have been sent to your bank", rewardCount));
                    //player1.getPoints().increaseMinigamePoints(2);
                } else {
					player1.getActionSender().sendMessage("You get no reward due to lack of participation");
				}
				player1.setPitsDamage(0);
				if(startPlayersAmount > 5 && getReward) {
					int dpAmt = startPlayersAmount/size;
					player1.getAchievementTracker().fightPitsWin();
					player1.sendServerMessage("For winning a large game, you gain @gre@"+dpAmt+"@red@ donator points!");
					player1.getPoints().increaseDonatorPoints(dpAmt, false);
					if(Misc.random(60 * size) == 1) { //if 10 chance is 1/80 in total (half players each have chance of 1/400)
						Item reward = Item.create(random(rewardItems));
						player1.sendMessage("You receive a "+reward.getDefinition().getName());
						player1.getExpectedValues().addItemtoInventory("Fight pits reward", reward);
						PushMessage.pushGlobalMessage("@gre@[Loot] " + player1.getSafeDisplayName() + " has just received a " + reward.getDefinition().getName() + " from fight pits!");
						player1.getInventory().add(reward);
					}
				}
			}
			if(EVENT)
				EVENT = false;
			if(NEXT_GAME_EVENT) {
				EVENT = true;
				NEXT_GAME_EVENT=false;
			}
			teamRed.clear();
			teamBlue.clear();
			playersInGame.clear();
			timeLeft = 35;
			/*if(monsters.size() > 0)
            {
				for(NPC npc : monsters){
                    npc.serverKilled = true;
                    if(!npc.isDead())
                    {
                        World.submit(new DeathEvent(npc));
                    }
                    npc.setDead(true);
                    npc.health = 0;
                }

                monsters.clear();
            }*/
		}
	}

	public static boolean inGame(Player player) {
		return playersInGame.contains(player);
	}

	public static Position getSpawnLoc() {
		int ai = 2386;
		int ai1 = 5151;
		int ai2 = 18;
		int ai3 = 10;
		return Position.create(ai + Combat.random(ai2), ai1 + Combat.random(ai3), 0);
	}

}
