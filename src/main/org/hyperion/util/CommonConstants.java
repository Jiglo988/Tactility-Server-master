package org.hyperion.util;

/**
 * Common constants.
 *
 * @author Graham Edgecombe
 */
public final class CommonConstants {

	/**
	 * Login server port.
	 */
	public static final int LOGIN_PORT = getLoginPort();

	public static int getLoginPort() {
		return 43596;
	}

}
