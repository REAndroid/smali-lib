package org.jf.util.collection;



import org.jf.dexlib2.analysis.CustomInlineMethodResolver;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorUtil {

    public static <F, T> Iterator<T> transform(final Iterator<F> fromIterator,
                                               final Function<? super F, ? extends T> function) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return fromIterator.hasNext();
            }
            @Override
            public T next() {
                return function.apply(fromIterator.next());
            }
        };
    }
}
