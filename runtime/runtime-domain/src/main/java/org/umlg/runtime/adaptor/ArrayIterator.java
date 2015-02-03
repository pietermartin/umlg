package org.umlg.runtime.adaptor;

import com.tinkerpop.gremlin.process.FastNoSuchElementException;

import java.util.Iterator;

/**
 * Date: 2014/07/04
 * Time: 12:07 PM
 */
public class ArrayIterator implements Iterator {
    private final Object[] array;
    private int count = 0;

    public ArrayIterator(final Object[] array) {
        this.array = array;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Object next() {
        if (count > array.length)
            throw FastNoSuchElementException.instance();

        return array[count++];
    }

    public boolean hasNext() {
        return count < array.length;
    }

}