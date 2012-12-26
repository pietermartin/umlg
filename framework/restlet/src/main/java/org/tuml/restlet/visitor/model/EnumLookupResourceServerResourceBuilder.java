package org.tuml.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedInterface;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.annotation.OJAnnotationValue;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.restlet.util.TumlRestletGenerationUtil;
import org.tuml.restlet.visitor.clazz.BaseServerResourceBuilder;

public class EnumLookupResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Model> {

	public EnumLookupResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedInterface enumLookupInf = new OJAnnotatedInterface(TumlRestletGenerationUtil.EnumerationLookupServerResource.getLast());
		OJPackage ojPackage = new OJPackage(TinkerGenerationUtil.TumlRootPackage.toJavaString());
		enumLookupInf.setMyPackage(ojPackage);
		addToSource(enumLookupInf);

		OJAnnotatedClass enumLookup = new OJAnnotatedClass(TumlRestletGenerationUtil.EnumerationLookupServerResouceImpl.getLast());
		enumLookup.setMyPackage(ojPackage);
		enumLookup.addToImplementedInterfaces(TumlRestletGenerationUtil.EnumerationLookupServerResource);
		enumLookup.setSuperclass(TumlRestletGenerationUtil.ServerResource);
		addToSource(enumLookup);
		
		addDefaultConstructor(enumLookup);
		
		addGetRepresentation(enumLookupInf, enumLookup);
		
		addToRouterEnum(model, enumLookup, "ENUM_LOOKUP", "\"/tumlEnumLookup\"");
	}

	@Override
	public void visitAfter(Model clazz) {
	}

	private void addGetRepresentation(OJAnnotatedInterface enumLookupInf, OJAnnotatedClass enumLookup) {
		OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		enumLookupInf.addToOperations(getInf);
		getInf.addAnnotationIfNew(new OJAnnotationValue(TumlRestletGenerationUtil.Get, "json"));
		enumLookupInf.addToOperations(getInf);
		
		OJAnnotatedOperation get = new OJAnnotatedOperation("get", TumlRestletGenerationUtil.Representation);
		get.addToThrows(TumlRestletGenerationUtil.ResourceException);
		enumLookup.addToImports(TumlRestletGenerationUtil.ResourceException);
		TinkerGenerationUtil.addOverrideAnnotation(get);
		enumLookup.addToOperations(get);
		
		get.getBody().addToStatements("String enumQualifiedName = getQuery().getFirst(\"enumQualifiedName\").getValue();");
		get.getBody().addToStatements("Class<?> enumClass = " + TinkerGenerationUtil.QualifiedNameClassMap.getLast() + ".INSTANCE.get(enumQualifiedName)");
		enumLookup.addToImports(TinkerGenerationUtil.QualifiedNameClassMap);

		get.getBody().addToStatements(TinkerGenerationUtil.TumlEnum.getLast() + "[] enumConstants = (" + TinkerGenerationUtil.TumlEnum.getLast() + "[])enumClass.getEnumConstants()");
		enumLookup.addToImports(TinkerGenerationUtil.TumlEnum);
		get.getBody().addToStatements("StringBuilder json = new StringBuilder()");
		get.getBody().addToStatements("json.append(\"{\\\"data\\\": [\")");
		get.getBody().addToStatements("json.append(" + TinkerGenerationUtil.ToJsonUtil.getLast() + ".enumsToJson(Arrays.asList(enumConstants)))");
		enumLookup.addToImports("java.util.Arrays");
		enumLookup.addToImports(TinkerGenerationUtil.ToJsonUtil);
		get.getBody().addToStatements("json.append(\"]}\")");
		get.getBody().addToStatements("return new " + TumlRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
		enumLookup.addToImports(TumlRestletGenerationUtil.JsonRepresentation);
	}

}
