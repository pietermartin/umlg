package org.tuml.tumltest;

import java.io.File;

import org.tuml.javageneration.Workspace;

public class TestGeneration {

	public static void main(String[] args) {
		File modelFile = new File("src/main/model/tinker-test.uml");
		File projectRoot = new File("/home/pieter/workspace-tuml/tuml/test/tuml-test");
		Workspace workspace = new Workspace(projectRoot, modelFile);
		workspace.generate();
	}
	
}
