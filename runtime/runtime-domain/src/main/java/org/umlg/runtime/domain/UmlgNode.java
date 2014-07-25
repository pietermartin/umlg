package org.umlg.runtime.domain;

import com.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.collection.Qualifier;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.domain.ocl.OclAny;
import org.umlg.runtime.validation.UmlgConstraintViolation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface UmlgNode extends UmlgEnum, OclAny, PersistentObject {
    public static final String ALLINSTANCES_EDGE_LABEL = "allinstances";
	Vertex getVertex();
	boolean isTinkerRoot();
	void initialiseProperties();
	void initialiseProperty(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse);
    UmlgRuntimeProperty inverseAdder(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse, UmlgNode umlgNode);
    void initVariables();
	List<Qualifier> getQualifiers(UmlgRuntimeProperty umlgRuntimeProperty, UmlgNode node, boolean inverse);
	void delete();
	int getSize(UmlgRuntimeProperty umlgRuntimeProperty);
	<E> UmlgSet<E> asSet();
	List<UmlgConstraintViolation> validateMultiplicities();
    List<UmlgConstraintViolation> checkClassConstraints();
	UmlgNode getOwningObject();
    boolean hasOnlyOneCompositeParent();
	<T extends UmlgNode> List<T> getPathToCompositionalRoot();
    UmlgMetaNode getMetaNode();
}
