package org.umlg.runtime.adaptor;

import java.util.Map;

/**
 * Date: 2014/06/14
 * Time: 9:01 AM
 */
public class UmlgParameter<K, V> implements Map.Entry<K, V> {

    private final K key;
    private V value;

    public UmlgParameter(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public V setValue(V value) {
        this.value = value;
        return value;
    }

    public boolean equals(Object object) {
        if (object.getClass().equals(UmlgParameter.class)) {
            final Object otherKey = ((UmlgParameter) object).getKey();
            final Object otherValue = ((UmlgParameter) object).getValue();
            if (otherKey == null) {
                if (key != null)
                    return false;
            } else {
                if (!otherKey.equals(key))
                    return false;
            }

            if (otherValue == null) {
                if (value != null)
                    return false;
            } else {
                if (!otherValue.equals(value))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    public String toString() {
        return "parameter[" + key + "," + value + "]";
    }
}