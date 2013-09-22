package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.TinkerQualifiedBag;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Set;

public class TinkerQualifiedBagImpl<E> extends BaseBag<E> implements TinkerQualifiedBag<E> {

	public TinkerQualifiedBagImpl(UmlgNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
//		this.index = GraphDb.getDb().getIndex(getQualifiedName(), Edge.class);
	}

	@Override
	public boolean remove(Object o) {
		if (!this.loaded) {
			// this.loaded = true;
			loadFromVertex();
		}
		boolean result = this.getInternalBag().remove(o);
		if (result) {
			Vertex v;
			if (o instanceof UmlgNode) {
				UmlgNode node = (UmlgNode) o;
				v = node.getVertex();
				Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
				for (Edge edge : edges) {
//					removeEdgefromIndex(edge);
					GraphDb.getDb().removeEdge(edge);
				}
			} else if (o.getClass().isEnum()) {
				v = this.internalVertexMap.get(((Enum<?>) o).name());
				Edge edge = v.getEdges(Direction.IN, this.getLabel()).iterator().next();
//				removeEdgefromIndex(edge);
				GraphDb.getDb().removeVertex(v);
			} else {
				v = this.internalVertexMap.get(o);
				Edge edge = v.getEdges(Direction.IN, this.getLabel()).iterator().next();
//				removeEdgefromIndex(edge);
				GraphDb.getDb().removeVertex(v);
			}
		}
		return result;
	}

//	private void removeEdgefromIndex(Edge edge) {
//		for (String key : edge.getPropertyKeys()) {
//			if (key.startsWith("index")) {
//				this.index.remove(key, edge.getProperty(key), edge);
//			}
//		}
//	}

//	protected void validateQualifiedMultiplicity(List<Qualifier> qualifiers) {
//		for (Qualifier qualifier : qualifiers) {
//			if (qualifier.isOne()) {
//				long count = this.index.count(qualifier.getKey(), qualifier.getValue());
//				if (count > 0) {
//					// Add info to exception
//					throw new IllegalStateException("qualifier fails, entry for qualifier already exist");
//				}
//			}
//		}
//	}

}
