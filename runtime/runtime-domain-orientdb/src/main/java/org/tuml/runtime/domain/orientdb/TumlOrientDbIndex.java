package org.tuml.runtime.domain.orientdb;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Index;
import org.apache.commons.lang.NotImplementedException;
import org.tuml.runtime.adaptor.TumlTinkerIndex;

/**
 * Date: 2013/01/07
 * Time: 7:57 PM
 */
public class TumlOrientDbIndex<T extends Element> implements TumlTinkerIndex<T> {

    private Index<T> index;

    public TumlOrientDbIndex(Index<T> index) {
        if (index == null) {
            throw new IllegalArgumentException("Index can not be null");
        }
        this.index = index;
    }

    @Override
    public CloseableIterable<T> queryList(Float from, boolean minInclusive, boolean reversed) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public T getEdgeToLastElementInSequence() {
//        CloseableIterable<T> iter = this.index.query("index", QueryContext.numericRange("index", 0F, null).sortNumeric("index", true));
//        for (T t : iter) {
//            return t;
//        }
        return null;
    }

    @Override
    public String getIndexName() {
        return this.index.getIndexName();
    }

    @Override
    public Class<T> getIndexClass() {
        return this.index.getIndexClass();
    }

    @Override
    public void put(String key, Object value, T element) {
        this.index.put(key, value, element);
    }

    @Override
    public CloseableIterable<T> get(String key, Object value) {
        return this.index.get(key, value);
    }

    @Override
    public CloseableIterable<T> query(String key, Object query) {
        throw new NotImplementedException();
    }

    @Override
    public long count(String key, Object value) {
        return this.index.count(key, value);
    }

    @Override
    public void remove(String key, Object value, T element) {
        this.index.remove(key, value, element);
    }
}
