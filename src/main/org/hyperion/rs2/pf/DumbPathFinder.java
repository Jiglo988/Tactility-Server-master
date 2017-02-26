package org.hyperion.rs2.pf;

import org.hyperion.rs2.model.Position;

/**
 * An implementation of a <code>PathFinder</code> which is 'dumb' and only
 * looks at surrounding tiles for a path, suitable for an NPC.
 *
 * @author Graham Edgecombe
 */
public class DumbPathFinder implements PathFinder {

	@Override
	public Path findPath(Position position, int radius, TileMap map, int srcX, int srcY, int dstX, int dstY) {
		int stepX = 0, stepY = 0;
		if(srcX > dstX && map.getTile(dstX, srcY).isEasternTraversalPermitted() && map.getTile(srcX, srcY).isWesternTraversalPermitted()) {
			stepX = - 1;
		} else if(srcX < dstX && map.getTile(dstX, srcY).isWesternTraversalPermitted() && map.getTile(srcX, srcY).isEasternTraversalPermitted()) {
			stepX = 1;
		}
		if(srcY > dstY && map.getTile(srcX, dstY).isNorthernTraversalPermitted() && map.getTile(srcX, srcY).isSouthernTraversalPermitted()) {
			stepY = - 1;
		} else if(srcY < dstY && map.getTile(srcX, dstY).isSouthernTraversalPermitted() && map.getTile(srcX, srcY).isNorthernTraversalPermitted()) {
			stepY = 1;
		}
		if(stepX != 0 || stepY != 0) {
			Path p = new Path();
			//p.addPoint(new Point(srcX, srcY));
			p.addPoint(new Point(stepX, stepY));
			return p;
		}
		return null;
	}

	
	/*public boolean canWalk(TileMap map, int srcX, int srcY, int dstX, int dstY){
	    int i = 0;
		if(srcX > dstY && map.getTile(srcX, dstY).isSouthernTraversalPermitted()) {
			i++;
		} else if(srcX > dstY && map.getTile(srcX, dstY).isNorthernTraversalPermitted()) {
			i++;
		}
		if(srcX > dstX && map.getTile(dstX, srcY).isWesternTraversalPermitted()) {
			i++;
		} else if(srcX < dstX && map.getTile(dstX, srcY).isEasternTraversalPermitted()) {
			i++;
		}
		if(i == 2)
			return true;
	}*/
}
