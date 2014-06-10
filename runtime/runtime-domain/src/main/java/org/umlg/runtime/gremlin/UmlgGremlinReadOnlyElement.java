package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.util.ElementHelper;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

import java.util.Set;

/**
 * Date: 2014/03/21
 * Time: 3:04 PM
 */
public class UmlgGremlinReadOnlyElement implements Element {

    protected final Element baseElement;

    protected UmlgGremlinReadOnlyElement(final Element baseElement) {
        this.baseElement = baseElement;
    }

    public Set<String> getPropertyKeys() {
        return this.baseElement.getPropertyKeys();
    }

    public Object getId() {
        return this.baseElement.getId();
    }

    /**
     * @throws UnsupportedOperationException
     */
    public Object removeProperty(final String key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    /**
     * The key param can be either the proper fully qualified name as saved on the element
     * or such that the last part of the fully qualified key matches '::' + key
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getProperty(final String key) {
        //Turn key back into umlg namespacing
        String properKey = findMatchingKey(key);
        if (properKey != null) {
            return this.baseElement.getProperty(properKey);
        } else {
            return null;
        }
    }

    private String findMatchingKey(String key) {
        for (String properKey : this.baseElement.getPropertyKeys()) {
            if (properKey.equals(key) || properKey.endsWith("_" + key)) {
                return properKey;
            }
        }
        return null;
    }

    /**
     * @throws UnsupportedOperationException
     */
    public void setProperty(final String key, final Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public String toString() {
        return this.baseElement.toString();
    }

    public int hashCode() {
        return this.baseElement.hashCode();
    }

    public void remove() {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public boolean equals(final Object object) {
        return ElementHelper.areEqual(this, object);
    }

}
