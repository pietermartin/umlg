package org.umlg.runtime.domain;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.Qualifier;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.domain.ocl.OclAny;
import org.umlg.runtime.validation.UmlgConstraintViolation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UmlgNode extends UmlgEnum, OclAny, PersistentObject {
	Vertex getVertex();
	boolean isTinkerRoot();
	void initialiseProperties(boolean loaded);
	void initialiseProperty(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse, boolean loaded);
    UmlgRuntimeProperty inverseAdder(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse, UmlgNode umlgNode);
    void initVariables();
    void initDataTypeVariablesWithDefaultValues();
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
//	void z_internalAddToCollection(UmlgRuntimeProperty umlgRuntimeProperty, UmlgNode umlgNode);
	void z_internalAddToCollection(UmlgRuntimeProperty umlgRuntimeProperty, Object umlgNode);
//	void z_internalAddDataTypeToCollection(UmlgRuntimeProperty umlgRuntimeProperty, Object object);
	Set<UmlgRuntimeProperty> z_internalBooleanProperties();
	Map<UmlgRuntimeProperty, Object> z_internalDataTypePropertiesWithDefaultValues();
	UmlgCollection<? extends Object> z_internalGetCollectionFor(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse);
}
