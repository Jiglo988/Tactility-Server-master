package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;

public interface ButtonAction {
	default boolean handle(final Player player, int id) {
		return false;
	}
}
