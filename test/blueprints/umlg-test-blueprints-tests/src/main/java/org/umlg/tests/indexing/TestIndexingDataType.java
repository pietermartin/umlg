package org.umlg.tests.indexing;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.indexing.PTYPE;
import org.umlg.indexing.Product;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/05/29
 * Time: 9:45 PM
 */
public class TestIndexingDataType extends BaseLocalDbTest {

    @Test
    public void testIndexDate() {
        Product product = new Product();
        product.setCreated(new LocalDate(10000));
        db.commit();
        Assert.assertEquals(1, Product.product_findByCreated(new LocalDate(10000)).size());
    }

    @Test
    public void testIndexDateTime() {
        Product product = new Product();
        product.setDeleted(new DateTime(10000123));
        db.commit();
        Assert.assertNotNull(Product.product_findByDeleted(new DateTime(10000123)));
        Assert.assertNull(Product.product_findByDeleted(new DateTime(10000124)));
    }

    @Test
    public void testIndexTime() {
        Product product = new Product();
        product.setTime(new LocalTime(10000123));
        db.commit();
        Assert.assertNotNull(Product.product_findByTime(new LocalTime(10000123)));
        Assert.assertNull(Product.product_findByTime(new LocalTime(10000124)));
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
