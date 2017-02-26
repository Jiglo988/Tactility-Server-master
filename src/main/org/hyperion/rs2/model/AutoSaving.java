package org.hyperion.rs2.model;

import org.hyperion.rs2.saving.PlayerSaving;

public class AutoSaving {

	public static final int CYCLES_TO_AUTOSAVE = 100 * 20; //One cycle is 600 ms, 100 cycles = 1 minute

	private Player player;

	private int timer = 0;

	public AutoSaving(Player player) {
		this.player = player;
	}

	public void process() {
		timer++;
		if(timer >= CYCLES_TO_AUTOSAVE) {
			PlayerSaving.save(player);
			timer = 0;
		}
	}

}
