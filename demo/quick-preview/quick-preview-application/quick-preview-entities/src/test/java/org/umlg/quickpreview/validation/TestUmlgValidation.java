package org.umlg.quickpreview.validation;

import org.junit.Test;
import org.umlg.quickpreview.BaseTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;
import org.umlg.validation.Validation;

/**
 * Date: 2014/05/04
 * Time: 12:08 PM
 */
public class TestUmlgValidation extends BaseTest {

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUmlgMinLengthValidationFails() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum length must be at least 5
        validation.setMinLength("1234");
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUmlgMinIntegerValidationFails() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum value must be at least 5
        validation.setMinInteger(4);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUmlgMinUnlimitedNaturalValidationFails() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum value must be at least 5
        validation.setMinUnlimitedNatural(4);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUmlgMinRealValidationFails() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum value must be at least 5.123
        validation.setMinReal(5.122D);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUmlgMinLongValidationFails() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum value must be at least 5
        validation.setMinLong(4L);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUmlgMinFloatValidationFails() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum value must be at least 5.123
        validation.setMinFloat(5.122F);
    }

    @Test(expected = UmlgConstraintViolationException.class)
    public void testUmlgMinDoubleValidationFails() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum value must be at least 5.123
        validation.setMinDouble(5.122D);
    }

    @Test
    public void testUmlgValidationPasses() {
        Validation validation = new Validation();
        //The stereotype validation that the minimum length must be at least 5
        validation.setMinLength("12345");
        validation.setMinInteger(5);
        validation.setMinUnlimitedNatural(5);
        validation.setMinReal(5.123D);
        validation.setMinLong(5L);
        validation.setMinFloat(5.123F);
        validation.setMinDouble(5.123D);
    }

}
