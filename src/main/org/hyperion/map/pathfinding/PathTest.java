package org.hyperion.map.pathfinding;


/**
 * A simple test to show some path finding at unit
 * movement for a tutorial at http://www.cokeandcode.com
 *
 * @author Kevin Glass
 */
public class PathTest {
	/**
	 * The map on which the units will move
	 */
	private static GameMap map = new GameMap();
	/**
	 * The path finder we'll use to search our map
	 */
	private static PathFinder finder = new AStarPathFinder(map, 32, true);

	public static int baseX = 0;
	public static int baseY = 0;

	public final static int maxRegionSize = 25;//*2 in reality

	public static Path getPath(int x, int y, int toX, int toY) {
		try {
			baseX = x - maxRegionSize;
			baseY = y - maxRegionSize;
			toX = (toX - baseX);
			toY = (toY - baseY);
			if(toX < 0 || toX > (maxRegionSize * 2) || toY < 0 || toY > (maxRegionSize * 2)) {
				return null;
			}

			try {
				return finder.findPath(maxRegionSize, maxRegionSize, toX, toY);
			} catch(Exception e) {
				e.printStackTrace();
				return new Path();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

