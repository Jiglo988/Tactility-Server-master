package org.hyperion.rs2.model.container.duel;

public final class DuelRule {
	public static enum DuelRules {
		FORFEIT(1, "forfeit"),
		MOVEMENT(2, "move"),
		RANGE(16, "use range"),
		MELEE(32, "use melee"),
		MAGE(64, "use magic"),
		DRINKS(128, "consume drinks"),
		FOOD(256,"eat food"),
		PRAYER(512, "use prayer"),
		OBSTACLES(1024, "not have obstacles"),
		SWITCH(4096, "switch"),
		SPECIAL(8192, "use special attacks");
		
		
		private final int flag;
		private final String message;
		private DuelRules(final int flag, final String message) {
			this.flag = flag;
			this.message =	new StringBuilder("You cannot ").append(message).append(" in this duel").toString();

		}
		
		public String getMessage() {
			return message;
		}
		
		public int getFlag() {
			return flag;
		}
	}
	
	public static DuelRules forId(final int id) {
		try {
		return DuelRules.values()[id];
		}catch(Exception ex) {}
		return null;
	}
	
	
}
