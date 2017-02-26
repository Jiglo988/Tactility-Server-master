package org.hyperion.rs2.model;

/**
 * The request manager manages
 *
 * @author Graham Edgecombe
 */
public class RequestManager {


	/**
	 * Represents the different types of request.
	 *
	 * @author Graham Edgecombe
	 */
	public enum RequestType {

		/**
		 * A trade request.
		 */
		TRADE("tradereq"),

		/**
		 * A duel request.
		 */
		DUEL("duelreq");

		/**
		 * The client-side name of the request.
		 */
		private String clientName;

		/**
		 * Creates a type of request.
		 *
		 * @param clientName The name of the request client-side.
		 */
		private RequestType(String clientName) {
			this.clientName = clientName;
		}

		/**
		 * Gets the client name.
		 *
		 * @return The client name.
		 */
		public String getClientName() {
			return clientName;
		}

		public void setClientName(String s) {
			clientName = s;
		}

	}

	/**
	 * Holds the different states the manager can be in.
	 *
	 * @author Graham Edgecombe
	 */
	public enum RequestState {

		/**
		 * Nobody has offered a request.
		 */
		NORMAL,

		/**
		 * Somebody has offered some kind of request.
		 */
		REQUESTED,

		/**
		 * The player is participating in an existing request of this type, so
		 * cannot accept new requests at all.
		 */
		PARTICIPATING;

	}


	/**
	 * The current request type.
	 */
	private RequestType requestType;

	/**
	 * The current 'acquaintance'.
	 */
	//private Player acquaintance;

	/**
	 * Creates the request manager.
	 *
	 * @param player The player whose requests the manager is managing.
	 */
	public RequestManager(Player player) {
		//this.player = player;
	}

	public void setRequestType(RequestType r) {
		requestType = r;
	}

	public RequestType getRequestType() {
		return requestType;
	}

}