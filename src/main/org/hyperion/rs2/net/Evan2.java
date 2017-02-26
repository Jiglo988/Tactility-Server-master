package org.hyperion.rs2.net;

/*
 * @author Martin
 */

public class Evan2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(computeValue());
	}

	public static int computeValue() {
		for(int i = 1; i < 1000; i++) {
			if(oneRemainer(i)) {
				if(i % 7 == 0) {
					return i;
				}
			}
		}
		return 0;
	}

	public static boolean oneRemainer(int number) {
		for(int i2 = 2; i2 < 6; i2++) {
			if(number % i2 != 1) {
				return false;
			}
		}
		return true;
	}

}
