package org.jf.util.collection;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConcatIterators<T> implements Iterator<T> {
    private final Iterator<? extends Iterator<? extends T>> iterators;
    private final Iterator<? extends Iterable<? extends T>> iterables;

    private Iterator<? extends T> mCurrentIterator;
    private T mNext;
    public ConcatIterators(Iterator<? extends Iterator<? extends T>> iterators){
        this.iterators = iterators;
        this.iterables = null;
    }
    public ConcatIterators(Iterator<? extends Iterable<? extends T>> iterables, boolean nothing){
        this.iterators = null;
        this.iterables = iterables;
    }
    @Override
    public boolean hasNext() {
        return getNext() != null;
    }

    @Override
    public T next() {
        T next = getNext();
        if(next == null){
            throw new NoSuchElementException();
        }
        mNext = null;
        return next;
    }
    private T getNext(){
        T next = mNext;
        if(next != null){
            return next;
        }
        Iterator<? extends T> current = getCurrentIterator();
        if(current == null){
            return null;
        }
        while (next == null && current != null){
            next = current.next();
            current = getCurrentIterator();
        }
        mNext = next;
        return next;
    }
    private Iterator<? extends T> getCurrent() {
        if(iterators != null){
            return getCurrentIterator();
        }
        return getCurrentIterables();
    }
    private Iterator<? extends T> getCurrentIterator() {
        Iterator<? extends T> current = mCurrentIterator;
        if(current != null && current.hasNext()){
            return current;
        }
        mCurrentIterator = null;
        if(iterators == null || !iterators.hasNext()){
            return null;
        }
        current = null;
        while (current == null && iterators.hasNext()){
            current = iterators.next();
            if(current != null && !current.hasNext()){
                current = null;
            }
        }
        mCurrentIterator = current;
        return current;
    }
    private Iterator<? extends T> getCurrentIterables() {
        Iterator<? extends T> current = mCurrentIterator;
        if(current != null && current.hasNext()){
            return current;
        }
        mCurrentIterator = null;
        if(iterables == null || !iterables.hasNext()){
            return null;
        }
        current = null;
        while (current == null && iterables.hasNext()){
            Iterable<? extends T> itr = iterables.next();
            if(itr != null){
                current = itr.iterator();
            }
            if(current != null && !current.hasNext()){
                current = null;
            }
        }
        mCurrentIterator = current;
        return current;
    }
}
