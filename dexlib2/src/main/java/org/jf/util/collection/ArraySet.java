package org.jf.util.collection;

import java.util.*;

public class ArraySet<T> implements Set<T> {
    private final ArrayList<T> items;
    public ArraySet(){
        this.items = new ArrayList<>();
    }
    public ArraySet(int initialCapacity){
        this.items = new ArrayList<>(initialCapacity);
    }
    public ArraySet(Collection<? extends T> collection){
        this.items = new ArrayList<>(collection);
    }
    public void trimToSize() {
        items.trimToSize();
    }
    public boolean addAll(Iterator<? extends T> iterator) {
        if(!iterator.hasNext()){
            return false;
        }
        while (iterator.hasNext()){
            items.add(iterator.next());
        }
        trimToSize();
        return true;
    }
    @Override
    public int size() {
        return items.size();
    }
    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }
    @Override
    public boolean contains(Object o) {
        return items.contains(o);
    }
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }
    @Override
    public Object[] toArray() {
        return items.toArray();
    }
    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return items.toArray(t1s);
    }
    @Override
    public boolean add(T t) {
        return items.add(t);
    }
    @Override
    public boolean remove(Object o) {
        return items.remove(o);
    }
    @Override
    public boolean containsAll(Collection<?> collection) {
        return items.containsAll(collection);
    }
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean result = items.addAll(collection);
        if(result){
            trimToSize();
        }
        return result;
    }
    @Override
    public boolean retainAll(Collection<?> collection) {
        return items.retainAll(collection);
    }
    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean result = items.removeAll(collection);
        trimToSize();
        return result;
    }
    @Override
    public void clear() {
        items.clear();
        trimToSize();
    }

    public static <E> Set<E> copyOf(Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return EmptySet.of();
        }
        ArraySet<E> arraySet = new ArraySet<>();
        arraySet.addAll(elements);
        return arraySet;
    }
}
