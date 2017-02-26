package org.hyperion.rs2.model.region;

/**
 * Holds the x and y coordinate for a region.
 *
 * @author Graham Edgecombe
 * @author Arsen Maxyutov
 */
public class RegionCoordinates {

	/**
	 * X coordinate.
	 */
	private int x;

	/**
	 * Y coordinate.
	 */
	private int y;

	/**
	 * Creates the region coordinate.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public RegionCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x coordinate.
	 *
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate.
	 *
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return 1000 * x + y;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		RegionCoordinates other = (RegionCoordinates) obj;
		if(x != other.x) {
			return false;
		}
		if(y != other.y) {
			return false;
		}
		return true;
	}

}
