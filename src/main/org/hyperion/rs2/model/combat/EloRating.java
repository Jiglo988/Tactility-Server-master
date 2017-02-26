package org.hyperion.rs2.model.combat;

/*
 * JOGRE (Java Online Gaming Real-time Engine) - API
 * Copyright (C) 2004  Bob Marks (marksie531@yahoo.com)
 * http://jogre.sourceforge.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


/**
 * JOGRE's implementation of the ELO rating system.  The following is an
 * example of how to use the Elo Rating System.
 * <code>
 * int userRating = 1600;
 * int opponentRating = 1650;
 * int newUserRating = getNewRating(userRating, opponentRating, WIN);
 * int newOpponentRating = getNewRating(opponentRating, userRating, LOSS);
 * </code>
 *
 * @author Garrett Lehman (gman)
 */
public class EloRating {


	// Score constants
	private final static double GWIN = 1.0;
	private final static double GDRAW = 0.5;
	private final static double GLOSS = 0.0;


	/**
	 * Default ELO starting rating for new users.
	 */
	public static final int DEFAULT_ELO_START_RATING = 1200;

	/**
	 * Default ELO k factor.
	 */
	public static final double DEFAULT_ELO_K_FACTOR = 24.0;

	/**
	 * Player wins a game.
	 */
	public static final int WIN = 1;


	/**
	 * Player losses a game.
	 */
	public static final int LOSE = 2;


	/**
	 * Player draws with another player.
	 */
	public static final int DRAW = 3;

	/**
	 * Convience overloaded version of getNewRating (int, int, double)
	 * which takes a result type and
	 *
	 * @param rating
	 * @param opponentRating
	 * @param resultType
	 * @return
	 */
	public static int getNewRating(int rating, int opponentRating, int resultType) {
		switch(resultType) {
			case WIN:
				return getNewRating(rating, opponentRating, GWIN);
			case LOSE:
				return getNewRating(rating, opponentRating, GLOSS);
			case DRAW:
				return getNewRating(rating, opponentRating, GDRAW);
		}
		return - 1;        // no score this time.
	}

	/**
	 * Get new rating.
	 *
	 * @param rating         Rating of either the current player or the average of the
	 *                       current team.
	 * @param opponentRating Rating of either the opponent player or the average of the
	 *                       opponent team or teams.
	 * @param score          Score: 0=Loss 0.5=Draw 1.0=Win
	 * @return the new rating
	 */
	public static int getNewRating(int rating, int opponentRating, double score) {
		double kFactor = getKFactor(rating);
		double expectedScore = getExpectedScore(rating, opponentRating);
		int newRating = calculateNewRating(rating, score, expectedScore, kFactor);

		/** Max elo rating you can loose. */
		int maxLoose = - 20;
		int ratingDelta = newRating - rating;

		if(ratingDelta < maxLoose) {
			newRating = rating + maxLoose;
			return newRating;
		} else {
			return newRating;
		}

	}

	/**
	 * Calculate the new rating based on the ELO standard formula.
	 * newRating = oldRating + constant * (score - expectedScore)
	 *
	 * @param oldRating     Old Rating
	 * @param score         Score
	 * @param expectedScore Expected Score
	 * @param constant      Constant
	 * @return the new rating of the player
	 */
	private static int calculateNewRating(int oldRating, double score, double expectedScore, double kFactor) {
		return oldRating + (int) (kFactor * (score - expectedScore));
	}

	/**
	 * This is the standard chess constant.  This constant can differ
	 * based on different games.  The higher the constant the faster
	 * the rating will grow.  That is why for this standard chess method,
	 * the constant is higher for weaker players and lower for stronger
	 * players.
	 *
	 * @param rating Rating
	 * @return Constant
	 */
	private static double getKFactor(int rating) {
		if(rating < 2100)
			return 32;
		else if(rating < 2400)
			return 24;
		else
			return 16;
	}

	/**
	 * Get expected score based on two players.  If more than two players
	 * are competing, then opponentRating will be the average of all other
	 * opponent's ratings.  If there is two teams against each other, rating
	 * and opponentRating will be the average of those players.
	 *
	 * @param rating         Rating
	 * @param opponentRating Opponent(s) rating
	 * @return the expected score
	 */
	private static double getExpectedScore(int rating, int opponentRating) {
		return 1.0 / (1.0 + Math.pow(10.0, ((double) (opponentRating - rating) / 400.0)));

	}

}
