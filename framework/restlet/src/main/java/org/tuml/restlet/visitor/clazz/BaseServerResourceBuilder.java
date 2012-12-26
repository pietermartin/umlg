package org.tuml.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Class; 
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public abstract class BaseServerResourceBuilder extends BaseVisitor {

	public BaseServerResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	protected void addDefaultConstructor(OJAnnotatedClass annotatedClass) {
		annotatedClass.getDefaultConstructor().getBody().addToStatements("setNegotiated(false)");
	}
	
	protected void addToRouterEnum(Model model, OJAnnotatedClass annotatedClass, String name, String path) {
		OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
		OJEnumLiteral ojLiteral = new OJEnumLiteral(name);

		OJField uri = new OJField();
		uri.setType(new OJPathName("String"));
		uri.setInitExp(path);
		ojLiteral.addToAttributeValues(uri);

		OJField serverResourceClassField = new OJField();
		serverResourceClassField.setType(new OJPathName("java.lang.Class"));
		serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
		ojLiteral.addToAttributeValues(serverResourceClassField);
		routerEnum.addToImports(annotatedClass.getPathName());
		routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

		routerEnum.addToLiterals(ojLiteral);

		OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", TumlRestletGenerationUtil.Router);
		attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
	}

	protected String getServerResourceImplName(Class clazz) {
		return TumlClassOperations.className(clazz) + "ServerResourceImpl";
	}

	protected String getServerResourceName(Class clazz) {
		return TumlClassOperations.className(clazz) + "ServerResource";
	}

}
