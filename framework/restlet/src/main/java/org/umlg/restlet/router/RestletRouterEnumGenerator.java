package org.umlg.restlet.router;

import org.eclipse.uml2.uml.Model;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.restlet.util.TumlRestletGenerationUtil;

public class RestletRouterEnumGenerator extends BaseVisitor implements Visitor<Model> {

	public RestletRouterEnumGenerator(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model element) {
		OJEnum restletRouterEnum = new OJEnum(TumlRestletGenerationUtil.RestletRouterEnum.getLast());
		OJPackage ojPackage = new OJPackage(TumlRestletGenerationUtil.UmlgBasePath.toJavaString());
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
