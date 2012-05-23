package org.tuml.javageneration;

import org.eclipse.uml2.uml.Model;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.ModelVisitor;
import org.tuml.javageneration.visitors.ClassifierVisitor;
import org.tuml.javageneration.visitors.PropertyVisitor;

public class JavaGeneration {

	public static void main(String[] args) {
		Model model = ModelLoader.loadModel();
		ModelVisitor.visitModel(model, new ClassifierVisitor());
		ModelVisitor.visitModel(model, new PropertyVisitor());
		Workspace.toText();
	}
	
}
