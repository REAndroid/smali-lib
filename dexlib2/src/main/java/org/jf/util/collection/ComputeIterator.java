package org.jf.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ComputeIterator<E, T> implements Iterator<T>{
    private final Iterator<? extends E> iterator;
    private final Function<? super E, ? extends T> function;
    private T mNext;

    public ComputeIterator(Iterator<? extends E> iterator,
                           Function<? super E, ? extends T> function){
        this.iterator = iterator;
        this.function = function;
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
                E input = iterator.next();
                if(!testInput(input)){
                    continue;
                }
                T output = function.apply(input);
                if (testOutput(output)) {
                    mNext = output;
                    break;
                }
            }
        }
        return mNext;
    }
    private boolean testInput(E input){
        return input != null;
    }
    private boolean testOutput(T output){
        return output != null;
    }
}