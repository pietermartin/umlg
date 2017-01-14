package org.umlg.runtime.domain;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.Qualifier;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.domain.ocl.OclAny;
import org.umlg.runtime.validation.UmlgConstraintViolation;

import java.util.List;

public interface UmlgNode extends UmlgEnum, OclAny, PersistentObject {
	Vertex getVertex();
	boolean isTinkerRoot();
	void initialiseProperties();
	void initialiseProperty(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse);
    UmlgRuntimeProperty inverseAdder(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse, UmlgNode umlgNode);
    void initVariables();
	List<Qualifier> getQualifiers(UmlgRuntimeProperty umlgRuntimeProperty, UmlgNode node, boolean inverse);
	void delete();
	int getSize(boolean inverse, UmlgRuntimeProperty umlgRuntimeProperty);
	<E> UmlgSet<E> asSet();
	List<UmlgConstraintViolation> validateMultiplicities();
    List<UmlgConstraintViolation> checkClassConstraints();
	UmlgNode getOwningObject();
    boolean hasOnlyOneCompositeParent();
	<T extends UmlgNode> List<T> getPathToCompositionalRoot();
//    UmlgMetaNode getMetaNode();
	//the get and set edge is for the case of sequences where the indexOf(more of an order by sequence) is stored
	void setEdge(UmlgRuntimeProperty umlgRuntimeProperty, Edge edge);
	Edge getEdge(UmlgRuntimeProperty umlgRuntimeProperty);
	void z_addToInternalCollection(UmlgRuntimeProperty umlgRuntimeProperty, UmlgNode umlgNode);
}
