package org.umlg.tests.allinstances;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.concretetest.God;
import org.umlg.inheritencetest.AbstractSpecies;
import org.umlg.inheritencetest.Biped;
import org.umlg.inheritencetest.Mamal;
import org.umlg.inheritencetest.Quadped;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/05/07
 * Time: 6:52 PM
 */
public class TestAllInstancesOnAbstractClass extends BaseLocalDbTest {

    @Test
    public void testAbstractSpeciesAllInstances() {
        God g = new God();
        Mamal mamal1 = new Mamal(g);
        mamal1.setName("mamal1");
        Mamal mamal2 = new Mamal(g);
        mamal2.setName("mamal2");
        Quadped quadped1 = new Quadped(g);
        quadped1.setName("quadped1");
        Quadped quadped2 = new Quadped(g);
        quadped2.setName("quadped2");
        Biped biped1 = new Biped(g);
        biped1.setName("biped1");
        Biped biped2 = new Biped(g);
        biped2.setName("biped2");
        db.commit();

        Assert.assertEquals(6, AbstractSpecies.allInstances().size());
    }
}
