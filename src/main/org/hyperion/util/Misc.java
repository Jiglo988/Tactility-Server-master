package org.hyperion.util;

import java.text.DecimalFormat;
import java.util.Random;

public class Misc {

	public static final Random RANDOM = new Random(System.currentTimeMillis());

	public static <T> T randomElement(T[] array) {
		return array[(int) (RANDOM.nextDouble() * array.length)];
	}

	public static String aOrAn(String word) {
		return ((word.startsWith("A") || word.startsWith("E") || word.startsWith("O")) ? "an" : "a");
	}

	/**
	 * @param amount , for instance 14300000
	 * @return formatted value, in this case 14,3 Mil
	 */

	public static String getFormattedValue(int amount) {
		if(amount >= 1000000) {
			double mills = (double) amount / (double) 1000000;
			mills = round(mills, 2);
			return mills + " Mil";
		}
		DecimalFormat formatter = new DecimalFormat("#,###,###");
		String priceString = formatter.format(amount);
		return priceString;
	}

	public static boolean isInCircle(int x, int y, int x2, int y2, int r) {
		return Math.pow((x - x2), 2) + Math.pow((y - y2), 2) <= Math.pow(r, 2);
	}

	public static int inclusiveRandom(int min, int max) {
		if (max < min) {
			max = min + 1;
		}
		return exclusiveRandom((max - min) + 1) + min;
	}

	public static int inclusiveRandom(int range) {
		return inclusiveRandom(0, range);
	}

	public static int exclusiveRandom(int min, int max) {
		if (max <= min) {
			max = min + 1;
		}
		return RANDOM.nextInt((max - min)) + min;
	}

	public static int exclusiveRandom(int range) {
		return exclusiveRandom(0, range);
	}

	public static boolean contains(char needle, char[] haystack) {
		for(int i = 0; i < haystack.length; i++) {
			if(needle == Character.toLowerCase(haystack[i]) || needle == Character.toUpperCase(haystack[i])) {
				return true;
			}
		}
		return false;
	}

	public static String centerQuestTab(String str) {
		String spaces = "";
		for(int i = str.length(); i < 25; i++) {
			spaces += " ";
		}
		return spaces + str;
	}

	public static String shortNumber(double amount) {
		if (amount < 1000)
			return String.format("%.0f", amount);
		if (amount < 1000000)
			return (String.format(((amount%1000)%100 != 0 ? "%.2f" : "%.1f"), amount / 1000)  + "K").replace(",", ".");
		else
			return (String.format(((amount%1000000)%100000 != 0 ? "%.2f" : "%.1f"), amount / 1000000)  + "M").replace(",", ".");
	}

	public static String wrapString(String string, int charWrap) {
		int lastBreak = 0;
		int nextBreak = charWrap;
		if (string.length() > charWrap) {
			String setString = "";
			do {
				while (string.charAt(nextBreak) != ' ' && nextBreak > lastBreak) {
					nextBreak--;
				}
				if (nextBreak == lastBreak) {
					nextBreak = lastBreak + charWrap;
				}
				setString += string.substring(lastBreak, nextBreak).trim() + "\n";
				lastBreak = nextBreak;
				nextBreak += charWrap;

			} while (nextBreak < string.length());
			setString += string.substring(lastBreak).trim();
			return setString;
		} else {
			return string;
		}
	}

	/**
	 * @param bigarray
	 * @param columnindex
	 * @return an array which is actually column nr columnindex in the big array
	 * for example
	 * {
	 * <p/>
	 * columnindex
	 * <p/>
	 * 0 1 2
	 * <p/>
	 * {2,3,4},
	 * {4,5,6},
	 * {7,9,8},
	 * }
	 * getColumn(bigarray,2) will return
	 * <p/>
	 * {4,6,8}
	 */
	public static int[] getColumn(int[][] bigarray, int columnindex) {
		int[] smallarray = new int[bigarray.length];
		int counter = 0;
		for(int i = 0; i < bigarray.length; i++) {
			smallarray[counter++] = bigarray[i][columnindex];
		}
		return smallarray;
	}

	/**
	 * @param range
	 * @return Returns a random Integer from 0 to "range".
	 */
	public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	/**
	 * @param range
	 * @return Returns a random Integer from 1 to "range".
	 */
	public static int random2(int range) {
		return (int) (java.lang.Math.random() * range) + 1;
	}

	/**
	 * @param array1
	 * @param array2
	 * @returns Array1 + Array2
	 */
	public static int[] mergeArrays(int[] array1, int[] array2) {
		int length1 = array1.length;
		int length2 = array2.length;
		int[] newarray = new int[length1 + length2];
		int count = 0;
		for(int i = 0; i < array1.length; i++) {
			newarray[count++] = array1[i];
		}
		for(int i = 0; i < array2.length; i++) {
			newarray[count++] = array2[i];
		}
		return newarray;
	}

	/**
	 * @param value        for example 13,615465
	 * @param decimalPlace for example 2,
	 * @return returns 13,61
	 */
	public static double round(double value, int decimalPlace) {
		double power_of_ten = 1;

		while(decimalPlace-- > 0)
			power_of_ten *= 10.0;
		return Math.round(value * power_of_ten) / power_of_ten;
	}

	public static String formatPlayerName(String str) {
		str = ucFirst(str);
		str.replace("_", " ");
		return str;
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if(str.length() > 1) {
			str = str.substring(0,1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}


	public static String formatDocumentNumber(int value) {
		char[] chars;
		int i;
		int c;
		int size;
		int width = 3;
		size = 10 + 2;
		chars = new char[size];
		for(i = 0; i < size; i++) {
			if(i == 3 || i == 7) {
				chars[width - i - 1] = ',';
				continue;
			}
			c = value % 10;
			chars[width - i - 1] = (char) ('0' + c);
			value = value / 10;
		}
		return new String(chars);
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @returns distance cuz we love Pythagoreans
	 */
	public static int distance(int x1, int y1, int x2, int y2) {
		int deltax = x2 - x1;
		int deltay = y2 - y1;

		int distancesquared = deltax * deltax + deltay * deltay;
		int distance = (int) Math.sqrt(distancesquared);
		return distance;
	}

	private static int RestAnimation = 11786;

	private static int DaxeSpecial = 2876;

	public static int[] HomeTeleportGfx = {775, 800, 801, 802, 803, 804, 1703, 1704, 1705, 1706, 1707, 1708, 1709, 1710, 1711, 1712, 1713};
	public static int[] HomeTeleportAnimations = {1722, 1723, 1724, 1725, 2798, 2799, 2800, 3195, 4643, 4645, 4646, 4847, 4848, 4849, 4850, 4851, 4852};

	/**
	 * @param level
	 * @return Returns the Skill Name as String.
	 */

	public static String getSkillName(int level) {
		switch(level) {
			case 0:
				return " Attack ";
			case 1:
				return " Defence ";
			case 2:
				return " Strength ";
			case 3:
				return " Hitpoints ";
			case 4:
				return " Ranged ";
			case 5:
				return " Prayer ";
			case 6:
				return " Magic ";
			case 7:
				return " Cooking ";
			case 8:
				return " Woodcutting ";
			case 9:
				return " Fletching ";
			case 10:
				return " Fishing ";
			case 11:
				return " Firemaking ";
			case 12:
				return " Crafting ";
			case 13:
				return " Smithing ";
			case 14:
				return " Mining ";
			case 15:
				return " Herblore ";
			case 16:
				return " Agility ";
			case 17:
				return " Thieving ";
			case 18:
				return " Slayer ";
			case 19:
				return " Farming ";
			case 20:
				return " Runecrafting ";
			case 21:
				return "Construction ";
			case 22:
				return "Hunter ";
			case 23:
				return "Summoning ";
		}
		return " ";
	}

    public static final double getPercentage(final double remaining, final double original) {
        return remaining/original * 100.0D;
    }

    public static final String getPercentString(final double remaining, final double total) {
        return getPercentString(remaining, total, 1);
    }

    public static final String getPercentString(final double remaining, final double original,final int places) {
        final String format = new StringBuilder("%.").append(places).append("f").toString();
        return String.format(format, getPercentage(remaining, original));
    }

	public static int expandNumber(final String s){
		if(s.matches("\\d+(\\.\\d+)?k"))
			return (int)(Double.parseDouble(s.substring(0, s.length()-1)) * 1_000d);
		else if(s.matches("\\d+(\\.\\d+)?m"))
			return (int)(Double.parseDouble(s.substring(0, s.length()-1)) * 1_000_000d);
		else if(s.matches("\\d+(\\.\\d+)?b"))
			return (int)(Double.parseDouble(s.substring(0, s.length()-1)) * 1_000_000_000d);
		else
			return Integer.parseInt(s);
	}

	public static String formatNumber(final int number){
		return null;
	}

}
