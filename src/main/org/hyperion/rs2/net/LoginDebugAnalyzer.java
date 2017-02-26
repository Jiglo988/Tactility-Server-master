package org.hyperion.rs2.net;

import java.io.BufferedReader;
import java.io.FileReader;

public class LoginDebugAnalyzer {

	private int counter = 0;

	private String lastName = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LoginDebugAnalyzer();
	}

	public static void printRectangle(int h, int l) {
		String blancoLine = "";
		for(int i = 0; i < l - 2; i++) {
			blancoLine += " ";
		}
		for(int i = 0; i < l; i++) {
			System.out.print("*");
		}
		for(int i = 0; i < h - 2; i++) {
			//System.out.println();
			//System.out.print("*" + blancoLine + "*");
		}
		System.out.println();
		for(int i = 0; i < l; i++) {
			//System.out.print("*");
		}
	}

	private LoginDebugAnalyzer() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("C:/Users/SaosinHax/Dropbox/Reckless/logindebug.log"));
			String line;
			while((line = in.readLine()) != null) {
				if(line.contains("Login Result")) {
					if(line.contains("unset"))
						continue;
					String name = findLoginResultName(line);
					//System.out.println(name);
					if(name.equals(lastName))
						counter++;
					else
						counter = 0;
					if(counter >= 1)
						System.out.println(line);
					lastName = name;
				}
			}
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String findLoginResultName(String line) {
		String name = line.split(":")[4].trim();
		return name.toLowerCase();
	}

}
