package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibCollection;

import java.util.Collection;
import java.util.Comparator;

public interface UmlgCollection<E> extends Collection<E>, OclStdLibCollection<E> {
    static final String ASSOCIATION_CLASS_VERTEX_ID = "associationClassVertexId";
    static final String ASSOCIATION_CLASS_EDGE_ID = "associationClassEdgeId";

//    default String toJson() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        int count = 0;
//        for (E e : this) {
//            count++;
//            if (e instanceof String) {
//                sb.append("\"");
//                sb.append(e);
//                sb.append("\"");
//            } else if (e instanceof UmlgEnum) {
//                sb.append("\"");
//                sb.append(e);
//                sb.append("\"");
//            } else {
//                sb.append(e);
//            }
//            if (count < size()) {
//                sb.append(",");
//            }
//        }
//        sb.append("]");
//        return sb.toString();
//    }

    boolean inverseAdder(E e);

    public default boolean addIgnoreInverse(E e) {
        throw new IllegalStateException("addIgnoreInverse() is only supported by a persistent collection BaseCollection!");
    }

    UmlgCollection<E> sortedBy(Comparator<E> e);
}
