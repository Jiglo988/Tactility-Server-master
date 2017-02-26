package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.net.Packet;
import org.hyperion.util.Boundary;

public class CharDesignPacketHandler implements PacketHandler {

	private static final Boundary[] MALE_VALUES = {
			new Boundary(0, 8), // head
			new Boundary(10, 17), // jaw
			new Boundary(18, 25), // torso
			new Boundary(26, 31), // arms
			new Boundary(33, 34), // hands
			new Boundary(36, 40),// legs
			new Boundary(42, 43), // feet
	};

	private static final Boundary[] FEMALE_VALUES = {
			new Boundary(45, 54), // head
			new Boundary(- 1, - 1), // jaw
			new Boundary(56, 60), // torso
			new Boundary(61, 65), // arms
			new Boundary(67, 68), // hands
			new Boundary(70, 77), // legs
			new Boundary(79, 80), // feet
	};

	private static final Boundary[] ALLOWED_COLORS = {
			new Boundary(0, 11), // hair color
			new Boundary(0, 15), // torso color
			new Boundary(0, 15), // legs color
			new Boundary(0, 5), // feet color
			new Boundary(0, 7) // skin color
	};

	public static final int SIZE = 1 + MALE_VALUES.length + ALLOWED_COLORS.length;

	@Override
	public void handle(Player player, Packet packet) {
		int gender = packet.getByte();
		if(gender != 0 && gender != 1)
			return;
		int[] look = new int[SIZE];
		look[0] = gender;
		/**
		 * Setting body and head
		 */
		for(int i = 0; i < MALE_VALUES.length; i++) {
			int value = packet.getByte();
			if(gender == 0) {
				value = Boundary.checkBounds(value, MALE_VALUES[i]);
			} else {
				value = Boundary.checkBounds(value, FEMALE_VALUES[i]);
			}
			look[i + 1] = value;
		}
		/**
		 * Setting colors
		 */
		for(int i = 0; i < ALLOWED_COLORS.length; i++) {
			int value = packet.getByte();
			//value = Boundary.checkBounds(value, ALLOWED_COLORS[i]);
			look[i + 1 + MALE_VALUES.length] = value;
		}
		/**
		 * Updating new look
		 */
		player.getAppearance().setLook2(look);
		//PlayerSaving.getSaving().saveLook(player);
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		player.getActionSender().removeAllInterfaces();
	}

}
