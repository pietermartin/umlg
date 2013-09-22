package org.umlg.runtime.adaptor;


public interface UmlgGraphFactory {
	UmlgGraph getTumlGraph(String url);
    void shutdown();
    void drop();
}
