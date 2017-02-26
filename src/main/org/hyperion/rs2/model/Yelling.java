package org.hyperion.rs2.model;

public class Yelling {

	public static final String[] COLOUR_SUFFICES = {
			"369", "mon", "red", "gre", "blu", "yel", "cya", "mag", "whi", "bla", "lre", "dre", "dbl", "or1", "or2", "or3", "gr1", "gr2", "gr3", "str", "end"
	};

	private String yellTitle = "";

	private boolean yellEnabled = true;

	private boolean yellColoursEnabled = true;

	private long yellTimer = 0;

	public void setYellTitle(String s) {
		yellTitle = s;
	}
	private static final String UNAVAILABLE_TAGS[] = {
			"Owner",
			"Mod",
			"Admin",
			"Staff",
			"Manager",
			"Creator",
			"Distributor",
			"Sell",
			"Buy",
			"Spawn",
			"Sucks",
			"Hate",
			"Flame",
			"Bitch",
			"Nigga",
			"Hoe",
			"Whore",
			"Scam",
			"Shit",
			"Demote",
			"Trusted",
			"Fuck",
			"Slut",
			"Kappa"

	};
	public static String isValidTitle(String s) {
		StringBuilder errorMessage = new StringBuilder("").append("You cannot have ");
		if(s.contains("@"))
			errorMessage.append("@s, ");
		for(String wrong : UNAVAILABLE_TAGS) {
			if(s.toLowerCase().contains(wrong.toLowerCase()))
				errorMessage.append(wrong).append(", ");
		}
		if(errorMessage.toString().length() > 18) {
			errorMessage.append(" in your tag");
			return errorMessage.toString();
		} else
			return "";
	}
	
	public String getTag() {
		return yellTitle;
	}
	public void updateYellTimer() {
		yellTimer = System.currentTimeMillis();
	}

	public void setYellEnabled(boolean b) {
		yellEnabled = b;
	}

	public void setYellColoursEnabled(boolean b) {
		yellColoursEnabled = b;
	}

	public boolean isYellEnabled() {
		return yellEnabled;
	}

	public boolean isYellColoursEnabled() {
		return yellColoursEnabled;
	}

	public long getYellTimer() {
		return yellTimer;
	}

	static {
	}
}
