package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Entity;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of an iterator for an entity list.
 *
 * @param <E> The type of entity.
 * @author Graham Edgecombe
 */
public class EntityListIterator<E extends Entity> implements Iterator<E> {

	/**
	 * The entities.
	 */
	private Entity[] entities;

	/**
	 * The entity list.
	 */
	private EntityList<E> entityList;

	/**
	 * The previous index.
	 */
	private int lastIndex = - 1;

	/**
	 * The current index.
	 */
	private int cursor = 0;

	/**
	 * The size of the list.
	 */
	private int size;

	/**
	 * Creates an entity list iterator.
	 *
	 * @param entityList The entity list.
	 */
	public EntityListIterator(EntityList<E> entityList) {
		this.entityList = entityList;

		entities = entityList.toArray(new Entity[0]);
		size = entities.length;
	}

	@Override
	public boolean hasNext() {
		return cursor < size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		if(! hasNext()) {
			throw new NoSuchElementException();
		}
		lastIndex = cursor++;
		if(entities[lastIndex] == null) {
			System.out.println("Null is next() ");
		}
		return (E) entities[lastIndex];
	}

	@Override
	public void remove() {
		if(lastIndex == - 1) {
			throw new IllegalStateException();
		}
		entityList.remove(entities[lastIndex]);
	}

}
