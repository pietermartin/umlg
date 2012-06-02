package org.tuml.runtime.domain.neo4j;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.index.lucene.ValueContext;
import org.tuml.runtime.adaptor.NakedTinkerIndex;

import com.tinkerpop.blueprints.pgm.CloseableSequence;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jElement;

public class NakedNeo4jIndex<T extends Neo4jElement, S extends PropertyContainer>
		implements NakedTinkerIndex<T> {
	private Index<T> index;

	public NakedNeo4jIndex(Index<T> index) {
		if (index == null) {
			throw new IllegalArgumentException("Index can not be null");
		}
		this.index = index;
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
	public com.tinkerpop.blueprints.pgm.Index.Type getIndexType() {
		return this.index.getIndexType();
	}

	@Override
	public void put(String key, Object value, T element) {
		if (value instanceof Integer) {
			this.index
					.put(key, new ValueContext(value).indexNumeric(), element);
		} else if (value instanceof Float) {
			this.index
					.put(key, new ValueContext(value).indexNumeric(), element);
		} else {
			this.index.put(key, value, element);
		}
	}

	@Override
	public CloseableSequence<T> get(String key, Object value) {
		return this.index.get(key, value);
	}

	public CloseableSequence<T> query(String key, Object value) {
		throw new NoSuchMethodError();
//		if (key.equals("index") && value == null) {
//			Query q = NumericRangeQuery.newIntRange("index", Integer.MIN_VALUE,
//					Integer.MAX_VALUE, true, true);
//			return this.index.query(key,
//					new QueryContext(q).sortNumeric("index", false));
//		} else if (key.equals("index") && value != null
//				&& value instanceof Integer) {
//			Query q = NumericRangeQuery.newIntRange("index", (Integer) value,
//					(Integer) value, true, true);
//			return this.index.query(key,
//					new QueryContext(q).sortNumeric("index", false));
//		} else {
//			return this.index.query(key, value);
//		}
	}

	@Override
	public CloseableSequence<T> get(Float value) {
		throw new NoSuchMethodError();
//		return this.index.query("index",
//				QueryContext.numericRange("index", value, value));
	}

	@Override
	public CloseableSequence<T> queryList(Float from, boolean minInclusive,
			boolean reversed) {
		throw new NoSuchMethodError();
//		Query q = NumericRangeQuery.newFloatRange("index", from,
//				Float.MAX_VALUE, minInclusive, true);
//		return this.index.query(null,
//				new QueryContext(q).sortNumeric("index", reversed));
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
