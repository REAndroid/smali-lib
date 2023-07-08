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

    public int length(){
        if(elements != null){
            return elements.length;
        }
        return 0;
    }
    @Override
    public boolean hasNext() {
        return index < length();
    }
    @Override
    public T next() {
        T item = elements[index];
        index++;
        return item;
    }
}
