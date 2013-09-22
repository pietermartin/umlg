package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.UmlgNode;

public interface UmlgSchemaMap {
    <T extends UmlgNode> Class<T> get(String qualifiedName);
}
