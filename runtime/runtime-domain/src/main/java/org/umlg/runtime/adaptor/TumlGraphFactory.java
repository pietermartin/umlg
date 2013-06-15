package org.umlg.runtime.adaptor;


public interface TumlGraphFactory {
	TumlGraph getTumlGraph(String url);
    void destroy();

}
