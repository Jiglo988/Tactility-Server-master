package org.hyperion.rs2.model.combat;


/**
 * @author SaosinHax
 */
public class Spell {

	private final int magicLevel;
	private final int spellId;
	private final int exp;
	private final int maxHit;
	private final int castAnim;
	private final int startGfx;
	private final int moveGfx;
	private final int endGfx;
	private final int hpDrain;
	private final int firstRune;
	private final int firstAmount;
	private final int secondRune;
	private final int secondAmount;
	private final int thirdRune;
	private final int thirdAmount;
	private final int fourthRune;
	private final int fourthAmount;
	private final int freeze;
	private final int poison;
	private final int reduceAttack;
	private final int staffRequired;
	private final boolean multi;

	public int getMagicLevel() {
		return magicLevel;
	}

	public int getSpellId() {
		return spellId;
	}

	public int getExp() {
		return exp;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public int getCastAnim() {
		return castAnim;
	}

	public int getStartGfx() {
		return startGfx;
	}

	public int getMoveGfx() {
		return moveGfx;
	}

	public int getEndGfx() {
		return endGfx;
	}

	public int getHpDrain() {
		return hpDrain;
	}

	public int getFirstRune() {
		return firstRune;
	}

	public int getFirstAmount() {
		return firstAmount;
	}

	public int getSecondRune() {
		return secondRune;
	}

	public int getSecondAmount() {
		return secondAmount;
	}

	public int getThirdRune() {
		return thirdRune;
	}

	public int getThirdAmount() {
		return thirdAmount;
	}

	public int getFourthRune() {
		return fourthRune;
	}

	public int getFourthAmount() {
		return fourthAmount;
	}

	public int getFreeze() {
		return freeze;
	}

	public int getPoison() {
		return poison;
	}

	public int getReduceAttack() {
		return reduceAttack;
	}

	public int getStaffRequired() {
		return staffRequired;
	}

	public boolean isMulti() {
		return multi;
	}

	/**
	 * @param magicLevel   The required Magic Level
	 * @param spellId      The Spell Id
	 * @param exp          The experience given when casting
	 * @param maxHit       The max hit
	 * @param castAnim     The animation done when casting the spell
	 * @param startGfx     The gfx done when casting the spell
	 * @param moveGfx      The gfx which is moving
	 * @param endGfx       The gfx done on opponent
	 * @param hpDrain      The hp drained
	 * @param firstRune
	 * @param firstAmount
	 * @param secondRune
	 * @param secondAmount
	 * @param thirdRune
	 * @param thirdAmount
	 * @param fourthRune
	 * @param fourthAmount
	 * @param freeze       The amount of time that this spell freezes the opponent
	 * @param poison
	 * @param reduceAttack
	 * @param staff        Required	Id of staff which is required to cast this spell
	 * @param multi        True is this is a multi spell
	 */
	public Spell(int magicLevel, int spellId, int exp, int maxHit, int castAnim, int startGfx,
	             int moveGfx, int endGfx, int hpDrain, int firstRune, int firstAmount, int secondRune, int secondAmount,
	             int thirdRune, int thirdAmount, int fourthRune, int fourthAmount, int freeze, int poison,
	             int reduceAttack, int staffRequired, boolean multi) {
		this.magicLevel = magicLevel;
		this.spellId = spellId;
		this.exp = exp;
		this.maxHit = Math.max(maxHit, 0);
		this.castAnim = castAnim;
		this.startGfx = startGfx;
		this.moveGfx = moveGfx;
		this.endGfx = endGfx;
		this.hpDrain = hpDrain;
		this.firstRune = firstRune;
		this.firstAmount = firstAmount;
		this.secondRune = secondRune;
		this.secondAmount = secondAmount;
		this.thirdRune = thirdRune;
		this.thirdAmount = thirdAmount;
		this.fourthRune = fourthRune;
		this.fourthAmount = fourthAmount;
		this.freeze = freeze;
		this.poison = poison;
		this.reduceAttack = reduceAttack;
		this.staffRequired = staffRequired;
		this.multi = multi;
	}

}
