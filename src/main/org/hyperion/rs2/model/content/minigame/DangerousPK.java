package org.hyperion.rs2.model.content.minigame;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.util.TextUtils;

import java.io.FileNotFoundException;

public class DangerousPK implements ContentTemplate {
	public enum ArmourClass {
		MAGE,
		MELEE,
		RANGE;
		public String toString() {
			String name = super.toString();
			name = name.replace("_", " ");
			name = TextUtils.titleCase(name);
			return name;
		}
	}

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c,
			int d) {
		//object clicking, entering/exiting
		if(type == 6) {
			if(a == 9359) {
				openDialogue(player);
			}
			if(a == 1755) {
				Magic.teleport(player, entrance(), false);
			}
		}
		return false;
	}
	//pick a class
	public static void openDialogue(Player player) {
		DialogueManager.openDialogue(player, 145);
	}
	//send back to entrance area
	public static Position entrance() {
		return Position.create(2480, 5175, 0);
	}
	//send to wait area
	public static void toWaitArea(final Player player) {
        if(!ItemSpawning.canSpawn(player)) {
            player.sendMessage("You can't access dangeroupk from here");
            return;
        }
        if(player.getPoints().getPkPoints() < 75) {
            player.sendMessage("You need at least 75PKP to enter this arena!");
            return;
        }
		player.playAnimation(Animation.create(7376));
		World.submit(new Task(1000, "dangerouspk") {
			public void execute() {
				player.setTeleportTarget(Position.create(2475, 5214, 0));
                this.stop();
			}
		});
	}
	@Override
	public void init() throws FileNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int[] getValues(int type) {
		return new int[]{9359};
	}
	
	/**
	 * Checks if in FFACombat, no location currently
	 */
	public static boolean inDangerousPK(Player player) {
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();
		return inDangerousPK(x, y);
	}

    public static boolean inDangerousPK(int x, int y) {
        return x > 2450 && y > 5205 && y < 5220 && x < 2477;
    }
	//2474, 5214
	
	
	static {
	}

}
