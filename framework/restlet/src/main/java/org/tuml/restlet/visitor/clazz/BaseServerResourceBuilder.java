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

    protected void addPostResource(Classifier concreteClassifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation add = new OJAnnotatedOperation("add", "String");
        add.setComment("This method adds a single new instance. If and id already exist it passes the existing id back as a tmpId");
        add.setVisibility(OJVisibilityKind.PRIVATE);
        add.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(add);
        add.getBody().addToStatements(TumlClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
        add.getBody().addToStatements("childResource.fromJson(propertyMap)");
        add.getBody().addToStatements("String jsonResult = childResource.toJson()");
        OJIfStatement ifContainsId = new OJIfStatement("propertyMap.containsKey(\"id\")");
        ifContainsId.addToThenPart("Long tmpId = Long.valueOf((Integer) propertyMap.get(\"id\"))");
        ifContainsId.addToThenPart("jsonResult = jsonResult.substring(1);");
        ifContainsId.addToThenPart("jsonResult = \"{\\\"tmpId\\\": \" + tmpId + \", \" + jsonResult;");
        add.getBody().addToStatements(ifContainsId);
        add.getBody().addToStatements("return jsonResult");

        OJAnnotatedOperation addWithoutData = new OJAnnotatedOperation("add", "String");
        addWithoutData.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToOperations(addWithoutData);
        addWithoutData.getBody().addToStatements(TumlClassOperations.getPathName(concreteClassifier).getLast() + " childResource = new " + TumlClassOperations.getPathName(concreteClassifier).getLast() + "(true)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(concreteClassifier));
        addWithoutData.getBody().addToStatements("return childResource.toJson()");
    }

    protected void addPutResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation put = new OJAnnotatedOperation("put", "String");
        put.setVisibility(OJVisibilityKind.PRIVATE);
        put.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(put);
        put.getBody().addToStatements("Long id = Long.valueOf((Integer)propertyMap.get(\"id\"))");
        put.getBody().addToStatements(
                TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
        put.getBody().addToStatements("childResource.fromJson(propertyMap)");
        put.getBody().addToStatements("return childResource.toJson()");
    }

    protected void addDeleteResource(Classifier classifier, OJAnnotatedClass annotatedClass, OJPathName parentPathName) {
        OJAnnotatedOperation delete = new OJAnnotatedOperation("delete");
        delete.setVisibility(OJVisibilityKind.PRIVATE);
        delete.addToParameters(new OJParameter("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object")));
        annotatedClass.addToOperations(delete);
        delete.getBody().addToStatements("Long id = Long.valueOf((Integer)propertyMap.get(\"id\"))");
        delete.getBody().addToStatements(
                TumlClassOperations.getPathName(classifier).getLast() + " childResource = GraphDb.getDb().instantiateClassifier(id)");
        annotatedClass.addToImports(TumlClassOperations.getPathName(classifier));
        delete.getBody().addToStatements("childResource.delete()");
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
