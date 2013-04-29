package org.tuml.javageneration.visitor.clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.*;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedField;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJAnnotationValue;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.java.metamodel.generated.OJVisibilityKindGEN;
import org.tuml.javageneration.util.DataTypeEnum;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ToFromJsonCreator extends BaseVisitor implements Visitor<Class> {

    public static final String URI_FOR_RESTFULL = "uri";

    public ToFromJsonCreator(Workspace workspace) {
        super(workspace);
    }

    @Override
    public void visitBefore(Class clazz) {
//        addToJson(clazz, "toJson", TumlClassOperations.getPrimitiveOrEnumOrComponentsProperties(clazz));
        addToJson(clazz, "toJson", TumlClassOperations.getPropertiesForToJson(clazz));
        addToJson(clazz, "toJsonWithoutCompositeParent", TumlClassOperations.getPropertiesForToJsonExcludingCompositeParent(clazz));
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
        TinkerGenerationUtil.addOverrideAnnotation(toJsonSimple);
        toJsonSimple.getBody().addToStatements("return " + operationName + "(false)");
        annotatedClass.addToOperations(toJsonSimple);

        OJAnnotatedOperation toJson = new OJAnnotatedOperation(operationName, new OJPathName("String"));
        TinkerGenerationUtil.addOverrideAnnotation(toJson);
        toJson.addParam("deep", "Boolean");
        if (clazz.getGenerals().isEmpty()) {
            toJson.getBody().addToStatements("StringBuilder sb = new StringBuilder()");
            toJson.getBody().addToStatements("sb.append(\"\\\"id\\\": \" + getId() + \", \")");
            if (this.workspace.containsVisitor(MetaClassBuilder.class)) {
                toJson.getBody().addToStatements("sb.append(\"\\\"metaNodeId\\\": \" + getMetaNode().getId() + \", \")");
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
                ifDeep.addToThenPart("sb.append(\"\\\"" + pWrap.getName() + "\\\": [\" + " + TinkerGenerationUtil.ToJsonUtil.getLast() + "." + operationName + "("
                        + pWrap.getter() + "(), true) + \"]" + "\")");
                ifDeep.addToElsePart("sb.delete(sb.length() - 2, sb.length())");
                annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
                toJson.getBody().addToStatements(ifDeep);
            } else if (pWrap.isOne() && pWrap.isComponent()) {
                ifDeep = new OJIfStatement("deep");
                ifDeep.addToThenPart("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() == null ? \"null\" : " + pWrap.getter() + "()." + operationName + "(true)) + \"" + "\")");
                ifDeep.addToElsePart("sb.delete(sb.length() - 2, sb.length())");
                annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
                toJson.getBody().addToStatements(ifDeep);
            } else if (pWrap.isMany() && pWrap.isPrimitive()) {
                toJson.getBody().addToStatements(
                        "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + " + TinkerGenerationUtil.ToJsonUtil.getLast() + ".primitivesToJson("
                                + pWrap.getter() + "()) + \"" + "\")");
                annotatedClass.addToImports(TinkerGenerationUtil.ToJsonUtil);
            } else if (pWrap.isEnumeration()) {
                if (pWrap.isMany()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() == null ? null : " + pWrap.getter()
                                    + "().toJson()" + "))");
                } else {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() == null ? null : \"\\\"\" + " + pWrap.getter()
                                    + "().toJson() + \"\\\"" + "\"))");
                }
            } else {
                if (pWrap.isNumber() || pWrap.isBoolean()) {
                    toJson.getBody().addToStatements("sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + " + pWrap.getter() + "() + \"" + "\")");
                } else if (!pWrap.isDataType()) {

                    // TODO need to implement display name interface or
                    // something
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"{\\\"id\\\": \" + " + pWrap.getter()
                                    + "().getId() + \", \\\"displayName\\\": \\\"\" + " + pWrap.getter()
                                    + "().getName() + \"\\\"}\" : \"{\\\"id\\\": \" + null + \", \\\"displayName\\\": \" + null + \"}\") + \"" + "\")");

                } else if (pWrap.isDateTime()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                    + TinkerGenerationUtil.tumlFormatter.getLast() + ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                } else if (pWrap.isDate()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                    + TinkerGenerationUtil.tumlFormatter.getLast() + ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                } else if (pWrap.isTime()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                    + TinkerGenerationUtil.tumlFormatter.getLast() + ".format(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                } else if (pWrap.isImage()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                    + TinkerGenerationUtil.tumlFormatter.getLast() + ".encode(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                } else if (pWrap.isVideo()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + "
                                    + TinkerGenerationUtil.tumlFormatter.getLast() + ".encode(" + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                } else if (pWrap.isAudio()) {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + TinkerFormatter.encode("
                                    + pWrap.getter() + "()) + \"\\\"\" : null " + "))");
                    annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                } else {
                    toJson.getBody().addToStatements(
                            "sb.append(\"\\\"" + pWrap.getName() + "\\\": \" + (" + pWrap.getter() + "() != null ? \"\\\"\" + " + pWrap.getter()
                                    + "() + \"\\\"\" : null " + "))");
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
        TinkerGenerationUtil.addOverrideAnnotation(fromJson);
        annotatedClass.addToOperations(fromJson);

        OJField objectMapper = new OJField("mapper", TinkerGenerationUtil.ObjectMapper);
        objectMapper.setInitExp("new ObjectMapper()");
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
        TinkerGenerationUtil.addOverrideAnnotation(fromJson);
        annotatedClass.addToOperations(fromJson);
        fromJson.getBody().addToStatements("fromJsonDataTypeAndComposite(propertyMap)");
        fromJson.getBody().addToStatements("fromJsonNonCompositeOne(propertyMap)");
    }

    private void addFromJsonDataTypeAndComposite(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJsonDataTypeAndComposite");
        fromJson.addParam("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        fromJson.setVisibility(OJVisibilityKindGEN.PROTECTED);
        annotatedClass.addToOperations(fromJson);
        if (!clazz.getGenerals().isEmpty()) {
            TinkerGenerationUtil.addOverrideAnnotation(fromJson);
            fromJson.getBody().addToStatements("super.fromJsonDataTypeAndComposite(propertyMap)");
        }
        List<Property> propertiesForToJson = new ArrayList<Property>(TumlClassOperations.getPrimitiveOrEnumOrComponentsExcludeOneProperties(clazz));
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
                            TinkerGenerationUtil.addSuppressWarning(fromJson);
                        } else if (pWrap.isDate()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Date.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseDate((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                        } else if (pWrap.isDateTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.DateTime.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseDateTime((String)propertyMap.get(\"" + pWrap.getName()
                                    + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                        } else if (pWrap.isTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Time.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseTime((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                        } else if (pWrap.isImage()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Image.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".decode((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
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
                        ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")" + TinkerGenerationUtil.graphDbAccess + ".instantiateClassifier(((Number)"
                                + pWrap.fieldname() + "Map.get(\"id\")).longValue()))");
                        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
                        ifNotNull.addToThenPart(ifSetToNull);
                        fromJson.getBody().addToStatements(ifInMap);


                    } else if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isComponent()) {

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ifNotNull.getThenPart().addToLocals(component);
                        OJIfStatement ojIfStatement = new OJIfStatement(pWrap.fieldname() + "Map.get(\"id\") instanceof Number");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(((Number)"+pWrap.fieldname()+"Map.get(\"id\")).longValue())");
                        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = TumlSchemaFactory.getTumlSchemaMap().get((String)"+pWrap.fieldname()+"Map.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(TinkerGenerationUtil.TumlSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ifNotNull.addToThenPart(ojIfStatement);
                        ifNotNull.addToThenPart(pWrap.fieldname() + ".fromJson("+pWrap.fieldname()+"Map)");



                    } else if (pWrap.isOne() && pWrap.isDataType()) {
                        // Enumeration is a DataType
                        ifNotNull.addToThenPart(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                    } else if (pWrap.isMany() && !pWrap.isDataType() && !pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Integer>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.javaBaseTypePath().getLast() + " " + pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(Long.valueOf(row.get(\"id\")))");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementConstructor);
                        OJSimpleStatement ojSimpleStatementFromJson = new OJSimpleStatement(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementFromJson);
                    } else if (pWrap.isMany() && !pWrap.isDataType() && pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");

                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Object>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ojForStatement.getBody().addToLocals(component);
                        OJIfStatement ojIfStatement = new OJIfStatement("row.get(\"id\") instanceof Number");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(((Number)row.get(\"id\")).longValue())");
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = TumlSchemaFactory.getTumlSchemaMap().get((String)row.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(TinkerGenerationUtil.TumlSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ojForStatement.getBody().addToStatements(ojIfStatement);
                        ojForStatement.getBody().addToStatements(pWrap.fieldname() + ".fromJson(row)");
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
                    ifNotNull.addToElsePart(pWrap.setter() + "(null)");
                }
            }
        }
    }

    private void addFromJsonNonCompositeOne(Class clazz) {
        OJAnnotatedClass annotatedClass = findOJClass(clazz);
        OJAnnotatedOperation fromJson = new OJAnnotatedOperation("fromJsonNonCompositeOne");
        fromJson.addParam("propertyMap", new OJPathName("java.util.Map").addToGenerics("String").addToGenerics("Object"));
        fromJson.setVisibility(OJVisibilityKindGEN.PROTECTED);
        annotatedClass.addToOperations(fromJson);
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
                            TinkerGenerationUtil.addSuppressWarning(fromJson);
                        } else if (pWrap.isDate()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Date.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseDate((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                        } else if (pWrap.isDateTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.DateTime.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseDateTime((String)propertyMap.get(\"" + pWrap.getName()
                                    + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                        } else if (pWrap.isTime()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Time.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".parseTime((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
                        } else if (pWrap.isImage()) {
                            field = new OJField(pWrap.getName(), DataTypeEnum.Image.getPathName());
                            field.setInitExp(TinkerGenerationUtil.tumlFormatter.getLast() + ".decode((String)propertyMap.get(\"" + pWrap.getName() + "\"))");
                            annotatedClass.addToImports(TinkerGenerationUtil.tumlFormatter);
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
                        ifSetToNull.addToElsePart(pWrap.setter() + "((" + pWrap.javaBaseTypePath().getLast() + ")" + TinkerGenerationUtil.graphDbAccess + ".instantiateClassifier(((Number)"
                                + pWrap.fieldname() + "Map.get(\"id\")).longValue()))");
                        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
                        ifNotNull.addToThenPart(ifSetToNull);
                        fromJson.getBody().addToStatements(ifInMap);


                    } else if (pWrap.isOne() && !pWrap.isDataType() && !pWrap.isEnumeration() && pWrap.isComponent()) {

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ifNotNull.getThenPart().addToLocals(component);
                        OJIfStatement ojIfStatement = new OJIfStatement(pWrap.fieldname() + "Map.get(\"id\") instanceof Number");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(((Number)"+pWrap.fieldname()+"Map.get(\"id\")).longValue())");
                        annotatedClass.addToImports(TinkerGenerationUtil.graphDbPathName);
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = TumlSchemaFactory.getTumlSchemaMap().get((String)"+pWrap.fieldname()+"Map.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(TinkerGenerationUtil.TumlSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ifNotNull.addToThenPart(ojIfStatement);
                        ifNotNull.addToThenPart(pWrap.fieldname() + ".fromJson("+pWrap.fieldname()+"Map)");



                    } else if (pWrap.isOne() && pWrap.isDataType()) {
                        // Enumeration is a DataType
                        ifNotNull.addToThenPart(pWrap.setter() + "(" + pWrap.fieldname() + ")");
                    } else if (pWrap.isMany() && !pWrap.isDataType() && !pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");
                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Integer>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.javaBaseTypePath().getLast() + " " + pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(Long.valueOf(row.get(\"id\")))");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementConstructor);
                        OJSimpleStatement ojSimpleStatementFromJson = new OJSimpleStatement(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojForStatement.getBody().addToStatements(ojSimpleStatementFromJson);
                    } else if (pWrap.isMany() && !pWrap.isDataType() && pWrap.isComponent()) {
                        ifNotNull.addToThenPart(pWrap.clearer() + "()");

                        OJForStatement ojForStatement = new OJForStatement("row", new OJPathName("Map<String,Object>"), pWrap.fieldname() + "Map");
                        ifNotNull.addToThenPart(ojForStatement);

                        OJField component = new OJField(pWrap.fieldname(), pWrap.javaBaseTypePath());
                        ojForStatement.getBody().addToLocals(component);
                        OJIfStatement ojIfStatement = new OJIfStatement("row.get(\"id\") instanceof Number");
                        OJSimpleStatement ojSimpleStatementConstructor = new OJSimpleStatement(pWrap.fieldname() + " = " + TinkerGenerationUtil.graphDbAccess
                                + ".instantiateClassifier(((Number)row.get(\"id\")).longValue())");
                        ojIfStatement.addToThenPart(ojSimpleStatementConstructor);

                        ojIfStatement.addToElsePart("Class<" + pWrap.javaBaseTypePath().getLast() + "> baseTumlClass = TumlSchemaFactory.getTumlSchemaMap().get((String)row.get(\"qualifiedName\"))");
                        OJTryStatement ojTryStatement = new OJTryStatement();
                        ojTryStatement.getTryPart().addToStatements("Constructor<" + pWrap.javaBaseTypePath().getLast() + "> constructor = baseTumlClass.getConstructor(Boolean.class)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.fieldname() + " = constructor.newInstance(true)");
                        ojTryStatement.getTryPart().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ")");
                        ojTryStatement.setCatchParam(new OJParameter("e", "java.lang.Exception"));
                        ojTryStatement.getCatchPart().addToStatements("throw new RuntimeException(e)");

                        annotatedClass.addToImports(TinkerGenerationUtil.TumlSchemaFactory);
                        annotatedClass.addToImports("java.lang.reflect.Constructor");

                        ojIfStatement.addToElsePart(ojTryStatement);

                        ojForStatement.getBody().addToStatements(ojIfStatement);
                        ojForStatement.getBody().addToStatements(pWrap.fieldname() + ".fromJson(row)");
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
                    ifNotNull.addToElsePart(pWrap.setter() + "(null)");
                }
            }
        }
    }


}
