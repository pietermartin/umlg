package org.tuml.runtime.adaptor;

import org.tuml.runtime.domain.TumlNode;

public interface TumlSchemaMap {
    <T extends TumlNode> Class<T> get(String qualifiedName);
}
