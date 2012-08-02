package org.tuml.javageneration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.uml2.uml.Model;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.JavaModelPrinter;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.ModelVisitor;
import org.tuml.javageneration.audit.visitor.clazz.AuditClassCreator;
import org.tuml.javageneration.audit.visitor.clazz.ClassAuditTransformation;
import org.tuml.javageneration.audit.visitor.interfaze.AuditInterfaceCreator;
import org.tuml.javageneration.audit.visitor.property.AuditPropertyVisitor;
import org.tuml.javageneration.visitor.clazz.ClassBuilder;
import org.tuml.javageneration.visitor.clazz.ClassCreator;
import org.tuml.javageneration.visitor.clazz.ClassImplementedInterfacePropertyVisitor;
import org.tuml.javageneration.visitor.clazz.ClassRuntimePropertyImplementorVisitor;
import org.tuml.javageneration.visitor.clazz.CompositionVisitor;
import org.tuml.javageneration.visitor.clazz.InterfaceRuntimePropertyImplementorVisitor;
import org.tuml.javageneration.visitor.enumeration.EnumerationVisitor;
import org.tuml.javageneration.visitor.interfaze.InterfaceVisitor;
import org.tuml.javageneration.visitor.operation.OperationImplementorSimple;
import org.tuml.javageneration.visitor.property.CompositionProperyVisitor;
import org.tuml.javageneration.visitor.property.DerivedPropertyVisitor;
import org.tuml.javageneration.visitor.property.ManyPropertyVisitor;
import org.tuml.javageneration.visitor.property.OnePropertyVisitor;
import org.tuml.javageneration.visitor.property.PropertyVisitor;
import org.tuml.javageneration.visitor.property.QualifierValidator;
import org.tuml.javageneration.visitor.property.QualifierVisitor;

public class Workspace {

	private static final Logger logger = Logger.getLogger(Workspace.class.getPackage().getName());
	public final static String RESOURCE_FOLDER = "src/main/generated-resources";
	private final Map<String, OJAnnotatedClass> javaClassMap = new HashMap<String, OJAnnotatedClass>();
	private File projectRoot;
	private File modelFile;
	private Model model;
	private boolean audit = false;

	public Workspace(File projectRoot, File modelFile, boolean audit) {
		this.projectRoot = projectRoot;
		this.modelFile = modelFile;
		this.audit = audit;
	}

//	public Workspace(File projectRoot, File modelFile) {
//		this.projectRoot = projectRoot;
//		this.modelFile = modelFile;
//	}

	public void addToClassMap(OJAnnotatedClass ojClass) {
		this.javaClassMap.put(ojClass.getQualifiedName(), ojClass);
	}

	public void generate() {
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
		ModelVisitor.visitModel(this.model, new InterfaceVisitor(this));
		ModelVisitor.visitModel(this.model, new ClassCreator(this));
		ModelVisitor.visitModel(this.model, new ClassBuilder(this));
		ModelVisitor.visitModel(this.model, new ClassRuntimePropertyImplementorVisitor(this));
		ModelVisitor.visitModel(this.model, new InterfaceRuntimePropertyImplementorVisitor(this));
		ModelVisitor.visitModel(this.model, new EnumerationVisitor(this));
		ModelVisitor.visitModel(this.model, new CompositionVisitor(this));
		ModelVisitor.visitModel(this.model, new CompositionProperyVisitor(this));
		ModelVisitor.visitModel(this.model, new PropertyVisitor(this));
		ModelVisitor.visitModel(this.model, new ManyPropertyVisitor(this));
		ModelVisitor.visitModel(this.model, new OnePropertyVisitor(this));
		ModelVisitor.visitModel(this.model, new ClassImplementedInterfacePropertyVisitor(this));
		ModelVisitor.visitModel(this.model, new DerivedPropertyVisitor(this));
		ModelVisitor.visitModel(this.model, new QualifierValidator(this));
		ModelVisitor.visitModel(this.model, new QualifierVisitor(this));
		ModelVisitor.visitModel(this.model, new OperationImplementorSimple(this));
		if (this.audit) {
			ModelVisitor.visitModel(this.model, new ClassAuditTransformation(this));
			ModelVisitor.visitModel(this.model, new AuditClassCreator(this));
			ModelVisitor.visitModel(this.model, new AuditInterfaceCreator(this));
			ModelVisitor.visitModel(this.model, new AuditPropertyVisitor(this));
		}
	}

	public Model getModel() {
		return model;
	}

}
