package org.umlg.runtime.adaptor;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Date: 2013/06/14
 * Time: 8:34 AM
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
            throw new NoSuchElementException();

        return array[count++];
    }

    public boolean hasNext() {
        return count < array.length;
    }
}