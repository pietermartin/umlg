package org.tuml.runtime.adaptor;

import java.io.File;

public class TumlOrientDbGraphFactory implements TumlGraphFactory {
	
	public static TumlOrientDbGraphFactory INSTANCE = new TumlOrientDbGraphFactory();
    private TumlGraph tumlGraph;

	private TumlOrientDbGraphFactory() {
	}
	
	public static TumlGraphFactory getInstance() {
		return INSTANCE;
	}
	
    @Override
    public TumlGraph getTumlGraph(String url) {
        File f = new File(url);
        this.tumlGraph = new TumlOrientDbGraph("local:" + f.getAbsolutePath());
        TransactionThreadEntityVar.remove();
        this.tumlGraph.addRoot();
        this.tumlGraph.registerListeners();
        this.tumlGraph.commit();
        return this.tumlGraph;
    }

    @Override
    public void destroy() {
        this.tumlGraph.shutdown();
    }

}
