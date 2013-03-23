package org.tuml.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Class;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJEnum;
import org.tuml.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TinkerGenerationUtil;
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

//    protected void checkIfTransactionSuspended(OJAnnotatedOperation post) {
//        //Check if transaction needs resuming
//        OJIfStatement ifTransactionNeedsResuming = new OJIfStatement("getAttribute(\"" + TinkerGenerationUtil.transactionIdentifier + "\") != null");
//        ifTransactionNeedsResuming.addToThenPart(TinkerGenerationUtil.graphDbAccess + ".resume(" + TinkerGenerationUtil.TumlTransactionManager + ".INSTANCE.get(getAttribute(\"" + TinkerGenerationUtil.transactionIdentifier + "\")))");
//        post.getBody().addToStatements(ifTransactionNeedsResuming);
//    }

//    protected void commitIfNotFromSuspendedTransaction(OJBlock block) {
//        //Check if transaction needs resuming
//        OJIfStatement ifTransactionNeedsResuming = new OJIfStatement("getAttribute(\"" + TinkerGenerationUtil.transactionIdentifier + "\") != null");
//        ifTransactionNeedsResuming.addToThenPart(TinkerGenerationUtil.graphDbAccess + ".commit()");
//        block.addToStatements(ifTransactionNeedsResuming);
//    }

//    protected void commitIfNotFromResume(OJBlock block) {
//        OJIfStatement ifTransactionNeedsResuming = new OJIfStatement("getAttribute(\"" + TinkerGenerationUtil.transactionIdentifier + "\") == null");
//        ifTransactionNeedsResuming.addToThenPart(TinkerGenerationUtil.graphDbAccess + ".commit()");
//        block.addToStatements(ifTransactionNeedsResuming);
//    }

    protected void commitOrRollback(OJBlock block) {
        OJIfStatement ifTransactionNeedsCommitOrRollback = new OJIfStatement("getQueryValue(\"" + TinkerGenerationUtil.rollback+ "\") != null && Boolean.valueOf(getQueryValue(\"" + TinkerGenerationUtil.rollback + "\"))");
        ifTransactionNeedsCommitOrRollback.addToThenPart(TinkerGenerationUtil.graphDbAccess + ".rollback()");
        ifTransactionNeedsCommitOrRollback.addToElsePart(TinkerGenerationUtil.graphDbAccess + ".commit()");
        block.addToStatements(ifTransactionNeedsCommitOrRollback);
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
