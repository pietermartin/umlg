package org.umlg.runtime.adaptor;

import java.util.List;

/**
 * Date: 2013/09/20
 * Time: 7:36 AM
 */
public interface VertexSchemaCreator {
    /**
     * This list must be in the inheritance order
     * @param classHierarchy
     */
    void create(List<String> classHierarchy);
}
