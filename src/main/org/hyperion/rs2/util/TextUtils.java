package org.hyperion.rs2.util;

import org.hyperion.rs2.Constants;

import java.io.*;

/**
 * Text utility class.
 *
 * @author Graham Edgecombe
 */
public class TextUtils {

	/**
	 * Unpacks text.
	 *
	 * @param packedData The packet text.
	 * @param size       The length.
	 * @return The string.
	 */
	public static String textUnpack(byte packedData[], int size) {
		byte[] decodeBuf = new byte[4096];
		int idx = 0, highNibble = - 1;
		for(int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if(highNibble == - 1) {
				if(val < 13) {
					decodeBuf[idx++] = (byte) Constants.XLATE_TABLE[val];
				} else {
					highNibble = val;
				}
			} else {
				decodeBuf[idx++] = (byte) Constants.XLATE_TABLE[((highNibble << 4) + val) - 195];
				highNibble = - 1;
			}
		}
		return new String(decodeBuf, 0, idx);
	}

    public static String pmText(int i, byte[] array) {
        int j = 0;
        final char[] charArr = new char[100];
        for(int l = 0; l < i; l++) {
			char ch = VALID_CHARS[array[l]];
            charArr[j++] = ch;
        }
        boolean flag1 = true;
        for(int k1 = 0; k1 < j; k1++) {
            char client = charArr[k1];
            if(flag1 && client >= 'a' && client <= 'z') {
                charArr[k1] += '\uFFE0';
                flag1 = false;
            }
            if(client == '.' || client == '!' || client == '?')
                flag1 = true;
        }
        return new String(charArr, 0, j);
    }

	public static String shortIp(String fullIp) {
		String[] parts = fullIp.split(":");
		return parts[0].replace("/", "");
	}

    private static final char[] USERNAME_TRANSLATE_TABLE = { ' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9' };

    public static String hashToUsername(long value) {
        int length = 0;
        char[] chars = new char[12];
        boolean capitalize = true;
        while (value != 0L) {
            long current = value;
            value /= 37L;
            if(length > 12)
                continue;
            chars[11 - length++] = USERNAME_TRANSLATE_TABLE[(int) (current - (value * 37L))];
        }
        for (int i = 12 - length; i < 12; i++) {
            if (capitalize) {
                if ((chars[i] >= 'a') && (chars[i] <= 'z')) {
                    chars[i] += '\uFFE0';
                }
                if (chars[i] != ' ') {
                    capitalize = false;
                }
            } else if (chars[i] == ' ') {
                capitalize = true;
            }
        }
        return new String(chars, 12 - length, length);
    }

/*	public static String textUnpack(byte[] stream, int i) {
		int j = 0;
		int k = -1;
		char[] decodeBuf = new char[4096];
		for(int l = 0; l < i; l++) {
			int i1 = stream[l];
			decodeBuf[j++] = validChars[i1];//oo gotta fix that i guess
		}
		boolean flag1 = true;
		for(int k1 = 0; k1 < j; k1++) {
			char c = decodeBuf[k1];
			if(flag1 && c >= 'a' && c <= 'z') {
				decodeBuf[k1] += '\uFFE0';
				flag1 = false;
			}
			if(c == '.' || c == '!' || c == '?')
				flag1 = true;
		}
		return new String(decodeBuf, 0, j);
	}*/

	private static char VALID_CHARS[] = {
			' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r',
			'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
			'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', ' ', '!', '?',
			'.', ',', ':', ';', '(', ')', '-', '&', '*', '\\',
			'\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[',
			']', '>', '<', '^', '/', '_'
	};

	/**
	 * Optimises text.
	 *
	 * @param text The text to optimise.
	 * @return The text.
	 */
	public static String optimizeText(String text) {
		char buf[] = text.toCharArray();
		boolean endMarker = true;
		for(int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if(endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if(c == '.' || c == '!' || c == '?') {
				endMarker = true;
			}
		}
		return new String(buf, 0, buf.length);
	}

	/**
	 * Gets the Username with the first Character capitalized.
	 *
	 * @param text
	 * @return
	 */
	public static String ucFirst(String text) {
		return java.lang.Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

	public static String titleCase(String source){
		return titleCase(source, true);
	}
	
	public static String titleCase(String source, boolean space) {
		StringBuilder stringBuilder = new StringBuilder();
        if(source == null || source.isEmpty())
            return "";
        int args = source.split(" ").length;
		for(String target : source.split(" ")){
            if(target == null || target.isEmpty())
                continue;
			stringBuilder.append(java.lang.Character.toUpperCase(target.charAt(0)));
			stringBuilder.append(target.substring(1).toLowerCase());
			if(space && args > 1)
				stringBuilder.append(" ");
			args--;
		}
        String tag = stringBuilder.toString();
		return tag.substring(0, 1).toUpperCase()+tag.substring(1);
	}

	public static void textPack(byte[] stream, String s) {
		if(s.length() > 80)
			s = s.substring(0, 80);
		s = s.toLowerCase();
		int i = - 1;
		for(int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);
			int k = 0;
			for(int l = 0; l < VALID_CHARS.length; l++) {
				if(c != VALID_CHARS[l])
					continue;
				k = l;
				break;
			}
			stream[++ i] = (byte) k;
		}
	}


	/**
	 * Filters invalid characters out of a string.
	 *
	 * @param s The string.
	 * @return The filtered string.
	 */
	public static String filterText(String s) {
		StringBuilder bldr = new StringBuilder();
		for(char c : s.toLowerCase().toCharArray()) {
			boolean valid = false;
			for(char validChar : Constants.XLATE_TABLE) {
				if(validChar == c) {
					valid = true;
				}
			}
			if(valid) {
				bldr.append(c);
			}
		}
		return bldr.toString();
	}

	public static void writeToFile(String file, String format, Object...args) {
		writeToFile(file, String.format(format,args));
	}

	public static void writeToFile(String file, String line) {
		writeToFile(new File(file), line);
	}

	public static void writeToFile(File file, String... lines) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
			for(String line : lines) {
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void writeToTxtFile(String write) {
		try {
			FileWriter outFile = new FileWriter("itemnames.txt");
			PrintWriter out = new PrintWriter(outFile);

			// Also could be written as follows on one line
			// Printwriter out = new PrintWriter(new FileWriter(args[0]));

			// Write text to file
			out.println(write);
			out.println("This is line 2");
			out.print("This is line3 part 1, ");
			out.println("this is line 3 part 2");
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
