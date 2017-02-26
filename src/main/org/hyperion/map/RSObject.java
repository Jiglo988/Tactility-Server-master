package org.hyperion.map;

public class RSObject {
	public RSObject(int objectId, int x, int y, int height, int type, int direction) {
		id = objectId;
		face = direction;
		this.type = type;
	}

	private int id, face, type;

	public int id() {
		return id;
	}

	public int direction() {
		return face;
	}

	public int type() {
		return type;
	}
}
