package org.umlg.runtime.adaptor;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.index.OIndex;
import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientElementIterable;
import com.tinkerpop.blueprints.impls.orient.OrientIndex;
import org.apache.commons.lang.NotImplementedException;

import java.util.Collection;

/**
 * Date: 2013/01/07
 * Time: 7:57 PM
 */
public class TumlOrientDbIndex<T extends OrientElement> implements TumlTinkerIndex<T> {

    private OrientIndex<T> index;
    private OrientBaseGraph orientBaseGraph;

    public TumlOrientDbIndex(OrientBaseGraph orientBaseGraph, Index<T> index) {
        if (index == null) {
            throw new IllegalArgumentException("Index can not be null");
        }
        this.index = (OrientIndex<T>) index;
        this.orientBaseGraph = orientBaseGraph;
    }

    @Override
    public CloseableIterable<T> queryList(Float from, boolean minInclusive, boolean reversed) {
        OIndex<?> underlying = index.getRawIndex();
        Collection<OIdentifiable> records = underlying.getValuesMajor(from, minInclusive);
        return new OrientElementIterable<T>(this.orientBaseGraph, records);
    }

    @Override
    public T getEdgeToLastElementInSequence() {
        //TODO all wrong
        CloseableIterable<T> results = index.get("index", 0);
        //Loop to end
        T t = null;
        while (results.iterator().hasNext()) {
            t = results.iterator().next();
        }
        return t;
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
