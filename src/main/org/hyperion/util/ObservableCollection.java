package org.hyperion.util;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This is a class that uses the
 *
 * Created by Gilles on 19/02/2016.
 */
public class ObservableCollection<T> implements Collection<T>, Observable {

    private final Collection<T> collection;
    private final Set<InvalidationListener> listeners = new HashSet<>();

    public ObservableCollection(Collection<T> collection) {
        this.collection = collection;
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return collection.toArray(a);
    }

    @Override
    public boolean add(T t) {
        if(collection.add(t)) {
            fireChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if(collection.remove(o)) {
            fireChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return collection.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if(collection.addAll(c)) {
            fireChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(collection.removeAll(c)) {
            fireChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if(collection.retainAll(c)) {
            fireChanged();
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public boolean equals(Object o) {
        return collection.equals(o);
    }

    @Override
    public int hashCode() {
        return collection.hashCode();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        if(collection.removeIf(filter)) {
            fireChanged();
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> spliterator() {
        return collection.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return collection.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return collection.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        collection.forEach(action);
    }

    protected void fireChanged(){
        listeners.forEach(l -> l.invalidated(this));
    }
}
