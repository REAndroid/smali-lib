package org.jf.util.collection;

import java.util.*;

public class ArraySet<T> implements Set<T>, Comparator<T>{
    private final ArrayList<T> items;
    private Comparator<? super T> comparator;
    public ArraySet(){
        this.items = new ArrayList<>();
    }
    public ArraySet(int initialCapacity){
        this.items = new ArrayList<>(initialCapacity);
    }
    public ArraySet(Collection<? extends T> collection){
        this.items = new ArrayList<>(collection);
    }

    public Comparator<? super T> comparator() {
        return comparator;
    }
    public void setComparator(Comparator<? super T> comparator) {
        this.comparator = comparator;
        if(comparator != null){
            sort(comparator);
        }
    }

    public void trimToSize() {
        items.trimToSize();
    }
    public boolean addAll(Iterable<? extends T> iterable) {
        if(iterable == null){
            return false;
        }
        if(iterable instanceof Collection){
            return addAll((Collection<? extends T>)iterable);
        }
        return addAll(iterable.iterator());
    }
    public boolean addAll(Iterator<? extends T> iterator) {
        if(iterator == null || !iterator.hasNext()){
            return false;
        }
        while (iterator.hasNext()){
            items.add(iterator.next());
        }
        trimToSize();
        return true;
    }
    public boolean addAll(T... elements) {
        if(elements == null || elements.length == 0){
            return false;
        }
        boolean added = false;
        for(T item : elements){
            if(item == null){
                continue;
            }
            added = items.add(item) || added;
        }
        if(added){
            trimToSize();
        }
        return added;
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
        if(collection == null){
            return false;
        }
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
    @Override
    public int compare(T t1, T t2) {
        if(t1 instanceof Comparable && t2 instanceof Comparable){
            Comparable comparable1 = (Comparable) t1;
            return comparable1.compareTo(t2);
        }
        return 0;
    }
    public ArraySet<T> sort(){
        return sort(this);
    }
    public ArraySet<T> sort(Comparator<? super T> comparator){
        items.sort(comparator);
        return this;
    }

    public static <E> ArraySet<E> sortedCopy(Iterator<? extends E> elements) {
        ArraySet<E> arraySet = copyOf(elements);
        arraySet.sort();
        return arraySet;
    }
    public static <E> ArraySet<E> copyOf(Iterable<? extends E> elements) {
        if (elements == null) {
            return EmptySet.of();
        }
        ArraySet<E> arraySet = new ArraySet<>();
        arraySet.addAll(elements);
        return arraySet;
    }
    public static <E> ArraySet<E> copyOf(Collection<? extends E> elements) {
        if (elements == null) {
            return EmptySet.of();
        }
        ArraySet<E> arraySet = new ArraySet<>();
        arraySet.addAll(elements);
        return arraySet;
    }
    public static <E> ArraySet<E> copyOf(Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return EmptySet.of();
        }
        ArraySet<E> arraySet = new ArraySet<>();
        arraySet.addAll(elements);
        return arraySet;
    }
    public static <E> ArraySet<E> copyOfElements(E... elements) {
        if (elements == null || elements.length == 0) {
            return EmptySet.of();
        }
        ArraySet<E> arraySet = new ArraySet<>();
        arraySet.addAll(elements);
        return arraySet;
    }
    public static <E> ArraySet<E> of() {
        return EmptySet.of();
    }
    public static <E> ArraySet<E> of(E... elements) {
        if (elements == null || elements.length == 0) {
            return EmptySet.of();
        }
        ArraySet<E> arraySet = new ArraySet<>();
        arraySet.addAll(elements);
        return arraySet;
    }

}
