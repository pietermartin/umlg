package org.umlg.restlet.visitor.model;

import org.eclipse.uml2.uml.Model;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.java.metamodel.annotation.OJAnnotationValue;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.restlet.util.UmlgRestletGenerationUtil;
import org.umlg.restlet.visitor.clazz.BaseServerResourceBuilder;

public class EnumLookupResourceServerResourceBuilder extends BaseServerResourceBuilder implements Visitor<Model> {

	public EnumLookupResourceServerResourceBuilder(Workspace workspace, String sourceDir) {
		super(workspace, sourceDir);
	}

	@Override
	public void visitBefore(Model model) {
		OJAnnotatedInterface enumLookupInf = new OJAnnotatedInterface(UmlgRestletGenerationUtil.EnumerationLookupServerResource.getLast());
		OJPackage ojPackage = new OJPackage(UmlgGenerationUtil.UmlgRootPackage.toJavaString());
		enumLookupInf.setMyPackage(ojPackage);
		addToSource(enumLookupInf);

		OJAnnotatedClass enumLookup = new OJAnnotatedClass(UmlgRestletGenerationUtil.EnumerationLookupServerResouceImpl.getLast());
		enumLookup.setMyPackage(ojPackage);
		enumLookup.addToImplementedInterfaces(UmlgRestletGenerationUtil.EnumerationLookupServerResource);
		enumLookup.setSuperclass(UmlgRestletGenerationUtil.ServerResource);
		addToSource(enumLookup);
		
		addDefaultConstructor(enumLookup);
		
		addGetRepresentation(enumLookupInf, enumLookup);
		
		addToRouterEnum(model, enumLookup, "ENUM_LOOKUP", "\"/tumlEnumLookup\"");
	}

	@Override
	public void visitAfter(Model clazz) {
	}

	private void addGetRepresentation(OJAnnotatedInterface enumLookupInf, OJAnnotatedClass enumLookup) {
		OJAnnotatedOperation getInf = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
		enumLookupInf.addToOperations(getInf);
		getInf.addAnnotationIfNew(new OJAnnotationValue(UmlgRestletGenerationUtil.Get, "json"));
		enumLookupInf.addToOperations(getInf);
		
		OJAnnotatedOperation get = new OJAnnotatedOperation("get", UmlgRestletGenerationUtil.Representation);
		get.addToThrows(UmlgRestletGenerationUtil.ResourceException);
		enumLookup.addToImports(UmlgRestletGenerationUtil.ResourceException);
		UmlgGenerationUtil.addOverrideAnnotation(get);
		enumLookup.addToOperations(get);
		
		get.getBody().addToStatements("String enumQualifiedName = getQuery().getFirst(\"enumQualifiedName\").getValue();");
		get.getBody().addToStatements("Class<?> enumClass = " + UmlgGenerationUtil.QualifiedNameClassMap.getLast() + ".INSTANCE.get(enumQualifiedName)");
		enumLookup.addToImports(UmlgGenerationUtil.QualifiedNameClassMap);

		get.getBody().addToStatements(UmlgGenerationUtil.UmlgEnum.getLast() + "[] enumConstants = (" + UmlgGenerationUtil.UmlgEnum.getLast() + "[])enumClass.getEnumConstants()");
		enumLookup.addToImports(UmlgGenerationUtil.UmlgEnum);
		get.getBody().addToStatements("StringBuilder json = new StringBuilder()");
		get.getBody().addToStatements("json.append(\"{\\\"data\\\": [\")");
		get.getBody().addToStatements("json.append(" + UmlgGenerationUtil.ToJsonUtil.getLast() + ".enumsToJson(Arrays.asList(enumConstants)))");
		enumLookup.addToImports("java.util.Arrays");
		enumLookup.addToImports(UmlgGenerationUtil.ToJsonUtil);
		get.getBody().addToStatements("json.append(\"]}\")");
		get.getBody().addToStatements("return new " + UmlgRestletGenerationUtil.JsonRepresentation.getLast() + "(json.toString())");
		enumLookup.addToImports(UmlgRestletGenerationUtil.JsonRepresentation);
	}

}
