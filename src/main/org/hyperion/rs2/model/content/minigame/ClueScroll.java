package org.hyperion.rs2.model.content.minigame;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

public class ClueScroll implements ContentTemplate {

	public void displayClue(Player player, int clueId) {
		int interfaceId = 0;
		switch(clueId) {
			case 1:
				interfaceId = 4305;
				break;
			case 2:
				interfaceId = 7045;
				break;
			case 3:
				interfaceId = 7162;
				break;
			case 4:
				interfaceId = 7113;
				break;
			case 5:
				interfaceId = 9043;
				break;
			case 6:
				interfaceId = 7271;
				break;
			case 7:
				interfaceId = 17537;
				break;
		}
		player.getActionSender().showInterface(interfaceId);
	}

	public void digClue(Player player, int clueId) {
		boolean found = false;
		switch(clueId) {
			case 1:
				if(Position.create(2906, 3294, 0).isWithinDistance(player.getPosition(), 0))
					found = true;
				break;
			case 2:
				if(Position.create(3290, 3372, 0).isWithinDistance(player.getPosition(), 0))
					found = true;
				break;
			case 3:
				if(Position.create(2696, 3428, 0).isWithinDistance(player.getPosition(), 0))
					found = true;
				break;
			case 4:
				if(Position.create(3092, 3226, 0).isWithinDistance(player.getPosition(), 0))
					found = true;
				break;
			case 5:
				if(Position.create(2617, 3076, 0).isWithinDistance(player.getPosition(), 0))
					found = true;
				break;
			case 6:
				if(Position.create(3043, 3398, 0).isWithinDistance(player.getPosition(), 0))
					found = true;
				break;
			case 7:
				if(Position.create(2970, 3414, 0).isWithinDistance(player.getPosition(), 0))
					found = true;
				break;
		}
		if(found)
			newClueStage(player, clueId);
	}

	public void newClueStage(Player player, int oldClueId) {
		player.clueStage--;
		//either give a new clue or give an item to teleport to an npc to kill
	}
	/*
     *  9108, 9196,
9275, 9359, 9454, 9507, 9632, 9720, 9839, 
17620, 17634, 17687, 17774, 17888, 17907, 18055.
*/

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c,
	                           int d) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getValues(int type) {
		// TODO Auto-generated method stub
		return null;
	}

}
