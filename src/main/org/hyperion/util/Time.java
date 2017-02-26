package org.hyperion.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author SaosinHax
 */
public class Time {

	/**
	 * The amount of time in milliseconds.
	 */
	public static final long
			ONE_SECOND = 1000L,
			FIVE_SECONDS = 5 * ONE_SECOND,
			TEN_SECONDS = 10 * ONE_SECOND,
			FIFTEEN_SECONDS = 15 * ONE_SECOND,
			THIRTY_SECONDS = 30 * ONE_SECOND,

	ONE_MINUTE = 1000 * 60L,
			FIVE_MINUTES = 5 * ONE_MINUTE,
			TEN_MINUTES = 10 * ONE_MINUTE,
			THIRTY_MINUTES = 30 * ONE_MINUTE,

	ONE_HOUR = ONE_MINUTE * 60,
			TEN_HOURS = ONE_HOUR * 10,
            FIVE_TEEN_HOURS = ONE_HOUR * 15,
			ONE_DAY = ONE_HOUR * 24,
			TWO_DAYS = ONE_DAY * 2,
			ONE_WEEK = ONE_DAY * 7,
			ONE_MONTH = (long) (ONE_DAY * 30.42);

	/**
	 * The formatter
	 */
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

	/**
	 * Gets the system time in seconds.
	 *
	 * @return the system time in seconds.
	 */
	public static long currentTimeSeconds() {
		return System.currentTimeMillis() / ONE_SECOND;
	}

	/**
	 * Gets the system time in minutes.
	 *
	 * @return the system time in minutes.
	 */
	public static long currentTimeMinutes() {
		return System.currentTimeMillis() / ONE_MINUTE;
	}

	/**
	 * Gets the system time in hours.
	 *
	 * @return the system time in hours.
	 */
	public static long currentTimeHours() {
		return System.currentTimeMillis() / ONE_HOUR;
	}

	/**
	 * Gets the GMT Date as a String.
	 *
	 * @return the GMT date string
	 */
	public static String getGMTDate() {
		return FORMATTER.format(new Date());
	}


	/**
	 * Sets the formatter to GMT.
	 */
	static {
		FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
}
