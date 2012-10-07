package org.nakeuml.tinker.validationtest;

import org.junit.Test;
import org.neo4j.graphdb.TransactionFailureException;
import org.tuml.componenttest.ValidationTest;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.runtime.validation.TumlConstraintViolationException;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestValidation extends BaseLocalDbTest {

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMaxLengthFail() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMaxLength("12345678911");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationMaxLengthPass() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMaxLength("1234567891");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMinLengthFail() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setTestMinLength("123");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationMinLengthPass() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setTestMinLength("12345");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeLengthFail1() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRangeLength("123");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeLengthFail2() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRangeLength("12345678911");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationRangeLengthPass() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRangeLength("123456789");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMinFail() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMin(5);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationMinPass() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMin(51);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMaxFail() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMax(101);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationMaxPass() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMax(51);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeFail1() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRange(1);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeFail2() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRange(5);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationRangePass() {
		db.startTransaction();
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRange(3);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test(expected = TransactionFailureException.class)
	public void testValidationRequiredFail() {
		db.startTransaction();
		God g = new God(true);
		@SuppressWarnings("unused")
		ValidationTest validationTest = new ValidationTest(g);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationRequiredPass() {
		db.startTransaction();
		God g = new God(true);
		ValidationTest validationTest = new ValidationTest(g);
		validationTest.setRequiredName("asd");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testFail() {
		db.startTransaction();
		try {
			God g = new God(true);
			ValidationTest validationTest = new ValidationTest(g);
			// validationTest.setRequiredName("asd");
			db.stopTransaction(Conclusion.SUCCESS);
		} catch (TransactionFailureException e) {
			System.out.println(e.getCause());
		}
	}

}
