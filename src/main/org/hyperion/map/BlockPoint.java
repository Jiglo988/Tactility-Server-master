package org.hyperion.map;


public class BlockPoint {

	public int x, y, height, s;

	public BlockPoint(int x, int y, int height, int s) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.s = s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + s;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		BlockPoint other = (BlockPoint) obj;
		if(height != other.height)
			return false;
		if(s != other.s)
			return false;
		if(x != other.x)
			return false;
		if(y != other.y)
			return false;
		return true;
	}

}
