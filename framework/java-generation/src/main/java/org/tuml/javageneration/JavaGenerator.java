package org.tuml.javageneration;

import java.io.File;
import java.util.logging.Logger;

public class JavaGenerator {

	private static Logger logger = Logger.getLogger(JavaGenerator.class.getPackage().getName());
	
	public static void main(String[] args) {
		if (args.length != 3) {
			throw new IllegalStateException("3 arguments expected, path to the modeFile and path to the workspace and an audit flag.");
		}
		File modelFile = new File(args[0]);
		File projectRoot = new File(args[1]);
		if (!modelFile.exists()) {
			throw new IllegalStateException(String.format("modelFile %s does not exist", modelFile.getAbsolutePath()));
		}
		if (!projectRoot.exists()) {
			throw new IllegalStateException(String.format("projectRoot %s does not exist", projectRoot.getAbsolutePath()));
		}
		boolean audit = Boolean.parseBoolean(args[2]);
		logger.info(String.format("Generating code for %s into %s", modelFile.getAbsolutePath(), projectRoot.getAbsolutePath()));
		Workspace workspace = new Workspace(projectRoot, modelFile, audit);
		workspace.generate();
	}
	
}
