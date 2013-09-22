package org.umlg.runtime.domain;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.collection.Qualifier;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.ocl.OclAny;
import org.umlg.runtime.validation.TumlConstraintViolation;

import java.util.List;

public interface UmlgNode extends UmlgEnum, OclAny, PersistentObject {
    public static final String ALLINSTANCES_EDGE_LABEL = "allinstances";
	Vertex getVertex();
	boolean isTinkerRoot();
	void initialiseProperties();
	void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty, boolean inverse);
    TumlRuntimeProperty inverseAdder(TumlRuntimeProperty tumlRuntimeProperty, boolean inverse, UmlgNode umlgNode);
    void initVariables();
	List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, UmlgNode node, boolean inverse);
	void delete();
	int getSize(TumlRuntimeProperty tumlRuntimeProperty);
	<E> TinkerSet<E> asSet();
	List<TumlConstraintViolation> validateMultiplicities();
    List<TumlConstraintViolation> checkClassConstraints();
	UmlgNode getOwningObject();
	<T extends UmlgNode> List<T> getPathToCompositionalRoot();
    void addEdgeToMetaNode();
    TumlMetaNode getMetaNode();
}
