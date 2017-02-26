package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.NPCManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

public class Edgeville implements ContentTemplate {


	public final static Position POSITION = Position.create(3087, 3491, 0);



	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c, int d) {
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
		NPCManager.addNPC(3089, 3485, 0, 2999, - 1);
	}

	@Override
	public int[] getValues(int type) {
		return null;
	}

}
