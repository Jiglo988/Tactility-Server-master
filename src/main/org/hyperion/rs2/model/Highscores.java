package org.hyperion.rs2.model;

import java.util.Arrays;

public class Highscores {

	/**
	 * The minimum level in a skill to be showed on highscores.
	 */
	public static final int MIN_HIGHSCORES_LEVEL = 50;

	/**
	 * Holds all column names for the database.
	 */
	public final static String[] SKILLS_COLUMN_NAMES = {"Atk", "Def", "Str", "Hit", "Rng",
			"Pray", "Mage", "Cook", "Wood", "Flet", "Fish", "Fire", "Craf", "Smit",
			"Mine", "Herb", "Agil", "Thief", "Slay", "Farm", "Rc", "Cons", "Hunt", "Summ",
	};


	public Highscores(Player player) {
		this.player = player;
		this.name = player.getName().replaceAll(" ", "_").toLowerCase();
		this.elo = player.getPoints().getEloRating();
		this.exps = Arrays.copyOf(player.getSkills().getExps(), player.getSkills().getExps().length);
		this.total = player.getSkills().getTotalLevel();
		this.overall = player.getSkills().getTotalExp();
		this.honors = player.getPoints().getHonorPoints();
	}

	/**
	 * @param player
	 * @param name
	 * @param elo
	 * @param exps
	 * @param total
	 * @param overall
	 */
	public Highscores(Player player, String name, int elo, int[] exps, int total, long overall) {
		this.player = player;
		this.name = name.replaceAll(" ", "_").toLowerCase();
		this.elo = elo;
		this.exps = Arrays.copyOf(exps, exps.length);
		this.total = total;
		this.honors = player.getPoints().getHonorPoints();
		this.overall = overall;
	}

	private final Player player;

	private final String name;

	private final int elo;

	private final int honors;

	private final int total;

	private final long overall;

	private final int[] exps;

	public boolean needsUpdate() {
		if(player.getExtraData().getBoolean("rhsu"))
			return true;
		for(int i = Skills.MAGIC + 1; i < player.getSkills().getExps().length; i++) {
			if(player.getSkills().getLevelForExp(i) >= MIN_HIGHSCORES_LEVEL && player.getSkills().getExperience(i) != exps[i]) {
				//System.out.println(player.getSkills().getLevelForExp(i));
				return true;
			}
		}
		return player.getPoints().getEloRating() != elo || player.getPoints().getHonorPoints() != honors;
	}

	public String getName() {
		return name;
	}

	public int getElo() {
		return elo;
	}

	public int[] getExps() {
		return exps;
	}

	public int getTotalLevel() {
		return total;
	}

	public long getOverallExp() {
		return overall;
	}

	public int getHonors() {
		return honors;
	}

	public String getInsertQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO highscores (");
		for(String skill : SKILLS_COLUMN_NAMES) {
			sb.append(skill).append(",");
		}

		sb.append("Total,Overall,elo,Name) VALUES (");
		for(int i = 0; i < SKILLS_COLUMN_NAMES.length; i++) {
			sb.append(player.getSkills().getExperience(i)).append(",");
		}
		sb.append(total + "," + overall + "," + elo + ",'" + name + "')");
		//System.out.println(sb.toString());
		return sb.toString();
	}

	public String getUpdateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE highscores SET ");
		for(int i = 0; i < SKILLS_COLUMN_NAMES.length; i++) {
			if(player.getSkills().hasChanged(i) || player.getExtraData().getBoolean("rhsu")) {
				sb.append(SKILLS_COLUMN_NAMES[i] + " = " + player.getSkills().getExperience(i) + ",");
			}
		}
		if(total != player.getSkills().getTotalLevel()) {
			sb.append("Total = " + player.getSkills().getTotalLevel() + ",");
		}
		if(overall != player.getSkills().getTotalExp()) {
			sb.append("Overall = " + player.getSkills().getTotalExp() + ",");
		}
		if(elo != player.getPoints().getEloRating()) {
			sb.append("elo = " + player.getPoints().getEloRating() + ",");
		}
		if(honors != player.getPoints().getHonorPoints()) {
			sb.append("honors = " + player.getPoints().getHonorPoints() + ",");
		}
		sb.deleteCharAt(sb.length() - 1); //delete the comma at the end.
		sb.append(" WHERE Name = '" + name + "'");
		//System.out.println(sb.toString());
		player.getExtraData().remove("rhsu");
		return sb.toString();
	}
}
