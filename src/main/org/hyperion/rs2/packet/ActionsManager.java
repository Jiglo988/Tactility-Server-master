package org.hyperion.rs2.packet;

import java.util.HashMap;

public class ActionsManager {

	private static ActionsManager manager = new ActionsManager();

	public static ActionsManager getManager() {
		return manager;
	}

	private HashMap<Integer, ButtonAction> buttonActions = new HashMap<Integer, ButtonAction>();

	public void submit(int button, ButtonAction action) {
		buttonActions.put(button, action);
	}

	public ButtonAction getButtonAction(int id) {
		return buttonActions.get(id);
	}

}
