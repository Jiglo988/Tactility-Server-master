package org.hyperion.rs2.model.container;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Item;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A container holds a group of items.
 *
 * @author Graham Edgecombe
 */
public class Container {

	/**
	 * The type of container.
	 *
	 * @author Graham Edgecombe
	 */
	public enum Type {

		/**
		 * A standard container such as inventory.
		 */
		STANDARD,

		/**
		 * A container which always stacks, e.g. the bank, regardless of the
		 * item.
		 */
		ALWAYS_STACK,

	}

	/**
	 * The capacity of this container.
	 */
	protected int capacity;

	/**
	 * The items in this container.
	 */
	protected Item[] items;

	/**
	 * The items that were initially in this container.
	 */
	private Item[] previousItems;

	/**
	 * The flag indicating whether this container has finished loading.
	 */
	private boolean loaded = false;

	/**
	 * A list of listeners.
	 */
	private List<ContainerListener> listeners = new LinkedList<ContainerListener>();

	/**
	 * The container type.
	 */
	private Type type;

    public Type getType() {
        return type;
    }

	/**
	 * Firing events flag.
	 */
	private boolean firingEvents = true;

    public Item[] getItems() {
        return items;
    }

	public boolean ignoreOnce = false;

	/**
	 * Creates the container with the specified capacity.
	 *
	 * @param type     The type of this container.
	 * @param capacity The capacity of this container.
	 */
	public Container(Type type, int capacity) {
		this.type = type;
		this.capacity = capacity;
        this.items = new Item[capacity];
        this.previousItems = new Item[capacity];
	}

	/**
	 * @param loaded
	 */
	public void setLoaded(boolean loaded) {
		if(loaded) {
			if(this.loaded) {
				System.out.println("Was already loaded!");
			} else {
				this.loaded = true;
				updatePreviousItems();
			}
		} else {
			System.out.println("Invalid input");
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Gets an array of
	 *
	 * @return
	 */
	public LinkedList<Integer> getChangedSlots() {
		LinkedList<Integer> changedSlots = new LinkedList<Integer>();
		for(int i = 0; i < capacity; i++) {
			Item item = items[i];
			Item previousItem = previousItems[i];
			if(item != null) {
				if(previousItem != null) {
					if(item.getId() != previousItem.getId() || item.getCount() != previousItem.getCount()) {
						changedSlots.add(i);
					}
				} else {
					changedSlots.add(i);
				}
			} else {
				if(previousItem != null) {
					changedSlots.add(i);
				}
			}
		}

		return changedSlots;
	}

	public void updatePreviousItems() {
		for(int i = 0; i < items.length; i++) {
			Item item = items[i];
			if(item != null)
				previousItems[i] = new Item(item.getId(), item.getCount());
			else
				previousItems[i] = null;
		}
	}

	/**
	 * Sets the firing events flag.
	 *
	 * @param firingEvents The flag.
	 */
	public void setFiringEvents(boolean firingEvents) {
		this.firingEvents = firingEvents;
	}

	/**
	 * Checks the firing events flag.
	 *
	 * @return <code>true</code> if events are fired, <code>false</code> if
	 * not.
	 */
	public boolean isFiringEvents() {
		return firingEvents;
	}

	/**
	 * Gets the listeners of this container.
	 *
	 * @return The listeners of this container.
	 */
	public Collection<ContainerListener> getListeners() {
		return Collections.unmodifiableCollection(listeners);
	}

	/**
	 * Adds a listener.
	 *
	 * @param listener The listener to add.
	 */
	public void addListener(ContainerListener listener) {
		listeners.add(listener);
		listener.itemsChanged(this);
	}

	/**
	 * Removes a listener.
	 *
	 * @param listener The listener to remove.
	 */
	public void removeListener(ContainerListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes all listeners.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}

	/**
	 * Shifts all items to the top left of the container leaving no gaps.
	 */
	public void shift() {
		Item[] old = items;
		items = new Item[capacity];
		int newIndex = 0;
		for(int i = 0; i < items.length; i++) {
			if(old[i] != null) {
				items[newIndex] = old[i];
				newIndex++;
			}
		}
		if(firingEvents) {
			fireItemsChanged();
		}
	}

	/**
	 * Gets the next free slot.
	 *
	 * @return The slot, or <code>-1</code> if there are no available slots.
	 */
	public int freeSlot() {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				return i;
			}
		}
		return - 1;
	}

	/**
	 * Attempts to add an item into the next free slot.
	 *
	 * @param item The item.
	 * @return <code>true</code> if the item was added,
	 * <code>false</code> if not.
	 */
    public boolean add(Item item) {
        if(item == null)
            return false;
        if(item.getId() < 0)
            return false;
        //System.out.println(item.getDefinition());
        if(item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) {
            for(int i = 0; i < items.length; i++) {
                if(items[i] != null && items[i].getId() == item.getId()) {
                    int totalCount = item.getCount() + items[i].getCount();
                    long fuck_all_count = BigInteger.valueOf(item.getCount()).add(BigInteger.valueOf(items[i].getCount())).longValueExact();
                    if(fuck_all_count >= Constants.MAX_ITEMS || totalCount < 1) {
                        return false;
                    }
                    set(i, new Item(items[i].getId(), items[i].getCount() + item.getCount()));
                    return true;
                }
            }
            int slot = freeSlot();
            if(slot == - 1) {
                return false;
            } else {
                set(slot, item);
                return true;
            }
        } else {
            int slots = freeSlots();
            if(slots >= item.getCount()) {
                boolean b = firingEvents;
                firingEvents = false;
                try {
                    for(int i = 0; i < item.getCount(); i++) {
                        set(freeSlot(), new Item(item.getId()));
                    }
                    if(b) {
                        fireItemsChanged();
                    }
                    return true;
                } finally {
                    firingEvents = b;
                }
            } else {
                return false;
            }
        }
    }

    public boolean add(Item item, int slot) {
		Item i = get(slot);
		if(i == null) {
			set(slot, item);
			fireItemsChanged();
			return true;
		}
		return false;
	}


	/**
	 * Gets the number of free slots.
	 *
	 * @return The number of free slots.
	 */
	public int freeSlots() {
		return capacity - size();
	}

	/**
	 * Gets an item.
	 *
	 * @param index The position in the container.
	 * @return The item.
	 */
	public Item get(int index) {
        if(index >= items.length) {
            return null;
        }
        return items[index];
	}

	/**
	 * Gets the item id of the item at the given index.
	 *
	 * @param index The position in the container.
	 * @return The item id if the item isn't null, -1 otherwise.
	 */
	public int getItemId(int index) {
		Item item = get(index);
		if(item == null)
			return - 1;
		return item.getId();
	}

	/**
	 * Checks if the container contains the given item ids.
	 *
	 * @param ids
	 * @return
	 */
	public boolean contains(int... ids) {
		for(int id : ids) {
			if(! contains(id))
				return false;
		}
		return true;
	}

	/**
	 * Gets an item by id.
	 *
	 * @param id The id.
	 * @return The item, or <code>null</code> if it could not be found.
	 */
	public Item getById(int id) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				continue;
			}
			if(items[i].getId() == id) {
				return items[i];
			}
		}
		return null;
	}

	/**
	 * Gets a slot by id.
	 *
	 * @param id The id.
	 * @return The slot, or <code>-1</code> if it could not be found.
	 */
	public int getSlotById(int id) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				continue;
			}
			if(items[i].getId() == id) {
				return i;
			}
		}
		return - 1;
	}

	/**
	 * Sets an item.
	 *
	 * @param index The position in the container.
	 * @param item  The item.
	 */
	public void set(int index, Item item) {
        if(index < 0) {
            return;
        }
		items[index] = item;
		if(firingEvents) {
			fireItemChanged(index);
		}
	}

	/**
	 * Gets the capacity of this container.
	 *
	 * @return The capacity of this container.
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Gets the size of this container.
	 *
	 * @return The size of this container.
	 */
	public int size() {
		int size = 0;
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null) {
				size++;
			}
		}
		return size;
	}

	/**
	 * Clears this container.
	 */
	public void clear() {
		items = new Item[items.length];
		if(firingEvents) {
			fireItemsChanged();
		}
	}

	/**
	 * Returns an array representing this container.
	 *
	 * @return The array.
	 */
	public Item[] toArray() {
		return items;
	}

    /**
	 * Checks if a slot is used.
	 *
	 * @param slot The slot.
	 * @return <code>true</code> if an item is present, <code>false</code> otherwise.
	 */
	public boolean isSlotUsed(int slot) {
		return items[slot] != null;
	}

	/**
	 * Checks if a slot is free.
	 *
	 * @param slot The slot.
	 * @return <code>true</code> if an item is not present, <code>false</code> otherwise.
	 */
	public boolean isSlotFree(int slot) {
		return items[slot] == null;
	}


	/**
	 * Removes an item.
	 *
	 * @param item The item to remove.
	 * @return The number of items removed.
	 */
	public int remove(Item item) {
        return remove(- 1, item);
	}

	/**
	 * Removes an item.
	 *
	 * @param preferredSlot The preferred slot.
	 * @param item          The item to remove.
	 * @return The number of items removed.
	 */
	public int remove(int preferredSlot, Item item) {
		int removed = 0;
		if(item == null || item.getDefinition() == null) {
			//System.out.println("Container null , PLEASE FIX MARTIN!");
			return removed;
		}
		//if(item.getCount() == 0)
		//	return 0;
		if(item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) {
			int slot = getSlotById(item.getId());
			if(slot == - 1)
				return removed;
			Item stack = get(slot);
			if(stack.getCount() > item.getCount()) {
				removed = item.getCount();
				set(slot, new Item(stack.getId(), stack.getCount() - item.getCount()));
			} else {
				removed = stack.getCount();
				set(slot, null);
			}
		} else {
			for(int i = 0; i < item.getCount(); i++) {
				int slot = getSlotById(item.getId());
				if(slot == - 1)
					continue;
				if(i == 0 && preferredSlot != - 1) {
					Item inSlot = get(preferredSlot);
					if(inSlot.getId() == item.getId()) {
						slot = preferredSlot;
					}
				}
				if(slot != - 1) {
					removed++;
					set(slot, null);
				} else {
					break;
				}
			}
		}
		return removed;
	}

    public void setItems(Item[] items)
    {
        this.items = items;
    }

    /**
	 * Transfers an item from one container to another.
	 *
	 * @param from     The container to transfer from.
	 * @param to       The container to transfer to.
	 * @param fromSlot The slot in the original container.
	 * @param id       The item id.
	 * @return A flag indicating if the transfer was successful.
	 */
	public static boolean transfer(Container from, Container to, int fromSlot, int id) {
		if(fromSlot < 0 || fromSlot > from.capacity())
			return false;
		Item fromItem = from.get(fromSlot);
		if(fromItem == null || fromItem.getId() != id) {
			return false;
		}
		if(to.add(fromItem)) {
			from.set(fromSlot, null);
			return true;
		} else {
			return false;
		}
	}

	public static boolean transfer(Container from, Container to) {
		for(int i = 0; i < from.capacity(); i++) {
			Item fromItem = from.get(i);
			if(fromItem == null)
				continue;
			if(to.add(fromItem)) {
				from.set(i, null);
			} else {
                return false;//if this happens there is something seriously wrong....
			}
		}
		return true;
	}

	/**
	 * Swaps two items.
	 *
	 * @param fromSlot From slot.
	 * @param toSlot   To slot.
	 */
	public void swap(int fromSlot, int toSlot) {
		Item temp = get(fromSlot);
		boolean b = firingEvents;
		firingEvents = false;
		try {
			set(fromSlot, get(toSlot));
			set(toSlot, temp);
			if(b) {
				fireItemsChanged(new int[]{fromSlot, toSlot});
			}
		} finally {
			firingEvents = b;
		}
	}

	/**
	 * Gets the total amount of an item, including the items in stacks.
	 *
	 * @param id The id.
	 * @return The amount.
	 */
	public int getCount(int id) {
		int total = 0;
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null) {
				if(items[i].getId() == id) {
					total += items[i].getCount();
				}
			}
		}
		//System.out.println(total + "");
		return total;
	}

	/**
	 * Inserts an item.
	 *
	 * @param fromSlot The old slot.
	 * @param toSlot   The new slot.
	 */
	public void insert(int fromSlot, int toSlot) {
		// we reset the item in the from slot
		Item from = items[fromSlot];
		if(from == null) {
			return;
		}
		items[fromSlot] = null;
		// find which direction to shift in
		if(fromSlot > toSlot) {
			int shiftFrom = toSlot;
			int shiftTo = fromSlot;
			for(int i = (toSlot + 1); i < fromSlot; i++) {
				if(items[i] == null) {
					shiftTo = i;
					break;
				}
			}
			Item[] slice = new Item[shiftTo - shiftFrom];
			System.arraycopy(items, shiftFrom, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, shiftFrom + 1, slice.length);
		} else {
			int sliceStart = fromSlot + 1;
			int sliceEnd = toSlot;
			for(int i = (sliceEnd - 1); i >= sliceStart; i--) {
				if(items[i] == null) {
					sliceStart = i;
					break;
				}
			}
			Item[] slice = new Item[sliceEnd - sliceStart + 1];
			System.arraycopy(items, sliceStart, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
		}
		// now fill in the target slot
		items[toSlot] = from;
		if(firingEvents) {
			fireItemsChanged();
		}
	}

	/**
	 * Fires an item changed event.
	 *
	 * @param slot The slot that changed.
	 */
	public void fireItemChanged(int slot) {
		for(ContainerListener listener : listeners) {
			listener.itemChanged(this, slot);
		}
	}

	/**
	 * Fires an items changed event.
	 */
	public void fireItemsChanged() {
		for(ContainerListener listener : listeners) {
			listener.itemsChanged(this);
		}
	}

	/**
	 * Fires an items changed event.
	 *
	 * @param slots The slots that changed.
	 */
	public void fireItemsChanged(int[] slots) {
		for(ContainerListener listener : listeners) {
			listener.itemsChanged(this, slots);
		}
	}

	/**
	 * Checks if the container contains the specified item.
	 *
	 * @param id The item id.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean contains(int id) {
        return getSlotById(id) != - 1;
    }

	public boolean hasItem(final Item item) {
		final int amount = getCount(item.getId());
		return (item.getCount() <= 0 && contains(item.getId()))
				|| (contains(item.getId()) && amount >= item.getCount());
	}

	/**
	 * Checks if there is room in the inventory for an item.
	 *
	 * @param item The item.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean hasRoomFor(Item item) {
		if(item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) {
			for(int i = 0; i < items.length; i++) {
				if(items[i] != null && items[i].getId() == item.getId()) {
					int totalCount = item.getCount() + items[i].getCount();
					if(totalCount >= Constants.MAX_ITEMS || totalCount < 1) {
						return false;
					}
					return true;
				}
			}
			int slot = freeSlot();
			return slot != - 1;
		} else {
			int slots = freeSlots();
			return slots >= item.getCount();
		}

	}

}
