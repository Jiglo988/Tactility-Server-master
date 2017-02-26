package org.hyperion.rs2.model.combat;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ContentEntity;

public class Curses {

	/**
	 * @param player, whoever is DOING the leech.
	 */
	public static void applyLeeches(final Player player) {
		if(! player.getPrayers().isLeeching())
			return;
		if(! (System.currentTimeMillis() - player.LastTimeLeeched > 5000 * (1 + player.getPrayers().activeLeeches() / 2))) {
			return;
		}
		if(player.cE.getOpponent() == null) {
			return;
		}
		final int leechId = player.getPrayers().pollLeech();
		final CombatEntity victim = player.cE.getOpponent();
		updateLeech(player);
		World.submit(new Task(800L) {
			public void execute() {
				ContentEntity.startAnimation(player, 12575);
				if(victim == null) {
					this.stop();
					return;
				}
				int offsetY = (player.cE.getAbsX() - victim.getAbsX()) * - 1;
				int offsetX = (player.cE.getAbsY() - victim.getAbsY()) * - 1;
				//find our lockon target
				int hitId = player.cE.getSlotId(player.cE.getEntity());

				int speed = 70;
				int time = 24;
				int slope = 0;
				player.getActionSender().createGlobalProjectile(player.cE.getAbsY(), player.cE.getAbsX(), offsetY, offsetX, 50, speed, getGfxIdForPrayerProjectile(leechId), 20, 20, hitId, time, slope);
				this.stop();
			}
		});
		Player pseudoPlayer = null;
		if(player.cE.getOpponent() != null && player.cE.getOpponent().getEntity() instanceof Player)
			pseudoPlayer = player.cE.getOpponent().getPlayer();
		final Player opponentPlayer = pseudoPlayer;
		World.submit(new Task(1800L) {
			public void execute() {
				if(opponentPlayer != null) {
					opponentPlayer.getCombat().doGfx(getGfxIdForPrayer(leechId));
					if(leechId == 46) {
						if(opponentPlayer.duelAttackable < 1) {
							opponentPlayer.getSpecBar().decrease(15);
							opponentPlayer.getSpecBar().sendSpecBar();
							opponentPlayer.getSpecBar().sendSpecAmount();
						}
					}
					opponentPlayer.getActionSender().sendMessage("Your " + DRAIN_NAMES[leechId - 40] + " has been leeched by " + player.getSafeDisplayName() + " !");
					player.getActionSender().sendMessage("You have leeched " + opponentPlayer.getSafeDisplayName() + "'s " + DRAIN_NAMES[leechId - 40] + " !");
				}
				this.stop();
			}
		});
	}

	public static void updateLeech(Player player) {
		player.LastTimeLeeched = System.currentTimeMillis();
	}

	private static int getGfxIdForPrayer(int i) {
		i -= 40;
		switch(i) {
			case 0://attack
				return 2253;
			case 3://range
				return 2238;
			case 4://magic
				return 2242;
			case 6://special
				return 2258;
			case 1://def
				return 2250;
			case 2://str
				return 2246;
		}
		return 0;
	}

	private static int getGfxIdForPrayerProjectile(int i) {
		switch(i) {
			case 40:
				return 2252;
			case 41:
				return 2236;
			case 42:
				return 2240;
			case 43:
				return 2248;
			case 44:
				return 2248;
			case 45:
				return 2256;
		}
		return 0;
	}

	private static final String[] DRAIN_NAMES = {"Attack", "Ranged", "Magic",
			"Defense", "Strength", "Energy", "Special Energy"};

	private static int getSkillIdForLeechId(int leechId) {
		switch(leechId) {
			case 40:
				return 0;
			case 41:
				return 4;
			case 42:
				return 6;
			case 43:
				return 1;
			case 44:
				return 2;
		}
		return - 1;
	}

}
