package org.hyperion.rs2.model.content.transport;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc2.Edgeville;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Levers implements ContentTemplate {

	private static Map<Position, Lever> LEVERS = new HashMap<Position, Lever>();

	private static final Animation LEVER_ANIMATION = Animation.create(2140);


	public static boolean handle(final Player player, final Position loc, final int objectId) {
		final Lever lever = LEVERS.get(loc);
		player.cE.face(loc.getX(), loc.getY() - 1);
		if(lever == null || objectId == 5961 /*You've already clicked the lever.*/) {
			return false;
		}
		if(lever.targetPosition.getX() == 2539) {
			if(player.getPosition().getX() < 3090 || player.getPosition().getY() != 3956)
				return false;
		}
	    /*
         * Prevents mass clicking them.
		 */
		if(player.isTeleBlocked()) {
			player.getActionSender().sendMessage(
					"You are currently teleblocked.");
			return false;
		}
		if(player.getTimeSinceLastTeleport() < 1600)
			return false;
		if(player.getPosition().getX() != loc.getX() || player.getPosition().getY() != loc.getY()) {
			return false;
		}
		player.playAnimation(LEVER_ANIMATION);
		player.getActionSender().sendCreateObject(5961, 4, lever.getDirection1(), loc);
		player.getCombat().setOpponent(null);
		player.setCanWalk(false);
		player.updateTeleportTimer();
		World.submit(new Task(1500,"levers1") {
			@Override
			public void execute() {
				player.getActionSender().sendCreateObject(objectId, 4, lever.getDirection2(), loc);
				player.playGraphics(Graphic.create(1576, 6553635));// perfect !
				player.playAnimation(Animation.create(8939, 0));
				World.submit(new Task(1800,"levers2") {

					@Override
					public void execute() {
						player.setTeleportTarget(lever.getTargetPosition());
						player.playAnimation(Animation.create(- 1));
						player.setCanWalk(true);
						this.stop();
					}

				});
				this.stop();
			}

		});
		return true;
	}

	private static class Lever {

		private final Position targetPosition;
		private final int direction1;
		private final int direction2;

		public Position getTargetPosition() {
			return targetPosition;
		}

		public int getDirection1() {
			return direction1;
		}

		public int getDirection2() {
			return direction2;
		}

		public Lever(Position target, int direction1, int direction2) {
			this.targetPosition = target;
			this.direction1 = direction1;
			this.direction2 = direction2;
		}
	}

	/**
	 * This populates the map.
	 */
	static {
		/*
		 * King Black Dragon levers.
		 */
		LEVERS.put(Position.create(3067, 10253, 0), new Lever(Position.create(2271, 4680, 0), 3, 3));
		LEVERS.put(Position.create(2271, 4680, 0), new Lever(Position.create(3067, 10253, 0), 3, 3));
		//Mage Bank to Wild
		LEVERS.put(Position.create(2539, 4712, 0), new Lever(Position.create(3090, 3956, 0), 3, 3));
		LEVERS.put(Position.create(3090, 3956, 0), new Lever(Position.create(2539, 4712, 0), 0, 0));

        LEVERS.put(Position.create(3153, 3923, 0), new Lever(Edgeville.POSITION, 0, 0));
		//edgville to magebank
		//player.getActionAssistant().pullLever(player, x, y, 5961, 0, 3, 3153, 3923, 0);

		//magebank back to edgeville
		//player.getActionAssistant().pullLever(player, x, y, 5961, 0, 4, 3079, 3489, 0);
	}

	@Override
	public boolean clickObject(Player player, int type, int leverId, int xcoord, int ycoord,
	                           int d) {
		handle(player, Position.create(xcoord, ycoord, 0), leverId);
		return false;
	}

	@Override
	public void init() throws FileNotFoundException {
	}

	@Override
	public int[] getValues(int type) {
		if(type == 6) {
			int[] levers = {1815, 5960, 1816, 1817, 5959};
			return levers;
		}
		return null;
	}

}
