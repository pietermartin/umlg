package org.tuml.testocl;

import java.io.File;

import org.eclipse.uml2.uml.Model;
import org.tuml.framework.ModelLoader;
import org.tuml.javageneration.Workspace;

public class TestOclGeneration {

	public static void main(String[] args) {
		Model model = ModelLoader.loadModel(new File("src/main/model/test-ocl.uml"));
		Workspace.visitModel(model);
		Workspace.toText(new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-ocl"));
		System.out.println("Generation fini");
	}
	
}
