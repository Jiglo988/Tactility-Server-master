package org.hyperion.rs2.model.newcombat;

/**
 * Represents a single item.
 *
 * @author Graham Edgecombe
 */
public class Item {

	/**
	 * The id.
	 */
	private int id;

	/**
	 * The number of items.
	 */
	private int count;

	/**
	 * Creates a single item.
	 *
	 * @param id The id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	/**
	 * Creates a stacked item.
	 *
	 * @param id    The id.
	 * @param count The number of items.
	 * @throws IllegalArgumentException if count is negative.
	 */
	public Item(int id, int count) {
		if(count < 0) {
			System.out.println("Count is " + count);
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = id;
		this.count = count;
	}

	/**
	 * Gets the item id.
	 *
	 * @return The item id.
	 */
	public int getId() {
		return id;
	}

	public void setId(int ID) {
		id = ID;
	}

	/**
	 * Gets the count.
	 *
	 * @return The count.
	 */
	public int getCount() {
		return count;
	}

	public void setCount(int c) {
		count = c;
	}

	@Override
	public boolean equals(Object object) {
		Item item;
		if(object instanceof Item)
			item = (Item) object;
		else
			return false;
		if(item != null)
			if(id == item.id)
				return true;
		return false;
	}

	@Override
	public String toString() {
		return Item.class.getName() + " [id=" + id + ", count=" + count + "]";
	}

}
