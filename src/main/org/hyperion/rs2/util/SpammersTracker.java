package org.hyperion.rs2.util;

public class SpammersTracker {


	private SpammersTracker() {

	}

	private static SpammersTracker singleton = new SpammersTracker();

	public static SpammersTracker getTracker() {
		return singleton;
	}

}
