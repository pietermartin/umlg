package org.tuml.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.annotation.*;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.restlet.util.TumlRestletGenerationUtil;
import org.tuml.restlet.visitor.clazz.BaseServerResourceBuilder;

public class TransactionResourceRouterAdder extends BaseServerResourceBuilder implements Visitor<Model> {

	public TransactionResourceRouterAdder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
        addToClassQueryRouterEnum(model, TumlRestletGenerationUtil.TumlTransactionResourceImpl, "TRANSACTION", "\"/transaction\"");
	}

    protected void addToClassQueryRouterEnum(Model model, OJPathName ojPathName, String name, String path) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass("restlet.RestletRouterEnum");
        OJEnumLiteral ojLiteral = new OJEnumLiteral(name);

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp(path);
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(ojPathName.getLast() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(ojPathName);
        routerEnum.addToImports(TumlRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", TumlRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

    @Override
	public void visitAfter(Model element) {

	}

}
