package org.tuml.runtime.adaptor;

import java.io.File;

public class TumlOrientDbGraphFactory implements TumlGraphFactory {
	
	public static TumlOrientDbGraphFactory INSTANCE = new TumlOrientDbGraphFactory();

	private TumlOrientDbGraphFactory() {
	}
	
	public static TumlGraphFactory getInstance() {
		return INSTANCE;
	}
	
    @Override
    public TumlGraph getTumlGraph(String url) {
        File f = new File(url);
        TumlGraph tumlGraph = new TumlOrientDbGraph("local:" + f.getAbsolutePath());
        TransactionThreadEntityVar.remove();
        tumlGraph.addRoot();
        tumlGraph.registerListeners();
//        this.tumlGraph.commit();
        return tumlGraph;
    }

    @Override
    public void destroy() {
        GraphDb.getDb().shutdown();
    }

}
