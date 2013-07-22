package org.umlg.restlet.visitor.clazz;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 2013/04/29
 * Time: 7:18 PM
 */
public class RestletFromJsonCreator extends BaseVisitor implements Visitor<Class> {

    public RestletFromJsonCreator(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        addFromJsonNonCompositeOne(clazz);
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    /**
     * Copied from ToFromJsonCreator
     *
     * @param clazz
     */
    private void addFromJsonNonCompositeOne(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJPathName pathName = new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object");
        OJAnnotatedOperation fromJson = (OJAnnotatedOperation) annotatedClass.findOperation("fromJsonNonCompositeOne", Arrays.<OJPathName>asList(pathName));
        //Rewrite the whole body
        fromJson.setBody(new OJBlock());
        if (!clazz.getGenerals().isEmpty()) {
            TinkerGenerationUtil.addOverrideAnnotation(fromJson);
            fromJson.getBody().addToStatements("super.fromJsonNonCompositeOne(propertyMap)");
        }
        List<Property> propertiesForToJson = new ArrayList<Property>(TumlClassOperations.getOneProperties(clazz));
        if (!propertiesForToJson.isEmpty()) {
            for (Property property : propertiesForToJson) {
                if (!property.isReadOnly()) {
                    PropertyWrapper pWrap = new PropertyWrapper(property);
                    OJField field;
                    if (pWrap.isMany()) {
                        if (pWrap.isPrimitive()) {
                            throw new IllegalStateException("Should not happen");
                        } else if (pWrap.isEnumeration()) {
                            throw new IllegalStateException("Should not happen");
                        } else if (pWrap.isComponent()) {
                            field = new OJField(pWrap.fieldname() + "Map", "List<Map<String, Object>>");
                            field.setInitExp("(ArrayList<Map<String, Object>>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        } else {
                            field = new OJField(pWrap.fieldname() + "Map", "List<Map<String, Integer>>");
                            field.setInitExp("(ArrayList<Map<String, Integer>>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        }
                    } else if (pWrap.isEnumeration()) {
                        throw new IllegalStateException("Should not happen");
                    } else {
                        if (pWrap.isNumber()) {
                            throw new IllegalStateException("Should not happen");
                        } else if (!pWrap.isDataType()) {
                            // Primitives are data types
                            field = new OJField(pWrap.getName() + "Map", new OJPathName("Map<String, Object>"));
                            field.setInitExp("(Map<String, Object>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            TinkerGenerationUtil.addSuppressWarning(fromJson);
                        } else if (pWrap.isDate()) {
                            throw new IllegalStateException("Should not happen");
                        } else if (pWrap.isDateTime()) {
                            throw new IllegalStateException("Should not happen");
                        } else if (pWrap.isTime()) {
                            throw new IllegalStateException("Should not happen");
                        } else if (pWrap.isImage()) {
                            throw new IllegalStateException("Should not happen");
                        } else {
                            throw new IllegalStateException("Should not happen");
                        }
                    }
                    OJIfStatement ifInMap = new OJIfStatement("propertyMap.containsKey(\"" + pWrap.fieldname() + "\")");
                    fromJson.getBody().addToStatements(ifInMap);

                    OJIfStatement ifNotNull = new OJIfStatement("propertyMap.get(\"" + pWrap.fieldname() + "\") != null");
                    ifInMap.addToThenPart(ifNotNull);
                    ifNotNull.getThenPart().addToLocals(field);

                    if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration() && !pWrap.isComponent()) {

                        OJIfStatement ifSetToNull;
                        if (pWrap.isMemberOfAssociationClass()) {
                            ifSetToNull = new OJIfStatement(field.getName() + ".isEmpty() || " + field.getName() + ".get(\"id\") == null",
                                    pWrap.setter() + "(null, null)");

                            ifSetToNull.addToElsePart("Long id");
                            ifSetToNull.addToElsePart("Object idFromMap = " + field.getName() + ".get(\"id\")");
                            OJIfStatement ifIdLong = new OJIfStatement("idFromMap instanceof Number");
                            ifIdLong.addToThenPart("id = ((Number)idFromMap).longValue()");
                            ifIdLong.addToElsePart("id = " + TinkerGenerationUtil.TumlTmpIdManager.getLast() + ".INSTANCE.get((String)idFromMap)");
                            ifSetToNull.addToElsePart(ifIdLong);

                            //Create an association class instance
                            ifSetToNull.addToElsePart(pWrap.getAssociationClassPathName().getLast() + " " + pWrap.getAssociationClassFakePropertyName() + " = new "
                                    + pWrap.getAssociationClassPathName().getLast() + "(true)");
                            ifSetToNull.addToElsePart(pWrap.getAssociationClassFakePropertyName() + ".fromJson((Map<String, Object>) propertyMap.get(\"" + pWrap.getAssociationClassFakePropertyName() + "\"))");
                            ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")" + TinkerGenerationUtil.graphDbAccess + ".instantiateClassifier(id), " + pWrap.getAssociationClassFakePropertyName() + ")");
                        } else {
                            ifSetToNull = new OJIfStatement(field.getName() + ".isEmpty() || " + field.getName() + ".get(\"id\") == null",
                                    pWrap.setter() + "(null)");

                            ifSetToNull.addToElsePart("Long id");
                            ifSetToNull.addToElsePart("Object idFromMap = " + field.getName() + ".get(\"id\")");
                            OJIfStatement ifIdLong = new OJIfStatement("idFromMap instanceof Number");
                            ifIdLong.addToThenPart("id = ((Number)idFromMap).longValue()");
                            ifIdLong.addToElsePart("id = " + TinkerGenerationUtil.TumlTmpIdManager.getLast() + ".INSTANCE.get((String)idFromMap)");
                            ifSetToNull.addToElsePart(ifIdLong);
                            ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")" + TinkerGenerationUtil.graphDbAccess + ".instantiateClassifier(id))");
                        }

                        annotatedClass.addToImports(TinkerGenerationUtil.TumlTmpIdManager);
                        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
                        ifNotNull.addToThenPart(ifSetToNull);
                        fromJson.getBody().addToStatements(ifInMap);


                    } else if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isComponent()) {

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ifNotNull.getThenPart().addToLocals(component);

                        OJIfStatement ojIfStatement;
                        //TODO
                        if (pWrap.isMemberOfAssociationClass()) {

                        } else {

                        }
                        ojIfStatement = new OJIfStatement(pWrap.fieldname() + "Map.get(\"id\") instanceof Number");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(((Number)" + pWrap.fieldname() + "Map.get(\"id\")).longValue())");
                        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Long id = " + TinkerGenerationUtil.TumlTmpIdManager.getLast() + ".INSTANCE.get((String)" + pWrap.fieldname() + "Map.get(\"tmpId\"))");
                        annotatedClass.addToImports(TinkerGenerationUtil.TumlTmpIdManager);
                        ojIfStatement.addToElsePart(pWrap.fieldname() + " = GraphDb.getDb().instantiateClassifier(id)");

                        annotatedClass.addToImports(TinkerGenerationUtil.TumlSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");


                        ifNotNull.addToThenPart(ojIfStatement);
                        ifNotNull.addToThenPart(pWrap.fieldname() + ".fromJsonNonCompositeOne(" + pWrap.fieldname() + "Map)");


                    } else if (pWrap.isOne() && pWrap.isDataType()) {
                        throw new IllegalStateException("Should not happen");
                    } else if (pWrap.isMany() && !pWrap.isDataType() && !pWrap.isComponent()) {
                        throw new IllegalStateException("Should not happen");
                    } else if (pWrap.isMany() && !pWrap.isDataType() && pWrap.isComponent()) {

                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Object>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ojForStatement.getBody().addToLocals(component);
                        OJIfStatement ojIfStatement = new OJIfStatement("row.get(\"id\") instanceof Number");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(((Number)row.get(\"id\")).longValue())");
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Long id = " + TinkerGenerationUtil.TumlTmpIdManager.getLast() + ".INSTANCE.get((String)row.get(\"tmpId\"))");
                        annotatedClass.addToImports(TinkerGenerationUtil.TumlTmpIdManager);
                        ojIfStatement.addToElsePart(pWrap.fieldname() + " = GraphDb.getDb().instantiateClassifier(id)");

                        annotatedClass.addToImports(TinkerGenerationUtil.TumlSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojForStatement.getBody().addToStatements(ojIfStatement);
                        ojForStatement.getBody().addToStatements(pWrap.fieldname() + ".fromJsonNonCompositeOne(row)");
                    } else if (pWrap.isMany() && pWrap.isEnumeration()) {
                        throw new IllegalStateException("Should not happen");
                    } else if (pWrap.isMany() && pWrap.isDataType()) {
                        throw new IllegalStateException("Should not happen");
                    } else {
                        throw new IllegalStateException("Should not happen");
                    }
                    if (pWrap.isMemberOfAssociationClass()) {
                        ifNotNull.addToElsePart(pWrap.setter() + "(null, null)");
                    } else {
                        ifNotNull.addToElsePart(pWrap.setter() + "(null)");
                    }
                }
            }
        }
    }


}