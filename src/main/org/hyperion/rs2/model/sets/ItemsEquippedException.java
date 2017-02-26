package org.hyperion.rs2.model.sets;

public class ItemsEquippedException extends Exception {

	private static final long serialVersionUID = -3481581866751441413L;
	
	@Override
	public String getMessage() {
		return "Please take off all your items before spawning an instant set";
	}
}
