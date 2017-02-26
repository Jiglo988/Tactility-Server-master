package org.hyperion.map.pathfinding;

import org.hyperion.map.WorldMap;

/**
 * The data map from our example game. This holds the state and context of each tile
 * on the map. It also implements the interface required by the path finder. It's implementation
 * of the path finder related methods add specific handling for the types of units
 * and terrain in the example game.
 *
 * @author Kevin Glass
 */
public class GameMap implements TileBasedMap {
	/**
	 * The map width in tiles
	 */
	public static final int WIDTH = PathTest.maxRegionSize * 2;
	/**
	 * The map height in tiles
	 */
	public static final int HEIGHT = PathTest.maxRegionSize * 2;

	/**
	 * Indicator if a given tile has been visited during the search
	 */
	private boolean[][] visited = new boolean[WIDTH][HEIGHT];

	/**
	 * Create a new test map with some default configuration
	 */
	public GameMap() {

	}

	/**
	 * Clear the array marking which tiles have been visted by the path
	 * finder.
	 */
	public void clearVisited() {
		for(int x = 0; x < getWidthInTiles(); x++) {
			for(int y = 0; y < getHeightInTiles(); y++) {
				visited[x][y] = false;
			}
		}
	}

	public boolean visited(int x, int y) {
		return visited[x][y];
	}


	public boolean blocked(int sx, int sy, int x, int y) {
		//return if you can move here
	    /*if(sx == x || sy == y){
			if(!WorldMap.isWalkAble(0, (PathTest.getSingleton().baseX+sx), (PathTest.getSingleton().baseY+sy), (PathTest.getSingleton().baseX+x), (PathTest.getSingleton().baseY+y), 0))
				return true;
		} else {
			if(!WorldMap.isWalkAble(0, (PathTest.getSingleton().baseX+sx), (PathTest.getSingleton().baseY+sy), (PathTest.getSingleton().baseX+x), (PathTest.getSingleton().baseY+sy), 0))
				return true;
			if(!WorldMap.isWalkAble(0, (PathTest.getSingleton().baseX+x), (PathTest.getSingleton().baseY+sy), (PathTest.getSingleton().baseX+x), (PathTest.getSingleton().baseY+y), 0))
				return true;
			
			if(!WorldMap.isWalkAble(0, (PathTest.getSingleton().baseX+sx), (PathTest.getSingleton().baseY+sy), (PathTest.getSingleton().baseX+sx), (PathTest.getSingleton().baseY+y), 0))
				return true;
			if(!WorldMap.isWalkAble(0, (PathTest.getSingleton().baseX+sx), (PathTest.getSingleton().baseY+y), (PathTest.getSingleton().baseX+x), (PathTest.getSingleton().baseY+y), 0))
				return true;
		}*/
		//return WorldMap.isWalkAble(0, (PathTest.getSingleton().baseX+sx), (PathTest.getSingleton().baseY+sy), (PathTest.getSingleton().baseX+x), (PathTest.getSingleton().baseY+y), 0);
		return !WorldMap.checkPos(0, (PathTest.baseX + sx), (PathTest.baseY + sy), (PathTest.baseX + x), (PathTest.baseY + y), 0);
	}

	public float getCost(int sx, int sy, int tx, int ty) {
		return 1;
	}

	/**
	 * @see TileBasedMap#getHeightInTiles()
	 */
	public int getHeightInTiles() {
		return WIDTH;
	}

	/**
	 * @see TileBasedMap#getWidthInTiles()
	 */
	public int getWidthInTiles() {
		return HEIGHT;
	}

	/**
	 * @see TileBasedMap#pathFinderVisited(int, int)
	 */
	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}


}
