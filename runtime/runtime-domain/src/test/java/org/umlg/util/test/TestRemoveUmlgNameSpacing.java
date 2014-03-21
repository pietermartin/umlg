package org.umlg.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.runtime.util.UmlgUtil;

/**
 * Date: 2014/03/21
 * Time: 10:07 AM
 */
public class TestRemoveUmlgNameSpacing {

    @Test
    public void testRemoveUmlgNameSpacing() {
        String parsed = UmlgUtil.removeUmlgNameSpacing("g.V.org::umlg::this::that::Human");
        Assert.assertEquals("g.V.org_umlg_this_that_Human", parsed);

        parsed = UmlgUtil.removeUmlgNameSpacing("g.V.getProperty('org::umlg::this::that::Human')");
        Assert.assertEquals("g.V.getProperty('org::umlg::this::that::Human')", parsed);

        parsed = UmlgUtil.removeUmlgNameSpacing("g.V.getProperty('org::umlg::this::that::Human').out.V.org::umlg::this::that::Human");
        Assert.assertEquals("g.V.getProperty('org::umlg::this::that::Human').out.V.org_umlg_this_that_Human", parsed);

    }
}
