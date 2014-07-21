package org.umlg.runtime.adaptor;


public interface UmlgGraphFactory {
	UmlgGraph getTumlGraph(String url);

    /**
     * Calls rollback and then shutdown on the graph.
     * Blueprints spec says shutdown must commit, I disagree so we first rollback.
     * Calling commit might throw validation exception which we would then have to deal with.
     */
    void shutdown();
    void drop();
    void clear();
}
