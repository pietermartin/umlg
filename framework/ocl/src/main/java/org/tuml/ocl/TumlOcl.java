package org.tuml.ocl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.emf.common.util.URI;
import org.tuml.framework.ModelLoader;


public class TumlOcl {

	public static void prepareDresdenOcl(File modelFile) {
		try {
			StandaloneFacade.INSTANCE.initialize(new URL("file:" + new File("../../framework/ocl/src/main/resources/log4j.properties").getAbsolutePath()));
			URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
			URI uri = URI.createURI(ModelLoader.findLocation(loader, true, "org/eclipse/uml2/uml/resources", "org.eclipse.uml2.uml.resources"));
			StandaloneFacade.INSTANCE.loadUMLModel(modelFile, uri);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
