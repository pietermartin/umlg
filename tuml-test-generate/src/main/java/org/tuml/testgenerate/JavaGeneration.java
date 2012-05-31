package org.tuml.testgenerate;

import java.io.File;

import org.eclipse.uml2.uml.Model;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.ModelVisitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.visitor.clazz.ClassVisitor1;
import org.tuml.javageneration.visitor.clazz.ClassVisitor2;
import org.tuml.javageneration.visitor.property.PropertyVisitor;

public class JavaGeneration {

	public static void main(String[] args) {
		Model model = ModelLoader.loadModel(new File("src/main/model/tinker-test.uml"));
		ModelVisitor.visitModel(model, new ClassVisitor1());
		ModelVisitor.visitModel(model, new ClassVisitor2());
		ModelVisitor.visitModel(model, new PropertyVisitor());
		Workspace.toText(new File("/home/pieter/workspace-tuml/tuml/tuml-test"));
		System.out.println("Generation fini");
	}
	
}
