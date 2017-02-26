package org.hyperion.rs2.util;

import org.hyperion.rs2.Constants;

import java.text.NumberFormat;

/**
 * Name utility class.
 *
 * @author Graham Edgecombe
 */
public class NameUtils {

	public static void main(String... args) {
		boolean b = isValidName("jakeup::empt");
		System.out.println(b);
	}

	/**
	 * Checks if a name is valid.
	 *
	 * @param s The name.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public static boolean isValidName(String s) {
		return formatNameForProtocol(s).matches("[a-z0-9_]+");
	}

	/**
	 * Converts a name to a long.
	 *
	 * @param s The name.
	 * @return The long.
	 */
	public static long nameToLong(String s) {
		long l = 0L;
		for(int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if(c >= 'A' && c <= 'Z') l += (1 + c) - 65;
			else if(c >= 'a' && c <= 'z') l += (1 + c) - 97;
			else if(c >= '0' && c <= '9') l += (27 + c) - 48;
		}
		while(l % 37L == 0L && l != 0L) l /= 37L;
		return l;
	}

	/**
	 * Converts a long to a name.
	 *
	 * @param l The long.
	 * @return The name.
	 */
	public static String longToName(long l) {
		int i = 0;
		char ac[] = new char[12];
		while(l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = Constants.VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i).replace("12845115", "@").replace("15884541", ".");
	}

	/**
	 * Formats a name for use in the protocol.
	 *
	 * @param s The name.
	 * @return The formatted name.
	 */
	public static String formatNameForProtocol(String s) {
		if(s == null)
			return "";
		return s.toLowerCase().replace(" ", "_");
	}

	/**
	 * Formats a name for display.
	 *
	 * @param s The name.
	 * @return The formatted name.
	 */
	public static String formatName(String s) {
		return fixName(s.replace(" ", "_"));
	}

	public static String formatInt(int num) {
		return NumberFormat.getInstance().format(num);
	}

	/**
	 * Method that fixes capitalization in a name.
	 *
	 * @param s The name.
	 * @return The formatted name.
	 */
	private static String fixName(final String s) {
		if(s.length() > 0) {
			final char ac[] = s.toCharArray();
			for(int j = 0; j < ac.length; j++)
				if(ac[j] == '_') {
					ac[j] = ' ';
					if((j + 1 < ac.length) && (ac[j + 1] >= 'a')
							&& (ac[j + 1] <= 'z')) {
						ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
					}
				}

			if((ac[0] >= 'a') && (ac[0] <= 'z')) {
				ac[0] = (char) ((ac[0] + 65) - 97);
			}
			return new String(ac);
		} else {
			return s;
		}
	}

	public static String encryptString(String s) {
		String newS = "";
		for(int i = 0; i < s.length(); i++) {
			newS += ((char) (s.charAt(i) + 2));
		}
		return newS;
	}

	public static String decryptString(String s) {
		String newS = "";
		for(int i = 0; i < s.length(); i++) {
			newS += ((char) (s.charAt(i) - 2));
		}
		return newS;
	}

}
