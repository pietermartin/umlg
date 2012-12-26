package org.tuml.restlet.router;

import org.eclipse.uml2.uml.Model;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.restlet.util.TumlRestletGenerationUtil;

public class RestletRouterEnumGenerator extends BaseVisitor implements Visitor<Model> {

	public RestletRouterEnumGenerator(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model element) {
		OJEnum restletRouterEnum = new OJEnum("RestletRouterEnum");
		OJPackage ojPackage = new OJPackage(Namer.name(element.getNearestPackage()) + ".restlet");
		restletRouterEnum.setMyPackage(ojPackage);
		addToSource(restletRouterEnum);
		
		OJField isOnePrimitiveField = new OJField();
		isOnePrimitiveField.setType(new OJPathName("String"));
		isOnePrimitiveField.setName("uri");
		restletRouterEnum.addToFields(isOnePrimitiveField);

		OJField inverseField = new OJField();
		inverseField.setType(new OJPathName("java.lang.Class<? extends ServerResource>"));
		inverseField.setName("serverResource");
		restletRouterEnum.addToFields(inverseField);
		
		restletRouterEnum.implementGetter();
		restletRouterEnum.createConstructorFromFields();
		
		OJAnnotatedOperation attach = new OJAnnotatedOperation("attach");
		attach.addParam("router", TumlRestletGenerationUtil.Router);
		attach.getBody().addToStatements("router.attach(uri, serverResource)");
		restletRouterEnum.addToOperations(attach);
		
		OJAnnotatedOperation attachAll = new OJAnnotatedOperation("attachAll");
		attachAll.setStatic(true);
		attachAll.addParam("router", TumlRestletGenerationUtil.Router);
		restletRouterEnum.addToOperations(attachAll);
	}

	@Override
	public void visitAfter(Model element) {
	}

}
