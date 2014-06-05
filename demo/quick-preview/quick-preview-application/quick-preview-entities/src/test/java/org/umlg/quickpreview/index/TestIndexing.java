package org.umlg.quickpreview.index;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.indexing.Program;
import org.umlg.indexing.ProgramType;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/06/02
 * Time: 7:38 PM
 */
public class TestIndexing extends BaseTest {

    @Test(expected = IllegalStateException.class)
    public void testUniqueIndexing() {
        Program program1 = new Program();
        program1.setName("Program1");
        db.commit();

        //This will fail as name has a unique index specified on it.
        Program program2 = new Program();
        program2.setName("Program1");
        db.commit();
    }

    @Test
    public void testIndexing() {
        for (int i = 0; i < 1000; i++) {
            Program program1 = new Program();
            program1.setName("Program" + i);
        }
        db.commit();

        Assert.assertNotNull(Program.findByName("Program1"));
        Assert.assertNotNull(Program.findByName("Program500"));
        Assert.assertNotNull(Program.findByName("Program999"));
    }

    @Test
    public void testNonUniqueIndexing() {
        LocalDate now = LocalDate.now();
        Program program1 = new Program();
        program1.setStart(now);
        Program program2 = new Program();
        program2.setStart(now);
        Program program3 = new Program();
        program3.setStart(now);

        LocalDate tomorrow = now.plusDays(1);
        Program program4 = new Program();
        program4.setStart(tomorrow);
        Program program5 = new Program();
        program5.setStart(tomorrow);
        db.commit();

        Assert.assertEquals(3, Program.findByStart(now).size());
        Assert.assertEquals(2, Program.findByStart(tomorrow).size());
    }

    @Test
    public void testNonUniqueEnumIndex() {
        Program program1 = new Program();
        program1.setProgramType(ProgramType.NEWS);
        Program program2 = new Program();
        program2.setProgramType(ProgramType.NEWS);
        Program program3 = new Program();
        program3.setProgramType(ProgramType.NEWS);

        Program program4 = new Program();
        program4.setProgramType(ProgramType.SPORT);
        Program program5 = new Program();
        program5.setProgramType(ProgramType.SPORT);

        db.commit();
        Assert.assertEquals(3, Program.findByProgramType(ProgramType.NEWS).size());
        Assert.assertEquals(2, Program.findByProgramType(ProgramType.SPORT).size());
    }
}
