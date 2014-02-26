package org.umlg.generation;

import org.umlg.framework.Visitor;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class JavaGenerator {

	private static Logger logger = Logger.getLogger(JavaGenerator.class.getPackage().getName());
	
	public JavaGenerator() {
		super();
	}

	public void generate(File modelFile, File projectRoot, List<Visitor<?>> visitors) {
		logger.info(String.format("Generating code for %s into %s", modelFile.getAbsolutePath(), projectRoot.getAbsolutePath()));
		Workspace workspace = Workspace.INSTANCE;
		workspace.clear();
		workspace.generate(projectRoot, projectRoot, modelFile, visitors);
	}

    public void generate(File modelFile, File entitiesRoot, File restletRoot, List<Visitor<?>> visitors) {
        logger.info(String.format("Generating code for model %s, entities into %s and restlet into %s", modelFile.getAbsolutePath(), entitiesRoot.getAbsolutePath(), restletRoot.getAbsolutePath()));
        Workspace workspace = Workspace.INSTANCE;
        workspace.clear();
        workspace.generate(entitiesRoot, restletRoot, modelFile, visitors);
    }

}
