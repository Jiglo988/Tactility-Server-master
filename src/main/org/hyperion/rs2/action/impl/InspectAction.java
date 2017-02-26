package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;

public abstract class InspectAction extends Action {

	/**
	 * The location.
	 */
	private Position position;

	/**
	 * Constructor.
	 *
	 * @param player
	 * @param position
	 */
	public InspectAction(Player player, Position position) {
		super(player, 0);
		this.position = position;
	}

	@Override
	public QueuePolicy getQueuePolicy() {
		return QueuePolicy.NEVER;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	/**
	 * Initialization method.
	 */
	public abstract void init();

	/**
	 * Inspection time consumption.
	 *
	 * @return
	 */
	public abstract long getInspectDelay();


	/**
	 * Rewards to give the player.
	 *
	 * @param player
	 * @param node
	 */
	public abstract void giveRewards(Player player);

	@Override
	public void execute() {
		final Player player = getPlayer();
		if(this.getDelay() == 0) {
			this.setDelay(getInspectDelay());
			init();
			if(this.isRunning()) {
				player.face(position);
			}
		} else {
			giveRewards(player);
			stop();
		}
	}

}