package org.umlg.quickpreview.enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.enumeration.JvmClass;
import org.umlg.enumeration.Language;
import org.umlg.enumeration.VisibilityKind;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/08
 * Time: 9:04 PM
 */
public class TestEnumeration extends BaseTest {

    @Test
    public void testEnumeration() {
        JvmClass javaClass = new JvmClass();
        javaClass.setLanguage(Language.JAVA);
        javaClass.setVisibility(VisibilityKind.PACKAGE);
        JvmClass scalaClass = new JvmClass();
        scalaClass.setLanguage(Language.SCALA);
        scalaClass.setVisibility(VisibilityKind.PUBLIC);
        db.commit();

        javaClass.reload();
        scalaClass.reload();
        Assert.assertEquals(VisibilityKind.PACKAGE, javaClass.getVisibility());
        Assert.assertEquals(Language.JAVA, javaClass.getLanguage());
        Assert.assertEquals(VisibilityKind.PUBLIC, scalaClass.getVisibility());
        Assert.assertEquals(Language.SCALA, scalaClass.getLanguage());
    }
}
