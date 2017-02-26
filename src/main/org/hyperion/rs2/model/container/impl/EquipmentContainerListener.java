package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;
import org.hyperion.rs2.model.container.Equipment;

/**
 * A ContainerListener which flags for an appearance update when the player
 * equips or removes an item.
 *
 * @author Graham Edgecombe
 */
public class EquipmentContainerListener implements ContainerListener {

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * Creates the container listener.
	 *
	 * @param player The player.
	 */
	public EquipmentContainerListener(Player player) {
		this.player = player;
	}

	@Override
	public void itemChanged(Container container, int slot) {

		player.getActionSender().calculateBonus();
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		if(player.getEquipment().get(Equipment.SLOT_WEAPON) == null)
			player.cE.setAtkSpeed(2400);
	}

	@Override
	public void itemsChanged(Container container) {
		player.getActionSender().calculateBonus();
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		if(player.getEquipment().get(Equipment.SLOT_WEAPON) == null)
			player.cE.setAtkSpeed(2400);
	}

	@Override
	public void itemsChanged(Container container, int[] slots) {
		player.getActionSender().calculateBonus();
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		if(player.getEquipment().get(Equipment.SLOT_WEAPON) == null)
			player.cE.setAtkSpeed(2400);
	}

}
