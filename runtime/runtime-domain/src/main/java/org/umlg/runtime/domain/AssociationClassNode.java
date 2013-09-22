package org.umlg.runtime.domain;

import org.umlg.runtime.collection.TumlRuntimeProperty;

/**
 * Date: 2013/06/20
 * Time: 7:13 AM
 */
public interface AssociationClassNode extends CompositionNode {

    TumlRuntimeProperty internalAdder(TumlRuntimeProperty tumlRuntimeProperty, boolean inverse, UmlgNode umlgNode);

}
