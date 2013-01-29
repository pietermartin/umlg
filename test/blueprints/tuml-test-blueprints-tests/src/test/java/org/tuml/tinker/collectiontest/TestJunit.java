package org.tuml.tinker.collectiontest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TumlGraphManager;

/**
 * Date: 2013/01/22
 * Time: 8:12 PM
 */
public class TestJunit {

    @Before
    public void before() {
    }

    @After
    public void after() {
        System.out.println("aaaaaaaaa");
    }

    @Test
    public void test() {
        try {
            if (true) {
                throw new IllegalStateException("asdasd");
            }
        } catch (Exception e) {
            System.out.println("aaaaaaaaaa");
        }
    }
}
