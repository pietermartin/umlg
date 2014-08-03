package org.umlg.javageneration.visitor.clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.VisitSubclasses;
import org.umlg.java.metamodel.*;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedField;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.generated.OJVisibilityKindGEN;
import org.umlg.javageneration.util.DataTypeEnum;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class ToFromJsonCreator extends BaseVisitor implements Visitor<Class> {

    public static final String URI_FOR_RESTFULL = "uri";

    public ToFromJsonCreator(Workspace workspace) {
        super(workspace);
    }

    @Override
    @VisitSubclasses({Class.class, AssociationClass.class})
    public void visitBefore(Class clazz) {
        Set<Property> propertiesForToJson = UmlgClassOperations.getPropertiesForToJson(clazz);
        if (clazz instanceof AssociationClass) {
            propertiesForToJson.addAll(((AssociationClass) clazz).getMemberEnds());
        }
        addToJson(clazz, "toJson", propertiesForToJson);
        Set<Property> propertiesForToJsonExcludingCompositeParent = UmlgClassOperations.getPropertiesForToJsonExcludingCompositeParent(clazz);
        if (clazz instanceof AssociationClass) {
            propertiesForToJsonExcludingCompositeParent.addAll(((AssociationClass) clazz).getMemberEnds());
        }
        addToJson(clazz, "toJsonWithoutCompositeParent", propertiesForToJsonExcludingCompositeParent);
        addFromJson(clazz);
        addFromJsonWithMapper(clazz);
        addFromJsonDataTypeAndComposite(clazz);
        addFromJsonNonCompositeOne(clazz);
    }

    @Override
    public void visitAfter(Class clazz) {
    }

    private void addToJson(Class clazz, String operationName, Set<Property> propertiesForToJson) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);

        OJAnnotatedOperation toJsonSimple = new OJAnnotatedOperation(operationName, new OJPathName("String"));
        UmlgGenerationUtil.addOverrideAnnotation(toJsonSimple);
        toJsonSimple.getBody().addToStatements("return " + operationName + "(false)");
        annotatedClass.addToOperations(toJsonSimple);

        OJAnnotatedOperation toJson = new OJAnnotatedOperation(operationName, new OJPathName("String"));
        UmlgGenerationUtil.addOverrideAnnotation(toJson);
        toJson.addParam("deep", "Boolean");
        toJson.setComment("deep indicates that components also be serialized.");
        if (clazz.getGenerals().isEmpty()) {
            toJson.getBody().addToStatements("StringBuilder sb = new StringBuilder()");
            toJson.getBody().addToStatements("sb.append(\"\\\"id\\\": \\\"\" + getId() + \"\\\", \")");
            if (this.workspace.containsVisitor(MetaClassBuilder.class)) {
                toJson.getBody().addToStatements("sb.append(\"\\\"metaNodeId\\\": \\\"\" + getMetaNode().getId() + \"\\\", \")");
            }
        } else {
            toJson.getBody().addToStatements("String result = super." + operationName + "(deep)");
            toJson.getBody().addToStatements("result = result.substring(1, result.length() - 1)");
            toJson.getBody().addToStatements("StringBuilder sb = new StringBuilder(result)");
            if (!propertiesForToJson.isEmpty()) {
                toJson.getBody().addToStatements("sb.append(\", \")");
            }
        }
        OJIfStatement ifDeep;
        boolean first = true;
        for (Property p : propertiesForToJson) {
            if (!first) {
                toJson.getBody().addToStatements("sb.append(\", \")");
            }
            PropertyWrapper pWrap = new PropertyWrapper(p);
            if (pWrap.isMany() && pWrap.isComponent()) {
                ifDeep = new OJIfStatement("deep");
                ifDeep.addToThenPart("sb.append(\"\\\"" + pWrap.getName() + "\\\": [\" + " + UmlgGenerationUtil.ToJsonUtil.getLast() + "." + operationName + "("
                        + pWrap.getter() + "(), true) + \"]" + "\")");
                ifDeep.addToElsePart("sb.delete(sb.length() - 2, sb.length())");
                annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                toJson.getBody().addToStatements(ifDeep);
            } else if (pWrap.isOne() && pWrap.isComponent()) {
                ifDeep = new OJIfStatement("deep");
                ifDeep.addToThenPart("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() == null ? \"null\" : " + pWrap.getter() + "()." + operationName + "(true)) + \"" + "\")");
                ifDeep.addToElsePart("sb.delete(sb.length() - 2, sb.length())");
                annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                toJson.getBody().addToStatements(ifDeep);
            } else if (pWrap.isMany() && pWrap.isPrimitive()) {
                toJson.getBody().addToStatements(
                        "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + " + UmlgGenerationUtil.ToJsonUtil.getLast() + ".primitivesToJson("
                                + pWrap.getter() + "()) + \"" + "\")"
                );
                annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
            } else if (pWrap.isEnumeration()) {
                if (pWrap.isMany()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() == null ? null : " + pWrap.getter()
                                    + "().toJson()" + "))"
                    );
                } else {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() == null ? null : \"\\\"\" + " + pWrap.getter()
                                    + "().toJson() + \"\\\"" + "\"))"
                    );
                }
            } else {
                if (pWrap.isNumber() || pWrap.isBoolean()) {
                    toJson.getBody().addToStatements("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + " + pWrap.getter() + "() + \"" + "\")");
                } else if (!pWrap.isDataType()) {
                    // TODO need to implement display name interface or something
                    OJIfStatement ifOneNotNull = new OJIfStatement(pWrap.getter() + "() != null");
                    OJIfStatement ifHasTmpId = new OJIfStatement(UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.get(" + pWrap.getter() + "().getId()) != null");
                    ifHasTmpId.addToThenPart("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + \"{\\\"id\\\": \\\"\" + " + pWrap.getter() + "().getId() + \"\\\", \\\"tmpId\\\": \\\"\" + " + UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.get(" + pWrap.getter() + "().getId())  + \"\\\",\\\"displayName\\\": \\\"\" + " + pWrap.getter() + "().getName() + \"\\\"}\" + \"" + "\")");
                    ifOneNotNull.addToThenPart(ifHasTmpId);
                    ifHasTmpId.addToElsePart("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + \"{\\\"id\\\": \\\"\" + " + pWrap.getter() + "().getId() + \"\\\", \\\"displayName\\\": \\\"\" + " + pWrap.getter() + "().getName() + \"\\\"}\" + \"" + "\")");
                    ifOneNotNull.addToElsePart("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + \"{\\\"id\\\": \" + null + \", \\\"displayName\\\": \" + null + \"}\")");
                    toJson.getBody().addToStatements(ifOneNotNull);
                    annotatedClass.addToImports(UmlgGenerationUtil.UmlgTmpIdManager);

                    //Check association class
                    if (pWrap.isOne() && pWrap.isMemberOfAssociationClass() && !(clazz instanceof AssociationClass)) {

                        ifDeep = new OJIfStatement("deep || (" + pWrap.associationClassGetter() + "() != null && " + UmlgGenerationUtil.UmlgAssociationClassManager.getLast() + ".INSTANCE.has(\"" + pWrap.getAssociationClassFakePropertyName() + "\", " + pWrap.getter() + "().getId()))");
                        ifDeep.addToThenPart("sb.append(\", \")");
                        ifDeep.addToThenPart("sb.append(\"\\\"" + pWrap.getAssociationClassFakePropertyName() + "\\\": \" + (" + pWrap.associationClassGetter() + "() == null ? \"null\" : " + pWrap.associationClassGetter() + "()." + operationName + "(true)) + \"" + "\")");
                        annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                        annotatedClass.addToImports(UmlgGenerationUtil.UmlgAssociationClassManager);
                        toJson.getBody().addToStatements(ifDeep);

                        // TODO need to implement display name interface or something
                        ifOneNotNull = new OJIfStatement(pWrap.associationClassGetter() + "() != null");
                        ifHasTmpId = new OJIfStatement(UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.get(" + pWrap.associationClassGetter() + "().getId()) != null");
                        ifHasTmpId.addToThenPart("sb.append(\", \\\"" + pWrap.getAssociationClassFakePropertyName() + "\\\": \" + \"{\\\"id\\\": \\\"\" + " + pWrap.associationClassGetter() + "().getId() + \"\\\", \\\"tmpId\\\": \\\"\" + " + UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.get(" + pWrap.associationClassGetter() + "().getId())  + \"\\\",\\\"displayName\\\": \\\"\" + " + pWrap.associationClassGetter() + "().getName() + \"\\\"}\" + \"" + "\")");
                        ifOneNotNull.addToThenPart(ifHasTmpId);
                        ifHasTmpId.addToElsePart("sb.append(\", \\\"" + pWrap.getAssociationClassFakePropertyName() + "\\\": \" + \"{\\\"id\\\": \\\"\" + " + pWrap.associationClassGetter() + "().getId() + \"\\\", \\\"displayName\\\": \\\"\" + " + pWrap.associationClassGetter() + "().getName() + \"\\\"}\" + \"" + "\")");
                        ifOneNotNull.addToElsePart("sb.append(\", \\\"" + pWrap.getAssociationClassFakePropertyName() + "\\\": \" + \"{\\\"id\\\": \" + null + \", \\\"displayName\\\": \" + null + \"}\")");
                        ifDeep.addToElsePart(ifOneNotNull);
                        toJson.getBody().addToStatements(ifDeep);
                    } else if (pWrap.isMemberOfAssociationClass() && clazz instanceof AssociationClass) {

//                        PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());
//                        toJson.getBody().addToStatements("sb.append(\", \\\"" + otherEnd.fieldname() + "\\\": \" + \"{\\\"id\\\": \" + " + pWrap.associationClassGetter() + "().getId() + \", \\\"tmpId\\\": \\\"\" + " + UmlgGenerationUtil.UmlgTmpIdManager.getLast() + ".INSTANCE.get(" + pWrap.associationClassGetter() + "().getId())  + \"\\\",\\\"displayName\\\": \\\"\" + " + otherEnd.getter() + "().getName() + \"\\\"}\" + \"" + "\")");

                    }
                } else if (pWrap.getDataTypeEnum() != null) {
                    if (pWrap.isOne()) {
                        if (pWrap.isDateTime()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                            + UmlgGenerationUtil.umlgFormatter.getLast() + ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isDate()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                            + UmlgGenerationUtil.umlgFormatter.getLast() + ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isTime()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                            + UmlgGenerationUtil.umlgFormatter.getLast() + ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isImage()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                            + UmlgGenerationUtil.umlgFormatter.getLast() + ".encode(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isVideo()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                            + UmlgGenerationUtil.umlgFormatter.getLast() + ".encode(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isAudio()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + TinkerFormatter.encode("
                                            + pWrap.getter() + "()) + \"\\\"\" : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + pWrap.getter()
                                            + "() + \"\\\"\" : null " + "))"
                            );
                        }
                    } else {
                        if (pWrap.isDateTime()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? "
                                            + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonDateTime(" + pWrap.getter() + "()) : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                        } else if (pWrap.isDate()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ?  "
                                            + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonLocalDate(" + pWrap.getter() + "()) : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                        } else if (pWrap.isTime()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? "
                                            + UmlgGenerationUtil.ToJsonUtil.getLast() + ".toJsonLocalTime(" + pWrap.getter() + "()) : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                        } else if (pWrap.isImage()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? "
                                            + UmlgGenerationUtil.ToJsonUtil.getLast() + ".encode(" + pWrap.getter() + "()) : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                        } else if (pWrap.isVideo()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? "
                                            + UmlgGenerationUtil.ToJsonUtil.getLast() + ".encode(" + pWrap.getter() + "()) : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                        } else if (pWrap.isAudio()) {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? TinkerFormatter.encode("
                                            + pWrap.getter() + "()) : null " + "))"
                            );
                        } else {
                            toJson.getBody().addToStatements(
                                    "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                            + UmlgGenerationUtil.ToJsonUtil.getLast() + ".primitivesToJson(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))"
                            );
                            annotatedClass.addToImports(UmlgGenerationUtil.ToJsonUtil);
                        }
                    }
                } else {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + pWrap.getter()
                                    + "().replace(\"\\n\", \"\\\\n\").replace(\"\\\"\", \"\\\\\\\"\") + \"\\\"\" : null " + "))"
                    );
                }
            }
            first = false;
        }

        if (clazz.getGenerals().isEmpty()) {
            //Add in qualified type name
            toJson.getBody().addToStatements("sb.append(\", \")");
            toJson.getBody().addToStatements("sb.append(\"\\\"qualifiedName\\\": \\\"\" + getQualifiedName() + \"\\\"\")");
            toJson.getBody().addToStatements("sb.append(\", \")");
            toJson.getBody().addToStatements(URI_FOR_RESTFULL, "//PlaceHolder for restful\nsb.append(\"\\\"uri\\\": {}\")");
        }
        toJson.getBody().addToStatements("sb.insert(0, \"{\")");
        toJson.getBody().addToStatements("sb.append(\"}\")");
        toJson.getBody().addToStatements("return sb.toString()");
        annotatedClass.addToOperations(toJson);
    }

    private void addFromJson(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJson");
        fromJson.addParam("json", new OJPathName("String"));
        UmlgGenerationUtil.addOverrideAnnotation(fromJson);
        annotatedClass.addToOperations(fromJson);

        OJField objectMapper = new OJField("mapper", UmlgGenerationUtil.ObjectMapper);
        objectMapper.setInitExp(UmlgGenerationUtil.ObjectMapperFactory.getLast() + ".INSTANCE.getObjectMapper()");
        annotatedClass.addToImports(UmlgGenerationUtil.ObjectMapperFactory);
        fromJson.getBody().addToLocals(objectMapper);

        OJTryStatement tryS = new OJTryStatement();

        OJAnnotatedField propertyMap = new OJAnnotatedField("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        propertyMap.setInitExp("mapper.readValue(json, Map.class)");
        propertyMap.addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.SuppressWarnings"), "unchecked"));
        tryS.getTryPart().addToLocals(propertyMap);

        tryS.getTryPart().addToStatements("fromJson(propertyMap)");

        tryS.setCatchParam(new OJParameter("e", new OJPathName("java.lang.Exception")));
        tryS.getCatchPart().addToStatements("throw new RuntimeException(e)");

        fromJson.getBody().addToStatements(tryS);
    }

    private void addFromJsonWithMapper(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJson");
        fromJson.addParam("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        UmlgGenerationUtil.addOverrideAnnotation(fromJson);
        annotatedClass.addToOperations(fromJson);
        fromJson.getBody().addToStatements("fromJsonDataTypeAndComposite(propertyMap)");
        fromJson.getBody().addToStatements("fromJsonNonCompositeOne(propertyMap)");
    }

    private void addFromJsonDataTypeAndComposite(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJsonDataTypeAndComposite");
        fromJson.addParam("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        fromJson.setVisibility(OJVisibilityKindGEN.PUBLIC);
        UmlgGenerationUtil.addOverrideAnnotation(fromJson);
        annotatedClass.addToOperations(fromJson);
        if (!clazz.getGenerals().isEmpty()) {
            fromJson.getBody().addToStatements("super.fromJsonDataTypeAndComposite(propertyMap)");
        }
        List<Property> propertiesForToJson = new ArrayList<Property>(UmlgClassOperations.getPrimitiveOrEnumOrComponentsExcludeOneProperties(clazz));
        if (!propertiesForToJson.isEmpty()) {
            for (Property property : propertiesForToJson) {
                if (!property.isReadOnly()) {
                    PropertyWrapper pWrap = new PropertyWrapper(property);
                    OJField field;
                    if (pWrap.isMany()) {
                        if (pWrap.isPrimitive()) {
                            if (pWrap.isNumber()) {
                                field = new OJField(pWrap.fieldname(), pWrap.javaTypePath().replaceGeneric(0, "Number"));
                                field.setInitExp("new " + pWrap.javaTumlMemoryTypePath().replaceGeneric(0, "Number").getLast() + "((Collection<Number>)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            } else {
                                field = new OJField(pWrap.fieldname(), pWrap.javaTypePath());
                                field.setInitExp("new " + pWrap.javaTumlMemoryTypePath().getLast() + "((Collection<" + pWrap.javaBaseTypePath()
                                        + ">)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            }
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        } else if (pWrap.isEnumeration()) {
                            field = new OJField(pWrap.fieldname(), "Collection<String>");
                            field.setInitExp("(Collection<String>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        } else if (pWrap.isComponent()) {
                            field = new OJField(pWrap.fieldname() + "Map", "List<Map<String, Object>>");
                            field.setInitExp("(ArrayList<Map<String, Object>>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        } else if (pWrap.getDataTypeEnum() != null) {
                            field = new OJField(pWrap.fieldname(), new OJPathName("java.util.Collection").addToGenerics("String"));
                            field.setInitExp("(Collection<String>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        } else {
                            field = new OJField(pWrap.fieldname() + "Map", "List<Map<String, Integer>>");
                            field.setInitExp("(ArrayList<Map<String, Integer>>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        }
                    } else if (pWrap.isEnumeration()) {
                        field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
                        field.setInitExp(pWrap.javaBaseTypePath().getLast() + ".fromJson((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                    } else {
                        if (pWrap.isNumber()) {
                            OJField fieldNumber = new OJField(pWrap.getName() + "AsNumber", new OJPathName("Number"));
                            fieldNumber.setInitExp("(" + new OJPathName("Number") + ")propertyMap.get(\"" + pWrap.getName() + "\")");
                            field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
                            if (pWrap.isInteger()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".intValue() : null");
                            } else if (pWrap.isUnlimitedNatural()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".intValue() : null");
                            } else if (pWrap.isFloat()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".floatValue() : null");
                            } else if (pWrap.isDouble()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".doubleValue() : null");
                            } else if (pWrap.isLong()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".longValue() : null");
                            } else if (pWrap.isShort()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".shortValue() : null");
                            } else if (pWrap.isByte()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".byteValue() : null");
                            } else {
                                throw new RuntimeException("Not yet implemented!");
                            }
                            fromJson.getBody().addToLocals(fieldNumber);
                        } else if (!pWrap.isDataType()) {
                            // Primitives are data types
                            field = new OJField(pWrap.fieldname() + "Map", new OJPathName("Map<String, Object>"));
                            field.setInitExp("(Map<String, Object>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            UmlgGenerationUtil.addSuppressWarning(fromJson);
                        } else if (pWrap.isDate()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Date.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".parseDate((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isDateTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.DateTime.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".parseDateTime((String)propertyMap.get(\"" + pWrap.getName()
                                    + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Time.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".parseTime((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isImage()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Image.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".decode((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else {
                            field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
                            field.setInitExp("(" + pWrap.javaBaseTypePath() + ")propertyMap.get(\"" + pWrap.getName() + "\")");
                        }
                    }
                    OJIfStatement ifInMap = new OJIfStatement("propertyMap.containsKey(\"" + pWrap.fieldname() + "\")");
                    fromJson.getBody().addToStatements(ifInMap);

                    OJIfStatement ifNotNull = new OJIfStatement("propertyMap.get(\"" + pWrap.fieldname() + "\") != null");
                    ifInMap.addToThenPart(ifNotNull);
                    ifNotNull.getThenPart().addToLocals(field);

                    if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration() && !pWrap.isComponent()) {

                        OJIfStatement ifSetToNull = new OJIfStatement(field.getName() + ".isEmpty() || " + field.getName() + ".get(\"id\") == null",
                                pWrap.setter() + "(null)");
                        ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")" + UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getEntity + "(("
                                + pWrap.fieldname() + "Map.get(\"id\"))))");
                        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
                        ifNotNull.addToThenPart(ifSetToNull);
                        fromJson.getBody().addToStatements(ifInMap);


                    } else if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isComponent()) {

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ifNotNull.getThenPart().addToLocals(component);
                        OJField idField = new OJField("idField", "String");
                        idField.setInitExp("(String)" + pWrap.fieldname() + "Map.get(\"id\")");
                        ifNotNull.getThenPart().addToLocals(idField);
                        OJIfStatement ojIfStatement = new OJIfStatement("idField != null && !idField.startsWith(\"fake\")");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + UmlgGenerationUtil.UMLGAccess
                                + "." + UmlgGenerationUtil.getEntity + "((" + pWrap.fieldname() + "Map.get(\"id\")))");
                        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = UmlgSchemaFactory.getUmlgSchemaMap().get((String)" + pWrap.fieldname() + "Map.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(UmlgGenerationUtil.UmlgSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ifNotNull.addToThenPart(ojIfStatement);
                        ifNotNull.addToThenPart(pWrap.fieldname() + ".fromJsonDataTypeAndComposite(" + pWrap.fieldname() + "Map)");


                    } else if (pWrap.isOne() && pWrap.isDataType()) {
                        // Enumeration is a DataType
                        ifNotNull.addToThenPart(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                    } else if (pWrap.isMany() && !pWrap.isDataType() && !pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Integer>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.javaBaseTypePath().getLast() + " " + pWrap.fieldname() + " = " + UmlgGenerationUtil.UMLGAccess
                                + "." + UmlgGenerationUtil.getEntity + "(row.get(\"id\"))");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementConstructor);
                        OJSimpleStatement ojSimpleStatementFromJson = new OJSimpleStatement(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementFromJson);
                    } else if (pWrap.isMany() && !pWrap.isDataType() && pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");

                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Object>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ojForStatement.getBody().addToLocals(component);
                        OJField idField = new OJField("idField", "String");
                        idField.setInitExp("(String)row.get(\"id\")");
                        ojForStatement.getBody().addToLocals(idField);
                        OJIfStatement ojIfStatement = new OJIfStatement("idField != null && !idField.startsWith(\"fake\")");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + UmlgGenerationUtil.UMLGAccess
                                + "." + UmlgGenerationUtil.getEntity + "((row.get(\"id\")))");
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = UmlgSchemaFactory.getUmlgSchemaMap().get((String)row.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(UmlgGenerationUtil.UmlgSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ojForStatement.getBody().addToStatements(ojIfStatement);
                        ojForStatement.getBody().addToStatements(pWrap.fieldname() + ".fromJsonDataTypeAndComposite(row)");
                    } else if (pWrap.isMany() && pWrap.isEnumeration()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("enumLiteral", new OJPathName("String"), pWrap.fieldname());
                        ojForStatement.getBody().addToStatements(pWrap.adder() + "(" + pWrap.javaBaseTypePath().getLast() + ".valueOf(enumLiteral))");
                        ifNotNull.addToThenPart(ojForStatement);
                    } else if (pWrap.isMany() && pWrap.getDataTypeEnum() != null) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("value", new OJPathName("String"), pWrap.fieldname());

                        if (pWrap.isDate()) {
                            ojForStatement.getBody().addToStatements(pWrap.adder() + "(" + UmlgGenerationUtil.umlgFormatter.getLast() + ".parseDate(value))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isDateTime()) {
                            ojForStatement.getBody().addToStatements(pWrap.adder() + "(" + UmlgGenerationUtil.umlgFormatter.getLast() + ".parseDateTime(value))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isTime()) {
                            ojForStatement.getBody().addToStatements(pWrap.adder() + "(" + UmlgGenerationUtil.umlgFormatter.getLast() + ".parseTime(value))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else {
                            ojForStatement.getBody().addToStatements(pWrap.adder() + "(value)");
                        }
                        ifNotNull.addToThenPart(ojForStatement);
                    } else if (pWrap.isMany() && pWrap.isDataType()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement;
                        if (pWrap.isNumber()) {
                            ojForStatement = new OJForStatement("value", new OJPathName("Number"), pWrap.fieldname());

                            if (pWrap.isInteger()) {
                                ojForStatement.getBody().addToStatements(pWrap.adder() + "(value.intValue())");
                            } else if (pWrap.isUnlimitedNatural()) {
                                ojForStatement.getBody().addToStatements(pWrap.adder() + "(value.intValue())");
                            } else if (pWrap.isFloat()) {
                                ojForStatement.getBody().addToStatements(pWrap.adder() + "(value.floatValue())");
                            } else if (pWrap.isDouble()) {
                                ojForStatement.getBody().addToStatements(pWrap.adder() + "(value.doubleValue())");
                            } else if (pWrap.isLong()) {
                                ojForStatement.getBody().addToStatements(pWrap.adder() + "(value.longValue())");
                            } else if (pWrap.isShort()) {
                                ojForStatement.getBody().addToStatements(pWrap.adder() + "(value.shortValue())");
                            } else if (pWrap.isByte()) {
                                ojForStatement.getBody().addToStatements(pWrap.adder() + "(value.byteValue())");
                            } else {
                                throw new RuntimeException("Not yet implemented!");
                            }

                        } else {
                            ojForStatement = new OJForStatement("value", pWrap.javaBaseTypePath(), pWrap.fieldname());
                            ojForStatement.getBody().addToStatements(pWrap.adder() + "(" + pWrap.javaBaseTypePath().getLast() + ".valueOf(value))");
                        }
                        ifNotNull.addToThenPart(ojForStatement);
                    } else {
                        ifNotNull.addToThenPart(pWrap.setter() + "(" + field.getName() + ")");
                    }
                    ifNotNull.addToElsePart(pWrap.setter() + "(null)");
                }
            }
        }
    }

    private void addFromJsonNonCompositeOne(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJsonNonCompositeOne");
        fromJson.addParam("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        fromJson.setVisibility(OJVisibilityKindGEN.PUBLIC);
        UmlgGenerationUtil.addOverrideAnnotation(fromJson);
        annotatedClass.addToOperations(fromJson);
        if (!clazz.getGenerals().isEmpty()) {
            fromJson.getBody().addToStatements("super.fromJsonNonCompositeOne(propertyMap)");
        }
        List<Property> propertiesForToJson = new ArrayList<Property>(UmlgClassOperations.getOneProperties(clazz));
        if (!propertiesForToJson.isEmpty()) {
            for (Property property : propertiesForToJson) {
                if (!property.isReadOnly()) {
                    PropertyWrapper pWrap = new PropertyWrapper(property);
                    OJField field;
                    if (pWrap.isMany()) {
                        if (pWrap.isPrimitive()) {
                            field = new OJField(pWrap.fieldname(), pWrap.javaTypePath());
                            field.setInitExp("new " + pWrap.javaTumlMemoryTypePath().getLast() + "((Collection<" + pWrap.javaBaseTypePath()
                                    + ">)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
                        } else if (pWrap.isEnumeration()) {
                            field = new OJField(pWrap.fieldname(), "Collection<String>");
                            field.setInitExp("(Collection<String>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            annotatedClass.addToImports(pWrap.javaTumlMemoryTypePath());
                            annotatedClass.addToImports(new OJPathName("java.util.Collection"));
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
                        field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
                        field.setInitExp(pWrap.javaBaseTypePath().getLast() + ".fromJson((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                    } else {
                        if (pWrap.isNumber()) {
                            OJField fieldNumber = new OJField(pWrap.getName() + "AsNumber", new OJPathName("Number"));
                            fieldNumber.setInitExp("(" + new OJPathName("Number") + ")propertyMap.get(\"" + pWrap.getName() + "\")");
                            field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
                            if (pWrap.isInteger()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".intValue() : null");
                            } else if (pWrap.isLong()) {
                                field.setInitExp(fieldNumber.getName() + " != null ? " + fieldNumber.getName() + ".longValue() : null");
                            } else {
                                throw new RuntimeException("Not yet implemented!");
                            }
                            fromJson.getBody().addToLocals(fieldNumber);
                        } else if (!pWrap.isDataType()) {
                            // Primitives are data types
                            field = new OJField(pWrap.getName() + "Map", new OJPathName("Map<String, Object>"));
                            field.setInitExp("(Map<String, Object>)propertyMap.get(\"" + pWrap.getName() + "\")");
                            UmlgGenerationUtil.addSuppressWarning(fromJson);
                        } else if (pWrap.isDate()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Date.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".parseDate((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isDateTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.DateTime.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".parseDateTime((String)propertyMap.get(\"" + pWrap.getName()
                                    + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Time.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".parseTime((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else if (pWrap.isImage()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Image.getPathName());
                            field.setInitExp(UmlgGenerationUtil.umlgFormatter.getLast() + ".decode((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(UmlgGenerationUtil.umlgFormatter);
                        } else {
                            field = new OJField(pWrap.getName(), pWrap.javaBaseTypePath());
                            field.setInitExp("(" + pWrap.javaBaseTypePath() + ")propertyMap.get(\"" + pWrap.getName() + "\")");
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

                            //TODO maybe this should be a remover instead???
                            ifSetToNull = new OJIfStatement(field.getName() + ".isEmpty() || " + field.getName() + ".get(\"id\") == null",
                                    pWrap.setter() + "(null, null)");

                            //Validate that association class json is in the map
                            OJIfStatement ifMapHasAssociationClassJson = new OJIfStatement("propertyMap.get(\"" + pWrap.getAssociationClassFakePropertyName() + "\") == null");
                            ifMapHasAssociationClassJson.addToThenPart("throw new IllegalStateException(\"Association class " + pWrap.getAssociationClassFakePropertyName() + " must be in the map!\")");
                            ifSetToNull.addToElsePart(ifMapHasAssociationClassJson);

                            //Create an association class instance
                            ifSetToNull.addToElsePart(pWrap.getAssociationClassPathName().getLast() + " " + pWrap.getAssociationClassFakePropertyName() + " = new "
                                    + pWrap.getAssociationClassPathName().getLast() + "(true)");
                            ifSetToNull.addToElsePart(pWrap.getAssociationClassFakePropertyName() + ".fromJson((Map<String, Object>) propertyMap.get(\"" + pWrap.getAssociationClassFakePropertyName() + "\"))");
                            ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")" + UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getEntity + "((" + field.getName() + ".get(\"id\"))), " + pWrap.getAssociationClassFakePropertyName() + ")");

                            ifSetToNull.addToElsePart("//Store the association class property name in a ThreadVar.");
                            ifSetToNull.addToElsePart("//The corresponding toJson checks the ThreadVar to know whether it should return this association class's data");
                            ifSetToNull.addToElsePart(UmlgGenerationUtil.UmlgAssociationClassManager.getLast() + ".INSTANCE.put(\"" + pWrap.getAssociationClassFakePropertyName() + "\", " + pWrap.getter() + "().getId())");

                        } else {
                            ifSetToNull = new OJIfStatement(field.getName() + ".isEmpty() || " + field.getName() + ".get(\"id\") == null",
                                    pWrap.setter() + "(null)");
                            ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")" + UmlgGenerationUtil.UMLGAccess + "." + UmlgGenerationUtil.getEntity + "(("
                                    + pWrap.fieldname() + "Map.get(\"id\"))))");
                        }
                        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
                        ifNotNull.addToThenPart(ifSetToNull);
                        fromJson.getBody().addToStatements(ifInMap);


                    } else if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isComponent()) {

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ifNotNull.getThenPart().addToLocals(component);
                        OJIfStatement ojIfStatement = new OJIfStatement(pWrap.fieldname() + "Map.get(\"id\") != null");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + UmlgGenerationUtil.UMLGAccess
                                + "." + UmlgGenerationUtil.getEntity + "((" + pWrap.fieldname() + "Map.get(\"id\")))");
                        annotatedClass.addToImports(UmlgGenerationUtil.UMLGPathName);
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = UmlgSchemaFactory.getUmlgSchemaMap().get((String)" + pWrap.fieldname() + "Map.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(UmlgGenerationUtil.UmlgSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ifNotNull.addToThenPart(ojIfStatement);
                        ifNotNull.addToThenPart(pWrap.fieldname() + ".fromJsonNonCompositeOne(" + pWrap.fieldname() + "Map)");


                    } else if (pWrap.isOne() && pWrap.isDataType()) {
                        // Enumeration is a DataType
                        ifNotNull.addToThenPart(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                    } else if (pWrap.isMany() && !pWrap.isDataType() && !pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Integer>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.javaBaseTypePath().getLast() + " " + pWrap.fieldname() + " = " + UmlgGenerationUtil.UMLGAccess
                                + "." + UmlgGenerationUtil.getEntity + "(row.get(\"id\"))");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementConstructor);
                        OJSimpleStatement ojSimpleStatementFromJson = new OJSimpleStatement(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementFromJson);
                    } else if (pWrap.isMany() && !pWrap.isDataType() && pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");

                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Object>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ojForStatement.getBody().addToLocals(component);
                        OJField idField = new OJField("idField", "String");
                        idField.setInitExp("(String)row.get(\"id\")");
                        ojForStatement.getBody().addToLocals(idField);
                        OJIfStatement ojIfStatement = new OJIfStatement("idField != null && !idField.startsWith(\"fake\")");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + UmlgGenerationUtil.UMLGAccess
                                + "." + UmlgGenerationUtil.getEntity + "((row.get(\"id\")))");
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = UmlgSchemaFactory.getUmlgSchemaMap().get((String)row.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(UmlgGenerationUtil.UmlgSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ojForStatement.getBody().addToStatements(ojIfStatement);
                        ojForStatement.getBody().addToStatements(pWrap.fieldname() + ".fromJsonNonCompositeOne(row)");
                    } else if (pWrap.isMany() && pWrap.isEnumeration()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("enumLiteral", new OJPathName("String"), pWrap.fieldname());
                        ojForStatement.getBody().addToStatements(pWrap.adder() + "(" + pWrap.javaBaseTypePath().getLast() + ".valueOf(enumLiteral))");
                        ifNotNull.addToThenPart(ojForStatement);
                    } else if (pWrap.isMany() && pWrap.isDataType()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("value", pWrap.javaBaseTypePath(), pWrap.fieldname());
                        ojForStatement.getBody().addToStatements(pWrap.adder() + "(" + pWrap.javaBaseTypePath().getLast() + ".valueOf(value))");
                        ifNotNull.addToThenPart(ojForStatement);
                    } else {
                        ifNotNull.addToThenPart(pWrap.setter() + "(" + field.getName() + ")");
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
