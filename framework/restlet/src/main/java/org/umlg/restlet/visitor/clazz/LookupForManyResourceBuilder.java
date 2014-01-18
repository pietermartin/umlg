package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.*;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;

import java.util.Set;

public class LookupForManyResourceBuilder extends BaseServerResourceBuilder implements Visitor<Property> {

    public LookupForManyResourceBuilder(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Property p) {
        PropertyWrapper pWrap = new PropertyWrapper(p);
        if (pWrap.hasLookup() && pWrap.isMany()) {
            OJAnnotatedClass owner = findOJClass(p);

            OJPackage ojPackage = owner.getMyPackage();

            OJAnnotatedClass annotatedClass = new OJAnnotatedClass(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast() + "_"
                    + pWrap.getOtherEnd().getName() + "_" + pWrap.getName() + "_lookUpForMany" + "_ServerResourceImpl");
            annotatedClass.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
//            annotatedClass.addToImplementedInterfaces(annotatedInf.getPathName());
            annotatedClass.setMyPackage(ojPackage);
            addToSource(annotatedClass);
            addDefaultConstructor(annotatedClass);

            addCompositeParentIdField(pWrap, annotatedClass);
            addGetObjectRepresentation(pWrap/*, annotatedInf*/, annotatedClass);
            addServerResourceToRouterEnum(pWrap, annotatedClass);
        }
    }

    @Override
    public void visitAfter(Property p) {
    }

    private void addCompositeParentIdField(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJField compositeParentFieldId = new OJField(UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType()).getLast().toLowerCase() + "Id",
                new OJPathName("Object"));
        compositeParentFieldId.setVisibility(OJVisibilityKind.PRIVATE);
        annotatedClass.addToFields(compositeParentFieldId);
    }

    private void addGetObjectRepresentation(PropertyWrapper pWrap/*, OJAnnotatedInterface annotatedInf*/, OJAnnotatedClass annotatedClass) {

//        OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
//        annotatedInf.addToOperations(getInf);
//        getInf.addAnnotationIfNew(new OJAnnotationValue(UmlgRestletGenerationUtil.Get, "json"));

        OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
        get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.ResourceException);
        UmlgGenerationUtil.addOverrideAnnotation(get);

        OJTryStatement ojTryStatement = new OJTryStatement();

        OJPathName parentPathName = UmlgClassOperations.getPathName(pWrap.getOtherEnd().getType());
        ojTryStatement.getTryPart().addToStatements(
                "this." + parentPathName.getLast().toLowerCase() + "Id = "+ UmlgRestletGenerationUtil.UmlgURLDecoder.getLast()+".decode((String)getRequestAttributes().get(\""
                        + parentPathName.getLast().toLowerCase() + "Id\"))");
        annotatedClass.addToImports(UmlgRestletGenerationUtil.UmlgURLDecoder);

        ojTryStatement.getTryPart().addToStatements(
                parentPathName.getLast() + " resource = GraphDb.getDb().instantiateClassifier(this." + parentPathName.getLast().toLowerCase() + "Id"
                        + ")");
        annotatedClass.addToImports(parentPathName);
        buildToJson(pWrap, annotatedClass, ojTryStatement.getTryPart());
        ojTryStatement.setCatchPart(null);

        ojTryStatement.getFinallyPart().addToStatements(UmlgGenerationUtil.graphDbAccess + ".rollback()");
        get.getBody().addToStatements(ojTryStatement);

        annotatedClass.addToImports(UmlgGenerationUtil.graphDbPathName);
        annotatedClass.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
        annotatedClass.addToOperations(get);
    }

    private void buildToJson(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass, OJBlock block) {

        Set<Classifier> concreteImplementations = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getType());
        Set<Classifier> concreteImplementationsFrom = UmlgClassOperations.getConcreteImplementations((Classifier) pWrap.getOwningType());
        if (!concreteImplementationsFrom.isEmpty()) {
            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
            block.addToStatements("StringBuilder json = new StringBuilder()");
            block.addToStatements("json.append(\"[\")");

            int count = 1;
            // For meta data, put where one is navigating to first, then where on is
            // navigating from
            // This is consistent with navigating to a entity with a vertex where
            // there is no navigating from.
            // i.e. the first meta data in the array is the entity navigating to.
            for (Classifier concreteClassifierTo : concreteImplementations) {
                annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo));
                block.addToStatements("json.append(\"{\\\"data\\\": [\")");
                block.addToStatements("json.append(" + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonWithoutCompositeParent(resource." + pWrap.lookup() + "().select(new "
                        + UmlgGenerationUtil.BooleanExpressionEvaluator.getCopy().addToGenerics(UmlgClassOperations.getPathName(pWrap.getType())).getLast()
                        + "() {\n			@Override\n			public Boolean evaluate(" + UmlgClassOperations.getPathName(pWrap.getType()).getLast()
                        + " e) {\n				return e instanceof " + UmlgClassOperations.getPathName(concreteClassifierTo).getLast() + ";\n			}\n		})))");
                annotatedClass.addToImports(UmlgGenerationUtil.BooleanExpressionEvaluator);
                annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getType()));
                block.addToStatements("json.append(\"],\")");

                block.addToStatements("json.append(\" \\\"meta\\\" : {\")");
                block.addToStatements("json.append(\"\\\"qualifiedName\\\": \\\"" + pWrap.getQualifiedName() + "\\\"\")");
                block.addToStatements("json.append(\", \\\"to\\\": \")");
                int countFrom = 1;
                OJIfStatement ifStatementFrom = new OJIfStatement();
                for (Classifier concreteClassifierFrom : concreteImplementationsFrom) {
                    OJBlock conditionBlockFrom = new OJBlock();
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom));
                    String condition = "resource instanceof " + UmlgClassOperations.getPathName(concreteClassifierFrom).getLast();
                    if (countFrom == 1) {
                        ifStatementFrom.setCondition(condition);
                        ifStatementFrom.setThenPart(conditionBlockFrom);
                    } else if (countFrom == concreteImplementationsFrom.size()) {
                        ifStatementFrom.setElsePart(conditionBlockFrom);
                    } else {
                        conditionBlockFrom = ifStatementFrom.addToElseIfCondition(condition, "");
                    }
                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierTo) + ".asJson())");
                    conditionBlockFrom.addToStatements("json.append(\", \\\"from\\\": \")");
                    conditionBlockFrom.addToStatements("json.append(" + UmlgClassOperations.propertyEnumName(concreteClassifierFrom) + ".asJson())");
                    annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierFrom).append(
                            UmlgClassOperations.propertyEnumName(concreteClassifierFrom)));
                    countFrom++;
                }
                block.addToStatements(ifStatementFrom);

                annotatedClass.addToImports(UmlgClassOperations.getPathName(pWrap.getOwningType()).append(
                        UmlgClassOperations.propertyEnumName(pWrap.getOwningType())));
                annotatedClass.addToImports(UmlgClassOperations.getPathName(concreteClassifierTo)
                        .append(UmlgClassOperations.propertyEnumName(concreteClassifierTo)));
                block.addToStatements("json.append(\"}\")");
                if (concreteImplementations.size() != 1 && count != concreteImplementations.size()) {
                    block.addToStatements("json.append(\"}, \")");
                }
                count++;
            }
            block.addToStatements("json.append(\"}]\")");
            block.addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
        } else {
            //TODO not thought through
            block.addToStatements("return null");
        }
    }

    private void addServerResourceToRouterEnum(PropertyWrapper pWrap, OJAnnotatedClass annotatedClass) {
        OJEnum routerEnum = (OJEnum) this.workspace.findOJClass(UmlgRestletGenerationUtil.RestletRouterEnum.toJavaString());
        OJEnumLiteral ojLiteral = new OJEnumLiteral(UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toUpperCase() + "_" + pWrap.lookup());

        OJField uri = new OJField();
        uri.setType(new OJPathName("String"));
        uri.setInitExp("\"/" + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "s/{"
                + UmlgClassOperations.getPathName(pWrap.getOwningType()).getLast().toLowerCase() + "Id}/" + pWrap.lookup() + "\"");
        ojLiteral.addToAttributeValues(uri);

        OJField serverResourceClassField = new OJField();
        serverResourceClassField.setType(new OJPathName("java.lang.Class"));
        serverResourceClassField.setInitExp(annotatedClass.getName() + ".class");
        ojLiteral.addToAttributeValues(serverResourceClassField);
        routerEnum.addToImports(annotatedClass.getPathName());
        routerEnum.addToImports(UmlgRestletGenerationUtil.ServerResource);

        routerEnum.addToLiterals(ojLiteral);

        OJAnnotatedOperation attachAll = routerEnum.findOperation("attachAll", UmlgRestletGenerationUtil.Router);
        attachAll.getBody().addToStatements(routerEnum.getName() + "." + ojLiteral.getName() + ".attach(router)");
    }

}
