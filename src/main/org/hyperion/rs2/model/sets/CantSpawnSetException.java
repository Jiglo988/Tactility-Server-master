package org.hyperion.rs2.model.sets;

public class CantSpawnSetException extends Exception {

	private static final long serialVersionUID = -9062959968137410368L;
	
	@Override
	public String getMessage() {
		return "You cannot spawn an instant set here";
	}

}
