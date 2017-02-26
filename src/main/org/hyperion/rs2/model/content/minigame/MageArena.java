package org.hyperion.rs2.model.content.minigame;
// Yay

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.NPCManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

public class MageArena implements ContentTemplate {

	public void startMinigame(Player player) {
		int height = 0;
		ContentEntity.teleport(player, 3104, 3934, height);
		//teleport to arena,
		NPCManager.addNPC(3104 + 3, 3934 + 3, height, 907, - 1);
		//spawn kolodian

	}

	public void wonMinigame(Player player) {
		ContentEntity.sendMessage(player, "Congratz you beat mage arena.");
		ContentEntity.teleport(player, 2541, 4716, 0);
	}

	@Override
	public void init() throws FileNotFoundException {
	}

	@Override
	public int[] getValues(int type) {
		if(type == 16) {
			int[] j = {905, 907, 908, 909, 910, 911,};
			return j;
		}
		if(type == 6) {
			int[] j = {2878, 2879, 9706, 9707};
			return j;
		}
		return null;
	}

	@Override
	public boolean clickObject(final Player client, final int type, final int oId, final int oX, final int oY, final int a) {
		if(type == 16) {
			if(oId == 905) {
				startMinigame(client);
			} else if(oId != 911)
				NPCManager.addNPC(client.getPosition().getX() + 3, client.getPosition().getY() + 3, 0, (oId + 1), - 1);
			else
				wonMinigame(client);
		} else if(type == 6) {
            if(oId == 9706) {
                Magic.teleport(client, Position.create(3105, 3951, 0), true);
            } else if(oId == 9707) {
                Magic.teleport(client, Position.create(3105, 3956, 0), true);
            }
			else if(oId == 2878 || oId == 2879) {
				client.getWalkingQueue().reset();
				if(oId == 2878) {
					client.getWalkingQueue().addStep(2542, client.getPosition().getY() + 1);
					client.getWalkingQueue().addStep(2542, client.getPosition().getY() + 2);
				} else {
					client.getWalkingQueue().addStep(2509, client.getPosition().getY() - 1);
					client.getWalkingQueue().addStep(2509, client.getPosition().getY() - 2);
				}
				client.getWalkingQueue().finish();
				World.submit(new Task(2000,"magearena1") {
					@Override
					public void execute() {
						ContentEntity.startAnimation(client, 804);
						this.stop();
					}
				});
				World.submit(new Task(3000,"magearena2") {
					@Override
					public void execute() {
						ContentEntity.startAnimation(client, - 1);
						if(oId == 2878)
							ContentEntity.teleport(client, 2509, 4689, 0);
						else
							ContentEntity.teleport(client, 2542, 4718, 0);
						this.stop();
					}
				});
			}
		}
		return false;
	}
}