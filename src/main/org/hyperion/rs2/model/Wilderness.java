package org.hyperion.rs2.model;

import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.combat.Combat;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class Wilderness {

	public static class Obelisk {
		public int[] x = new int[4];
		public int[] y = new int[4];

		public Obelisk(int[] x, int[] y) {
			this.x = x;
			this.y = y;
		}
	}

	public static List<Obelisk> obelisks = new LinkedList<Obelisk>();

	public static void useObelisk(final Player player, int x, int y) {
		final Obelisk o = useObelisk(x, y);
		if(o == null)
			return;
		int maxX2 = 0;
		int maxY2 = 0;

		final int minX = o.x[0];
		final int minY = o.y[0];
		GameObject[] list = new GameObject[4];
		for(int i = 0; i < 4; i++) {
			if(o.x[i] > minX)
				maxX2 = o.x[i];
			if(o.y[i] > minY)
				maxY2 = o.y[i];
			list[i] = replaceGlobalObject(o.x[i], o.y[i], 14825, - 1, 10);
		}
		final GameObject[] list2 = list;
		final int maxX = maxX2;
		final int maxY = maxY2;
		final Obelisk o2 = randomObelisk();
		World.submit(new Task(3000, "wilderness tele") {
			public int timer = 2;

			@Override
			public void execute() {
				if(timer == 2) {
					/*for(int i = 1; i < 4; i++){
						for(int j = 1; j < 4; j++)
							createGfx(player,343,minX+i,minY+j);*/
					//synchronized(player.getLocalPlayers()) {
					for(Player p : player.getLocalPlayers()) {
						createGfx(player, 343, p.getPosition().getX(), p.getPosition().getY());
					}
					//}
					createGfx(player, 343, player.getPosition().getX(), player.getPosition().getY());
				} else if(timer == 1) {
					for(int j = 0; j < 4; j++) {
						ObjectManager.removeObject(list2[j]);
					}
					for(Player p : player.getLocalPlayers()) {
						tele(o2, p, minX, minY, maxX, maxY);
					}

					tele(o2, player, minX, minY, maxX, maxY);
					//reset the oblisks
					for(int i = 0; i < 4; i++) {
						ObjectManager.removeObject(replaceGlobalObject(o.x[i], o.y[i], 14826, - 1, 10));
					}
					this.stop();
				}
				timer--;
			}
		});
		list = null;
	}

	public static void createGfx(Player player, int id, int x, int y) {
		for(Player p : player.getLocalPlayers()) {
			p.getActionSender().sendStillGraphics(id, 0, y, x, 50);
		}
		player.getActionSender().sendStillGraphics(id, 0, y, x, 50);
	}

	public static void tele(final Obelisk o2, Player p, int minX, int minY, int maxX, int maxY) {
		//System.out.println("x: "+minX+" y: "+minY +" x2: "+maxX+" y2: "+maxY);
		if(p.getPosition().getX() > minX && p.getPosition().getX() < maxX) {
			if(p.getPosition().getY() > minY && p.getPosition().getY() < maxY) {
				if(p.isTeleBlocked()) {
					p.getActionSender().sendMessage("The teleblock spell prevented you from teleporting..");
					return;
				}
				//teleport and gfx
				int addX = p.getPosition().getX() - minX;
				int addY = p.getPosition().getY() - minY;
				//p.startAnimation(1979);
				if(p == null || o2 == null)
					return;
				p.setTeleportTarget(Position.create(o2.x[0] + addX, o2.y[0] + addY, 0));
			}
		}
	}

	public static GameObject replaceGlobalObject(int x, int y, int id, int face, int type) {
		GameObject gO = new GameObject(GameObjectDefinition.forId(id), Position.create(x, y, 0), type, face);
		ObjectManager.addObject(gO);
		return gO;
	}

	public static Obelisk useObelisk(int x, int y) {
		for(Obelisk o : obelisks) {
			for(int i = 0; i < 4; i++) {
				if(o.x[i] == x && o.y[i] == y) {
					return o;
				}
			}
		}
		return null;
	}

	public static Obelisk randomObelisk() {
		int r = Combat.random(obelisks.size() - 1);
		int i = 0;
		for(Obelisk o : obelisks) {
			if(r == i)
				return o;
			i++;
		}
		return null;
	}

	public static void init() {
		int[] x = {3305, 3305, 3309, 3309,};
		int[] y = {3914, 3918, 3918, 3914,};
		obelisks.add(new Obelisk(x, y));
		int[] x2 = {3104, 3104, 3108, 3108,};
		int[] y2 = {3792, 3796, 3792, 3796,};
		obelisks.add(new Obelisk(x2, y2));
		int[] x3 = {3154, 3154, 3158, 3158,};
		int[] y3 = {3618, 3622, 3618, 3622,};
		obelisks.add(new Obelisk(x3, y3));
		int[] x4 = {3225, 3225, 3229, 3229,};
		int[] y4 = {3665, 3669, 3665, 3669,};
		obelisks.add(new Obelisk(x4, y4));
		int[] x5 = {2978, 2978, 2982, 2982,};
		int[] y5 = {3864, 3868, 3864, 3868,};
		obelisks.add(new Obelisk(x5, y5));
		int[] x6 = {3033, 3033, 3037, 3037,};
		int[] y6 = {3730, 3734, 3730, 3734,};
		obelisks.add(new Obelisk(x6, y6));
		if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
			Server.getLogger().log(Level.INFO, "Wilderness has successfully loaded.");
	}
}