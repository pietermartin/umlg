package org.umlg.runtime.domain;

import org.umlg.runtime.collection.UmlgRuntimeProperty;

/**
 * Date: 2013/06/20
 * Time: 7:13 AM
 */
public interface AssociationClassNode extends CompositionNode {

    UmlgRuntimeProperty internalAdder(UmlgRuntimeProperty umlgRuntimeProperty, boolean inverse, UmlgNode umlgNode);

}
