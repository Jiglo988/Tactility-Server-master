package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.util.Time;

/**
 * @author Arsen Maxyutov.
 */
public class OverloadStatsTask extends Task {
	/**
	 * 
	 * @author Wasay
	 *
	 */
	public static class OverloadFactory {
		/**
		 * Ends overload, resets stats, heals 50 HP
		 */
		public static void endOverload(Player player) {
			player.getSkills().speedNormalizeLevel(Skills.ATTACK);
			player.getSkills().speedNormalizeLevel(Skills.STRENGTH);
			player.getSkills().speedNormalizeLevel(Skills.DEFENCE);
			player.getSkills().speedNormalizeLevel(Skills.RANGED);
			player.getSkills().speedNormalizeLevel(Skills.MAGIC);
			player.sendImportantMessage("Your overload has worn out!");
			if(player.isOverloaded())
				ContentEntity.increaseSkill(player, Skills.HITPOINTS, 50);
			player.getExtraData().remove(KEY);
			player.setOverloaded(false);
			player.resetOverloadCounter();
		}
		
		/**
		 * Gets boost for skill ID for player
		 */
		public static int getBoost(Player player, int skillId) {
			double boostPercentage = 0;
			if(Position.inAttackableArea(player))
				boostPercentage = .15;
			else
				boostPercentage = MELEE_PERCENTAGE_BOOST;
			int bonus = MELEE_BONUS;
			if(skillId == Skills.RANGED) {
				if(!Position.inAttackableArea(player))
					boostPercentage = NON_MELEE_PERCENTAGE_BOOST;
				else
					boostPercentage = .08;
			}
			if(skillId == Skills.MAGIC) {
				boostPercentage = 0;
			}
			int boostedLevel = (int)(boostPercentage * player.getSkills().getLevelForExp(skillId)) + bonus;
			int currentLevelDiff = player.getSkills().getLevel(skillId) - player.getSkills().getRealLevels()[skillId];
			if((currentLevelDiff < boostedLevel) || skillId != Skills.MAGIC)
				return boostedLevel;
			else
				return currentLevelDiff;
		}
		/**
		 * Appends boosts
		 */
		public static void applyBoosts(Player player) {
			ContentEntity.setOvlSkill(player, Skills.ATTACK, getBoost(player, Skills.ATTACK));
            if(System.currentTimeMillis() - player.getExtraData().getLong("ovlreset1") > 10000)
			    ContentEntity.setOvlSkill(player, Skills.DEFENCE, getBoost(player, Skills.DEFENCE));
			ContentEntity.setOvlSkill(player, Skills.STRENGTH, getBoost(player, Skills.STRENGTH));
			ContentEntity.increaseSkill(player, Skills.RANGED, getBoost(player, Skills.RANGED));
			ContentEntity.increaseSkill(player, Skills.MAGIC, getBoost(player, Skills.MAGIC));
		}
	}

	/**
	 * The delay of 1 cycle.
	 */
	public static final long DELAY = Time.FIFTEEN_SECONDS;

	/**
	 * The amount of cycles that should be done before terminating this event.
	 */
	public static final int CYCLES = (int) (Time.FIVE_MINUTES / Time.FIFTEEN_SECONDS);

	/**
	 * The extra data key.
	 */
	public static final String KEY = "overload";

	/**
	 * Howmuch is added to the melee skill boost after applying the percentage boost.
	 */
	public static final int MELEE_BONUS = 5;

	/**
	 * Howmuch is added to the ranged or magic skill boost after applying the percentage boost.
	 */
	public static final int NON_MELEE_BONUS = 3;

	/**
	 * The ranged percentage boost.
	 */
	public static final double NON_MELEE_PERCENTAGE_BOOST = 0.19;

	/**
	 * The melee percentage boost.
	 */
	public static final double MELEE_PERCENTAGE_BOOST = 0.22;

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * The counter holding the amount of cycles passed.
	 */
	private int counter = 0;


	/**
	 * Creates a new stat boosting event.
	 *
	 * @param player
	 */
	public OverloadStatsTask(Player player) {
		super(DELAY);
		this.player = player;
		this.player.getExtraData().put(KEY, true);
		OverloadFactory.applyBoosts(player);
	}

	/**
	 * Executes the event.
	 */
	@Override
	public void execute() {
		if(player == null) {
			this.stop();
			return;
		}
		if(player.getOverloadCounter().incrementAndGet() < CYCLES && player.isOverloaded()) {
			if((CYCLES - player.getOverloadCounter().get()) == 4)
				player.getActionSender().sendMessage("@dre@Your overload will run out in 1 minute.");
			OverloadFactory.applyBoosts(player);
		} else {
			OverloadFactory.endOverload(player);
			this.stop();
		}
	}

}
