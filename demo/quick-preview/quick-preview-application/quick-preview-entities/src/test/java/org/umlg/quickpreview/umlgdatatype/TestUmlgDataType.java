package org.umlg.quickpreview.umlgdatatype;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.quickpreview.BaseTest;
import org.umlg.runtime.util.UmlgFormatter;
import org.umlg.umlgdatatype.UmlgDataType;
import org.umlg.umlprimitivetype.UmlPrimitiveType;

/**
 * Date: 2014/05/04
 * Time: 12:08 PM
 */
public class TestUmlgDataType extends BaseTest {

    @Test
    public void testUmlgDataType() {
        UmlgDataType umlgDataType = new UmlgDataType();
        umlgDataType.setEmail("john.smith@hero.com");
        //yy-mm-dd HH:mm:ss
        umlgDataType.setDatetime(new DateTime(UmlgFormatter.parseDateTime("2014-05-04 12:12:12")));
        umlgDataType.setDate(new LocalDate(UmlgFormatter.parseDate("2014-05-04")));
        umlgDataType.setTime(new LocalTime(UmlgFormatter.parseTime("12:12")));
        db.commit();
        //Ensure we are getting the values from the db, clearing any cached values in the object
        umlgDataType.reload();
        Assert.assertEquals("john.smith@hero.com", umlgDataType.getEmail());
        Assert.assertEquals("2014-05-04 12:12:12", UmlgFormatter.format(umlgDataType.getDatetime()));
        Assert.assertEquals("2014-05-04", UmlgFormatter.format(umlgDataType.getDate()));
        Assert.assertEquals("12:12", UmlgFormatter.format(umlgDataType.getTime()));
    }
}
