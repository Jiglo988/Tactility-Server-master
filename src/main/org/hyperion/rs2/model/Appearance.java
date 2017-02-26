package org.hyperion.rs2.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds information about a single player's look.
 *
 * @author Graham Edgecombe
 */
public class Appearance {

	/**
	 * The gender.
	 */
	private int gender;

	/**
	 * The chest model.
	 */
	private int chest;

	/**
	 * The arms model.
	 */
	private int arms;

	/**
	 * The legs model.
	 */
	private int legs;

	/**
	 * The head model.
	 */
	private int head;

	/**
	 * The hands model.
	 */
	private int hands;

	/**
	 * The feet model.
	 */
	private int feet;

	/**
	 * The beard model.
	 */
	private int beard;

	/**
	 * The hair colour.
	 */
	private int hairColour;

	/**
	 * The torso colour.
	 */
	private int torsoColour;

	/**
	 * The legs colour.
	 */
	private int legColour;

	/**
	 * The feet colour.
	 */
	private int feetColour;

	/**
	 * The skin colour.
	 */
	private int skinColour;

	/**
	 * The standing animation.
	 */
	private int standAnim;

	/**
	 * The running animation.
	 */
	private int runAnim;

	/**
	 * The walking animation.
	 */
	private int walkAnim;

	/**
	 * The previous look.
	 */
	private int[] previousLook = new int[13];

	/**
	 * Creates the default player appearance.
	 */
	public Appearance() {
		resetAppearance();
		previousLook = getLook();
	}

	public void resetAppearance() {
		gender = 0;
		head = 0;
		chest = 18;
		arms = 26;
		hands = 33;
		legs = 36;
		feet = 42;
		beard = 10;
		hairColour = 7;
		torsoColour = 8;
		legColour = 9;
		feetColour = 5;
		skinColour = 0;
		standAnim = 0x328;
		walkAnim = 0x333;
		runAnim = 0x338;
	}

	/**
	 * Gets the look array, which is an array with 13 elements describing the
	 * look of a player.
	 *
	 * @return The look array.
	 */
	public int[] getLook() {
		return new int[]{
				gender,
				hairColour,
				torsoColour,
				legColour,
				feetColour,
				skinColour,
				head,
				chest,
				arms,
				hands,
				legs,
				feet,
				beard
		};
	}


	public List<Integer> getChangedSlots() {
		LinkedList<Integer> list = new LinkedList<Integer>();
		int[] look = getLook();
		for(int i = 0; i < look.length; i++) {
			if(look[i] != previousLook[i])
				list.add(i);
		}
		return list;
	}

	/**
	 * Sets the look array in non-char design.
	 *
	 * @param look The look array.
	 * @throws IllegalArgumentException if the array length is not 13.
	 */
	public void setLook(int[] look) {
		if(look.length != 13) {
			throw new IllegalArgumentException("Array length must be 13.");
		}
		gender = look[0];
		hairColour = look[1];
		torsoColour = look[2];
		legColour = look[3];
		feetColour = look[4];
		skinColour = look[5];
		head = look[6];
		chest = look[7];
		arms = look[8];
		hands = look[9];
		legs = look[10];
		feet = look[11];
		beard = look[12];
		previousLook = getLook();
	}

	/**
	 * Sets look in char design.
	 *
	 * @param look
	 */
	public void setLook2(int[] look) {
		if(look.length != 13) {
			throw new IllegalArgumentException("Array length must be 13.");
		}
		gender = look[0];
		head = look[1];
		beard = look[2];
		chest = look[3];
		arms = look[4];
		hands = look[5];
		legs = look[6];
		feet = look[7];
		hairColour = look[8];
		torsoColour = look[9];
		legColour = look[10];
		feetColour = look[11];
		skinColour = look[12];

	}

	public void setAnimations(int a, int b, int c) {
		standAnim = a;
		walkAnim = b;
		runAnim = c;
	}

	public void setWalkAnim(int i) {
		walkAnim = i;
	}

	/**
	 * Gets the hair colour.
	 *
	 * @return The hair colour.
	 */
	public int getHairColour() {
		return hairColour;
	}

	/**
	 * Gets the torso colour.
	 *
	 * @return The torso colour.
	 */
	public int getTorsoColour() {
		return torsoColour;
	}

	/**
	 * Gets the leg colour.
	 *
	 * @return The leg colour.
	 */
	public int getLegColour() {
		return legColour;
	}

	/**
	 * Gets the feet colour.
	 *
	 * @return The feet colour.
	 */
	public int getFeetColour() {
		return feetColour;
	}

	/**
	 * Gets the skin colour.
	 *
	 * @return The skin colour.
	 */
	public int getSkinColour() {
		return skinColour;
	}

	/**
	 * Gets the gender.
	 *
	 * @return The gender.
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * Gets the chest model.
	 *
	 * @return The chest model.
	 */
	public int getChest() {
		return chest;
	}

	/**
	 * Gets the arms model.
	 *
	 * @return The arms model.
	 */
	public int getArms() {
		return arms;
	}

	/**
	 * Gets the head model.
	 *
	 * @return The head model.
	 */
	public int getHead() {
		return head;
	}

	/**
	 * Gets the hands model.
	 *
	 * @return The hands model.
	 */
	public int getHands() {
		return hands;
	}

	/**
	 * Gets the legs model.
	 *
	 * @return The legs model.
	 */
	public int getLegs() {
		return legs;
	}

	/**
	 * Gets the feet model.
	 *
	 * @return The feet model.
	 */
	public int getFeet() {
		return feet;
	}

	/**
	 * Gets the beard model.
	 *
	 * @return The beard model.
	 */
	public int getBeard() {
		return beard;
	}

	/**
	 * Gets the standing animation.
	 *
	 * @return The standing animation.
	 */
	public int getStandAnim() {
		return standAnim;
	}

	/**
	 * Gets the runing animation.
	 *
	 * @return The runing animation.
	 */
	public int getRunAnim() {
		return runAnim;
	}

	/**
	 * Gets the walking animation.
	 *
	 * @return The walking animation.
	 */
	public int getWalkAnim() {
		return walkAnim;
	}

}
