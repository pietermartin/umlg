package org.tuml.testbasic;

import java.io.File;

import org.eclipse.uml2.uml.Model;
import org.tuml.framework.ModelLoader;
import org.tuml.javageneration.Workspace;
import org.tuml.ocl.TumlOcl;

public class TestBasicGeneration {

	public static void main(String[] args) {
		File modelFile = new File("src/main/model/tinker-test-basic.uml");
		Model model = ModelLoader.loadModel(modelFile);
		TumlOcl.prepareDresdenOcl(modelFile);
		Workspace.visitModel(model);
		Workspace.toText(new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-basic"));
		System.out.println("Generation fini");
	}
	
}
