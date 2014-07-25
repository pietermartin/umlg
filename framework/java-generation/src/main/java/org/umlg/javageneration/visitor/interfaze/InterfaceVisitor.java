package org.umlg.javageneration.visitor.interfaze;

import org.eclipse.uml2.uml.Classifier;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgInterfaceOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.Set;

public class InterfaceVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Interface> {

	public InterfaceVisitor(Workspace workspace) {
		super(workspace);
	}

	public void visitBefore(org.eclipse.uml2.uml.Interface inf) {
		OJAnnotatedInterface annotatedInterface = new OJAnnotatedInterface(Namer.name(inf));
		OJPackage ojPackage = new OJPackage(Namer.name(inf.getNearestPackage()));
		annotatedInterface.setMyPackage(ojPackage);
		if (UmlgInterfaceOperations.hasCompositeOwner(inf)) {
			extendCompositionNode(annotatedInterface);
		} else {
			extendTumlNode(annotatedInterface);
		}
		
		for (Classifier c : inf.getGenerals()) {
			annotatedInterface.addToSuperInterfaces(UmlgClassOperations.getPathName(c));
		}

        addAllInstances(inf, annotatedInterface);
        addAllInstancesWithFilter(inf, annotatedInterface);
		
		addToSource(annotatedInterface);
	}

	public void visitAfter(org.eclipse.uml2.uml.Interface clazz) {
	}

    private void addAllInstances(Classifier classifier, OJAnnotatedInterface interfaze) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("allInstances");
        allInstances.setStatic(true);
        OJPathName classPathName = UmlgClassOperations.getPathName(classifier);
        allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics("? extends " + classPathName.getLast()));

        OJField resultField = new OJField("result", UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName));
        resultField.setInitExp("new " + UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName).getLast() + "()");
        allInstances.getBody().addToLocals(resultField);

        Set<Classifier> concreteImplementations = UmlgClassOperations.getConcreteRealization(classifier);
        for (Classifier concreteImplementation : concreteImplementations) {
            allInstances.getBody().addToStatements("result.addAll(" + UmlgGenerationUtil.UMLGAccess + ".allInstances(" + UmlgClassOperations.getPathName(concreteImplementation).getLast() + ".class.getName()))");
            interfaze.addToImports(UmlgClassOperations.getPathName(concreteImplementation));
        }
        allInstances.getBody().addToStatements("return result");

        interfaze.addToImports(UmlgGenerationUtil.UMLGPathName);
        interfaze.addToImports(UmlgGenerationUtil.UMLG_NODE);
        interfaze.addToOperations(allInstances);
    }

    private void addAllInstancesWithFilter(Classifier classifier, OJAnnotatedClass interfaze) {
        OJAnnotatedOperation allInstances = new OJAnnotatedOperation("allInstances");
        allInstances.addToParameters(new OJParameter("filter", UmlgGenerationUtil.Filter));
        allInstances.setStatic(true);
        OJPathName classPathName = UmlgClassOperations.getPathName(classifier);
        allInstances.setReturnType(UmlgGenerationUtil.umlgSet.getCopy().addToGenerics("? extends " + classPathName.getLast()));

        OJField resultField = new OJField("result", UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName));
        resultField.setInitExp("new " + UmlgGenerationUtil.umlgMemorySet.getCopy().addToGenerics(classPathName).getLast() + "()");
        allInstances.getBody().addToLocals(resultField);

        Set<Classifier> concreteImplementations = UmlgClassOperations.getConcreteRealization(classifier);
        for (Classifier concreteImplementation : concreteImplementations) {
            allInstances.getBody().addToStatements("result.addAll(" + UmlgGenerationUtil.UMLGAccess + ".allInstances(" + UmlgClassOperations.getPathName(concreteImplementation).getLast() + ".class.getName(), filter))");
            interfaze.addToImports(UmlgClassOperations.getPathName(concreteImplementation));
        }
        allInstances.getBody().addToStatements("return result");

        interfaze.addToImports(UmlgGenerationUtil.UMLGPathName);
        interfaze.addToImports(UmlgGenerationUtil.UMLG_NODE);
        interfaze.addToOperations(allInstances);
    }

    private void extendCompositionNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(UmlgGenerationUtil.umlgCompositionNodePathName);
	}
	
	private void extendTumlNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(UmlgGenerationUtil.UMLG_NODE);
	}
}
