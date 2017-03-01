package org.hyperion.rs2.model.content.minigame.lastmanstanding;

import org.hyperion.rs2.model.GameObject;
import org.hyperion.rs2.model.GameObjectDefinition;
import org.hyperion.rs2.model.Position;

public class ChestObject extends GameObject implements Cloneable{

	public static final int ID = 378;
	
	private static final int TYPE = 10;
	
	private int remaining;
	
	//GameObjectDefinition.forId(EXPIRED_ORE), Location.create(objectX, objectY, player.getLocation().getZ()), 10, 0
	public ChestObject(Position location, int rotation, int defaultRemaining) {
		super(GameObjectDefinition.forId(ID), location, TYPE, rotation);
		this.setRemaining(defaultRemaining);
	}
	
	public ChestObject(ChestObject obj) {
		super(GameObjectDefinition.forId(ID), obj.getPosition(), TYPE, obj.getRotation());
		this.setRemaining(obj.getRemaining());
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public boolean decrement() {
		if (remaining == 0) {
			return false;
		}
		remaining--;
		return true;
	}

	public ChestObject createNew() {
		return new ChestObject(this);
	}
}
