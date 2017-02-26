package org.hyperion.rs2.model;

import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;

public class PlayerChecker {
	private ContainerListener inventoryListener;
	private ContainerListener bankListener;
	private Container inventoryContainer;
	private Container bankContainer;
	
	public synchronized ContainerListener getBankListener() {
		return bankListener;
	}
	public synchronized ContainerListener getInvListener() {
		return inventoryListener;
	}	
	public synchronized Container getBank() {
		return bankContainer;
	}
	public synchronized Container getInv() {
		return inventoryContainer;
	}
	
	
	public synchronized void setBankListener(ContainerListener listener) {
		this.bankListener = listener;
	}
	public synchronized void setInvListener(ContainerListener listener) {
		this.inventoryListener = listener;
	}
	public synchronized void setInv(Container inventoryContainer) {
		this.inventoryContainer = inventoryContainer;
	}
	public synchronized void setBank(Container bankContainer) {
		this.bankContainer = bankContainer;
	}
	
	private PlayerChecker() {

	}
	
	/**
	 *	Create methods always help with arguments etc because eclipse auto-fills them
	 *	unlike constructors
	 */
	
	public static PlayerChecker create() {
		return new PlayerChecker();
	}

}
