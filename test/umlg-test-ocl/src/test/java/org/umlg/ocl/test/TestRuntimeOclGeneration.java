package org.umlg.ocl.test;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.logging.LogManager;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.umlg.framework.ModelLoader;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.ocl.TumlOclExecutor;
import org.umlg.qualifier.Bank;
import org.umlg.qualifier.Employee;
import org.umlg.runtime.collection.TinkerOrderedSet;
import org.umlg.runtime.collection.TinkerSequence;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.test.BaseLocalDbTest;

public class TestRuntimeOclGeneration extends BaseLocalDbTest {

	@BeforeClass
	public static void beforeClass() {
		try {
			URL url = BaseLocalDbTest.class.getResource("/logging.properties");
			LogManager.getLogManager().readConfiguration(url.openStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ModelLoader.INSTANCE.loadModel(new File("./src/main/model/test-ocl.uml"));
		@SuppressWarnings("unused")
        UmlgOcl2Parser instance = UmlgOcl2Parser.INSTANCE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test() {
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
        db.commit();

		Object result = TumlOclExecutor.executeOclQuery("testoclmodel::org::umlg::qualifier::Bank", bank, "self.employee->select(name='employee3')");
		Assert.assertTrue(result instanceof TinkerOrderedSet<?>);
		TinkerOrderedSet result2 = (TinkerOrderedSet<Employee>)result;
		Assert.assertEquals(1, result2.size());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testAllInstances() {
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
        db.commit();

		Object result = TumlOclExecutor.executeOclQuery("testoclmodel::org::umlg::qualifier::Employee", "Employee.allInstances()");
		Assert.assertTrue(result instanceof TinkerSet<?>);
		TinkerSet result2 = (TinkerSet<Employee>)result;
		Assert.assertEquals(3, result2.size());
	}
	
	@Test
	public void testToJson() {
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
        db.commit();

		String json = TumlOclExecutor.executeOclQueryToJson("testoclmodel::org::umlg::qualifier::Bank", bank, "self.employee->select(name='employee3')");
		System.out.println(json);
	}


	@Test
	public void testTuples1() {
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
        db.commit();

		Object result = TumlOclExecutor.executeOclQuery("testoclmodel::org::umlg::qualifier::Bank", bank, "Tuple{name: String = name, employeeSize: Integer = employeeSize}");
		Assert.assertTrue(result instanceof Map);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, Object> resultAsMap = (Map)result;
		Assert.assertEquals(resultAsMap.get("name"), "thebank");
		Assert.assertEquals(resultAsMap.get("employeeSize"), 3);

	}

	@Test
	public void testTuples1ToJson() {
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
        db.commit();

		String json = TumlOclExecutor.executeOclQueryToJson("testoclmodel::org::umlg::qualifier::Bank", bank, "Tuple{name: String = name, employeeSize: Integer = employeeSize}");
		System.out.println(json);
	}

	@Test
	public void testTuples2() {
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
        db.commit();

		//The employee names should specify a Bag, bug in eclipse ocl
		Object result = TumlOclExecutor.executeOclQuery("testoclmodel::org::umlg::qualifier::Bank", bank, "Tuple{bank: Bank = self, employeeNames: Sequence(String) = employee.name}");
		Assert.assertTrue(result instanceof Map);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, Object> resultAsMap = (Map)result;
		Assert.assertTrue(resultAsMap.get("bank") instanceof Bank);
		Assert.assertTrue(resultAsMap.get("employeeNames") instanceof TinkerSequence);
		@SuppressWarnings({ "rawtypes", "unchecked" })
        TinkerSequence<String> employeeNameBag = (TinkerSequence)resultAsMap.get("employeeNames");
		Assert.assertEquals(3, employeeNameBag.size());
	}

	@Test
	public void testTuples2ToJSon() {
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
        db.commit();

		//The employee names should specify a Bag, bug in eclipse ocl
		String json = TumlOclExecutor.executeOclQueryToJson("testoclmodel::org::umlg::qualifier::Bank", bank, "Tuple{bank: Bank = self, employeeNames: Sequence(String) = employee.name}");
		System.out.println(json);
	}

}
