package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibCollection;
import org.umlg.runtime.domain.UmlgEnum;

import java.util.Collection;

public interface UmlgCollection<E> extends Collection<E>, OclStdLibCollection<E> {
    public static final String ASSOCIATION_CLASS_VERTEX_ID = "associationClassVertexId";
    public static final String ASSOCIATION_CLASS_EDGE_ID = "associationClassEdgeId";

    public default String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int count = 0;
        for (E e : this) {
            count++;
            if (e instanceof String) {
                sb.append("\"");
                sb.append(e);
                sb.append("\"");
            } else if (e instanceof UmlgEnum) {
                sb.append("\"");
                sb.append(e);
                sb.append("\"");
            } else {
                sb.append(e);
            }
            if (count < size()) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    boolean inverseAdder(E e);

    public default boolean addIgnoreInverse(E e) {
        throw new IllegalStateException("addIgnoreInverse() is only supported by a persistent collection BaseCollection!");
    }

}
