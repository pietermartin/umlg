package org.umlg.restlet.visitor.clazz;

import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Class;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.restlet.util.TumlRestletGenerationUtil;

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

    protected void commitOrRollback(OJTryStatement ojTryStatement) {
        OJIfStatement ifTransactionNeedsCommitOrRollback = new OJIfStatement("!(getQueryValue(\"" + TinkerGenerationUtil.rollback+ "\") != null && Boolean.valueOf(getQueryValue(\"" + TinkerGenerationUtil.rollback + "\")))");
        ifTransactionNeedsCommitOrRollback.addToThenPart(TinkerGenerationUtil.graphDbAccess + ".commit()");
        ojTryStatement.getTryPart().addToStatements(ifTransactionNeedsCommitOrRollback);
        ojTryStatement.getFinallyPart().addToStatements(TinkerGenerationUtil.graphDbAccess + ".rollback()");
    }

    protected void addToRouterEnum(Model model, OJAnnotatedClass annotatedClass, String name, String path) {
		OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(TumlRestletGenerationUtil.RestletRouterEnum.toJavaString());
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

    protected String getLookupServerResourceImplName(Class clazz) {
        return TumlClassOperations.className(clazz) + "LookupServerResourceImpl";
    }

	protected String getServerResourceImplName(Classifier classifier) {
		return TumlClassOperations.className(classifier) + "ServerResourceImpl";
	}

    protected String getServerResourceMetatDataImplName(Classifier clazz) {
        return TumlClassOperations.className(clazz) + "MetaDataServerResourceImpl";
    }

    protected String getLookupServerResourceName(Classifier clazz) {
        return TumlClassOperations.className(clazz) + "LookupServerResource";
    }

    protected String getServerResourceName(Classifier clazz) {
		return TumlClassOperations.className(clazz) + "ServerResource";
	}

    protected String getServerResourceMetaDataName(Classifier clazz) {
        return TumlClassOperations.className(clazz) + "MetaDataServerResource";
    }

    protected void addPrivateIdVariable(Classifier clazz, OJAnnotatedClass annotatedClass) {
        OJField privateId = new OJField(getIdFieldName(clazz), new OJPathName("Object"));
        privateId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(privateId);
    }

    protected String getIdFieldName(Classifier clazz) {
        return StringUtils.uncapitalize(TumlClassOperations.className(clazz)).toLowerCase() + "Id";
    }

}
