package org.hyperion.rs2.model;

/**
 * Represents a single game object.
 *
 * @author Graham Edgecombe
 */
public class GameObject {

	/**
	 * The location.
	 */
	private Position position;

	/**
	 * The definition.
	 */
	private GameObjectDefinition definition;

	/**
	 * The type.
	 */
	private int type;

	/**
	 * The rotation.
	 */
	private int rotation;

    public final boolean onAllHeights;

    public GameObject(GameObjectDefinition definition, Position position, int type, int rotation) {
        this(definition, position, type, rotation, true);
    }

	/**
	 * Creates the game object.
	 *
	 * @param definition The definition.
	 * @param position   The location.
	 * @param type       The type.
	 * @param rotation   The rotation.
	 */
	public GameObject(GameObjectDefinition definition, Position position, int type, int rotation, boolean onAllHeights) {
		this.definition = definition;
		this.position = position;
		this.type = type;
		this.rotation = rotation;
        this.onAllHeights = onAllHeights;
    }
	/**
	 * Gets the location.
	 *
	 * @return The location.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the definition.
	 *
	 * @return The definition.
	 */
	public GameObjectDefinition getDefinition() {
		return definition;
	}

	/**
	 * Gets the type.
	 *
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the rotation.
	 *
	 * @return The rotation.
	 */
	public int getRotation() {
		return rotation;
	}


    /**
     * Chec if the object is at
     * @param loc
     * @return
     */
    public boolean isAt(Position loc) {
        if(!onAllHeights)
            return this.position.equals(loc);
        else
            return position.equalsIgnoreHeight(loc);
    }

    /**
     * Check if the object is visible from the other location
     * @param loc
     * @return
     */

    public boolean isVisible(Position loc) {
		return (onAllHeights ? position.distance(loc) < 64 && loc.getZ()%4 == position.getZ()%4 : loc.isWithinDistance(position, 64));
    }

}
