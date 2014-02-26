package org.umlg.ocl.test;

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.umlg.ocl.UmlgOclExecutor;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.qualifier.Bank;
import org.umlg.qualifier.Employee;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.logging.LogManager;

public class TestRuntimeOclGeneration extends BaseLocalDbTest {

	@BeforeClass
	public static void beforeClass() {
		try {
			URL url = BaseLocalDbTest.class.getResource("/logging.properties");
			LogManager.getLogManager().readConfiguration(url.openStream());
            URL umlUrl = BaseLocalDbTest.class.getResource("/test-ocl.uml");
            UmlgOcl2Parser.INSTANCE.init(umlUrl.toURI());
            @SuppressWarnings("unused")
            UmlgOcl2Parser instance = UmlgOcl2Parser.INSTANCE;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
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

        UmlgOrderedSet<?> result = UmlgOclExecutor.executeOclQuery(bank, "self.employee->select(name='employee3')");
		Assert.assertTrue(result instanceof UmlgOrderedSet<?>);
		Assert.assertEquals(1, result.size());
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

		Object result = UmlgOclExecutor.executeOclQuery("testoclmodel::org::umlg::qualifier::Employee", "Employee.allInstances()");
		Assert.assertTrue(result instanceof UmlgSet<?>);
		UmlgSet result2 = (UmlgSet<Employee>)result;
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

		String json = UmlgOclExecutor.executeOclQueryToJson(bank, "self.employee->select(name='employee3')");
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

		Object result = UmlgOclExecutor.executeOclQuery(bank, "Tuple{name: String = name, employeeSize: Integer = employeeSize}");
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

		String json = UmlgOclExecutor.executeOclQueryToJson(bank, "Tuple{name: String = name, employeeSize: Integer = employeeSize}");
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
		Object result = UmlgOclExecutor.executeOclQuery(bank, "Tuple{bank: Bank = self, employeeNames: Sequence(String) = employee.name}");
		Assert.assertTrue(result instanceof Map);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, Object> resultAsMap = (Map)result;
		Assert.assertTrue(resultAsMap.get("bank") instanceof Bank);
		Assert.assertTrue(resultAsMap.get("employeeNames") instanceof UmlgSequence);
		@SuppressWarnings({ "rawtypes", "unchecked" })
        UmlgSequence<String> employeeNameBag = (UmlgSequence)resultAsMap.get("employeeNames");
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
		String json = UmlgOclExecutor.executeOclQueryToJson(bank, "Tuple{bank: Bank = self, employeeNames: Sequence(String) = employee.name}");
		System.out.println(json);
	}

}
