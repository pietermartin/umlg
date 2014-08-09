package org.umlg.runtime.collection.persistent;

import com.google.common.base.Preconditions;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgQualifiedBag;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

import java.util.List;
import java.util.Set;

public class UmlgQualifiedBagImpl<E> extends BaseBag<E> implements UmlgQualifiedBag<E> {

	public UmlgQualifiedBagImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
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
				Set<Edge> edges = UMLG.get().getEdgesBetween(this.vertex, v, this.getLabel());
				for (Edge edge : edges) {
                    edge.remove();
				}
			} else if (o.getClass().isEnum()) {
                List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + o.toString());
                Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
                v = vertexes.get(0);
                v.remove();
			} else {
                List<Vertex> vertexes = this.internalVertexMap.get(getPersistentName() + o.toString());
                Preconditions.checkState(vertexes.size() > 0, "BaseCollection.internalVertexMap must have a value for the key!");
                v = vertexes.get(0);
                v.remove();
			}
		}
		return result;
	}

}