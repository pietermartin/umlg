package org.tuml.ocl.test;

import java.io.File;
import java.net.URL;
import java.util.logging.LogManager;

import junit.framework.Assert;

import org.apache.commons.lang.time.StopWatch;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.uml.IteratorExp;
import org.eclipse.ocl.uml.OperationCallExp;
import org.eclipse.ocl.uml.PropertyCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opaeum.java.metamodel.OJConstructor;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.ruml.runtime.groovy.TumlGroovyShell;
import org.tuml.framework.ModelLoader;
import org.tuml.javageneration.ocl.TumlOcl2Java;
import org.tuml.javageneration.ocl.util.TumlCollectionKindEnum;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.OperationWrapper;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.ocl.TumlOcl2Parser;
import org.tuml.qualifier.Bank;
import org.tuml.qualifier.Customer;
import org.tuml.qualifier.Employee;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.test.BaseLocalDbTest;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestRuntimeOclGeneration extends BaseLocalDbTest {

	@BeforeClass
	public static void beforeClass() {
		try {
			URL url = BaseLocalDbTest.class.getResource("/logging.properties");
			LogManager.getLogManager().readConfiguration(url.openStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ModelLoader.loadModel(new File("src/main/model/test-ocl.uml"));
		TumlOcl2Parser instance = TumlOcl2Parser.INSTANCE;
	}

	@Test
	public void test() {
		db.startTransaction();
		Bank bank = new Bank(true);
		bank.setName("thebank");
		Employee employee1 = new Employee(true);
		employee1.setName("employee1");
		bank.addToEmployee(employee1);
		Employee employee2 = new Employee(true);
		employee2.setName("employee2");
		bank.addToEmployee(employee2);
		Employee employee3 = new Employee(true);
		employee3.setName("employee3");
		bank.addToEmployee(employee3);
		db.stopTransaction(Conclusion.SUCCESS);

		Classifier classifier = (Classifier) ModelLoader.findNamedElement("testoclmodel::org::tuml::qualifier::Bank");
		OJAnnotatedClass oclClass = new OJAnnotatedClass("OclQuery");
		oclClass.setSuperclass(TumlClassOperations.getPathName(classifier));
		OJPackage ojPackage = new OJPackage(Namer.name(classifier.getNearestPackage()));
		oclClass.setMyPackage(ojPackage);
		oclClass.setVisibility(TumlClassOperations.getVisibility(classifier.getVisibility()));
		OJConstructor constructor = new OJConstructor();
		constructor.addParam("vertex", TinkerGenerationUtil.vertexPathName);
		constructor.getBody().addToStatements("super(vertex)");
		oclClass.addToConstructors(constructor);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TumlOcl2Parser.INSTANCE.getHelper().setContext(classifier);
		try {
			OCLExpression<Classifier> expr = TumlOcl2Parser.INSTANCE.getHelper().createQuery("self.employee->select(name='employee3')");
			OJAnnotatedOperation getter = new OJAnnotatedOperation("execute");
			getter.setReturnType(TumlOcl2Java.calcReturnType(expr));
			getter.getBody().addToStatements("return " + TumlOcl2Java.oclToJava(oclClass, expr));
			oclClass.addToOperations(getter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		String javaString = oclClass.toJavaString();
		System.out.println(javaString);
		Object result = TumlGroovyShell.executeQuery(javaString, bank.getVertex());
		Assert.assertTrue(result instanceof TinkerOrderedSet<?>);
		TinkerOrderedSet result2 = (TinkerOrderedSet<Employee>)result;
		Assert.assertEquals(1, result2.size());
	}



}
