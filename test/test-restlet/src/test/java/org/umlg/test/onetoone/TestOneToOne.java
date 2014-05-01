package org.umlg.test.onetoone;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.test.standard.Alien;
import org.umlg.test.standard.SpaceCraft;
import org.umlg.test.standard.TerrestrialCraft;

/**
 * Date: 2013/05/19
 * Time: 9:50 AM
 */
public class TestOneToOne extends BaseLocalDbTest {

    @Test
    public void testOneToOne() {

        Alien alien = new Alien(true);
        alien.setName("alien1");
        TerrestrialCraft terrestrialCraft1 = new TerrestrialCraft(alien);
        terrestrialCraft1.setName("terrestrialCraft1");
        TerrestrialCraft terrestrialCraft2 = new TerrestrialCraft(alien);
        terrestrialCraft2.setName("terrestrialCraft2");
        SpaceCraft spaceCraft1 = new SpaceCraft(alien);
        spaceCraft1.setName("spaceCraft1");
        SpaceCraft spaceCraft2 = new SpaceCraft(alien);
        spaceCraft2.setName("spaceCraft2");

        db.commit();

        terrestrialCraft1.setSpaceCraft(spaceCraft1);

        db.commit();

        SpaceCraft spaceCraft1Test = new SpaceCraft(spaceCraft1.getVertex());
        Assert.assertNotNull(spaceCraft1Test.getTerrestrialCraft());

    }
}
