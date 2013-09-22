package org.umlg.runtime.adaptor;

public interface UmlgSchemaCreator {
    void createVertexSchemas(VertexSchemaCreator schemaCreator);
    void createEdgeSchemas(EdgeSchemaCreator schemaCreator);
}
