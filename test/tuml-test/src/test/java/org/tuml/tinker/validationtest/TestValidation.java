package org.nakeuml.tinker.validationtest;

import org.junit.Test;
import org.neo4j.graphdb.TransactionFailureException;
import org.tuml.componenttest.Space;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.Time;
import org.tuml.componenttest.ValidationTest;
import org.tuml.concretetest.God;
import org.tuml.concretetest.Universe;
import org.tuml.runtime.test.BaseLocalDbTest;
import org.tuml.runtime.validation.TumlConstraintViolationException;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;

public class TestValidation extends BaseLocalDbTest {

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMaxLengthFail() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMaxLength("12345678911");
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
			db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testValidationMaxLengthPass() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		u1.setMaxLength("1234567891");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMinLengthFail() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setTestMinLength("123");
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testValidationMinLengthPass() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setTestMinLength("12345");
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeLengthFail1() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRangeLength("123");
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeLengthFail2() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRangeLength("12345678911");
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testValidationRangeLengthPass() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRangeLength("123456789");
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMinFail() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMin(5);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testValidationMinPass() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMin(51);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationMaxFail() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMax(101);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testValidationMaxPass() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setMax(51);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeFail1() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRange(1);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testValidationRangeFail2() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRange(5);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testValidationRangePass() {
		God g = new God(true);
		Universe u1 = new Universe(g);
		u1.setRange(3);
		SpaceTime st = new SpaceTime(u1);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test(expected = TransactionFailureException.class)
	public void testValidationRequiredFail() {
		God g = new God(true);
		ValidationTest validationTest = new ValidationTest(g);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@Test
	public void testValidationRequiredPass() {
		God g = new God(true);
		ValidationTest validationTest = new ValidationTest(g);
		validationTest.setRequiredName("asd");
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testFail() {
		try {
			God g = new God(true);
			ValidationTest validationTest = new ValidationTest(g);
			// validationTest.setRequiredName("asd");
			db.stopTransaction(Conclusion.SUCCESS);
		} catch (TransactionFailureException e) {
			System.out.println(e.getCause());
		}
	}

	@SuppressWarnings("unused")
	@Test(expected = TumlConstraintViolationException.class)
	public void testEmailValidationFail() {
		God g = new God(true);
		g.setName("asda");
		Universe universe = new Universe(g);
		universe.setEmail("asdasd");
		SpaceTime st = new SpaceTime(universe);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}

	@SuppressWarnings("unused")
	@Test
	public void testEmailValidationPass() {
		God g = new God(true);
		g.setName("asda");
		Universe universe = new Universe(g);
		universe.setEmail("asdasd@asd.com");
		SpaceTime st = new SpaceTime(universe);
		Space s = new Space(st);
		Time t = new Time(st);
		db.stopTransaction(Conclusion.SUCCESS);
	}
}
