package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

public class Lumbridge implements ContentTemplate {

	public final static Position POSITION = Position.create(3221, 3218, 0);

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
