package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.EntityCooldowns.CooldownFlags;

/**
 * This event handles the expiry of a cooldown.
 *
 * @author Brett Russell
 */

public class CooldownTask extends Task {

	private Entity entity;

	private CooldownFlags cooldown;

	/**
	 * Creates a cooldown event for a single CooldownFlag.
	 *
	 * @param entity   The entity for whom we are expiring a cooldown.
	 * @param duration The length of the cooldown.
	 */
	public CooldownTask(Entity entity, CooldownFlags cooldown, int duration) {
		super(duration);
		this.entity = entity;
		this.cooldown = cooldown;
	}

	@Override
	public void execute() {
		entity.getEntityCooldowns().set(cooldown, false);
		this.stop();
	}

}
