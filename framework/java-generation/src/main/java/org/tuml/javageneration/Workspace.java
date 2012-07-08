package org.tuml.javageneration;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.eclipse.uml2.uml.Model;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.JavaModelPrinter;
import org.tuml.framework.ModelLoader;
import org.tuml.framework.ModelVisitor;
import org.tuml.javageneration.ocl.visitor.DerivedPropertyVisitor;
import org.tuml.javageneration.visitor.clazz.ClassBuilder;
import org.tuml.javageneration.visitor.clazz.ClassCreator;
import org.tuml.javageneration.visitor.clazz.ClassImplementedInterfacePropertyVisitor;
import org.tuml.javageneration.visitor.clazz.ClassRuntimePropertyImplementorVisitor;
import org.tuml.javageneration.visitor.clazz.CompositionVisitor;
import org.tuml.javageneration.visitor.enumeration.EnumerationVisitor;
import org.tuml.javageneration.visitor.interfaze.InterfaceVisitor;
import org.tuml.javageneration.visitor.operation.OperationImplementorSimple;
import org.tuml.javageneration.visitor.property.CompositionProperyVisitor;
import org.tuml.javageneration.visitor.property.ManyPropertyVisitor;
import org.tuml.javageneration.visitor.property.OnePropertyVisitor;
import org.tuml.javageneration.visitor.property.PropertyVisitor;
import org.tuml.javageneration.visitor.property.QualifierValidator;
import org.tuml.javageneration.visitor.property.QualifierVisitor;
import org.tuml.ocl.TumlOcl;

public class Workspace {

	private static final Logger logger = Logger.getLogger(Workspace.class.getPackage().getName());
	public final static String RESOURCE_FOLDER = "src/main/generated-resources";
	private final Map<String, OJAnnotatedClass> javaClassMap = new HashMap<String, OJAnnotatedClass>();
	private File projectRoot;
	private File modelFile;
	private Model model;

	public Workspace(File projectRoot, File modelFile) {
		this.projectRoot = projectRoot;
		this.modelFile = modelFile;
	}

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
	
	public File writeOclFile(String ocl, String qualifiedName) {
		try {
			File oclFile = new File("src/main/generated-resources/" + qualifiedName + ".ocl");
			FileWriter fw = new FileWriter(oclFile);
			fw.write(ocl);
			fw.flush();
			return oclFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	private void clearOclFiles() {
		Iterator<File> iter = FileUtils.iterateFiles(new File(this.projectRoot, RESOURCE_FOLDER), new String[] { "ocl" }, false);
		while (iter.hasNext()) {
			File file = (File) iter.next();
			file.delete();
		}
	}

	private void visitModel() {
		clearOclFiles();
		this.model = ModelLoader.loadModel(modelFile);
		TumlOcl.prepareDresdenOcl(this.modelFile);
		ModelVisitor.visitModel(this.model, new InterfaceVisitor(this));
		ModelVisitor.visitModel(this.model, new ClassCreator(this));
		ModelVisitor.visitModel(this.model, new ClassBuilder(this));
		ModelVisitor.visitModel(this.model, new ClassRuntimePropertyImplementorVisitor(this));
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
	}

}
