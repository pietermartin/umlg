package org.tuml.testbasic;

import java.io.File;

import org.tuml.javageneration.Workspace;

public class TestBasicGeneration {

	public static void main(String[] args) {
		File modelFile = new File("src/main/model/tinker-test-basic.uml");
		File projectRoot = new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-basic");
		Workspace workspace = new Workspace(projectRoot, modelFile);
		workspace.generate();
	}
	
}
