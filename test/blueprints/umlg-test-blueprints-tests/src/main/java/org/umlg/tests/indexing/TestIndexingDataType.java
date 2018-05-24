package org.umlg.tests.indexing;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.indexing.PTYPE;
import org.umlg.indexing.Product;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Date: 2014/05/29
 * Time: 9:45 PM
 */
public class TestIndexingDataType extends BaseLocalDbTest {

    @Test
    public void testIndexDate() {
        LocalDate localDate = LocalDate.now();
        Product product = new Product();
        product.setCreated(localDate);
        db.commit();
        Assert.assertEquals(1, Product.product_findByCreated(localDate).size());
    }

    @Test
    public void testIndexDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Product product = new Product();
        product.setDeleted(localDateTime);
        db.commit();
        Assert.assertNotNull(Product.product_findByDeleted(localDateTime));
        LocalDateTime localDateTime2 = LocalDateTime.now().plus(2, ChronoUnit.SECONDS);
        Assert.assertNull(Product.product_findByDeleted(localDateTime2));
    }

    @Test
    public void testIndexTime() {
        Product product = new Product();
        db.commit();
        LocalTime localTime = LocalTime.now();
        product.setTime(localTime);
        db.commit();
        Assert.assertNotNull(Product.product_findByTime(localTime));

        LocalTime localTime2 = LocalTime.now().plus(3, ChronoUnit.SECONDS);
        Assert.assertNull(Product.product_findByTime(localTime2));
    }

    @Test
    public void testIndexType() {
        Product product1 = new Product();
        product1.setType(PTYPE.REAL);
        Product product2 = new Product();
        product2.setType(PTYPE.REAL);
        Product product3 = new Product();
        product3.setType(PTYPE.REAL);
        Product product4 = new Product();
        product4.setType(PTYPE.REAL);
        Product product5 = new Product();
        product5.setType(PTYPE.REAL);
        db.commit();
        product5.reload();
        Assert.assertEquals(PTYPE.REAL, product5.getType());
        Assert.assertEquals(5, Product.product_findByType(PTYPE.REAL).size());
        Assert.assertEquals(0, Product.product_findByType(PTYPE.TOY).size());
    }

}
