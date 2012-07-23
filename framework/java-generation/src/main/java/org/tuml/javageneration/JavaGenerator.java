package org.tuml.javageneration;

import java.io.File;
import java.util.logging.Logger;

public class JavaGenerator {

	private static Logger logger = Logger.getLogger(JavaGenerator.class.getPackage().getName());
	
	public static void main(String[] args) {
		if (args.length < 2) {
			throw new IllegalStateException("2 arguments expected, path to the modeFile and path to the workspace.");
		}
		File modelFile = new File(args[0]);
		File projectRoot = new File(args[1]);
		if (!modelFile.exists()) {
			throw new IllegalStateException(String.format("modelFile %s does not exist", modelFile.getAbsolutePath()));
		}
		if (!projectRoot.exists()) {
			throw new IllegalStateException(String.format("projectRoot %s does not exist", projectRoot.getAbsolutePath()));
		}
		logger.info(String.format("Generating code for %s into %s", modelFile.getAbsolutePath(), projectRoot.getAbsolutePath()));
		Workspace workspace = new Workspace(projectRoot, modelFile);
		workspace.generate();
	}
	
}
