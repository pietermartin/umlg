package org.tuml.runtime.adaptor;


public interface NakedGraphFactory {
	NakedGraph getNakedGraph(String url, TinkerSchemaHelper schemaHelper, boolean withSchema);
}
