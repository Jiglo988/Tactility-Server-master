package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.content.ContentTemplate;

public class Zanaris implements ContentTemplate {

	public final static Position POSITION = Position.create(2395, 4455, 0);


	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c,
	                           int d) {
		if(type == 7 || type == 6) {
			switch(a) {
				case 12121:
				case 12120:
					Bank.open(player, false);
					break;
			}
			return true;
		}
		return false;
	}

	@Override
	public int[] getValues(int type) {
		if(type == 7 || type == 6) {
			int[] ids = {12121, 12120, 12355};
			return ids;
		}
		return null;
	}


}
