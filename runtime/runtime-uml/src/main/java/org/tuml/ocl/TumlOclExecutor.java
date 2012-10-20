package org.tuml.ocl;

import org.apache.commons.lang.time.StopWatch;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.ruml.runtime.groovy.TumlGroovyShell;
import org.tuml.framework.ModelLoader;
import org.tuml.javageneration.ocl.TumlOcl2Java;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.runtime.domain.TumlNode;

public class TumlOclExecutor {

	public static Object executeOclQuery(String contextQualifiedName, TumlNode contextTumlNode, String query) {
		Classifier contextClassifier = (Classifier) ModelLoader.findNamedElement(contextQualifiedName);
		OJAnnotatedClass oclClass = new OJAnnotatedClass("OclQuery");
		oclClass.setSuperclass(TumlClassOperations.getPathName(contextClassifier));
		OJPackage ojPackage = new OJPackage(Namer.name(contextClassifier.getNearestPackage()));
		oclClass.setMyPackage(ojPackage);
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
		constructor.getBody().addToStatements("super(vertex)");
		oclClass.addToConstructors(constructor);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TumlOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
		try {
			OCLExpression<Classifier> expr = TumlOcl2Parser.INSTANCE.getHelper().createQuery(query);
			OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
			getter.setReturnType(TumlOcl2Java.calcReturnType(expr));
			getter.getBody().addToStatements("return " + TumlOcl2Java.oclToJava(oclClass, expr));
			oclClass.addToOperations(getter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		String javaString = oclClass.toJavaString();
		Object result = TumlGroovyShell.executeQuery(javaString, contextTumlNode.getVertex());
		return result;
	}
}
