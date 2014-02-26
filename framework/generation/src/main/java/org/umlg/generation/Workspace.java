package org.umlg.generation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.uml2.uml.Model;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.framework.JavaModelPrinter;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.ModelVisitor;
import org.umlg.framework.Visitor;
import org.umlg.ocl.UmlgOcl2Parser;

public class Workspace {

	private static final Logger logger = Logger.getLogger(Workspace.class.getPackage().getName());
	public final static String DEFAULT_SOURCE_FOLDER = "src/main/generated-java";
    public static final String RESTLET_SOURCE_FOLDER = "src/main/generated-java-restlet";
    public static final String META_SOURCE_FOLDER = "src/main/generated-java-meta";

    public final static String RESOURCE_FOLDER = "src/main/resources";
	private final Map<JavaModelPrinter.Source, OJAnnotatedClass> javaClassMap = new HashMap<JavaModelPrinter.Source, OJAnnotatedClass>();
    private File entitiesRoot;
    private File restletRoot;
    private File modelFile;
	private Model model;
	private List<Visitor<?>> visitors;
	private JavaModelPrinter javaModelPrinter = new JavaModelPrinter();

	public final static Workspace INSTANCE = new Workspace();

	private Workspace() {
	}

    public File getModelFile() {
        return modelFile;
    }

    public void clear() {
        ModelLoader.INSTANCE.clear();
		javaClassMap.clear();
		javaModelPrinter.clear();
	}

	public void addToClassMap(OJAnnotatedClass ojClass, String sourceDir) {
		this.javaClassMap.put(new JavaModelPrinter.Source(ojClass.getQualifiedName(), sourceDir), ojClass);
	}

	public void generate(File entitiesRoot, File restletRoot, File modelFile, List<Visitor<?>> visitors) {
		this.entitiesRoot = entitiesRoot;
        this.restletRoot = restletRoot;
		this.modelFile = modelFile;
		this.visitors = visitors;
		logger.info("Generation started");
		visitModel();
		toText();
		logger.info(String.format("Generation completed for project %s and model %s entities into directory %s, restlet into directory %s",
                new Object[] { this.entitiesRoot.getName(), this.modelFile.getName(),
                        this.entitiesRoot.getAbsolutePath(), this.restletRoot.getAbsolutePath() }));
	}

    /**
     * DEFAULT_SOURCE_FOLDER to entitiesRoot
     * RESTLET_SOURCE_FOLDER to restletRoot
     */
	private void toText() {
        //entities first
		for (Map.Entry<JavaModelPrinter.Source, OJAnnotatedClass> entry : this.javaClassMap.entrySet()) {
            if (entry.getKey().sourceDir.equals(DEFAULT_SOURCE_FOLDER) || entry.getKey().sourceDir.equals(META_SOURCE_FOLDER)) {
			    this.javaModelPrinter.addToSource(entry.getKey().qualifiedName, entry.getKey().sourceDir, entry.getValue().toJavaString());
            }
		}
		this.javaModelPrinter.toText(this.entitiesRoot);
        this.javaModelPrinter.clear();

        //reslet next
        for (Map.Entry<JavaModelPrinter.Source, OJAnnotatedClass> entry : this.javaClassMap.entrySet()) {
            if (entry.getKey().sourceDir.equals(RESTLET_SOURCE_FOLDER)) {
                this.javaModelPrinter.addToSource(entry.getKey().qualifiedName, entry.getKey().sourceDir, entry.getValue().toJavaString());
            }
        }
        this.javaModelPrinter.toText(this.restletRoot);

	}

	public OJAnnotatedClass findOJClass(String name) {
		for (Map.Entry<JavaModelPrinter.Source, OJAnnotatedClass> entry : this.javaClassMap.entrySet()) {
			if (entry.getKey().qualifiedName.equals(name)) {
				return entry.getValue();
			}
		}
		return null;
	}

	private void visitModel() {
//		this.model = ModelLoader.INSTANCE.loadModel(modelFile);
        this.model = UmlgOcl2Parser.INSTANCE.init(modelFile.toURI());
		logger.info(String.format("Start visiting the model"));
		for (Visitor<?> v : visitors) {
			ModelVisitor.visitModel(this.model, v);
		}
	}

	public Model getModel() {
		return model;
	}

    public boolean containsVisitor(Class<?> visitorClass) {
        for (Visitor<?> v : this.visitors) {
            if (v.getClass().equals(visitorClass)) {
                return true;
            }
        }
        return false;
    }

    public File getEntitiesRoot() {
        return entitiesRoot;
    }

}
