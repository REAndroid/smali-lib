package org.jf.util.collection;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T> {
    private final T[] elements;
    private int index;
    private T mNext;

    public ArrayIterator(T[] elements){
        this.elements = elements;
    }

    public void resetIndex(int index){
        if(index == this.index){
            return;
        }
        if(index < 0){
            index = 0;
        }
        this.index = index;
        mNext = null;
    }
    public int length(){
        if(elements != null){
            return elements.length;
        }
        return 0;
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
        T[] elements = this.elements;
        if(mNext == null) {
            while (index < elements.length) {
                T item = elements[index];
                index ++;
                if (item != null) {
                    mNext = item;
                    break;
                }
            }
        }
        return mNext;
    }
}
