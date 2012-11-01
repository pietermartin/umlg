package org.tuml.generation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.uml2.uml.Model;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.JavaModelPrinter;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.ModelVisitor;
import org.tuml.framework.Visitor;

public class Workspace {

	private static final Logger logger = Logger.getLogger(Workspace.class.getPackage().getName());
	public final static String RESOURCE_FOLDER = "src/main/generated-resources";
	private final Map<String, OJAnnotatedClass> javaClassMap = new HashMap<String, OJAnnotatedClass>();
	private File projectRoot;
	private File modelFile;
	private Model model;
	private List<Visitor<?>> visitors;

	public final static Workspace INSTANCE = new Workspace();

	private Workspace() {
	}

	public void addToClassMap(OJAnnotatedClass ojClass) {
		this.javaClassMap.put(ojClass.getQualifiedName(), ojClass);
	}

	public void generate(File projectRoot, File modelFile, List<Visitor<?>> visitors) {
		this.projectRoot = projectRoot;
		this.modelFile = modelFile;
		this.visitors = visitors;
		File sourceDir = new File(projectRoot, JavaModelPrinter.SOURCE_FOLDER);
		logger.info("Generation started");
		visitModel();
		toText();
		logger.info(String.format("Generation completed for project %s and model %s into directory %s", new Object[] { this.projectRoot.getName(), this.modelFile.getName(),
				sourceDir.getAbsolutePath() }));
	}

	private void toText() {
		for (Map.Entry<String, OJAnnotatedClass> entry : this.javaClassMap.entrySet()) {
			JavaModelPrinter.addToSource(entry.getKey(), entry.getValue().toJavaString());
		}
		JavaModelPrinter.toText(this.projectRoot);
	}

	public OJAnnotatedClass findOJClass(String name) {
		return this.javaClassMap.get(name);
	}

	private void visitModel() {
		this.model = ModelLoader.loadModel(modelFile);
		logger.info(String.format("Start visiting the model"));
		for (Visitor<?> v : visitors) {
			ModelVisitor.visitModel(this.model, v);
		}
	}

	public Model getModel() {
		return model;
	}

}
