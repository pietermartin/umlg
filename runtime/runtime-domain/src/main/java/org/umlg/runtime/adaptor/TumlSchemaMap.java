package org.umlg.runtime.adaptor;

import org.umlg.runtime.domain.TumlNode;

public interface TumlSchemaMap {
    <T extends TumlNode> Class<T> get(String qualifiedName);
}
