package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.TumlMetaNode;
import org.umlg.runtime.domain.UmlgNode;

import java.lang.reflect.Method;
import java.util.Iterator;

public class TinkerBagClosableIterableImpl<E> extends BaseBag<E> implements TinkerBag<E> {

	private Iterator<Edge> closeableIterator;

	public TinkerBagClosableIterableImpl(Iterator<Edge> closeableIterator, TumlRuntimeProperty tumlRuntimeProperty) {
		super(tumlRuntimeProperty);
		this.closeableIterator = closeableIterator;
	}

	@Override
	protected Iterator<Edge> getEdges() {
		return this.closeableIterator;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadFromVertex() {
		for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
			Edge edge = iter.next();
			E node = null;
			try {
				Class<?> c = this.getClassToInstantiate(edge);
				if (c.isEnum()) {
					Object value = this.getVertexForDirection(edge).getProperty(getQualifiedName());
					node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                    putToInternalMap(node, this.getVertexForDirection(edge));
                } else if (TumlMetaNode.class.isAssignableFrom(c)) {
                    Method m = c.getDeclaredMethod("getInstance", new Class[0]);
                    node = (E) m.invoke(null);
				} else if (UmlgNode.class.isAssignableFrom(c)) {
					node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
				} else {
					Object value = this.getVertexForDirection(edge).getProperty(getQualifiedName());
					node = (E) value;
                    putToInternalMap(value, this.getVertexForDirection(edge));
				}
				this.internalCollection.add(node);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

}
