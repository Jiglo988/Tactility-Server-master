package org.hyperion.rs2.model.combat.weapons;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.weapons.impl.*;
import org.hyperion.rs2.model.combat.weapons.impl.Longsword;

/**
 * @author Jack Daniels.
 */
public class Weapon extends Item {

	/**
	 * The default weapon.
	 */
	public static final Weapon DEFAULT_WEAPON = new Weapon(- 1, Constants.MELEE);

    /**
     * Unarmed
     */

    private static final Weapon UNARMED = Weapon.create(-1, Constants.MELEE, DEFAULT_WEAPON.getSpeed(), DEFAULT_WEAPON.isTwohanded(), DEFAULT_WEAPON.isControlled(), new WeaponAnimations(808, 819, 824, 422, 1156));

	/**
	 * The default weapon speed.
	 */
	public static final int DEFAULT_SPEED = 2400;

	/**
	 * The ranged weapon type.
	 */
	public static final int RANGED_TYPE = Constants.RANGE;

	/**
	 * The melee weapon type.
	 */
	public static final int MELEE_TYPE = Constants.MELEE;

	/**
	 * The type of weapon.
	 */
	private final int type;

	/**
	 * The 2h flag.
	 */
	private final boolean twohanded;

	/**
	 * The controlled flag, indicating whether an item can't give only strength exp.
	 */
	private final boolean controlled;

	/**
	 * The weapon speed.
	 */
	private final int speed;

	/**
	 * The weapon animations.
	 */
	private final WeaponAnimations animations;

	/**
	 * Gets the weapon for the specified id if such a weapon
	 * was saved, otherwise the <code>DEFAULT_WEAPON</code> will be returned.
	 *
	 * @param id
	 * @return
	 */
	public static Weapon forId(int id) {
		//System.out.println("Getting weapon id: " + id);
		Weapon weapon = WeaponManager.getManager().get(id);
		//System.out.println("Got weapon: " + weapon);
		if(weapon != null)
			return weapon;
		return DEFAULT_WEAPON;
	}

	/**
	 * Creates a Weapon for the specified name and id, if this name and id indicate a
	 * weapon type, otherwise a null object wll be returned.
	 *
	 * @param name
	 * @param id
	 * @return
	 */
	public static Weapon getWeapon(String name, int id) {
		name = name.toLowerCase();
		if(id == 16337)
			return new Bow(id, 1200);
		if(id == 16887)
			return new Bow(id, 900);
        if(id == 6603)
            return new StaffOfTheDead();
        //if (name.contains("maul") && !name.contains("granite"))
        //    return new DungeoneeringMaul(id);
        if(id == -1 || name.startsWith("unarmed"))
            return UNARMED;
		if(name.contains("ahrim") && name.contains("staff"))
			return new AhrimsStaff(id);
		if(name.contains("dharok") && name.contains("greataxe"))
			return new GreatAxe(id);
		if(name.contains("zamorakian"))
			return new ZSpear(id);
		else if(name.contains("spear"))
			return new GuthansSpear(id);
		if(name.contains("karil") && name.contains("bow"))
			return new KarilsBow(id);
		if(name.contains("verac") && name.contains("flail"))
			return new Flail(id);
		if(name.contains("torag") && name.contains("hammer"))
			return new ToragsHammers(id);
		if(name.contains("anchor"))
			return new Anchor(id);
		if(name.contains("longsword"))
			return new Longsword(id);
		if(name.contains("battleaxe"))
			return new BattleAxe(id);
		if(name.contains("shortbow"))
			return new Bow(id, Bow.WEAPON_SHORTBOW_SPEED);
		if(name.contains("longbow"))
			return new Bow(id, Bow.WEAPON_LONGBOW_SPEED);
		if(name.contains("bow") && name.contains("dark"))
			return new Bow(id, Bow.WEAPON_DARKBOW_SPEED);
		if(name.contains("crossbow") || name.contains("c'bow"))
			return new Crossbow(id);
		if(name.contains("dragon") || id == 5698) {
			if(name.contains("dagger"))
				return new DragonDagger(id);
		}
		if(name.contains("godsword") || name.contains("2h"))
			return new TwohandedSword(id);
		if(name.contains("halberd"))
			return new Halberd(id);
		if(name.equals("hand cannon"))
			return new HandCannon(id);
		if(name.contains("javelin"))
			return new Javelin(id);
		if(name.contains("granite") && name.contains("maul"))
			return new GraniteMaul(id);
		if(name.contains("maul") || name.contains("tzhaar-ket-om"))
			return new Maul(id);
		if(name.contains("rapier"))
			return new Rapier(id);
		if(name.contains("sled"))
			return new Sled(id);
		if(name.contains("chaotic staff") || name.contains("catalytic staff"))
			new SmashStaff(id);
		else if(name.contains("staff"))
			return new Staff(id);
		if(name.contains("throw") && name.contains("axe"))
			return new ThrowingAxe(id);
		if(name.contains("whip"))
			return new Whip(id);
		if(name.contains("dagger") && !name.contains("dragon"))
			return new Dagger(id);
		return null;
	}

	/**
	 * Constructs a new weapon with the specified item id.
	 *
	 * @param id
	 */
	public Weapon(int id, int type) {
		this(id, type, DEFAULT_SPEED);
	}

	/**
	 * Constructs a new Weapon with the default weapon animations.
	 *
	 * @param id
	 * @param type
	 * @param speed
	 */
	public Weapon(int id, int type, int speed) {
		this(id, type, speed, false, false, WeaponAnimations.DEFAULT_ANIMS);
	}

	/**
	 * Constructs a new Weapon.
	 *
	 * @param id
	 * @param type
	 * @param speed
	 * @param twohanded
	 * @param controlled
	 * @param anims
	 */
	public Weapon(int id, int type, int speed, boolean twohanded, boolean controlled, WeaponAnimations anims) {
		super(id);
		this.type = type;
		this.speed = speed;
		this.twohanded = twohanded;
		this.controlled = controlled;
		this.animations = anims;
	}
	
	public static Weapon create(int id, int type, int speed, boolean twohanded, boolean controlled, WeaponAnimations anims) {
		return new Weapon(id, type, speed, twohanded, controlled, anims);
	}

	/**
	 * @return true if the item is 2handed, false it not.
	 */
	public boolean isTwohanded() {
		return twohanded;
	}

	/**
	 * @return True is the weapon is controlled, can't give only strength exp, false if not.
	 */
	public boolean isControlled() {
		return controlled;
	}

	/**
	 * @return the type of weapon, Melee or Ranged.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the weapon speed.
	 */
	public int getSpeed() {
		return speed;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: " + this.getId() + " ");
		if(this instanceof Crossbow)
			sb.append(" is crossbow ");
		return sb.toString();
	}

	public int getWalkAnimation(Player player) {
		return animations.getWalkAnimation();
	}


	public int getStandAnimation(Player player) {
		return animations.getStandAnimation();
	}


	public int getRunAnimation(Player player) {
		return animations.getRunAnimation();
	}


	public int getAttackAnimation(Player player) {
		return animations.getAttackAnimation();
	}

	public int getDefendAnimation(Player player) {
		return animations.getDefendAnimation();
	}

	/**
	 * Checks if a player meets the requirements to wield this item.
	 * If the player doesn't meet the requirements, a message explaining why, is returned.
	 * If the player meets the requirements, a null object is returned.
	 *
	 * @param player
	 * @return
	 */
	public String meetsRequirements(Player player) {
		return null;
	}
}
