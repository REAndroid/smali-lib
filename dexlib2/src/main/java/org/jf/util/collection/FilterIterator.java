package org.jf.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class FilterIterator<E, T extends E> implements Iterator<T>{
    private final Iterator<? extends T> iterator;
    private T mNext;
    private final Predicate<E> filter;
    public FilterIterator(Iterator<? extends T> iterator, Predicate<E> filter){
        this.iterator = iterator;
        this.filter = filter;
    }

    @Override
    public boolean hasNext() {
        return getNext() != null;
    }
    @Override
    public T next() {
        T item = getNext();
        if(item == null){
            throw new NoSuchElementException();
        }
        mNext = null;
        return item;
    }
    private T getNext(){
        if(mNext == null) {
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (testAll(item)) {
                    mNext = item;
                    break;
                }
            }
        }
        return mNext;
    }
    private boolean testAll(T item){
        if(item == null && filter == null){
            return false;
        }
        return filter == null
                || filter.test(item);
    }
}
