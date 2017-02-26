package org.hyperion.rs2.model.content.minigame;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CastleWars implements ContentTemplate {

	/* this is for castle of death arghhhh!
	 */
	@Override
	public boolean clickObject(Player player, int type, int id, int b, int c,
	                           int d) {
		if(type == 6) {
			if(id == 4408) {//guthix enter
				if(waitingRoomZammy.size() < waitingRoomSara.size())
					joinWaitingRoomZammy(player);
				else
					joinWaitingRoomSara(player);
			}
			if(id == 4387) {//sara portal enter
				joinWaitingRoomSara(player);
			}
			if(id == 4388) {//zammy enter
				joinWaitingRoomZammy(player);
			}
			return true;
		}
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
		castleWars = this;
		World.submit(new Task(1000, "castlewars") {
			@Override
			public void execute() {
				process();
			}
		});
	}

	@Override
	public int[] getValues(int type) {
		if(type == 6) {
			int[] j = {4408, 4387, 4388,};
			return j;
		}
		return null;
	}

	public void joinWaitingRoomSara(Player player) {
		if(waitingRoomZammy.size() < waitingRoomSara.size()) {
			player.getActionSender().sendMessage("This team currently has too many players.");
			return;
		}
		player.setTeleportTarget(Position.create(2379, 9489, 0));
		waitingRoomSara.add(player);
		openInterface(player);
	}

	public void joinWaitingRoomZammy(Player player) {
		if(waitingRoomSara.size() < waitingRoomZammy.size()) {
			player.getActionSender().sendMessage("This team currently has too many players.");
			return;
		}
		player.setTeleportTarget(Position.create(2421, 9523, 0));
		waitingRoomZammy.add(player);
		openInterface(player);
		//add player cape onto them
	}

	public void process() {

		if(gameInProgress) {
			timeLeft--;
			if(timeLeft == 0 || gameZammy.size() == 0 || gameSara.size() == 0)
				endGame();
		} else {
			//we are counting down until game begins :) must be a minimum of 3 players on ea team
			if(waitingRoomZammy.size() < 1 || waitingRoomSara.size() < 1) {
				return;
			}
			//System.out.println("cw tick");
			timeLeft--;
			int newTime = (timeLeft / 60) - 1;
			if(timeInMinsLeft != newTime || newTime <= 0) {
				timeInMinsLeft = newTime;
				updateTimer();
			}
			if(timeLeft <= 0)
				startGame();
			//ok count down a bit
		}
	}

	private void openInterface(Player player) {
		player.getActionSender().showInterfaceWalkable(11479);
		if(waitingRoomZammy.size() == 1 || waitingRoomSara.size() == 1) {
			resetTimer(1);
		}
		if(waitingRoomZammy.size() < 1 || waitingRoomSara.size() < 1) {
			player.getActionSender().sendClientConfig(380, 0);
			return;
		} else {
			player.getActionSender().sendClientConfig(380, 1);
			player.getActionSender().sendString(11480, "Time until next game starts: " + (timeLeft / 60));
		}
	}

	public void updateTimer() {
		int timeShowing = (timeLeft / 60);
		if(timeShowing == 0)
			timeShowing = timeLeft;
		for(Player player : waitingRoomZammy) {
			player.getActionSender().sendString(11480, "Time until next game starts: " + timeShowing);
		}
		for(Player player : waitingRoomSara) {
			player.getActionSender().sendString(11480, "Time until next game starts: " + timeShowing);
		}
	}

	public void resetTimer(int value) {
		for(Player player : waitingRoomZammy) {
			player.getActionSender().sendClientConfig(380, value);
		}
		for(Player player : waitingRoomSara) {
			player.getActionSender().sendClientConfig(380, value);
		}
	}

	private void startGame() {
		gameInProgress = true;
		timeLeft = 600;//10 min games?
		for(Player player : waitingRoomZammy) {
			player.setTeleportTarget(zammySpawn);
			refreshScore(player);
			player.getActionSender().showInterfaceWalkable(11344);
			gameZammy.add(player);
		}
		for(Player player : waitingRoomSara) {
			player.setTeleportTarget(saraSpawn);
			refreshScore(player);
			player.getActionSender().showInterfaceWalkable(11344);
			gameSara.add(player);
		}
		waitingRoomSara.clear();
		waitingRoomZammy.clear();
	}

	private void refreshScore(Player player) {
		player.getActionSender().sendString(11345, "Zamorak: " + zammyKills);
		player.getActionSender().sendString(11346, "Saradomin: " + saraKills);
	}

	private void endGame() {
		gameInProgress = false;
		timeLeft = 120;//em 2 mins waiting timer?
		boolean draw = true;
		if(zammyKills > saraKills)
			draw = false;
		for(Player player : gameZammy) {
			player.setTeleportTarget(lobby);
			player.setDead(false);
			player.heal(99);
			giveWinItems(player, draw);
		}
		draw = saraKills <= zammyKills;
		for(Player player : gameSara) {
			player.setTeleportTarget(lobby);
			player.setDead(false);
			player.heal(99);
			giveWinItems(player, draw);
		}
		gameSara.clear();
		gameZammy.clear();
	}

	private static CastleWars castleWars = null;

	public static CastleWars getCastleWars() {
		if(castleWars == null)
			castleWars = new CastleWars();
		return castleWars;
	}

	public boolean playerHasDied(Player player) {
		if(gameSara.contains(player)) {
			zammyKills++;
			player.setTeleportTarget(saraSpawn);
			return true;
		}
		if(gameZammy.contains(player)) {
			saraKills++;
			player.setTeleportTarget(zammySpawn);
			return true;
		}
		for(Player player2 : waitingRoomZammy) {
			refreshScore(player2);
		}
		for(Player player2 : waitingRoomSara) {
			refreshScore(player2);
		}
		return false;
	}

	public boolean canAttack(Player attacker, CombatEntity ce) {
		if(ce.getEntity() instanceof NPC)
			return true;
		Player sucker = ce.getPlayer();
		if(gameZammy.contains(attacker)) {
			if(gameSara.contains(sucker))
				return true;
		} else if(gameSara.contains(attacker)) {
			if(gameZammy.contains(sucker))
				return true;
		}
		return false;
	}

	private void giveWinItems(Player player, boolean draw) {

	}

	private Position lobby = Position.create(2440, 3090, 0);
	private Position zammySpawn = Position.create(2373, 3130, 1);
	private Position saraSpawn = Position.create(2427, 3076, 1);

	private int timeInMinsLeft = 1;
	private int timeLeft = 60;
	private int zammyKills = 0;
	private int saraKills = 0;

	private ArrayList<Player> waitingRoomZammy = new ArrayList<Player>();
	private ArrayList<Player> waitingRoomSara = new ArrayList<Player>();

	private ArrayList<Player> gameZammy = new ArrayList<Player>();
	private ArrayList<Player> gameSara = new ArrayList<Player>();

	private boolean gameInProgress = false;

	public boolean isInGame(Player player) {
		return (gameZammy.contains(player) || gameSara.contains(player));
	}

	public void giveItemObject(Player player, int id) {
		if(id == 4470)//bandages
			ContentEntity.addItem(player, 4049);
		if(id == 4463)//explosion pots
			ContentEntity.addItem(player, 4045);
		if(id == 4462)//ropes
			ContentEntity.addItem(player, 954);
		if(id == 4464)//rocks
			ContentEntity.addItem(player, 968);
		if(id == 4460)//pickaxes
			ContentEntity.addItem(player, 1265);
		if(id == 4461)//barricades
			ContentEntity.addItem(player, 4053);
		if(id == 4459)//toolkits
			ContentEntity.addItem(player, 4052);
	}

	public void staircase(Player player, int id) {
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();
		if(id == 4415 && x == 2379 && y == 3127)
			player.setTeleportTarget(Position.create(player.getPosition().getX() + 1, player.getPosition().getY() + 3, player.getPosition().getZ() - 1));
		else if(id == 4415 && x == 2369 && y == 3127)
			player.setTeleportTarget(Position.create(player.getPosition().getX() + 3, player.getPosition().getY() - 1, player.getPosition().getZ() - 1));


	}


}
