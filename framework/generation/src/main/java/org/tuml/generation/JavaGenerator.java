package org.tuml.generation;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.tuml.framework.Visitor;

public class JavaGenerator {

	private static Logger logger = Logger.getLogger(JavaGenerator.class.getPackage().getName());
	
	public JavaGenerator() {
		super();
	}

//	public static void main(String[] args) {
//		if (args.length != 3) {
//			throw new IllegalStateException("3 arguments expected, path to the modeFile and path to the workspace and an audit flag.");
//		}
//		File modelFile = new File(args[0]);
//		File projectRoot = new File(args[1]);
//		if (!modelFile.exists()) {
//			throw new IllegalStateException(String.format("modelFile %s does not exist", modelFile.getAbsolutePath()));
//		}
//		if (!projectRoot.exists()) {
//			throw new IllegalStateException(String.format("projectRoot %s does not exist", projectRoot.getAbsolutePath()));
//		}
//		boolean audit = Boolean.parseBoolean(args[2]);
//		logger.info(String.format("Generating code for %s into %s", modelFile.getAbsolutePath(), projectRoot.getAbsolutePath()));
//	}

	public void generate(File modelFile, File projectRoot, List<Visitor<?>> visitors) {
		logger.info(String.format("Generating code for %s into %s", modelFile.getAbsolutePath(), projectRoot.getAbsolutePath()));
		Workspace workspace = Workspace.INSTANCE;
		workspace.clear();
		workspace.generate(projectRoot, modelFile, visitors);
	}
	
}
