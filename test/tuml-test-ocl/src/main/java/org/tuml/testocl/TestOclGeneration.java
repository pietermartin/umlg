package org.tuml.testocl;

import java.io.File;

import org.tuml.javageneration.Workspace;

public class TestOclGeneration {

	public static void main(String[] args) {
		File modelFile = new File("src/main/model/test-ocl.uml");
		File projectRoot = new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-ocl");
		Workspace workspace = new Workspace(projectRoot, modelFile, false);
		workspace.generate();
	}
	
}
