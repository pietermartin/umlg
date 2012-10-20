package org.tuml.ocl.test;

import java.io.File;
import java.net.URL;
import java.util.logging.LogManager;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tuml.framework.ModelLoader;
import org.tuml.ocl.TumlOcl2Parser;
import org.tuml.ocl.TumlOclExecutor;
import org.tuml.qualifier.Bank;
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
		@SuppressWarnings("unused")
		TumlOcl2Parser instance = TumlOcl2Parser.INSTANCE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

		Object result = TumlOclExecutor.executeOclQuery("testoclmodel::org::tuml::qualifier::Bank", bank, "self.employee->select(name='employee3')");
		Assert.assertTrue(result instanceof TinkerOrderedSet<?>);
		TinkerOrderedSet result2 = (TinkerOrderedSet<Employee>)result;
		Assert.assertEquals(1, result2.size());
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testTuples() {
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

		Object result = TumlOclExecutor.executeOclQuery("testoclmodel::org::tuml::qualifier::Bank", bank, "Tuple{name: String = name}");
		System.out.println(result);
	}


}
