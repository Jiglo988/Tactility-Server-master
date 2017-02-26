package org.hyperion.rs2;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * A static gateway type class that is used to limit the maximum amount of
 * connections per host.
 *
 * @author blakeman8192
 */
public class HostGateway {

	/**
	 * The maximum amount of connections per host.
	 */
	public static final int MAX_CONNECTIONS_PER_HOST = 10;

	/**
	 * Used to keep track of hosts and their amount of connections.
	 */
	private static Map<String, Integer> map = new HashMap<>();

	/**
	 * Checks the host into the gateway.
	 *
	 * @param ip the host
	 * @return true if the host can connect, false if it has reached the maximum
	 * amount of connections
	 */
	public static boolean canEnter(String ip) {
		Integer amount = map.get(ip);
		return amount == null || amount <= MAX_CONNECTIONS_PER_HOST;
	}

	/**
	 * Checks the host into the gateway.
	 *
	 * @param ip the host
	 * @return true if the host can connect, false if it has reached the maximum
	 * amount of connections
	 */
	public static void enter(String ip) {
		Integer amount = map.get(ip);
		if(amount == null) {
			map.put(ip, 1);
		} else {
			map.put(ip, amount + 1);
		}
	}

	/**
	 * Unchecks the host from the gateway.
	 *
	 * @param ip the host
	 */
	public static void exit(String ip) {
		if(map.get(ip) == null)
			return;
		Integer amount = map.get(ip);
		if(amount != null) {
			if(amount == 1)
				map.remove(ip);
			else
				map.put(ip, amount - 1);
		}
	}
}
