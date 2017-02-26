package org.hyperion.rs2.model.content.skill.fletching;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fletching skill handler
 *
 * @author Glis
 */
public class Fletching implements ContentTemplate {

	public static int EXPMULTIPLIER = Constants.XPRATE * 2;

	public Fletching() {
	}

	public boolean isFletchable(Player client, int slot1, int slot2, int useItem, int usedItem) {
		client.getExtraData().put("fletching", true);
		//LogCutting
		if(useItem == 946 && LogCutting.getLog(usedItem) != null) {
			return LogCutting.chooseItem(client, usedItem);
		}
		if(usedItem == 946 && LogCutting.getLog(useItem) != null) {
			return LogCutting.chooseItem(client, useItem);
		}
		//BowStringing
		else if(BowStringing.getString(usedItem) != null) {
			BowStringing.StrungItems items[] = BowStringing.getString(usedItem).getItems();
			for(int i = 0; i < items.length; i++) {
				if(items[i].getItemId() == useItem) {
					return BowStringing.stringBow(client, usedItem, i);
				}
			}
		}
		else if(BowStringing.getString(useItem) != null) {
			BowStringing.StrungItems items[] = BowStringing.getString(useItem).getItems();
			for(int i = 0; i < items.length; i++) {
				if(items[i].getItemId() == usedItem) {
					return BowStringing.stringBow(client, useItem, i);
				}
			}
		}
		//Headless arrows
		 else if(useItem == 52 && usedItem == 314 || useItem == 314 && usedItem == 52) {
			return HeadlessArrows.createHeadlessArrows(client, 52);
		}
		//ArrowMaking
		else if(useItem == 53) {
			ArrowMaking.Arrow item = ArrowMaking.getArrow(usedItem);
			if(item != null)
				ArrowMaking.createArrows(client, usedItem);
		}
		else if(usedItem == 53) {
			ArrowMaking.Arrow item = ArrowMaking.getArrow(useItem);
			if(item != null)
				ArrowMaking.createArrows(client, useItem);
		}
		//DartMaking
		else if(useItem == 314) {
			DartMaking.Dart item = DartMaking.getDart(usedItem);
			if(item != null)
				DartMaking.createDarts(client, usedItem);
		}
		else if(usedItem == 314) {
			DartMaking.Dart item = DartMaking.getDart(useItem);
			if(item != null)
				DartMaking.createDarts(client, useItem);
		}
		return false;
	}

	private int[] fletchingTools = {314, 53};

	@Override
	public int[] getValues(int type) {
		if (type == 13) {
			List<Integer> j = new ArrayList();
			for(ArrowMaking.Arrow arrow : ArrowMaking.Arrow.values()) {
				j.add(arrow.getArrowHeadId());
			}
			for(BowStringing.StringItem string : BowStringing.StringItem.values()) {
				j.add(string.getItemId());
			}
			for(BowStringing.StrungItems item : BowStringing.StrungItems.values()) {
				j.add(item.getItemId());
			}
			for(DartMaking.Dart dart : DartMaking.Dart.values()) {
				j.add(dart.getDartTipId());
			}
			for(int i = 0; i < fletchingTools.length; i++) {
				j.add(fletchingTools[i]);
			}
			return ArrayUtils.fromList(j);
		}
		return null;
	}

	public static boolean clickInterface(final Player client, final int id) {
		switch(id) {
			case 8909:
			case 8889:
			case 8949:
			case 8874:
				return LogCutting.startFletching(client, 1, 0);
			case 8913:
			case 8893:
			case 8953:
			case 8878:
				return LogCutting.startFletching(client, 1, 1);
			case 8917:
			case 8897:
			case 8957:
				return LogCutting.startFletching(client, 1, 2);
			case 8921:
			case 8961:
				return LogCutting.startFletching(client, 1, 3);
			case 8965:
				return LogCutting.startFletching(client, 1, 4);

			case 8908:
			case 8888:
			case 8948:
			case 8873:
				return LogCutting.startFletching(client, 5, 0);
			case 8912:
			case 8892:
			case 8952:
			case 8877:
				return LogCutting.startFletching(client, 5, 1);
			case 8916:
			case 8896:
			case 8956:
				return LogCutting.startFletching(client, 5, 2);
			case 8920:
			case 8960:
				return LogCutting.startFletching(client, 5, 3);
			case 8964:
				return LogCutting.startFletching(client, 5, 4);

			case 8907:
			case 8887:
			case 8947:
			case 8872:
				return LogCutting.startFletching(client, 10, 0);
			case 8911:
			case 8891:
			case 8951:
			case 8876:
				return LogCutting.startFletching(client, 10, 1);
			case 8915:
			case 8895:
			case 8955:
				return LogCutting.startFletching(client, 10, 2);
			case 8919:
			case 8959:
				return LogCutting.startFletching(client, 10, 3);
			case 8963:
				return LogCutting.startFletching(client, 10, 4);

			case 8906:
			case 8946:
			case 8886:
			case 8871:
				return LogCutting.startFletching(client, 28, 0);
			case 8910:
			case 8950:
			case 8890:
			case 8875:
				return LogCutting.startFletching(client, 28, 1);
			case 8914:
			case 8954:
			case 8894:
				return LogCutting.startFletching(client, 28, 2);
			case 8918:
			case 8958:
				return LogCutting.startFletching(client, 28, 3);
			case 8962:
				return LogCutting.startFletching(client, 28, 4);
		}
		return false;
	}

	@Override
	public boolean clickObject(final Player player, final int type, final int id, final int slot, final int itemId2, final int itemSlot2) {
		if(type == 13) {
			return isFletchable(player, slot, itemSlot2, id, itemId2);
		}
		return false;
	}
}
