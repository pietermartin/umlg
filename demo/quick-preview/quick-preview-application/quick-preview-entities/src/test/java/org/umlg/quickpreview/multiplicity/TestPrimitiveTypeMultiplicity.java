package org.umlg.quickpreview.multiplicity;

import org.junit.Test;
import org.umlg.multiplicity.Account;
import org.umlg.multiplicity.Child;
import org.umlg.multiplicity.Customer;
import org.umlg.quickpreview.BaseTest;
import org.umlg.runtime.validation.UmlgConstraintViolationException;

/**
 * Date: 2014/05/05
 * Time: 10:32 PM
 */
public class TestPrimitiveTypeMultiplicity extends BaseTest {

    //This will fail as the customer needs to 5 top favourite numbers
    @Test(expected = UmlgConstraintViolationException.class)
    public void testPrimitiveMultiplicityFails() {
        Child child = new Child();
        child.setName("John");
        child.addToFavouriteNumbers(1);
        child.addToFavouriteNumbers(2);
        child.addToTop5FavouriteNumbers(1);
        child.addToTop5FavouriteNumbers(2);
        child.addToTop5FavouriteNumbers(3);
        child.addToTop5FavouriteNumbers(4);
        db.commit();
    }

    @Test
    public void testPrimitiveMultiplicity() {
        Child child = new Child();
        child.setName("John");
        child.addToFavouriteNumbers(1);
        child.addToFavouriteNumbers(2);
        child.addToTop5FavouriteNumbers(1);
        child.addToTop5FavouriteNumbers(2);
        child.addToTop5FavouriteNumbers(3);
        child.addToTop5FavouriteNumbers(4);
        child.addToTop5FavouriteNumbers(5);
        db.commit();
    }

}
