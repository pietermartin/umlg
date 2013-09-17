package org.umlg.runtime.adaptor;


public interface UmlgGraphFactory {
	TumlGraph getTumlGraph(String url);
    void shutdown();
    void drop();
}
